/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import cimav.restrh.services.ParametrosREST;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author juan.calderon
 */
@Singleton
@Startup
public class Quincena {
    
    private final static Logger logger = Logger.getLogger(Quincena.class.getName() ); 
    
    private Integer year;
    private Integer bimestre;
    private Integer quincena;
    
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

    @EJB
    private ParametrosREST parametrosREST;
    
    
    public Quincena() {
    }
    
    /**
     * Inicializa la Quincena con el Year y Quincena acuales
     * traídos desde la DB
     */
    @PostConstruct
    public void init() {
        Parametros parametros = parametrosREST.get();
        //logger.log(Level.INFO, "%%>> " + quin);
        // TODO 2015
        this.set(2015, parametros.getQuincenaActual());
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
    
    /**
     * Inicializa la Quincena de acuerdo a los parametros.
     * 
     * Importante: después de utilizar la quincena con set, 
     * llamar el método init() para recuperar la quincena actual.
     * 
     * @param pyear
     * @param pquincena 
     */
    public void set(int pyear, int pquincena) {
        this.year = pyear;
        this.quincena = pquincena;
        
        boolean isQuincenaPar = (quincena & 1) == 0;
        mes = (int)Math.ceil(quincena/2.0);
        diaInicio = isQuincenaPar ? 16 : 1;
        
        this.bimestre = (int)Math.ceil(this.mes / 2.0);
        
        Calendar fechaInicioCal = Calendar.getInstance();
        fechaInicioCal.set(year, mes-1, diaInicio, 0, 0, 0);
        
        // si es quincena non, dia real y topado coinciden en 15
        int diaFinalCalendario = 15;
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

        this.fechaInicio = fechaInicioCal.getTime(); 
        this.fechaFinCalendario = fechaFinCalendarioCal.getTime();
        this.fechaFin = fechaFinCalculoCal.getTime();
        this.diasOrdinarios = diasOrdinariosCount;
        this.diasDescanso = diasDescansoCount;
        this.diasImss = diasImssCount;
        this.diasAsueto = 0; // TODO faltan los días de Asueto
        
        String formatStr = "\"yyyy-MM-dd'T'HH:mm:ssZ\""; //"\"yyyy-MM-dd'T'HH:mm:ssXXX\"";
        String fi = new SimpleDateFormat(formatStr).format(this.fechaInicio);
        String ffr = new SimpleDateFormat(formatStr).format(this.fechaFinCalendario);
        String fff = new SimpleDateFormat(formatStr).format(this.fechaFin);
        
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
            "\t" + "\"diasImss\": " + diasImss + "" +
            "}";
    }
    
    public String toJSON() {
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
 * @return  Fecha Fin de la Quincena
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
     * Días de la Quincena
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
    
}
