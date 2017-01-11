/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.money.MonetaryAmount;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
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
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount subTotal;
    @Column(name = "iva")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount iva;
    @Column(name = "importe")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount importe;
    
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
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaElaboracion;
    @Column(name = "fecha_Inicio")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaInicio;
    @Column(name = "fecha_termino")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaTermino;
    
    @Column(name = "monto_uno")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount montoUno;
    @Column(name = "monto_dos")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount montoDos;
    @Column(name = "monto_tres")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount montoTres;
    
    @Column(name = "id_moneda")
    private Short idMoneda;
    
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

    public MonetaryAmount getIva() {
        return iva;
    }

    public void setIva(MonetaryAmount iva) {
        this.iva = iva;
    }

    public MonetaryAmount getImporte() {
        return importe;
    }

    public void setImporte(MonetaryAmount importe) {
        this.importe = importe;
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

    public MonetaryAmount getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(MonetaryAmount subTotal) {
        this.subTotal = subTotal;
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

    public MonetaryAmount getMontoUno() {
        return montoUno;
    }

    public void setMontoUno(MonetaryAmount montoUno) {
        this.montoUno = montoUno;
    }

    public MonetaryAmount getMontoDos() {
        return montoDos;
    }

    public void setMontoDos(MonetaryAmount montoDos) {
        this.montoDos = montoDos;
    }

    public MonetaryAmount getMontoTres() {
        return montoTres;
    }

    public void setMontoTres(MonetaryAmount montoTres) {
        this.montoTres = montoTres;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Short getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(Short idMoneda) {
        this.idMoneda = idMoneda;
    }

    public JustificacionTipo getJustificacionTipo() {
        return justificacionTipo;
    }

    public void setJustificacionTipo(JustificacionTipo justificacionTipo) {
        this.justificacionTipo = justificacionTipo;
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
