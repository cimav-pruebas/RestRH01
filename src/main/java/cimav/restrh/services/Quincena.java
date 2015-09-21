/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

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
    private final Date fechaFinCalendario;
    private final Date fechaFinCalculo;
    private final Integer diasOrdinarios;
    private final Integer diasDescanso;
    private final Integer diasImss;

    private static Quincena instance;
    
    public static Quincena get() {
        if (instance == null) {
            // TODO quincena y año deben venir desde la DB
            int pquincena = 1;
            int pyear = 2015;
            instance = new Quincena(pyear, pquincena);
        }
        return instance;
    }
    
    private Quincena(int pyear, int pquincena) {
        
        this.year = pyear;
        this.quincena = pquincena;
        
        boolean isQuincenaPar = (quincena & 1) == 0;
        int mes = (int)Math.ceil(quincena/2.0);
        int diaInicio = isQuincenaPar ? 16 : 1;
        
        Calendar fechaInicioCal = Calendar.getInstance();
        fechaInicioCal.set(year, mes-1, diaInicio);
        
        // si es quincena non, dia real y topado coinciden en 15
        int diaFinalReal = 15;
        int diaFinalFiscal = 15;
        if (isQuincenaPar) {
            // último día real del mes
            diaFinalReal = fechaInicioCal.getActualMaximum(Calendar.DAY_OF_MONTH);
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
        
        DateTime start = new DateTime(year, mes, fechaInicioCal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC).minusDays(1);
        DateTime end = new DateTime(year, mes, fechaFinCalendarioCal.get(Calendar.DATE), 0, 0, 0, DateTimeZone.UTC);
        
        int diasImssCount = Days.daysBetween(start, end).getDays();
        
        this.fechaInicio = fechaInicioCal.getTime();
        this.fechaFinCalendario = fechaFinCalendarioCal.getTime();
        this.fechaFinCalculo = fechaFinCalculoCal.getTime();
        this.diasOrdinarios = diasOrdinariosCount;
        this.diasDescanso = diasDescansoCount;
        this.diasImss = diasImssCount;
    }
    
    public String getAsJSON() {
        String result = 
            "{ " + "\"quincena\": " + quincena + "," +
            "\t" + "\"fecha_inicio\": " + fechaInicio + "," +
            "\t" + "\"fecha_fin_calendario\": " + fechaFinCalendario + "," +
            "\t" + "\"fecha_fin_calculo\": " + fechaFinCalculo + "," +
            "\t" + "\"dias_ordinarios\": " + diasOrdinarios + "," +
            "\t" + "\"dias_descando\": " + diasDescanso + "," +
            "\t" + "\"dias_calculo\": " + (diasOrdinarios + diasDescanso) + "," +
            "\t" + "\"dias_imss\": " + diasImss + "" +
            "}";
        return result;
    }

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

    public Date getFechaFinCalculo() {
        return fechaFinCalculo;
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

    
}
