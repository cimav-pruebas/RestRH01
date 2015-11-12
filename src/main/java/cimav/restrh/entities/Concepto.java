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

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_concepto")
    private Character idTipoConcepto;

    @Column(name = "id_tipo_movimiento")
    private Character idTipoMovimiento;    
        
    @Column(name = "suma")
    private Boolean suma;

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
    
}
