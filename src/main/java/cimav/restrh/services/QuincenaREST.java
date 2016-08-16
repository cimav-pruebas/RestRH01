/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Concepto;
import cimav.restrh.entities.Empleado;
import cimav.restrh.entities.EmpleadoHisto;
import cimav.restrh.entities.EmpleadoPlaza;
import cimav.restrh.entities.Movimiento;
import cimav.restrh.entities.MovimientoHisto;
import cimav.restrh.entities.Nomina;
import cimav.restrh.entities.NominaHisto;
import cimav.restrh.entities.Quincena;
import cimav.restrh.entities.QuincenaSingleton;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
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
@Path("quincena")
@PermitAll
//@TransactionManagement(TransactionManagementType.BEAN)
public class QuincenaREST extends AbstractFacade<Quincena>{

    private final static Logger logger = Logger.getLogger(QuincenaREST.class.getName() );

//    @PersistenceContext(unitName = "PU_JPA")
//    private EntityManager em;

    @EJB
    private MovimientoFacadeREST movimientosREST;
    @EJB
    private NominaREST nominaREST;
    @EJB
    private EmpleadoFacadeREST empleadoREST;
    @EJB
    private EmpleadoPlazaREST empleadoPlazaREST;
    
    @Inject
    private QuincenaSingleton quincenaSingleton;

