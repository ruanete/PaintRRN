/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author Raul
 */
public class AntialiasingRRN extends AtributoRRN{
    RenderingHints antialiasing;
    boolean activado;

    public AntialiasingRRN(ShapeRRN shape, boolean activado) {
        super(shape, "Antialiasing");
        this.activado = activado;
                
        if(activado)
            antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        else
            antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    public boolean isActivado() {
        return activado;
    }
    
    @Override
    public void aplicarAtributo(Graphics2D g2d) {
        g2d.setRenderingHints(antialiasing);
    }
    
}
