/*
 * To change ((Line2D.Double) shape) license header, choose License Headers in Project Properties.
 * To change ((Line2D.Double) shape) template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Clase que define la figura LineaRRN la cual es un ShapeRRN
 * @author Raúl Ruano Narváez
 */
public class LineaRRN extends ShapeRRN{
    
    /**
     * Constructor por parámetros para crear un LineaRRN
     * @param puntoInicial Punto con la posición inicial de la figura
     * @param puntoFinal Punto con la posición final de la figura
     */
    public LineaRRN(Point2D puntoInicial, Point2D puntoFinal){
        super(new Line2D.Double(puntoInicial, puntoFinal));
    }
    
    /**
     * Método para comprobar si un punto esta a distancia menos o igual que 4 de la LineaRRN
     * @param p Punto desde donde se quiere comprobar la distancia a la LineaRRN
     * @return Devuelve un boolean de si esta a menos de 4 de distancia de la LineaRRN
     */
    public boolean isNear(Point2D p){
        return ((Line2D.Double) shape).ptLineDist(p)<=4.0;
    }
    
    /**
     * Método para comprobar si un punto está dentro de una LineaRRN
     * @param p Punto que se comprueba si está dentro de una LineaRRN
     * @return Devuelve true si esta cerca de 
     */
    @Override
    public boolean contains(Point2D p){
        return isNear(p);
    }
    
    /**
     * Método para modificar la posición inicial del LineaRRN
     * @param pos Posición inicial de la figura
     */
    @Override
    public void setLocation(Point2D pos){
        Point paux = (Point) pos;
        Point bounds = ((Line2D.Double) shape).getBounds().getLocation();
        
        if(bounds.getX() == ((Line2D.Double) shape).getX1() && bounds.getY() == ((Line2D.Double) shape).getY1() - ((Line2D.Double) shape).getBounds2D().getHeight())
            paux = new Point((int)pos.getX(), (int)pos.getY() + (int)((Line2D.Double) shape).getBounds2D().getHeight());
        else if(bounds.getX() == ((Line2D.Double) shape).getX2() && bounds.getY() == ((Line2D.Double) shape).getY2())
            paux = new Point((int)pos.getX() + (int)((Line2D.Double) shape).getBounds2D().getWidth(), (int)pos.getY() + (int)((Line2D.Double) shape).getBounds2D().getHeight());
        else if((int)bounds.getX() == (int)((Line2D.Double) shape).getX2() && (int)bounds.getY() == (int)((Line2D.Double) shape).getY2() - (int)((Line2D.Double) shape).getBounds2D().getHeight())
            paux = new Point((int)pos.getX() + (int)((Line2D.Double) shape).getBounds2D().getWidth(), (int)pos.getY());
                
        double dx=paux.getX()-((Line2D.Double) shape).getX1();
        double dy=paux.getY()-((Line2D.Double) shape).getY1();
        Point2D newp2 = new Point2D.Double(((Line2D.Double) shape).getX2()+dx,((Line2D.Double) shape).getY2()+dy);
        ((Line2D.Double) shape).setLine(paux,newp2);
    }
    
    /**
     * Método para modificar la posición final del LineaRRN
     * @param puntoInicial Punto con la posición inicial de la figura
     * @param puntoFinal Punto con la posición final a la que se moverá la figura
     */
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal){
        ((Line2D.Double) shape).setLine(puntoInicial, puntoFinal);
    }
    
    @Override
    public String toString() {
        return "Linea";
    }
}
