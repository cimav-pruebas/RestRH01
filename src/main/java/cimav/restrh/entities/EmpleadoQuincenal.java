/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Se calcula SOLO UNA VEZ la iniciar la Quincena.
 * Asi se evita re-calcular en exceso.
 * 
 * Por Ejemplo, los dias de PAnt 
 * 
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "empleadoquincenal", catalog = "rh_development", schema = "public")
@XmlRootElement(name = "empleadoquincenal")
public class EmpleadoQuincenal implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // Conexion con el Empleado
    @Column(name = "id_empleado")
    private Integer idEmpleado;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private EmpleadoNomina empleadoNomina; /* Read Only */
    
    // Faltas, Incapacidades, Ordinarios y Descanso se modifican junto
    // a las Incidencias
    private Integer faltas;
    @Column(name = "incapacidad_habiles")
    private Integer incapacidadHabiles;
    @Column(name = "incapacidad_inhabiles")
    private Integer incapacidadInhabiles;
    private Integer ordinarios;
    private Integer descanso;
    
    // Si cumple años durante la quicena la PAnt es proporcional.
    // diasPAntUno corresponde a los dias con los años anteriores
    // diasPAntDos corresponde a los dias con los años nuevos
    @Column(name = "years_pant")
    private Integer yearPAnt;
    @Column(name = "dias_pant_uno") 
    private Integer diasPAntUno;
    @Column(name = "dias_pant_dos")
    private Integer diasPAntDos;

    public EmpleadoQuincenal() {
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

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }

    public Integer getIncapacidadHabiles() {
        return incapacidadHabiles;
    }

    public void setIncapacidadHabiles(Integer incapacidadHabiles) {
        this.incapacidadHabiles = incapacidadHabiles;
    }

    public Integer getIncapacidadInhabiles() {
        return incapacidadInhabiles;
    }

    public void setIncapacidadInhabiles(Integer incapacidadInhabiles) {
        this.incapacidadInhabiles = incapacidadInhabiles;
    }

    public Integer getOrdinarios() {
        return ordinarios;
    }

    public void setOrdinarios(Integer ordinarios) {
        this.ordinarios = ordinarios;
    }

    public Integer getDescanso() {
        return descanso;
    }

    public void setDescanso(Integer descanso) {
        this.descanso = descanso;
    }

    public Integer getYearPAnt() {
        return yearPAnt;
    }

    public void setYearPAnt(Integer yearPAnt) {
        this.yearPAnt = yearPAnt;
    }

    public Integer getDiasPAntUno() {
        return diasPAntUno;
    }

    public void setDiasPAntUno(Integer diasPAntUno) {
        this.diasPAntUno = diasPAntUno;
    }

    public Integer getDiasPAntDos() {
        return diasPAntDos;
    }

    public void setDiasPAntDos(Integer diasPAntDos) {
        this.diasPAntDos = diasPAntDos;
    }
    
}
