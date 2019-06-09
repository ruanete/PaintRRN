/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

/**
 * Clase que define el atributo Transparencia
 * @author Raul Ruano Narvaez
 */
public class TransparenciaRRN extends AtributoRRN{
    boolean activado;
    AlphaComposite transparencia;
    float nivelTransparencia;

    /**
     * Constructor por parametros que asocia al ShapeRRN el atributo RellenoRRN
     * @param shape ShapeRRN al que se le asociado el atributo RellenoDegradadoRRN
     * @param activado Parametro que especifica si la transparencia del ShapeRRN esta activa o no
     * @param nivelTransparencia Valor con el nivel de transparencia
     */
    public TransparenciaRRN(ShapeRRN shape, boolean activado, float nivelTransparencia) {
        super(shape, "Transparencia");
        this.activado = activado;
        this.nivelTransparencia = nivelTransparencia;
        
        if(activado)
            transparencia = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, nivelTransparencia);
        else
            transparencia = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
    }
    
    /**
     * Método para setear el nivel de transparencia
     * @param nivelTransparencia Valor de la transparencia
     */
    public void setTransparencia(float nivelTransparencia){
        this.nivelTransparencia = nivelTransparencia;
        transparencia = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, nivelTransparencia);
    }

    /**
     * Método que devuelve el nivel de transparencia
     * @return Float con el nivel de transparencia
     */
    public float getNivelTransparencia() {
        return nivelTransparencia;
    }

    /**
     * Método que devuelve si esta activa o no la transparencia
     * @return Boolean con true o false
     */
    public boolean isActivado() {
        return activado;
    }

    @Override
    public void aplicarAtributo(Graphics2D g2d) {
        g2d.setComposite(transparencia);
    }
    
}
