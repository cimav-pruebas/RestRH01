/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.JustificacionRef;
import cimav.restrh.entities.Justificacion;
import cimav.restrh.tools.Numero_a_Letra;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.money.MonetaryAmount;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author calderon
 */
@Stateless
@Path("justificacion")
@PermitAll
public class JustificacionREST extends AbstractFacade<Justificacion> {

    private final static Logger logger = Logger.getLogger(JustificacionREST.class.getName());

    public JustificacionREST() {
        super(Justificacion.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
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
*/
    
    @GET
    @Path("all_by_id_empleado/{id_empleado}")
    @Produces("application/json")
    public List<Justificacion> findAllByIdEmpleado(@PathParam("id_empleado") Integer idEmpleado) {

        TypedQuery<Justificacion> query = getEntityManager().createQuery("SELECT j FROM Justificacion AS j WHERE j.empleado.id = :id_empleado", Justificacion.class);
        query.setParameter("id_empleado", idEmpleado);
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
        //List<Justificacion> emps = super.findAll();
        return results;
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
    @Produces("application/pdf")
    public Response pdficar(@DefaultValue("0") @QueryParam("id") Integer id_param) {
        Justificacion justi = (Justificacion) JustificacionREST.this.find(id_param);

    // <editor-fold defaultstate="collapsed" desc="Constantes de texto">            
        
        HashMap<String, String> mapa = new HashMap();
        mapa.put("texto1_I", "No existan bienes o servicios alternativos o sustitutos técnicamente razonables, o bien, que en el "
                + "mercado sólo existe un posible oferente, o se trate de una persona que posee la titularidad o el "
                + "licenciamiento exclusivo de patentes, derechos de autor, u otros derechos exclusivos, o por "
                + "tratarse de obras de arte.");
        mapa.put("texto1_III", "Existan circunstancias que puedan provocar pérdidas o costos adicionales importantes, "
                + "cuantificados y justificados.");
        mapa.put("texto1_XIV", "Se trate de los servicios prestados por una persona física a que se refiere la fracción "
                + "VII del artículo 3 de esta Ley, siempre que éstos sean realizados por ella misma sin "
                + "requerir de la utilización de más de un especialista o técnico.");
        mapa.put("texto1_XV", "Se trate de servicios de mantenimiento de bienes en los que no sea posible precisar "
                + "su alcance, establecer las cantidades de trabajo o determinar las especificaciones "
                + "correspondientes.");
        mapa.put("texto1_XVII", "Se trate de equipos especializados, sustancias y materiales de origen químico, físico "
                + "químico o bioquímico para ser utilizadas en actividades experimentales requeridas "
                + "en proyectos de investigación científica y desarrollo tecnológico, siempre que dichos "
                + "proyectos se encuentren autorizados por quien determine el titular de la dependencia "
                + "o el órgano de gobierno de la entidad.");
        mapa.put("plazo_0", "El plazo en que se requiere el suministro de los " + justi.getBienServicioTxt() + ", corresponde al periodo del "
                + justi.getFechaInicio().getDayOfMonth() + " de "
                + justi.getFechaInicio().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " de " + justi.getFechaInicio().getYear()
                + " y hasta el " + justi.getFechaTermino().getDayOfMonth() + " de "
                + justi.getFechaTermino().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " de " + justi.getFechaTermino().getYear()
                + ". Las condiciones en las que se "
                + "entregarán los " + justi.getBienServicioTxt() + " son las siguientes:\n\n " + justi.getCondicionesPago());
        mapa.put("plazo_1", "La fecha en que se requiere el suministro de los " + justi.getBienServicioTxt() + ", corresponde al día "
                + justi.getFechaTermino().getDayOfMonth() + " de "
                + justi.getFechaTermino().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " de " + justi.getFechaTermino().getYear()
                + ". Las condiciones en las que se "
                + "entregarán los " + justi.getBienServicioTxt() + " son las siguientes:\n\n " + justi.getCondicionesPago());
        mapa.put("plazo_2", "El plazo en que se requiere el suministro de los " + justi.getBienServicioTxt() + ", corresponde a los " + justi.getNumDiasPlazo()
                + " despues de la elabora...-----------"
                + ". Las condiciones en las que se "
                + "entregarán los " + justi.getBienServicioTxt() + " son las siguientes:\n\n " + justi.getCondicionesPago());
        mapa.put("nota_1", "Asimismo se hace constar mediante el sello y firma del responsable del área de "
                + "Almacén, la No Existencia de Bienes o Nivel de Inventario que demuestra que se "
                + "cumplió con lo establecido en el artículo 27 del RLAASP.");
        mapa.put("transparencia_unico", "Para la integración del procedimiento de contratación por adjudicación directa, los servidores "
                + "públicos de las áreas requirentes han tenido acceso de manera oportuna, clara y completa de "
                + "las características requeridas de los " + justi.getBienServicioTxt() + " con el fin de demostrar que es "
                + "el único proveedor que proporciona los " + justi.getBienServicioTxt() + " que se pretenden contratar, en "
                + "el entendido que para garantizar la transparencia del procedimiento de contratación, la "
                + "información respectiva será incorporada al Sistema de Compras Gubernamentales "
                + "(CompraNet), en los términos de las disposiciones legales aplicables, "
                + "Lo anterior de acuerdo con lo establecido en el numeral 4.2.4 (ADJUDICACIÓN DIRECTA) y "
                + "numeral 4.2.4.1.1 (Verificar Acreditamiento de Excepción) del Acuerdo por el que se modifica el "
                + "Manual Administrativo de Aplicación General en Materia de Adquisiciones, Arrendamientos y "
                + "Servicios del Sector Público, publicado en el Diario Oficial de la Federación el 21 de noviembre "
                + "de 2012.");
        mapa.put("transparencia_no_unico", "Todas las personas que han presentado cotización para la integración del procedimiento de "
                + "contratación por adjudicación directa, han tenido acceso de manera oportuna, clara y completa "
                + "de las características requeridas de los " + justi.getBienServicioTxt() + ", en el entendido que para "
                + "garantizar la transparencia del procedimiento de contratación, la información respectiva será "
                + "incorporada al Sistema de Compras Gubernamentales (CompraNet), en los términos de las "
                + "disposiciones legales aplicables. "
                + "Lo anterior de acuerdo con lo establecido en el numeral 4.2.4 (ADJUDICACIÓN DIRECTA) y "
                + "numeral 4.2.4.1.1 (Verificar Acreditamiento de Excepción) del Acuerdo por el que se modifica el "
                + "Manual Administrativo de Aplicación General en Materia de Adquisiciones, Arrendamientos y "
                + "Servicios del Sector Público, publicado en el Diario Oficial de la Federación el 21 de noviembre "
                + "de 2012.");

    // </editor-fold>

        
        
        StreamingOutput streamingOutput = new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {

                try {
                    
                    //Create Document instance.
                    Document document = new Document();
                    PdfWriter.getInstance(document, outputStream);
                    
                    document.addAuthor("Generador adquisiciones | " + justi.getEmpleado().getCuentaCimav());
                    String fileName1 =  (justi.getRequisicion() + "-" + justi.getEmpleado().getCuentaCimav() ).replace(" ", "");
                    document.addTitle("Justificación: " + fileName1);
                    document.addSubject("Justificación de Requisición");
                    
                    document.open();

                    Paragraph parrafo = new Paragraph(
                            "Centro de Investigación en Materiales Avanzados S. C.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    document.add(parrafo);

                    parrafo = new Paragraph("JUSTIFICACIÓN PARA ACREDITAR Y FUNDAR PROCEDIMIENTOS DE "
                            + "CONTRATACIÓN POR ADJUDICACIÓN DIRECTA, COMO EXCEPCIÓN AL DE "
                            + "LICITACIÓN PÚBLICA EN EL SUPUESTO DEL ARTICULO 41 FRACCION " + justi.getRomano() + " DE LA "
                            + "LEY DE ADQUISICIONES, ARRENDAMIENTOS Y SERVICIOS DEL SECTOR "
                            + "PÚBLICO.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    document.add(parrafo);

                    parrafo = new Paragraph("COMITÉ DE ADQUISICIONES, ARRENDAMIENTOS Y SERVICIOS");
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setSpacingBefore(20);
                    parrafo.setIndentationLeft(80);
                    parrafo.setIndentationRight(80);
                    document.add(parrafo);

                    parrafo = new Paragraph("P R E S E N T E:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setIndentationLeft(30);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph("Oficio número: ",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setIndentationLeft(300);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);

                    Chunk frase = new Chunk(justi.getRequisicion() + "\n",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);
                    frase = new Chunk("Asunto: Se emite justificación por la que se "
                            + "acredita y funda la contratación por adjudicación directa que se indica.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk("En cumplimiento a lo establecido en el segundo párrafo del artículo 40 de la Ley de "
                            + "Adquisiciones, Arrendamientos y Servicios del Sector Público, así como en el artículo 71 del "
                            + "Reglamento de la Ley de Adquisiciones, Arrendamientos y Servicios del Sector Público, y con el "
                            + "carácter de Titular del Área Requirente, por este conducto hago constar el acreditamiento del o "
                            + "de los criterios, razones, fundamentos y motivos para no llevar a cabo el procedimiento de "
                            + "licitación pública y celebrar la contratación a través del procedimiento de adjudicación directa en "
                            + "los términos establecidos en el artículo 41 Fracción " + justi.getRomano() + " de la Ley de Adquisiciones, "
                            + "Arrendamientos y Servicios del Sector Público, que a la letra menciona ",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.add(frase);

                    frase = new Chunk("“Las dependencias y "
                            + "entidades, bajo su responsabilidad, podrán contratar adquisiciones, arrendamientos y "
                            + "servicios, sin sujetarse al procedimiento de licitación pública, a través de los "
                            + "procedimientos de invitación a cuando menos tres personas o de adjudicación directa, "
                            + "cuando:\n" + mapa.get("texto1_" + justi.getRomano()),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("Para tal efecto presento la siguiente información:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(60);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    document.add(parrafo);

                    parrafo = new Paragraph("I.- DESCRIPCIÓN DE LOS " + justi.getBienServicioTxt().toUpperCase(),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setSpacingAfter(10);
                    document.add(parrafo);

                    parrafo = new Paragraph("El/Los " + justi.getBienServicioTxt() + " que se pretende contratar, son los siguientes:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.setIndentationLeft(30);
                    document.add(parrafo);

                    parrafo = new Paragraph(justi.getDescripcion(),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.setSpacingBefore(20);
                    parrafo.setLeading(15);
                    parrafo.setIndentationLeft(30);
                    document.add(parrafo);

                    parrafo = new Paragraph("II.- PLAZOS Y CONDICIONES DEL SUMINISTRO DE LOS " + justi.getBienServicioTxt().toUpperCase(),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph(mapa.get("plazo_" + justi.getPlazo()),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.setSpacingBefore(20);
                    parrafo.setLeading(15);
                    parrafo.setIndentationLeft(30);
                    document.add(parrafo);

                    parrafo = new Paragraph("III.- RESULTADO DE LA INVESTIGACIÓN DE MERCADO",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph("La Investigación de Mercado fue realizada en los términos de los artículos 28, 29 y 30 del "
                            + "Reglamento de la Ley de Adquisiciones, Arrendamientos y Servicios del Sector Público, en "
                            + "forma conjunta por el Área Requirente y el Área Contratante, en la cual se verificó previo al "
                            + "inicio del procedimiento de contratación, la existencia de oferta, en la cantidad, calidad y "
                            + "oportunidad requeridas; la existencia de proveedores a nivel nacional o internacional con "
                            + "posibilidad de cumplir con las necesidades de la contratación, conocer el precio prevaleciente al "
                            + "momento de llevar a cabo la Investigación de mercado así como en la información disponible "
                            + "en el Sistema informático denominado COMPRANET:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.setSpacingBefore(20);
                    parrafo.setSpacingAfter(20);
                    parrafo.setLeading(15);
                    parrafo.setIndentationLeft(30);
                    document.add(parrafo);

                    PdfPTable table = new PdfPTable(2); // 3 columns.

                    table.setWidths(new int[]{100, 50});

                    if (justi.getEsUnico()) {
                        PdfPCell cell1 = new PdfPCell(new Paragraph(justi.getProveedorUno().toUpperCase(),
                                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                        PdfPCell cell2 = new PdfPCell(new Paragraph(montoFormatComas(justi.getMontoUno()),
                                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                        cell1.setBorder(PdfPCell.NO_BORDER);
                        cell2.setBorder(PdfPCell.NO_BORDER);
                        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell1);
                        table.addCell(cell2);
                        document.add(table);
                        parrafo = new Paragraph("Concluyendo que es la única oferta en cuanto a obtener las mejores condiciones, calidad, "
                                + "precio, oportunidad y financiamiento, por ser el único proveedor que proporcione los " + justi.getBienServicioTxt()
                                + " que se pretende contratar la de " + justi.getProveedorUno().toUpperCase() + ". La referida "
                                + "Investigación de Mercado se acompaña a la presente justificación para determinar que el "
                                + "procedimiento de contratación por adjudicación directa es el idóneo.",
                                new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                        parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                        parrafo.setSpacingBefore(20);
                        parrafo.setLeading(15);
                        parrafo.setIndentationLeft(30);

                        document.add(parrafo);
                    } else {
                        PdfPCell cell1 = new PdfPCell(new Paragraph(justi.getProveedorUno().toUpperCase(),
                                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                        PdfPCell cell2 = new PdfPCell(new Paragraph(montoFormatComas(justi.getMontoUno()),
                                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                        cell1.setBorder(PdfPCell.NO_BORDER);
                        cell2.setBorder(PdfPCell.NO_BORDER);
                        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell1);
                        table.addCell(cell2);
                        cell1 = new PdfPCell(new Paragraph(justi.getProveedorDos().toUpperCase(),
                                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                        cell2 = new PdfPCell(new Paragraph(montoFormatComas(justi.getMontoDos()),
                                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                        cell1.setBorder(PdfPCell.NO_BORDER);
                        cell2.setBorder(PdfPCell.NO_BORDER);
                        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell1);
                        table.addCell(cell2);
                        cell1 = new PdfPCell(new Paragraph(justi.getProveedorTres().toUpperCase(),
                                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                        cell2 = new PdfPCell(new Paragraph(montoFormatComas(justi.getMontoTres()),
                                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
                        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell1.setBorder(PdfPCell.NO_BORDER);
                        cell2.setBorder(PdfPCell.NO_BORDER);
                        table.addCell(cell1);
                        table.addCell(cell2);
                        document.add(table);

                        parrafo = new Paragraph("Siendo la oferta que presenta las mejores condiciones en cuanto a calidad, precio, oportunidad  "
                                + "y financiamiento, la de " + justi.getProveedorUno().toUpperCase() + ". "
                                + "La referida Investigación de Mercado se acompaña a la presente justificación para determinar  "
                                + "que el procedimiento de contratación por adjudicación directa es el idóneo.",
                                new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                        parrafo.setSpacingBefore(20);
                        parrafo.setLeading(15);
                        parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                        parrafo.setIndentationLeft(30);
                        document.add(parrafo);
                    }

                    parrafo = new Paragraph("IV.- PROCEDIMIENTO DE CONTRATACIÓN PROPUESTO",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk("El procedimiento de contratación propuesto es el de adjudicación directa, en virtud de que en el "
                            + "presente caso la adjudicación se llevaría a cabo conforme la fracción " + justi.getRomano() + " del artículo 41 el cual "
                            + "menciona que este tipo de adjudicación se puede llevar a cabo siempre y cuando: \n",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.add(frase);

                    frase = new Chunk(mapa.get("texto1_" + justi.getRomano()),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.add(frase);

                    frase = new Chunk(" Actualizándose el supuesto de excepción a la licitación pública "
                            + "establecido en la fracción " + justi.getRomano() + " del artículo 41 de la Ley de Adquisiciones, Arrendamientos y "
                            + "Servicios del Sector Público, en relación con lo establecido en el artículo 72 de su Reglamento.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("IV.1.     MOTIVACIÓN Y FUNDAMENTACIÓN LEGAL:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setSpacingBefore(20);
                    parrafo.setIndentationLeft(30);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(60);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk("A)",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);

                    frase = new Chunk("  MOTIVOS: ",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);

                    frase = new Chunk("La contratación de los " + justi.getBienServicioTxt() + " objeto de la presente justificación "
                            + "es necesaria para satisfacer los requerimientos del " + justi.getProyecto() + ". Por lo anterior, la "
                            + "contratación propuesta se adecúa al supuesto de excepción establecido en la Ley "
                            + "de Adquisiciones, Arrendamientos y Servicios del Sector Público en su artículo 41, "
                            + "fracción " + justi.getRomano() + "; además de que se reúnen los requisitos previstos en el artículo 72 del "
                            + "Reglamento de la Ley de Adquisiciones, Arrendamientos y Servicios del Sector "
                            + "Público, tal y como se desprende de la información presentada en esta justificación, "
                            + "así como de la Investigación de Mercado; "
                            + "por lo que resulta procedente la contratación bajo el procedimiento de adjudicación "
                            + "directa previsto en el artículo 26, fracción III de la Ley antes mencionada.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(60);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk("B)",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);

                    frase = new Chunk("  FUNDAMENTOS: ",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);

                    frase = new Chunk("La contratación se encuentra debidamente fundada en el artículo "
                            + "134 de la Constitución Política de los Estados Unidos Mexicanos; en los artículos 26 "
                            + "fracción III, 40 y 41 fracción " + justi.getRomano() + " de la Ley de Adquisiciones, Arrendamientos y "
                            + "Servicios del Sector Público; así como en los artículos 71 y 72 del Reglamento de la "
                            + "Ley de Adquisiciones, Arrendamientos y Servicios del Sector Público.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("V.- MONTO ESTIMADO Y FORMA DE PAGO PROPUESTO:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph("V.1.      MONTO ESTIMADO:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setSpacingBefore(20);
                    parrafo.setIndentationLeft(30);
                    document.add(parrafo);

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    parrafo = new Paragraph("El monto estimado de la contratación es la cantidad de " + montoFormatComas(justi.getSubTotal())
                            + " (" + new Numero_a_Letra().Convertir(decimalFormat.format(justi.getSubTotal().getNumber()), true) + " "
                            + justi.getSubTotal().getCurrency().getCurrencyCode() + ") más IVA, mismo que "
                            + "resultó el más conveniente de acuerdo con la Investigación de Mercado"
                            + ", mediante la cual se verificó previo al inicio del procedimiento "
                            + "de contratación, la existencia de oferta de los " + justi.getBienServicioTxt() + " en la cantidad, "
                            + "calidad y oportunidad requeridos en los términos del artículo 28 del Reglamento de la Ley de "
                            + "Adquisiciones, Arrendamientos y Servicios del Sector Público.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    document.add(parrafo);

                    parrafo = new Paragraph("V.1.      FORMA DE PAGO PROPUESTA:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setSpacingBefore(20);
                    parrafo.setIndentationLeft(30);
                    document.add(parrafo);

                    parrafo = new Paragraph("El monto total será pagado en " + justi.getNumPagos() + " pago/s de " + signoDivisa(justi.getSubTotal())
                            + String.format("%,.2f",(justi.getSubTotal().divide(justi.getNumPagos()).getNumber().doubleValue()))
                            + " (" + new Numero_a_Letra().Convertir(decimalFormat.format(justi.getSubTotal().divide(justi.getNumPagos()).getNumber()).toString(), true) + " "
                            + justi.getSubTotal().getCurrency().getCurrencyCode() + ") más IVA. Los pagos se realizarán previa verificación de la entrega y calidad de los "
                            + justi.getBienServicioTxt() + " así como previo envío en formatos .pdf y .xml del Comprobante Fiscal "
                            + "Digital por Internet (CFDI) correspondiente que reúna los requisitos fiscales respectivos. Los "
                            + "pagos se efectuarán mediante " + justi.getFormaPago(),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    document.add(parrafo);

                    parrafo = new Paragraph("VI.- PERSONA PROPUESTA PARA LA ADJUDICACIÓN DIRECTA:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph("Por lo anteriormente expuesto y fundado, se propone a " + justi.getProveedorUno().toUpperCase()
                            + ", con domicilio ubicado en " + justi.getDomicilio() + ", Registro Federal de Contribuyentes: "
                            + justi.getRfc() + ", correo electrónico: " + justi.getCorreo() + " y número telefónico " + justi.getTelefono(),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    document.add(parrafo);

                    parrafo = new Paragraph("VII.- ACREDITAMIENTO DEL O LOS CRITERIOS EN LOS QUE SE FUNDA Y MOTIVA LA "
                            + "SELECCIÓN DEL PROCEDIMIENTO DE EXCEPCIÓN A LA LICITACIÓN PÚBLICA:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setLeading(15);
                    parrafo.setIndentationLeft(30);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph("El procedimiento de contratación por adjudicación directa es el idóneo, al actualizarse el "
                            + "supuesto de excepción al procedimiento de licitación pública previsto en el artículo 41, fracción " + justi.getRomano()
                            + " de la Ley de Adquisiciones, Arrendamientos y Servicios del Sector Público, aunado a que se "
                            + "corroboró la capacidad y experiencia de la persona propuesta, quien por ser proveedor único "
                            + "presentó las mejores condiciones en cuanto a precio, calidad, financiamiento, oportunidad y "
                            + "demás circunstancias pertinentes a efecto de asegurar a esta Entidad las mejores condiciones "
                            + "para su contratación, tal y como se acredita con la información presentada en esta justificación, "
                            + "así como con la Investigación de Mercado.\n\n"
                            + "El acreditamiento del o los criterios en los que se funda la excepción de licitación pública, son "
                            + "los siguientes:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk(" -  Economía\n",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);
                    frase = new Chunk("Con la Investigación de Mercado se establecieron precios y demás condiciones de calidad, "
                            + "financiamiento y oportunidad, respecto de los " + justi.getBienServicioTxt() + " requeridos, con lo cual "
                            + "se asegura cumplir con los principios del artículo 134 de la Constitución Política de los Estados "
                            + "Unidos Mexicanos y de la Ley de Adquisiciones, Arrendamientos y Servicios del Sector Público, "
                            + "en cuanto a precio, calidad, financiamiento, oportunidad y demás circunstancias pertinentes, por "
                            + "lo que el procedimiento de adjudicación directa permite en contraposición al procedimiento de "
                            + "licitación pública, obtener con mayor oportunidad los " + justi.getBienServicioTxt() + " requeridos al "
                            + "menor costo económico para el CIMAV, S.C. según lo detallado en la investigación de mercado "
                            + "que se realizó, generando ahorro de recursos por estar proponiendo la adjudicación al "
                            + "proveedor único cuya propuesta se considera aceptable en cuanto a su solvencia. "
                            + "Lo anterior de acuerdo con lo establecido en el numeral 4.2.4 (ADJUDICACIÓN DIRECTA) y "
                            + "numeral 4.2.4.1.1 (Verificar Acreditamiento de Excepción) del Acuerdo por el que se modifica el "
                            + "Manual Administrativo de Aplicación General en Materia de Adquisiciones, Arrendamientos y "
                            + "Servicios del Sector Público, publicado en el Diario Oficial de la Federación el 21 de noviembre "
                            + "de 2012.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk(" -  Eficacia\n",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);
                    frase = new Chunk("Con el procedimiento de contratación por adjudicación directa, se logrará obtener con "
                            + "oportunidad los " + justi.getBienServicioTxt() + " atendiendo a las características requeridas en "
                            + "contraposición con el procedimiento de licitación pública, dado que se reducen tiempos y se "
                            + "generan economías; aunado a que la persona propuesta cuenta con experiencia y capacidad "
                            + "para satisfacer las necesidades requeridas, además de que es el único que ofrece las mejores "
                            + "condiciones disponibles en cuanto a precio, calidad y oportunidad, con lo que se lograría el "
                            + "cumplimiento de los objetivos y resultados deseados en el tiempo requerido, situación que se "
                            + "puede demostrar en base a la investigación de mercado. "
                            + "Lo anterior de acuerdo con lo establecido en el numeral 4.2.4 (ADJUDICACIÓN DIRECTA) y "
                            + "numeral 4.2.4.1.1 (Verificar Acreditamiento de Excepción) del Acuerdo por el que se modifica el "
                            + "Manual Administrativo de Aplicación General en Materia de Adquisiciones, Arrendamientos y "
                            + "Servicios del Sector Público, publicado en el Diario Oficial de la Federación el 21 de noviembre "
                            + "de 2012.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk(" -  Eficiencia\n",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);
                    frase = new Chunk("Con el procedimiento de adjudicación directa, a diferencia del procedimiento de licitación "
                            + "pública, se logra el uso racional de recursos con los que cuenta la Entidad para realizar la "
                            + "contratación, obteniendo las mejores condiciones de precio, calidad y oportunidad, evitando la "
                            + "pérdida de tiempo y recursos al Estado, lo cual se demuestra con la investigación de mercado "
                            + "que se realizó, quedando evidencia de su resultado ya que los recursos disponibles con los que "
                            + "cuenta el CIMAV se aplican conforme a los lineamientos de racionalidad y austeridad "
                            + "presupuestaria. "
                            + "Lo anterior de acuerdo con lo establecido en el numeral 4.2.4 (ADJUDICACIÓN DIRECTA) y "
                            + "numeral 4.2.4.1.1 (Verificar Acreditamiento de Excepción) del Acuerdo por el que se modifica el "
                            + "Manual Administrativo de Aplicación General en Materia de Adquisiciones, Arrendamientos y "
                            + "Servicios del Sector Público, publicado en el Diario Oficial de la Federación el 21 de noviembre "
                            + "de 2012.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk(" -  Imparcialidad\n",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);
                    frase = new Chunk("El tipo de adjudicación que se propone, se llevó a cabo sin prejuicios ni situaciones que "
                            + "pudieran afectar la imparcialidad, y sin que medie algún interés personal de los servidores "
                            + "públicos involucrados en la contratación o de cualquier otra índole que pudiera otorgar "
                            + "condiciones ventajosas a alguna persona, en relación con los demás ni limitar la libre "
                            + "participación, esto debido a que es proveedor único, dicha situación queda demostrada "
                            + "conforme al resultado que se da con base a la investigación de mercado. "
                            + "Lo anterior de acuerdo con lo establecido en el numeral 4.2.4 (ADJUDICACIÓN DIRECTA) y "
                            + "numeral 4.2.4.1.1 (Verificar Acreditamiento de Excepción) del Acuerdo por el que se modifica el "
                            + "Manual Administrativo de Aplicación General en Materia de Adquisiciones, Arrendamientos y "
                            + "Servicios del Sector Público, publicado en el Diario Oficial de la Federación el 21 de noviembre "
                            + "de 2012.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.add(frase);
                    document.add(parrafo);

                    parrafo = new Paragraph("",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setSpacingAfter(20);
                    parrafo.setIndentationLeft(30);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                    frase = new Chunk(" -  Honradez\n",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.add(frase);
                    frase = new Chunk("La selección del procedimiento de adjudicación directa tiene como único fin contratar bajo "
                            + "las mejores condiciones los " + justi.getBienServicioTxt() + " requeridos, actuando con rectitud, "
                            + "responsabilidad e integridad y con apego estricto al marco jurídico aplicable, evitando así "
                            + "incurrir en actos de corrupción y conflictos de interés, ya que por parte de los servidores "
                            + "públicos que intervinieron en este procedimiento quedo evidenciado que no se ha favorecido a "
                            + "persona alguna interesada en la contratación ya que en base a la investigación de mercado "
                            + "queda demostrado que es proveedor único.\n\n"
                            + "Lo anterior de acuerdo con lo establecido en el numeral 4.2.4 (ADJUDICACIÓN DIRECTA) y "
                            + "numeral 4.2.4.1.1 (Verificar Acreditamiento de Excepción) del Acuerdo por el que se modifica el "
                            + "Manual Administrativo de Aplicación General en Materia de Adquisiciones, Arrendamientos y "
                            + "Servicios del Sector Público, publicado en el Diario Oficial de la Federación el 21 de noviembre "
                            + "de 2012.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.add(frase);
                    document.add(parrafo);

                    if (justi.getEsUnico()) {
                        parrafo = new Paragraph("",
                                new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                        parrafo.setSpacingAfter(20);
                        parrafo.setIndentationLeft(30);
                        parrafo.setLeading(15);
                        parrafo.setSpacingBefore(20);
                        parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                        frase = new Chunk(" -  Transparencia\n",
                                new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                        parrafo.add(frase);
                        frase = new Chunk(mapa.get("transparencia_unico"),
                                new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                        parrafo.add(frase);
                        document.add(parrafo);
                    } else {
                        parrafo = new Paragraph("",
                                new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                        parrafo.setSpacingAfter(20);
                        parrafo.setIndentationLeft(30);
                        parrafo.setLeading(15);
                        parrafo.setSpacingBefore(20);
                        parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

                        frase = new Chunk(" -  Transparencia\n",
                                new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                        parrafo.add(frase);
                        frase = new Chunk(mapa.get("transparencia_no_unico"),
                                new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                        parrafo.add(frase);
                        document.add(parrafo);
                    }

                    parrafo = new Paragraph("VIII.- LUGAR Y FECHA DE EMISIÓN:",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph("En la Ciudad de Chihuahua, Estado de Chihuahua a los " + justi.getFechaElaboracion().getDayOfMonth()
                            + " días del mes de " + justi.getFechaElaboracion().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " de "
                            + justi.getFechaElaboracion().getYear() + ", se emite la presente justificación para los efectos legales a que haya lugar.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.setLeading(15);
                    parrafo.setIndentationLeft(30);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    parrafo = new Paragraph("En cumplimiento a lo establecido en el penúltimo párrafo del artículo 71 del Reglamento "
                            + "de la Ley de Adquisiciones, Arrendamientos y Servicios del Sector Público, se acompaña a la "
                            + "presente como “ANEXO DOS”, la Requisición o Solicitud de Contratación (Requisición) A la "
                            + "cual se deberá anexar, mediante sello del departamento de Presupuesto, la Constancia con la "
                            + "que se acredita la existencia de recursos para iniciar el procedimiento de contratación.",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL));
                    parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                    parrafo.setLeading(15);
                    parrafo.setIndentationLeft(30);
                    parrafo.setSpacingBefore(20);
                    document.add(parrafo);

                    if (justi.getEsUnico()) {
                        parrafo = new Paragraph(mapa.get("nota_1"),
                                new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD));
                        parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
                        parrafo.setLeading(15);
                        parrafo.setIndentationLeft(30);
                        parrafo.setSpacingBefore(20);
                        document.add(parrafo);
                    }

                    parrafo = new Paragraph("ATENTAMENTE",
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(60);
                    document.add(parrafo);

                    parrafo = new Paragraph(justi.getAutoriza().getName() + "\n" + justi.getAutorizaCargo().toUpperCase(),
                            new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD));
                    parrafo.setAlignment(Element.ALIGN_CENTER);
                    parrafo.setLeading(15);
                    parrafo.setSpacingBefore(60);
                    document.add(parrafo);

                    document.close();
                    outputStream.close();

                } catch (DocumentException ex) {
                    Logger.getLogger(JustificacionREST.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        
        ResponseBuilder response = Response.ok(streamingOutput);
        String fileName =  ("inline; filename=" + justi.getRequisicion() + "-" + justi.getEmpleado().getCuentaCimav() + ".pdf").replace(" ", "");
        response.header("Content-Disposition", fileName);
        
        return response.build();
    }

    //@Context private HttpServletResponse response;
    
    public char signoDivisa(MonetaryAmount monto) {
        char moneda = '$';
        switch (monto.getCurrency().getCurrencyCode()) {
            case "MXN":
                moneda = '$';
                break;
            case "USD":
                moneda = '$';
                break;
            case "EUR":
                moneda = '€';
                break;
        }
        return moneda;
    }
    
    public String montoFormatComas(MonetaryAmount monto) {
        return monto.getCurrency().getCurrencyCode() + " " + signoDivisa(monto) + String.format("%,.2f", monto.getNumber().doubleValue());
    }

}
