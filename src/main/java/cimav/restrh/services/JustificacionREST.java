/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.JustificacionRef;
import cimav.restrh.entities.Justificacion;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 *
 * @author calderon
 */
@Stateless
@Path("justificacion")
@PermitAll
public class JustificacionREST extends AbstractFacade<Justificacion>{

    private final static Logger logger = Logger.getLogger(JustificacionREST.class.getName() ); 

    public JustificacionREST() {
        super(Justificacion.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("one_by_id_empleado/{id_empleado}")
    @Produces("application/json")
    public Justificacion findOneByIdEmpleado(@PathParam("id_empleado") Integer idEmpleado) {
        Query query = getEntityManager().createQuery("SELECT j FROM Justificacion AS j WHERE j.empleado.id = :id_empleado", Justificacion.class);
        query.setParameter("id_empleado", idEmpleado);
        try {
            Justificacion justificacion = (Justificacion) query.getSingleResult();
            return justificacion;
        } catch (NoResultException | NonUniqueResultException nue) {
            Justificacion justificacion = new Justificacion();
            justificacion.setId(-1);
            return justificacion;
        }
    }

    @GET
    @Path("all_by_id_empleado/{id_empleado}")
    @Produces("application/json")
    public List<Justificacion> findAllByIdEmpleado(@PathParam("id_empleado") Integer idEmpleado) {
        
        TypedQuery<Justificacion> query = getEntityManager().createQuery("SELECT j FROM Justificacion AS j", Justificacion.class);
        List<Justificacion> results = query.getResultList();        
        /*
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        ParameterExpression<Integer> p = cb.parameter(Integer.class);
        Root<Justificacion> j = cq.from(JustificacionRef.class);
        cq.select(j).where(cb.gt(j.get("empleado.Id"), p));
        
        TypedQuery<Justificacion> query = getEntityManager().createQuery(cq);
        query.setParameter(p, idEmpleado);
        List<Justificacion> results = query.getResultList();
        */
        /*
        Query query = getEntityManager().createQuery("SELECT j FROM JustificacionRef AS j WHERE j.empleado.id = :id_empleado", JustificacionRef.class);
        query.setParameter("id_empleado", idEmpleado);
        List<Justificacion> result = new ArrayList<>();
        try {
            result= query.getResultList();
        } catch (NoResultException nue) {
        }
*/
        List<Justificacion> emps = super.findAll();
        return emps;
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Justificacion insert(Justificacion entity) {
        super.insert(entity); // <-- regresa con el Id nuevo, code, consecutivo y resto de los campos
        return entity; 
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void edit(@PathParam("id") Integer id, Justificacion entity) {
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
    public JustificacionRef find(@PathParam("id") Integer id) {
        return super.find(id); 
    }

    @GET
    @Override
    @Produces("application/json")
    public List<Justificacion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("pdficar")
    @QueryParam("id")
    @Produces("application/pdf")
    public StreamingOutput pdficar(@QueryParam("id")Integer id_param) {
        Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, "DEBBUG::>> Uno ");
        
        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                
                Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, "DEBBUG::>> Dentro");
                
                try {
                    HashMap hmParams = new HashMap();
                    hmParams.put("id_justi", 18);
                    hmParams.put("real_path", "/jasper/");
                    
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, "DEBBUG::>> Before JR");
                    
                    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("/jasper/JustiRpt.jasper"));
                    //JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResource("/jasper/JustiRpt.jrxml").getFile());
                    
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, "DEBBUG::>> After JR");
                    
                    //JasperReport jasperReport=JasperCompileManager.compileReport("jasper/JustiRpt.jasper");
                    
                    Driver postgresDriver = new org.postgresql.Driver();
                    DriverManager.registerDriver(postgresDriver);
                    Connection connPostgres = DriverManager.getConnection("jdbc:postgresql://10.0.4.40:5432/rh_production", "rh_user", "rh_1ser");
                    Statement stmtPostgres = connPostgres.createStatement();
                    ResultSet rsPostgres= stmtPostgres.executeQuery(
                            "select j.*, e1.nombre as nombre_solicitante, e2.nombre as nombre_elaboro, e3.nombre as nombre_autorizo, \n" +
                            "case  when (bienoservicio = 0) then 'Bien' else 'Servicio' end as BienOServicio_txt,\n" +
                            "case  when (id_moneda = 0) then 'MXN' else 'USD' end as moneda_txt\n" +
                            "from justificaciones j \n" +
                            "left join empleados e1 on j.id_empleado = e1.id\n" +
                            "left join empleados e2 on j.id_empleado_elaboro = e2.id\n" +
                            "left join empleados e3 on j.id_empleado_autorizo = e3.id\n" +
                            "where j.id = " + id_param + " "
                    );
                    
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, "DEBBUG::>> En JDBC ");
                    
                    JRDataSource ds = new JRResultSetDataSource(rsPostgres);
                    
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, "DEBBUG::>> DS Ok");
                    
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hmParams, ds);
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                    SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                    configuration.setMetadataAuthor("GeneradorJustificaciones"); //Set your pdf configurations, 
                    exporter.setConfiguration(configuration);
                    exporter.exportReport();
                    
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, "DEBBUG::>> END ");
                    
                    //            try {
                    //                PDFGenerator generator = new PDFGenerator(getEntity());
                    //                generator.generatePDF(output);
                    //            } catch (Exception e) {
                    //                throw new WebApplicationException(e);
                    //            }
                } catch (JRException ex) {
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        //return Response.ok().build();
    }
    
}
