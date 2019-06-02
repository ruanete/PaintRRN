/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 *
 * @author raulr
 */
public class ArcoRRN extends ShapeRRN{
    
    public ArcoRRN(Point2D punto, double width, double height, double start, double extent, int type){
        super(new Arc2D.Double(punto.getX(), punto.getY(), width, height, start, extent, type));
    }
    
    @Override
    public void setLocation(Point2D pos) {
        ((Arc2D.Double) shape).setFrameFromDiagonal(pos.getX(),pos.getY(),pos.getX()+((Arc2D.Double) shape).width,pos.getY()+((Arc2D.Double) shape).height);
    }

    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ((Arc2D.Double) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    }
    
    public void setAngle(double angle){
        ((Arc2D.Double) shape).extent = angle;
    }

    @Override
    public String toString() {
        return "Arco";
    }
    
}
