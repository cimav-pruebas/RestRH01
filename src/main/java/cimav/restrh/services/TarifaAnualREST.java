/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.TarifaAnual;
import java.util.List;
import javax.ejb.Stateless;
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
@Path("tarifa_anual")
public class TarifaAnualREST extends AbstractFacade<TarifaAnual> {
    
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public TarifaAnualREST() {
        super(TarifaAnual.class);
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public TarifaAnual find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<TarifaAnual> findAll() {
        List<TarifaAnual> result = super.findAll();
        return result;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
