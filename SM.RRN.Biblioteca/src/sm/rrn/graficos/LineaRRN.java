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
 *
 * @author raulr
 */
public class LineaRRN extends ShapeRRN{
    
    public LineaRRN(Point2D puntoInicial, Point2D puntoFinal){
        super(new Line2D.Double(puntoInicial, puntoFinal));
    }
    
    public boolean isNear(Point2D p){
        return ((Line2D.Double) shape).ptLineDist(p)<=4.0;
    }
    
    @Override
    public boolean contains(Point2D p){
        return isNear(p);
    }
    
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
    
    @Override
    public void setFinalShape(Point2D puntoInicial, Point2D puntoFinal){
        ((Line2D.Double) shape).setLine(puntoInicial, puntoFinal);
    }
    
    @Override
    public String toString() {
        return "Linea";
    }
}
