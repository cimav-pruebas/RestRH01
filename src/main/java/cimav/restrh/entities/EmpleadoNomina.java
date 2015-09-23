/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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

    @Column(name = "id_tipo_antiguedad")
    private Short idTipoAntiguedad;
    
//    @Column(name = "fecha_antiguedad")
//    @Temporal(TemporalType.DATE)
//    private Date fechaAntiguedad;
    
    @OneToMany(mappedBy = "EmpleadoBase")
    private Collection<NominaQuincenal> nominaQuincenalCollection;
    
    @OneToMany(mappedBy = "empleadoNomina")
    private Collection<Incidencia> incidencias;

    public EmpleadoNomina() {
        super();
    }

    public Short getIdTipoAntiguedad() {
        return idTipoAntiguedad;
    }

    public void setIdTipoAntiguedad(Short idTipoAntiguedad) {
        this.idTipoAntiguedad = idTipoAntiguedad;
    }

//    public Date getFechaAntiguedad() {
//        return fechaAntiguedad;
//    }
//
//    public void setFechaAntiguedad(Date fechaAntiguedad) {
//        this.fechaAntiguedad = fechaAntiguedad;
//    }

    public Collection<NominaQuincenal> getNominaQuincenalCollection() {
        return nominaQuincenalCollection;
    }

    public void setNominaQuincenalCollection(Collection<NominaQuincenal> nominaQuincenalCollection) {
        this.nominaQuincenalCollection = nominaQuincenalCollection;
    }

    public Collection<Incidencia> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(Collection<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }
    
}
