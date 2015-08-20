/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.EmpleadoNomina;
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
@Path("empleado_nomina")
public class EmpleadoNominaFacadeREST extends AbstractFacade<EmpleadoNomina> {
    
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public EmpleadoNominaFacadeREST() {
        super(EmpleadoNomina.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public EmpleadoNomina find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<EmpleadoNomina> findAll() {
        List<EmpleadoNomina> emps = super.findAll();
        return emps;
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<EmpleadoNomina> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }
    
}