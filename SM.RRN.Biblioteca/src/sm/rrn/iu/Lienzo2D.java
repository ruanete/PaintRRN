/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.iu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import sm.rrn.eventos.LienzoEvent;
import sm.rrn.eventos.LienzoListener;
import sm.rrn.graficos.AntialiasingRRN;
import sm.rrn.graficos.ArcoRRN;
import sm.rrn.graficos.AtributoRRN;
import sm.rrn.graficos.CubicCurveRRN;
import sm.rrn.graficos.ElipseRRN;
import sm.rrn.graficos.FormaPersonalizadaRRN;
import sm.rrn.graficos.LineaRRN;
import sm.rrn.graficos.PoligonoRRN;
import sm.rrn.graficos.PuntoRRN;
import sm.rrn.graficos.QuadCurveRRN;
import sm.rrn.graficos.RectanguloRRN;
import sm.rrn.graficos.RellenoDegradadoRRN;
import sm.rrn.graficos.RellenoRRN;
import sm.rrn.graficos.RoundedRectangleRRN;
import sm.rrn.graficos.ShapeRRN;
import sm.rrn.graficos.TransparenciaRRN;
import sm.rrn.graficos.TrazoLibreRRN;
import sm.rrn.graficos.TrazoRRN;

/**
 *
 * @author raulr
 */
public class Lienzo2D extends javax.swing.JPanel {
    private ArrayList<LienzoListener> lienzoEventListeners;
    private ArrayList<ShapeRRN> vShape;
    private ShapeRRN nuevaFigura, figura_seleccionada;
    private Point pIni, pFin, pRef;
    private int paso;
    
    protected ModoPintado modo_pintado;
    protected Color colorStroke, colorRelleno;
    protected int tamañoTrazo;
    protected TipoTrazo trazo;
    protected boolean relleno;
    protected TipoRelleno tipo_relleno;
    protected boolean transparencia;
    protected float nivelTransparencia;
    protected boolean alisar;
    
    
    /**
     * Creates new form Lienzo2D
     */
    public Lienzo2D() {
        initComponents();
        lienzoEventListeners = new ArrayList<>();
        vShape = new ArrayList();
        nuevaFigura = figura_seleccionada=null;
        pIni = pFin = pRef = null;
        paso=1;
        
        modo_pintado = ModoPintado.Punto;
        colorStroke = colorRelleno = new Color(0,0,0);
        tamañoTrazo=1;
        trazo=TipoTrazo.LINEA_CONTINUA;
        relleno=false;
        tipo_relleno=TipoRelleno.COLOR_SOLIDO;
        transparencia=false;
        nivelTransparencia = 1.0f;
        alisar=false;     
    }
    
    public Lienzo2D(ModoPintado modo) {
        this();
        modo_pintado = modo;
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d=(Graphics2D)g;

        g2d.clip(new Rectangle(this.getPreferredSize()));
        for( ShapeRRN s: vShape) { 
            s.drawShape(g2d, modo_pintado, figura_seleccionada);
        }
    }
    
    public void addLienzoListener(LienzoListener listener){
        if(listener!=null)
            lienzoEventListeners.add(listener);
    }
    
    
    private void notifyShapeAddedEvent(LienzoEvent evt) {
        if (!lienzoEventListeners.isEmpty()) {
            for (LienzoListener listener : lienzoEventListeners) {
                listener.shapeAdded(evt);
            }
        }
    }

    private void notifyShapeEdit(LienzoEvent evt) {
        if (!lienzoEventListeners.isEmpty()) {
            for (LienzoListener listener : lienzoEventListeners) {
                listener.updateLienzo(evt);
            }
        }
    }
    
    private void notifyShapeCoordEvent(LienzoEvent evt) {
        if (!lienzoEventListeners.isEmpty()) {
            for (LienzoListener listener : lienzoEventListeners) {
                listener.updateCoord(evt);
            }
        }
    }
    
    private void notifyEditableCoordEvent(LienzoEvent evt) {
        if (!lienzoEventListeners.isEmpty()) {
            for (LienzoListener listener : lienzoEventListeners) {
                listener.updateCoordEditable(evt);
            }
        }
    }
    
