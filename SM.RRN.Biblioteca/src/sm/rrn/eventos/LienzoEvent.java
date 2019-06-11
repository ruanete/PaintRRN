/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.eventos;

import java.util.EventObject;
import sm.rrn.graficos.ShapeRRN;
import sm.rrn.iu.Lienzo2D;

/**
 * Clase con los eventos de Lienzo2D el cual tendrá un ShapeRRN y un Lienzo2D
 * @author Raúl Ruano Narváez
 */
public class LienzoEvent extends EventObject{
    private ShapeRRN shape;
    private Lienzo2D lienzo;
    
    /**
     *
     * @param source Referencia al objeto donde se usa LienzoEvent
     * @param shape Define el ShapeRRN que será usado luego para aádir a la lista de figuras
     * @param lienzo Define el Lienzo2D que usara los eventos
     */
    public LienzoEvent(Object source, ShapeRRN shape, Lienzo2D lienzo) {
        super(source);
        this.shape = shape;
        this.lienzo = lienzo;
    }

    /**
     * Método que devuelve el ShapeRRN asociado al evento de Lienzo2D
     * @return Devuelve un ShapeRRN
     */
    public ShapeRRN getShape() {
        return shape;
    }

    /**
     * Método que devuelve el Lienzo2D asociado al evento de Lienzo2D
     * @return Devuelve un Lienzo2D
     */
    public Lienzo2D getLienzo() {
        return lienzo;
    }
}
