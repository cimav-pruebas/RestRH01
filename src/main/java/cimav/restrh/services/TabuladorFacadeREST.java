/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Tabulador;
import java.util.List;
import javax.ejb.Stateless;
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
@Path("tabulador")
public class TabuladorFacadeREST extends AbstractFacade<Tabulador> {
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public TabuladorFacadeREST() {
        super(Tabulador.class);
    }

    @POST
    @Override
    @Consumes("application/json")
    public void create(Tabulador entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, Tabulador entity) {
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
    public Tabulador find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<Tabulador> findAll() {
        List<Tabulador> result = super.findAll();
        return result;
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<Tabulador> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
