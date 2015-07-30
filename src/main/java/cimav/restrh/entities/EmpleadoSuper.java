/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author juan.calderon
 */
@MappedSuperclass
public class EmpleadoSuper extends BaseEntity implements Serializable {
    
    @Column(name = "id_status")
    private Short idStatus;
    
    @Size(max = 300)
    @Column(name = "url_photo")
    private String urlPhoto;
    
    @Size(max = 60)
    @Column(name = "cuenta_cimav")
    private String cuentaCimav;
    
    @JoinColumn(name = "id_tabulador", referencedColumnName = "id")
    @ManyToOne
    private Tabulador nivel;
    
    @JoinColumn(name = "id_grupo", referencedColumnName = "id")
    @ManyToOne
    private Grupo grupo;
    
    @XmlElement(name = "departamento")
    @JoinColumn(name = "id_departamento", referencedColumnName = "id")
    @ManyToOne
    private Departamento departamento;
    
    @Column(name = "id_sede")
    private Short idSede;
    
//    @PostLoad
//    public void reduceJefe() {
//        // TODO Buscar best approach de reducción profundidad de Entidad en EmpleadoOld.Jefe
//        if (this.getJefe() != null) {
//            EmpleadoOld miniJefe = new EmpleadoOld();
//            miniJefe.setId(this.getJefe().getId());
//            miniJefe.setCode(this.getJefe().getCode());
//            miniJefe.setName(this.getJefe().getName());
//            miniJefe.setUrlPhoto(this.getJefe().getUrlPhoto());
//
//            this.setJefe(miniJefe);
//        }
//    }
    
    public EmpleadoSuper() {
        super();
    }

    public Short getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Short idStatus) {
        this.idStatus = idStatus;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getCuentaCimav() {
        return cuentaCimav;
    }

    public void setCuentaCimav(String cuentaCimav) {
        this.cuentaCimav = cuentaCimav;
    }

    public Tabulador getNivel() {
        return nivel;
    }

    public void setNivel(Tabulador nivel) {
        this.nivel = nivel;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Short getIdSede() {
        return idSede;
    }

    public void setIdSede(Short idSede) {
        this.idSede = idSede;
    }
    
    
}
