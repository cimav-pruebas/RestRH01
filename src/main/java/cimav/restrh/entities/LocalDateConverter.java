/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.time.Instant;
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
public class LocalDateConverter implements AttributeConverter<java.time.LocalDate, Date> {
    
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        Date date = null;
        if (localDate != null) {
            date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        }
        System.out.println(localDate + " >>>Local>>> " + date);
        return date;
    }
        
        
        //return (localDate == null ? null : Date.valueOf(localDate));
//         //Date result = localDate == null ? null : java.sql.Date.valueOf(localDate);
//         
//         Date result = null;
//         if (localDate != null) {
//             result = new Date(2015, 1, 13);
//         }
//         
//         
//         //System.out.println("Loc_>_Date " + localDate + ":" + result);
//         //Date date = java.sql.Date.valueOf("1997-01-13");//localDate);;
//         System.out.println("Date>>> " + result);
//        return result;
//        Date sqlDate = null;
//        if (localDate != null) {
//            sqlDate = Date.valueOf(localDate);
//        }
//        return sqlDate;
        
        //return (localDate == null ? null : Date.valueOf(localDate));
//        Date date = null;
//        if (localDate != null) {
//            try {
//                //date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//                date = new SimpleDateFormat("yyyy-MM-dd").parse(localDate.toString());
//            } catch (ParseException ex) {
//                Logger.getLogger(LocalDateConverter.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return date;
 //   }

    @Override
    public LocalDate convertToEntityAttribute(Date date) { 
        
        LocalDate result = null;
        if (date != null) {
            result = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        System.out.println("Local>>> " + result);
        return result;
//        LocalDate result = sqlDate == null ? null : sqlDate.toLocalDate();
//        //System.out.println("DtoL " + sqlDate + ":" + result);
//        System.out.println("LocalDate>>> " + result);
//        return result;
////        LocalDate localDate = null;
////        if (sqlDate != null) {
////            localDate = sqlDate.toLocalDate();
////        }
////        return localDate;
//        //return (sqlDate == null ? null : sqlDate.toLocalDate());
////        LocalDate locaDate = null;
////        if (date != null) {
////            locaDate = new java.sql.Date(date.getTime()).toLocalDate();
////        }
////        return locaDate;
    }
    
}
/*
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
	
    @Override
    public Date convertToDatabaseColumn(LocalDate locDate) {
    	return (locDate == null ? null : Date.valueOf(locDate));
    }

    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
    	return (sqlDate == null ? null : sqlDate.toLocalDate());
    }
}
*/