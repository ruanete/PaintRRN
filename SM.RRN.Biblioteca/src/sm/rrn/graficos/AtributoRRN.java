/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import sm.rrn.graficos.ShapeRRN;
import java.awt.Graphics2D;

/**
 * Clase abstracta que es padre de todos los atributos contendrá un ShapeRRN al que hace referencia y un String que identifica el nombre del AtributoRRN
 * @author Raul
 */
public abstract class AtributoRRN {
    private ShapeRRN shape;
    private String nombre_atributo;
    
    /**
     * Constructor por parámetros de AtributoRRN
     * @param shape ShapeRRN al que se le asociará el AtributoRRN
     * @param nombre_atributo String que pondrá nombre al AtributoRRN
     */
    public AtributoRRN(ShapeRRN shape, String nombre_atributo){
        this.shape = shape;
        this.nombre_atributo = nombre_atributo;
    }
    
    /**
     * Método abstracto que será implementado en cada clase de AtributoRRN específica
     * @param g2d Graphics2D para poder ser pintado dicho AtributoRRN
     */
    public abstract void aplicarAtributo(Graphics2D g2d);
    
    /**
     * Método que devuelve el ShapeRRN que está asociado al AtributoRRN
     * @return ShapeRRN con el shape asociado
     */
    public ShapeRRN getShape(){
        return shape;
    }
    
    /**
     * Método que devuelve el nombre del AtributoRRN
     * @return String con el nombre del AtributoRRN
     */
    public String getNombreAtributo(){
        return nombre_atributo;
    }
}
