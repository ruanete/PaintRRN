/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Clase que define la figura Rectangulo con Bordes Redondeados
 * @author Raúl Ruano Narváez
 */
public class RoundedRectangleRRN extends ShapeRRN{
    
    /**
     * Constructor por parámetros de RoundedRectangleRRN
     * @param archeight Valor de la altura del arco del borde
     * @param arcwidth Valor de la anchura del arco del borde
     * @param height Altura de la figura
     * @param widht Ancho de la figura
     * @param punto Punto inicial de la figura
     */
    public RoundedRectangleRRN(double archeight, double arcwidth, double height, double widht, Point2D punto){
        super(new RoundRectangle2D.Double(archeight, arcwidth, height, widht, punto.getX(), punto.getY()));
    }
    
    /**
     * Método para modificar la posición
     * @param pos Posición final que se quiere setear
     */
    @Override
    public void setLocation(Point2D pos) {
        ((RoundRectangle2D.Double) shape).setFrameFromDiagonal(pos.getX(),pos.getY(),pos.getX()+((RoundRectangle2D.Double) shape).width,pos.getY()+((RoundRectangle2D.Double) shape).height);
    }

    /**
     * Método que establece la diagonal que encuadra a la figura entre dos Point2D especificados
     * @param puntoInicial Punto inicial
     * @param puntoFinal Punto final
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ((RoundRectangle2D.Double) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    } 
    
    /**
     * Método que setea el angulo de la altura y anchura del borde redondeado
     * @param borde Valor del angulo
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
