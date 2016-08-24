/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.money.MonetaryAmount;

/**
 *
 * @author juan.calderon
 */
@Singleton // TODO checar el alcance del Sigleton: session del usuario? app en el server? etc.
@Startup
public class QuincenaSingleton {
    
    private final static Logger logger = Logger.getLogger(QuincenaSingleton.class.getName() ); 
    
    public static final Integer INICIAL     = 0;
    public static final Integer ABIERTA     = 1;
    public static final Integer CERRANDOSE  = 2;
    public static final Integer CERRADA     = 3;
    
    private Integer year;
    private Integer bimestre;
    private Integer quincena;
    
    private Integer status;
    private MonetaryAmount salarioMinimo;
    
    private Date fechaInicio = null;
    private Date fechaFin = null;
    private Date fechaFinCalendario = null;
    
    private Integer diasOrdinarios = null;
    private Integer diasDescanso = null;
    private Integer diasImss = null;
    private Integer diasAsueto = null;
    
    private String asJson = null;
    
    private int mes;
    private int diaInicio;    
    private int diaFinal;
    
    private Integer yearSDIVariable;
    private Integer bimestreSDIVariable;
    private Integer quinInicialSDIVariable;
    private Integer quinFinSDIVariable;

    private String fi;
    private String ffr;
    private String fff;
    private int diaFinalCalendario;
    
    /*******************
     Poner un EJB o accesar al em en un Singleton no es recomendable. Se bloquea
     muy fácil. Para desbloquear hay que reiniciar GlassFish y DB.
     *******************/
    
//    @EJB
//    private QuincenaREST quincenaREST;
//    @PersistenceContext(unitName = "PU_JPA")
//    EntityManager em;
    
    public QuincenaSingleton() {
        logger.log(Level.INFO, "QuincenaSingleton()");
    }
    
    /**
     * Inicializa la QuincenaSingleton con el Year y QuincenaSingleton acuales
 traídos desde la DB
     */
    //@PostConstruct
    public void load() {

        /* // Se movió de aqui porque el EntityManagerFactory causa problemas en un Singleton
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "PU_JPA" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        
        Query query = entitymanager.createQuery("SELECT q FROM Quincena AS q ORDER BY q.id DESC");
        Quincena quincenaEntity = (Quincena) query.setMaxResults(1).getSingleResult();
        //logger.log(Level.SEVERE, "Prueba quincena loaded>" + quincenaEntity);
        
        this.status = quincenaEntity.getStatus();
        this.salarioMinimo = quincenaEntity.getSalarioMinimo();
        this.year = quincenaEntity.getYear();
        this.quincena = quincenaEntity.getQuincena();
        */
        
        if (quincena == null) {
            return;
        }
        
        boolean isQuincenaPar = (quincena & 1) == 0;
        mes = (int)Math.ceil(quincena/2.0);
        diaInicio = isQuincenaPar ? 16 : 1;
        
        this.bimestre = (int)Math.ceil(this.mes / 2.0);
        
        Calendar fechaInicioCal = Calendar.getInstance();
        fechaInicioCal.set(year, mes-1, diaInicio, 0, 0, 0);
        
        // si es quincena non, dia real y topado coinciden en 15
        diaFinalCalendario = 15;
        diaFinal = 15;
        if (isQuincenaPar) {
            // último día real del mes
            diaFinalCalendario = fechaInicioCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            // maximo llegan al día treinta
            diaFinal = diaFinalCalendario > 30 ? 30 : diaFinalCalendario; 
        }
        
        Calendar fechaFinCalendarioCal = Calendar.getInstance();
        fechaFinCalendarioCal.set(year, mes-1, diaFinalCalendario, 0, 0, 0);
        
        Calendar fechaFinCalculoCal = Calendar.getInstance();
        fechaFinCalculoCal.set(year, mes-1, diaFinal, 0, 0, 0);
        
        Calendar fechaAvanzaCal = Calendar.getInstance();
        fechaAvanzaCal.set(year, mes-1, diaInicio, 0, 0, 0); //igual a la de inicio
        
        int diasOrdinariosCount = 0;
        int diasDescansoCount = 0;
        while (fechaFinCalculoCal.after(fechaAvanzaCal) || fechaFinCalculoCal.equals(fechaAvanzaCal)) {
            if (fechaAvanzaCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || fechaAvanzaCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                diasDescansoCount++;
            } else {
                diasOrdinariosCount++;
            }
            fechaAvanzaCal.add(Calendar.DATE, 1);
        }
        
        LocalDate start = LocalDate.of(year, mes, fechaInicioCal.get(Calendar.DAY_OF_MONTH)).minusDays(1); // TODO PELIGROSO con minus1Day considera el día 
        // TODO Verificar el TimeZone
        LocalDate end = LocalDate.of(year, mes, fechaFinCalendarioCal.get(Calendar.DAY_OF_MONTH));
        int diasImssCount = Period.between(start, end).getDays();  // TODO ya se INCLUYO el dia final.
        
//                DateTime start1 = new DateTime(year, mes, fechaInicioCal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC).minusDays(1); 
//        DateTime end1 = new DateTime(year, mes, fechaFinCalendarioCal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC);
//        
//        int diasImssCount = Days.daysBetween(start, end).getDays();

        // datos para obtener el SDI Variable (acumulado de las 4 quincenas del bimestre anterior)
        yearSDIVariable = year;
        bimestreSDIVariable = bimestre - 1;
        if (bimestreSDIVariable <= 0) {
            bimestreSDIVariable = 6;
            yearSDIVariable = yearSDIVariable - 1;
        }
        quinFinSDIVariable = bimestreSDIVariable * 4;
        quinInicialSDIVariable = quinFinSDIVariable - 3;

        this.fechaInicio = fechaInicioCal.getTime(); 
        this.fechaFinCalendario = fechaFinCalendarioCal.getTime();
        this.fechaFin = fechaFinCalculoCal.getTime();
        this.diasOrdinarios = diasOrdinariosCount;
        this.diasDescanso = diasDescansoCount;
        this.diasImss = diasImssCount;
        this.diasAsueto = 0; // TODO faltan los días de Asueto
        
        String formatStr = "\"yyyy-MM-dd'T'HH:mm:ssZ\""; //"\"yyyy-MM-dd'T'HH:mm:ssXXX\"";
        fi = new SimpleDateFormat(formatStr).format(this.fechaInicio);
        ffr = new SimpleDateFormat(formatStr).format(this.fechaFinCalendario);
        fff = new SimpleDateFormat(formatStr).format(this.fechaFin);
        
        this.asJson = 
            "{ " + "\"quincena\": " + quincena + "," +
            "\t" + "\"mes\": " + mes + "," +
            "\t" + "\"bimestre\": " + bimestre + "," +
            "\t" + "\"dias_bimestre\": " + this.getDiasBimestre() + "," +
            "\t" + "\"year\": " + year + "," +
            "\t" + "\"fechaInicio\": " + fi + "," +
            "\t" + "\"diaInicio\": " + diaInicio + "," +
            "\t" + "\"diaFin\": " + diaFinal + "," +
            "\t" + "\"diaFinCalendario\": " + diaFinalCalendario + "," +
            "\t" + "\"fechaFin\": " + fff + "," +
            "\t" + "\"fechaFinCalendario\": " + ffr + "," +
            "\t" + "\"dias\": " + (diasOrdinarios + diasDescanso) + "," +
            "\t" + "\"diasOrdinarios\": " + diasOrdinarios + "," +
            "\t" + "\"diasDescando\": " + diasDescanso + "," +
            "\t" + "\"diasAsueto\": " + diasAsueto + "," +
            "\t" + "\"diasImss\": " + diasImss + "," +
            "\t" + "\"status\": " + status + "," +
            "\t" + "\"salarioMinimo\": " + salarioMinimo.getNumber() +
            "}";
    }
    
