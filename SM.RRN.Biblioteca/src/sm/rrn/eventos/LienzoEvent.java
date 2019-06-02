/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.eventos;

import java.awt.Color;
import java.util.EventObject;
import sm.rrn.graficos.ShapeRRN;
import sm.rrn.iu.Lienzo2D;
import sm.rrn.iu.LienzoImagen2D;
import sm.rrn.iu.ModoPintado;
import sm.rrn.iu.TipoRelleno;
import sm.rrn.iu.TipoTrazo;

/**
 *
 * @author raulr
 */
public class LienzoEvent extends EventObject{
    private ShapeRRN shape;
    private Lienzo2D lienzo;
    
    public LienzoEvent(Object source, ShapeRRN shape, Lienzo2D lienzo) {
        super(source);
        this.shape = shape;
        this.lienzo = lienzo;
    }

    public ShapeRRN getShape() {
        return shape;
    }

    public Lienzo2D getLienzo() {
        return lienzo;
    }
}
