/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Clase que define el atributo Relleno
 * @author Raul Ruano Narváez
 */
public class RellenoRRN extends AtributoRRN{
    boolean activado;
    Color color;
    
    /**
     * Constructor por parametros que asocia al ShapeRRN el atributo RellenoRRN
     * @param shape ShapeRRN al que se le asociado el atributo RellenoDegradadoRRN
     * @param activado Parametro que especifica si el relleno del ShapeRRN esta activo o no
     * @param color Color del relleno
     */
    public RellenoRRN(ShapeRRN shape, boolean activado, Color color){
        super(shape, "Relleno");
        this.activado = activado;
        this.color = color;
    }

    /**
     * Método que devuelve el color del relleno
     * @return Color del relleno
     */
    public Color getColor() {
        return color;
    }

    /**
     * Método que devuelve si esta activo o no el relleno
     * @return Boolean con true o false
     */
    public boolean isActivado() {
        return activado;
    }
    
    @Override
    public void aplicarAtributo(Graphics2D g2d) {
        if(activado){
            g2d.setPaint(color);
            g2d.fill(getShape());
        }
    }
    
}
