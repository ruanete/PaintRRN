/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.image;

import java.awt.image.Kernel;

/**
 * Clase que define filtros para realizarle la convolución a la imagen con el Kernel generado en esta.
 * @author Raúl Ruano Narváez
 */
public class KernelProducerRRN {

    /**
     * Modo filtro TYPE_MEDIA_5x5, suavizado de 5x5
     */
    public static final int TYPE_MEDIA_5x5 = 0;

    /**
     * Modo filtro TYPE_MEDIA_7x7, suavizado de 7x7
     */
    public static final int TYPE_MEDIA_7x7 = 1;
    
    /**
     * MASCARA_MEDIA_5x5
     */
    public static float [] MASCARA_MEDIA_5x5 = {};

    /**
     * MASCARA_MEDIA_7x7
     */
    public static float [] MASCARA_MEDIA_7x7 = {};
    
    /**
     * Método que genera la máscara correspondiente al filtro de suavizado (media)
     * @param valor Valor del suavizado 1/ancho*alto de la mascara (5x5=25 luego la máscara sería de 1/25)
     * @return Vector de float con la máscara
     */
    public static float [] crearMascaraMedia(int valor){
        float vector[] = new float[valor*valor];
        
        for(int i=0;i<valor*valor;i++){
            vector[i] = 1.0F/(valor*valor);
        }
                
        return vector;
    }
        
    /**
     * Método que genera el Kernel respectivo a la máscara generada anteriormente con el método crearMascaraMedia(int valor)
     * @param opcion TYPE_MEDIA_5x5 o TYPE_MEDIA_7x7, suavizado de 5x5 o 7x7
     * @return Kernel asociado al suavizado elegido
     */
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
