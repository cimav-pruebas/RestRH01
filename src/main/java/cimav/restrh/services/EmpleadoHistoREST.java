/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.EmpleadoHisto;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author calderon
 */
@Stateless
@Path("empleado_histo")
@DeclareRoles(AbstractFacade.ADMIN_ROLE)
public class EmpleadoHistoREST extends AbstractFacade<EmpleadoHisto> {
    
    public EmpleadoHistoREST() {
        super(EmpleadoHisto.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public EmpleadoHisto find(@PathParam("id") Integer id) {
        EmpleadoHisto empleadoHisto = super.find(id);
        return empleadoHisto;
    }

    @GET
    @Override
    @Produces("application/json")
    public List<EmpleadoHisto> findAll() {
        List<EmpleadoHisto> emps = super.findAll();
        return emps;
    }

    @GET
    @Path("/by_code/{code}")
    @Produces("application/json")
    public List<EmpleadoHisto> findByCodeEmpleado(@PathParam("code") String code) {
        
            String query = "SELECT en FROM EmpleadoHisto en WHERE en.code like '%" + code.trim() +"'";
            List<EmpleadoHisto>  result = getEntityManager().createQuery(query).getResultList();
        
        return result;
    }
    
}
