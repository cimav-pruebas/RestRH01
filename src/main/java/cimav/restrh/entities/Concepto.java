/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "conceptos", catalog = "rh_development", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Concepto.findByCode", query = "SELECT t FROM Concepto t WHERE t.code = :code")})
public class Concepto extends BaseEntity implements Serializable {

    public static final char MOV_CALCULADO       = 'C';
    public static final char MOV_PAGO            = 'P';
    public static final char TIPO_PERCEPCION     = 'P';
    public static final char TIPO_DEDUCCION      = 'D';
    public static final char TIPO_INTERNO        = 'I';
    public static final char TIPO_REPERCUCION    = 'R';
    public static final Integer NO_INTEGRA          = 0;
    public static final Integer INTEGRA             = 1;
    public static final Integer INTEGRA_VARIADO     = 2;
    public static final Integer GRAVA               = 0;
    public static final Integer EXENTA              = 1;
    public static final Integer GRAVA_PARCIAL       = 2;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_concepto")
    private Character idTipoConcepto;

    @Column(name = "id_tipo_movimiento")
    private Character idTipoMovimiento;    
        
    @Column(name = "suma")
    private Boolean suma;

    @Column(name = "grava")
    private Integer grava;
    @Column(name = "integra")
    private Integer integra;
    
    public Concepto() {
        super();
    }

    public Character getIdTipoConcepto() {
        return idTipoConcepto;
    }

    public void setIdTipoConcepto(Character idTipoConcepto) {
        this.idTipoConcepto = idTipoConcepto;
    }
    
    public Character getIdTipoMovimiento() {
        return idTipoMovimiento;
    }

    public void setIdTipoMovimiento(Character idTipoMovimiento) {
        this.idTipoMovimiento = idTipoMovimiento;
    }

    public Boolean getSuma() {
        return suma;
    }

    public void setSuma(Boolean suma) {
        this.suma = suma;
    }

    public Integer getGrava() {
        return grava;
    }

    public void setGrava(Integer grava) {
        this.grava = grava;
    }

    public Integer getIntegra() {
        return integra;
    }

    public void setIntegra(Integer integra) {
        this.integra = integra;
    }

    
}
