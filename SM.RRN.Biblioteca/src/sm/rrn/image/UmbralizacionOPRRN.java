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
 * @author raulr
 */
public class UmbralizacionOPRRN extends BufferedImageOpAdapter{
    private int umbral;
    
    public UmbralizacionOPRRN(int umbral){
        this.umbral = umbral;
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
                    
                    double I = (RED+GREEN+BLUE)/3;
                                        
                    if(I >= umbral){
                        dest.setRGB(x, y, 255);
                    }else if(I < umbral){
                        dest.setRGB(x, y, 0);
                    }
                }
            }    
        }else{
            throw new NullPointerException("src image does not have 3 bands");
        }
        
        return dest;
    }
    
}
