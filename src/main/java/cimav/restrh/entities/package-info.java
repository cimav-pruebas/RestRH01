@XmlJavaTypeAdapters({
//    @XmlJavaTypeAdapter(value=LocalDateAdapter.class, type=LocalDate.class),
    @XmlJavaTypeAdapter(value=MonetaryAmountAdapter.class, type=MonetaryAmount.class)
})
package cimav.restrh.entities;

import javax.money.MonetaryAmount;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

