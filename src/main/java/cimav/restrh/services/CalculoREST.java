/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Concepto;
import cimav.restrh.entities.EGrupo;
import cimav.restrh.entities.EmpleadoNomina;
import cimav.restrh.entities.Nomina;
import cimav.restrh.entities.Movimiento;
import cimav.restrh.entities.Tabulador;
import cimav.restrh.entities.TarifaAnual;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.function.MonetaryUtil;


/**
 *
 * @author juan.calderon
 */
@Stateless
@Path("calculo")
public class CalculoREST {

    private final static Logger logger = Logger.getLogger(CalculoREST.class.getName() ); 
    
    // http://stackoverflow.com/questions/1359817/using-bigdecimal-to-work-with-currencies
    public static final int BIG_SCALE = 5;
    public static final MathContext mc = new MathContext(BIG_SCALE, RoundingMode.HALF_UP);    
    
    public static final String MXN = "MXN";
    
    @PersistenceContext(unitName = "PU_JPA")
    EntityManager em;

    @EJB
    private NominaREST nominaREST;
    @EJB
    private MovimientoFacadeREST movimientosREST;
    
    public CalculoREST() {
    }

    public EntityManager getEntityManager() {
        return em;
    }

    /* Code de Conceptos en la DB */
    private final String SUELDO_ORDINARIO               = "00001";
    private final String SUELDO_DIAS_DESCANSO           = "00003";
    private final String PRIMA_ANTIGUEDAD               = "00005";
    private final String CARGA_ADMINISTRATIVA           = "00007";
    //private final String ESTIMULOS_PRODUCTIVIDAD        = "00009";
    private final String COMPENSA_GARANTIZADA           = "00010";
    //private final String VALES_DESPENSA             = "00014";
    private final String HONORARIOS_ASIMILABLES         = "00027"; // sueldo
    private final String MATERIALES                     = "00012";
    private final String ESTIMULOS_PRODUCTIVIDAD_AYA    = "00018";
    private final String ESTIMULOS_PRODUCTIVIDAD_CYT    = "00019";
    private final String FONDO_AHORRO_EXENTO            = "00021";
    private final String FONDO_AHORRO_GRAVADO           = "00022";
    private final String APOYO_MANTENIMIENTO_VEHICULAR  = "00035";
    private final String PRIMA_QUINQUENAL               = "00067";
    private final String HORAS_EXTRAS_EXENTO            = "00056";
    private final String HORAS_EXTRAS_GRAVADO           = "00058";
    private final String FONDO_AHORRO                   = "00111";
    private final String APORTACION_FONDO_AHORRO        = "00112";
    private final String PENSION_ALIMENTICIA            = "00118";
    private final String PENSION_ALIMENTICIA_MON_DESP   = "00181";
    private final String MONEDERO_DESPENSA              = "00092";
    private final String IMSS                           = "00106";
    private final String SEG_SEPARACION_IND             = "00093";
    private final String SEG_SEPARACION_IND_CIMAV       = "00114";
    private final String SEG_SEPARACION_IND_EMPLEADO   = "00115";
    private final String SEG_SEPARACION_IND_ISR         = "00182";
    
    // repercuciones
    private final String EXCEDENTE_3SMG                 = "E3SMG";
    private final String PRESTACIONES_EN_DINERO         = "PED";
    private final String GTOS_MEDICOS_Y_PENSION         = "GMYP";
    private final String INVALIDEZ_Y_VIDA               = "IYV";
    private final String CESANTIA_Y_VEJEZ               = "CYV";
    private final String CUOTA_FIJA                     = "CFIJA";
    private final String RIESGO_DE_TRABJO               = "RIESGOT";
    private final String GUARDERIAS_Y_PRESTA_SOCIALES   = "GYPS";
    private final String SEGURO_RETIRO                  = "SEGRET";
    private final String INFONAVIT                      = "INFONA";

    // internos
    private final String BASE_GRAVABLE              = "BG";
    private final String BASE_EXENTA                = "BE";    
    private final String ISR                        = "00101";
    private final String SUELDO_DIARIO              = "SUD";
    private final String SALARIO_DIARIO_FIJO            = "SDF";
    private final String SALARIO_DIARIO_VARIABLE        = "SDV";
    private final String SALARIO_DIARIO_COTIZADO        = "SDC";
    private final String SALARIO_DIARIO_COTIZADO_TOPADO = "SDCT";
    
    private final Double FACTOR_PANT_AYA_5_20       = 0.018;
    private final Double FACTOR_PANT_AYA_21         = 0.023;
    private final Double FACTOR_PANT_CYT_5_20       = 0.020;  // 2% de los 11 a los 20 aÃ±os
    private final Double FACTOR_PANT_CYT_21         = 0.025;  // es 2.1" pero aplica cuando el Cimav Cumpla 21 aÃ±os
    private final String PORCEN_FONDO_AHORRO_CYT    = "0.02";
    private final String PORCEN_FONDO_AHORRO_AYA    = "0.018";
    private final String PORCEN_MATERIALES          = "0.06";
    private final Double PORCEN_FONDO_AHORRO        = 0.13;
    private final Double PORCEN_FONDO_AHORRO_EXENTO = 1.3;
    private final Double SALARIO_MINIMO             = 73.04; //= 70.10; //TODO SM
    private final Integer SALARIO_DIARIO_TOPE        = 25; // 25 veces el Salario MÃ­nimo
    
    private final Integer DIAS_MES_30           = 30;
    private final Integer DIAS_QUINCENA_15      = 15;
    private final Integer DIAS_AJUSTE_5_6       = 6;

    private int idEmpleado;
    
    private List<TarifaAnual> listTarifaAnual;
    
    @POST
    @Consumes(value = "application/json")
    @Produces(value = "application/json")
    @Asynchronous
    public void calcularTodos(@Suspended final AsyncResponse asyncResponse, final JsonArray ids) {
        asyncResponse.resume(doCalculo(ids));
    }

    private String doCalculo(JsonArray ids) {
        
        String resultJson = "[";
        
        long startTime = System.currentTimeMillis();
        
        for (JsonValue idVal : ids) {
            int id = ((JsonObject)idVal).getInt("id");
            resultJson = resultJson + this.calcular(id) + ",";
        }
        resultJson = (resultJson + "]").replace(",]", "]");
        
        long estimatedTime = System.currentTimeMillis() - startTime;
        logger.log(Level.INFO, "TE> " + estimatedTime);
        
        return resultJson;
    }

    @GET
    @Path("{idEmpleado}")
    @Produces("application/json")
    public String calcularUno(@PathParam("idEmpleado") int idEmpleado) {
        
        //Instant a = Instant.now();
        
        String resultJson = this.calcular(idEmpleado);
        
        //logger.log(Level.INFO, "Calculo en: " + Duration.between(a, Instant.now()).toMillis());
        
        return resultJson;
    }
    
