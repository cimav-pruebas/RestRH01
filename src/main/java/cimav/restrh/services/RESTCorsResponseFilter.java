/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author juan.calderon
 */
@Provider
@PreMatching
public class RESTCorsResponseFilter implements ContainerResponseFilter {

    //private final static Logger log = Logger.getLogger(RESTCorsResponseFilter.class.getName() ); 
    
   @Override
   public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext cres) throws IOException {
       
        //log.info( ">>>>>>>>>>> CERO <<<<<<<<<<<<<<< Executing REST response filter" );
       
        cres.getHeaders().add("Access-Control-Allow-Origin", "*");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET, HEAD, POST, OPTIONS, PUT,DELETE");
//        cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
//        cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, X-HTTP-Method-Override, Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
        cres.getHeaders().add("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, X-HTTP-Method-Override, Origin, X-Requested-With, Accept");    
        cres.getHeaders().add("Access-Control-Max-Age","86400"); // "1209600"); // 14 días
        
        
//        cres.getHeaders().add("Access-Control-Allow-Origin", "*");
  //      cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
        //cres.getHeaders().add("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
//        cres.getHeaders().add("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");    
        
   }

}
