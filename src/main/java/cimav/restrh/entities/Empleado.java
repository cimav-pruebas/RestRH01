/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "empleados", catalog = "rh_development", schema = "public")
@XmlRootElement(name = "empleado")
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e"),
    @NamedQuery(name = "Empleado.findById", query = "SELECT e FROM Empleado e WHERE e.id = :id"),
    @NamedQuery(name = "Empleado.findByCode", query = "SELECT e FROM Empleado e WHERE e.code = :code"),
    @NamedQuery(name = "Empleado.findByConsecutivo", query = "SELECT e FROM Empleado e WHERE e.consecutivo = :consecutivo"),
    @NamedQuery(name = "Empleado.findByStatus", query = "SELECT e FROM Empleado e WHERE e.status = :status"),
    @NamedQuery(name = "Empleado.findByCurp", query = "SELECT e FROM Empleado e WHERE e.curp = :curp"),
    @NamedQuery(name = "Empleado.findByRfc", query = "SELECT e FROM Empleado e WHERE e.rfc = :rfc"),
    @NamedQuery(name = "Empleado.findByImss", query = "SELECT e FROM Empleado e WHERE e.imss = :imss"),
    @NamedQuery(name = "Empleado.findByIdProyecto", query = "SELECT e FROM Empleado e WHERE e.idProyecto = :idProyecto"),
    @NamedQuery(name = "Empleado.findByCuentaBanco", query = "SELECT e FROM Empleado e WHERE e.cuentaBanco = :cuentaBanco"),
    @NamedQuery(name = "Empleado.findByUrlPhoto", query = "SELECT e FROM Empleado e WHERE e.urlPhoto = :urlPhoto"),
    @NamedQuery(name = "Empleado.findByName", query = "SELECT e FROM Empleado e WHERE e.name = :name"),
    @NamedQuery(name = "Empleado.findByApellidoPaterno", query = "SELECT e FROM Empleado e WHERE e.apellidoPaterno = :apellidoPaterno"),
    @NamedQuery(name = "Empleado.findByApellidoMaterno", query = "SELECT e FROM Empleado e WHERE e.apellidoMaterno = :apellidoMaterno"),
    @NamedQuery(name = "Empleado.findByNombre", query = "SELECT e FROM Empleado e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Empleado.findByIdClinica", query = "SELECT e FROM Empleado e WHERE e.idClinica = :idClinica"),
    @NamedQuery(name = "Empleado.findByCuentaCimav", query = "SELECT e FROM Empleado e WHERE e.cuentaCimav = :cuentaCimav"),
    @NamedQuery(name = "Empleado.findByIdBanco", query = "SELECT e FROM Empleado e WHERE e.idBanco = :idBanco"),
    @NamedQuery(name = "Empleado.findByIdSede", query = "SELECT e FROM Empleado e WHERE e.idSede = :idSede"),
    @NamedQuery(name = "Empleado.findByIdTipoEmpleado", query = "SELECT e FROM Empleado e WHERE e.idTipoEmpleado = :idTipoEmpleado"),
    @NamedQuery(name = "Empleado.findByIdTipoContrato", query = "SELECT e FROM Empleado e WHERE e.idTipoContrato = :idTipoContrato"),
    @NamedQuery(name = "Empleado.findByFechaIngreso", query = "SELECT e FROM Empleado e WHERE e.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "Empleado.findByFechaInicioContrato", query = "SELECT e FROM Empleado e WHERE e.fechaInicioContrato = :fechaInicioContrato"),
    @NamedQuery(name = "Empleado.findByFechaFinContrato", query = "SELECT e FROM Empleado e WHERE e.fechaFinContrato = :fechaFinContrato"),
    @NamedQuery(name = "Empleado.findByFechaBaja", query = "SELECT e FROM Empleado e WHERE e.fechaBaja = :fechaBaja"),
    @NamedQuery(name = "Empleado.findByIdTipoAntiguedad", query = "SELECT e FROM Empleado e WHERE e.idTipoAntiguedad = :idTipoAntiguedad"),
    @NamedQuery(name = "Empleado.findByFechaAntiguedad", query = "SELECT e FROM Empleado e WHERE e.fechaAntiguedad = :fechaAntiguedad"),
    @NamedQuery(name = "Empleado.findByIdTipoSni", query = "SELECT e FROM Empleado e WHERE e.idTipoSni = :idTipoSni"),
    @NamedQuery(name = "Empleado.findByNumSni", query = "SELECT e FROM Empleado e WHERE e.numSni = :numSni"),
    @NamedQuery(name = "Empleado.findByFechaSni", query = "SELECT e FROM Empleado e WHERE e.fechaSni = :fechaSni")})
public class Empleado extends BaseEntity implements Serializable {
    
    @Column(name = "consecutivo")
    private Integer consecutivo;
    
    @Column(name = "status")
    private Short status;
    
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
    