    private String calcular(int idEmpleado) {

        Instant a = Instant.now();
        
        this.idEmpleado = idEmpleado;

        MonetaryAmount sueldo_tabulador;
        MonetaryAmount sueldo_diario            = Money.of(0.00, "MXN");
        MonetaryAmount sueldo_ordinario         = Money.of(0.00, "MXN");
        MonetaryAmount sueldo_dias_descanso     = Money.of(0.00, "MXN");
        MonetaryAmount sueldo                   = Money.of(0.00, "MXN");
        MonetaryAmount sueldo_honorarios_diario = Money.of(0.00, "MXN");
        MonetaryAmount sueldo_honorarios        = Money.of(0.00, "MXN");
        
        MonetaryAmount prima_antiguedad_tabulador   = Money.of(0.00, "MXN");
        MonetaryAmount prima_antiguedad_diaria      = Money.of(0.00, "MXN");
        MonetaryAmount prima_antiguedad             = Money.of(0.00, "MXN");
        
        MonetaryAmount materiales_tabulador     = Money.of(0.00, "MXN");
        MonetaryAmount materiales_diario        = Money.of(0.00, "MXN");
        MonetaryAmount materiales               = Money.of(0.00, "MXN");

        MonetaryAmount estimulos_cyt_diario = Money.of(0.00, "MXN");
        MonetaryAmount estimulos_cyt = Money.of(0.00, "MXN");
        
        MonetaryAmount apoyo_mto_vehicular = Money.of(0.00, "MXN");
        MonetaryAmount apoyo_mto_vehicular_diario = Money.of(0.00, "MXN");
        
        MonetaryAmount prima_quinquenal = Money.of(0.00, "MXN");
        MonetaryAmount prima_quinquenal_diaria = Money.of(0.00, "MXN");
        
        MonetaryAmount ajuste_calendario_diario = Money.of(0.00, "MXN");
        MonetaryAmount ajuste_calendario = Money.of(0.00, "MXN");
        MonetaryAmount aguinaldo_diario = Money.of(0.00, "MXN");
        MonetaryAmount aguinaldo = Money.of(0.00, "MXN");
        MonetaryAmount prima_vacacional_diaria = Money.of(0.00, "MXN");
        MonetaryAmount prima_vacacional = Money.of(0.00, "MXN");
        
        MonetaryAmount compensa_garantiza_diaria = Money.of(0.00, "MXN");
        MonetaryAmount compensa_garantiza = Money.of(0.00, "MXN");
        
        MonetaryAmount carga_admin_diaria = Money.of(0.00, "MXN");
        MonetaryAmount carga_admin = Money.of(0.00, "MXN");
        MonetaryAmount fondo_ahorro_exento = Money.of(0.00, "MXN");
        MonetaryAmount fondo_ahorro_gravado = Money.of(0.00, "MXN");
        MonetaryAmount fondo_ahorro = Money.of(0.00, "MXN");
        MonetaryAmount mondero_despensa = Money.of(0.00, "MXN");

        MonetaryAmount pension_alimenticia = Money.of(0.00, "MXN");
        
        MonetaryAmount tiempo_extra_gravado = Money.of(0.00, "MXN");
        MonetaryAmount tiempo_extra_exento = Money.of(0.00, "MXN");
        
        MonetaryAmount base_gravable= Money.of(0.00, "MXN");
        MonetaryAmount base_exenta = Money.of(0.00, "MXN");
        
        MonetaryAmount impuesto_antes_subsidio = Money.of(0.00, "MXN");
        
        MonetaryAmount salario_diario_fijo = Money.of(0.00, "MXN"); // Salario Diario Integrado (todas las percepciones fijas)
        MonetaryAmount salario_diario_variable = Money.of(0.00, "MXN"); // Remanente, EstimulosAYA, etc.
        MonetaryAmount salario_diario_cotizado = Money.of(0.00, "MXN"); // Salario Diario de CotizaciÃ³n (SDI + todss las percepciones variables)
        MonetaryAmount salario_diario_cotizado_topado = Money.of(0.00, "MXN"); // Topado a 25 Salarios MÃ­nimos
        
        MonetaryAmount imss_obrero              = Money.of(0.00, "MXN");
        MonetaryAmount excedente_3SM_diario     = Money.of(0.00, "MXN");
        MonetaryAmount excedente_3SM_diario_empresa = Money.of(0.00, "MXN");
        MonetaryAmount prestaciones_en_dinero   = Money.of(0.00, "MXN");
        MonetaryAmount prestaciones_en_dinero_empresa   = Money.of(0.00, "MXN");
        MonetaryAmount gtos_medicos_y_pension   = Money.of(0.00, "MXN");
        MonetaryAmount gtos_medicos_y_pension_empresa   = Money.of(0.00, "MXN");
        MonetaryAmount invalidez_y_vida         = Money.of(0.00, "MXN");
        MonetaryAmount invalidez_y_vida_empresa         = Money.of(0.00, "MXN");
        MonetaryAmount cesantia_y_vejez         = Money.of(0.00, "MXN");
        MonetaryAmount cesantia_y_vejez_empresa         = Money.of(0.00, "MXN");

        MonetaryAmount cuota_fija_empresa              = Money.of(0.00, "MXN");
        MonetaryAmount riesgo_trabajo_empresa              = Money.of(0.00, "MXN");
        MonetaryAmount guarderias_y_prestaciones_sociales_empresa              = Money.of(0.00, "MXN");
        MonetaryAmount seguro_retiro_empresa              = Money.of(0.00, "MXN");
        MonetaryAmount infonavit_empresa              = Money.of(0.00, "MXN");
        
        MonetaryAmount seg_sep_ind_paramidado = Money.of(0.00, "MXN");
        MonetaryAmount seg_sep_ind_cimav_emp = Money.of(0.00, "MXN");
        MonetaryAmount seg_sep_ind_isr = Money.of(0.00, "MXN");
        
        try {
            
            EmpleadoNomina empleadoNomina = getEntityManager().find(EmpleadoNomina.class, this.idEmpleado);
            if (empleadoNomina == null ) {
                throw new NullPointerException("EMPLEADO");
            }
            if (empleadoNomina.getNomina()== null ) {
                // solo lo carga una vez
                Nomina tmp = nominaREST.inicializar(empleadoNomina);
                empleadoNomina.setNomina(tmp);
                
                //todo throw new NullPointerException("EMPLEADO -- QUINCENAL");
                // se tiene que haber executado antes (al inicio de quincena).
                // http://localhost:8080/RestRH01/api/empleado_quincenal/init/155
            } 
            Nomina nomina = empleadoNomina.getNomina();
            nominaREST.calcularIncidencias(nomina);
            nominaREST.calcularTiempoExtra(nomina);
            
            /*
            Faltas:
            Restar los dÃ­as faltados de los dÃ­as ordinarios. 
            Los dÃ­as de descanso no 'causan' faltas.
            Todas las faltas son dentro de la misma quincenas 
            Se supone que mÃ¡ximo son 3 faltas por quincena.

            Incapacidades:
            Para cuestiÃ³n de pagos (SB, etc.) tomar los dÃ­as 'financieros' (15, 28, 29 o 30).
            Restas los dÃ­as de los ordinarios (hÃ¡biles) y tambiÃ©n de los inhÃ¡biles (descanso) 
            porque ambos se los paga el Imss.
            Excepcion: los dÃ­as 31 se deben capturar pero no afectan los cÃ¡lculos.
            Tomar los dÃ­as calendario para contabilizarlos al final de aÃ±o (ajuste).
            Para el ajuste hace regla de tres: por 365(6) le tocaban 5 dÃ­as ... cuanto le toca por 354?

            Faltas e Incapacidades no se traslapan.
            
            //TODO Falta incluir dÃ­as de Asueto
            
            */
            
            // faltas, incapacidades y asueto del empleado en la quincena
            Integer faltas = nomina.getFaltas();
            Integer incapacidadesHabiles = nomina.getIncapacidadHabiles();
            Integer incapacidadesInhabiles = nomina.getIncapacidadInhabiles();
            // TODO faltan dias de ASUETO/VACACIONES  (16 Sept, etc.)
            
            // TODO faltan dias de ASUETO/VACACIONES  (16 Sept, etc.) tambien en dias_trabajados
            
            Integer dias_ordinarios = nomina.getOrdinarios(); // dias ordianrios que trabajÃ³
            Integer dias_descanso = nomina.getDescanso(); // los dÃ­as de descanso que si le contaron
            Integer dias_trabajados = nomina.getDiasTrabajados(); //dias_ordinarios + dias_descanso; // los dias ordinarios y de descanso q si le contaron
            
            Tabulador nivel = empleadoNomina.getNivel();
            if (nivel == null) {
                throw new NullPointerException("NIVEL");
            }

            boolean isCYT = Objects.equals(EGrupo.CYT.getId(), empleadoNomina.getIdGrupo());
            boolean isAYA = Objects.equals(EGrupo.AYA.getId(), empleadoNomina.getIdGrupo());
            boolean isMMS = Objects.equals(EGrupo.MMS.getId(), empleadoNomina.getIdGrupo());
            boolean isHON = Objects.equals(EGrupo.HON.getId(), empleadoNomina.getIdGrupo());
            
            /*
            Percepciones que dependen del Tabulador:
            - Sueldo, Materiales, ...
            Percepciones que dependen del Sueld
            - Prima Antiguedad, ...
            Percepciones que dependen del Salario Minimo
            - Estimulos, Fondo de Ahorro
            */

            /* Sueldo */
            if (isHON) {
                // HON
                sueldo_tabulador = nivel.getHonorarios();
                if (sueldo_tabulador == null) {
                    throw new NullPointerException("SUELDO_BASE_MES-HONORARIOS");
                }

                sueldo_honorarios_diario = sueldo_tabulador.divide(DIAS_MES_30);

                // Honorarios tienen dias faltados e incapacitados proporcionales
                sueldo_honorarios = sueldo_honorarios_diario.multiply(dias_trabajados);
            } else {
                // CYT, MMS, AYA
                sueldo_tabulador = nivel.getSueldo();
                if (sueldo_tabulador == null) {
                    throw new NullPointerException("SUELDO_BASE_MES");
                }

                // El Sueldo Diario es mes / 30
                sueldo_diario = sueldo_tabulador.divide(DIAS_MES_30);

                sueldo_ordinario = sueldo_diario.multiply(dias_ordinarios);
                sueldo_dias_descanso = sueldo_diario.multiply(dias_descanso);
                
                sueldo = sueldo_ordinario.add(sueldo_dias_descanso);
                
                // TODO Asi como los dias ordinarios y los dias de descanso, faltan los DIAS ASUETO/VACACIONES
                // los 3 son del tipo 001 (001, 003 y 004 Â¿cual es el 002?) . Ver Recibo nÃ³mina [18].
                // EL SAT en facturaciÃ³n electrÃ³nica del NetMultix tiene una clasifiaciÃ³n propia; las del NetMultix se agrupan allÃ­.
                
            }
            
            /* CompensaciÃ³n Garantizada */

            
            // TODO PAnt Odd|Even
            int yearsCumplidos = empleadoNomina.getPantYears();

            /* Prima Antiguedad */
            if (isCYT || isAYA) {
                // TODO Para PAnt faltan los casos donde cumple aÃ±os (complejo)

                Double pant_porcen_tope = 0.525; //default
                // excepciones 
                // el tope es %52.5 pero se aplica a partir del 01/Oct/2015
                // la excepcion es para quienes rebasaron ese tope desde antes de esa fecha
                // su tope sera el % que alcanzaron esa fecha
                if ("00076".equals(empleadoNomina.getCode())) { // villafañe
                    pant_porcen_tope = 0.78; // 20% de 39 años que tenia antes del quiebre. 
                } else if ("00081".equals(empleadoNomina.getCode())) { // miki
                    pant_porcen_tope = 0.62; // 20% de 31 años que tenia antes del quiebre
                }  else if ("00036".equals(empleadoNomina.getCode())) { // Carlos Domingguez
                    pant_porcen_tope = 0.54; // 
                }  else if ("00071".equals(empleadoNomina.getCode())) { // MArquez Lucero
                    pant_porcen_tope = 0.58; // 
                }  else if ("00064".equals(empleadoNomina.getCode())) { // Matutes
                    pant_porcen_tope = 0.68; // 
                }  else if ("00086".equals(empleadoNomina.getCode())) { // Murillo
                    pant_porcen_tope = 0.54; // 
                }  else if ("00087".equals(empleadoNomina.getCode())) { // Neri
                    pant_porcen_tope = 0.64; // 
                }              
                
                Double factorPAnt = 0.00;
                
                if (isAYA) {
                    if (yearsCumplidos >= 21 ) {
                        factorPAnt = (20 * FACTOR_PANT_AYA_5_20);
                        factorPAnt = factorPAnt + ((yearsCumplidos - 20) * FACTOR_PANT_AYA_21);
                    } else if (yearsCumplidos >= 5 ){
                        factorPAnt = yearsCumplidos * FACTOR_PANT_AYA_5_20;
                    }
                } else if (isCYT) {
                    if (yearsCumplidos >= 21 ) {
                        if (empleadoNomina.getPantRegimenAnterior()) {
                            factorPAnt = yearsCumplidos * FACTOR_PANT_CYT_5_20;
                        } else {
                            factorPAnt = (20 * FACTOR_PANT_CYT_5_20);
                            factorPAnt = factorPAnt + ((yearsCumplidos - 20) * FACTOR_PANT_CYT_21);
                        }
                    } else if (yearsCumplidos >= 5 ){
                        factorPAnt = yearsCumplidos * FACTOR_PANT_CYT_5_20;
                    }
                }
                
                // topar si aplica
                if (factorPAnt > pant_porcen_tope) {
                    factorPAnt = pant_porcen_tope;
                }
                
               // factorPAnt = factorPAnt * yearsCumplidos;
                prima_antiguedad_tabulador = sueldo_tabulador.multiply(factorPAnt);
                prima_antiguedad_diaria = sueldo_diario.multiply(factorPAnt);  //prima_antiguedad_tabulador.divide(new BigDecimal(DIAS_MES_30), BIG_SCALE, RoundingMode.HALF_DOWN);
                // prima_antiguedad = dias_trabajados.multiply(prima_antiguedad_tabulador).divide(new BigDecimal(DIAS_MES_30), BIG_SCALE, RoundingMode.HALF_DOWN);
                // Proporcionales al Sueldo o con regla de tres con dÃ­as trabajados dan el mismo resultado
                prima_antiguedad = sueldo.multiply(factorPAnt);
            }

            /* Materiales */
            if (isCYT) {
                materiales_tabulador = sueldo_tabulador.multiply(new BigDecimal(PORCEN_MATERIALES).setScale(BIG_SCALE, RoundingMode.HALF_DOWN));
                materiales_diario = sueldo_diario.multiply(new BigDecimal(PORCEN_MATERIALES).setScale(BIG_SCALE, RoundingMode.HALF_DOWN));
                materiales = sueldo.multiply(new BigDecimal(PORCEN_MATERIALES).setScale(BIG_SCALE, RoundingMode.HALF_DOWN));
            }
                    
            /* Estimulos Productividad */
            if (isCYT) {
                // Son proporcionales a los DT?
                estimulos_cyt_diario = Money.of(SALARIO_MINIMO * empleadoNomina.getEstimulosProductividad(), MXN);
                estimulos_cyt = estimulos_cyt_diario.multiply(dias_trabajados);
            }
            
            // TODO todos las operaciones Moneda con scale a 5.
            
            // fondo de ahorro NO suma para SDI pero grava para el Impuesto cuando se excede de 1.3 SM.
            
            /* Fondo de Ahorro */
            if (isCYT || isAYA) {
                fondo_ahorro = sueldo.multiply(PORCEN_FONDO_AHORRO);
                fondo_ahorro_exento = Money.of(SALARIO_MINIMO * PORCEN_FONDO_AHORRO_EXENTO * DIAS_QUINCENA_15, MXN);
                if (fondo_ahorro_exento.isGreaterThanOrEqualTo(fondo_ahorro)) {
                    // exenta completo
                    fondo_ahorro_exento = fondo_ahorro;
                } else {
                    // la parte gravable
                    fondo_ahorro_gravado = fondo_ahorro.subtract(fondo_ahorro_exento);
                }
            }
            
            /* CompensaciÃ³n Garantizada */
            if(nivel.getCompGarantizada() != null && !nivel.getCompGarantizada().isZero()) {
                // El tabulador / 30
                compensa_garantiza_diaria = nivel.getCompGarantizada().divide(DIAS_MES_30);
                compensa_garantiza = compensa_garantiza_diaria.multiply(dias_trabajados);
            }
            
            /* Carga Administrativa */
            if(nivel.getCargaAdmin() != null && !nivel.getCargaAdmin().isZero()) {
                // El tabulador / 30
                carga_admin_diaria = nivel.getCargaAdmin().divide(DIAS_MES_30);
                carga_admin = carga_admin_diaria.multiply(dias_trabajados);
            }
            
            /* Monedero Despensa */
            if (isAYA || isCYT) {
                // TODO constantes de Vales no HARD CODE
                mondero_despensa = Money.of(931.00, MXN).divide(2); //771
                // TODO despensa 15 es correcto
                mondero_despensa = mondero_despensa.divide(15).multiply(dias_trabajados);
            } else if (isMMS) {
                mondero_despensa = Money.of(365.00, MXN).divide(2);
                mondero_despensa = mondero_despensa.divide(15).multiply(dias_trabajados);
            } 

            // La nÃ³mina no incluye  Jovenes Catedras?
            // Si incluye Catedreas Repatriadas
            
            /* Ajuste de Calendario */
            // Ajuste aplica para Aya, Cyt y MMS; no para Hon
            if (isAYA || isCYT /*|| isHON*/) {
                // TODO Ajuste 5 o 6 aÃ±os Constante
                // sueldo_diario * 5 /360
                ajuste_calendario_diario = sueldo_diario.multiply(DIAS_AJUSTE_5_6).divide(360);
                
                // Si es quincena de calculo semestral
                if (false) {
                    // 180 por semestre; proporcionales con Faltas, Permisos con goce, Asueto e Incapacidades
                    // 180 por 2.5 o 3 si es bisiesto
                    ajuste_calendario = ajuste_calendario_diario.multiply(180);
                }
            }
            
            /* Aguinaldo */ 
            // por 360 dias tocan 40 dias
            aguinaldo_diario = sueldo_diario.multiply(40).divide(360);
            // TODO para honorarios el aguinaldo juega como variables para ene-feb
            
            /* Prima Vacional */
            // por 360 dias tocan 24 dias para CYT y AYA (24 = 40% de 60 dias)
            prima_vacacional_diaria = sueldo_diario.multiply(24).divide(360);
            
            /* Prima Quinquenal / Seguro de separacion indivudualizado */
            if (isMMS) {
                if (yearsCumplidos >= 5 && yearsCumplidos < 10) {
                    prima_quinquenal =  Money.of(100.00, "MXN").divide(2);   
                } else if (yearsCumplidos >= 10 && yearsCumplidos < 15) {
                    prima_quinquenal =  Money.of(125.00, "MXN").divide(2);   
                } else if (yearsCumplidos >= 15 && yearsCumplidos < 20) {
                    prima_quinquenal =  Money.of(175.00, "MXN").divide(2);   
                } else if (yearsCumplidos >= 20 && yearsCumplidos < 25) {
                    prima_quinquenal =  Money.of(200.00, "MXN").divide(2);   
                }  else if (yearsCumplidos >= 25) {
                    prima_quinquenal =  Money.of(225.00, "MXN").divide(2);   
                }
                prima_quinquenal_diaria = prima_quinquenal.divide(DIAS_QUINCENA_15);
                prima_quinquenal = prima_quinquenal_diaria.multiply(dias_trabajados);
                
                double porcen = empleadoNomina.getPorcenSegSeparacionInd() / 100;
                seg_sep_ind_cimav_emp = sueldo.add(compensa_garantiza).multiply(porcen);
                // TODO falta piramidar SSI y ISR_SSI y el % por cada MMS, meter el piramidado diario al sdi
                seg_sep_ind_paramidado = Money.of(0.00, "MXN");
                seg_sep_ind_isr = Money.of(0.00, "MXN");
                
            }
            // TODO PrimaQuinquenal y suma para SDI?
            
            // Apoyo Mantenimiento Vehicular
            if (nivel.getCode().startsWith("KA")) {
                // Sólo para el Director General
                apoyo_mto_vehicular_diario = Money.of(5916 / 2 / 15, "MXN");
                apoyo_mto_vehicular = apoyo_mto_vehicular_diario.multiply(dias_trabajados);
            }
            
            /* Seguro de SeparaciÃ³n Individualizado */
            
            // TODO Falta obtener y Agregar Otros_Fijos Al SDI
            /* Otros Fijos */
            // En NetMultix tiene Estimulos, Materiales, CAdmin pero aquÃ­ se pueden poner por separado
            MonetaryAmount otros_fijos = Money.of(0.00, MXN);
            
            // Variables: Remanente, Retros, para HON el Aguinaldo, Estimulos de AYA, 
            
            // Los pagos extra no van al SDI, no aplican al IMSS
            // La despensa si aplica al IMSS, lo que exceda del 40% que entra como variables y se sabes hasta la 24 integrado a Ene-Feb            
            
            // Tiempo Extra
            // http://www.idconline.com.mx/laboral/2013/07/26/pago-correcto-de-tiempo-extra
            
            
            /* Tiempo Horas Extras */
            if (isAYA) {
                // Jornada de 8 horas
                MonetaryAmount sueldo_hora = sueldo_diario.divide(8.0); // Para el TE se usa el diario o el cotizado
                tiempo_extra_gravado = sueldo_hora.multiply(nomina.getHorasExtrasDobles());
                if (nomina.getHorasExtrasDobles() > 0) {
                    tiempo_extra_gravado = tiempo_extra_gravado.add(sueldo_hora.multiply(nomina.getHorasExtrasTriples()));
                }
            }
            
//            // TODO Horas extras gravadas y exentas
//            if (idEmpleado == 180) {
//                // aya gonzalez trevizo
//                base_gravable = Money.of(771.41,MXN);
//            }
//            if (idEmpleado == 67) {
//                // aya mariana lopez
//                base_gravable = Money.of(302.55,MXN);
//            }

            /**************************************/
            /* Integrar: Salario Diario Integrado */
            /**************************************/
            
            salario_diario_fijo = salario_diario_fijo.add(sueldo_diario);
            salario_diario_fijo = salario_diario_fijo.add(sueldo_honorarios_diario);
            salario_diario_fijo = salario_diario_fijo.add(prima_antiguedad_diaria);
            salario_diario_fijo = salario_diario_fijo.add(ajuste_calendario_diario);
            salario_diario_fijo = salario_diario_fijo.add(aguinaldo_diario);
            salario_diario_fijo = salario_diario_fijo.add(carga_admin_diaria);
            salario_diario_fijo = salario_diario_fijo.add(compensa_garantiza_diaria);
            salario_diario_fijo = salario_diario_fijo.add(prima_vacacional_diaria);
            salario_diario_fijo = salario_diario_fijo.add(materiales_diario);
            salario_diario_fijo = salario_diario_fijo.add(estimulos_cyt_diario);
            salario_diario_fijo = salario_diario_fijo.add(otros_fijos);
            salario_diario_fijo = salario_diario_fijo.add(prima_quinquenal_diaria);
            salario_diario_fijo = salario_diario_fijo.add(apoyo_mto_vehicular_diario);
            // Tiempo extra no cotiza
            
            // mondero_despensa no integra pq no es $$$
            
            // TODO otras percepciones capturadas van en Fijo ??
            
            // TODO Faltan el SDI Variable (Remanente CYT, Estimulo AYA, Bimestre Anterior)
            salario_diario_variable = nomina.getSdiVariableBimestreAnterior();
            if (salario_diario_variable == null) {
                // TODO que pasa si salario_diario_variable es null
                salario_diario_variable = Money.of(BigDecimal.ZERO, MXN);
            }
            
            // cotizado = fijo + variables Â¿Equivalente al Mixto del Imss?
            salario_diario_cotizado = salario_diario_fijo.add(salario_diario_variable);
            
            // cotizado se topa (no el fijo)
            MonetaryAmount topeSalarioDiarioCotizado = Money.of(SALARIO_DIARIO_TOPE * SALARIO_MINIMO, MXN);
            if (salario_diario_cotizado.compareTo(topeSalarioDiarioCotizado) > 0) {
                // Si rebasa el tope, agarra el topo
                salario_diario_cotizado_topado = topeSalarioDiarioCotizado;
            } else {
                // Si no rebasa el tope, agarra el cotizado directo
                salario_diario_cotizado_topado = salario_diario_cotizado;
            }

            /* IMSS */
       //     Integer dias_reales_bimestre = quincena.getDiasBimestre(); 
            // TODO IMSS: donde uso los dias_reales_bimestre ?!?!
            
            // ES CON DIAS COTIZADOS
            
            // TODO calculo del Imss
            // TODO Â¿Con el Cotizado_Topado se calculan las ramas? Â¿Cotizado_Topado equivalente la SDI_Calculado de Excel?
            // con dias reales de la quincena menos incapacidades
            // con topes
            // el execente incluye falta e incapacidades
            
            // TODO para las repercuciones del Imss, los Dias Trabajados DEBEN ser Dias Cotizados
            
            MonetaryAmount excedente_3SM_diario_factor = Money.of(3 * SALARIO_MINIMO, MXN);
            if (salario_diario_cotizado_topado.compareTo(excedente_3SM_diario_factor) > 0) {
                excedente_3SM_diario_factor = salario_diario_cotizado_topado.subtract(excedente_3SM_diario_factor);
            }
            excedente_3SM_diario = excedente_3SM_diario_factor.multiply(0.0040).multiply(dias_trabajados); //especie - excedente
            prestaciones_en_dinero = salario_diario_cotizado_topado.multiply(0.0025).multiply(dias_trabajados); // prestaciones en dinero
            gtos_medicos_y_pension = salario_diario_cotizado_topado.multiply(0.003750).multiply(dias_trabajados); // pensionados y beneficiarios
            invalidez_y_vida = salario_diario_cotizado_topado.multiply(0.006250).multiply(dias_trabajados);
            cesantia_y_vejez = salario_diario_cotizado_topado.multiply(0.011250).multiply(dias_trabajados);
            
            imss_obrero = excedente_3SM_diario.add(prestaciones_en_dinero).add(gtos_medicos_y_pension).add(invalidez_y_vida).add(cesantia_y_vejez);
            //imss_obrero = imss_obrero.multiply(dias_trabajados); // TODO ¿Es por dias trabajado o dias quincena o dias reales de la quincena?
            
            excedente_3SM_diario_empresa = excedente_3SM_diario_factor.multiply(0.0110).multiply(dias_trabajados); //especie - excedente
            prestaciones_en_dinero_empresa = salario_diario_cotizado_topado.multiply(0.0070).multiply(dias_trabajados); // prestaciones en dinero
            gtos_medicos_y_pension_empresa = salario_diario_cotizado_topado.multiply(0.0105).multiply(dias_trabajados); // pensionados y beneficiarios
            invalidez_y_vida_empresa = salario_diario_cotizado_topado.multiply(0.0175).multiply(dias_trabajados);
            cesantia_y_vejez_empresa = salario_diario_cotizado_topado.multiply(0.0315).multiply(dias_trabajados);

            cuota_fija_empresa = Money.of(SALARIO_MINIMO, MXN).multiply(0.2040).multiply(dias_trabajados); 
            riesgo_trabajo_empresa = salario_diario_cotizado_topado.multiply(0.005436).multiply(dias_trabajados);
            guarderias_y_prestaciones_sociales_empresa = salario_diario_cotizado_topado.multiply(0.0100).multiply(dias_trabajados); 
            seguro_retiro_empresa = salario_diario_cotizado_topado.multiply(0.0200).multiply(dias_trabajados); 
            infonavit_empresa = salario_diario_cotizado_topado.multiply(0.0500).multiply(dias_trabajados); 
            
            // TODO Al termino del ejercicio fiscal (Ãºltima quincena de dic) se acumulan todos los pagos 
            // por concepto de previsiÃ³n social para gravar la diferencia y / o integrar la diferencia de vales 
            // como variable para integraciÃ³n del IMSS en enero y febrero del siguiente aÃ±o.

            /* Gravar */

            base_gravable = base_gravable.add(sueldo_ordinario);
            base_gravable = base_gravable.add(sueldo_dias_descanso);
            base_gravable = base_gravable.add(sueldo_honorarios);
            base_gravable = base_gravable.add(prima_antiguedad);
            base_gravable = base_gravable.add(materiales);
            base_gravable = base_gravable.add(estimulos_cyt);
            base_gravable = base_gravable.add(carga_admin);
            base_gravable = base_gravable.add(compensa_garantiza);
            base_gravable = base_gravable.add(fondo_ahorro_gravado);
            // base_gravable = base_gravable.add(mondero_despensa); // Monedero excenta
            base_gravable = base_gravable.add(prima_quinquenal);
            base_gravable = base_gravable.add(apoyo_mto_vehicular);
            base_gravable = base_gravable.add(tiempo_extra_gravado);

            /* Exentar */

            base_exenta = base_exenta.add(fondo_ahorro_exento);
            base_exenta = base_exenta.add(tiempo_extra_exento);
            base_exenta = base_exenta.add(mondero_despensa); // Monedero excenta

            /* Impuesto */
            impuesto_antes_subsidio = calcularImpuesto(base_gravable);

            
            // Eliminar los Calculos Previos para evitar conceptos rezagados
            // no incluidos en el este proceso
            this.vaciarCalculos(idEmpleado);
            
            insertarCalculo(SUELDO_ORDINARIO, sueldo_ordinario);
            insertarCalculo(SUELDO_DIAS_DESCANSO, sueldo_dias_descanso);
            insertarCalculo(HONORARIOS_ASIMILABLES, sueldo_honorarios);
            insertarCalculo(PRIMA_ANTIGUEDAD, prima_antiguedad);
            insertarCalculo(CARGA_ADMINISTRATIVA, carga_admin);
            insertarCalculo(MATERIALES, materiales);
            insertarCalculo(ESTIMULOS_PRODUCTIVIDAD_CYT, estimulos_cyt);
            insertarCalculo(COMPENSA_GARANTIZADA, compensa_garantiza);
            insertarCalculo(FONDO_AHORRO_EXENTO, fondo_ahorro_exento);
            insertarCalculo(FONDO_AHORRO_GRAVADO, fondo_ahorro_gravado);
            insertarCalculo(FONDO_AHORRO, fondo_ahorro);
            insertarCalculo(HORAS_EXTRAS_GRAVADO, tiempo_extra_gravado);
            insertarCalculo(HORAS_EXTRAS_EXENTO, tiempo_extra_exento);
            insertarCalculo(APORTACION_FONDO_AHORRO, fondo_ahorro);
            insertarCalculo(PRIMA_QUINQUENAL, prima_quinquenal);
            insertarCalculo(APOYO_MANTENIMIENTO_VEHICULAR, apoyo_mto_vehicular);
            
            insertarCalculo(MONEDERO_DESPENSA, mondero_despensa); //TODO la Despensa no suma puesto que se paga con Vales
            insertarCalculo(IMSS, imss_obrero);

            insertarCalculo(SEG_SEPARACION_IND_CIMAV, seg_sep_ind_cimav_emp);
            insertarCalculo(SEG_SEPARACION_IND_EMPLEADO, seg_sep_ind_cimav_emp);

            insertarCalculo(PENSION_ALIMENTICIA, pension_alimenticia);
            
            insertarCalculo(BASE_GRAVABLE, base_gravable);
            insertarCalculo(BASE_EXENTA, base_exenta);
            
            insertarCalculo(ISR, impuesto_antes_subsidio);
            
            insertarCalculo(SUELDO_DIARIO, sueldo_diario);
            insertarCalculo(SUELDO_DIARIO, sueldo_honorarios_diario);
            
            insertarCalculo(SALARIO_DIARIO_FIJO, salario_diario_fijo);
            insertarCalculo(SALARIO_DIARIO_VARIABLE, salario_diario_variable);
            insertarCalculo(SALARIO_DIARIO_COTIZADO, salario_diario_cotizado);
            insertarCalculo(SALARIO_DIARIO_COTIZADO_TOPADO, salario_diario_cotizado_topado);

            //insertarCalculo(EXCEDENTE_3SMG, excedente_3SM_diario);
            insertarCalculoImssEmpresa(EXCEDENTE_3SMG, excedente_3SM_diario, excedente_3SM_diario_empresa);
            insertarCalculoImssEmpresa(PRESTACIONES_EN_DINERO, prestaciones_en_dinero, prestaciones_en_dinero_empresa);
            insertarCalculoImssEmpresa(GTOS_MEDICOS_Y_PENSION, gtos_medicos_y_pension, gtos_medicos_y_pension_empresa);
            insertarCalculoImssEmpresa(INVALIDEZ_Y_VIDA, invalidez_y_vida, invalidez_y_vida_empresa);
            insertarCalculoImssEmpresa(CESANTIA_Y_VEJEZ, cesantia_y_vejez, cesantia_y_vejez_empresa);
            insertarCalculoImssEmpresa(CUOTA_FIJA, Money.of(0.00, "MXN"), cuota_fija_empresa);
            insertarCalculoImssEmpresa(RIESGO_DE_TRABJO, Money.of(0.00, "MXN"), riesgo_trabajo_empresa);
            insertarCalculoImssEmpresa(GUARDERIAS_Y_PRESTA_SOCIALES, Money.of(0.00, "MXN"), guarderias_y_prestaciones_sociales_empresa);
            insertarCalculoImssEmpresa(SEGURO_RETIRO, Money.of(0.00, "MXN"), seguro_retiro_empresa);
            insertarCalculoImssEmpresa(INFONAVIT, Money.of(0.00, "MXN"), infonavit_empresa);
            
            /** Pensión Alimenticia (después de calcular y persistir todos los movimientos) **/
            BigDecimal big_pension_alimenticia = this.getPensionAlimenticia(idEmpleado, empleadoNomina.getPensionIdTipo(), empleadoNomina.getPensionPorcen());
            pension_alimenticia = Money.of(big_pension_alimenticia.doubleValue(), "MXN");

            insertarCalculoImssEmpresa(PENSION_ALIMENTICIA, Money.of(0.00, "MXN"), pension_alimenticia);
            
        } catch (NullPointerException | RollbackException ex) {
            return "-Unico";
        }
            
        String resultJSON = "\"PERCEPCION\": {";
            resultJSON = resultJSON + "\"" + SUELDO_ORDINARIO + "\": " + sueldo_ordinario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + SUELDO_DIAS_DESCANSO + "\": " + sueldo_dias_descanso.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + HONORARIOS_ASIMILABLES + "\": " + sueldo_honorarios.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + PRIMA_ANTIGUEDAD + "\": " + prima_antiguedad.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + CARGA_ADMINISTRATIVA + "\": " + carga_admin.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + MATERIALES + "\": " + materiales.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + ESTIMULOS_PRODUCTIVIDAD_CYT + "\": " + estimulos_cyt.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + COMPENSA_GARANTIZADA + "\": " + compensa_garantiza.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + PRIMA_QUINQUENAL + "\": " + prima_quinquenal.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + APOYO_MANTENIMIENTO_VEHICULAR + "\": " + apoyo_mto_vehicular.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + FONDO_AHORRO_EXENTO + "\": " + fondo_ahorro_exento.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + FONDO_AHORRO_GRAVADO + "\": " + fondo_ahorro_gravado.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + HORAS_EXTRAS_GRAVADO + "\": " + tiempo_extra_gravado.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + MONEDERO_DESPENSA + "\": " + mondero_despensa.getNumber().toString();
        resultJSON = resultJSON + " }";
        
        resultJSON = resultJSON + ",\"DEDUCCION\": {";
            resultJSON = resultJSON + "\"" + ISR + "\": " + impuesto_antes_subsidio.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + IMSS + "\": " + imss_obrero.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + FONDO_AHORRO + "\": " + fondo_ahorro.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + APORTACION_FONDO_AHORRO + "\": " + fondo_ahorro.getNumber().toString()+ ",";
            resultJSON = resultJSON + "\"" + PENSION_ALIMENTICIA + "\": " + pension_alimenticia.getNumber().toString()+ ",";
            resultJSON = resultJSON + "\"" + SEG_SEPARACION_IND_CIMAV + "\": " + seg_sep_ind_cimav_emp.getNumber().toString()+ ",";
            resultJSON = resultJSON + "\"" + SEG_SEPARACION_IND_EMPLEADO + "\": " + seg_sep_ind_cimav_emp.getNumber().toString()+ ",";
            resultJSON = resultJSON + "\"" + PENSION_ALIMENTICIA + "\": " + pension_alimenticia.getNumber().toString();
        resultJSON = resultJSON + " }";
        
        resultJSON = resultJSON + ",\"BASE_GRAVABLE\": {";
            resultJSON = resultJSON + "\"" + "TOTAL" + "\": " + base_gravable.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Sueldo ordinario" + "\": " + sueldo_ordinario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Sueldo descanso" + "\": " + sueldo_dias_descanso.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Sueldo honorarios" + "\": " + sueldo_honorarios.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Prima antg" + "\": " + prima_antiguedad.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Mater" + "\": " + materiales.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Carga" + "\": " + carga_admin.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Compensa" + "\": " + compensa_garantiza.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Fondo ahorro" + "\": " + fondo_ahorro_gravado.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Estimulos" + "\": " + estimulos_cyt.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Tiempo Extra" + "\": " + tiempo_extra_gravado.getNumber().toString();
//            resultJSON = resultJSON + "\"" + "Monedero" + "\": " + mondero_despensa.getNumber().toString();
        resultJSON = resultJSON + " }";
        
        resultJSON = resultJSON + ",\"BASE_EXENTA\": {";
            resultJSON = resultJSON + "\"" + "TOTAL" + "\": " + base_exenta.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Monedero despensa" + "\": " + mondero_despensa.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Fondo ahorro" + "\": " + fondo_ahorro_exento.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Tiempo Extra" + "\": " + tiempo_extra_exento.getNumber().toString();
        resultJSON = resultJSON + " }";
        
        resultJSON = resultJSON + ",\"SDI\": {";
            resultJSON = resultJSON + "\"" + "FIJO" + "\": " + salario_diario_fijo.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "VARIABLE" + "\": " + salario_diario_variable.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "COTIZADO" + "\": " + salario_diario_cotizado.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "COTIZADO_TOPADO" + "\": " + salario_diario_cotizado_topado.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Sueldo" + "\": " + sueldo_diario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Sueldo honorarios" + "\": " + sueldo_honorarios_diario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "PAnt" + "\": " + prima_antiguedad_diaria.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Carga" + "\": " + carga_admin_diaria.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Compensa" + "\": " + compensa_garantiza_diaria.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Ajuste" + "\": " + ajuste_calendario_diario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Aguin" + "\": " + aguinaldo_diario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "PrimaVac" + "\": " + prima_vacacional_diaria.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Materiales" + "\": " + materiales_diario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Prima Quinquenal" + "\": " + prima_quinquenal_diaria.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Apoyo Manto Vehicular" + "\": " + apoyo_mto_vehicular_diario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "Estimulos" + "\": " + estimulos_cyt_diario.getNumber().toString();
        resultJSON = resultJSON + " }";

        resultJSON = resultJSON + ",\"IMSS_OBRERO\": {";
            resultJSON = resultJSON + "\"" + "IMSS_OBRERO" + "\": " + imss_obrero.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "excedente_3SMG" + "\": " + excedente_3SM_diario.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "prestaciones_en_dinero" + "\": " + prestaciones_en_dinero.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "gtos_medicos_y_pension" + "\": " + gtos_medicos_y_pension.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "invalidez_y_vida" + "\": " + invalidez_y_vida.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "cesantia_y_vejez" + "\": " + cesantia_y_vejez.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "couta_fija" + "\": " + cuota_fija_empresa.getNumber().toString() + ",";
            resultJSON = resultJSON + "\"" + "riesgo_trabajo" + "\": " + riesgo_trabajo_empresa.getNumber().toString()+ ",";
            resultJSON = resultJSON + "\"" + "guarderias_y_presta_sociales" + "\": " + guarderias_y_prestaciones_sociales_empresa.getNumber().toString()+ ",";
            resultJSON = resultJSON + "\"" + "seguro_retiro" + "\": " + seguro_retiro_empresa.getNumber().toString()+ ",";
            resultJSON = resultJSON + "\"" + "infonavit" + "\": " + infonavit_empresa.getNumber().toString();
            
        resultJSON = resultJSON + " }";
        
        resultJSON = ("{" + "\"id\":" + this.idEmpleado +"," + resultJSON + "}").replace(",}", "}");
        
        return resultJSON;
    }
    private  HashMap<String, Concepto> hmapConceptos = new HashMap<>();
    
