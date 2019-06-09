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
 * Clase que define el atributo Relleno con Degradado
 * @author Raul Ruano Narváez
 */
public class RellenoDegradadoRRN extends AtributoRRN{
    boolean activado;
    Color colorInicio, colorFinal;
    TipoRelleno tipoRelleno;
    GradientPaint degradado;

    /**
     * Constructor por parametros que asocia al ShapeRRN el atributo RellenoDegradadoRRN
     * @param shape ShapeRRN al que se le asociado el atributo RellenoDegradadoRRN
     * @param activado Parametro que especifica si el relleno del ShapeRRN esta activo o no
     * @param colorInicio Color inicial del degradado
     * @param colorFinal Color final del degradado
     * @param tipoRelleno Tipo de degradado: DEGRADADO_VERTICAL o DEGRADADO_HORIZONTAL
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
     * Método que devuelve el color de inicio del degradado
     * @return Color inicial del degradado
     */
    public Color getColorInicio() {
        return colorInicio;
    }

    /**
     * Método que devuelve el color de final del degradado
     * @return Color final del degradado
     */
    public Color getColorFinal() {
        return colorFinal;
    }

    /**
     * Método que devuelve el tipo de degradado
     * @return TipoRelleno con el tipo
     */
    public TipoRelleno getTipoRelleno() {
        return tipoRelleno;
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
            g2d.setPaint(degradado);
            g2d.fill(getShape());
        }  
    }
    
}