    public void setLienzoEdit(){
        if(figura_seleccionada!=null){
            relleno=false;
            alisar=false;
            transparencia=false;
            TrazoRRN trazoEDIT = (TrazoRRN) figura_seleccionada.getAtributoTrazo();
            RellenoRRN rellenoEDIT = (RellenoRRN) figura_seleccionada.getAtributoRelleno();
            RellenoDegradadoRRN rellenoDegradadoEDIT = (RellenoDegradadoRRN) figura_seleccionada.getAtributoRellenoDegradado();
            TransparenciaRRN transparenciaEDIT = (TransparenciaRRN) figura_seleccionada.getAtributoTransparencia();
            AntialiasingRRN antialiasingEDIT = (AntialiasingRRN) figura_seleccionada.getAtributoAntialising();
            
            if(trazoEDIT!=null){
                colorStroke = trazoEDIT.getColor();
                tamañoTrazo = trazoEDIT.getTamañoTrazo();
                trazo = trazoEDIT.getTipoTrazo();
            }

            if(rellenoEDIT!=null){
                relleno=true;
                colorRelleno = rellenoEDIT.getColor();
                tipo_relleno = TipoRelleno.COLOR_SOLIDO;
            }

            if(rellenoDegradadoEDIT!=null){
                relleno=true;
                colorStroke = rellenoDegradadoEDIT.getColorInicio();
                colorRelleno = rellenoDegradadoEDIT.getColorFinal();
                tipo_relleno = rellenoDegradadoEDIT.getTipoRelleno();
            }

            if(transparenciaEDIT!=null){
                transparencia = transparenciaEDIT.isActivado();
                nivelTransparencia = transparenciaEDIT.getNivelTransparencia();
            }

            if(antialiasingEDIT!=null){
                alisar = antialiasingEDIT.isActivado();
            } 
            
            notifyShapeEdit(new LienzoEvent(this,nuevaFigura,this) );
        } 
    }
    
    public ShapeRRN createShape(Point puntoInicial){
        ShapeRRN shape = null;
        if(modo_pintado!=null && puntoInicial!=null){
            switch(modo_pintado){
                case Punto:
                    shape = (ShapeRRN) new PuntoRRN(puntoInicial);
                    break;
                case Linea:
                    shape = (ShapeRRN) new LineaRRN(puntoInicial, puntoInicial);
                    break;
                case Rectangulo:
                    shape = (ShapeRRN) new RectanguloRRN((Point)puntoInicial, 1, 1);
                    break;
                case Elipse:
                    shape = (ShapeRRN) new ElipseRRN(puntoInicial, 1, 1);
                    break;
                case RoundedRectangle:
                    shape = new RoundedRectangleRRN(0, 0, 1, 1, pIni);
                    break;
                case Arco:
                    shape = new ArcoRRN(puntoInicial, 1, 1, 0, 180, Arc2D.OPEN);
                    break;
                case QuadCurve:
                    shape = new QuadCurveRRN(puntoInicial, puntoInicial, puntoInicial);
                    break;
                case CubicCurve:
                    shape = (ShapeRRN) new CubicCurveRRN(puntoInicial, puntoInicial, puntoInicial, puntoInicial);
                    break;
                case TrazoLibre:
                    shape = new TrazoLibreRRN();
                    break;
                case Poligono:
                    shape = new PoligonoRRN();
                    break; 
                case FormaPersonalizada:
                    shape = new FormaPersonalizadaRRN(puntoInicial);
                    updateAtributes(shape);
                    break;
            }
        }
                
        return shape;
    }
    
    public void updateShape(ShapeRRN shape, Point puntoInicial, Point puntoFinal){
        if(shape!=null && puntoInicial!=null && puntoFinal!=null){
            shape.setFinalShape(puntoInicial, puntoFinal);
            updateAtributes(shape);
        }   
    }
    
    public void editShape(ShapeRRN shape, Point puntoInicial, Point puntoFinal, Point puntoRef){
        if(shape!=null && puntoRef!=null && puntoInicial!=null && puntoFinal!=null){
            Point p;
            p = new Point((int)puntoRef.getX() + (int)puntoFinal.getX()-(int)puntoInicial.getX(), (int)puntoRef.getY() + (int)puntoFinal.getY()-(int)puntoInicial.getY());
            shape.setLocation(p);
        }
    }
    
