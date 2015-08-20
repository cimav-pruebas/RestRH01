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
    CYT(1),
    MMS(2),
    AYA(3),
    HON(4);
    
    private int id;
    
    EGrupo(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