    @Size(max = 300)
    @Column(name = "url_photo")
    private String urlPhoto;
    
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
    
    @Size(max = 60)
    @Column(name = "cuenta_cimav")
    private String cuentaCimav;
    
    @Column(name = "id_banco")
    private Short idBanco;
    
    @Column(name = "id_sede")
    private Short idSede;
    
    @Column(name = "id_tipo_empleado")
    private Short idTipoEmpleado;
    
    @Column(name = "id_tipo_contrato")
    private Short idTipoContrato;
    
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;

    @Column(name = "fecha_inicio_contrato")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioContrato;
    
    @Column(name = "fecha_fin_contrato")
    @Temporal(TemporalType.DATE)
    private Date fechaFinContrato;
    
    @Column(name = "fecha_baja")
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;
    
    @Column(name = "id_tipo_antiguedad")
    private Short idTipoAntiguedad;
    
    @Column(name = "fecha_antiguedad")
    @Temporal(TemporalType.DATE)
    private Date fechaAntiguedad;
    
    @Column(name = "id_tipo_sni")
    private Short idTipoSni;
    
    @Size(max = 30)
    @Column(name = "num_sni")
    private String numSni;
    
    @Column(name = "fecha_sni")
    @Temporal(TemporalType.DATE)
    private Date fechaSni;
    
    @JoinColumn(name = "id_tabulador", referencedColumnName = "id")
    @ManyToOne
    private Tabulador nivel;
    
    @JoinColumn(name = "id_grupo", referencedColumnName = "id")
    @ManyToOne
    private Grupo grupo;
    
    @OneToMany(mappedBy = "jefe")
    private Collection<Empleado> jefeCollection;
    
    @XmlElement(name = "jefe")
    @JoinColumn(name = "id_jefe", referencedColumnName = "id")
    @ManyToOne
    private Empleado jefe;
    
    @XmlElement(name = "departamento")
    @JoinColumn(name = "id_departamento", referencedColumnName = "id")
    @ManyToOne
    private Departamento departamento;
    
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

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
    
    @OneToMany(mappedBy = "empleado")
    private Collection<NominaQuincenal> nominaQuincenalCollection;

    
    @PostLoad
    public void reduceJefe() {
        // TODO Buscar best approach de reducción profundidad de Entidad en Empleado.Jefe
        if (this.getJefe() != null) {
            Empleado miniJefe = new Empleado();
            miniJefe.setId(this.getJefe().getId());
            miniJefe.setCode(this.getJefe().getCode());
            miniJefe.setName(this.getJefe().getName());
            miniJefe.setUrlPhoto(this.getJefe().getUrlPhoto());

            this.setJefe(miniJefe);
        }
    }

    public Empleado() {
        super();
    }

    public Empleado(Integer id, String code, String name, String cuentaCimav) {
        super();
        this.setId(id);
        this.setCode(code);
        this.setName(name);
        this.setCuentaCimav(cuentaCimav);
    }
    
    public Integer getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
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

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
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
        this.nombre = WordUtils.capitalizeFully(nombre);
    }

    public Integer getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Integer idClinica) {
        this.idClinica = idClinica;
    }

    public String getCuentaCimav() {
        return cuentaCimav;
    }

    public void setCuentaCimav(String cuentaCimav) {
        this.cuentaCimav = cuentaCimav;
    }

    public Short getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Short idBanco) {
        this.idBanco = idBanco;
    }

    public Short getIdSede() {
        return idSede;
    }

    public void setIdSede(Short idSede) {
        this.idSede = idSede;
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

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaInicioContrato() {
        return fechaInicioContrato;
    }

    public void setFechaInicioContrato(Date fechaInicioContrato) {
        this.fechaInicioContrato = fechaInicioContrato;
    }

    public Date getFechaFinContrato() {
        return fechaFinContrato;
    }

    public void setFechaFinContrato(Date fechaFinContrato) {
        this.fechaFinContrato = fechaFinContrato;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
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

    public Date getFechaSni() {
        return fechaSni;
    }

    public void setFechaSni(Date fechaSni) {
        this.fechaSni = fechaSni;
    }

    public Tabulador getNivel() {
        return nivel;
    }

    public void setNivel(Tabulador nivel) {
        this.nivel = nivel;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @XmlTransient
    public Collection<Empleado> getJefeCollection() {
        return jefeCollection;
    }

    public void setJefeCollection(Collection<Empleado> jefeCollection) {
        this.jefeCollection = jefeCollection;
    }

    public Empleado getJefe() {
        return jefe;
    }

    public void setJefe(Empleado jefe) {
        this.jefe = jefe;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    @XmlTransient
    public Collection<NominaQuincenal> getNominaQuincenalCollection() {
        return nominaQuincenalCollection;
    }

    public void setNominaQuincenalCollection(Collection<NominaQuincenal> nominaQuincenalCollection) {
        this.nominaQuincenalCollection = nominaQuincenalCollection;
    }

}
