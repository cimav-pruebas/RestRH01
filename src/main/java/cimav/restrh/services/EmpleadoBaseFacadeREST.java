/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Departamento;
import cimav.restrh.entities.EmpleadoBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Stateless;
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
@Path("empleado_base")
public class EmpleadoBaseFacadeREST extends AbstractFacade<EmpleadoBase>{
 
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public EmpleadoBaseFacadeREST() {
        super(EmpleadoBase.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public EmpleadoBase find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<EmpleadoBase> findAll() {
        List<EmpleadoBase> emps = super.findAll();
        return emps;
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<EmpleadoBase> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("by_depto/{idDepto}")
    @Produces("application/json")
    public List<EmpleadoBase> findAllByDepto(@PathParam("idDepto") Integer idDepto) {
        
        List<EmpleadoBase> results = new ArrayList<>();
        
        Query query = getEntityManager().createQuery("SELECT d FROM Departamento d WHERE d.id = :id", Departamento.class);
        query.setParameter("id", idDepto);
        Departamento depto = null;
        try {
            depto = (Departamento) query.getSingleResult();
            results.addAll((List<EmpleadoBase>)depto.getEmpleadoCollection());
            //Sorting by name
            Collections.sort(results, new Comparator<EmpleadoBase>() { @Override public int compare(EmpleadoBase  emp1, EmpleadoBase  emp2) { return  emp1.getName().compareTo(emp2.getName()); }});
        } catch (Exception nr) {
            
        }
        return results;
    }
    
    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    
}
