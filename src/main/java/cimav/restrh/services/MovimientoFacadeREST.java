/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Movimiento;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
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
@Path("movimientos")
@PermitAll
public class MovimientoFacadeREST extends AbstractFacade<Movimiento> {
    
    private final static Logger logger = Logger.getLogger(MovimientoFacadeREST.class.getName() ); 
    
//    @PersistenceContext(unitName = "PU_JPA")
//    private EntityManager em;

    public MovimientoFacadeREST() {
        super(Movimiento.class);
    }

    @POST
    @Path("find_by_empleado_ids")
    @Consumes(value = "application/json")
    @Produces(value = "application/json")
    public List<Movimiento> doFindByIds(JsonArray ids) {
        List<Movimiento> result = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        ids.stream().map((idVal) -> ((JsonObject)idVal).getInt("id")).forEach((i) -> {
            idList.add(i);
        });
        if (idList.size() > 0) {
            // el constructor usa el aliasCantidad porque es del tipo BigDecimal.
            // No usa directamente cantidad porque es del tipo MonetaryAmount que no es reconocido por el JPA
            // TODO podria faltarle el aliasCantidad para cantidadEmpresa
            String qlString = "SELECT NEW cimav.restrh.entities.Movimiento(0, m.concepto, SUM(m.aliasCantidad)) FROM Movimiento AS m " +
                    " WHERE m.idEmpleado IN :idList GROUP BY m.concepto ORDER BY m.concepto.id";
            Query query = getEntityManager().createQuery(qlString, Movimiento.class);
            query.setParameter("idList", idList); //.setParameter("quincena", 1); //quincena.getQuincena());
            result.addAll(query.getResultList());
        }
        return result;
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Movimiento insert(Movimiento entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, Movimiento entity) {
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
    public Movimiento find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("/by_empleado/{id}")
    @Produces("application/json")
    public List<Movimiento> findByIdEmpleado(@PathParam("id") Integer idEmpleado) {
        
            String query = "SELECT nq FROM Movimiento nq WHERE nq.idEmpleado = :id_empleado";
            List<Movimiento>  result = getEntityManager().createQuery(query).setParameter("id_empleado", idEmpleado).getResultList();
        
        return result;
    }

    @GET
    @Override
    @Produces("application/json")
    public List<Movimiento> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces("application/json")
    public List<Movimiento> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
