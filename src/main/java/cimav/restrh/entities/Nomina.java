/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import cimav.restrh.services.CalculoREST;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import org.javamoney.moneta.Money;

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
@Table(name = "nomina", catalog = "rh_development", schema = "public")
@XmlRootElement(name = "nomina")
public class Nomina implements Serializable {
    
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
    private EmpleadoNomina empleadoNomina; /* Read Only */
    
//    // Conexion con el EmpleadoSuper
//    @JoinColumn(name = "id_empleado", referencedColumnName = "id", insertable = false, updatable = false)
//    @OneToOne
//    private EmpleadoBase empleadoBase; /* Read Only */
    
    // Faltas, Incapacidades, Ordinarios y Descanso se modifican junto
    // a las Incidencias
    @Column(name = "faltas")
    private Integer faltas;
    @Column(name = "incapacidad_habiles")
    private Integer incapacidadHabiles;
    @Column(name = "incapacidad_inhabiles")
    private Integer incapacidadInhabiles;
    @Column(name = "ordinarios")
    private Integer ordinarios;
    @Column(name = "descanso")
    private Integer descanso;
    
    @Transient
    private Integer diasTrabajados;
    @Transient
    private Integer diasOrdinariosDeLaQuincena;
    @Transient
    private Integer diasDescansoDeLaQuincena;
    // TODO Falta Trasiente Asueto
    
    @Column(name = "sdi_variable_bimestre_anterior") 
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount sdiVariableBimestreAnterior;
    
    @OneToMany(mappedBy = "nomina", orphanRemoval = true)
    private Collection<Incidencia> incidencias;
    
    @OneToMany(mappedBy = "nomina", orphanRemoval = true)
    private Collection<HoraExtra> horasExtras;
    
    @Column(name = "horas_extras_dobles") 
    private Double horasExtrasDobles;
    @Column(name = "horas_extras_triples") 
    private Double horasExtrasTriples;
    
    public Nomina() {
        this.diasDescansoDeLaQuincena = 4;
        this.diasOrdinariosDeLaQuincena = 11;
        this.horasExtrasDobles = 0.00;
        this.horasExtrasTriples = 0.00;
        this.sdiVariableBimestreAnterior = Money.of(BigDecimal.ZERO, CalculoREST.MXN);
        // TODO Falta iniciarlizar el sdiVariableBimestreAnterior
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
        this.ordinarios = diasOrdinariosDeLaQuincena - faltas - incapacidadHabiles;
        return ordinarios;
    }

    public void setOrdinarios(Integer ordinarios) {
        this.ordinarios = ordinarios;
    }

    public Integer getDescanso() {
        this.descanso = this.diasDescansoDeLaQuincena - incapacidadInhabiles;
        return descanso;
    }

    public void setDescanso(Integer descanso) {
        this.descanso = descanso;
    }

    public Integer getDiasTrabajados() {
        // TODO faltan de asueto
        this.diasTrabajados = this.ordinarios + this.descanso;
        return this.diasTrabajados;
    }

    public void setDiasTrabajados(Integer diasTrabajados) {
        this.diasTrabajados = diasTrabajados;
    }

    public Integer getDiasOrdinariosDeLaQuincena() {
        return diasOrdinariosDeLaQuincena;
    }

    public void setDiasOrdinariosDeLaQuincena(Integer diasOrdinariosDeLaQuincena) {
        this.diasOrdinariosDeLaQuincena = diasOrdinariosDeLaQuincena;
    }

    public Integer getDiasDescansoDeLaQuincena() {
        return diasDescansoDeLaQuincena;
    }

    public void setDiasDescansoDeLaQuincena(Integer diasDescansoDeLaQuincena) {
        this.diasDescansoDeLaQuincena = diasDescansoDeLaQuincena;
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

    public Collection<HoraExtra> getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Collection<HoraExtra> horasExtras) {
        this.horasExtras = horasExtras;
    }

    public Collection<Incidencia> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(Collection<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }
    
    
}
