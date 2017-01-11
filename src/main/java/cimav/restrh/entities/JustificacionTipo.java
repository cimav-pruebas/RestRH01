/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author calderon
 */
@Entity
@Cacheable(false)
@Table(name = "justificacionTipos", schema = "public")
@XmlRootElement(name="JustificacionTipo")
public class JustificacionTipo implements Serializable {
//    ACCESORIOS(1, "Accesorios y Material de Laboratorio", 17),
//    SUBCONTRATACION(2, "Subcontratación por Honorarios", 14),
//    MANTENIMIENTO(3, "NowMantenimiento a equipos", 15),
//    EQUIPIO(4, "Equipo especializado", 17),
//    OTRO(5, "Otro", 3);

    @Id
    private Short id;
    private String code;
    private Short fraccion;

    public JustificacionTipo() {
    }

//    public static JustificacionTipo get(Short id) {
//        JustificacionTipo result = JustificacionTipo.ACCESORIOS; // default
//        for (JustificacionTipo value : JustificacionTipo.values()) {
//            if (Objects.equals(value.getId(), id)) {
//                result = value;
//                break;
//            }
//        }
//        return result;
//    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Short getFraccion() {
        return fraccion;
    }

    public void setFraccion(Short fraccion) {
        this.fraccion = fraccion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JustificacionTipo other = (JustificacionTipo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JustificacionTipo{" + "id=" + id + ", fraccion=" + fraccion + '}';
    }

    

}
