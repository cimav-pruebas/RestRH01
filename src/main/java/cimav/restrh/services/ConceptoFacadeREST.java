/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Concepto;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Stateless;
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
@Path("concepto")
public class ConceptoFacadeREST extends AbstractFacade<Concepto> {
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public ConceptoFacadeREST() {
        super(Concepto.class);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Concepto insert(Concepto entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, Concepto entity) {
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
    public Concepto find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<Concepto> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<Concepto> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("base")
    // @JsonView(View.Base.class) no funciona
    @Produces("application/json")
    public List<Concepto> findAllBase() {
        
        // usa SELECT NEW CONSTRUCTOR en lugar del @JsonView que no funcionó
        Query query = getEntityManager().createQuery("SELECT NEW cimav.restrh.entities.Concepto(c.id, c.code, c.name, c.tipoMvto) FROM Concepto AS c", Concepto.class);
        List<Concepto> results = query.getResultList();
        
        //Sorting by code
        Collections.sort(results, new Comparator<Concepto>() { @Override public int compare(Concepto  emp1, Concepto  emp2) { return  emp1.getCode().compareTo(emp2.getCode()); }});        
        
        return results;
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
