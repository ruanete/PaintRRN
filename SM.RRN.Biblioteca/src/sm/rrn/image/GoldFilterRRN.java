/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import sm.image.BufferedImageOpAdapter;

/**
 *
 * @author Raúl Ruano Narváez
 */
public class GoldFilterRRN extends BufferedImageOpAdapter{
    
    /**
     *
     */
    public GoldFilterRRN(){
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
            for (int x = 0; x < src.getWidth(); x++) {
                for (int y = 0; y < src.getHeight(); y++) {
                    Color colorPixel = new Color(src.getRGB(x, y));
                    
                    int RED = (int)colorPixel.getRed();
                    int GREEN = (int)colorPixel.getGreen();
                    int BLUE = (int)colorPixel.getBlue();
                    
                    //DORADO CLARO (RGB: (212, 175, 55) %=(83, 68, 21))
                    RED = Math.min(255, (int) ((colorPixel.getRed()+colorPixel.getGreen() + colorPixel.getBlue())*0.83));
                    GREEN = Math.min(255, (int) ((colorPixel.getRed() + colorPixel.getGreen() + colorPixel.getBlue())*0.68));
                    BLUE = Math.min(255, (int) ((colorPixel.getRed() + colorPixel.getGreen() + colorPixel.getBlue())*0.21));
                    
                    //DORADO VIEJO
                    /*RED = Math.min(255, (int) ((colorPixel.getRed()+colorPixel.getGreen() + colorPixel.getBlue())*0.81));
                    GREEN = Math.min(255, (int) ((colorPixel.getRed() + colorPixel.getGreen() + colorPixel.getBlue())*0.71));
                    BLUE = Math.min(255, (int) ((colorPixel.getRed() + colorPixel.getGreen() + colorPixel.getBlue())*0.23));*/
                                        
                    dest.setRGB(x, y, new Color(RED, GREEN, BLUE).getRGB());
                }
            }    
        }else{
            throw new NullPointerException("src image does not have 3 bands");
        }
        
        return dest;
    }
    
}
