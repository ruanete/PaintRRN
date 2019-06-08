/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Clase que define la figura PuntoRRN la cual es un ShapeRRN
 * @author Raúl Ruano Narváez
 */
public class PuntoRRN extends ShapeRRN{
    
    /**
     * Constructor por parámetros de PuntoRRN
     * @param punto Punto que forma a la figura
     */
    public PuntoRRN(Point2D punto){
        super(new Line2D.Double(punto.getX(), punto.getY(), punto.getX(), punto.getY()));
    }
    
    /**
     * Método que comprueba si un punto está a 4 pixeles de PuntoRRN
     * @param p Punto que se comprueba
     * @return Devuelve true o false
     */
    public boolean isNear(Point2D p){
        return ((Line2D.Double) shape).getP1().distance(p)<=4.0;
    }
    
    /**
     * Método que comprueba si un punto contiene un PuntoRRN
     * @param p Punto que se comprueba
     * @return Devuelve true o false
     */
    @Override
    public boolean contains(Point2D p){
        return isNear(p);
    }

    /**
     * Método para modificar la posición inicial del PuntoRRN
     * @param pos Posición inicial de la figura
     */
    @Override
    public void setLocation(Point2D pos) {
        double dx=pos.getX()-((Line2D.Double) shape).getX1();
        double dy=pos.getY()-((Line2D.Double) shape).getY1();
        Point2D newp2 = new Point2D.Double(((Line2D.Double) shape).getX2()+dx,((Line2D.Double) shape).getY2()+dy);
        ((Line2D.Double) shape).setLine(pos,newp2);
    }

    /**
     * Método para modificar la posición final del PuntoRRN
     * @param puntoInicial Punto con la posición inicial de la figura
     * @param puntoFinal Punto con la posición final a la que será movida la figura
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ;
    }
    
    @Override
    public String toString() {
        return "Punto";
    }
}
