/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Empleado;
import cimav.restrh.entities.EmpleadoBase;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
@Path("empleado")
//@DeclareRoles(AbstractFacade.ADMIN_ROLE)
@PermitAll
public class EmpleadoREST extends AbstractFacade<Empleado> {

//    @PersistenceContext(unitName = "PU_JPA")
//    private EntityManager em;

    public EmpleadoREST() {
        super(Empleado.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Empleado insert(Empleado entity) {
        this.setName(entity);
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    
    private void setName(Empleado entity) {
        // al guardar (POST y PUT), actualizar el Name
        String name = entity.getApellidoPaterno() + " " + entity.getApellidoMaterno() + " " + entity.getNombre();
        entity.setName(name);
    }

    @PUT
    @Consumes("application/json")
    @Override
    public void edit(Empleado entity) {
        this.setName(entity);
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
    public Empleado find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<Empleado> findAll() {
        List<Empleado> emps = super.findAll();
        return emps;
    }

    @GET
    @Path("/activos")
    @Produces("application/json")
    public List<Empleado> findActivos() {
        
        Query query = getEntityManager().createQuery("SELECT e FROM Empleado e WHERE e.idStatus = " + EmpleadoBase.ACTIVO, Empleado.class);
        List<Empleado> result = query.getResultList();
        
        return result;
    }
    
    @GET
    @Path("/cuenta/{cuenta}")
    @Produces("application/json")
    public Empleado findByCuenta(@PathParam("cuenta") String cuenta) {
        
            String query = "SELECT e FROM Empleado e WHERE e.cuentaCimav LIKE :cuenta";
            Empleado  result = (Empleado) getEntityManager().createQuery(query).setParameter("cuenta", cuenta).getSingleResult();
        
        return result;
    }
    
    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<Empleado> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

//    @GET
//    @Path("base")
//    // @JsonView(View.Base.class) no funciona
//    @Produces("application/json")
//    public List<EmpleadoOld> findAllBase() {
//        
//        // usa SELECT NEW CONSTRUCTOR en lugar del @JsonView que no funcionó
//        Query query = getEntityManager().createQuery("SELECT NEW cimav.restrh.entities.Empleado(e.id, e.code, e.name, e.cuentaCimav) FROM Empleado AS e", EmpleadoOld.class);
//        List<EmpleadoOld> results = query.getResultList();
//        
//        //Sorting by name
//        Collections.sort(results, new Comparator<EmpleadoOld>() { @Override public int compare(EmpleadoOld  emp1, EmpleadoOld  emp2) { return  emp1.getName().compareTo(emp2.getName()); }
//    });        
//        return results;
//    }

    
}
