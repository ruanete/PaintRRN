/*
 * To change ((CubicCurve2D.Double) shape) license header, choose License Headers in Project Properties.
 * To change ((CubicCurve2D.Double) shape) template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

/**
 * Clase que define la figura Curva con Dos Puntos de Control la cual es un ShapeRRN
 * @author Raúl Ruano Narváez
 */
public class CubicCurveRRN extends ShapeRRN{
    
    /**
     * Constructor por parámetros para crear una CubicCurveRRN
     * @param puntoInicial Punto inicial de la CubicCurveRRN
     * @param puntoControlInicial Primer punto de control de la CubicCurveRRN
     * @param puntoControlFinal Segundo punto de control de la CubicCurveRRN
     * @param puntoFinal Punto final de la CubicCurveRRN
     */
    public CubicCurveRRN(Point2D puntoInicial, Point2D puntoControlInicial, Point2D puntoControlFinal, Point2D puntoFinal){
        super(new CubicCurve2D.Double(puntoInicial.getX(), puntoInicial.getY(), puntoControlInicial.getX(), puntoControlInicial.getY(), puntoControlFinal.getX(), puntoControlFinal.getY(), puntoFinal.getX(), puntoFinal.getY()));
    }
    
    /**
     * Método para modificar la posición inicial del CubicCurveRRN
     * @param pos Posición final que se quiere setear
     */
    @Override
    public void setLocation(Point2D pos){
        double dx=pos.getX()-((CubicCurve2D.Double) shape).getX1();
        double dy=pos.getY()-((CubicCurve2D.Double) shape).getY1();
        Point2D newp2 = new Point2D.Double(((CubicCurve2D.Double) shape).getX2()+dx,((CubicCurve2D.Double) shape).getY2()+dy);
        Point2D newc1 = new Point2D.Double(((CubicCurve2D.Double) shape).getCtrlX1()+dx, ((CubicCurve2D.Double) shape).getCtrlY1()+dy);
        Point2D newc2 = new Point2D.Double(((CubicCurve2D.Double) shape).getCtrlX2()+dx, ((CubicCurve2D.Double) shape).getCtrlY2()+dy);
        ((CubicCurve2D.Double) shape).setCurve(pos,newc1, newc2,newp2);
    }

    /**
     * Método para modificar la posición final del CubicCurveRRN
     * @param puntoInicial Punto con la posición inicial de la figura
     * @param puntoFinal Punto con la posición final a la que será movida la figura
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ((CubicCurve2D.Double) shape).setCurve(puntoInicial, puntoInicial, puntoInicial, puntoFinal);
    }
    
    /**
     * Método para setear el primer punto de control de CubicCurveRRN
     * @param puntoControl Punto que definirá el primer punto de control
     */
    public void setPrimerPuntoControl(Point2D puntoControl){
       ((CubicCurve2D.Double) shape).ctrlx1 = puntoControl.getX();
       ((CubicCurve2D.Double) shape).ctrly1 = puntoControl.getY();
    }
    
    /**
     * Método para setear el segundo punto de control de CubicCurveRRN
     * @param puntoControl Punto que definirá el segundo punto de control
     */
    public void setSegundoPuntoControl(Point2D puntoControl){
       ((CubicCurve2D.Double) shape).ctrlx2 = puntoControl.getX();
       ((CubicCurve2D.Double) shape).ctrly2 = puntoControl.getY();
    }

    @Override
    public String toString() {
        return "Cubic Curve";
    }
}
