/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Concepto;
import cimav.restrh.entities.EGrupo;
import cimav.restrh.entities.EmpleadoNomina;
import cimav.restrh.entities.NominaQuincenal;
import cimav.restrh.entities.Tabulador;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author juan.calderon
 */
@Stateless
@Path("calculo")
public class CalculoREST {

    // http://stackoverflow.com/questions/1359817/using-bigdecimal-to-work-with-currencies
    public final int BIG_SCALE = 5;

    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public CalculoREST() {
    }

    public EntityManager getEntityManager() {
        return em;
    }

    /* Code de Conceptos en la DB */
    private final String SUELDO_ORDINARIO = "00001";
    private final String SUELDO_DIAS_DESCANSO = "00003";
    private final String HONORARIOS_ASIMILABLES = "00027"; // sueldo

    private final Integer DIAS_MES = 30;
    private final Integer DIAS_QUINCENA = 15;
    private final Integer DIAS_ORDINARIOS = 11;
    private final Integer DIAS_DESCANSO = 4;

    private int idEmpleado;

    @GET
    @Path("{idEmpleado}")
    @Produces("application/json")
    public String calcular(@PathParam("idEmpleado") int idEmpleado) {

        this.idEmpleado = idEmpleado;

        BigDecimal sueldo_base_mes;
        BigDecimal sueldo_base_dia;
        BigDecimal sueldo_ordinario = BigDecimal.ZERO;
        BigDecimal sueldo_dias_descanso = BigDecimal.ZERO;
        BigDecimal sueldo_honorarios = BigDecimal.ZERO;

        try {

            EmpleadoNomina empleadoNomina = getEntityManager().find(EmpleadoNomina.class, this.idEmpleado);
            if (empleadoNomina == null) {
                throw new NullPointerException("EMPLEADO");
            }

            Tabulador nivel = empleadoNomina.getNivel();
            if (nivel == null) {
                throw new NullPointerException("NIVEL");
            }

            if (EGrupo.HON.getId() == empleadoNomina.getIdGrupo()) {
                // HON
                sueldo_base_mes = nivel.getHonorarios();
                if (sueldo_base_mes == null) {
                    throw new NullPointerException("SUELDO_BASE_MES-HONORARIOS");
                }

                sueldo_base_dia = sueldo_base_mes.divide(new BigDecimal(DIAS_MES), BIG_SCALE, RoundingMode.HALF_UP);

                sueldo_honorarios = sueldo_base_dia.multiply(new BigDecimal(DIAS_QUINCENA), MathContext.UNLIMITED);
            } else {
                // CYT, MMS, AYA
                sueldo_base_mes = nivel.getSueldo();
                if (sueldo_base_mes == null) {
                    throw new NullPointerException("SUELDO_BASE_MES");
                }

                sueldo_base_dia = sueldo_base_mes.divide(new BigDecimal(DIAS_MES), BIG_SCALE, RoundingMode.HALF_UP);

                sueldo_ordinario = sueldo_base_dia.multiply(new BigDecimal(DIAS_ORDINARIOS), MathContext.UNLIMITED);
                sueldo_dias_descanso = sueldo_base_dia.multiply(new BigDecimal(DIAS_DESCANSO), MathContext.UNLIMITED);
            }

        } catch (NullPointerException e) {
            return "-1";
        }

        try {
            // sueldo ordinario
            insertarMov(SUELDO_ORDINARIO, sueldo_ordinario);

            // sueldo dias descanso
            insertarMov(SUELDO_DIAS_DESCANSO, sueldo_dias_descanso);

            // sueldo dias descanso
            insertarMov(HONORARIOS_ASIMILABLES, sueldo_honorarios);

        } catch (NullPointerException | RollbackException ex) {
            return "-2";
        }

        return "0";
    }

    private void insertarMov(String strConcepto, BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) == 0) {
            // insertar solo los mayores a cero
            return;
        }
        // obtener concepto
        Concepto concepto = finConceptoByCode(strConcepto);
        if (concepto == null) {
            throw new NullPointerException(strConcepto);
        }
        // movimiento
        NominaQuincenal nomQuin = this.findNominaQuincenal(this.idEmpleado, concepto);
        if (nomQuin == null) {
            // es creaciÃ³n
            nomQuin = new NominaQuincenal(this.idEmpleado, concepto, monto);
        } else {
            // es update
            nomQuin.setCantidad(monto);
        }
        // persistirlo
        getEntityManager().persist(nomQuin);
    }

    public Concepto finConceptoByCode(String code) {
        try {
            return (Concepto) getEntityManager().createNamedQuery("Concepto.findByCode").setParameter("code", code).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public NominaQuincenal findNominaQuincenal(Integer idEmpleado, Concepto concepto) {
        try {
            return (NominaQuincenal) getEntityManager().createNamedQuery("NominaQuincenal.findBy_IdEmpleado_Concepto")
                    .setParameter("id_empleado", idEmpleado).setParameter("concepto", concepto).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
