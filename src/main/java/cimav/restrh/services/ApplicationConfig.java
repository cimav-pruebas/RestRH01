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
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(cimav.restrh.services.DepartamentoFacadeREST.class);
        resources.add(cimav.restrh.services.EmpleadoFacadeREST.class);
        resources.add(cimav.restrh.services.GrupoFacadeREST.class);
        resources.add(cimav.restrh.services.TabuladorFacadeREST.class);
    }
    
}
