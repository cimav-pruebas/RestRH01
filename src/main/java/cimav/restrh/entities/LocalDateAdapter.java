/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author calderon
 */
public class LocalDateAdapter extends XmlAdapter<Date, LocalDate> {

    @Override
    public LocalDate unmarshal(Date date) throws Exception {
        LocalDate localDate = null;
        if (date != null) {
            localDate = new java.sql.Date(date.getTime()).toLocalDate();
        } 
        System.out.println("Xunmarshal " + date + ":" + localDate);
        return localDate;
    }

    @Override
    public Date marshal(LocalDate localDate) throws Exception {
        Date date = null;
        if (localDate != null) {
            // Puff simplemente para agregarle 10:10
            LocalDateTime ldt = localDate.atTime(10, 10);
            date = Date.from(ldt.toInstant(ZoneOffset.UTC));
        }
        System.out.println("Ymarshal " + localDate + ":" + date);
        return date;
    }
   
}
