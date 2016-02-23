/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juan.calderon
 */
@Entity
@Cacheable(false)
@Table(name = "empleados", catalog = "rh_development", schema = "public")
@XmlRootElement(name = "empleado_nomina")
public class EmpleadoNomina extends EmpleadoSuper implements Serializable {

    @OneToMany(mappedBy = "EmpleadoBase")
    private Collection<Movimiento> movimientos;
    
    @OneToOne(mappedBy = "empleadoNomina")
    private Nomina nomina;
    
    public EmpleadoNomina() {
        super();
    }

    public Collection<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Collection<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public Nomina getNomina() {
        return nomina;
    }

    public void setNomina(Nomina nomina) {
        this.nomina = nomina;
    }
    
}
