/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.JustificacionTipo;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author calderon
 */
@Stateless
@Path("justificacion_tipo")
@PermitAll
public class JustificacionTipoREST extends AbstractFacade<JustificacionTipo>{

    public JustificacionTipoREST() {
        super(JustificacionTipo.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Override
    @Produces("application/json")
    public List<JustificacionTipo> findAll() {
        return super.findAll();
    }
    
}
