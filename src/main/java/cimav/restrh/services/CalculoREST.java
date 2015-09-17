/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Concepto;
import cimav.restrh.entities.EGrupo;
import cimav.restrh.entities.EmpleadoNomina;
import cimav.restrh.entities.NominaQuincenal;
import cimav.restrh.entities.Tabulador;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Years;


/**
 *
 * @author juan.calderon
 */
@Stateless
@Path("calculo")
public class CalculoREST {

    // http://stackoverflow.com/questions/1359817/using-bigdecimal-to-work-with-currencies
    public final int BIG_SCALE = 5;

    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public CalculoREST() {
    }

    public EntityManager getEntityManager() {
        return em;
    }

    /* Code de Conceptos en la DB */
    private final String SUELDO_ORDINARIO           = "00001";
    private final String SUELDO_DIAS_DESCANSO       = "00003";
    private final String PRIMA_ANTIGUEDAD           = "00005";
    private final String CARGA_ADMINISTRATIVA       = "00007";
    private final String COMPENSA_GARANTIZADA       = "00010";
    private final String HONORARIOS_ASIMILABLES     = "00027"; // sueldo
    private final String MATERIALES                 = "00012";
    private final String FONDO_AHORRO_EXENTO        = "00021";
    private final String FONDO_AHORRO_GRAVADO       = "00022";
    private final String FONDO_AHORRO               = "00111";
    private final String APORTACION_FONDO_AHORRO    = "00112";
    
    private final String BASE_GRAVABLE              = "BG";
    private final String BASE_EXENTA                = "BE";
    
    
    private final String PORCEN_FONDO_AHORRO_CYT    = "0.02";
    private final String PORCEN_FONDO_AHORRO_AYA    = "0.018";
    private final String PORCEN_MATERIALES          = "0.06";
    private final String PORCEN_FONDO_AHORRO        = "0.13";
    private final String PORCEN_FONDO_AHORRO_EXENTO = "1.3";
    private final String SALARIO_ZONA_B             = "68.28";
    
    private final Integer DIAS_MES          = 30;
    private final Integer DIAS_QUINCENA     = 15;
//    private final Integer DIAS_ORDINARIOS   = 11;
//    private final Integer DIAS_DESCANSO     = 4;

    private int idEmpleado;
    
    private String  calculoJSON;

    @POST
    @Consumes(value = "application/json")
    @Produces(value = "application/json")
    @Asynchronous
    public void calcularTodos(@Suspended final AsyncResponse asyncResponse, final JsonArray ids) {
        asyncResponse.resume(doCalculo(ids));
    }

    private String doCalculo(JsonArray ids) {
        String resultJson = "[";
        
        for (JsonValue idVal : ids) {
            int id = ((JsonObject)idVal).getInt("id");
            resultJson = resultJson + this.calcular(id) + ",";
        }
        resultJson = (resultJson + "]").replace(",]", "]");
        
        return resultJson;
    }
    
    @GET
    @Path("{idEmpleado}")
    @Produces("application/json")
    public String calcularUno(@PathParam("idEmpleado") int idEmpleado) {
        String resultJson = this.calcular(idEmpleado);
        return resultJson;
    }