    private void insertarCalculo(String strConcepto, MonetaryAmount monto) {
        
        if (monto == null || monto.isZero()) {
            // insertar solo los mayores a cero
            return;
        }
        // obtener concepto
        Concepto concepto = finConceptoByCode(strConcepto);
        if (concepto == null) {
            throw new NullPointerException(strConcepto);
        }
        
        // calculo
        Movimiento nomQuin = new Movimiento(this.idEmpleado, concepto, monto, Money.of(0.00, "MXN"));
        // siempre es inserción de nuevo, dado que previo vacié/eliminé todos sus cálculos
        /*
        Movimiento nomQuin = this.findNominaQuincenal(this.idEmpleado, concepto);
        if (nomQuin == null) {
            // es creaciÃƒÂ³n
            nomQuin = new Movimiento(this.idEmpleado, concepto, monto);
        } else {
            // es update
            nomQuin.setCantidad(monto);
        }
        */
        
        // persistirlo
        getEntityManager().persist(nomQuin);
    }

    private void insertarCalculoImssEmpresa(String strConcepto, MonetaryAmount monto, MonetaryAmount montoEmpresa) {
        
//        if (monto == null || monto.isZero()) {
//            // insertar solo los mayores a cero
//            return;
//        }
        // obtener concepto
        Concepto concepto = finConceptoByCode(strConcepto);
        if (concepto == null) {
            throw new NullPointerException(strConcepto);
        }
        
        // calculo
        Movimiento movimiento = new Movimiento(this.idEmpleado, concepto, monto, montoEmpresa);
        // siempre es inserción de nuevo, dado que previo vacié/eliminé todos sus cálculos
        /*
        Movimiento nomQuin = this.findNominaQuincenal(this.idEmpleado, concepto);
        if (nomQuin == null) {
            // es creaciÃƒÂ³n
            nomQuin = new Movimiento(this.idEmpleado, concepto, monto);
        } else {
            // es update
            nomQuin.setCantidad(monto);
        }
        */
        
        // persistirlo
        getEntityManager().persist(movimiento);
        
    }
    
