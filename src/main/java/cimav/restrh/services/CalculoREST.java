/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.services;

import cimav.restrh.entities.Concepto;
import cimav.restrh.entities.EmpleadoBase;
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
    
    @PersistenceContext(unitName = "PU_JPA")
    private EntityManager em;

    public CalculoREST() {
    }
    
    public EntityManager getEntityManager() {
        return em;
    }
    
    private final String SUELDO_ORDINARIO = "00001";
    private final String SUELDO_DIAS_DESCANSO = "00003";
    
    private final Integer DIAS_MES = 30;
    private final Integer DIAS_ORDINARIOS = 11;
    private final Integer DIAS_DESCANSO = 4;

    @GET
    @Path("{idEmpleado}")
    @Produces("text/plain")
    public String calcular(@PathParam("idEmpleado") int idEmpleado) {
        
        BigDecimal sueldo_ordinario = BigDecimal.ZERO;
        BigDecimal sueldo_dias_descanso = BigDecimal.ONE;
        
        try {
            EmpleadoNomina empleadoNomina = getEntityManager().find(EmpleadoNomina.class, idEmpleado);
            if(empleadoNomina == null) throw new NullPointerException("EMPLEADO");

            Tabulador nivel = empleadoNomina.getNivel();
            if (nivel == null) throw new NullPointerException("NIVEL");

            BigDecimal sueldo_base_mes = nivel.getSueldo();
            if (sueldo_base_mes == null) throw new NullPointerException("SUELD_BASE_MES");

            BigDecimal sueldo_base_dia = sueldo_base_mes.divide(new BigDecimal(DIAS_MES), RoundingMode.HALF_UP);

            sueldo_ordinario = sueldo_base_dia.multiply(new BigDecimal(DIAS_ORDINARIOS), MathContext.UNLIMITED);
            sueldo_dias_descanso = sueldo_base_dia.multiply(new BigDecimal(DIAS_DESCANSO), MathContext.UNLIMITED);
            
        } catch (NullPointerException e) {
            return "-1";
        }

        try { 
            // sueldo ordinario
            Concepto conceptoSueldoOrdinario = finConceptoByCode(SUELDO_ORDINARIO);
            if (conceptoSueldoOrdinario == null) throw new NullPointerException(SUELDO_ORDINARIO);
            NominaQuincenal nq_sueldo_ordinario = this.findNomina(idEmpleado, conceptoSueldoOrdinario);
            if (nq_sueldo_ordinario == null) {
                nq_sueldo_ordinario = new NominaQuincenal(idEmpleado, conceptoSueldoOrdinario, sueldo_ordinario);
            } else {
                nq_sueldo_ordinario.setCantidad(sueldo_ordinario);
            } 
            getEntityManager().persist(nq_sueldo_ordinario);
            
            // sueldo dias descanso
            Concepto conceptoSueldoDescanso = finConceptoByCode(SUELDO_DIAS_DESCANSO);
            if (conceptoSueldoDescanso == null) throw new NullPointerException(SUELDO_DIAS_DESCANSO);
            NominaQuincenal nq_sueldo_descanso = this.findNomina(idEmpleado, conceptoSueldoDescanso);
            if (nq_sueldo_descanso == null) {
                nq_sueldo_descanso = new NominaQuincenal(idEmpleado, conceptoSueldoDescanso, sueldo_dias_descanso);
            } else {
                nq_sueldo_descanso.setCantidad(sueldo_dias_descanso);
            }
            getEntityManager().persist(nq_sueldo_descanso);
            
        } catch(NullPointerException | RollbackException ex) {
            return "-2";
        }
        
        return "0";
    }
    
 public Concepto finConceptoByCode(String code) {
    try {
        return (Concepto) getEntityManager().createNamedQuery("Concepto.findByCode").setParameter("code", code).getSingleResult();
    } catch (NoResultException e) {
        return null;
    }
  }
 
 public NominaQuincenal findNomina(Integer idEmpleado, Concepto concepto) {
    try {
        return (NominaQuincenal) getEntityManager().createNamedQuery("NominaQuincenal.findBy_IdEmpleado_Concepto")
                .setParameter("id_empleado", idEmpleado).setParameter("concepto", concepto).getSingleResult();
    } catch (NoResultException e) {
        return null;
    }
  }
 

}
