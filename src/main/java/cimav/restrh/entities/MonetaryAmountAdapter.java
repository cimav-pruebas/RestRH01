/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.math.BigDecimal;
import javax.money.MonetaryAmount;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.FastMoney;

/**
 *
 * @author juan.calderon
 */
public class MonetaryAmountAdapter extends XmlAdapter<BigDecimal, MonetaryAmount>{

    @Override
    public MonetaryAmount unmarshal(BigDecimal val) throws Exception {
        MonetaryAmount amount = null;
        if (val != null) {
            amount = Money.of(val, "MXN");
        } 
        return amount;
    }

    @Override
    public BigDecimal marshal(MonetaryAmount val) throws Exception {
        BigDecimal big = null;
        if (val != null) {
            String str = val.getNumber().toString();
            big = new BigDecimal(str);
        }
        return big;
    }
    
}
