/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
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

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "nominaquincenal", catalog = "rh_development", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NominaQuincenal.findBy_IdEmpleado_Concepto", query = "SELECT t FROM NominaQuincenal t WHERE t.idEmpleado = :id_empleado AND t.concepto = :concepto")})
public class NominaQuincenal implements Serializable {
    
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
    
    @JoinColumn(name = "id_concepto", referencedColumnName = "id")
    @ManyToOne
    private Concepto concepto;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_quincenas")
    private short numQuincenas;

    @Basic(optional = false)
    @Column(name = "pago")
    private BigDecimal pago;
    
    @Basic(optional = false)
    @Column(name = "saldo")
    private BigDecimal saldo;
    
    @Basic(optional = false)
    @Column(name = "permanente")
    private Boolean permanente;
    
    public NominaQuincenal() {
        idEmpleado = 0;
        numQuincenas = 1;
        cantidad = BigDecimal.ZERO;
        saldo = BigDecimal.ZERO;
        pago = BigDecimal.ZERO;
        permanente = false;
    }

    public NominaQuincenal(int idEmpleado, Concepto concepto, BigDecimal cantidad) {
        this();
        this.idEmpleado = idEmpleado;
        this.concepto = concepto;
        this.cantidad = cantidad;
    }
    public NominaQuincenal(Integer id) {
        this.id = id;
    }

    public NominaQuincenal(Integer id, BigDecimal cantidad, short numQuincenas) {
        this.id = id;
        this.cantidad = cantidad;
        this.numQuincenas = numQuincenas;
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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

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

    public BigDecimal getPago() {
        return pago;
    }

    public void setPago(BigDecimal pago) {
        this.pago = pago;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Boolean getPermanente() {
        return permanente;
    }

    public void setPermanente(Boolean permanente) {
        this.permanente = permanente;
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
        if (!(object instanceof NominaQuincenal)) {
            return false;
        }
        NominaQuincenal other = (NominaQuincenal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cimav.restrh.entities.NominaQuincenal[ id=" + id + " ]";
    }
    
}
