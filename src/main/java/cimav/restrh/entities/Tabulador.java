/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.money.MonetaryAmount;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juan.calderon
 */
@Entity(name = "Tabulador")
@Cacheable(false)
@Table(name = "tabulador", catalog = "rh_development", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tabulador.findAll", query = "SELECT t FROM Tabulador t"),
    @NamedQuery(name = "Tabulador.findById", query = "SELECT t FROM Tabulador t WHERE t.id = :id"),
    @NamedQuery(name = "Tabulador.findByCode", query = "SELECT t FROM Tabulador t WHERE t.code = :code"),
    @NamedQuery(name = "Tabulador.findByName", query = "SELECT t FROM Tabulador t WHERE t.name = :name")})
public class Tabulador extends BaseEntity implements Serializable {

    @Column(name = "sueldo")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount sueldo;

    @Column(name = "mat_didacticos")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount matDidacticos;

    @Column(name = "comp_garantizada")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount compGarantizada;

    @Column(name = "honorarios")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount honorarios;

    @Column(name = "carga_admin")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount cargaAdmin;
    
    @OneToMany(mappedBy = "nivel")
    private Collection<EmpleadoBase> empleadoCollection;

    @Column(name = "extra")
    @Convert(converter = MonetaryAmountConverter.class)
    //@XmlJavaTypeAdapter(MonedaAdapter.class)
    private MonetaryAmount extra;

    public MonetaryAmount getExtra() {
        return extra;
    }

    public void setExtra(MonetaryAmount extra) {
        this.extra = extra;
    }
    
    public Tabulador() {
    }

    public MonetaryAmount getSueldo() {
        return sueldo;
    }

    public void setSueldo(MonetaryAmount sueldo) {
        this.sueldo = sueldo;
    }

    public MonetaryAmount getMatDidacticos() {
        return matDidacticos;
    }

    public void setMatDidacticos(MonetaryAmount matDidacticos) {
        this.matDidacticos = matDidacticos;
    }

    public MonetaryAmount getCompGarantizada() {
        return compGarantizada;
    }

    public void setCompGarantizada(MonetaryAmount compGarantizada) {
        this.compGarantizada = compGarantizada;
    }

    public MonetaryAmount getHonorarios() {
        return honorarios;
    }

    public void setHonorarios(MonetaryAmount honorarios) {
        this.honorarios = honorarios;
    }

    public MonetaryAmount getCargaAdmin() {
        return cargaAdmin;
    }

    public void setCargaAdmin(MonetaryAmount cargaAdmin) {
        this.cargaAdmin = cargaAdmin;
    }
    
    @XmlTransient
    public Collection<EmpleadoBase> getEmpleadoCollection() {
        return empleadoCollection;
    }

    public void setEmpleadoCollection(Collection<EmpleadoBase> empleadoCollection) {
        this.empleadoCollection = empleadoCollection;
    }

}
