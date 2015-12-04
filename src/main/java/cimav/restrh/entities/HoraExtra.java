/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "horasextras", catalog = "rh_development", schema = "public")
@XmlRootElement(name = "horasextras")
public class HoraExtra implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // No tiene Get/Set y No Insertable ni Updatable; es decir, es OnlyRead y sirve como mappedBy en EmpleadoQuincenal
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado", insertable = false, updatable = false)
    @ManyToOne
    private EmpleadoQuincenal empleadoQuincenal;
    
    @Column(name = "id_empleado")
    private Integer idEmpleado;
    
    @Column(name = "dia")
    @Temporal(TemporalType.DATE)
    private Date dia;

    @Column(name = "horas")
    private Double horas;

    @Column(name = "quincena")
    private Integer quincena;
    
    @Column(name = "week_of_year")
    private Integer weekOfYear;
    
    public HoraExtra() {
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getDia() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dia);
        this.weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Double getHoras() {
        return horas;
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }

    public Integer getQuincena() {
        return quincena;
    }

    public void setQuincena(Integer quincena) {
        this.quincena = quincena;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final HoraExtra other = (HoraExtra) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
}