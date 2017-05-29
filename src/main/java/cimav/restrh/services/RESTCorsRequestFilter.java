/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author juan.calderon
 */
@Provider
@PreMatching
public class RESTCorsRequestFilter implements ContainerRequestFilter {

    //private final static Logger log = Logger.getLogger( RESTCorsRequestFilter.class.getName() );

    @Override
    public void filter( ContainerRequestContext requestCtx ) throws IOException {
        
       // log.info( ">>>>>>>>>>> UNO <<<<<<<<<<<<<<< Executing REST request filter" );

       // http://stackoverflow.com/questions/1256593/why-am-i-getting-an-options-request-instead-of-a-get-request

        requestCtx.getHeaders().add("Access-Control-Allow-Origin", "*");
        requestCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
        requestCtx.getHeaders().add("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT,DELETE");
//        cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, X-HTTP-Method-Override, Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
        requestCtx.getHeaders().add("Access-Control-Allow-Headers", "authorization, content-type, Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, X-HTTP-Method-Override, Origin, X-Requested-With, Accept");    
        requestCtx.getHeaders().add("Access-Control-Max-Age", "1209600"); // 14 días
       
        // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
         //   log.info( "HTTP Method (OPTIONS) - Detected!" );

            // Just send a OK signal back to the browser
            requestCtx.abortWith( Response.status( Response.Status.OK ).build() );
        }
        
        
    }
}