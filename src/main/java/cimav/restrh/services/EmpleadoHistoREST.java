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
    
    @GET
    @Path("/by_id_empleado/{id_empleado}")
    @Produces("application/json")
    public List<EmpleadoHisto> findByCodeEmpleado(@PathParam("id_empleado") Integer id_empleado) {
        
            String query = "SELECT en FROM EmpleadoHisto en WHERE en.idEmpleado = " + id_empleado;
            List<EmpleadoHisto>  result = getEntityManager().createQuery(query).getResultList();
        
        return result;
    }
    
    @DELETE
    @Path("{id_empleado}/{year}/{quincena}")
    @Produces("application/text")
    public String delete(@PathParam("id_empleado") Integer id_empleado, @PathParam("year") Integer year, @PathParam("quincena") Integer quincena) {
        Query query = em.createQuery("DELETE FROM EmpleadoHisto eh WHERE eh.idEmpleado = :id_empleado AND eh.year = :year AND eh.quincena = :quincena");
        int deletedCount = query
                .setParameter("id_empleado", id_empleado)
                .setParameter("year", year)
                .setParameter("quincena", quincena).executeUpdate();
        return "borrado " + id_empleado + " - " + year + ":" + quincena;
    }
    
}
