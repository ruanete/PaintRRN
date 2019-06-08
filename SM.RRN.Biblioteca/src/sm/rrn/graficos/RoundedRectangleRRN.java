/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author Raúl Ruano Narváez
 */
public class RoundedRectangleRRN extends ShapeRRN{
    
    /**
     *
     * @param archeight
     * @param arcwidth
     * @param height
     * @param widht
     * @param punto
     */
    public RoundedRectangleRRN(double archeight, double arcwidth, double height, double widht, Point2D punto){
        super(new RoundRectangle2D.Double(archeight, arcwidth, height, widht, punto.getX(), punto.getY()));
    }
    
    /**
     *
     * @param pos
     */
    @Override
    public void setLocation(Point2D pos) {
        ((RoundRectangle2D.Double) shape).setFrameFromDiagonal(pos.getX(),pos.getY(),pos.getX()+((RoundRectangle2D.Double) shape).width,pos.getY()+((RoundRectangle2D.Double) shape).height);
    }

    /**
     *
     * @param puntoInicial
     * @param puntoFinal
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ((RoundRectangle2D.Double) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    } 
    
    /**
     *
     * @param borde
     */
    public void setArcoBorde(double borde){
        ((RoundRectangle2D.Double) shape).archeight=borde;
        ((RoundRectangle2D.Double) shape).arcwidth=borde;
    }
    
    @Override
    public String toString() {
        return "Rounded Rectangle";
    }
}