    public Concepto finConceptoByCode(String code) {
        Concepto result = hmapConceptos.get(code);
        if (result == null) {
            try {
                result = (Concepto) getEntityManager().createNamedQuery("Concepto.findByCode").setParameter("code", code).getSingleResult();
                if (result != null) {
                    hmapConceptos.put(code, result);
                }
            } catch (NoResultException e) {
            }
        }
        return result;
    }

    public Movimiento findMovimiento(Integer idEmpleado, Concepto concepto) {
        try {
            return (Movimiento) getEntityManager().createNamedQuery("Movimiento.findBy_IdEmpleado_Concepto")
                    .setParameter("id_empleado", idEmpleado).setParameter("concepto", concepto).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
//    public Integer[] findIncidencias(Integer idEmpleado, String clase) {
//        Integer[] result = new Integer[2];
//        result[0] = 0;
//        result[1] = 0;
//        try {
//            Object[] tmp = (Object[])getEntityManager().createQuery("SELECT SUM(i.diasHabiles), SUM(i.diasInhabiles) FROM Incidencia i WHERE i.idEmpleado = :id_empleado AND i.clase = :clase")
//                    .setParameter("id_empleado", idEmpleado).setParameter("clase", clase).getSingleResult();
//            if (tmp != null) {
//                result[0] = tmp[0] == null ? 0 : ((Long)tmp[0]).intValue();
//                result[1] = tmp[1] == null ? 0 : ((Long)tmp[1]).intValue();
//            }
//        } catch (NonUniqueResultException | NoResultException e) {
//        }
//        return result;
//    }
    
    public void vaciarCalculos(int idEmpleado) {
        // Elimina todos los calculos del empleado de Movimiento
        // Solo calculos; no movimientos (saldos y pagos).
        try {
            String query = "DELETE FROM Movimiento n WHERE n.idEmpleado = :id_empleado AND n.concepto.idTipoMovimiento = :id_tipo_mov";
            getEntityManager().createQuery(query).setParameter("id_empleado", idEmpleado).setParameter("id_tipo_mov", 'C').executeUpdate();
        } catch (Exception e) {
            System.out.println("vaciarCalculos ::> " + e.getMessage());
        }
    }
    
    private TarifaAnual getTarifaAnual(MonetaryAmount base_gravable) {
        TarifaAnual result = null;
        if (listTarifaAnual == null) {
            listTarifaAnual = new ArrayList<>();
            List<TarifaAnual> tarifas = getEntityManager().createNamedQuery("TarifaAnual.findAll").getResultList();
            if (tarifas != null) {
                listTarifaAnual.addAll(tarifas);
            }
        }
        for(TarifaAnual tarifa : listTarifaAnual) {
            if (tarifa.getLimiteInferior().isLessThanOrEqualTo(base_gravable) && tarifa.getLimiteSuperior().isGreaterThanOrEqualTo(base_gravable)) {
                result = tarifa;
                break;
            }
        }
        return result;
    }
    
    private MonetaryAmount calcularImpuesto(MonetaryAmount base_gravable) {
        if (base_gravable.isLessThanOrEqualTo(Money.of(BigDecimal.ZERO, MXN))) {
            return Money.of(BigDecimal.ZERO, MXN);
        }
        TarifaAnual tarifaAnual = getTarifaAnual(base_gravable);
        MonetaryAmount excedente = base_gravable.subtract(tarifaAnual.getLimiteInferior());
        MonetaryOperator opePercen = MonetaryUtil.percent(tarifaAnual.getPorcentaje());
        MonetaryAmount porcen = excedente.with(opePercen);
        MonetaryAmount impAntesSubsidio = porcen.add(tarifaAnual.getCuota());
        
        return impAntesSubsidio;
    }

    @GET
    @Path("/sdi_vinculacion/{id_empleado}")
    @Produces("application/json")
    public String getSdi(@PathParam("id_empleado") int idEmpleado) {
        Concepto concepto = finConceptoByCode(SALARIO_DIARIO_FIJO);
        if (concepto == null) {
            return "0.00";
        }
        Movimiento nomQuin = this.findMovimiento(idEmpleado, concepto);
        if (nomQuin != null && nomQuin.getCantidad() != null) {
            return "" + nomQuin.getCantidad().getNumber();
        }
        return "-1.00";
    }

    @GET
    @Path("/pension_alimenticia/{id_empleado}/{tipo}/{percen}")
    public BigDecimal getPensionAlimenticia(@PathParam("id_empleado") Integer idEmp, @PathParam("tipo") Integer tipo, @PathParam("percen") Double percen) {
        
        if (percen > 0) {
            percen = percen / 100;
        }
        
        String nativeSql= "SELECT CAST(c.id AS Integer) FROM conceptos AS c JOIN pensionalimenticia AS pa ON c.id = pa.id_concepto JOIN empleados AS e ON pa.id_empleado = e.id "
                + "WHERE e.id = " + idEmp;
        Query query = getEntityManager().createNativeQuery(nativeSql);
        query.setParameter("id_empleado", idEmp);
        List<Integer> pensionConceptoIds = query.getResultList();
        
        MonetaryAmount totPercepPension = Money.of(BigDecimal.ZERO, "MXN");
        MonetaryAmount totDeducPension = Money.of(BigDecimal.ZERO, "MXN");
        List<Movimiento> movimientos = movimientosREST.findByIdEmpleado(idEmp);
        for(Movimiento movi : movimientos) {
            Concepto concepto = movi.getConcepto();
            if (concepto.getSuma()) {
                // cosidera solo conceptos que suman
                if ('P' == concepto.getIdTipoConcepto()) {
                    // suma todas las percepcions
                    totPercepPension = totPercepPension.add(movi.getCantidad());
                } else if ('D' == concepto.getIdTipoConcepto() && pensionConceptoIds.contains(concepto.getId())) {
                    // suma las deducciones incluidas en Pensiones del empleado
                    totDeducPension = totDeducPension.add(movi.getCantidad());
                }
            }
        }

        BigDecimal result = BigDecimal.ZERO;
        
        if(tipo == 0) {
            
        } else if (tipo == 1) {
        
        }  else if (tipo == 2) {
            String str = totPercepPension.subtract(totDeducPension).multiply(percen).getNumber().toString();
            result = new BigDecimal(str).setScale(5, RoundingMode.HALF_EVEN);
        }  else if (tipo == 3) {
            
        }
        
        return result;
    }
    
}

