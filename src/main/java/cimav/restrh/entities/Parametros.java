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
import javax.persistence.Table;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "parametros", catalog = "rh_development", schema = "public")
public class Parametros implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "quincena_actual")
    private Integer quincenaActual;
    @Column(name = "status_quincena")
    private Integer statusQuincena;

    public Parametros() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuincenaActual() {
        return quincenaActual;
    }

    public void setQuincenaActual(Integer quincenaActual) {
        this.quincenaActual = quincenaActual;
    }

    public Integer getStatusQuincena() {
        return statusQuincena;
    }

    public void setStatusQuincena(Integer statusQuincena) {
        this.statusQuincena = statusQuincena;
    }
    
    
}
