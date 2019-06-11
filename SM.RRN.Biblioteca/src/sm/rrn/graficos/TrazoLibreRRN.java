/*
 * To change ((Path2D.Double) shape) license header, choose License Headers in Project Properties.
 * To change ((Path2D.Double) shape) template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Clase que define la figura Trazo Libre
 * @author Raúl Ruano Narváez
 */
public class TrazoLibreRRN extends ShapeRRN{
    private ArrayList<Point> puntosTrazo;

    /**
     * Constructor por defecto
     */
    public TrazoLibreRRN(){
        super(new Path2D.Double());
        puntosTrazo = new ArrayList<>();
    }
    
    /**
     * Método para modificar la posición
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
    }

    /**
     * Método que setea el primer punto del trazo libre unido al segundo punto inicial
     * @param puntoInicial Punto inicial
     * @param puntoFinal Punto final
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        if(puntosTrazo.size()==0){
            this.moveTo((Point) puntoInicial);
        }else{
            this.lineTo((Point) puntoFinal);
        }
        
    }
    
    /**
     * Crea una adición a la ruta moviéndose a las coordenadas especificadas.
     * @param p Punto al que se movera
     */
    public void moveTo(Point p){
        puntosTrazo.add(p);
        ((Path2D.Double) shape).moveTo(p.x, p.y);
    }
    
    /**
     * Metodo que añade un punto más al path y que lo conecta con el ultimo
     * @param p Punto que hara linea con el anterior metido en el Path
     */
    public void lineTo(Point p){
        puntosTrazo.add(p);
        ((Path2D.Double) shape).lineTo(p.x, p.y);
    }
    
    /**
     * Método que cierra el path
     */
    public void closePath(){
        //((Path2D.Double) shape).closePath();
    }
    
    @Override
    public String toString() {
        return "Trazo libre";
    }
}
