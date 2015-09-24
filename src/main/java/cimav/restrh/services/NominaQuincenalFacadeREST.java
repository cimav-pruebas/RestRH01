/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.NominaQuincenal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@Path("nomina_quincenal")
public class NominaQuincenalFacadeREST extends AbstractFacade<NominaQuincenal> {
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public NominaQuincenalFacadeREST() {
        super(NominaQuincenal.class);
    }

    @POST
    @Path("find_by_empleado_ids")
    @Consumes(value = "application/json")
    @Produces(value = "application/json")
    public List<NominaQuincenal> doFindByIds(JsonArray ids) {
        List<NominaQuincenal> result = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        for (JsonValue idVal : ids) {
            int i = ((JsonObject)idVal).getInt("id");
            idList.add(i);
        }
        if (idList.size() > 0) {
            String qlString = "SELECT NEW cimav.restrh.entities.NominaQuincenal(0, nq.concepto, SUM(nq.cantidad)) FROM NominaQuincenal AS nq " +
                    " WHERE nq.idEmpleado IN :idList GROUP BY nq.concepto ORDER BY nq.concepto.id";
            Query query = getEntityManager().createQuery(qlString, NominaQuincenal.class);
            query.setParameter("idList", idList);
            result.addAll(query.getResultList());
            
//            String sql = "SELECT t FROM NominaQuincenal t WHERE t.idEmpleado IN :idList GROUP BY t.concepto";
//            result.addAll(getEntityManager().createQuery(sql , NominaQuincenal.class)
//                    .setParameter("idList", idList).getResultList());
            
            
//        Query query = getEntityManager().createQuery("SELECT NEW cimav.restrh.entities.Empleado(e.id, e.code, e.name, e.cuentaCimav) FROM Empleado AS e", EmpleadoOld.class);
//        List<EmpleadoOld> results = query.getResultList();

            
            
        }
        return result;
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public NominaQuincenal insert(NominaQuincenal entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, NominaQuincenal entity) {
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
    public NominaQuincenal find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces("application/json")
    public List<NominaQuincenal> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<NominaQuincenal> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
