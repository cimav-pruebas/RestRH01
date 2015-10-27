
package cimav.restrh.services;

import cimav.restrh.entities.EGrupo;
import cimav.restrh.entities.EmpleadoNomina;
import cimav.restrh.entities.EmpleadoQuincenal;
import cimav.restrh.entities.Quincena;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author juan.calderon
 */
@Stateless
@Path("empleado_quincenal")
public class EmpleadoQuincenalREST extends AbstractFacade<EmpleadoQuincenal>{
    
    private final static Logger logger = Logger.getLogger(EmpleadoQuincenalREST.class.getName() ); 
    
    @EJB
    private EmpleadoNominaFacadeREST empleadoNominaFacadeREST;

    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    @Inject
    private Quincena quincena;
    
    public EmpleadoQuincenalREST() {
        super(EmpleadoQuincenal.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @GET
    @Path("init/{id_emp}")
    @Produces("text/plain")
    public String init(@PathParam("id_emp") Integer idEmp) {
        String r ="Oka";
        
        
        LocalDate localDateInicioQuincena = Quincena.convert(quincena.getFechaInicio());
        // TODO Excepcionalmente damos por hecho que ningún empleado cumple años el día 28 o 29 o 31
        LocalDate localDateFinQuincena = Quincena.convert(quincena.getFechaFin());
        
        try {
            // vaciar
            getEntityManager().createQuery("DELETE FROM EmpleadoQuincenal").executeUpdate();
            getEntityManager().createNativeQuery("ALTER SEQUENCE empleadoquincenal_id_seq RESTART WITH 1").executeUpdate(); 
            
            //getEntityManager().createQuery(query).executeUpdate();
        } catch (Exception er){
            logger.log(Level.INFO, er.getMessage());
        }
        
        try{
            // probar que fecha tiene la quincena correcta
            logger.log(Level.INFO, "---------------------------------------------------------");
            logger.log(Level.INFO, "1> \n" + quincena.toJSON());
            quincena.init();
            logger.log(Level.INFO, "2> \n" + quincena.toJSON());
            
            // recorrer todos los empleados activos
            // TODO falta condicion para solo los activos y solo los CYT y AYA en caso de PAnt
            List<EmpleadoNomina> empleadosNomina = empleadoNominaFacadeREST.findAll();
            
            for(EmpleadoNomina empleadoNomina : empleadosNomina) {
                
                boolean isCYT = empleadoNomina.getIdGrupo().equals(EGrupo.CYT.getId());
                boolean isAYA = empleadoNomina.getIdGrupo().equals(EGrupo.AYA.getId());
                if (/*empleadoNomina.getId() == 155 &&*/ (isCYT || isAYA)) {
                    
                    LocalDate localDateFechaAntiguedad = Quincena.convert(empleadoNomina.getFechaAntiguedad());
                    
                    logger.log(Level.INFO, empleadoNomina.getId() + " | " + empleadoNomina.getName() 
                            + " | " + empleadoNomina.getNivel() + " | " + empleadoNomina.getFechaAntiguedad() + " | " + localDateFechaAntiguedad);
                    // TODO Para cuando la PAnt se cumpla en la quincena, no consideramos incidencias (faltas e incapacidades);
                    // se debe corregir.
                    
                    //TODO BUG Muy Lento y problema con JodaTime Resource not found: "org/joda/time/tz/data/ZoneInfoMap"
                    // TODO Checar que incluya el dia Inicial.
                    // Se da por hecho q nadie cumple el 28, 29 o 31 
                    // Se calculan los años en base al último día de la quincena 
                    
                    
//                Date fAnt = empleadoNomina.getFechaAntiguedad() == null ? new Date() : empleadoNomina.getFechaAntiguedad();
//                Instant instant = Instant.ofEpochMilli(fAnt.getTime());
//                LocalDate lfAnt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
//                
//                instant = Instant.ofEpochMilli(quincena.getFechaFin().getTime());
//                LocalDate lfFin = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
                
//                int yearsCumplidos = Period.between(lfAnt, lfFin).getYears();
                    
                    Period period = Period.between(localDateFechaAntiguedad, localDateFinQuincena);
                    
                    Integer diasPAntAnterior = 0;
                    Integer diasPAntActual = quincena.getDiasLaborables();
                    
                    EmpleadoQuincenal empleadoQuincenal = new EmpleadoQuincenal();
                    empleadoQuincenal.setIdEmpleado(empleadoNomina.getId());
                    empleadoQuincenal.setYearPAnt(period.getYears());
                    empleadoQuincenal.setMonthsPAnt(period.getMonths());
                    empleadoQuincenal.setDaysPAnt(period.getDays());
                    empleadoQuincenal.setDescanso(quincena.getDiasDescanso());
                    empleadoQuincenal.setOrdinarios(quincena.getDiasOrdinarios());
                    empleadoQuincenal.setDiasPAntUno(diasPAntAnterior);
                    empleadoQuincenal.setDiasPAntDos(diasPAntActual);
                    empleadoQuincenal.setFaltas(0);
                    empleadoQuincenal.setIncapacidadHabiles(0);
                    empleadoQuincenal.setIncapacidadInhabiles(0);
                    
                    getEntityManager().persist(empleadoQuincenal);
                            
                }
            }
            
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
        }
        
        return r;
    }

    private int daysBetween(Date d1, Date d2){
      return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }    
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public EmpleadoQuincenal insert(EmpleadoQuincenal entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, EmpleadoQuincenal entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public EmpleadoQuincenal find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<EmpleadoQuincenal> findAll() {
        return super.findAll();
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

}
