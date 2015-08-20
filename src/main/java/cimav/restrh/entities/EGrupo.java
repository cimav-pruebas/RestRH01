/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.entities;

/**
 *
 * @author juan.calderon
 */
public enum EGrupo {
    AYA(1),
    CYT(2),
    MMS(3),
    HON(4);
    
    private Integer id;
    
    EGrupo(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
}
