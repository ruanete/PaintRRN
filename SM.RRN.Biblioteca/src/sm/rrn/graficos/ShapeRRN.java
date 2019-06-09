/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import sm.rrn.iu.LienzoImagen2D;
import sm.rrn.iu.ModoPintado;

/**
 * Clase abstracta que es padre de todas las figuras contendrá un Shape de Java al que hace referencia y un vector de AtributosRRN
 * @author Raúl Ruano Narváez
 */
public abstract class ShapeRRN implements Shape{
    protected Shape shape;
    protected ArrayList<AtributoRRN> atributos = new ArrayList<AtributoRRN>();
    
    /**
     * Constructor por parámetros
     * @param shape Shape al que se le asociará el ShapeRRN
     */
    public ShapeRRN(Shape shape){
        this.shape = shape;
    }
    
    /**
     * Constructor por parámetros
     * @param shape Shape al que se le asociará el ShapeRRN
     * @param atributos Vector de AtributoRRN
     */
    public ShapeRRN(Shape shape, ArrayList<AtributoRRN> atributos){
        this.shape = shape;
        this.atributos = atributos;
    }
    
    /**
     * Constructor de copia
     * @param shape ShapeRRN que será copiado
     */
    public ShapeRRN(ShapeRRN shape){
        this.shape = shape.shape;
        this.atributos = shape.atributos;
    }
    
    /**
     * Método para modificar la posición de una figura
     * @param pos
     */
    public abstract void setLocation(Point2D pos);

    /**
     * Método para setear la posición inicial cuando una figura es creada
     * @param puntoInicial Punto inicial
     * @param puntoFinal Punto final
     */
    public abstract void setFinalShape(Point2D puntoInicial, Point2D puntoFinal);
     
    /**
     * Método para imprimir por la salida estandar los atributos de la figura asociada
     */
    public void imprimirAtributos(){
        if(!atributos.isEmpty()){
            System.out.println("\nATRIBUTO: ");
            for(AtributoRRN a: atributos){
                if(a instanceof TrazoRRN)
                    System.out.println("-" + a.getNombreAtributo() + "-- tam: " + ((TrazoRRN) a).getTamañoTrazo() + "-- color: " + ((TrazoRRN) a).getColor() + " -- tipo: " + ((TrazoRRN) a).getTipoTrazo());
                else if(a instanceof AntialiasingRRN)
                    System.out.println("-" + a.getNombreAtributo() + "-- activado: " + ((AntialiasingRRN) a).isActivado());
                else if(a instanceof RellenoDegradadoRRN)
                    System.out.println("-" + a.getNombreAtributo() + "-- color1: " + ((RellenoDegradadoRRN) a).getColorInicio() + " -- color2: " + ((RellenoDegradadoRRN) a).getColorFinal() + " --tipo: " + ((RellenoDegradadoRRN) a).getTipoRelleno());
                else if(a instanceof RellenoRRN)
                    System.out.println("-" + a.getNombreAtributo() + "-- color: " + ((RellenoRRN) a).getColor());
                else if(a instanceof TransparenciaRRN)
                    System.out.println("-" + a.getNombreAtributo() + "-- nivel: " + ((TransparenciaRRN) a).getNivelTransparencia());
            }       
        }
    }
    
    /////////////////////
    //METODOS ATRIBUTOS//
    /////////////////////

    /**
     * Método que dibuja una figura con sus atributos
     * @param g2d Graphics2D que permitirá el pintado de la figura
     * @param modoPintado Modo en el que se va a pintar para realizar el bound a la figura o no, concretamente en el modo edición
     * @param figura_seleccionada Figura usada para la edición
     */
    
    public void drawShape(Graphics2D g2d, ModoPintado modoPintado, ShapeRRN figura_seleccionada){
        if(modoPintado == ModoPintado.Edicion && figura_seleccionada!=null && figura_seleccionada.equals(this) && LienzoImagen2D.activarBound){
            Stroke trazo;
            float patronDiscontinuidad[] = {5.0f, 5.0f};
            trazo = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, patronDiscontinuidad, 0.0f);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(trazo);
            g2d.draw(shape.getBounds());
        }
        
