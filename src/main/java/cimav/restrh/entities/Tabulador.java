/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Cacheable;
import javax.persistence.Column;
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
@Entity
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
    private BigDecimal sueldo;

    @Column(name = "mat_didacticos")
    private BigDecimal matDidacticos;

    @Column(name = "comp_garantizada")
    private BigDecimal compGarantizada;

    @Column(name = "honorarios")
    private BigDecimal honorarios;

    @Column(name = "carga_admin")
    private BigDecimal cargaAdmin;
    
    @OneToMany(mappedBy = "nivel")
    private Collection<Empleado> empleadoCollection;

    public Tabulador() {
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }

    public BigDecimal getMatDidacticos() {
        return matDidacticos;
    }

    public void setMatDidacticos(BigDecimal matDidacticos) {
        this.matDidacticos = matDidacticos;
    }

    public BigDecimal getCompGarantizada() {
        return compGarantizada;
    }

    public void setCompGarantizada(BigDecimal compGarantizada) {
        this.compGarantizada = compGarantizada;
    }

    public BigDecimal getHonorarios() {
        return honorarios;
    }

    public void setHonorarios(BigDecimal honorarios) {
        this.honorarios = honorarios;
    }

    public BigDecimal getCargaAdmin() {
        return cargaAdmin;
    }

    public void setCargaAdmin(BigDecimal cargaAdmin) {
        this.cargaAdmin = cargaAdmin;
    }
    
    @XmlTransient
    public Collection<Empleado> getEmpleadoCollection() {
        return empleadoCollection;
    }

    public void setEmpleadoCollection(Collection<Empleado> empleadoCollection) {
        this.empleadoCollection = empleadoCollection;
    }

}
