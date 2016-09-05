/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author calderon
 */
@Converter(autoApply = true) // <---- Importante
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        Date date = null;
        if (localDate != null) {
            date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return date;
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        LocalDate locaDate = null;
        if (date != null) {
            locaDate = new java.sql.Date(date.getTime()).toLocalDate();
        }
        return locaDate;
    }
    
}