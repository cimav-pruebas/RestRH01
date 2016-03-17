
package cimav.restrh.services;

import cimav.restrh.entities.EmpleadoNomina;
import cimav.restrh.entities.Nomina;
import cimav.restrh.entities.HoraExtra;
import cimav.restrh.entities.Incidencia;
import cimav.restrh.entities.QuincenaSingleton;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@Path("nomina")
public class NominaREST extends AbstractFacade<Nomina>{
    
    private final static Logger logger = Logger.getLogger(NominaREST.class.getName() ); 
    
    @EJB
    private EmpleadoNominaFacadeREST empleadoNominaFacadeREST;

//    @PersistenceContext(unitName = "PU_JPA")
//    private EntityManager em;

    @Inject
    private QuincenaSingleton quincena;
    
    public NominaREST() {
        super(Nomina.class);
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
            
            // vaciar
            getEntityManager().createQuery("DELETE FROM Nomina").executeUpdate();
            //TODO falta cambiar ALTER SEQUENCE empleadoquincenal_id_seq RESTART WITH 1
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
    public Nomina init(@PathParam("id_emp") Integer idEmp) {
        // inicializa un empleado
        Nomina result = null;
        try {
            EmpleadoNomina empleadoNomina = empleadoNominaFacadeREST.find(idEmp);
            result = this.inicializar(empleadoNomina);
        } catch (Exception er){
            logger.log(Level.INFO, er.getMessage());
        }
        return result;
    }
    
    public Nomina inicializar(EmpleadoNomina empNom) {
        /*
        Dias ordinarios, descanso, trabajados de la quincena
        Sdi del bimestre para el empleado
        */
        
        Nomina nomina = null;
        
        // borrarlo si ya existe
        Query query = getEntityManager().createQuery("DELETE FROM Nomina eq WHERE eq.idEmpleado = :id_emp");
        int deletedCount = query.setParameter("id_emp", empNom.getId()).executeUpdate();

        nomina = new Nomina();
        nomina.setIdEmpleado(empNom.getId());
        nomina.setDescanso(0);
        nomina.setOrdinarios(0);
        nomina.setDiasDescansoDeLaQuincena(quincena.getDiasDescanso());
        nomina.setDiasOrdinariosDeLaQuincena(quincena.getDiasOrdinarios());
        //TODO faltan días de asueto
        nomina.setFaltas(0);
        nomina.setIncapacidadHabiles(0);
        nomina.setIncapacidadInhabiles(0);
        nomina.setHorasExtrasDobles(0.00);
        nomina.setHorasExtrasTriples(0.00);
        // TODO falta inicializar el sdiVariableBimestreAnterior
        nomina.setSdiVariableBimestreAnterior(Money.of(BigDecimal.ZERO, CalculoREST.MXN)); 

        this.insert(nomina); // insertarlo en la DB
        
        return nomina;
    }
    
//    @GET
//    @Path("incidencias/{id_empleado}")
//    @Produces("text/plain")
//    public String incidencias(@PathParam("id_empleado") Integer idEmpleado) {
//        Query query = getEntityManager().createQuery("SELECT eq FROM Nomina AS eq WHERE eq.idEmpleado =:id_empleado", Nomina.class);
//        query.setParameter("id_empleado", idEmpleado);
//        Nomina nomina = (Nomina) query.getSingleResult();
//        return this.calcularIncidencias(nomina);
//    }
    
    public String calcularIncidencias(Nomina nomina) {
        // se llama desde calculo
        if (nomina != null && !nomina.getIncidencias().isEmpty()) {
            Integer faltas = 0;
            Integer incapacidadHabiles = 0;
            Integer incapacidadInhabiles = 0;
            for(Incidencia incidencia : nomina.getIncidencias()) {
                if (Incidencia.FALTA.equals(incidencia.getClase())) {
                    faltas += incidencia.getDiasHabiles();
                } else if (Incidencia.INCAPACIDAD.equals(incidencia.getClase())) {
                    incapacidadHabiles += incidencia.getDiasHabiles();
                    incapacidadInhabiles += incidencia.getDiasInhabiles();
                }
            }
            nomina.setFaltas(faltas);
            nomina.setIncapacidadHabiles(incapacidadHabiles);
            nomina.setIncapacidadInhabiles(incapacidadInhabiles);
        }
        return "none";
    }
    
    @GET
    @Path("by_id_empleado/{id_empleado}")
    @Produces("application/json")
    public Nomina findByIdEmpleado(@PathParam("id_empleado") Integer idEmpleado) {
        Query query = getEntityManager().createQuery("SELECT eq FROM Nomina AS eq WHERE eq.idEmpleado =:id_empleado", Nomina.class);
        query.setParameter("id_empleado", idEmpleado);
        Nomina nomina = (Nomina) query.getSingleResult();
        return nomina;
    }
    
//    @GET
//    @Path("tiempo_extra/{id_empleado}")
//    @Produces("text/plain")
//    public String tiempoExtrax(@PathParam("id_empleado") Integer idEmpleado) {
//        Query query = getEntityManager().createQuery("SELECT eq FROM Nomina AS eq WHERE eq.idEmpleado =:id_empleado", Nomina.class);
//        query.setParameter("id_empleado", idEmpleado);
//        Nomina nomina = (Nomina) query.getSingleResult();
//        return this.calcularTiempoExtra2(nomina);
//    }
    
    public String calcularTiempoExtra(Nomina nomina) {
        // se llama desde calculo
        if (nomina != null && !nomina.getHorasExtras().isEmpty()) {
            // agrupar hrs extras por semana
            HashMap<Integer, List<HoraExtra>> hashMap = new HashMap<>();
            for (HoraExtra horaExtra : nomina.getHorasExtras()) {
                if (!hashMap.containsKey(horaExtra.getWeekOfYear())) {
                    List<HoraExtra> hrs = new ArrayList<>();
                    hrs.add(horaExtra);
                    hashMap.put(horaExtra.getWeekOfYear(), hrs);
                } else {
                    hashMap.get(horaExtra.getWeekOfYear()).add(horaExtra);
                }
            }
            nomina.getHorasExtras().stream().forEach((horaExtra) -> {
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
            nomina.setHorasExtrasDobles(hrsDobles);
            nomina.setHorasExtrasTriples(hrsTriples);

            //TODO Â¿Cuando se persiste? Lo hace pero no sÃ© cuando.
        }
        return "aucune";
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Nomina insert(Nomina entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, Nomina entity) {
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
    public Nomina find(@PathParam("id") Integer id) {
        return super.find(id); 
    }

    @GET
    @Override
    @Produces("application/json")
    public List<Nomina> findAll() {
        return super.findAll();
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

}
