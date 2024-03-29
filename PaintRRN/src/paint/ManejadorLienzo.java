/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import sm.rrn.eventos.LienzoEvent;
import sm.rrn.eventos.LienzoListener;
import sm.rrn.graficos.AntialiasingRRN;
import sm.rrn.graficos.RellenoDegradadoRRN;
import sm.rrn.graficos.RellenoRRN;
import sm.rrn.graficos.ShapeRRN;
import sm.rrn.graficos.TransparenciaRRN;
import sm.rrn.graficos.TrazoRRN;
import sm.rrn.iu.ModoPintado;
import sm.rrn.iu.TipoRelleno;
import sm.rrn.iu.TipoTrazo;

/**
 * Clase manejadora de Lienzo que implementa el listener que creamos en la biblioteca SM.RRN.Biblioteca
 * @author Raúl Ruano Narváez
 */
public class ManejadorLienzo implements LienzoListener{
    private VentanaPrincipal ventanaPrincipal;

    /**
    * Constructor por parámetros de ManejadorLienzo el cual usa como parámetro la VentanaPrincipal padre
     * @param ventanaPrincipal VentanaPrincipal a la que referencia
    */
    public ManejadorLienzo(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
    }
   
    /**
    * Método que añade un nuevo elemento en la lista de figuras cuando una figura es añadida
    */
    @Override
    public void shapeAdded(LienzoEvent evt) {
        ((DefaultComboBoxModel) ventanaPrincipal.listaDesplegableFiguras.getModel()).addElement(evt.getShape());
    }

    /**
    * Método que realiza una actualización de las variables de lienzo cuando se selecciona una figura
    */
    @Override
    public void updateLienzo(LienzoEvent evt) {
        if(evt.getLienzo().getFigura_seleccionada()!=null){
            TrazoRRN trazo = (TrazoRRN) evt.getLienzo().getFigura_seleccionada().getAtributoTrazo();
            RellenoRRN relleno = (RellenoRRN) evt.getLienzo().getFigura_seleccionada().getAtributoRelleno();
            RellenoDegradadoRRN rellenoDegradado = (RellenoDegradadoRRN) evt.getLienzo().getFigura_seleccionada().getAtributoRellenoDegradado();
            TransparenciaRRN transparencia = (TransparenciaRRN) evt.getLienzo().getFigura_seleccionada().getAtributoTransparencia();
            AntialiasingRRN antialiasing = (AntialiasingRRN) evt.getLienzo().getFigura_seleccionada().getAtributoAntialising();

            //Inicialmente
            ventanaPrincipal.checkRelleno.setSelected(false);
            ventanaPrincipal.checkAlisar.setSelected(false);
            ventanaPrincipal.checkTransparencia.setSelected(false);

            if(trazo!=null){
                ventanaPrincipal.listaDesplegableColoresContorno.setSelectedItem((Color)trazo.getColor());
                ventanaPrincipal.spinnerGrosor.setValue(trazo.getTamañoTrazo());

                if(trazo.getTipoTrazo()==TipoTrazo.LINEA_CONTINUA){
                    ventanaPrincipal.listaDesplegableTipoTrazo.setSelectedIndex(0);
                }else if(trazo.getTipoTrazo()==TipoTrazo.LINEA_DISCONTINUA){
                    ventanaPrincipal.listaDesplegableTipoTrazo.setSelectedIndex(1);
                }   
            }

            if(relleno!=null){
                ventanaPrincipal.checkRelleno.setSelected(true);
                ventanaPrincipal.listaDesplegableColoresRelleno.setSelectedItem((Color)relleno.getColor());
                ventanaPrincipal.listaDesplegableTipoRelleno.setSelectedIndex(0);
            }

            if(rellenoDegradado!=null){
                ventanaPrincipal.checkRelleno.setSelected(true);
                ventanaPrincipal.listaDesplegableColoresContorno.setSelectedItem((Color)rellenoDegradado.getColorInicio());
                ventanaPrincipal.listaDesplegableColoresRelleno.setSelectedItem((Color)rellenoDegradado.getColorFinal());

                if(rellenoDegradado.getTipoRelleno()==TipoRelleno.DEGRADADO_HORIZONTAL){
                    ventanaPrincipal.listaDesplegableTipoRelleno.setSelectedIndex(1);
                }else if(rellenoDegradado.getTipoRelleno()==TipoRelleno.DEGRADADO_VERTICAL){
                    ventanaPrincipal.listaDesplegableTipoRelleno.setSelectedIndex(2);
                }
            }

            if(transparencia!=null){
                ventanaPrincipal.checkTransparencia.setSelected(transparencia.isActivado());
                ventanaPrincipal.spinnerNivelTransparencia.setValue(transparencia.getNivelTransparencia());
            }

            if(antialiasing!=null){
                ventanaPrincipal.checkAlisar.setSelected(antialiasing.isActivado());
            }
            
            ventanaPrincipal.listaDesplegableFiguras.setSelectedItem(evt.getLienzo().getFigura_seleccionada());
            
            ventanaPrincipal.coordenadaX.setEditable(true);
            ventanaPrincipal.coordenadaY.setEditable(true);
            ventanaPrincipal.coordenadaX.setValue(evt.getLienzo().getFigura_seleccionada().getBounds().getX());
            ventanaPrincipal.coordenadaY.setValue(evt.getLienzo().getFigura_seleccionada().getBounds().getY());
            
        }    
    }

    /**
    * Método que realiza la actualización de las coordenadas cuando una figura es seleccionada
    */
    @Override
    public void updateCoord(LienzoEvent evt) {
        if(evt.getLienzo().getFigura_seleccionada()!=null){
            ventanaPrincipal.coordenadaX.setValue(evt.getLienzo().getFigura_seleccionada().getBounds().getX());
            ventanaPrincipal.coordenadaY.setValue(evt.getLienzo().getFigura_seleccionada().getBounds().getY());        
        }
    }

    /**
    * Método que pone a 0 las coordenadas cuando no hay ninguna figura seleccionada
    */
    @Override
    public void updateCoordEditable(LienzoEvent evt) {
        ventanaPrincipal.coordenadaX.setText("0");
        ventanaPrincipal.coordenadaY.setText("0");
        ventanaPrincipal.coordenadaX.setEditable(false);
        ventanaPrincipal.coordenadaY.setEditable(false);
    }
        
}
