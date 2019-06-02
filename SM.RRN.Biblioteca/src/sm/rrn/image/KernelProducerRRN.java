/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.image;

import java.awt.image.Kernel;

/**
 *
 * @author raulr
 */
public class KernelProducerRRN {
    public static final int TYPE_MEDIA_5x5 = 0;
    public static final int TYPE_MEDIA_7x7 = 1;
    
    public static float [] MASCARA_MEDIA_5x5 = {};
    public static float [] MASCARA_MEDIA_7x7 = {};
    
    public static float [] crearMascaraMedia(int valor){
        float vector[] = new float[valor*valor];
        
        for(int i=0;i<valor*valor;i++){
            vector[i] = 1.0F/(valor*valor);
        }
                
        return vector;
    }
    
    public static Kernel createKernel(int opcion){
        if(opcion==TYPE_MEDIA_5x5){
            MASCARA_MEDIA_5x5 = crearMascaraMedia(5);
            return new Kernel(5, 5, MASCARA_MEDIA_5x5);
        }else if(opcion==TYPE_MEDIA_7x7){
            MASCARA_MEDIA_7x7 = crearMascaraMedia(7);
            return new Kernel(7, 7, MASCARA_MEDIA_7x7);
        }
        
        return null;
    }
}
