/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.time.LocalDate;
import javax.money.MonetaryAmount;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author juan.calderon
 */
@MappedSuperclass
public class EmpleadoSuper extends BaseEntity implements Serializable {

    @Column(name = "id_status")
    private Integer idStatus;

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
    private Integer idSede;

    @Column(name = "id_tipo_antiguedad")
    private Integer idTipoAntiguedad;
    
    @Column(name = "fecha_antiguedad")
    //@Temporal(TemporalType.DATE)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate fechaAntiguedad;
    
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

    @Column(name = "pant_regimen_anterior") // todo 01/Oct/2015 punto de quiebre
    private Boolean pantRegimenAnterior;

    @Column(name = "porcen_seg_separacion_ind") 
    private Double porcenSegSeparacionInd;
    
    @Column(name = "pension_tipo")
    private Integer pensionIdTipo;
    @Column(name = "pension_porcentaje")
    private Double pensionPorcen;
    @Column(name = "pension_cantidad_fija")
    @Convert(converter = MonetaryAmountConverter.class)
    private MonetaryAmount pensionCantidaFija;
    @Column(name = "pension_incluye_monedero")
    private Boolean pensionIncluyeMonedero;
    
    
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

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
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

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public LocalDate getFechaAntiguedad() {
        return fechaAntiguedad;
    }

    public void setFechaAntiguedad(LocalDate fechaAntiguedad) {
        //this.fechaAntiguedad = fechaAntiguedad;
        this.fechaAntiguedad = this.fechaAntiguedad; //DateUtils.truncate(fechaAntiguedad, Calendar.DAY_OF_MONTH);
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

    public Integer getIdTipoAntiguedad() {
        return idTipoAntiguedad;
    }

    public void setIdTipoAntiguedad(Integer idTipoAntiguedad) {
        this.idTipoAntiguedad = idTipoAntiguedad;
    }

    public Boolean getPantRegimenAnterior() {
        return pantRegimenAnterior;
    }

    public void setPantRegimenAnterior(Boolean pantRegimenAnterior) {
        this.pantRegimenAnterior = pantRegimenAnterior;
    }

    public Double getPorcenSegSeparacionInd() {
        return porcenSegSeparacionInd;
    }

    public void setPorcenSegSeparacionInd(Double porcenSegSeparacionInd) {
        this.porcenSegSeparacionInd = porcenSegSeparacionInd;
    }

    public Integer getPensionIdTipo() {
        return pensionIdTipo;
    }

    public void setPensionIdTipo(Integer pensionIdTipo) {
        this.pensionIdTipo = pensionIdTipo;
    }

    public Double getPensionPorcen() {
        return pensionPorcen;
    }

    public void setPensionPorcen(Double pensionPorcen) {
        this.pensionPorcen = pensionPorcen;
    }

    public MonetaryAmount getPensionCantidaFija() {
        return pensionCantidaFija;
    }

    public void setPensionCantidaFija(MonetaryAmount pensionCantidaFija) {
        this.pensionCantidaFija = pensionCantidaFija;
    }

    public Boolean getPensionIncluyeMonedero() {
        return pensionIncluyeMonedero;
    }

    public void setPensionIncluyeMonedero(Boolean pensionIncluyeMonedero) {
        this.pensionIncluyeMonedero = pensionIncluyeMonedero;
    }

}
