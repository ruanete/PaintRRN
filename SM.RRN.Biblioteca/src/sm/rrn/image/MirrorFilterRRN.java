/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import sm.image.BufferedImageOpAdapter;

/**
 * Clase que define un filtro pixel a pixel el cual realiza el efecto espejo de la imagen. Lo que hace simplemente es mover los pixeles de la izquierda a la derecha y viceversa
 * @author Raúl Ruano Narváez
 */
public class MirrorFilterRRN extends BufferedImageOpAdapter{
    
    /**
     * Constructor por defecto
     */
    public MirrorFilterRRN(){
        ;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        if (src == null) {
            throw new NullPointerException("src image is null");
        }
           
        if (dest == null) {
            dest = createCompatibleDestImage(src, null);
        }
        
        if(src.getRaster().getNumBands() >=3){
            int[] pixelComp = new int[src.getRaster().getNumBands()];
            for(int y=0; y < src.getHeight(); y++){   
                ArrayList v = new ArrayList();
                for (int x = 0; x < src.getWidth(); x++) {
                    v.add(new Color(src.getRGB(x, y)));
                }
                Collections.reverse(v);
                for (int i = 0; i < v.size(); i++) {
                    dest.setRGB(i, y, ((Color)v.get(i)).getRGB());
                }
            }
        }else{
            throw new NullPointerException("src image does not have 3 bands");
        }

        return dest;
    }
    
}
