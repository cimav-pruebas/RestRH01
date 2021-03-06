/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author juan.calderon
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        /* aqui para asegurarse que siempre los agregue */
        resources.add(cimav.restrh.services.RESTCorsRequestFilter.class);
        resources.add(cimav.restrh.services.RESTCorsResponseFilter.class);
        resources.add(cimav.restrh.services.AuthenticationFilter.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(cimav.restrh.services.AuthenticationFilter.class);
        resources.add(cimav.restrh.services.CalculoREST.class);
        resources.add(cimav.restrh.services.ConceptoFacadeREST.class);
        resources.add(cimav.restrh.services.DepartamentoFacadeREST.class);
        resources.add(cimav.restrh.services.EmpleadoBaseFacadeREST.class);
        resources.add(cimav.restrh.services.EmpleadoHistoREST.class);
        resources.add(cimav.restrh.services.EmpleadoNominaFacadeREST.class);
        resources.add(cimav.restrh.services.EmpleadoNominaHistoREST.class);
        resources.add(cimav.restrh.services.EmpleadoREST.class);
        resources.add(cimav.restrh.services.EmpleadoTempoREST.class);
        resources.add(cimav.restrh.services.HorasExtrasFacadeREST.class);
        resources.add(cimav.restrh.services.IncidenciaFacadeREST.class);
        resources.add(cimav.restrh.services.JustificacionREST.class);
        resources.add(cimav.restrh.services.JustificacionTipoREST.class);
        resources.add(cimav.restrh.services.MonedaREST.class);
        resources.add(cimav.restrh.services.MovimientoFacadeREST.class);
        resources.add(cimav.restrh.services.NominaREST.class);
        resources.add(cimav.restrh.services.ParametrosREST.class);
        resources.add(cimav.restrh.services.QuincenaREST.class);
        resources.add(cimav.restrh.services.RESTCorsRequestFilter.class);
        resources.add(cimav.restrh.services.RESTCorsResponseFilter.class);
        resources.add(cimav.restrh.services.Reportes.class);
        resources.add(cimav.restrh.services.TabuladorFacadeREST.class);
        resources.add(cimav.restrh.services.TarifaAnualREST.class);
    }
    
}