    public void updateAtributes(ShapeRRN shape){ 
        shape.añadirAtributo(new TransparenciaRRN(shape, transparencia, nivelTransparencia));
        
        if(relleno){
            switch (tipo_relleno) {
                case COLOR_SOLIDO:
                    shape.añadirAtributo(new RellenoRRN(shape, relleno, colorRelleno));
                    break;
                case DEGRADADO_VERTICAL:
                    shape.añadirAtributo(new RellenoDegradadoRRN(shape, relleno, colorStroke, colorRelleno, TipoRelleno.DEGRADADO_VERTICAL));
                    break;
                case DEGRADADO_HORIZONTAL:
                    shape.añadirAtributo(new RellenoDegradadoRRN(shape, relleno, colorStroke, colorRelleno, TipoRelleno.DEGRADADO_HORIZONTAL));
                    break;
                default:
                    break;
            }
        }else{
            shape.eliminarRellenos();
        }
            
        shape.añadirAtributo(new TrazoRRN(shape, colorStroke, trazo, tamañoTrazo));
        shape.añadirAtributo(new AntialiasingRRN(shape, alisar));
        
        repaint();
    }
    
    private ShapeRRN getSelectedShape(Point2D p){
        for(ShapeRRN s:vShape)
            if(s.contains(p)) return s;
        return null;
    }
    
    

    ////////////////////////
    //  GETTERS Y SETTERS //
    ////////////////////////
    
    public ArrayList<ShapeRRN> getvShape() {
        return vShape;
    }
    public void setvShape(ArrayList<ShapeRRN> vShape) {    
        this.vShape = vShape;
    }

    public ShapeRRN getFigura_seleccionada() {
        return figura_seleccionada;
    }

    public void setFigura_seleccionada(ShapeRRN figura_seleccionada) {
        this.figura_seleccionada = figura_seleccionada;
    }
    
    public ModoPintado getModoPintado() {
        return modo_pintado;
    }

    public void setModoPintado(ModoPintado modo_pintado) {
        this.modo_pintado = modo_pintado;
        if(modo_pintado!=ModoPintado.Edicion)
            notifyEditableCoordEvent(new LienzoEvent(this,nuevaFigura, this) );
    }

    public Color getColorStroke() {
        return colorStroke;
    }
    
    public void setColorStroke(Color colorStroke) {
        this.colorStroke = colorStroke;
    }
    
    public Color getColorRelleno() {
        return colorRelleno;
    }

    public void setColorRelleno(Color colorRelleno) {
        this.colorRelleno = colorRelleno;
    }
    
    public int getTamañoTrazo() {
        return tamañoTrazo;
    }

    public void setTamañoTrazo(int tamañoTrazo) {
        this.tamañoTrazo = tamañoTrazo;
    }
    
    public TipoTrazo getTrazo() {
        return trazo;
    }

    public void setTrazo(TipoTrazo trazo) {
        this.trazo = trazo;
    }

    public boolean isRelleno() {
        return relleno;
    }

    public void setRelleno(boolean relleno) {
        this.relleno = relleno;
    }
    
    public TipoRelleno getTipoRelleno() {
        return tipo_relleno;
    }
    
    public void setTipoRelleno(TipoRelleno relleno) {
        this.tipo_relleno = relleno;
    }
    
    public boolean isTransparencia() {
        return transparencia;
    }
    
    public void setTransparencia(boolean transparencia) {
        this.transparencia = transparencia;
    }
    
    public float getNivelTransparencia() {
        return nivelTransparencia;
    }
    
    public void setNivelTransparencia(float nivelTransparencia) {
        this.nivelTransparencia = nivelTransparencia;
    }
    
    public boolean isAlisar() {
        return alisar;
    }

