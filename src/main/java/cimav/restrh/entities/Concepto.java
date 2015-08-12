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
public class Concepto extends BaseEntity implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_concepto")
    private Character idTipoConcepto;

    @Column(name = "status")
    private Short status;

    @Column(name = "id_tipo_movimiento")
    private Character idTipoMovimiento;    
        
    public Concepto() {
        super();
    }

    public Character getIdTipoConcepto() {
        return idTipoConcepto;
    }

    public void setIdTipoConcepto(Character idTipoConcepto) {
        this.idTipoConcepto = idTipoConcepto;
    }
    
    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Character getIdTipoMovimiento() {
        return idTipoMovimiento;
    }

    public void setIdTipoMovimiento(Character idTipoMovimiento) {
        this.idTipoMovimiento = idTipoMovimiento;
    }
    
}
