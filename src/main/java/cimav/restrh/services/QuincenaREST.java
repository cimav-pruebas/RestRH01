/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Concepto;
import cimav.restrh.entities.Empleado;
import cimav.restrh.entities.EmpleadoHisto;
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
import javax.persistence.PersistenceContext;
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

    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    @EJB
    private MovimientoFacadeREST movimientosREST;
    @EJB
    private NominaREST nominaREST;
    @EJB
    private EmpleadoFacadeREST empleadoREST;
    
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
    public String quincenaActual() {
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
    @Path("cierre/{cerrar}")
    @Produces("application/json")
    @Transactional(rollbackOn = Exception.class)
    /**
     * Cierra la quincena actual
     */
    public String cierre(@PathParam("cerrar") boolean cerrar) throws Exception {

        
//        Parametros parametros = parametrosREST.get();

//        if (!cerrar) {
//            // previene cierre por error
//            return quincenaSingleton.toJSON();
//        }

        // determinar quincena & year
        Integer year = 0; //quincenaSingleton.getYear();
        Integer quinActual = 0;// quincenaSingleton.getQuincena();
        Integer quinNext = quinActual + 1;
        if (quinNext == 25) {
            quinNext = 1;
            year = year + 1;
//            parametros.setYear(year);
        }
//        parametros.setQuincenaActual(quinNext);

//        quincenaSingleton.load();

        if (false) {
            // para pruebas
            throw new Exception("***** Verdadero ******");
        }

        // usra  @OneToMany(orphanRemoval=true)
        // @OneToOne(cascade=CascadeType.REMOVE)

        /********
         * Movimientos 
         ********/
        
        // eliminar posibles registros del year|quincena en el histórico
        String qlString = "DELETE FROM MovimientoHisto mh WHERE mh.year = :p_year AND mh.quincena = :p_quincena";
        Query query = em.createQuery(qlString, MovimientoHisto.class);
        query.setParameter("p_year", year);
        query.setParameter("p_quincena", quinNext); // TODO ¿porque del quinNext?
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
        query.setParameter("p_quincena", quinNext);
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

        // Vaciar nomina - se realiza en NominaREST.init()
        // Cada registro (de Nomina) se inicializa (creacion e inyección en NominaEmpleado)
        // conforme se Calcula el empleado

        /********
         * Empleados 
         ********/
        
        // asegurarse no haya registro del year|quincena en el histórico
        qlString = "DELETE FROM EmpleadoHisto eh WHERE eh.year = :p_year AND eh.quincena = :p_quincena";
        query = em.createQuery(qlString, EmpleadoHisto.class);
        query.setParameter("p_year", year);
        query.setParameter("p_quincena", quinNext);
        query.executeUpdate();

        // insertar los registros de empleados en el histórico con year|quincena
        List<Empleado> empleados = empleadoREST.findAll(); // TODO .findAllActivos filtrar dados de baja antes y en la quicena
        for (Empleado empleado : empleados) {
            EmpleadoHisto empleadoHisto = new EmpleadoHisto();
            empleadoHisto.setQuincena(quinActual.shortValue());
            empleadoHisto.setYear(year.shortValue());

            String antg = "" + empleado.getPantYears() + " año(s), " + empleado.getPantMonths() + " mes(es), " + empleado.getPantDayEven() + " día(s)";
            empleadoHisto.setAntiguedad(antg);
            empleadoHisto.setCode(empleado.getCode());
            empleadoHisto.setEstimulosProductividad(empleado.getEstimulosProductividad());
            empleadoHisto.setFechaAntiguedad(empleado.getFechaAntiguedad());
            empleadoHisto.setFechaBaja(empleado.getFechaBaja());
            empleadoHisto.setFechaIngreso(empleado.getFechaIngreso());
            empleadoHisto.setIdDepartamento(empleado.getDepartamento().getId());
            empleadoHisto.setIdEmpleado(empleado.getId());
            empleadoHisto.setIdGrupo(empleado.getIdGrupo().shortValue());
            empleadoHisto.setIdSede(empleado.getIdSede());
            empleadoHisto.setIdStatus(empleado.getIdStatus());
            empleadoHisto.setNivelCode(empleado.getNivel().getCode());
            empleadoHisto.setIdTipoAntiguedad(empleado.getIdTipoAntiguedad());
            empleadoHisto.setName(empleado.getName());

            em.persist(empleadoHisto);
        }

        // Verificar Empleados dados de Baja, etc.
        return "{}";// quincenaSingleton.toJSON();
    }


}
