/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author calderon
 */
@Entity
@Cacheable(false)
@Table(name = "justificaciones", schema = "public")
@XmlRootElement(name="Justificacion")
public class Justificacion extends JustificacionRef implements Serializable {
    
    @Transient
    private String any;
    
    public Justificacion() {
        super();
        any = "Holas";
    }
    
    public String getFraccion() {
        return "F:" + this.getIdTipo() + "" ;
    }
    
    public String getAny() {
        return any;
    }
    
}
