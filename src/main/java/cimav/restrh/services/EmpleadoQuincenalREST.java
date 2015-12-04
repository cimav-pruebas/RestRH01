
package cimav.restrh.services;

import cimav.restrh.entities.EGrupo;
import cimav.restrh.entities.EmpleadoNomina;
import cimav.restrh.entities.EmpleadoQuincenal;
import cimav.restrh.entities.HoraExtra;
import cimav.restrh.entities.Quincena;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    @Path("init")
    @Produces("text/plain")
    public String init() {
        try {
            // Inicializa a todos
            quincena.init();
            
            // vaciar
            getEntityManager().createQuery("DELETE FROM EmpleadoQuincenal").executeUpdate();
            getEntityManager().createNativeQuery("ALTER SEQUENCE empleadoquincenal_id_seq RESTART WITH 1").executeUpdate(); 

            // TODO Filtrar que solo inicialize a los empleados activos.
            List<EmpleadoNomina> empleadosNomina = empleadoNominaFacadeREST.findAll();
            empleadosNomina.stream().forEach((empleadoNomina) -> {
                this.inicializar(empleadoNomina);
            });

        } catch (Exception er){
            logger.log(Level.INFO, er.getMessage());
        }
        return "";
    }
    
    @GET
    @Path("init/{id_emp}")
    @Produces("text/plain")
    public String init(@PathParam("id_emp") Integer idEmp) {
        // inicializa un empleado
        String result = "";
        try {
            quincena.init();
            EmpleadoNomina empleadoNomina = empleadoNominaFacadeREST.find(idEmp);
            result = this.inicializar(empleadoNomina);
        } catch (Exception er){
            logger.log(Level.INFO, er.getMessage());
        }
        return result;
    }
    
    private String inicializar(EmpleadoNomina empNom) {
        LocalDate localDateFinQuincena = Quincena.convert(quincena.getFechaFin());
        boolean isCYT = empNom.getIdGrupo().equals(EGrupo.CYT.getId());
        boolean isAYA = empNom.getIdGrupo().equals(EGrupo.AYA.getId());
        if (true || /*empleadoNomina.getId() == 155 &&*/ (isCYT || isAYA)) {

            LocalDate localDateFechaAntiguedad = Quincena.convert(empNom.getFechaAntiguedad());

            logger.log(Level.INFO, empNom.getId() + " | " + empNom.getName() 
                    + " | " + empNom.getNivel() + " | " + empNom.getFechaAntiguedad() + " | " + localDateFechaAntiguedad);
            // TODO Para cuando la PAnt se cumpla en la quincena, no consideramos incidencias (faltas e incapacidades);
            // se debe corregir.

            //TODO BUG Muy Lento y problema con JodaTime Resource not found: "org/joda/time/tz/data/ZoneInfoMap"
            // TODO Checar que incluya el dia Inicial.
            // Se da por hecho q nadie cumple el 28, 29 o 31 
            // Se calculan los años en base al último día de la quincena 

            Period period = Period.between(localDateFechaAntiguedad, localDateFinQuincena);

            Integer diasPAntAnterior = 0;
            Integer diasPAntActual = quincena.getDiasLaborables();

            EmpleadoQuincenal empleadoQuincenal = new EmpleadoQuincenal();
            empleadoQuincenal.setIdEmpleado(empNom.getId());
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
        return "";
    }
    
    private int daysBetween(Date d1, Date d2){
      return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }    
    
    @GET
    @Path("tiempo_extra/{id_empleado}")
    @Produces("text/plain")
    public String calcularTiempoExtra(@PathParam("id_empleado") Integer idEmpleado) {
        Query query = getEntityManager().createQuery("SELECT eq FROM EmpleadoQuincenal AS eq WHERE eq.idEmpleado =:id_empleado", EmpleadoQuincenal.class);
        query.setParameter("id_empleado", idEmpleado);
        EmpleadoQuincenal empleadoQuincenal = (EmpleadoQuincenal) query.getSingleResult();
        if (empleadoQuincenal != null) {
            // agrupar hrs extras por semana
            HashMap<Integer, List<HoraExtra>> hashMap = new HashMap<>();
            for (HoraExtra horaExtra : empleadoQuincenal.getHorasExtras()) {
                if (!hashMap.containsKey(horaExtra.getWeekOfYear())) {
                    List<HoraExtra> hrs = new ArrayList<>();
                    hrs.add(horaExtra);
                    hashMap.put(horaExtra.getWeekOfYear(), hrs);
                } else {
                    hashMap.get(horaExtra.getWeekOfYear()).add(horaExtra);
                }
            }
            empleadoQuincenal.getHorasExtras().stream().forEach((horaExtra) -> {
                if (!hashMap.containsKey(horaExtra.getWeekOfYear())) {
                    List<HoraExtra> hrs = new ArrayList<>();
                    hrs.add(horaExtra);
                    hashMap.put(horaExtra.getWeekOfYear(), hrs);
                } else {
                    hashMap.get(horaExtra.getWeekOfYear()).add(horaExtra);
                }
            });

            Double hrsDobles = 0.00;
            Double hrsTriples = 0.00;

            // agrupar total hrs pero contabilizadas por semana
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                List<HoraExtra> hrs = (List<HoraExtra>) pair.getValue();
                Double tot = 0.00;
                for (HoraExtra hr : hrs) {
                    tot = tot + hr.getHoras();
                }
                if (tot > 0) {
                    // la semana tiene hrs
                    Double hd = tot > 9 ? 9 : tot;
                    Double ht = tot > 9 ? tot - 9 : 0.00;
                    hrsDobles = hrsDobles + hd;
                    hrsTriples = hrsTriples + ht;
                }
            }
            empleadoQuincenal.setHorasExtrasDobles(hrsDobles);
            empleadoQuincenal.setHorasExtrasTriples(hrsTriples);

            //TODO ¿Cuando se persiste? Lo hace pero no sé cuando.
        }
        return "nada";
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