    private String calcular(int idEmpleado) {
        
        this.idEmpleado = idEmpleado;

        BigDecimal sueldo_base_mes;
        BigDecimal sueldo_base_dia;
        BigDecimal sueldo_ordinario = BigDecimal.ZERO;
        BigDecimal sueldo_dias_descanso = BigDecimal.ZERO;
        BigDecimal sueldo_quincenal = BigDecimal.ZERO;
        BigDecimal sueldo_honorarios = BigDecimal.ZERO;
        BigDecimal prima_antiguedad = BigDecimal.ZERO;
        BigDecimal materiales = BigDecimal.ZERO;
        BigDecimal compensa_garantiza = BigDecimal.ZERO;
        BigDecimal carga_admin = BigDecimal.ZERO;
        BigDecimal fondo_ahorro_exento = BigDecimal.ZERO;
        BigDecimal fondo_ahorro_gravado = BigDecimal.ZERO;
        BigDecimal fondo_ahorro = BigDecimal.ZERO;
        
        BigDecimal dias_trabajados;
        BigDecimal dias_ordinarios_trabajados;
        BigDecimal dias_descanso;
        
        BigDecimal base_gravable= BigDecimal.ZERO;
        BigDecimal base_exenta = BigDecimal.ZERO;
        
        try {

            EmpleadoNomina empleadoNomina = getEntityManager().find(EmpleadoNomina.class, this.idEmpleado);
            if (empleadoNomina == null) {
                throw new NullPointerException("EMPLEADO");
            }

            // TODO Faltas e Incapacidades de la DB
            Integer faltas = 0; 
            Integer incapacidades = 0; 
            dias_trabajados = new BigDecimal(Quincena.get().getDiasCalculo() - faltas - incapacidades);
            dias_ordinarios_trabajados = new BigDecimal(Quincena.get().getDiasOrdinarios() - faltas - incapacidades);
            dias_descanso = new BigDecimal(Quincena.get().getDiasDescanso());
            // TODO faltan dias de asueto
            
            Tabulador nivel = empleadoNomina.getNivel();
            if (nivel == null) {
                throw new NullPointerException("NIVEL");
            }

            boolean isCYT = Objects.equals(EGrupo.CYT.getId(), empleadoNomina.getIdGrupo());
            boolean isAYA = Objects.equals(EGrupo.AYA.getId(), empleadoNomina.getIdGrupo());
            boolean isMMS = Objects.equals(EGrupo.MMS.getId(), empleadoNomina.getIdGrupo());
            boolean isHON = Objects.equals(EGrupo.HON.getId(), empleadoNomina.getIdGrupo());
            
            if (isHON) {
                // HON
                sueldo_base_mes = nivel.getHonorarios();
                if (sueldo_base_mes == null) {
                    throw new NullPointerException("SUELDO_BASE_MES-HONORARIOS");
                }

                sueldo_base_dia = sueldo_base_mes.divide(new BigDecimal(DIAS_MES), BIG_SCALE, RoundingMode.HALF_DOWN);

                sueldo_honorarios = sueldo_base_dia.multiply(dias_trabajados, MathContext.UNLIMITED);
            } else {
                // CYT, MMS, AYA
                sueldo_base_mes = nivel.getSueldo();
                if (sueldo_base_mes == null) {
                    throw new NullPointerException("SUELDO_BASE_MES");
                }

                sueldo_base_dia = sueldo_base_mes.divide(new BigDecimal(DIAS_MES), BIG_SCALE, RoundingMode.HALF_DOWN);

                sueldo_ordinario = sueldo_base_dia.multiply(dias_ordinarios_trabajados, MathContext.UNLIMITED);
                // TODO Dias descanso y Dias ordinarios se les quita Faltas ¿?
                sueldo_dias_descanso = sueldo_base_dia.multiply(dias_descanso, MathContext.UNLIMITED);
                
                sueldo_quincenal = sueldo_ordinario.add(sueldo_dias_descanso);
            }

            // TODO dias trabajados
            Date finDeQuincena = new Date(); // o donde cumple los años
            // y separa en dos si tiene diferencia
            if (isCYT) {

                //TODO BUG Muy Lento y problema con JodaTime Resource not found: "org/joda/time/tz/data/ZoneInfoMap"
                int years = Years.yearsBetween(new DateTime(empleadoNomina.getFechaAntiguedad()), new DateTime(finDeQuincena)).getYears();
                if (years >= 5) {
                    prima_antiguedad = sueldo_quincenal.multiply(new BigDecimal(PORCEN_FONDO_AHORRO_CYT));
                    prima_antiguedad = prima_antiguedad.multiply(new BigDecimal(years));
                }
                
                materiales = sueldo_quincenal.multiply(new BigDecimal(PORCEN_MATERIALES));
                
            } else if (isAYA) {
                //TODO usar la fecha de Fin De Quincena
                int years = Years.yearsBetween(new DateTime(empleadoNomina.getFechaAntiguedad()), new DateTime(finDeQuincena)).getYears();
                if (years >= 5) {
                    prima_antiguedad = sueldo_quincenal.multiply(new BigDecimal(PORCEN_FONDO_AHORRO_AYA));
                    prima_antiguedad = prima_antiguedad.multiply(new BigDecimal(years));
                }
            }

            if (isCYT || isAYA) {
                fondo_ahorro = sueldo_quincenal.multiply(new BigDecimal(PORCEN_FONDO_AHORRO));
                
                fondo_ahorro_exento = new BigDecimal(SALARIO_ZONA_B);
                fondo_ahorro_exento = fondo_ahorro_exento.multiply(new BigDecimal(PORCEN_FONDO_AHORRO_EXENTO)).multiply(new BigDecimal(DIAS_QUINCENA));
                
                if (fondo_ahorro_exento.compareTo(fondo_ahorro) > 0) {
                    // exenta todo
                    fondo_ahorro_exento = fondo_ahorro;
                } else if (fondo_ahorro_exento.compareTo(fondo_ahorro) < 0){
                    // la parte gravable
                    fondo_ahorro_gravado = fondo_ahorro.subtract(fondo_ahorro_exento);
                }
            }
            
            if(nivel.getCompGarantizada() != null && nivel.getCompGarantizada().compareTo(BigDecimal.ZERO) > 0) {
                // TODO ¿Compensacion Dias trabajados?
                compensa_garantiza = nivel.getCompGarantizada().divide(new BigDecimal(DIAS_MES), BIG_SCALE, RoundingMode.HALF_DOWN);
                compensa_garantiza = compensa_garantiza.multiply(dias_trabajados);
            }
            
            if(nivel.getCargaAdmin() != null && nivel.getCargaAdmin().compareTo(BigDecimal.ZERO) > 0) {
                // TODO ¿Carga dias trabajado?
                carga_admin = nivel.getCargaAdmin().divide(new BigDecimal(DIAS_MES), BIG_SCALE, RoundingMode.HALF_DOWN);
                carga_admin = carga_admin.multiply(dias_trabajados);
            }
            
        } catch (NullPointerException e) {
            return "-1";
        }
        
        base_gravable = base_gravable.add(sueldo_ordinario);
        base_gravable = base_gravable.add(sueldo_dias_descanso);
        base_gravable = base_gravable.add(sueldo_honorarios);
        base_gravable = base_gravable.add(prima_antiguedad);
        base_gravable = base_gravable.add(materiales);
        base_gravable = base_gravable.add(carga_admin);
        base_gravable = base_gravable.add(compensa_garantiza);
        base_gravable = base_gravable.add(fondo_ahorro_gravado);
        
        base_exenta = base_exenta.add(fondo_ahorro_exento);

        try {
            
            this.calculoJSON = "";
            
            // Eliminar los Calculos Previos para evitar conceptos rezagados
            // no incluidos en el este proceso
            this.vaciarCalculos(idEmpleado);
            
            // sueldo ordinario
            insertarMov(SUELDO_ORDINARIO, sueldo_ordinario);
            // sueldo dias descanso
            insertarMov(SUELDO_DIAS_DESCANSO, sueldo_dias_descanso);
            // sueldo dias descanso
            insertarMov(HONORARIOS_ASIMILABLES, sueldo_honorarios);
            // prima antiguedad
            insertarMov(PRIMA_ANTIGUEDAD, prima_antiguedad);
            // materiales
            insertarMov(MATERIALES, materiales);
            // cargada admin
            insertarMov(CARGA_ADMINISTRATIVA, carga_admin);
            // compesa garantizada
            insertarMov(COMPENSA_GARANTIZADA, compensa_garantiza);
            // fondo ahorro exento
            insertarMov(FONDO_AHORRO_EXENTO, fondo_ahorro_exento);
            // fondo ahorro gravado
            insertarMov(FONDO_AHORRO_GRAVADO, fondo_ahorro_gravado);

            // fondo ahorro 
            insertarMov(FONDO_AHORRO, fondo_ahorro);
            // aportación fondo ahorro 
            insertarMov(APORTACION_FONDO_AHORRO, fondo_ahorro);
            
            // base gravada
            insertarMov(BASE_GRAVABLE, base_gravable);
            // base exenta
            insertarMov(BASE_EXENTA, base_exenta);

        } catch (NullPointerException | RollbackException ex) {
            return "-2";
        }

        this.calculoJSON = ("{" + "\"id\":" + this.idEmpleado +"," + this.calculoJSON + "}").replace(",}", "}");
        
        return this.calculoJSON;
    }
    
