/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Raul
 */
public class RellenoRRN extends AtributoRRN{
    boolean activado;
    Color color;
    
    public RellenoRRN(ShapeRRN shape, boolean activado, Color color){
        super(shape, "Relleno");
        this.activado = activado;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

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