        aplicarAtributos(g2d);
        g2d.draw(shape);
    }
    
    /**
     * Método que realiza llamadas a aplicarAtributo() de cada atributo asociado a la figura
     * @param g2d Graphics2D que permitirá el pintado de la figura
     */
    public void aplicarAtributos(Graphics2D g2d){
        for(AtributoRRN a: atributos)
            a.aplicarAtributo(g2d);
    }
    
    /**
     * Método para añadir un nuevo AtributoRRN a la figura
     * @param atributo AtributoRRN que se añadirá
     */
    public void añadirAtributo(AtributoRRN atributo){
        ArrayList<AtributoRRN> atributosAux = new ArrayList<AtributoRRN>();
        
        if(!atributos.isEmpty()){
            if(atributo.getNombreAtributo().equals("Relleno") || atributo.getNombreAtributo().equals("RellenoDegradado"))
                eliminarRellenos();
            
            for(AtributoRRN a: atributos)
                if(!a.getNombreAtributo().equals(atributo.getNombreAtributo()))
                    atributosAux.add(a);            
        }
        
        this.atributos.clear();
        this.atributos = atributosAux;
        
        atributos.add(atributo);
        
    }
    
    /**
     * Método que elimina todos los rellenos de la figura
     */
    public void eliminarRellenos(){
        ArrayList<AtributoRRN> atributosAux = new ArrayList<AtributoRRN>();
        
        if(!atributos.isEmpty()){
            for(AtributoRRN a: atributos)
                if(!a.getNombreAtributo().equals("Relleno") && !a.getNombreAtributo().equals("RellenoDegradado"))
                    atributosAux.add(a);
        }
        
        this.atributos.clear();
        this.atributos = atributosAux;    
    }
    
    /**
     * Método para eliminar las transparencias de la figura
     */
    public void eliminarTransparencias(){
        ArrayList<AtributoRRN> atributosAux = new ArrayList<AtributoRRN>();
        
        if(!atributos.isEmpty()){
            for(AtributoRRN a: atributos)
                if(!a.getNombreAtributo().equals("Transparencia"))
                    atributosAux.add(a);
        }
        
        this.atributos.clear();
        this.atributos = atributosAux;    
    }

    /**
     * Metodo que devuelve el vector de AtributoRRN
     * @return Vector de AtributoRRN
     */
    public ArrayList<AtributoRRN> getAtributos() {
        return atributos;
    }

    /**
     * Método que setea un vector de AtributoRRN al ShapeRRN
     * @param atributos Vector de AtributoRRN que se quiere setear
     */
    public void setAtributos(ArrayList<AtributoRRN> atributos) {
        this.atributos = atributos;
    }
    
    /**
     * Método para borrar todo el vector de atributos del ShapeRRN
     */
    public void clearAtributos(){
        atributos.clear();
    }
    
    /**
     * Método que devuelve el atributo AntialisingRRN
     * @return AtributoRRN con el atributo AntialisingRRN
     */
    public AtributoRRN getAtributoAntialising(){
        if(!atributos.isEmpty()){
            for(AtributoRRN a: atributos)
                if(a.getNombreAtributo().equals("Antialiasing"))
                    return a;
        }   
        return null;
    }
    
    /**
     * Método que devuelve el atributo RellenoDegradadoRRN
     * @return AtributoRRN con el atributo RellenoDegradadoRRN
     */
    public AtributoRRN getAtributoRellenoDegradado(){
        if(!atributos.isEmpty()){
            for(AtributoRRN a: atributos)
                if(a.getNombreAtributo().equals("RellenoDegradado"))
                    return a;
        }   
        return null;
    }
    
    /**
     * Método que devuelve el atributo RellenoRRN
     * @return AtributoRRN con el atributo RellenoRRN
     */
    public AtributoRRN getAtributoRelleno(){
        if(!atributos.isEmpty()){
            for(AtributoRRN a: atributos)
                if(a.getNombreAtributo().equals("Relleno"))
                    return a;
        }   
        return null;
    }
    
    /**
     * Método que devuelve el atributo TransparenciaRRN
     * @return AtributoRRN con el atributo TransparenciaRRN
     */
    public AtributoRRN getAtributoTransparencia(){
        if(!atributos.isEmpty()){
            for(AtributoRRN a: atributos)
                if(a.getNombreAtributo().equals("Transparencia"))
                    return a;
        }   
        return null;
    }
    
    /** 
     * Método que devuelve el atributo TrazoRRN
     * @return AtributoRRN con el atributo TrazoRRN
     */
    public AtributoRRN getAtributoTrazo(){
        if(!atributos.isEmpty()){
            for(AtributoRRN a: atributos)
                if(a.getNombreAtributo().equals("Trazo"))
                    return a;
        }   
        return null;
    }
    
    @Override
    public abstract String toString();
    
    ////////////////////////
    //METODOS OBLIGATORIOS//
    ////////////////////////
    
    @Override
    public Rectangle getBounds() {
        return shape.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return shape.getBounds();
    }

    @Override
    public boolean contains(double x, double y) {     
        return shape.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return shape.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return shape.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return shape.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return shape.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return shape.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return shape.getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return shape.getPathIterator(at, flatness);
    }
}
