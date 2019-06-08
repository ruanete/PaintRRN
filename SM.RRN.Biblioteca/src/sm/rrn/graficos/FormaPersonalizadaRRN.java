/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Clase que define la figura FormaPersonalizadaRRN la cual es un ShapeRRN
 * @author Raul
 */
public class FormaPersonalizadaRRN extends ShapeRRN{
    private ArrayList<Point> puntosTrazo;
    
    /**
     * Constructor por parámetros para crear una FormaPersonalizadaRRN
     * @param pos Punto inicial de la ElipseRRN
     */
    public FormaPersonalizadaRRN(Point pos){
        super(new Path2D.Double());      
        puntosTrazo = new ArrayList<>();
        crearEstrella(pos.getX(), pos.getY());
    }

    /**
     * Método para modificar la posición inicial del FormaPersonalizadaRRN
     * @param pos Posición final que se quiere setear
     */
    @Override
    public void setLocation(Point2D pos) {
        ((Path2D.Double) shape).reset();
        
        int deltax = (int) (pos.getX() - puntosTrazo.get(0).x);
        int deltay = (int) (pos.getY() - puntosTrazo.get(0).y);
        for(Point p: puntosTrazo){
            p.translate(deltax, deltay);
            if(p == puntosTrazo.get(0))
                ((Path2D.Double) shape).moveTo(p.x, p.y);
            else
                ((Path2D.Double) shape).lineTo(p.x, p.y);
        }
        ((Path2D.Double) shape).closePath();
    }

    /**
     * No implementado
     * @param puntoInicial No implementado
     * @param puntoFinal No implementado
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        ;
    }
    
    /**
     * Método que genera una estrella con un centro en X y en Y
     * @param centroX Posición X del punto
     * @param centroY Posición Y del punto
     */
    private void crearEstrella(double centroX, double centroY){
        for (int i = 0; i < 5 * 2; i++){
            double angulo = 50 + i * Math.PI / 5;
            double aumentoX = Math.cos(angulo);
            double aumentoY = Math.sin(angulo);
            if ((i & 1) == 0){
                aumentoX *= 50;
                aumentoY *= 50;
            }else{
                aumentoX *= 20;
                aumentoY *= 20;
            }
            if (i == 0){
                puntosTrazo.add(new Point((int)centroX + (int)aumentoX, (int)centroY + (int)aumentoY));
                ((Path2D.Double) shape).moveTo(centroX + aumentoX, centroY + aumentoY);
            }else{
                puntosTrazo.add(new Point((int)centroX + (int)aumentoX, (int)centroY + (int)aumentoY));
                ((Path2D.Double) shape).lineTo(centroX + aumentoX, centroY + aumentoY);
            }
        }
        ((Path2D.Double) shape).closePath();
    }
    
    @Override
    public String toString() {
        return "Estrella";
    }
}
