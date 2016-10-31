/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author calderon
 */
@Entity
@Cacheable(false)
@Table(name = "solicitantes", schema = "public")
@XmlRootElement(name = "solicitante")
public class Solicitante implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @XmlElement(name = "empleado")
    @JoinColumn(name = "id_empleado", referencedColumnName = "id")
    @ManyToOne
    private EmpleadoBase empleado;

    @XmlElement(name = "elabora")
    @JoinColumn(name = "id_elabora", referencedColumnName = "id")
    @ManyToOne
    private EmpleadoBase elabora;

    @XmlElement(name = "autoriza")
    @JoinColumn(name = "id_autoriza", referencedColumnName = "id")
    @ManyToOne
    private EmpleadoBase autoriza;

    public Solicitante() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EmpleadoBase getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoBase empleado) {
        this.empleado = empleado;
    }

    public EmpleadoBase getElabora() {
        return elabora;
    }

    public void setElabora(EmpleadoBase elabora) {
        this.elabora = elabora;
    }

    public EmpleadoBase getAutoriza() {
        return autoriza;
    }

    public void setAutoriza(EmpleadoBase autoriza) {
        this.autoriza = autoriza;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Solicitante other = (Solicitante) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Solicitante{" + "id=" + id + ", empleado=" + empleado + ", elabora=" + elabora + ", autoriza=" + autoriza + '}';
    }

    
}
