/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "empleadoshisto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpleadoHisto.findAll", query = "SELECT e FROM EmpleadoHisto e"),
    @NamedQuery(name = "EmpleadoHisto.findById", query = "SELECT e FROM EmpleadoHisto e WHERE e.id = :id"),
    @NamedQuery(name = "EmpleadoHisto.findByIdEmpleado", query = "SELECT e FROM EmpleadoHisto e WHERE e.idEmpleado = :idEmpleado"),
    @NamedQuery(name = "EmpleadoHisto.findByName", query = "SELECT e FROM EmpleadoHisto e WHERE e.name = :name"),
    @NamedQuery(name = "EmpleadoHisto.findByCode", query = "SELECT e FROM EmpleadoHisto e WHERE e.code = :code"),
    @NamedQuery(name = "EmpleadoHisto.findByIdStatus", query = "SELECT e FROM EmpleadoHisto e WHERE e.idStatus = :idStatus"),
    @NamedQuery(name = "EmpleadoHisto.findByIdTabulador", query = "SELECT e FROM EmpleadoHisto e WHERE e.idTabulador = :idTabulador"),
    @NamedQuery(name = "EmpleadoHisto.findByIdGrupo", query = "SELECT e FROM EmpleadoHisto e WHERE e.idGrupo = :idGrupo"),
    @NamedQuery(name = "EmpleadoHisto.findByIdDepartamento", query = "SELECT e FROM EmpleadoHisto e WHERE e.idDepartamento = :idDepartamento"),
    @NamedQuery(name = "EmpleadoHisto.findByIdSede", query = "SELECT e FROM EmpleadoHisto e WHERE e.idSede = :idSede"),
    @NamedQuery(name = "EmpleadoHisto.findByIdTipoAntiguedad", query = "SELECT e FROM EmpleadoHisto e WHERE e.idTipoAntiguedad = :idTipoAntiguedad"),
    @NamedQuery(name = "EmpleadoHisto.findByFechaAntiguedad", query = "SELECT e FROM EmpleadoHisto e WHERE e.fechaAntiguedad = :fechaAntiguedad"),
    @NamedQuery(name = "EmpleadoHisto.findByAntiguedad", query = "SELECT e FROM EmpleadoHisto e WHERE e.antiguedad = :antiguedad"),
    @NamedQuery(name = "EmpleadoHisto.findByFechaIngreso", query = "SELECT e FROM EmpleadoHisto e WHERE e.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "EmpleadoHisto.findByFechaBaja", query = "SELECT e FROM EmpleadoHisto e WHERE e.fechaBaja = :fechaBaja"),
    @NamedQuery(name = "EmpleadoHisto.findByEstimulosProductividad", query = "SELECT e FROM EmpleadoHisto e WHERE e.estimulosProductividad = :estimulosProductividad"),
    @NamedQuery(name = "EmpleadoHisto.findByYear", query = "SELECT e FROM EmpleadoHisto e WHERE e.year = :year"),
    @NamedQuery(name = "EmpleadoHisto.findByQuincena", query = "SELECT e FROM EmpleadoHisto e WHERE e.quincena = :quincena")})
public class EmpleadoHisto implements Serializable {
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
    private Short idStatus;
    @Column(name = "id_tabulador")
    private Integer idTabulador;
    @Column(name = "id_grupo")
    private Short idGrupo;
    @Column(name = "id_departamento")
    private Integer idDepartamento;
    @Column(name = "id_sede")
    private Short idSede;
    @Column(name = "id_tipo_antiguedad")
    private Short idTipoAntiguedad;
    @Column(name = "fecha_antiguedad")
    @Temporal(TemporalType.DATE)
    private Date fechaAntiguedad;
    @Size(max = 10)
    @Column(name = "antiguedad")
    private String antiguedad;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "fecha_baja")
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;
    @Column(name = "estimulos_productividad")
    private BigDecimal estimulosProductividad;
    
    @Column(name = "year")
    private Short year;
    @Column(name = "quincena")
    private Short quincena;

    public EmpleadoHisto() {
    }

    public EmpleadoHisto(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Short getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Short idStatus) {
        this.idStatus = idStatus;
    }

    public Integer getIdTabulador() {
        return idTabulador;
    }

    public void setIdTabulador(Integer idTabulador) {
        this.idTabulador = idTabulador;
    }

    public Short getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Short idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Short getIdSede() {
        return idSede;
    }

    public void setIdSede(Short idSede) {
        this.idSede = idSede;
    }

    public Short getIdTipoAntiguedad() {
        return idTipoAntiguedad;
    }

    public void setIdTipoAntiguedad(Short idTipoAntiguedad) {
        this.idTipoAntiguedad = idTipoAntiguedad;
    }

    public Date getFechaAntiguedad() {
        return fechaAntiguedad;
    }

    public void setFechaAntiguedad(Date fechaAntiguedad) {
        this.fechaAntiguedad = fechaAntiguedad;
    }

    public String getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(String antiguedad) {
        this.antiguedad = antiguedad;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public BigDecimal getEstimulosProductividad() {
        return estimulosProductividad;
    }

    public void setEstimulosProductividad(BigDecimal estimulosProductividad) {
        this.estimulosProductividad = estimulosProductividad;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Short getQuincena() {
        return quincena;
    }

    public void setQuincena(Short quincena) {
        this.quincena = quincena;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpleadoHisto)) {
            return false;
        }
        EmpleadoHisto other = (EmpleadoHisto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cimav.restrh.entities.EmpleadoHisto[ id=" + id + " ]";
    }
    
}
