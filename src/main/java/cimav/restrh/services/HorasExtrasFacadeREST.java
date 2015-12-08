/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.HoraExtra;
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
@Path("horas_extras")
public class HorasExtrasFacadeREST extends AbstractFacade<HoraExtra> {
    
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

//    @EJB
//    private EmpleadoQuincenalREST empleadoQuincenalREST;
    
    public HorasExtrasFacadeREST() {
        super(HoraExtra.class);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public HoraExtra insert(HoraExtra entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        
//        empleadoQuincenalREST.calcularTiempoExtra(entity.getIdEmpleado());
        
        return entity; 
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, HoraExtra entity) {
        super.edit(entity);
        
//        empleadoQuincenalREST.calcularTiempoExtra(entity.getIdEmpleado());
        
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        HoraExtra horaExtra = super.find(id);
        super.remove(horaExtra);
        
//        empleadoQuincenalREST.calcularTiempoExtra(horaExtra.getIdEmpleado());
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public HoraExtra find(@PathParam("id") Integer id) {
        HoraExtra horaExtra = super.find(id);
        
        // TODO quitarlo de aqui; solo esta para test
//        empleadoQuincenalREST.calcularTiempoExtra(horaExtra.getIdEmpleado());
        
        return horaExtra;
    }

    @GET
    @Override
    @Produces("application/json")
    public List<HoraExtra> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<HoraExtra> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
