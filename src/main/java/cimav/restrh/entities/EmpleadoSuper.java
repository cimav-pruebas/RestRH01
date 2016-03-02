/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author juan.calderon
 */
@MappedSuperclass
public class EmpleadoSuper extends BaseEntity implements Serializable {

    @Column(name = "id_status")
    private Short idStatus;

    @Size(max = 300)
    @Column(name = "url_photo")
    private String urlPhoto;

    @Size(max = 60)
    @Column(name = "cuenta_cimav")
    private String cuentaCimav;

    @JoinColumn(name = "id_tabulador", referencedColumnName = "id")
    @ManyToOne
    private Tabulador nivel;

    @Column(name = "id_grupo")
    private Integer idGrupo;

    @XmlElement(name = "departamento")
    @JoinColumn(name = "id_departamento", referencedColumnName = "id")
    @ManyToOne
    private Departamento departamento;

    @Column(name = "id_sede")
    private Short idSede;

    @Column(name = "id_tipo_antiguedad")
    private Short idTipoAntiguedad;
    
    @Column(name = "fecha_antiguedad")
    @Temporal(TemporalType.DATE)
    private Date fechaAntiguedad;
    
    @Column(name = "estimulos_productividad")
    private Double estimulosProductividad;
    
    
    // TODO Si cumple años durante la quicena la PAnt es proporcional.
    // diasPAntUno corresponde a los dias con los años anteriores
    // diasPAntDos corresponde a los dias con los años nuevos
    @Column(name = "pant_years")
    private Integer pantYears;
    @Column(name = "pant_months")
    private Integer pantMonths;
    @Column(name = "pant_days_odd")
    private Integer pantDayOdd;
    @Column(name = "pant_days_even")
    private Integer pantDayEven;

    @Column(name = "pant_regimen_anterior") // 01/Oct/2015 punto de quiebre
    private Boolean pantRegimenAnterior;
    
//    @PostLoad
//    public void reduceJefe() {
//        // TODO Buscar best approach de reducción profundidad de Entidad en EmpleadoOld.Jefe
//        if (this.getJefe() != null) {
//            EmpleadoOld miniJefe = new EmpleadoOld();
//            miniJefe.setId(this.getJefe().getId());
//            miniJefe.setCode(this.getJefe().getCode());
//            miniJefe.setName(this.getJefe().getName());
//            miniJefe.setUrlPhoto(this.getJefe().getUrlPhoto());
//
//            this.setJefe(miniJefe);
//        }
//    }
    public EmpleadoSuper() {
        super();
    }

    public Short getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Short idStatus) {
        this.idStatus = idStatus;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getCuentaCimav() {
        return cuentaCimav;
    }

    public void setCuentaCimav(String cuentaCimav) {
        this.cuentaCimav = cuentaCimav;
    }

    public Tabulador getNivel() {
        return nivel;
    }

    public void setNivel(Tabulador nivel) {
        this.nivel = nivel;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Short getIdSede() {
        return idSede;
    }

    public void setIdSede(Short idSede) {
        this.idSede = idSede;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Date getFechaAntiguedad() {
        return fechaAntiguedad;
    }

    public void setFechaAntiguedad(Date fechaAntiguedad) {
        //this.fechaAntiguedad = fechaAntiguedad;
        this.fechaAntiguedad = DateUtils.truncate(fechaAntiguedad, Calendar.DAY_OF_MONTH);
    }

    public Double getEstimulosProductividad() {
        return estimulosProductividad;
    }

    public void setEstimulosProductividad(Double estimulosProductividad) {
        this.estimulosProductividad = estimulosProductividad;
    }

    public Integer getPantYears() {
        return pantYears;
    }

    public void setPantYears(Integer pantYears) {
        this.pantYears = pantYears;
    }

    public Integer getPantMonths() {
        return pantMonths;
    }

    public void setPantMonths(Integer pantMonths) {
        this.pantMonths = pantMonths;
    }

    public Integer getPantDayOdd() {
        return pantDayOdd;
    }

    public void setPantDayOdd(Integer pantDayOdd) {
        this.pantDayOdd = pantDayOdd;
    }

    public Integer getPantDayEven() {
        return pantDayEven;
    }

    public void setPantDayEven(Integer pantDayEven) {
        this.pantDayEven = pantDayEven;
    }

    public Short getIdTipoAntiguedad() {
        return idTipoAntiguedad;
    }

    public void setIdTipoAntiguedad(Short idTipoAntiguedad) {
        this.idTipoAntiguedad = idTipoAntiguedad;
    }

//    public void initAntiguedad() {
//        /*
//        Inicializa la AntigÃ¼edad del Empleado.
//        Dias ordinarios, descanso, trabajados de la quincena
//        Sdi del bimestre para el empleado
//        */
//        
//        LocalDate localDateFinQuincena = Quincena.convert(new Date());//quincena.getFechaFin());
//        boolean isCYT = this.getIdGrupo().equals(EGrupo.CYT.getId());
//        boolean isAYA = this.getIdGrupo().equals(EGrupo.AYA.getId());
//        if (isCYT || isAYA) {
//
//            LocalDate localDateFechaAntiguedad = Quincena.convert(this.getFechaAntiguedad());
//
//            logger.log(Level.INFO, this.getId() + " | " + this.getName() 
//                    + " | " + this.getNivel() + " | " + this.getFechaAntiguedad() + " | " + localDateFechaAntiguedad);
//            // TODO Para cuando la PAnt se cumpla en la quincena, no consideramos incidencias (faltas e incapacidades);
//            // se debe corregir.
//
//            //TODO BUG Muy Lento y problema con JodaTime Resource not found: "org/joda/time/tz/data/ZoneInfoMap"
//            // TODO Checar que incluya el dia Inicial.
//            // Se da por hecho q nadie cumple el 28, 29 o 31 
//            // Se calculan los aÃ±os en base al Ãºltimo dÃ­a de la quincena 
//
//            Period period = Period.between(localDateFechaAntiguedad, localDateFinQuincena);
//
//            this.setPantYears(period.getYears());
//            this.setPantMonths(period.getMonths());
//            this.setPantDayOdd(period.getDays());
//            this.setPantDayEven(0); // TODO PAnt Odd|Even
//        }
//    }

    public Boolean getPantRegimenAnterior() {
        return pantRegimenAnterior;
    }

    public void setPantRegimenAnterior(Boolean pantRegimenAnterior) {
        this.pantRegimenAnterior = pantRegimenAnterior;
    }
    
}
