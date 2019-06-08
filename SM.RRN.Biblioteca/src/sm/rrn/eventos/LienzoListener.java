/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.eventos;

import java.util.EventListener;

/**
 * Clase interface Listener de Lienzo2D
 * @author Raúl Ruano Narváez
 */
public interface LienzoListener extends EventListener{

    /**
     * Método que añade un ShapeRRN a la lista de figuras que tiene el Lienzo2D
     * @param evt Parámetro de tipo LienzoEvento
     */
    public void shapeAdded(LienzoEvent evt);

    /**
     * Evento que realiza una actualización de la VentanaPrincipal
     * @param evt Parámetro de tipo LienzoEvento
     */
    public void updateLienzo(LienzoEvent evt);

    /**
     * Evento que realiza una actualización de las coordenadas cuando una figura se selecciona para editar
     * @param evt Parámetro de tipo LienzoEvento
     */
    public void updateCoord(LienzoEvent evt);

    /**
     * Evento que pone a 0 los dos campos de coordenadas cuando no hay ninguna figura seleccionada
     * @param evt Parámetro de tipo LienzoEvento
     */
    public void updateCoordEditable(LienzoEvent evt);
}
