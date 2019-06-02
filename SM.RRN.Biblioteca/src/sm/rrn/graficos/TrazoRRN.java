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
 *
 * @author Raul
 */
public class TrazoRRN extends AtributoRRN{
    BasicStroke stroke;
    Color color;
    TipoTrazo tipoTrazo;
    int tamañoTrazo;
            
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

    public Color getColor() {
        return color;
    }

    public TipoTrazo getTipoTrazo() {
        return tipoTrazo;
    }

    public int getTamañoTrazo() {
        return tamañoTrazo;
    }
        
    @Override
    public void aplicarAtributo(Graphics2D g2d) {
        g2d.setPaint(color);
        g2d.setStroke(stroke);
    }
    
}
