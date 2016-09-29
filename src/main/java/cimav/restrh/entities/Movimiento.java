/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import org.javamoney.moneta.Money;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "movimientos", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movimiento.findBy_IdEmpleado_Concepto", query = "SELECT t FROM Movimiento t WHERE t.idEmpleado = :id_empleado AND t.concepto = :concepto")})
public class Movimiento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public final static Integer PAGO_QUINCENAL      = 0;
    public final static Integer PAGO_MENSUAL        = 1;
    public final static Integer PAGO_BIMESTRAL      = 2;
    public final static Integer PAGO_CARGO_SALDO    = 5;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    // No tiene Get/Set y No Insertable ni Updatable; es decir, es OnlyRead y sirve como mappedBy en EmpleadoNomina
    @JoinColumn(name = "id_empleado", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private EmpleadoBase empleadoBase;
    
    @Column(name = "id_empleado")
    private Integer idEmpleado;
    
    @JoinColumn(name = "id_concepto", referencedColumnName = "id")
    @ManyToOne
    private Concepto concepto;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount cantidad;
    
    // aliasCantidad (tipo BigDecimal) es alias de cantidad (tipo MonetaryaAmount)
    // porque el constructor usado en el query de NominaQuincenalFacadeREST.doFindByIds 
    // no entiende el tipo MonetaryAmount
    @Basic(optional = false)
    @Column(name = "cantidad", updatable = false, insertable = false)
    private BigDecimal aliasCantidad;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_quincenas")
    private short numQuincenas;

    @Basic(optional = false)
    @Column(name = "pago")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount pago;
    
    @Basic(optional = false)
    @Column(name = "saldo")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount saldo;
    
    @Basic(optional = false)
    @Column(name = "permanente")
    private Boolean permanente;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_empresa")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount cantidadEmpresa;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "periocidad")
    private Integer periocidad;
    
    public Movimiento() {
        idEmpleado = 0;
        numQuincenas = 1;
        cantidad = Money.of(0.00, "MXN");
        saldo = Money.of(0.00, "MXN");
        pago = Money.of(0.00, "MXN");
        permanente = false;
        cantidadEmpresa = Money.of(0.00, "MXN");
        periocidad = Movimiento.PAGO_QUINCENAL;
    }

    // constructor usado con aliasCantidad (en lugar de con cantidad)
    // porque el constructor usado en el query de MovimientoFacadeREST.doFindByIds 
    // no entiende el tipo MonetaryAmount
    public Movimiento(Integer idEmpleado, Concepto concepto, BigDecimal aliasCantidad) {
        this();
        this.idEmpleado = idEmpleado;
        this.concepto = concepto;
        this.cantidad = Money.of(aliasCantidad, "MXN");
        this.cantidadEmpresa = Money.of(aliasCantidad, "MXN");
    }
    public Movimiento(int idEmpleado, Concepto concepto, MonetaryAmount cantidad, MonetaryAmount cantidadEmpresa) {
        this();
        this.idEmpleado = idEmpleado;
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.cantidadEmpresa = cantidadEmpresa;
    }
    public Movimiento(Integer id) {
        this.id = id;
    }

    public Movimiento(Integer id, MonetaryAmount cantidad, short numQuincenas, MonetaryAmount cantidadEmpresa) {
        this.id = id;
        this.cantidad = cantidad;
        this.numQuincenas = numQuincenas;
        this.cantidadEmpresa = cantidadEmpresa;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MonetaryAmount getCantidad() {
        return cantidad;
    }

    public void setCantidad(MonetaryAmount cantidad) {
        this.cantidad = cantidad;
    }

//    public EmpleadoBase getEmpleadoBase() {
//        return empleadoBase;
//    }
//
//    public void setEmpleadoBase(EmpleadoBase empleadoBase) {
//        this.empleadoBase = empleadoBase;
//    }

//    public BigDecimal getAliasCantidad() {
//        return aliasCantidad;
//    }
//
//    public void setAliasCantidad(BigDecimal aliasCantidad) {
//        this.aliasCantidad = aliasCantidad;
//    }
    
    public short getNumQuincenas() {
        return numQuincenas;
    }

    public void setNumQuincenas(short numQuincenas) {
        this.numQuincenas = numQuincenas;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public MonetaryAmount getPago() {
        return pago;
    }

    public void setPago(MonetaryAmount pago) {
        this.pago = pago;
    }

    public MonetaryAmount getSaldo() {
        return saldo;
    }

    public void setSaldo(MonetaryAmount saldo) {
        this.saldo = saldo;
    }

    public Boolean getPermanente() {
        return permanente;
    }

    public void setPermanente(Boolean permanente) {
        this.permanente = permanente;
    }

    public BigDecimal getAliasCantidad() {
        return aliasCantidad;
    }

    public void setAliasCantidad(BigDecimal aliasCantidad) {
        this.aliasCantidad = aliasCantidad;
    }

    public MonetaryAmount getCantidadEmpresa() {
        return cantidadEmpresa;
    }

    public void setCantidadEmpresa(MonetaryAmount cantidadEmpresa) {
        this.cantidadEmpresa = cantidadEmpresa;
    }

    public Integer getPeriocidad() {
        return periocidad;
    }

    public void setPeriocidad(Integer periocidad) {
        this.periocidad = periocidad;
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
        if (!(object instanceof Movimiento)) {
            return false;
        }
        Movimiento other = (Movimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cimav.restrh.entities.Movimiento[ id=" + id + " ]";
    }
    
}
