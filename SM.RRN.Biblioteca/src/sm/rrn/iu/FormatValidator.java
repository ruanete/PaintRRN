/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.iu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Raúl Ruano Narváez
 */
public class FormatValidator {
    private Pattern pattern;
    private Matcher matcher;
  
    /**
     *
     */
    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    /**
     *
     */
    public static final String SOUND_PATTERN = "([^\\s]+(\\.(?i)(wav|au|mp3))$)";
  
    /**
     *
     * @param Opcion
     */
    public FormatValidator(String Opcion){
       pattern = Pattern.compile(Opcion);
    }
  
    /**
     *
     * @param file
     * @return
     */
    public boolean validate(final String file){
       matcher = pattern.matcher(file);
       return matcher.matches();
    }
}
