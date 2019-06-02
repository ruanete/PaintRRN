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
 * @author raulr
 */
public class RectanguloRRN extends ShapeRRN{
    
    public RectanguloRRN(Point punto, int w, int h){
        super(new Rectangle(punto.x, punto.y, w, h));
    }

    @Override
    public void setLocation(Point2D pos) {
        ((Rectangle) shape).setLocation((Point) pos);
    }
    
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal){
        ((Rectangle) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    }
    
    @Override
    public String toString() {
        return "Rectangulo";
    }
    
}
