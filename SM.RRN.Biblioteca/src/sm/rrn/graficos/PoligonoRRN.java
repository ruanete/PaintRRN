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
 *
 * @author Raul
 */
public class PoligonoRRN extends ShapeRRN{
    private ArrayList<Point> puntosTrazo;
    
    public PoligonoRRN(){
        super(new Path2D.Double());
        puntosTrazo = new ArrayList<>();
    }
    
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

    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal) {
        if(puntosTrazo.size()==0){
            this.moveTo((Point) puntoInicial);
        }else{
            this.lineTo((Point) puntoFinal);
        }
    }
    
    public void moveTo(Point p){
        puntosTrazo.add(p);
        ((Path2D.Double) shape).moveTo(p.x, p.y);
    }
    
    public void lineTo(Point p){
        puntosTrazo.add(p);
        ((Path2D.Double) shape).lineTo(p.x, p.y);
    }
    
    public void closePath(){
        ((Path2D.Double) shape).closePath();
    }
    
    @Override
    public String toString() {
        return "Poligono";
    }
}
