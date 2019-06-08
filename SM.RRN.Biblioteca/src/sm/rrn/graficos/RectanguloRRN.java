/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Raúl Ruano Narváez
 */
public class RectanguloRRN extends ShapeRRN{
    
    /**
     *
     * @param punto
     * @param w
     * @param h
     */
    public RectanguloRRN(Point punto, int w, int h){
        super(new Rectangle(punto.x, punto.y, w, h));
    }

    /**
     *
     * @param pos
     */
    @Override
    public void setLocation(Point2D pos) {
        ((Rectangle) shape).setLocation((Point) pos);
    }
    
    /**
     *
     * @param puntoInicial
     * @param puntoFinal
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal){
        ((Rectangle) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    }
    
    @Override
    public String toString() {
        return "Rectangulo";
    }
    
}
