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
 * Clase que define la figura ElipseRRN la cual es un ShapeRRN
 * @author Raúl Ruano Narváez
 */
public class ElipseRRN extends ShapeRRN{
    
    /**
     * Constructor por parámetros para crear una ElipseRRN
     * @param punto Punto inicial de la ElipseRRN
     * @param w Ancho de la ElipseRRN
     * @param h Altura de la ElipseRRN
     */
    public ElipseRRN(Point2D punto, double w, double h){
        super(new Ellipse2D.Double(punto.getX(), punto.getY(), w, h));
    }
    
    /**
     * Método para modificar la posición inicial del ElipseRRN
     * @param pos Posición final que se quiere setear
     */
    @Override
    public void setLocation(Point2D pos){
        ((Ellipse2D.Double) shape).setFrameFromDiagonal(pos.getX(),pos.getY(),pos.getX()+((Ellipse2D.Double) shape).width,pos.getY()+((Ellipse2D.Double) shape).height);
    }
    
    /**
     * Método para modificar la posición final del ElipseRRN
     * @param puntoInicial Punto con la posición inicial de la figura
     * @param puntoFinal Punto con la posición final a la que será movida la figura
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal){
        ((Ellipse2D.Double) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    }
    
    @Override
    public String toString() {
        return "Elipse";
    }
}
