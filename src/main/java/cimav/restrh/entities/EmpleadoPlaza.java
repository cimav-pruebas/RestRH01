/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Objects;
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
@Table(name = "empleadosplaza", catalog = "rh_development", schema = "public")
@XmlRootElement
public class EmpleadoPlaza extends Plaza implements Serializable {

    public EmpleadoPlaza() {
        super();
    }

    public boolean equivalente(Empleado empleado) {
        if (empleado == null) {
            return false;
        }
        if (!Objects.equals(this.getName(), empleado.getName())) {
            return false;
        }
        if (!Objects.equals(this.getCode(), empleado.getCode())) {
            return false;
        }
        if (!Objects.equals(this.getNivelCode(), empleado.getNivel().getCode())) {
            return false;
        }
        if (!Objects.equals(this.getIdEmpleado(), empleado.getId())) {
            return false;
        }
        if (!Objects.equals(this.getIdStatus(), empleado.getIdStatus())) {
            return false;
        }
        if (!Objects.equals(this.getIdGrupo(), empleado.getIdGrupo())) {
            return false;
        }
        if (!Objects.equals(this.getIdDepartamento(), empleado.getDepartamento().getId())) {
            return false;
        }
        if (!Objects.equals(this.getIdSede(), empleado.getIdSede())) {
            return false;
        }
        if (!Objects.equals(this.getIdTipoAntiguedad(), empleado.getIdTipoAntiguedad())) {
            return false;
        }
        if (!Objects.equals(this.getFechaAntiguedad(), empleado.getFechaAntiguedad())) {
            return false;
        }
        if (!Objects.equals(this.getFechaIngreso(), empleado.getFechaIngreso())) {
            return false;
        }
        if (!Objects.equals(this.getFechaBaja(), empleado.getFechaBaja())) {
            return false;
        }
        if (!Objects.equals(this.getEstimulosProductividad(), empleado.getEstimulosProductividad())) {
            return false;
        }
        return true;
    }
    
}
