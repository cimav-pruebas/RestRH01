/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Moneda;
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
@Path("moneda")
@PermitAll
public class MonedaREST extends AbstractFacade<Moneda>{

    public MonedaREST() {
        super(Moneda.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Override
    @Produces("application/json")
    public List<Moneda> findAll() {
        return super.findAll();
    }
    
}
