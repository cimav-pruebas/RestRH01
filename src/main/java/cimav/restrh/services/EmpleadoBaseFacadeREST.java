/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Departamento;
import cimav.restrh.entities.EGrupo;
import cimav.restrh.entities.EmpleadoBase;
import cimav.restrh.entities.QuincenaSingleton;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author juan.calderon
 */
@Stateless
@Path("empleado_base")
public class EmpleadoBaseFacadeREST extends AbstractFacade<EmpleadoBase>{
 
    private final static Logger logger = Logger.getLogger(EmpleadoBaseFacadeREST.class.getName() ); 
    
//    @PersistenceContext(unitName = "PU_JPA")
//    private EntityManager em;

    @Inject
    private QuincenaSingleton quincena;
    
    public EmpleadoBaseFacadeREST() {
        super(EmpleadoBase.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public EmpleadoBase find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<EmpleadoBase> findAll() {
        List<EmpleadoBase> emps = super.findAll();
        return emps;
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<EmpleadoBase> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("by_depto/{idDepto}")
    @Produces("application/json")
    public List<EmpleadoBase> findAllByDepto(@PathParam("idDepto") Integer idDepto) {
        
        List<EmpleadoBase> results = new ArrayList<>();
        
        Query query = getEntityManager().createQuery("SELECT d FROM Departamento d WHERE d.id = :id", Departamento.class);
        query.setParameter("id", idDepto);
        Departamento depto = null;
        try {
            depto = (Departamento) query.getSingleResult();
            results.addAll((List<EmpleadoBase>)depto.getEmpleadoCollection());
            //Sorting by name
            Collections.sort(results, new Comparator<EmpleadoBase>() { @Override public int compare(EmpleadoBase  emp1, EmpleadoBase  emp2) { return  emp1.getName().compareTo(emp2.getName()); }});
        } catch (Exception nr) {
            
        }
        return results;
    }
    
    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("init_antiguedad")
    @Produces("text/plain")
    public String initAntiguedad() {
        List<EmpleadoBase> empleados = this.findAll();
        empleados.stream().forEach((emp) -> {
            this.initAntiguedad(emp.getId());
        });
        return "Empleados inicialiados: " + empleados.size();
    }
    
    @GET
    @Path("init_antiguedad/{id_emp}")
    @Produces("application/json")
    public EmpleadoBase initAntiguedad(@PathParam("id_emp") Integer idEmp) {
        
        EmpleadoBase empBase = null;
        
        // inicializa un empleado
        try {
            // TODO es necesario el quincena.init();?
            //quincena.init();
            /*
            Inicializa la AntigÃ¼edad del Empleado.
            */
        
            empBase = this.find(idEmp);
            
            LocalDate localDateFinQuincena = QuincenaSingleton.convert(quincena.getFechaFin());
            boolean isCYT = empBase.getIdGrupo().equals(EGrupo.CYT.getId());
            boolean isAYA = empBase.getIdGrupo().equals(EGrupo.AYA.getId());

            LocalDate localDateFechaAntiguedad = QuincenaSingleton.convert(empBase.getFechaAntiguedad());

            logger.log(Level.INFO, empBase.getId() + " | " + empBase.getName() 
                    + " | " + empBase.getNivel() + " | " + empBase.getFechaAntiguedad() + " | " + localDateFechaAntiguedad
                    + " >> " + localDateFinQuincena);
            // TODO Para cuando la PAnt se cumpla en la quincena, no consideramos incidencias (faltas e incapacidades);
            // se debe corregir.

            //TODO BUG Muy Lento y problema con JodaTime Resource not found: "org/joda/time/tz/data/ZoneInfoMap"
            // TODO Checar que incluya el dia Inicial.
            // Se da por hecho q nadie cumple el 28, 29 o 31 
            // Se calculan los aÃ±os en base al Ãºltimo dÃ­a de la quincena 


           // localDateFinQuincena = localDateFinQuincena.plusDays(1); // TODO Plus Day Hell
            // requierse agregarse un día por: Period between(LocalDate startDateInclusive, LocalDate endDateExclusive)
            Period period = Period.between(localDateFechaAntiguedad, localDateFinQuincena);
            empBase.setPantYears(period.getYears());
            empBase.setPantMonths(period.getMonths());
            empBase.setPantDayOdd(period.getDays());
            empBase.setPantDayEven(0); // TODO PAnt Odd|Even

            // persistir
            this.edit(empBase);

        } catch (Exception er){
            logger.log(Level.INFO, er.getMessage());
        }
        return empBase;
    }
    
}
