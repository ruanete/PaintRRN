/*
 * To change ((QuadCurve2D.Double) shape) license header, choose License Headers in Project Properties.
 * To change ((QuadCurve2D.Double) shape) template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

/**
 *
 * @author raulr
 */
public class QuadCurveRRN extends ShapeRRN{
    
    public QuadCurveRRN(Point2D puntoInicial, Point2D puntoControl, Point2D puntoFinal){
        super(new QuadCurve2D.Double(puntoInicial.getX(), puntoInicial.getY(), puntoControl.getX(), puntoControl.getY(), puntoFinal.getX(), puntoFinal.getX()));
    }
    
    @Override
    public void setLocation(Point2D pos){
        double dx=pos.getX()-((QuadCurve2D.Double) shape).getX1();
        double dy=pos.getY()-((QuadCurve2D.Double) shape).getY1();
        Point2D newp2 = new Point2D.Double(((QuadCurve2D.Double) shape).getX2()+dx,((QuadCurve2D.Double) shape).getY2()+dy);
        Point2D newc2 = new Point2D.Double(((QuadCurve2D.Double) shape).getCtrlX()+dx, ((QuadCurve2D.Double) shape).getCtrlY()+dy);
        ((QuadCurve2D.Double) shape).setCurve(pos,newc2,newp2);
    }

    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ((QuadCurve2D.Double) shape).setCurve(puntoInicial, puntoInicial, puntoFinal);
    }
    
    public void setPuntoControl(Point2D puntoControl){
       ((QuadCurve2D.Double) shape).ctrlx = puntoControl.getX();
       ((QuadCurve2D.Double) shape).ctrly = puntoControl.getY();
    }
    
    @Override
    public String toString() {
        return "Quad Curve";
    }
}
