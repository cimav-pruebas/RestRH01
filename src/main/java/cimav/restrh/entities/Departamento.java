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
@Table(name = "departamentos", catalog = "rh_development", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departamento.findAll", query = "SELECT d FROM Departamento d"),
    @NamedQuery(name = "Departamento.findById", query = "SELECT d FROM Departamento d WHERE d.id = :id"),
    @NamedQuery(name = "Departamento.findByCode", query = "SELECT d FROM Departamento d WHERE d.code = :code"),
    @NamedQuery(name = "Departamento.findByName", query = "SELECT d FROM Departamento d WHERE d.name = :name")})
public class Departamento extends BaseEntity implements Serializable {

    @OneToMany(mappedBy = "departamento")
    private Collection<EmpleadoBase> empleadoCollection;

    public Departamento() {
    }

    @XmlTransient
    public Collection<EmpleadoBase> getEmpleadoCollection() {
        return empleadoCollection;
    }

    public void setEmpleadoCollection(Collection<EmpleadoBase> empleadoCollection) {
        this.empleadoCollection = empleadoCollection;
    }

}
