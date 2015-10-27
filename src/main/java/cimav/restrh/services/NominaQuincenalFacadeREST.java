/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.NominaQuincenal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
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
    
    private final static Logger logger = Logger.getLogger(NominaQuincenalFacadeREST.class.getName() ); 
    
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
        ids.stream().map((idVal) -> ((JsonObject)idVal).getInt("id")).forEach((i) -> {
            idList.add(i);
        });
        if (idList.size() > 0) {
            // el constructor usa el aliasCantidad porque es del tipo BigDecimal.
            // No usa directamente cantidad porque es del tipo MonetaryAmount que no es reconocido por el JPA
            String qlString = "SELECT NEW cimav.restrh.entities.NominaQuincenal(0, nq.concepto, SUM(nq.aliasCantidad)) FROM NominaQuincenal AS nq " +
                    " WHERE nq.idEmpleado IN :idList GROUP BY nq.concepto ORDER BY nq.concepto.id";
            Query query = getEntityManager().createQuery(qlString, NominaQuincenal.class);
            query.setParameter("idList", idList);
            result.addAll(query.getResultList());
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
