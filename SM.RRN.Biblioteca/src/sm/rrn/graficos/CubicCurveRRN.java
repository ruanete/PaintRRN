/*
 * To change ((CubicCurve2D.Double) shape) license header, choose License Headers in Project Properties.
 * To change ((CubicCurve2D.Double) shape) template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

/**
 *
 * @author raulr
 */
public class CubicCurveRRN extends ShapeRRN{
    
    public CubicCurveRRN(Point2D puntoInicial, Point2D puntoControlInicial, Point2D puntoControlFinal, Point2D puntoFinal){
        super(new CubicCurve2D.Double(puntoInicial.getX(), puntoInicial.getY(), puntoControlInicial.getX(), puntoControlInicial.getY(), puntoControlFinal.getX(), puntoControlFinal.getY(), puntoFinal.getX(), puntoFinal.getY()));
    }
    
    @Override
    public void setLocation(Point2D pos){
        double dx=pos.getX()-((CubicCurve2D.Double) shape).getX1();
        double dy=pos.getY()-((CubicCurve2D.Double) shape).getY1();
        Point2D newp2 = new Point2D.Double(((CubicCurve2D.Double) shape).getX2()+dx,((CubicCurve2D.Double) shape).getY2()+dy);
        Point2D newc1 = new Point2D.Double(((CubicCurve2D.Double) shape).getCtrlX1()+dx, ((CubicCurve2D.Double) shape).getCtrlY1()+dy);
        Point2D newc2 = new Point2D.Double(((CubicCurve2D.Double) shape).getCtrlX2()+dx, ((CubicCurve2D.Double) shape).getCtrlY2()+dy);
        ((CubicCurve2D.Double) shape).setCurve(pos,newc1, newc2,newp2);
    }

    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ((CubicCurve2D.Double) shape).setCurve(puntoInicial, puntoInicial, puntoInicial, puntoFinal);
    }
    
    public void setPrimerPuntoControl(Point2D puntoControl){
       ((CubicCurve2D.Double) shape).ctrlx1 = puntoControl.getX();
       ((CubicCurve2D.Double) shape).ctrly1 = puntoControl.getY();
    }
    
    public void setSegundoPuntoControl(Point2D puntoControl){
       ((CubicCurve2D.Double) shape).ctrlx2 = puntoControl.getX();
       ((CubicCurve2D.Double) shape).ctrly2 = puntoControl.getY();
    }

    @Override
    public String toString() {
        return "Cubic Curve";
    }
}
