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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author calderon
 */
/*
@Entity
@Cacheable(false)
@Table(name = "justificaciones", schema = "public")
@XmlRootElement
*/
@MappedSuperclass
public class JustificacionRef implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    //@XmlElement(name = "empleado")
    @JoinColumn(name = "id_empleado", referencedColumnName = "id")
    @ManyToOne
    private EmpleadoBase empleado;

    @XmlElement(name = "elabora")
    @JoinColumn(name = "id_empleado_elaboro", referencedColumnName = "id")
    @ManyToOne
    private EmpleadoBase elabora;

    @XmlElement(name = "autoriza")
    @JoinColumn(name = "id_empleado_autorizo", referencedColumnName = "id")
    @ManyToOne
    private EmpleadoBase autoriza;

    @XmlElement(name = "tipo")
    @JoinColumn(name = "id_tipo", referencedColumnName = "id")
    @ManyToOne
    private JustificacionTipo justificacionTipo;

    @XmlElement(name = "moneda")
    @JoinColumn(name = "id_moneda", referencedColumnName = "id")
    @ManyToOne
    private Moneda moneda;
    
    @Column(name = "requisicion")
    private String requisicion;
    @Column(name = "proyecto")
    private String proyecto;
    @Column(name = "proveedoruno")
    private String proveedorUno;
    @Column(name = "proveedordos")
    private String proveedorDos;
    @Column(name = "proveedortres")
    private String proveedorTres;
    
    @Column(name = "bienoservicio")
    private Short bienOServicio;
    
    @Column(name = "subtotal")
    private Double subTotal;
    @Column(name = "iva")
    private Double iva;
    @Column(name = "importe")
    private Double importe;
    
    @Column(name = "condicionespago")
    private String condicionesPago;
    @Column(name = "datosbanco")
    private String datosBanco;
    @Column(name = "razoncompra")
    private String razonCompra;
    @Column(name = "terminosentrega")
    private String terminosEntrega;
    @Column(name = "plazoEntrega")
    private String plazoEntrega;


    @Column(name = "rfc")
    private String rfc;
    @Column(name = "curp")
    private String curp;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "email")
    private String correo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "domicilio")
    private String domicilio;

    @Column(name = "fecha_elaboracion")
    private LocalDate fechaElaboracion;
    @Column(name = "fecha_Inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_termino")
    private LocalDate fechaTermino;
    
    @Column(name = "monto_uno")
    private Double montoUno;
    @Column(name = "monto_dos")
    private Double montoDos;
    @Column(name = "monto_tres")
    private Double montoTres;
    
//    @Column(name = "id_moneda")
//    private Short idMoneda;
    
    @Column(name = "es_unico")
    private Boolean esUnico;    
    
    @Column(name = "plazo")
    private Short plazo;
    @Column(name = "num_dias_plazo")
    private Short numDiasPlazo;
    @Column(name = "num_pagos")
    private Short numPagos;
    @Column(name = "porcen_anticipo")
    private Short porcenAnticipo;
    @Column(name = "forma_pago")
    private String formaPago;
    @Column(name = "autoriza_cargo")
    private String autorizaCargo;
    
    public JustificacionRef() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EmpleadoBase getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoBase empleado) {
        this.empleado = empleado;
    }

    public EmpleadoBase getElabora() {
        return elabora;
    }

    public void setElabora(EmpleadoBase elabora) {
        this.elabora = elabora;
    }

    public EmpleadoBase getAutoriza() {
        return autoriza;
    }

    public void setAutoriza(EmpleadoBase autoriza) {
        this.autoriza = autoriza;
    }

    
    public String getRequisicion() {
        return requisicion;
    }

    public void setRequisicion(String requisicion) {
        this.requisicion = requisicion;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public String getProveedorUno() {
        return proveedorUno;
    }

    public void setProveedorUno(String proveedorUno) {
        this.proveedorUno = proveedorUno;
    }

    public String getProveedorDos() {
        return proveedorDos;
    }

    public void setProveedorDos(String proveedorDos) {
        this.proveedorDos = proveedorDos;
    }

    public String getProveedorTres() {
        return proveedorTres;
    }

    public void setProveedorTres(String proveedorTres) {
        this.proveedorTres = proveedorTres;
    }

    public Short getBienOServicio() {
        return bienOServicio;
    }

    public void setBienOServicio(Short bienOServicio) {
        this.bienOServicio = bienOServicio;
    }

    public String getCondicionesPago() {
        return condicionesPago;
    }

    public void setCondicionesPago(String condicionesPago) {
        this.condicionesPago = condicionesPago;
    }

    public String getDatosBanco() {
        return datosBanco;
    }

    public void setDatosBanco(String datosBanco) {
        this.datosBanco = datosBanco;
    }

    public String getRazonCompra() {
        return razonCompra;
    }

    public void setRazonCompra(String razonCompra) {
        this.razonCompra = razonCompra;
    }

    public String getTerminosEntrega() {
        return terminosEntrega;
    }

    public void setTerminosEntrega(String terminosEntrega) {
        this.terminosEntrega = terminosEntrega;
    }

    public String getPlazoEntrega() {
        return plazoEntrega;
    }

    public void setPlazoEntrega(String plazoEntrega) {
        this.plazoEntrega = plazoEntrega;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(LocalDate fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(LocalDate fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public JustificacionTipo getJustificacionTipo() {
        return justificacionTipo;
    }

    public void setJustificacionTipo(JustificacionTipo justificacionTipo) {
        this.justificacionTipo = justificacionTipo;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
    
    public Boolean getEsUnico() {
        return esUnico;
    }

    public void setEsUnico(Boolean esUnico) {
        this.esUnico = esUnico;
    }

    public Short getPlazo() {
        return plazo;
    }

    public void setPlazo(Short plazo) {
        this.plazo = plazo;
    }

    public Short getNumPagos() {
        if (numPagos <= 0) numPagos = 1;
        return numPagos;
    }

    public void setNumPagos(Short numPagos) {
        this.numPagos = numPagos;
    }

    public Short getPorcenAnticipo() {
        return porcenAnticipo;
    }

    public void setPorcenAnticipo(Short porcenAnticipo) {
        this.porcenAnticipo = porcenAnticipo;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getAutorizaCargo() {
        return autorizaCargo;
    }

    public void setAutorizaCargo(String autorizaCargo) {
        this.autorizaCargo = autorizaCargo;
    }

    public Short getNumDiasPlazo() {
        return numDiasPlazo;
    }

    public void setNumDiasPlazo(Short numDiasPlazo) {
        this.numDiasPlazo = numDiasPlazo;
    }

    public Double getSubTotal() {
        if (subTotal < 0.00) subTotal = 0.00;
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getIva() {
        if (iva < 0.00) iva = 0.00;
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getImporte() {
        if (importe < 0.00) importe = 0.00;
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Double getMontoUno() {
        if (montoUno < 0.00) montoUno = 0.00;
        return montoUno;
    }

    public void setMontoUno(Double montoUno) {
        this.montoUno = montoUno;
    }

    public Double getMontoDos() {
        return montoDos;
    }

    public void setMontoDos(Double montoDos) {
        if (montoDos < 0.00) montoDos = 0.00;
        this.montoDos = montoDos;
    }

    public Double getMontoTres() {
        return montoTres;
    }

    public void setMontoTres(Double montoTres) {
        if (montoTres < 0.00) montoTres = 0.00;
        this.montoTres = montoTres;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final JustificacionRef other = (JustificacionRef) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "justificacion{" + "id=" + id + ", empleado=" + empleado + ", elabora=" + elabora + ", autoriza=" + autoriza + '}';
    }

    
}
