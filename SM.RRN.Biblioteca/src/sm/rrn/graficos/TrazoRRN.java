/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import sm.rrn.iu.TipoTrazo;

/**
 * Clase que define el atributo Trazo
 * @author Raul Ruano Narváez
 */
public class TrazoRRN extends AtributoRRN{
    BasicStroke stroke;
    Color color;
    TipoTrazo tipoTrazo;
    int tamañoTrazo;
            
    /**
     * Constructor por parametros que asocia al ShapeRRN el atributo TrazoRRN
     * @param shape ShapeRRN al que se le asociado el atributo RellenoDegradadoRRN
     * @param color Color del trazo
     * @param tipoTrazo Tipo del trazo: LINEA_CONTINUA o LINEA_DISCONTINUA
     * @param tamañoTrazo Tamaño del trazo, el grosor
     */
    public TrazoRRN(ShapeRRN shape, Color color, TipoTrazo tipoTrazo, int tamañoTrazo){
        super(shape, "Trazo");
        this.color = color;
        this.tipoTrazo = tipoTrazo;
        this.tamañoTrazo = tamañoTrazo;
        
        if(tipoTrazo==TipoTrazo.LINEA_CONTINUA){
            stroke = new BasicStroke(tamañoTrazo);
        }else if(tipoTrazo==TipoTrazo.LINEA_DISCONTINUA){
            float patronDiscontinuidad[] = {5.0f, 5.0f};
            this.stroke = new BasicStroke(tamañoTrazo, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, patronDiscontinuidad, 0.0f);
        }  
    }

    /**
     * Método que devuelve el color del trazo
     * @return Color del trazo
     */
    public Color getColor() {
        return color;
    }

    /**
     * Método que devuelve el tipo de trazo
     * @return TipoTrazo con el tipo de trazo
     */
    public TipoTrazo getTipoTrazo() {
        return tipoTrazo;
    }

    /**
     * Método que devuelve el grosor del trazo
     * @return Int con el grosor del trazo
     */
    public int getTamañoTrazo() {
        return tamañoTrazo;
    }
        
    @Override
    public void aplicarAtributo(Graphics2D g2d) {
        g2d.setPaint(color);
        g2d.setStroke(stroke);
    }
    
}
