/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.graficos;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

/**
 *
 * @author Raul
 */
public class TransparenciaRRN extends AtributoRRN{
    boolean activado;
    AlphaComposite transparencia;
    float nivelTransparencia;

    /**
     *
     * @param shape
     * @param activado
     * @param nivelTransparencia
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
     *
     * @param nivelTransparencia
     */
    public void setTransparencia(float nivelTransparencia){
        this.nivelTransparencia = nivelTransparencia;
        transparencia = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, nivelTransparencia);
    }

    /**
     *
     * @return
     */
    public float getNivelTransparencia() {
        return nivelTransparencia;
    }

    /**
     *
     * @return
     */
    public boolean isActivado() {
        return activado;
    }

    @Override
    public void aplicarAtributo(Graphics2D g2d) {
        g2d.setComposite(transparencia);
    }
    
}
