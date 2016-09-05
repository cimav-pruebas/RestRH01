/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Movimiento;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.money.MonetaryAmount;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
//import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
//import net.sf.dynamicreports.report.builder.DynamicReports;
//import static net.sf.dynamicreports.report.builder.DynamicReports.col;
//import net.sf.dynamicreports.report.builder.column.Columns;
//import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
//import net.sf.dynamicreports.report.builder.component.Components;
//import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
//import net.sf.dynamicreports.report.builder.datatype.DataTypes;
//import net.sf.dynamicreports.report.builder.datatype.DoubleType;
//import net.sf.dynamicreports.report.builder.datatype.NumberType;
//import net.sf.dynamicreports.report.builder.datatype.StringType;
//import net.sf.dynamicreports.report.constant.HorizontalAlignment;
//import net.sf.dynamicreports.report.exception.DRException;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.javamoney.moneta.spi.DefaultNumberValue;

/**
 *
 * @author juan.calderon
 */
@Stateless
@Path("reportes")
public class Reportes {
    
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    @EJB
    private MovimientoFacadeREST nominaQuincenalFacadeREST;
    
    public Reportes() {
    }
/*
    @GET
    @Path("{id}")
    @Produces("application/pdf")
    public Response testing(@PathParam("id") Integer id) {
        
        List<Movimiento> list = nominaQuincenalFacadeREST.findByIdEmpleado(id);
        
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(list);
        TextColumnBuilder<DefaultNumberValue> cantidadColumna = col.column("Cantidad", "cantidad.number", DefaultNumberValue.class);
        cantidadColumna.setPattern("$##,###,###.00");
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); 
	JasperReportBuilder report = DynamicReports.report();//a new report
	report
	  .columns(
	      Columns.column("Code", "concepto.code", DataTypes.stringType()),
	      Columns.column("Tipo", "concepto.idTipoConcepto", DataTypes.characterType()),
	      Columns.column("Movimiento", "concepto.idTipoMovimiento", DataTypes.characterType()),
	      Columns.column("Concepto", "concepto.name", DataTypes.stringType()),
              cantidadColumna,
	      Columns.column("Cantidad", "aliasCantidad", DataTypes.bigDecimalType()))
	  .title(//title of the report
	      Components.text("Testing...")
		  .setHorizontalAlignment(HorizontalAlignment.CENTER))
		  .pageFooter(Components.pageXofY())//show page number on the page footer
		  .setDataSource(beanCollectionDataSource);
        
        try {
            //export the report to a pdf file
            report.toPdf(byteArrayOutputStream);  
        } catch (DRException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Response.ResponseBuilder res = Response.ok(byteArrayOutputStream.toByteArray(), "application/pdf");
        Response r = res.build();
        return r; 
    }
  */      
}
