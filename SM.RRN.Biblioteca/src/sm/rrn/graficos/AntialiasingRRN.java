/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Clase que define el atributo de Antialising (suavizado de bordes) que podrá ser aplicado a ShapeRRN
 * @author Raul Ruano Narváez
 */
public class AntialiasingRRN extends AtributoRRN{
    RenderingHints antialiasing;
    boolean activado;

    /**
     * Constructor por parametros que asocia al ShapeRRN el atributo AntialisingRRN
     * @param shape ShapeRRN al que se le asociado el atributo AntialisingRRN
     * @param activado Parametro que especifica si el atributo del ShapeRRN esta activo o no
     */
    public AntialiasingRRN(ShapeRRN shape, boolean activado) {
        super(shape, "Antialiasing");
        this.activado = activado;
                
        if(activado)
            antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        else
            antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    /**
     * Método que devuelve si el atributo del ShapeRRN esta activado o no
     * @return Devuelve un Booleano true o false
     */
    public boolean isActivado() {
        return activado;
    }
    
    /**
     * Método que aplica el atributo Antialising cuando la figura es pintada desde el método paint()
     * @param g2d
     */
    @Override
    public void aplicarAtributo(Graphics2D g2d) {
        g2d.setRenderingHints(antialiasing);
    }
    
}