    public static LocalDate convert(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day_month = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate localDate = LocalDate.of(year, month, day_month);
        return localDate;
    }
        
    public String toJSON() {
        //logger.log(Level.SEVERE, asJson);
        
        return this.asJson;
    }
    
    public Integer getBimestre() {
        return bimestre;
    }
    
    public final Integer getDiasBimestre() {
        return this.getDiasBimestre(this.bimestre, this.year);
    }
    public Integer getDiasBimestre(int bimestre) {
        return this.getDiasBimestre(bimestre, this.year);
    }
    public Integer getDiasBimestre(int bimestre, int year) {
        int m1 = bimestre + bimestre -1;
        int diasM1 = getDiasMes(m1, year);
        int m2 = bimestre + bimestre;
        int diasM2 = getDiasMes(m2, year);
        return diasM1 + diasM2;
    }
    
    public Integer getDiasMes() {
        return this.getDiasMes(this.mes, this.year);
    }
    public Integer getDiasMes(int month) {
        return this.getDiasMes(month, this.year);
    }
    public Integer getDiasMes(int month, int year) {
        Integer result = 31;
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            result = 30;
        } else if (month == 2) {
            result = this.isLeapYear(year) ? 29 : 28;
        }
        return result;
    }
    
    public boolean isLeapYear() {
        return this.isLeapYear(this.year);
    }    
    public boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }    
    
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMes() {
        return mes;
    }
    
    public Integer getQuincena() {
        return quincena;
    }

    public void setQuincena(Integer quincena) {
        this.quincena = quincena;
    }
    
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFinCalendario() {
        return fechaFinCalendario;
    }
/**
 * 
 * @return  Fecha Fin de la QuincenaSingleton
 */
    public Date getFechaFin() {
        return fechaFin;
    }

    public Integer getDiasOrdinarios() {
        return diasOrdinarios;
    }

    public Integer getDiasDescanso() {
        return diasDescanso;
    }

    /**
     * Días de la QuincenaSingleton
     */
    public Integer getDiasLaborables() {
        return diasOrdinarios + diasDescanso;
    }
    
    public Integer getDiasImss() {
        return diasImss;
    }

    public Integer getDiasAsueto() {
        // TODO Faltan los dias de Asueto
        return diasAsueto;
    }
    
    public Integer getDiaInicio() {
        return diaInicio;
    }
    public Integer getDiaFinal() {
        return diaFinal;
    }

    public Integer getYearSDIVariable() {
        return yearSDIVariable;
    }

    public Integer getQuinInicialSDIVariable() {
        return quinInicialSDIVariable;
    }

    public Integer getQuinFinSDIVariable() {
        return quinFinSDIVariable;
    }

    public Integer getStatus() {
        return status;
    }

    public MonetaryAmount getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setSalarioMinimo(MonetaryAmount salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    
}
