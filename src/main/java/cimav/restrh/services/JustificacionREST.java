/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.JustificacionRef;
import cimav.restrh.entities.Justificacion;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    @Path("one_by_id_empleado/{id_empleado}")
    @Produces("application/json")
    public Justificacion findOneByIdEmpleado(@PathParam("id_empleado") Integer idEmpleado) {
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

    @GET
    @Path("all_by_id_empleado/{id_empleado}")
    @Produces("application/json")
    public List<Justificacion> findAllByIdEmpleado(@PathParam("id_empleado") Integer idEmpleado) {
        
        TypedQuery<Justificacion> query = getEntityManager().createQuery("SELECT j FROM Justificacion AS j", Justificacion.class);
        List<Justificacion> results = query.getResultList();        
        /*
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        ParameterExpression<Integer> p = cb.parameter(Integer.class);
        Root<Justificacion> j = cq.from(JustificacionRef.class);
        cq.select(j).where(cb.gt(j.get("empleado.Id"), p));
        
        TypedQuery<Justificacion> query = getEntityManager().createQuery(cq);
        query.setParameter(p, idEmpleado);
        List<Justificacion> results = query.getResultList();
        */
        /*
        Query query = getEntityManager().createQuery("SELECT j FROM JustificacionRef AS j WHERE j.empleado.id = :id_empleado", JustificacionRef.class);
        query.setParameter("id_empleado", idEmpleado);
        List<Justificacion> result = new ArrayList<>();
        try {
            result= query.getResultList();
        } catch (NoResultException nue) {
        }
*/
        List<Justificacion> emps = super.findAll();
        return emps;
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
    public JustificacionRef find(@PathParam("id") Integer id) {
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
