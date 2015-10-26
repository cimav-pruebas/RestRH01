/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
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
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "incidencias", catalog = "rh_development", schema = "public")
@XmlRootElement
public class Incidencia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static String FALTA          = "F";
    public static String INCAPACIDAD    = "I";
    public static String PERMISO        = "P";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "clase")
    private String clase;

    @Column(name = "dias")
    private Integer dias;

    @Column(name = "dias_habiles")
    private Integer diasHabiles;

    @Column(name = "dias_inhabiles")
    private Integer diasInhabiles;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    
    @Column(name = "folio")
    private String folio;

    @JoinColumn(name="id_empleado", referencedColumnName = "id", 
            insertable = false, updatable = false)
    @ManyToOne
    private EmpleadoNomina empleadoNomina;
    
    @Column(name = "id_empleado") 
    private Integer idEmpleado;

    public Incidencia() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getDiasHabiles() {
        return diasHabiles;
    }

    public void setDiasHabiles(Integer diasHabiles) {
        this.diasHabiles = diasHabiles;
    }

    public Integer getDiasInhabiles() {
        return diasInhabiles;
    }

    public void setDiasInhabiles(Integer diasInhabiles) {
        this.diasInhabiles = diasInhabiles;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
        this.fechaInicial = DateUtils.truncate(fechaInicial, Calendar.DAY_OF_MONTH);
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }
    
    
}
