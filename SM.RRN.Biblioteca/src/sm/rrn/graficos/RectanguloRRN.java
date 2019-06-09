/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Clase que define la figura Rectangulo
 * @author Raúl Ruano Narváez
 */
public class RectanguloRRN extends ShapeRRN{
    
    /**
     * Constructor por parámetros de RectanguloRRN
     * @param punto Punto inicial
     * @param w Ancho del rectangulo
     * @param h Altura del rectangulo
     */
    public RectanguloRRN(Point punto, int w, int h){
        super(new Rectangle(punto.x, punto.y, w, h));
    }

    /**
     * Método para modificar la posición
     * @param pos Posición final que se quiere setear
     */
    @Override
    public void setLocation(Point2D pos) {
        ((Rectangle) shape).setLocation((Point) pos);
    }
    
    /**
     * Método que establece la diagonal que encuadra a la figura entre dos Point2D especificados
     * @param puntoInicial Punto inicial
     * @param puntoFinal Punto final
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal){
        ((Rectangle) shape).setFrameFromDiagonal(puntoInicial, puntoFinal);
    }
    
    @Override
    public String toString() {
        return "Rectangulo";
    }
    
}
