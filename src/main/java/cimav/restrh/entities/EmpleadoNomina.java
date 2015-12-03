/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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

    @OneToOne(mappedBy = "empleadoNomina")
    private EmpleadoQuincenal empleadoQuincenal;
    
    @OneToMany(mappedBy = "EmpleadoBase")
    private Collection<HoraExtra> horasExtras;
    
    @Transient 
    private Double horasDobles;
    @Transient 
    private Double horasTriples;
            
    public EmpleadoNomina() {
        super();
    }

    public void ajustarTiempoExtra() {
        for(HoraExtra horaExtra : horasExtras) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(horaExtra.getDia());
            cal.get(Calendar.WEEK_OF_YEAR);
        }
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

    public EmpleadoQuincenal getEmpleadoQuincenal() {
        return empleadoQuincenal;
    }

    public void setEmpleadoQuincenal(EmpleadoQuincenal empleadoQuincenal) {
        this.empleadoQuincenal = empleadoQuincenal;
    }

    public Collection<HoraExtra> getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Collection<HoraExtra> horasExtras) {
        this.horasExtras = horasExtras;
    }
    
}
