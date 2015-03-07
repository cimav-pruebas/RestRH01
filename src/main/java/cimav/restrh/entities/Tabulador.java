/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Cacheable;
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

    @OneToMany(mappedBy = "nivel")
    private Collection<Empleado> empleadoCollection;

    public Tabulador() {
    }

    @XmlTransient
    public Collection<Empleado> getEmpleadoCollection() {
        return empleadoCollection;
    }

    public void setEmpleadoCollection(Collection<Empleado> empleadoCollection) {
        this.empleadoCollection = empleadoCollection;
    }

}
