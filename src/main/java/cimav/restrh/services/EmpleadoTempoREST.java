/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.EmpleadoTempo;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author calderon
 */
@Stateless
@Path("empleado_tempo")
@DeclareRoles(AbstractFacade.ADMIN_ROLE)
public class EmpleadoTempoREST extends AbstractFacade<EmpleadoTempo> {
    
    public EmpleadoTempoREST() {
        super(EmpleadoTempo.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public EmpleadoTempo find(@PathParam("id") Integer id) {
        EmpleadoTempo empleadoTempo = super.find(id);
        return empleadoTempo;
    }

    @GET
    @Override
    @Produces("application/json")
    public List<EmpleadoTempo> findAll() {
        List<EmpleadoTempo> emps = super.findAll();
        return emps;
    }

    @GET
    @Path("/by_id_empleado/{idEmpleado}")
    @Produces("application/json")
    public EmpleadoTempo findByIdEmpleado(@PathParam("idEmpleado") Integer idEmpleado) {
        
            String query = "SELECT en FROM EmpleadoTempo en WHERE en.idEmpleado = " + idEmpleado;
            EmpleadoTempo result = (EmpleadoTempo) getEntityManager().createQuery(query).getSingleResult();
        
        return result;
    }
    
    @GET
    @Path("/by_code/{code}")
    @Produces("application/json")
    public List<EmpleadoTempo> findByCodeEmpleado(@PathParam("code") String code) {
        
            String query = "SELECT en FROM EmpleadoTempo en WHERE en.code like '%" + code.trim() +"'";
            List<EmpleadoTempo>  result = getEntityManager().createQuery(query).getResultList();
        
        return result;
    }
    
    @DELETE
    @Path("{id_empleado}/{year}/{quincena}")
    @Produces("application/text")
    public String delete(@PathParam("id_empleado") Integer id_empleado) {
        Query query = em.createQuery("DELETE FROM EmpleadoTempo eh WHERE eh.idEmpleado = :id_empleado");
        int deletedCount = query .setParameter("id_empleado", id_empleado).executeUpdate();
        return "borrado " + id_empleado;
    }
    
}
