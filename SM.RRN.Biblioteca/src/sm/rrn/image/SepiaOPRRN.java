/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.image;

import java.awt.image.BufferedImage;
import sm.image.BufferedImageOpAdapter;

/**
 * Clase que define el filtro Sepia el cual es un color rojo anaranjado oscuro y de saturación débil y que aplicado a una imagen le da un efecto de antigüedad. 
 * Es un filtro pixel componente a componente el cual aplica un color a cada componente respectivo a la siguiente ecuación:
 * sepiaR = min(255 , 0.393·R + 0.769·G + 0.189·B)
 * sepiaG = min(255, 0.349·R + 0.686·G + 0.168·B)
 * sepiaB = min(255, 0.272·R + 0.534·G + 0.131·B)
 * @author Raúl Ruano Narváez
 */
public class SepiaOPRRN extends BufferedImageOpAdapter{
    
    /**
     * Constructor por defecto
     */
    public SepiaOPRRN(){
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
            for (int x = 0; x < src.getWidth(); x++) {
                for (int y = 0; y < src.getHeight(); y++) {
                    pixelComp = src.getRaster().getPixel(x, y, pixelComp);
                    pixelComp[0]=Integer.min(255, (int) (0.393*pixelComp[0] + 0.769*pixelComp[1] + 0.189*pixelComp[2]));
                    pixelComp[1]=Integer.min(255, (int) (0.349*pixelComp[0] + 0.686*pixelComp[1] + 0.168*pixelComp[2]));
                    pixelComp[2]=Integer.min(255, (int) (0.272*pixelComp[0] + 0.534*pixelComp[1] + 0.131*pixelComp[2]));
                    dest.getRaster().setPixel(x, y, pixelComp);
                }
            }    
        }else{
            throw new NullPointerException("src image does not have 3 bands");
        }

        
            
        return dest;
    }    
}
