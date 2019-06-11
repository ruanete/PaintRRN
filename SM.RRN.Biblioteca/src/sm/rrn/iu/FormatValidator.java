/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.iu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que define un validador de formatos de archivos, en este caso es posible validar archivos de imagen y de sonido usado a la hora de abrir fichero.
 * @author Raúl Ruano Narváez
 */
public class FormatValidator {
    private Pattern pattern;
    private Matcher matcher;
  
    /**
     * Expresión regular para ficheros de imagenes
     */
    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    /**
     * Expresión regular para ficheros de sonido
     */
    public static final String SOUND_PATTERN = "([^\\s]+(\\.(?i)(wav|au|mp3))$)";
  
    /**
     * Constructor al cual le pasas una expresión regular y genera un Pattern que es usado por el validador de expresiones regulares.
     * @param Opcion Para facilitar el uso al usuario existen dos opciones definidas, FormatValidator.IMAGE_PATTERN y FormatValidator.SOUND_PATTERN, para las imagenes y sonidos respectivamente.
     */
    public FormatValidator(String Opcion){
       pattern = Pattern.compile(Opcion);
    }
  
    /**
     * Método que comprueba si un String es validado por la expresión regular anteriormente generada. 
     * @param file Nombre del fichero (String) que se va a validar
     * @return True o false si se corresponde al patrón elegido
     */
    public boolean validate(final String file){
       matcher = pattern.matcher(file);
       return matcher.matches();
    }
}
