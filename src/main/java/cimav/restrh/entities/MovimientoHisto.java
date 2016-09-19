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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "movimientoshisto", catalog = "rh_development", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimientoHisto.findAll", query = "SELECT m FROM MovimientoHisto m"),
    @NamedQuery(name = "MovimientoHisto.findById", query = "SELECT m FROM MovimientoHisto m WHERE m.id = :id"),
    @NamedQuery(name = "MovimientoHisto.findByIdEmpleado", query = "SELECT m FROM MovimientoHisto m WHERE m.idEmpleado = :idEmpleado"),
    @NamedQuery(name = "MovimientoHisto.findByCantidad", query = "SELECT m FROM MovimientoHisto m WHERE m.cantidad = :cantidad"),
    @NamedQuery(name = "MovimientoHisto.findByNumQuincenas", query = "SELECT m FROM MovimientoHisto m WHERE m.numQuincenas = :numQuincenas"),
    @NamedQuery(name = "MovimientoHisto.findByPago", query = "SELECT m FROM MovimientoHisto m WHERE m.pago = :pago"),
    @NamedQuery(name = "MovimientoHisto.findBySaldo", query = "SELECT m FROM MovimientoHisto m WHERE m.saldo = :saldo"),
    @NamedQuery(name = "MovimientoHisto.findByPermanente", query = "SELECT m FROM MovimientoHisto m WHERE m.permanente = :permanente"),
    @NamedQuery(name = "MovimientoHisto.findByCantidadEmpresa", query = "SELECT m FROM MovimientoHisto m WHERE m.cantidadEmpresa = :cantidadEmpresa"),
    @NamedQuery(name = "MovimientoHisto.findByYear", query = "SELECT m FROM MovimientoHisto m WHERE m.year = :year"),
    @NamedQuery(name = "MovimientoHisto.findByQuincena", query = "SELECT m FROM MovimientoHisto m WHERE m.quincena = :quincena")})
public class MovimientoHisto implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
    
    //@Column(name = "id_concepto")
    //private Integer idConcepto;
    @XmlElement(name = "concepto")
    @JoinColumn(name = "id_concepto", referencedColumnName = "id")
    @ManyToOne
    private Concepto concepto;
    
    @Column(name = "cantidad")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount cantidad;
    @Column(name = "num_quincenas")
    private Short numQuincenas;
    @Column(name = "pago")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount pago;
    @Column(name = "saldo")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount saldo;
    @Column(name = "permanente")
    private Boolean permanente;
    @Column(name = "cantidad_empresa")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount cantidadEmpresa;

    @Column(name = "year")
    private Short year;
    @Column(name = "quincena")
    private Short quincena;

    @Column(name = "id_movimiento")
    private Integer idMovimiento;
    
    public MovimientoHisto() {
    }

    public MovimientoHisto(Integer id) {
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

//    public Integer getIdConcepto() {
//        return idConcepto;
//    }
//
//    public void setIdConcepto(Integer idConcepto) {
//        this.idConcepto = idConcepto;
//    }

    public MonetaryAmount getCantidad() {
        return cantidad;
    }

    public void setCantidad(MonetaryAmount cantidad) {
        this.cantidad = cantidad;
    }

    public Short getNumQuincenas() {
        return numQuincenas;
    }

    public void setNumQuincenas(Short numQuincenas) {
        this.numQuincenas = numQuincenas;
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

    public MonetaryAmount getCantidadEmpresa() {
        return cantidadEmpresa;
    }

    public void setCantidadEmpresa(MonetaryAmount cantidadEmpresa) {
        this.cantidadEmpresa = cantidadEmpresa;
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

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
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
        if (!(object instanceof MovimientoHisto)) {
            return false;
        }
        MovimientoHisto other = (MovimientoHisto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cimav.restrh.entities.MovimientoHisto[ id=" + id + " ]";
    }
    
}
