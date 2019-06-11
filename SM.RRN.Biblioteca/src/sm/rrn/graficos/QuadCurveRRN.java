/*
 * To change ((QuadCurve2D.Double) shape) license header, choose License Headers in Project Properties.
 * To change ((QuadCurve2D.Double) shape) template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

/**
 * Clase que define la figura Curva con Un Puntos de Control
 * @author Raúl Ruano Narváez
 */
public class QuadCurveRRN extends ShapeRRN{
    
    /**
     * Constructor por parámetros de QuadCurveRRN
     * @param puntoInicial Punto inicial de QuadCurveRRN
     * @param puntoControl Primer punto de control de QuadCurveRRN
     * @param puntoFinal Punto final de QuadCurveRRN
     */
    public QuadCurveRRN(Point2D puntoInicial, Point2D puntoControl, Point2D puntoFinal){
        super(new QuadCurve2D.Double(puntoInicial.getX(), puntoInicial.getY(), puntoControl.getX(), puntoControl.getY(), puntoFinal.getX(), puntoFinal.getX()));
    }
    
    /**
     * Método para modificar la posición
     * @param pos Posición final que se quiere setear
     */
    @Override
    public void setLocation(Point2D pos){
        double dx=pos.getX()-((QuadCurve2D.Double) shape).getX1();
        double dy=pos.getY()-((QuadCurve2D.Double) shape).getY1();
        Point2D newp2 = new Point2D.Double(((QuadCurve2D.Double) shape).getX2()+dx,((QuadCurve2D.Double) shape).getY2()+dy);
        Point2D newc2 = new Point2D.Double(((QuadCurve2D.Double) shape).getCtrlX()+dx, ((QuadCurve2D.Double) shape).getCtrlY()+dy);
        ((QuadCurve2D.Double) shape).setCurve(pos,newc2,newp2);
    }

    /**
     * Método que establece la ubicación de los puntos inicial y final de la figura QuadCurve
     * @param puntoInicial Punto inicial
     * @param puntoFinal Punto final
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ((QuadCurve2D.Double) shape).setCurve(puntoInicial, puntoInicial, puntoFinal);
    }
    
    /**
     * Método para setear el punto de control
     * @param puntoControl Punto nuevo de control
     */
    public void setPuntoControl(Point2D puntoControl){
       ((QuadCurve2D.Double) shape).ctrlx = puntoControl.getX();
       ((QuadCurve2D.Double) shape).ctrly = puntoControl.getY();
    }
    
    @Override
    public String toString() {
        return "Quad Curve";
    }
}
