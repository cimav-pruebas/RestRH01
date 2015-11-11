/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.money.MonetaryAmount;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan.calderon
 */
@Entity(name = "TarifaAnual")
@Cacheable(true)
@Table(name = "tarifaanual", catalog = "rh_development", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TarifaAnual.findAll", query = "SELECT ta FROM TarifaAnual ta"),
})
public class TarifaAnual implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "lim_inferior")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount limiteInferior;
    
    @Column(name = "lim_superior")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount limiteSuperior;
    
    @Column(name = "cuota_fija")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount cuota;
    
    @Column(name = "porcentaje_excedente")
    private Double porcentaje;

    public Integer getId() {
        return id;
    }

    public MonetaryAmount getLimiteInferior() {
        return limiteInferior;
    }

    public MonetaryAmount getLimiteSuperior() {
        return limiteSuperior;
    }

    public MonetaryAmount getCuota() {
        return cuota;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLimiteInferior(MonetaryAmount limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public void setLimiteSuperior(MonetaryAmount limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public void setCuota(MonetaryAmount cuota) {
        this.cuota = cuota;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.limiteInferior);
        hash = 79 * hash + Objects.hashCode(this.limiteSuperior);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TarifaAnual other = (TarifaAnual) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.limiteInferior, other.limiteInferior)) {
            return false;
        }
        if (!Objects.equals(this.limiteSuperior, other.limiteSuperior)) {
            return false;
        }
        return true;
    }

    
}
