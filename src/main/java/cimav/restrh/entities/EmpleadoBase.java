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
@Table(name = "empleados", schema = "public")
@XmlRootElement(name = "empleado_base")
public class EmpleadoBase extends EmpleadoSuper implements Serializable{

    public static final Integer ACTIVO  = 0;
    public static final Integer BAJA    = 1;
    
    public EmpleadoBase() {
        super();
    }
    
}
