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
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "empleados", catalog = "rh_development", schema = "public")
@XmlRootElement(name = "empleado_base")
public class EmpleadoBase extends EmpleadoSuper implements Serializable{

    public EmpleadoBase() {
        super();
    }
    
}
