/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import sm.rrn.graficos.ShapeRRN;
import java.awt.Graphics2D;

/**
 *
 * @author Raul
 */
public abstract class AtributoRRN {
    private ShapeRRN shape;
    private String nombre_atributo;
    
    public AtributoRRN(ShapeRRN shape, String nombre_atributo){
        this.shape = shape;
        this.nombre_atributo = nombre_atributo;
    }
    
    public abstract void aplicarAtributo(Graphics2D g2d);
    
    public ShapeRRN getShape(){
        return shape;
    }
    
    public String getNombreAtributo(){
        return nombre_atributo;
    }
}
