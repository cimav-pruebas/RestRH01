/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

/**
 *
 * @author calderon
 */
@MappedSuperclass
public class Plaza extends BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "id_empleado")
    private Integer idEmpleado;
    
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 10)
    @Column(name = "code")
    private String code;
    @Column(name = "id_status")
    private Integer idStatus;
    @Column(name = "nivel_code")
    private String nivelCode;
    @Column(name = "id_grupo")
    private Integer idGrupo;
    @Column(name = "id_departamento")
    private Integer idDepartamento;
    @Column(name = "id_sede")
    private Integer idSede;
    @Column(name = "id_tipo_antiguedad")
    private Integer idTipoAntiguedad;
    @Column(name = "fecha_antiguedad")
    //@Temporal(TemporalType.DATE)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaAntiguedad;
    @Column(name = "antiguedad")
    private String antiguedad;
    @Column(name = "fecha_ingreso")
    //@Temporal(TemporalType.DATE)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaIngreso;
    @Column(name = "fecha_baja")
    //@Temporal(TemporalType.DATE)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaBaja;
    @Column(name = "estimulos_productividad")
    private Double estimulosProductividad;
    
    public Plaza() {
    }

    public Plaza(Integer id) {
        this.id = id;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public String getNivelCode() {
        return nivelCode;
    }

    public void setNivelCode(String nivelCode) {
        this.nivelCode = nivelCode;
    }
    
    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public Integer getIdTipoAntiguedad() {
        return idTipoAntiguedad;
    }

    public void setIdTipoAntiguedad(Integer idTipoAntiguedad) {
        this.idTipoAntiguedad = idTipoAntiguedad;
    }

    public LocalDate getFechaAntiguedad() {
        return fechaAntiguedad;
    }

    public void setFechaAntiguedad(LocalDate fechaAntiguedad) {
        this.fechaAntiguedad = fechaAntiguedad;
    }

    public String getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(String antiguedad) {
        this.antiguedad = antiguedad;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Double getEstimulosProductividad() {
        return estimulosProductividad;
    }

    public void setEstimulosProductividad(Double estimulosProductividad) {
        this.estimulosProductividad = estimulosProductividad;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Plaza other = (Plaza) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