    private void insertarMov(String strConcepto, BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) == 0) {
            // insertar solo los mayores a cero
            return;
        }
        // obtener concepto
        Concepto concepto = finConceptoByCode(strConcepto);
        if (concepto == null) {
            throw new NullPointerException(strConcepto);
        }
        // movimiento
        NominaQuincenal nomQuin = this.findNominaQuincenal(this.idEmpleado, concepto);
        if (nomQuin == null) {
            // es creaciÃ³n
            nomQuin = new NominaQuincenal(this.idEmpleado, concepto, monto);
        } else {
            // es update
            nomQuin.setCantidad(monto);
        }
        // persistirlo
        getEntityManager().persist(nomQuin);
        
        this.calculoJSON = this.calculoJSON + "\"" + strConcepto + "\": " + monto + ",";
    }

    public Concepto finConceptoByCode(String code) {
        try {
            return (Concepto) getEntityManager().createNamedQuery("Concepto.findByCode").setParameter("code", code).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public NominaQuincenal findNominaQuincenal(Integer idEmpleado, Concepto concepto) {
        try {
            return (NominaQuincenal) getEntityManager().createNamedQuery("NominaQuincenal.findBy_IdEmpleado_Concepto")
                    .setParameter("id_empleado", idEmpleado).setParameter("concepto", concepto).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public void vaciarCalculos(int idEmpleado) {
        // Elimina todos los calculos del empleado de NominaQuincenal
        // Solo calculos; no movimientos (saldos y pagos).
        try {
            String query = "DELETE FROM NominaQuincenal n WHERE n.idEmpleado = :id_empleado AND n.concepto.idTipoMovimiento = :id_tipo_mov";
            getEntityManager().createQuery(query).setParameter("id_empleado", idEmpleado).setParameter("id_tipo_mov", 'C').executeUpdate();
        } catch (Exception e) {
            System.out.println("vaciarCalculos ::> " + e.getMessage());
        }
    }

    @GET
    @Path("fechas/{quincena}")
    @Produces("application/json")
    public String calcularDias(@PathParam("quincena") int quincena) {
        String result = prnFechas(quincena);
        return result;
    }
    
    @GET
    @Path("fechas")
    @Produces("application/json")
    public String fechas() {
        String result = "";
        for(int i=1; i<=24; i++) {
            result = result + prnFechas(i) +",\n";
        }
        return result;
    }
    
    private String prnFechas(int quincena) {
        
        // TODO Fechas, falta días de asueto
        
        String result;
        boolean isPar = (quincena & 1) == 0;
        int year = 2015;
        int mes = (int)Math.ceil(quincena/2.0);
        int diaInicio = isPar ? 16 : 1;
        
        Calendar fechaInicio = Calendar.getInstance();
        fechaInicio.set(year, mes-1, diaInicio);
        
        // si es quincena non, dia real y topado coinciden en 15
        int diaFinalReal = 15;
        int diaFinalFiscal = 15;
        if (isPar) {
            // último día real del mes
            diaFinalReal = fechaInicio.getActualMaximum(Calendar.DAY_OF_MONTH);
            // maximo llegan al día treinta
            diaFinalFiscal = diaFinalReal > 30 ? 30 : diaFinalReal; 
        }
        
        Calendar fechaFinReal = Calendar.getInstance();
        fechaFinReal.set(year, mes-1, diaFinalReal);
        
        Calendar fechaFinFiscal = Calendar.getInstance();
        fechaFinFiscal.set(year, mes-1, diaFinalFiscal);
        
        Calendar fechaAvanza = Calendar.getInstance();
        fechaAvanza.set(year, mes-1, diaInicio); //igual a la de inicio
            
        int diasOrdinarios = 0;
        int diasDescanso = 0;
        while (fechaFinFiscal.after(fechaAvanza) || fechaFinFiscal.equals(fechaAvanza)) {
            if (fechaAvanza.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || fechaAvanza.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                diasDescanso++;
            } else {
                diasOrdinarios++;
            }
            fechaAvanza.add(Calendar.DATE, 1);
        }
        
        DateTime start = new DateTime(year, mes, fechaInicio.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC).minusDays(1);
        DateTime end = new DateTime(year, mes, fechaFinReal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC);
        
        long diasImss = Days.daysBetween(start, end).getDays();
        
        //Duration duration = new Duration(start, end);
        //long diasImss = duration.getStandardDays();
        
        
        String fi = "\"" + fechaInicio.get(Calendar.DATE) + "/" +(fechaInicio.get(Calendar.MONTH)+1) +"/"+fechaInicio.get(Calendar.YEAR) + "\"";
        String ffr = "\"" + fechaFinReal.get(Calendar.DATE) + "/" +(fechaFinReal.get(Calendar.MONTH)+1) +"/"+fechaFinReal.get(Calendar.YEAR) + "\"";
        String fff = "\"" + fechaFinFiscal.get(Calendar.DATE) + "/" +(fechaFinFiscal.get(Calendar.MONTH)+1) +"/"+fechaFinFiscal.get(Calendar.YEAR) + "\"";
        
        //result = String.format("%02d%15s%15s%15s%5s%5s%5s", quincena, fi, ffr, fff, diasOrdinarios, diasDescanso, diasOrdinarios+diasDescanso);
        
        result = 
                "{ " + "\"quincena\": " + quincena + "," +
                "\t" + "\"fecha_inicio\": " + fi + "," +
                "\t" + "\"fecha_fin_calendario\": " + ffr + "," +
                "\t" + "\"fecha_fin_calculo\": " + fff + "," +
                "\t" + "\"dias_ordinarios\": " + diasOrdinarios + "," +
                "\t" + "\"dias_descando\": " + diasDescanso + "," +
                "\t" + "\"dias_calculo\": " + (diasOrdinarios + diasDescanso) + "," +
                "\t" + "\"dias_imss\": " + diasImss + "" +
                "}";
                
        
        return result;
    }

}
