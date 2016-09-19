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
 * @author calderon
 */
@Entity
@Cacheable(false)
@Table(name = "empleados", catalog = "rh_development", schema = "public")
@XmlRootElement(name = "empleado_nomina_histo")
public class EmpleadoNominaHisto  extends EmpleadoSuper implements Serializable {
    
    @OneToMany(mappedBy = "EmpleadoBase")
    private Collection<MovimientoHisto> movimientosHisto;
    
    @OneToOne(mappedBy = "empleadoNominaHisto")
    private NominaHisto nominaHisto;

    public EmpleadoNominaHisto() {
        super();
    }

    public Collection<MovimientoHisto> getMovimientosHisto() {
        return movimientosHisto;
    }

    public void setMovimientosHisto(Collection<MovimientoHisto> movimientosHisto) {
        this.movimientosHisto = movimientosHisto;
    }

    public NominaHisto getNominaHisto() {
        return nominaHisto;
    }

    public void setNominaHisto(NominaHisto nominaHisto) {
        this.nominaHisto = nominaHisto;
    }
    
}
