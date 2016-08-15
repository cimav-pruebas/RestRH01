/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.EmpleadoPlaza;
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
@Path("empleado_plaza")
@DeclareRoles(AbstractFacade.ADMIN_ROLE)
public class EmpleadoPlazaREST extends AbstractFacade<EmpleadoPlaza> {
    
    public EmpleadoPlazaREST() {
        super(EmpleadoPlaza.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public EmpleadoPlaza find(@PathParam("id") Integer id) {
        EmpleadoPlaza empleadoPlaza = super.find(id);
        return empleadoPlaza;
    }

    @GET
    @Override
    @Produces("application/json")
    public List<EmpleadoPlaza> findAll() {
        List<EmpleadoPlaza> emps = super.findAll();
        return emps;
    }

    @GET
    @Path("/by_id_empleado/{idEmpleado}")
    @Produces("application/json")
    public EmpleadoPlaza findByIdEmpleado(@PathParam("idEmpleado") Integer idEmpleado) {
        
            String query = "SELECT en FROM EmpleadoPlaza en WHERE en.idEmpleado = " + idEmpleado;
            EmpleadoPlaza result = (EmpleadoPlaza) getEntityManager().createQuery(query).getSingleResult();
        
        return result;
    }
    
    @GET
    @Path("/by_code/{code}")
    @Produces("application/json")
    public List<EmpleadoPlaza> findByCodeEmpleado(@PathParam("code") String code) {
        
            String query = "SELECT en FROM EmpleadoPlaza en WHERE en.code like '%" + code.trim() +"'";
            List<EmpleadoPlaza>  result = getEntityManager().createQuery(query).getResultList();
        
        return result;
    }
    
    @DELETE
    @Path("{id_empleado}/{year}/{quincena}")
    @Produces("application/text")
    public String delete(@PathParam("id_empleado") Integer id_empleado, @PathParam("year") Integer year, @PathParam("quincena") Integer quincena) {
        Query query = em.createQuery("DELETE FROM EmpleadoPlaza eh WHERE eh.idEmpleado = :id_empleado AND eh.year = :year AND eh.quincena = :quincena");
        int deletedCount = query
                .setParameter("id_empleado", id_empleado)
                .setParameter("year", year)
                .setParameter("quincena", quincena).executeUpdate();
        return "borrado " + id_empleado + " - " + year + ":" + quincena;
    }
    
}
