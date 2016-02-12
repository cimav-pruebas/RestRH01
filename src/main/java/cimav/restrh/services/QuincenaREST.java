/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Parametros;
import cimav.restrh.entities.QuincenaSingleton;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        
        Integer quinActual = quincenaSingleton.getQuincena() + 1;
        if (quinActual == 25) {
            quinActual = 1;
            Integer nextYear = quincenaSingleton.getYear() + 1;
            parametros.setYear(nextYear);
        }
        parametros.setQuincenaActual(quinActual);
        
        quincenaSingleton.load();
        return quincenaSingleton.toJSON();
    }

    
}