    public void setAlisar(boolean alisar) {
        this.alisar = alisar;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
        if(modo_pintado!=ModoPintado.Poligono){
            pIni = evt.getPoint();
        
            if(modo_pintado==ModoPintado.Edicion){
                figura_seleccionada = getSelectedShape(evt.getPoint());
                if(figura_seleccionada!=null){
                    setLienzoEdit();
                    pRef = figura_seleccionada.getBounds().getLocation();
                }else{
                    notifyEditableCoordEvent(new LienzoEvent(this,nuevaFigura, this) );
                }
                    
            }else{
                figura_seleccionada=null;
                if(paso<=1){
                    nuevaFigura = createShape(pIni);
                    vShape.add(nuevaFigura);
                    notifyShapeAddedEvent( new LienzoEvent(this,nuevaFigura, this) );
                }
            }

            this.repaint();
        }
        
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
        if(modo_pintado!=ModoPintado.Poligono){
            pFin = evt.getPoint();

            if(modo_pintado==ModoPintado.Edicion){
                editShape(figura_seleccionada, pIni, pFin, pRef);
                notifyShapeCoordEvent(new LienzoEvent(this,nuevaFigura,this) );
            }else{
                if(paso==3){
                    if(modo_pintado==ModoPintado.CubicCurve)
                        ((CubicCurveRRN)nuevaFigura).setSegundoPuntoControl(pFin);              
                }else if(paso==2){
                    if(modo_pintado==ModoPintado.Arco)
                        ((ArcoRRN)nuevaFigura).setAngle(pFin.getY()-pIni.getY()); 
                    else if(modo_pintado==ModoPintado.RoundedRectangle)
                        ((RoundedRectangleRRN)nuevaFigura).setArcoBorde(pFin.getY()-pIni.getY());
                    else if(modo_pintado==ModoPintado.QuadCurve)
                        ((QuadCurveRRN)nuevaFigura).setPuntoControl(pFin);
                    else if(modo_pintado==ModoPintado.CubicCurve)
                        ((CubicCurveRRN)nuevaFigura).setPrimerPuntoControl(pFin);
                }else if(modo_pintado!=ModoPintado.FormaPersonalizada){
                    
                    updateShape(nuevaFigura, pIni, pFin);
                    if(modo_pintado==ModoPintado.TrazoLibre)
                        pIni=pFin;
                    
                }
            }

            this.repaint();
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        // TODO add your handling code here:
        if(modo_pintado!=ModoPintado.Poligono){
            pFin = evt.getPoint();

            if(modo_pintado==ModoPintado.Edicion){    
                editShape(figura_seleccionada, pIni, pFin, pRef);
            }else if(modo_pintado==ModoPintado.TrazoLibre){
                ((TrazoLibreRRN)nuevaFigura).closePath();
            }else{
                if(paso==3){
                    if(modo_pintado==ModoPintado.CubicCurve)
                        ((CubicCurveRRN)nuevaFigura).setSegundoPuntoControl(pFin);              
                }else if(paso==2){
                    if(modo_pintado==ModoPintado.Arco)
                        ((ArcoRRN)nuevaFigura).setAngle(pFin.getY()-pIni.getY());
                    else if(modo_pintado==ModoPintado.RoundedRectangle)
                        ((RoundedRectangleRRN)nuevaFigura).setArcoBorde(pFin.getY()-pIni.getY());
                    else if(modo_pintado==ModoPintado.QuadCurve)
                        ((QuadCurveRRN)nuevaFigura).setPuntoControl(pFin);
                    else if(modo_pintado==ModoPintado.CubicCurve)
                        ((CubicCurveRRN)nuevaFigura).setPrimerPuntoControl(pFin);
                }else if(modo_pintado!=ModoPintado.FormaPersonalizada){
                    updateShape(nuevaFigura, pIni, pFin);
                }

                if(modo_pintado==ModoPintado.Arco || modo_pintado==ModoPintado.RoundedRectangle || modo_pintado==ModoPintado.QuadCurve || modo_pintado==ModoPintado.CubicCurve){
                    if(modo_pintado==ModoPintado.CubicCurve && paso==3)
                        paso=1;
                    else if(modo_pintado!=ModoPintado.CubicCurve && paso==2)
                        paso=1;
                    else
                        paso++;
                }   
            }

            pIni=null;
            pFin=null;
            pRef=null;

            this.repaint();
        }
    }//GEN-LAST:event_formMouseReleased

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        if(modo_pintado==ModoPintado.Poligono){
            if (evt.getClickCount()==2){
                ((PoligonoRRN)nuevaFigura).closePath();
                paso=1;
            }else{ 
                if(paso<=1){
                    pIni = evt.getPoint();
                    nuevaFigura = createShape(pIni);
                    vShape.add(nuevaFigura);
                    notifyShapeAddedEvent( new LienzoEvent(this,nuevaFigura, this) );
                    updateShape(nuevaFigura, pIni, pIni);
                    paso++;
                }else{
                    pFin = evt.getPoint();
                    updateShape(nuevaFigura, pIni, pFin);
                    pIni=pFin;
                    paso++;
                }
            }
            this.repaint();
        }
    }//GEN-LAST:event_formMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
