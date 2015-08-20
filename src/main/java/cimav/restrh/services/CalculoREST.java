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
import java.util.Objects;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.joda.time.DateTime;
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
    
    
    private final String PORCEN_FONDO_AHORRO_CYT    = "0.02";
    private final String PORCEN_FONDO_AHORRO_AYA    = "0.018";
    private final String PORCEN_MATERIALES          = "0.06";
    private final String PORCEN_FONDO_AHORRO        = "0.13";
    private final String PORCEN_FONDO_AHORRO_EXENTO = "1.3";
    private final String SALARIO_ZONA_B             = "68.28";
    
    private final Integer DIAS_MES = 30;
    private final Integer DIAS_QUINCENA = 15;
    private final Integer DIAS_ORDINARIOS = 11;
    private final Integer DIAS_DESCANSO = 4;
    
    private int idEmpleado;

    @GET
    @Path("{idEmpleado}")
    @Produces("application/json")
    public String calcular(@PathParam("idEmpleado") int idEmpleado) {

        // TODO conviene vaciar el NominaQuincenal para evitar conceptos rezagados
        
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

        try {

            EmpleadoNomina empleadoNomina = getEntityManager().find(EmpleadoNomina.class, this.idEmpleado);
            if (empleadoNomina == null) {
                throw new NullPointerException("EMPLEADO");
            }

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

                sueldo_honorarios = sueldo_base_dia.multiply(new BigDecimal(DIAS_QUINCENA), MathContext.UNLIMITED);
            } else {
                // CYT, MMS, AYA
                sueldo_base_mes = nivel.getSueldo();
                if (sueldo_base_mes == null) {
                    throw new NullPointerException("SUELDO_BASE_MES");
                }

                sueldo_base_dia = sueldo_base_mes.divide(new BigDecimal(DIAS_MES), BIG_SCALE, RoundingMode.HALF_DOWN);

                sueldo_ordinario = sueldo_base_dia.multiply(new BigDecimal(DIAS_ORDINARIOS), MathContext.UNLIMITED);
                sueldo_dias_descanso = sueldo_base_dia.multiply(new BigDecimal(DIAS_DESCANSO), MathContext.UNLIMITED);
                
                sueldo_quincenal = sueldo_ordinario.add(sueldo_dias_descanso);
            }

            if (isCYT) {
                //TODO usar la fecha de Fin De Quincena
                int anios = Years.yearsBetween(new DateTime(empleadoNomina.getFechaAntiguedad()), new DateTime()).getYears();
                if (anios >= 5) {
                    prima_antiguedad = sueldo_quincenal.multiply(new BigDecimal(PORCEN_FONDO_AHORRO_CYT));
                    prima_antiguedad = prima_antiguedad.multiply(new BigDecimal(anios));
                }
                
                materiales = sueldo_quincenal.multiply(new BigDecimal(PORCEN_MATERIALES));
                
            } else if (isAYA) {
                //TODO usar la fecha de Fin De Quincena
                int anios = Years.yearsBetween(new DateTime(empleadoNomina.getFechaAntiguedad()), new DateTime()).getYears();
                if (anios >= 5) {
                    prima_antiguedad = sueldo_quincenal.multiply(new BigDecimal(PORCEN_FONDO_AHORRO_AYA));
                    prima_antiguedad = prima_antiguedad.multiply(new BigDecimal(anios));
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
                // TODO ¿Dias trabajados?
                compensa_garantiza = nivel.getCompGarantizada().divide(new BigDecimal(2), BIG_SCALE, RoundingMode.HALF_DOWN);
            }
            
            if(nivel.getCargaAdmin() != null && nivel.getCargaAdmin().compareTo(BigDecimal.ZERO) > 0) {
                // TODO ¿dias trabajado?
                carga_admin = nivel.getCargaAdmin().divide(new BigDecimal(2), BIG_SCALE, RoundingMode.HALF_DOWN);
            }
            
        } catch (NullPointerException e) {
            return "-1";
        }

        try {
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

        } catch (NullPointerException | RollbackException ex) {
            return "-2";
        }

        return "0";
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

}
