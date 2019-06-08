/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import sm.rrn.iu.TipoRelleno;

/**
 *
 * @author Raul
 */
public class RellenoDegradadoRRN extends AtributoRRN{
    boolean activado;
    Color colorInicio, colorFinal;
    TipoRelleno tipoRelleno;
    GradientPaint degradado;

    /**
     *
     * @param shape
     * @param activado
     * @param colorInicio
     * @param colorFinal
     * @param tipoRelleno
     */
    public RellenoDegradadoRRN(ShapeRRN shape, boolean activado, Color colorInicio, Color colorFinal, TipoRelleno tipoRelleno) {
        super(shape, "RellenoDegradado");
        this.activado = activado;
        this.colorInicio = colorInicio;
        this.colorFinal = colorFinal;
        this.tipoRelleno = tipoRelleno;
        
        Point puntoInicio, puntoFin;
        
        switch(this.tipoRelleno){
            case DEGRADADO_VERTICAL:
                puntoInicio = new Point(shape.getBounds().x + shape.getBounds().width/2, 0);
                puntoFin = new Point(shape.getBounds().x + shape.getBounds().width/2, shape.getBounds().y + shape.getBounds().height);
                
                degradado = new GradientPaint(puntoInicio, colorInicio, puntoFin, colorFinal);
                break;
            case DEGRADADO_HORIZONTAL:
                puntoInicio = new Point(0, shape.getBounds().y + shape.getBounds().height/2);
                puntoFin = new Point(shape.getBounds().x + shape.getBounds().width, shape.getBounds().y + shape.getBounds().height/2);
                
                degradado = new GradientPaint(puntoInicio, colorInicio, puntoFin, colorFinal);
                break;
        }
            
    }

    /**
     *
     * @return
     */
    public Color getColorInicio() {
        return colorInicio;
    }

    /**
     *
     * @return
     */
    public Color getColorFinal() {
        return colorFinal;
    }

    /**
     *
     * @return
     */
    public TipoRelleno getTipoRelleno() {
        return tipoRelleno;
    }

    /**
     *
     * @return
     */
    public boolean isActivado() {
        return activado;
    }

    @Override
    public void aplicarAtributo(Graphics2D g2d) {
        if(activado){
            g2d.setPaint(degradado);
            g2d.fill(getShape());
        }  
    }
    
}
