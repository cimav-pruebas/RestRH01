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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NamedQuery(name = "Concepto.findAll", query = "SELECT c FROM Concepto c"),
    @NamedQuery(name = "Concepto.findById", query = "SELECT c FROM Concepto c WHERE c.id = :id"),
    @NamedQuery(name = "Concepto.findByCode", query = "SELECT c FROM Concepto c WHERE c.code = :code"),
    @NamedQuery(name = "Concepto.findByName", query = "SELECT c FROM Concepto c WHERE c.name = :name"),
    @NamedQuery(name = "Concepto.findByTipoMvto", query = "SELECT c FROM Concepto c WHERE c.tipoMvto = :tipoMvto"),
    @NamedQuery(name = "Concepto.findByStatus", query = "SELECT c FROM Concepto c WHERE c.status = :status")})
public class Concepto extends BaseEntity implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_mvto")
    private Character tipoMvto;

    @Column(name = "status")
    private Short status;

    public Concepto() {
    }

    public Concepto(Integer id, String code, String name, Character tipoMvto) {
        super(id, code, name);
        this.tipoMvto = tipoMvto;
    }

    public Character getTipoMvto() {
        return tipoMvto;
    }

    public void setTipoMvto(Character tipoMvto) {
        this.tipoMvto = tipoMvto;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    
}
