
package cimav.restrh.services;

import cimav.restrh.entities.EGrupo;
import cimav.restrh.entities.EmpleadoNomina;
import cimav.restrh.entities.EmpleadoQuincenal;
import cimav.restrh.entities.HoraExtra;
import cimav.restrh.entities.Incidencia;
import cimav.restrh.entities.Quincena;
import java.math.BigDecimal;
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
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.javamoney.moneta.Money;

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

//    @PersistenceContext(unitName = "PU_JPA")
//    private EntityManager em;

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
    @Produces(value = "application/json")
    public EmpleadoQuincenal init(@PathParam("id_emp") Integer idEmp) {
        // inicializa un empleado
        EmpleadoQuincenal result = null;
        try {
            // TODO es necesario el quincena.init();?
            quincena.init();
            EmpleadoNomina empleadoNomina = empleadoNominaFacadeREST.find(idEmp);
            result = this.inicializar(empleadoNomina);
        } catch (Exception er){
            logger.log(Level.INFO, er.getMessage());
        }
        return result;
    }
    
    public EmpleadoQuincenal inicializar(EmpleadoNomina empNom) {
        /*
        Inicializa la Antigüedad del Empleado.
        Dias ordinarios, descanso, trabajados de la quincena
        Sdi del bimestre para el empleado
        */
        
        EmpleadoQuincenal empleadoQuincenal = null;
        
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

            // borrarlo si ya existe
            Query query = getEntityManager().createQuery("DELETE FROM EmpleadoQuincenal eq WHERE eq.idEmpleado = :id_emp");
            int deletedCount = query.setParameter("id_emp", empNom.getId()).executeUpdate();
            
            empleadoQuincenal = new EmpleadoQuincenal();
            empleadoQuincenal.setIdEmpleado(empNom.getId());
            empleadoQuincenal.setYearPAnt(period.getYears());
            empleadoQuincenal.setMonthsPAnt(period.getMonths());
            empleadoQuincenal.setDaysPAnt(period.getDays());
            empleadoQuincenal.setDescanso(0);
            empleadoQuincenal.setOrdinarios(0);
            empleadoQuincenal.setDiasDescansoDeLaQuincena(quincena.getDiasDescanso());
            empleadoQuincenal.setDiasOrdinariosDeLaQuincena(quincena.getDiasOrdinarios());
            empleadoQuincenal.setDiasPAntUno(diasPAntAnterior);
            empleadoQuincenal.setDiasPAntDos(diasPAntActual);
            empleadoQuincenal.setFaltas(0);
            empleadoQuincenal.setIncapacidadHabiles(0);
            empleadoQuincenal.setIncapacidadInhabiles(0);
            empleadoQuincenal.setHorasExtrasDobles(0.00);
            empleadoQuincenal.setHorasExtrasTriples(0.00);
            // TODO falta inicializar el sdiVariableBimestreAnterior
            empleadoQuincenal.setSdiVariableBimestreAnterior(Money.of(BigDecimal.ZERO, CalculoREST.MXN)); 

            this.insert(empleadoQuincenal);
            //getEntityM anager().persist(empleadoQuincenal);
        }
        return empleadoQuincenal;
    }
    
    private int daysBetween(Date d1, Date d2){
      return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }    

    @GET
    @Path("incidencias/{id_empleado}")
    @Produces("text/plain")
    public String incidencias(@PathParam("id_empleado") Integer idEmpleado) {
        Query query = getEntityManager().createQuery("SELECT eq FROM EmpleadoQuincenal AS eq WHERE eq.idEmpleado =:id_empleado", EmpleadoQuincenal.class);
        query.setParameter("id_empleado", idEmpleado);
        EmpleadoQuincenal empleadoQuincenal = (EmpleadoQuincenal) query.getSingleResult();
        return this.calcularIncidencias(empleadoQuincenal);
    }
    
    public String calcularIncidencias(EmpleadoQuincenal empleadoQuincenal) {
        if (empleadoQuincenal != null) {
            Integer faltas = 0;
            Integer incapacidadHabiles = 0;
            Integer incapacidadInhabiles = 0;
            for(Incidencia incidencia : empleadoQuincenal.getIncidencias()) {
                if (Incidencia.FALTA.equals(incidencia.getClase())) {
                    faltas += incidencia.getDiasHabiles();
                } else if (Incidencia.INCAPACIDAD.equals(incidencia.getClase())) {
                    incapacidadHabiles += incidencia.getDiasHabiles();
                    incapacidadInhabiles += incidencia.getDiasInhabiles();
                }
            }
            empleadoQuincenal.setFaltas(faltas);
            empleadoQuincenal.setIncapacidadHabiles(incapacidadHabiles);
            empleadoQuincenal.setIncapacidadInhabiles(incapacidadInhabiles);
        }
        return "none";
    }
    
    @GET
    @Path("tiempo_extra/{id_empleado}")
    @Produces("text/plain")
    public String tiempoExtra(@PathParam("id_empleado") Integer idEmpleado) {
        Query query = getEntityManager().createQuery("SELECT eq FROM EmpleadoQuincenal AS eq WHERE eq.idEmpleado =:id_empleado", EmpleadoQuincenal.class);
        query.setParameter("id_empleado", idEmpleado);
        EmpleadoQuincenal empleadoQuincenal = (EmpleadoQuincenal) query.getSingleResult();
        return this.calcularTiempoExtra(empleadoQuincenal);
    }
    
    public String calcularTiempoExtra(EmpleadoQuincenal empleadoQuincenal) {
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
        return "aucune";
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
