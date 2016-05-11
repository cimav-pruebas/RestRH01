/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Concepto;
import cimav.restrh.entities.EmpleadoNomina;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author juan.calderon
 */
@Stateless
@Path("empleado_nomina")
@DeclareRoles(AbstractFacade.ADMIN_ROLE)
public class EmpleadoNominaFacadeREST extends AbstractFacade<EmpleadoNomina> {
    
//    @PersistenceContext(unitName = "PU_JPA")
//    private EntityManager em;

    public EmpleadoNominaFacadeREST() {
        super(EmpleadoNomina.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public EmpleadoNomina find(@PathParam("id") Integer id) {
        EmpleadoNomina empleadoNomina = super.find(id);
        return empleadoNomina;
    }

    @GET
    @Override
    @Produces("application/json")
    public List<EmpleadoNomina> findAll() {
        List<EmpleadoNomina> emps = super.findAll();
        return emps;
    }

    @GET
    @Path("/by_code/{code}")
    @Produces("application/json")
    public List<EmpleadoNomina> findByCodeEmpleado(@PathParam("code") String code) {
        
            String query = "SELECT en FROM EmpleadoNomina en WHERE en.code like '%" + code.trim() +"'";
            List<EmpleadoNomina>  result = getEntityManager().createQuery(query).getResultList();
        
        return result;
    }
    
    @GET
    @Path("/pension_alimenticia/{id_empleado}")
    @Produces("application/json")
    public List<Concepto> findPensionAlimenticiaByIdEmpleado(@PathParam("id_empleado") Integer idEmp) {
        String idEmpleado = idEmp == null || idEmp < 0 ? "0" : ""+idEmp;
        String nativeSql= "SELECT c.* FROM conceptos AS c JOIN pensionalimenticia AS pa ON c.id = pa.id_concepto JOIN empleados AS e ON pa.id_empleado = e.id "
                + "WHERE e.id = " + idEmpleado;
        Query query = getEntityManager().createNativeQuery(nativeSql, Concepto.class);
        query.setParameter("id_empleado", idEmpleado);
        List<Concepto> result = query.getResultList();
        return result;
    }
    
    @GET
    @Path("/pension_alimenticia_ids/{id_empleado}")
    @Produces("application/json")
    public String findPensionAlimenticiaIDsByIdEmpleado(@PathParam("id_empleado") Integer idEmp) {
        String idEmpleado = idEmp == null || idEmp < 0 ? "0" : ""+idEmp;
        String nativeSql= "SELECT c.id FROM conceptos AS c JOIN pensionalimenticia AS pa ON c.id = pa.id_concepto JOIN empleados AS e ON pa.id_empleado = e.id "
                + "WHERE e.id = " + idEmpleado;
        Query query = getEntityManager().createNativeQuery(nativeSql);
        query.setParameter("id_empleado", idEmpleado);
        List<Integer> result = query.getResultList();
        String array = "[";
        for(int i : result) {
            array = array + "," + i;
        }
        array = array.replace("[,", "");
        return array;
    }
    
}
