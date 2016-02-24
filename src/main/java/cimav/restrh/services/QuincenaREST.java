/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Movimiento;
import cimav.restrh.entities.MovimientoHisto;
import cimav.restrh.entities.Nomina;
import cimav.restrh.entities.NominaHisto;
import cimav.restrh.entities.Parametros;
import cimav.restrh.entities.QuincenaSingleton;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@Path("quincena")
public class QuincenaREST {
    
    private final static Logger logger = Logger.getLogger(QuincenaREST.class.getName() ); 
    
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    @Inject
    private QuincenaSingleton quincenaSingleton;
    
    @EJB
    private ParametrosREST parametrosREST;
    @EJB
    private MovimientoFacadeREST movimientosREST;
    @EJB
    private NominaREST nominaREST;

    public QuincenaREST() {
    }

    
    @GET
    @Path("")
    @Produces("application/json")
    public String quincena() {
        String result = quincenaSingleton.toJSON();
        return result;
    }
    
    @GET
    @Path("{year}/{quincena}")
    @Produces("application/json")
    public String quincena(@PathParam("year") int year, @PathParam("quincena") int quin) {
        quincenaSingleton.set(year, quin);
        String result = quincenaSingleton.toJSON();
        quincenaSingleton.load(); // recargar la quincena actual
        return result;
    }
    
    @GET
    @Path("cierre/{cerrar}")
    @Produces("application/json")
    public String cerrarQuincena(@PathParam("cerrar") boolean cerrar) {

        Parametros parametros = parametrosREST.get();
        
        if (!cerrar) {
            // previene cierre por error
            return quincenaSingleton.toJSON();
        }

        Integer year = quincenaSingleton.getYear();
        Integer quinActual = quincenaSingleton.getQuincena();
        Integer quinNext = quinActual + 1;
        if (quinNext == 25) {
            quinNext = 1;
            year = year + 1;
            parametros.setYear(year);
        }
        parametros.setQuincenaActual(quinNext);
        
        quincenaSingleton.load();
        
        // usra  @OneToMany(orphanRemoval=true)
        // @OneToOne(cascade=CascadeType.REMOVE)
        
        // asegurarse no haya registro del year|quincena en el histórico
        String qlString = "DELETE FROM MovimientoHisto mh WHERE mh.year = :p_year AND mh.quincena = :p_quincena";
        Query query = em.createQuery(qlString, MovimientoHisto.class);
        query.setParameter("p_year", year);
        query.setParameter("p_quincena", quinNext);
        query.executeUpdate();
        
        // insertar los movimientos en el histórico con year|quincena
        List<Movimiento> movimientos = movimientosREST.findAll();
        for(Movimiento movimiento : movimientos) {
            MovimientoHisto movimientoHisto = new MovimientoHisto();
            movimientoHisto.setQuincena(quinNext.shortValue());
            movimientoHisto.setYear(year.shortValue());
            movimientoHisto.setCantidad(movimiento.getCantidad());
            movimientoHisto.setCantidadEmpresa(movimiento.getCantidadEmpresa());
            movimientoHisto.setIdConcepto(movimiento.getConcepto().getId());
            movimientoHisto.setIdEmpleado(movimiento.getIdEmpleado());
            movimientoHisto.setNumQuincenas(movimiento.getNumQuincenas());
            movimientoHisto.setPago(movimiento.getPago());
            movimientoHisto.setPermanente(movimiento.getPermanente());
            movimientoHisto.setSaldo(movimiento.getSaldo());
            
            em.persist(movimientoHisto);
        }
        
        // vaciar movimientos
        
        
        // asegurarse no haya registro del year|quincena en el histórico
        qlString = "DELETE FROM NominaHisto mh WHERE mh.year = :p_year AND mh.quincena = :p_quincena";
        query = em.createQuery(qlString, NominaHisto.class);
        query.setParameter("p_year", year);
        query.setParameter("p_quincena", quinNext);
        query.executeUpdate();
        
        // insertar los registros de nómina en el histórico con year|quincena
        List<Nomina> nominas = nominaREST.findAll();
        for(Nomina nomina : nominas) {
            NominaHisto nominaHisto = new NominaHisto();
            nominaHisto.setQuincena(quinNext.shortValue());
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
        
        // vaciar nomina
        
        return quincenaSingleton.toJSON();
    }

    
}
