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
public class Concepto extends BaseEntity implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_mvto")
    private Character idTipoMvto;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_calculo")
    private Character idTipoCalculo;

    @Column(name = "status")
    private Short status;

    public Concepto() {
        super();
    }

    public Character getIdTipoMvto() {
        return idTipoMvto;
    }

    public void setIdTipoMvto(Character idTipoMvto) {
        this.idTipoMvto = idTipoMvto;
    }

    public Character getIdTipoCalculo() {
        return idTipoCalculo;
    }

    public void setIdTipoCalculo(Character idTipoCalculo) {
        this.idTipoCalculo = idTipoCalculo;
    }
    
    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
    
}
