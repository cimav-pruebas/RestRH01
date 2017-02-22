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
    
    public Justificacion() {
        super();
    }
    
    public String getRomano() {
        String result = "I";

        switch(this.getJustificacionTipo().getId()) {
            case 1: result = "XVII"; break;
            case 2: result = "XIV"; break;
            case 3: result = "XV"; break;
            case 4: result = "XVII"; break;
            case 5: result = "III"; break;
        }
        
        if (this.getJustificacionTipo().getId() != 2 && this.getEsUnico()) {
            result = "I";
        }
        
        return result;
    }
    
    public String getBienServicioTxt(){
        return this.getBienOServicio() == 0 ? "bienes" : "servicios"; 
    }
}
