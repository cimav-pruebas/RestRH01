/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import javax.money.MonetaryAmount;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "nominahisto", catalog = "rh_development", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NominaHisto.findAll", query = "SELECT n FROM NominaHisto n"),
    @NamedQuery(name = "NominaHisto.findById", query = "SELECT n FROM NominaHisto n WHERE n.id = :id"),
    @NamedQuery(name = "NominaHisto.findByIdEmpleado", query = "SELECT n FROM NominaHisto n WHERE n.idEmpleado = :idEmpleado"),
    @NamedQuery(name = "NominaHisto.findByIncapacidadHabiles", query = "SELECT n FROM NominaHisto n WHERE n.incapacidadHabiles = :incapacidadHabiles"),
    @NamedQuery(name = "NominaHisto.findByIncapacidadInhabiles", query = "SELECT n FROM NominaHisto n WHERE n.incapacidadInhabiles = :incapacidadInhabiles"),
    @NamedQuery(name = "NominaHisto.findByOrdinarios", query = "SELECT n FROM NominaHisto n WHERE n.ordinarios = :ordinarios"),
    @NamedQuery(name = "NominaHisto.findByDescanso", query = "SELECT n FROM NominaHisto n WHERE n.descanso = :descanso"),
    @NamedQuery(name = "NominaHisto.findByFaltas", query = "SELECT n FROM NominaHisto n WHERE n.faltas = :faltas"),
    @NamedQuery(name = "NominaHisto.findBySdiVariableBimestreAnterior", query = "SELECT n FROM NominaHisto n WHERE n.sdiVariableBimestreAnterior = :sdiVariableBimestreAnterior"),
    @NamedQuery(name = "NominaHisto.findByHorasExtrasDobles", query = "SELECT n FROM NominaHisto n WHERE n.horasExtrasDobles = :horasExtrasDobles"),
    @NamedQuery(name = "NominaHisto.findByHorasExtrasTriples", query = "SELECT n FROM NominaHisto n WHERE n.horasExtrasTriples = :horasExtrasTriples"),
    @NamedQuery(name = "NominaHisto.findByYear", query = "SELECT n FROM NominaHisto n WHERE n.year = :year"),
    @NamedQuery(name = "NominaHisto.findByQuincena", query = "SELECT n FROM NominaHisto n WHERE n.quincena = :quincena")})
public class NominaHisto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "id_empleado")
    private Integer idEmpleado;
    // Conexion con el EmpleadoNomina
    @JoinColumn(name = "id_empleado", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne
    private EmpleadoNominaHisto empleadoNominaHisto; /* Read Only */
    
    @Column(name = "incapacidad_habiles")
    private Short incapacidadHabiles;
    @Column(name = "incapacidad_inhabiles")
    private Short incapacidadInhabiles;
    @Column(name = "ordinarios")
    private Short ordinarios;
    @Column(name = "descanso")
    private Short descanso;
    @Column(name = "faltas")
    private Short faltas;
    @Column(name = "sdi_variable_bimestre_anterior")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount sdiVariableBimestreAnterior;
    @Column(name = "horas_extras_dobles")
    private Double horasExtrasDobles;
    @Column(name = "horas_extras_triples")
    private Double horasExtrasTriples;

    @Column(name = "year")
    private Short year;
    @Column(name = "quincena")
    private Short quincena;

    public NominaHisto() {
    }

    public NominaHisto(Integer id) {
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

    public Short getIncapacidadHabiles() {
        return incapacidadHabiles;
    }

    public void setIncapacidadHabiles(Short incapacidadHabiles) {
        this.incapacidadHabiles = incapacidadHabiles;
    }

    public Short getIncapacidadInhabiles() {
        return incapacidadInhabiles;
    }

    public void setIncapacidadInhabiles(Short incapacidadInhabiles) {
        this.incapacidadInhabiles = incapacidadInhabiles;
    }

    public Short getOrdinarios() {
        return ordinarios;
    }

    public void setOrdinarios(Short ordinarios) {
        this.ordinarios = ordinarios;
    }

    public Short getDescanso() {
        return descanso;
    }

    public void setDescanso(Short descanso) {
        this.descanso = descanso;
    }

    public Short getFaltas() {
        return faltas;
    }

    public void setFaltas(Short faltas) {
        this.faltas = faltas;
    }

    public MonetaryAmount getSdiVariableBimestreAnterior() {
        return sdiVariableBimestreAnterior;
    }

    public void setSdiVariableBimestreAnterior(MonetaryAmount sdiVariableBimestreAnterior) {
        this.sdiVariableBimestreAnterior = sdiVariableBimestreAnterior;
    }

    public Double getHorasExtrasDobles() {
        return horasExtrasDobles;
    }

    public void setHorasExtrasDobles(Double horasExtrasDobles) {
        this.horasExtrasDobles = horasExtrasDobles;
    }

    public Double getHorasExtrasTriples() {
        return horasExtrasTriples;
    }

    public void setHorasExtrasTriples(Double horasExtrasTriples) {
        this.horasExtrasTriples = horasExtrasTriples;
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
        if (!(object instanceof NominaHisto)) {
            return false;
        }
        NominaHisto other = (NominaHisto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cimav.restrh.entities.NominaHisto[ id=" + id + " ]";
    }
    
}
