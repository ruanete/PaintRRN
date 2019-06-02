/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author raulr
 */
public class PuntoRRN extends ShapeRRN{
    
    public PuntoRRN(Point2D punto){
        super(new Line2D.Double(punto.getX(), punto.getY(), punto.getX(), punto.getY()));
    }
    
    public boolean isNear(Point2D p){
        return ((Line2D.Double) shape).getP1().distance(p)<=4.0;
    }
    
    @Override
    public boolean contains(Point2D p){
        return isNear(p);
    }

    @Override
    public void setLocation(Point2D pos) {
        double dx=pos.getX()-((Line2D.Double) shape).getX1();
        double dy=pos.getY()-((Line2D.Double) shape).getY1();
        Point2D newp2 = new Point2D.Double(((Line2D.Double) shape).getX2()+dx,((Line2D.Double) shape).getY2()+dy);
        ((Line2D.Double) shape).setLine(pos,newp2);
    }

    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ;
    }
    
    @Override
    public String toString() {
        return "Punto";
    }
}
