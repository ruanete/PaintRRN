/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 *
 * @author raulr
 */
public class ElipseRRN extends ShapeRRN{
    
    public ElipseRRN(Point2D punto, double w, double h){
        super(new Ellipse2D.Double(punto.getX(), punto.getY(), w, h));
    }
    
    @Override
    public void setLocation(Point2D pos){
        ((Ellipse2D.Double) shape).setFrameFromDiagonal(pos.getX(),pos.getY(),pos.getX()+((Ellipse2D.Double) shape).width,pos.getY()+((Ellipse2D.Double) shape).height);
    }
    
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal){
        ((Ellipse2D.Double) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    }
    
    @Override
    public String toString() {
        return "Elipse";
    }
}