    public QuincenaREST() {
        super(Quincena.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @GET
    @Produces("application/json")
    public String quincenaInit() {
        // Carga la quincena
        
        // Este debe ser el 1er método que lea.

        Query query = getEntityManager().createQuery("SELECT q FROM Quincena AS q ORDER BY q.id DESC");
        Quincena quincenaEntity = (Quincena) query.setMaxResults(1).getSingleResult();
        quincenaSingleton.setYear(quincenaEntity.getYear());
        quincenaSingleton.setQuincena(quincenaEntity.getQuincena());
        quincenaSingleton.setSalarioMinimo(quincenaEntity.getSalarioMinimo());
        quincenaSingleton.setStatus(quincenaEntity.getStatus());
        
        quincenaSingleton.load();
        
        return quincenaSingleton.toJSON();
    }
    
    @GET
    @Path("ultima")
    @Produces("application/json")
    public Quincena quincenaUltima() {
        Query query = getEntityManager().createNativeQuery("SELECT * FROM quincenas ORDER BY id DESC LIMIT 1", Quincena.class);
        Quincena result = (Quincena) query.getSingleResult();
        return result;
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Quincena find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("all")
    @Override
    @Produces("application/json")
    public List<Quincena> findAll() {
        return super.findAll();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Quincena insert(Quincena entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, Quincena entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<Quincena> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

//    @GET
//    @Path("transaction2/{p}")
//    @Produces("application/text")
//    @Transactional(rollbackOn = Exception.class)
//    // https://en.wikipedia.org/wiki/Java_Transaction_API#Open_source_JTA_implementations
//    public String testTrans2(@PathParam("p") Integer p) throws Exception {
//
//            Parametros parametros = parametrosREST.get();
//            parametros.setStatusQuincena(p);
//
//            if (p % 2 == 0) throw new Exception("Num Par");
//
//            Empleado juan = empleadoREST.find(154);
//            juan.setIdProyecto(p.shortValue());
//
//        return "END";
//    }
  /*
    la clase requier @TransactionManagement(TransactionManagementType.BEAN)

    @GET
    @Path("transaction/{p}")
    @Produces("application/text")
    public String testTrans(@PathParam("p") Integer p) {

        UserTransaction utx = ctx.getUserTransaction();
        try {
            utx.begin();

            Parametros parametros = parametrosREST.get();
            parametros.setStatusQuincena(p);

            if (p % 2 == 0) throw new Exception("Num Par");

            Empleado juan = empleadoREST.find(154);
            juan.setIdProyecto(p.shortValue());

            utx.commit();

            return "Commit " + p;

        } catch (Exception ex) {
            Logger.getLogger(QuincenaREST.class.getName()).log(Level.SEVERE, null, ex);
            try {
                utx.rollback();
                return "Rollback " + p;
            } catch (Exception ex1) {
                Logger.getLogger(QuincenaREST.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return "OK END";
    }
    */

    @GET
    @Path("init")
    @Produces("text/plain")
    @Transactional(rollbackOn = Exception.class)
    public String initTodos() {
        
        List<Empleado> empleadosActivos = empleadoREST.findAll();
        empleadosActivos.stream().forEach((empleado) -> {
            inicializar(empleado);
        });
        
        return "Init " + empleadosActivos.size() + " empleados";
    }
    
    @GET
    @Path("init/{id_empleado}")
    @Produces("application/json")
    @Transactional(rollbackOn = Exception.class)
    public EmpleadoPlaza initEmpleado(@PathParam("id_empleado") Integer id_empleado) {
        
        Empleado empleadoActivo = empleadoREST.find(id_empleado);
        EmpleadoPlaza plaza = inicializar(empleadoActivo);
        
        return plaza;
    }
    
    public EmpleadoPlaza inicializar(Empleado empleado) {
        
        // crear una copia de la plaza del Empleado en el Histo al inicializar la quincena para corroborar cambios en la plaza al cierre
        // empleado x empleado
        
        // 1ero lo borro en caso de que ya exista
        empleadoPlazaREST.delete(empleado.getId(), quincenaSingleton.getYear(), quincenaSingleton.getQuincena());
        
        EmpleadoPlaza empleadoPlaza =  new EmpleadoPlaza();
        empleadoPlaza.setYear(quincenaSingleton.getYear());
        empleadoPlaza.setQuincena(quincenaSingleton.getQuincena());
        empleadoPlaza.setIdEmpleado(empleado.getId());
        empleadoPlaza.setCode(empleado.getCode());
        empleadoPlaza.setName(empleado.getName());
        empleadoPlaza.setAntiguedad(empleado.getPantYears() + " años, " + empleado.getPantMonths() + " meses, " + empleado.getPantDayEven() + " días");
        empleadoPlaza.setEstimulosProductividad(empleado.getEstimulosProductividad());
        empleadoPlaza.setFechaAntiguedad(empleado.getFechaAntiguedad());
        empleadoPlaza.setFechaBaja(empleado.getFechaBaja());
        empleadoPlaza.setFechaIngreso(empleado.getFechaIngreso());
        empleadoPlaza.setIdDepartamento(empleado.getDepartamento().getId());
        empleadoPlaza.setIdGrupo(empleado.getIdGrupo());
        empleadoPlaza.setIdSede(empleado.getIdSede());
        empleadoPlaza.setIdStatus(empleado.getIdStatus());
        empleadoPlaza.setIdTipoAntiguedad(empleado.getIdTipoAntiguedad());
        empleadoPlaza.setNivelCode(empleado.getNivel().getCode());
        
        // insertar la copia
        empleadoPlazaREST.insert(empleadoPlaza);
        
        return empleadoPlaza;
    }
    
    
    @GET
    @Path("cierre/{cerrar}")
    @Produces("application/json")
    @Transactional(rollbackOn = Exception.class)
    /**
     * Cierra la quincena actual
     */
    public String cierre(@PathParam("cerrar") boolean cerrar) throws Exception {

        
//        Parametros parametros = parametrosREST.get();

        if (!cerrar) {
            // previene cierre por error
            return quincenaSingleton.toJSON();
        }

        // determinar quincena & year
        Integer year = quincenaSingleton.getYear();
        Integer quinActual = quincenaSingleton.getQuincena();
        
        if (false) {
            // para pruebas de transaccion
            throw new Exception("***** Verdadero ******");
        }

        // usra  @OneToMany(orphanRemoval=true)
        // @OneToOne(cascade=CascadeType.REMOVE)

        /********
         * Movimientos 
         ********/
        
        // eliminar posibles registros en el histórico 
        String qlString = "DELETE FROM MovimientoHisto mh WHERE mh.year = :p_year AND mh.quincena = :p_quincena";
        Query query = em.createQuery(qlString, MovimientoHisto.class);
        query.setParameter("p_year", year);
        query.setParameter("p_quincena", quinActual); 
        query.executeUpdate();

        // insertar los movimientos (y cálculos) en el histórico con year|quincena
        List<Movimiento> movimientos = movimientosREST.findAll();
        for (Movimiento movimiento : movimientos) {
            MovimientoHisto movimientoHisto = new MovimientoHisto();

            movimientoHisto.setQuincena(quinActual.shortValue());
            movimientoHisto.setYear(year.shortValue());

            // en historico, el id_movimiento permite el track del movimiento (pago) quincena tras quincena
            movimientoHisto.setIdMovimiento(movimiento.getId());

            movimientoHisto.setCantidad(movimiento.getCantidad());
            movimientoHisto.setCantidadEmpresa(movimiento.getCantidadEmpresa());
            movimientoHisto.setIdConcepto(movimiento.getConcepto().getId());
            movimientoHisto.setIdEmpleado(movimiento.getIdEmpleado());
            movimientoHisto.setNumQuincenas(movimiento.getNumQuincenas());
            movimientoHisto.setPago(movimiento.getPago());
            movimientoHisto.setPermanente(movimiento.getPermanente());
            movimientoHisto.setSaldo(movimiento.getSaldo());

            em.persist(movimientoHisto);

            // Eliminar movimientos tipo Calculos para la siguiente quincena
            // Actualizar movimientos tipo Pagos (saldos, pago, num_quincenas y permanente).
            if (Concepto.MOV_PAGO == movimiento.getConcepto().getIdTipoMovimiento()) {
                if (movimiento.getPermanente()) {
                    // si es permanente lo deja todo igual
                }

                // TODO Importante. De momento, quincena 1 a la 2, no actualiza movimientos por seguridad.
                // simplemente los deja igual; como si fueran permanentes.
//                    else if (movimiento.getNumQuincenas() > 0) {
//
//                        short numQuincenas = movimiento.getNumQuincenas();
//                        MonetaryAmount saldo = movimiento.getSaldo();
//                        MonetaryAmount pago = movimiento.getPago();
//
//                        assert saldo.isGreaterThanOrEqualTo(pago) : "El saldo no puede ser menor al pago: " + movimiento.getId();
//
//                        // nuevo saldo
//                        saldo = saldo.subtract(pago);
//                        // restar 1 periodo
//                        numQuincenas = (short) (numQuincenas - 1);
//
//                        if ((numQuincenas <= 0) || saldo.isLessThanOrEqualTo(Money.of(BigDecimal.ZERO, "MXN"))) {
//                            // si ya no queda saldo, se elimina
//                            em.remove(movimiento);
//                        } else {
//                            // persistir la cobranza
//                            movimiento.setNumQuincenas(numQuincenas);
//                            movimiento.setSaldo(saldo);
//                        }
//                    }
            } else {
                // Elimina todos los Calculos
                em.remove(movimiento);
            }

        }

        /*********
         * Nomina 
         *********/
        
        // asegurarse no haya registros del year|quincena en el histórico 
        qlString = "DELETE FROM NominaHisto nh WHERE nh.year = :p_year AND nh.quincena = :p_quincena";
        query = em.createQuery(qlString, NominaHisto.class);
        query.setParameter("p_year", year);
        query.setParameter("p_quincena", quinActual);
        query.executeUpdate();

        // insertar los registros de nómina en el histórico con year|quincena
        List<Nomina> nominas = nominaREST.findAll();
        for (Nomina nomina : nominas) {
            NominaHisto nominaHisto = new NominaHisto();
            nominaHisto.setQuincena(quinActual.shortValue());
            nominaHisto.setYear(year.shortValue());

            nominaHisto.setDescanso(nomina.getDescanso().shortValue());
            nominaHisto.setFaltas(nomina.getFaltas().shortValue());
            nominaHisto.setHorasExtrasDobles(nomina.getHorasExtrasDobles());
            nominaHisto.setHorasExtrasTriples(nomina.getHorasExtrasTriples());
            nominaHisto.setIdEmpleado(nomina.getIdEmpleado());
            nominaHisto.setIncapacidadHabiles(nomina.getIncapacidadHabiles().shortValue());
            nominaHisto.setIncapacidadInhabiles(nomina.getIncapacidadInhabiles().shortValue());
            nominaHisto.setOrdinarios(nomina.getOrdinarios().shortValue());
            nominaHisto.setSdiVariableBimestreAnterior(nomina.getSdiVariableBimestreAnterior());

            em.persist(nominaHisto);
        }

        // Vaciar nomina - se realiza de nuevo en NominaREST.init()
        // Cada registro (de Nomina) se inicializa (creacion e inyección en NominaEmpleado)
        // conforme se Calcula el empleado
        getEntityManager().createQuery("DELETE FROM Nomina").executeUpdate();
        getEntityManager().createNativeQuery("ALTER SEQUENCE nomina_id_seq RESTART WITH 1").executeUpdate(); 

        /********
         * Empleados 
         * Guarda los cambios en la Plaza (EmpleadosHisto)
         ********/
        
        // asegurarse no haya registro del year|quincena en el histórico
        qlString = "DELETE FROM EmpleadoHisto eh WHERE eh.year = :p_year AND eh.quincena = :p_quincena";
        query = em.createQuery(qlString, EmpleadoHisto.class);
        query.setParameter("p_year", year);
        query.setParameter("p_quincena", quinActual); 
        query.executeUpdate();

        // insertar los registros de empleados en el histórico con year|quincena
        List<Empleado> empleados = empleadoREST.findAll(); // TODO .findAllActivos filtrar dados de baja antes y en la quicena
        for(Empleado empleado : empleados) {
            EmpleadoPlaza empleadoPlaza = empleadoPlazaREST.findByIdEmpleado(empleado.getId());
            if (empleadoPlaza != null && !empleadoPlaza.equivalente(empleado)) {

                EmpleadoHisto empleadoHisto = new EmpleadoHisto();
                empleadoHisto.setQuincena(quinActual);
                empleadoHisto.setYear(year);

                String antg = "" + empleado.getPantYears() + " año(s), " + empleado.getPantMonths() + " mes(es), " + empleado.getPantDayEven() + " día(s)";
                empleadoHisto.setAntiguedad(antg);
                empleadoHisto.setCode(empleado.getCode());
                empleadoHisto.setEstimulosProductividad(empleado.getEstimulosProductividad());
                empleadoHisto.setFechaAntiguedad(empleado.getFechaAntiguedad());
                empleadoHisto.setFechaBaja(empleado.getFechaBaja());
                empleadoHisto.setFechaIngreso(empleado.getFechaIngreso());
                empleadoHisto.setIdDepartamento(empleado.getDepartamento().getId());
                empleadoHisto.setIdEmpleado(empleado.getId());
                empleadoHisto.setIdGrupo(empleado.getIdGrupo());
                empleadoHisto.setIdSede(empleado.getIdSede());
                empleadoHisto.setIdStatus(empleado.getIdStatus());
                empleadoHisto.setNivelCode(empleado.getNivel().getCode());
                empleadoHisto.setIdTipoAntiguedad(empleado.getIdTipoAntiguedad());
                empleadoHisto.setName(empleado.getName());
            
                em.persist(empleadoHisto);
            }
        }
        
        // vaciar plazas
        getEntityManager().createQuery("DELETE FROM EmpleadoPlaza").executeUpdate();
        getEntityManager().createNativeQuery("ALTER SEQUENCE empleadosplaza_id_seq RESTART WITH 1").executeUpdate();         

        /*
        INSERT INTO empleadoshisto 
            (id, id_empleado, year, quincena, name, code, id_status, id_grupo, id_departamento, id_sede, id_tipo_antiguedad, fecha_antiguedad, antiguedad, fecha_ingreso, 
            fecha_baja, estimulos_productividad, nivel_code, porcen_seg_separacion_ind, pension_tipo, pension_porcentaje, pension_cantidad_fija) 
        VALUES (0, 0, 0, 0, '', '', 0, 0, 0, 0, 0, '', '', '', '', 0, '', 0, 0, 0, 0);
        */
        
        // Status Cerrada quincena actual
        query = getEntityManager().createQuery("SELECT q FROM Quincena q WHERE q.year = :year AND q.quincena = :quincena", Quincena.class);
        Quincena quincenaActual = (Quincena) query.setParameter("year", year).setParameter("quincena", quinActual).getSingleResult();
        quincenaActual.setStatus(QuincenaSingleton.CERRADA);
        em.persist(quincenaActual);
        
        Integer quinNext = quinActual + 1;
        if (quinNext == 25) {
            quinNext = 1;
            year = year + 1;
        }
        // Crear nueva quincena 
        Quincena quincenaNueva = new Quincena();
        quincenaNueva.setYear(year);
        quincenaNueva.setQuincena(quinNext);
        quincenaNueva.setSalarioMinimo(quincenaActual.getSalarioMinimo());
        quincenaNueva.setStatus(QuincenaSingleton.ABIERTA);
        em.persist(quincenaNueva);
        
        // Verificar Empleados dados de Baja, etc.
        return "{}";// quincenaSingleton.toJSON();
    }


}
