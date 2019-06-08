/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.image;

import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

/**
 *
 * @author Raúl Ruano Narváez
 */
public class ImageFunctionRRN {

    /**
     *
     */
    public static final int TYPE_SENO = 0;

    /**
     *
     */
    public static final int TYPE_INVERTIR = 1;

    /**
     *
     */
    public static final int TYPE_FUNCTIONRRN = 2;
    
    /**
     *
     * @param tipo_funcion
     * @return
     */
    public static LookupTable createFunction(int tipo_funcion){
        LookupTable lt = null;
        
        switch(tipo_funcion){
            case TYPE_SENO:
                lt = seno(Math.PI/255.0);
                break;
            case TYPE_INVERTIR:
                lt = negativo();
                break;
            case TYPE_FUNCTIONRRN:
                lt = functionRRN();
                break;
        }
        
        return lt;
    }
    
    /**
     *
     * @param w
     * @return
     */
    public static LookupTable seno(double w){
        double K = 255.0;
        byte f[] = new byte[256];
        
        for (int i=0; i<256; i++)
            f[i] = (byte)(K * Math.abs(Math.sin(w*i)));
        
        LookupTable lt = new ByteLookupTable(0, f);
        
        return lt;
    }
    
    /**
     *
     * @return
     */
    public static LookupTable negativo(){
        double K = 255.0;
        byte f[] = new byte[256];
        
        for (int i=0; i<256; i++)
            f[i] = (byte)(255-i);
        
        LookupTable lt = new ByteLookupTable(0, f);
        
        return lt;
    } 

    /**
     *
     * @return
     */
    public static LookupTable functionRRN(){
        double K = 255.0;
        byte f[] = new byte[256];
        
        for (int i=0; i<256; i++)
            f[i] = (byte) (K * Math.abs(Math.acos(Math.toRadians(i))*0.55));
        
        LookupTable lt = new ByteLookupTable(0, f);
        
        return lt;
    }
}
