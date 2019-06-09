/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * Clase que define la figura Arco
 * @author Raúl Ruano Narváez
 */
public class ArcoRRN extends ShapeRRN{
    
    /**
     * Constructor por parámetros para crear un ArcoRRN
     * @param punto Punto referencia del ArcoRRN
     * @param width Ancho que tomara el ArcoRRN
     * @param height Altura que tomará el ArcoRRN
     * @param start Inicio del arco
     * @param extent Hasta donde se extendiende el ArcoRRN en angulo
     * @param type Tipo de cierre
     */
    public ArcoRRN(Point2D punto, double width, double height, double start, double extent, int type){
        super(new Arc2D.Double(punto.getX(), punto.getY(), width, height, start, extent, type));
    }
    
    /**
     * Método para modificar la posición
     * @param pos Posición final que se quiere setear
     */
    @Override
    public void setLocation(Point2D pos) {
        ((Arc2D.Double) shape).setFrameFromDiagonal(pos.getX(),pos.getY(),pos.getX()+((Arc2D.Double) shape).width,pos.getY()+((Arc2D.Double) shape).height);
    }

    /**
     * Método que establece la diagonal que encuadra a la figura entre dos Point2D especificados
     * @param puntoInicial Punto inicial
     * @param puntoFinal Punto final
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ((Arc2D.Double) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    }
    
    /**
     * Método para cambiar el angulo del ArcoRRN
     * @param angle Angulo final del ArcoRRN
     */
    public void setAngle(double angle){
        ((Arc2D.Double) shape).extent = angle;
    }

    @Override
    public String toString() {
        return "Arco";
    }
    
}
