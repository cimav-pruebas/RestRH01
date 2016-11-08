/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Justificacion;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
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
 * @author calderon
 */
@Stateless
@Path("justificacion")
@PermitAll
public class JustificacionREST extends AbstractFacade<Justificacion>{

    private final static Logger logger = Logger.getLogger(JustificacionREST.class.getName() ); 

    public JustificacionREST() {
        super(Justificacion.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("by_id_empleado/{id_empleado}")
    @Produces("application/json")
    public Justificacion findByIdEmpleado(@PathParam("id_empleado") Integer idEmpleado) {
        Query query = getEntityManager().createQuery("SELECT j FROM Justificacion AS j WHERE j.empleado.id = :id_empleado", Justificacion.class);
        query.setParameter("id_empleado", idEmpleado);
        try {
            Justificacion justificacion = (Justificacion) query.getSingleResult();
            return justificacion;
        } catch (NoResultException | NonUniqueResultException nue) {
            Justificacion justificacion = new Justificacion();
            justificacion.setId(-1);
            return justificacion;
        }
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Justificacion insert(Justificacion entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, Justificacion entity) {
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
    public Justificacion find(@PathParam("id") Integer id) {
        return super.find(id); 
    }

    @GET
    @Override
    @Produces("application/json")
    public List<Justificacion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    
}
