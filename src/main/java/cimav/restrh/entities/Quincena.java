/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

/**
 *
 * @author juan.calderon
 */
public class Quincena {
    
    private Integer year;
    private Integer quincena;
    
    private final Date fechaInicio;
    private final Date fechaFin;
    private final Date fechaFinCalendario;
    
    private final Integer diasOrdinarios;
    private final Integer diasDescanso;
    private final Integer diasImss;
    private final Integer diasAsueto;
    
    private static Quincena instance;
    
    private final String asJson;
    
//    private boolean isQuincenaPar;
//    private int mes;
//    private int diaInicio;    
//    private int diaFinalCalendario;
//    private int diaFinal;
    
    public static Quincena get() {
        if (instance == null) {
            // TODO quincena y año deben venir desde la DB
            int pquincena = 18;
            int pyear = 2015;
            instance = new Quincena(pyear, pquincena);
        }
        return instance;
    }

    public static Quincena get(int pyear, int pquincena) {
        return new Quincena(pyear, pquincena);
    }
    
    private Quincena(int pyear, int pquincena) {
        
        this.year = pyear;
        this.quincena = pquincena;
        
        boolean isQuincenaPar = (quincena & 1) == 0;
        int mes = (int)Math.ceil(quincena/2.0);
        int diaInicio = isQuincenaPar ? 16 : 1;
        
        Calendar fechaInicioCal = Calendar.getInstance();
        fechaInicioCal.set(year, mes-1, diaInicio, 0, 0, 0);
        
        // si es quincena non, dia real y topado coinciden en 15
        int diaFinalCalendario = 15;
        int diaFinal = 15;
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
        
        DateTime start = new DateTime(year, mes, fechaInicioCal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC).minusDays(1); // con minus1Day considera el día 
        DateTime end = new DateTime(year, mes, fechaFinCalendarioCal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC);
        
        int diasImssCount = Days.daysBetween(start, end).getDays();
        
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
            "{ " + "\"year\": " + year + "," +
            "\t" + "\"mes\": " + mes + "," +
            "\t" + "\"quincena\": " + quincena + "," +
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
    
    public String getAsJSON() {
        return this.asJson;
    }
    
    /*
    public int[] getDias(Date fechaDada, int dias) {

        int[] result = new int[2];
        
        // TODO Falta considera el 31; especial para las incapacidades que se captura pero no se cobra
        
        // la fecha dada debe estar entre el inicio y fin de la quincena
        boolean isBetween = fechaDada.compareTo(fechaInicio) >= 0 && fechaDada.compareTo(fechaFin) <= 0; // incluye endpoints
        if (!isBetween) {
            result[0] = -1;
            result[1] = -1;
        }
        
        // la fecha dada es el calendario de inicio
        Calendar calInicioAux = Calendar.getInstance();
        calInicioAux.setTime(fechaDada);
        
        // el tope es la Fecha Fin (15, 28, 29, 30 o 31)
        Calendar calFinAux = Calendar.getInstance();
        calFinAux.set(year, mes-1, diaFinal);
        
        int diasOrdinariosCount = 0;
        int diasDescansoCount = 0;
        int contadorDias = 0;
        while ((contadorDias < dias) && (calFinAux.after(calInicioAux) || calFinAux.equals(calInicioAux))) {
            dias++;
            if (calInicioAux.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calInicioAux.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                diasDescansoCount++;
            } else {
                diasOrdinariosCount++;
            }
            calInicioAux.add(Calendar.DATE, 1);
        }
        
        
        // si es quincena non, dia real y topado coinciden en 15
        int diaFinalReal = 15;
        int diaFinalFiscal = 15;
        if (isQuincenaPar) {
            // último día real del mes
            diaFinalReal = fechaInicio.getActualMaximum(Calendar.DAY_OF_MONTH);
            // maximo llegan al día treinta
            diaFinalFiscal = diaFinalReal > 30 ? 30 : diaFinalReal; 
        }
        
        Calendar fechaFinCalendarioCal = Calendar.getInstance();
        fechaFinCalendarioCal.set(year, mes-1, diaFinalReal);
        
        Calendar fechaFinCalculoCal = Calendar.getInstance();
        fechaFinCalculoCal.set(year, mes-1, diaFinalFiscal);
        
        Calendar fechaAvanzaCal = Calendar.getInstance();
        fechaAvanzaCal.set(year, mes-1, diaInicio); //igual a la de inicio
            
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
        
        DateTime start = new DateTime(year, mes, fechaInicioCal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC).minusDays(1); // con minus1Day considera el día 
        DateTime end = new DateTime(year, mes, fechaFinCalendarioCal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC);
        
        int diasImssCount = Days.daysBetween(start, end).getDays();
        
        this.fechaInicio = fechaInicioCal.getTime();
        this.fechaFinCalendario = fechaFinCalendarioCal.getTime();
        this.fechaFin = fechaFinCalculoCal.getTime();
        this.diasOrdinarios = diasOrdinariosCount;
        this.diasDescanso = diasDescansoCount;
        this.diasImss = diasImssCount;
        this.diasAsueto = 0; // TODO faltan los días de Asueto
        
        int[] result = new int[2];
        result[0] = diasOrdinariosCount;
        result[1] = diasDescansoCount;
        
        return result;
    }
*/
    
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
    
    
}
