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
 * Clase que define funciones LookUp aplicables a la imagen. En este caso se pueden realizar la función TYPE_SENO, TYPE_INVERTIR y TYPE_FUNCTIONRRN.
 * @author Raúl Ruano Narváez
 */
public class ImageFunctionRRN {

    /**
     * Modo función SENO 𝑓(x) = |sin(w*x)|, siendo w la velocidad angular y x el valor del color
     */
    public static final int TYPE_SENO = 0;

    /**
     * Modo función INVERTIR, siendo el valor del color 255 - color actual
     */
    public static final int TYPE_INVERTIR = 1;

    /**
     * Modo función FUNCTIONRRN realizando el 55% del arcocoseno del color actual
     */
    public static final int TYPE_FUNCTIONRRN = 2;
    
    /**
     * Método que genera un LookupTable aplicable a la imagen con el modo función elegido.
     * @param tipo_funcion Modo usado TYPE_SENO, TYPE_INVERTIR o TYPE_FUNCTIONRRN
     * @return LookupTable con el modo usado
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
     * Método que genera el LookupTable de la función SENO
     * @param w Velocidad angular
     * @return LookupTable con la función seno
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
     * Método que genera el LookupTable de la función NEGATIVO
     * @return LookupTable con la función negativo
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
     * Método que genera el LookupTable de la función FUNCTIONRRN
     * @return LookupTable con la función functionRRN
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
