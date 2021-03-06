/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.time.LocalDate;
import javax.money.MonetaryAmount;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
//@EntityListeners(HistoryEmpleadoEventListener.class)
@Table(name = "empleados", schema = "public")
@XmlRootElement(name = "empleado")
public class Empleado extends EmpleadoSuper implements Serializable {
    
    @Column(name = "consecutivo")
    private Integer consecutivo;
    
    @Size(max = 100)
    @Column(name = "curp")
    private String curp;
    
    @Size(max = 50)
    @Column(name = "rfc")
    private String rfc;
    
    @Size(max = 100)
    @Column(name = "imss")
    private String imss;
    
    @Column(name = "id_proyecto")
    private Short idProyecto;
    
    @Size(max = 40)
    @Column(name = "cuenta_banco")
    private String cuentaBanco;
    
    @Size(max = 40)
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    
    @Size(max = 40)
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    
    @Size(max = 40)
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "id_clinica")
    private Integer idClinica;
    
    @Column(name = "id_banco")
    private Short idBanco;
    
    @Column(name = "id_tipo_empleado")
    private Short idTipoEmpleado;
    
    @Column(name = "id_tipo_contrato")
    private Short idTipoContrato;
    
    @Column(name = "fecha_ingreso")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaIngreso;

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
//        System.out.println("SETFi " + this.fechaIngreso + " : " + fechaIngreso) ;//fechaIngreso.format(DateTimeFormatter.ISO_DATE));
        this.fechaIngreso = fechaIngreso;
    }
    
    @Column(name = "fecha_inicio_contrato")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaInicioContrato;
    
    @Column(name = "fecha_fin_contrato")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaFinContrato;
    
    @Column(name = "id_tipo_sni")
    private Short idTipoSni;
    
    @Size(max = 30)
    @Column(name = "num_sni")
    private String numSni;
    
    @Column(name = "fecha_sni")
    //@Temporal(TemporalType.DATE)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaSni;
      
    @XmlElement(name = "jefe")
    @JoinColumn(name = "id_jefe", referencedColumnName = "id")
    @ManyToOne
    private EmpleadoBase jefe;
    
    @Column(name = "fecha_nacimiento")
    //@Temporal(TemporalType.DATE)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaNacimiento;

    @Column(name = "id_sexo")
    private Integer idSexo;
    
    @Column(name = "id_edo_civil")
    private Integer idEdoCivil;
    
    @Column(name = "direccion_calle")
    private String direccionCalle;
    
    @Column(name = "direccion_colonia")
    private String direccionColonia;
    
    @Column(name = "direccion_cp")
    private String direccionCP;
    
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "email")
    private String emailPersonal;

    @Column(name = "pension_beneficiario")
    private String pensionBeneficiario;
    @Column(name = "pension_id_banco")
    private Short pensionIdBanco;
    @Column(name = "pension_cuenta")
    private String pensionCuenta;
    
    public Empleado() {
        super();
    }
    
    public Integer getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getImss() {
        return imss;
    }

    public void setImss(String imss) {
        this.imss = imss;
    }

    public Short getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Short idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getCuentaBanco() {
        return cuentaBanco;
    }

    public void setCuentaBanco(String cuentaBanco) {
        this.cuentaBanco = cuentaBanco;
    }

    public String getApellidoPaterno() {
        if (apellidoPaterno == null) {
            apellidoPaterno = "";
        }
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno.toUpperCase();
    }

    public String getApellidoMaterno() {
        if (apellidoMaterno == null) {
            apellidoMaterno = "";
        }
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno.toUpperCase();
    }

    public String getNombre() {
        if (nombre == null) {
            nombre = "";
        }
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase(); // WordUtils.capitalizeFully(nombre);
    }

    public Integer getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Integer idClinica) {
        this.idClinica = idClinica;
    }

    public Short getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Short idBanco) {
        this.idBanco = idBanco;
    }

    public Short getIdTipoEmpleado() {
        return idTipoEmpleado;
    }

    public void setIdTipoEmpleado(Short idTipoEmpleado) {
        this.idTipoEmpleado = idTipoEmpleado;
    }

    public Short getIdTipoContrato() {
        return idTipoContrato;
    }

    public void setIdTipoContrato(Short idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }


    public LocalDate getFechaInicioContrato() {
        return fechaInicioContrato;
    }

    public void setFechaInicioContrato(LocalDate fechaInicioContrato) {
        //this.fechaInicioContrato = fechaInicioContrato;
        this.fechaInicioContrato = fechaInicioContrato;// DateUtils.truncate(fechaInicioContrato, Calendar.DAY_OF_MONTH);
    }

    public LocalDate getFechaFinContrato() {
        return fechaFinContrato;
    }

    public void setFechaFinContrato(LocalDate fechaFinContrato) {
        //this.fechaFinContrato = fechaFinContrato;
        this.fechaFinContrato = fechaFinContrato; // DateUtils.truncate(fechaFinContrato, Calendar.DAY_OF_MONTH);
    }

    public Short getIdTipoSni() {
        return idTipoSni;
    }

    public void setIdTipoSni(Short idTipoSni) {
        this.idTipoSni = idTipoSni;
    }

    public String getNumSni() {
        return numSni;
    }

    public void setNumSni(String numSni) {
        this.numSni = numSni;
    }

    public LocalDate getFechaSni() {
        return fechaSni;
    }

    public void setFechaSni(LocalDate fechaSni) {
        //this.fechaSni = fechaSni;
        this.fechaSni = fechaSni; //DateUtils.truncate(fechaSni, Calendar.DAY_OF_MONTH);
    }

    public EmpleadoBase getJefe() {
        return jefe;
    }

    public void setJefe(EmpleadoBase jefe) {
        this.jefe = jefe;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        //this.fechaNacimiento = fechaNacimiento;
        this.fechaNacimiento = fechaNacimiento; //DateUtils.truncate(fechaNacimiento, Calendar.DAY_OF_MONTH);
    }

    public Integer getIdSexo() {
        return idSexo;
    }

    public void setIdSexo(Integer idSexo) {
        this.idSexo = idSexo;
    }

    public Integer getIdEdoCivil() {
        return idEdoCivil;
    }

    public void setIdEdoCivil(Integer idEdoCivil) {
        this.idEdoCivil = idEdoCivil;
    }

    public String getDireccionCalle() {
        return direccionCalle;
    }

    public void setDireccionCalle(String direccionCalle) {
        this.direccionCalle = direccionCalle;
    }

    public String getDireccionColonia() {
        return direccionColonia;
    }

    public void setDireccionColonia(String direccionColonia) {
        this.direccionColonia = direccionColonia;
    }

    public String getDireccionCP() {
        return direccionCP;
    }

    public void setDireccionCP(String direccionCP) {
        this.direccionCP = direccionCP;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmailPersonal() {
        return emailPersonal;
    }

    public void setEmailPersonal(String emailPersonal) {
        this.emailPersonal = emailPersonal;
    }

    public String getPensionBeneficiario() {
        return pensionBeneficiario;
    }

    public void setPensionBeneficiario(String pensionBeneficiario) {
        this.pensionBeneficiario = pensionBeneficiario;
    }

    public Short getPensionIdBanco() {
        return pensionIdBanco;
    }

    public void setPensionIdBanco(Short pensionIdBanco) {
        this.pensionIdBanco = pensionIdBanco;
    }

    public String getPensionCuenta() {
        return pensionCuenta;
    }

    public void setPensionCuenta(String pensionCuenta) {
        this.pensionCuenta = pensionCuenta;
    }

    }
