/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Parametros;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author juan.calderon
 */
@Stateless
@Path("parametros")
public class ParametrosREST {
 
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public ParametrosREST() {
    }
    
    public EntityManager getEntityManager() {
        return em;
    }

    @GET
    //@Path("")
    @Produces(value = "application/json")
    public Parametros get() {
        Parametros parametros = getEntityManager().find(Parametros.class, 1);
        return parametros;
    }
    /*
    @GET
    @Path("/num_quincena_actual")
    @Produces("text/plain")
    public String getNumQuincenaActual() {
        String result = "0";
        try {
            String query = "SELECT p.quincenaActual FROM Parametros p";
            Integer num = (Integer) getEntityManager().createQuery(query).getSingleResult();
            result = Integer.toString(num);
        } catch (Exception e) {
            System.out.println("num_quincena_actual ::> " + e.getMessage());
        }
        return result;
    }
     */   
}
