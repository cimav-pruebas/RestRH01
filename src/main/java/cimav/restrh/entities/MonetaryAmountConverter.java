/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.money.MonetaryAmount;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.javamoney.moneta.Money;

/**
 *
 * @author juan.calderon
 */
@Converter(autoApply = true)
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(MonetaryAmount attribute) {
        BigDecimal big = null;
        if (attribute != null) {
            String str = attribute.getNumber().toString();
            big = new BigDecimal(str).setScale(5, RoundingMode.HALF_EVEN);
        }
        return big;
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(BigDecimal dbData) {
        MonetaryAmount asAmount = null;
        if (dbData != null) {
            asAmount = Money.of(dbData.doubleValue(), "MXN");
        }
        return asAmount;
    }
    
    
}