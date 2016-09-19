/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.EmpleadoNominaHisto;
import cimav.restrh.entities.MovimientoHisto;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author calderon
 */
@Stateless
@Path("empleado_nomina_histo")
@DeclareRoles(AbstractFacade.ADMIN_ROLE)
public class EmpleadoNominaHistoREST extends AbstractFacade<EmpleadoNominaHisto>{
    
    public EmpleadoNominaHistoREST() {
        super(EmpleadoNominaHisto.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
        @GET
    @Path("{id}/{year}/{quincena}")
    @Produces("application/json")
    public EmpleadoNominaHisto findHisto(@PathParam("id") Integer id, @PathParam("year") Integer year, @PathParam("quincena") Short quincena) {
        EmpleadoNominaHisto empleadoNominaHisto = super.find(id);
        
            //Collection<MovimientoHisto> movs = empleadoNominaHisto.getMovimientosHisto();
            
            Stream<MovimientoHisto> movs =  empleadoNominaHisto.getMovimientosHisto().stream()
                  .filter((MovimientoHisto m) -> Objects.equals(m.getQuincena(), quincena));
            empleadoNominaHisto.setMovimientosHisto(movs.collect(Collectors.toList()));   
            
        
        return empleadoNominaHisto;
    }

    
}
