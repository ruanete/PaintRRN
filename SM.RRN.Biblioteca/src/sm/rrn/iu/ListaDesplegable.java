/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.iu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * Clase para renderizar con un boton y su respectivo color en un ComboBox
 * @author Raúl Ruano Narváez
 */
public class ListaDesplegable extends JPanel implements ListCellRenderer<Color>{
    private JButton boton = new JButton();
    
    /**
     * Constructor por defecto
     */
    public ListaDesplegable(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(30, 30));
        
        boton.setText("");
        boton.setPreferredSize(new Dimension(30,30));
        add(boton, BorderLayout.WEST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Color> list, Color value, int index, boolean isSelected, boolean cellHasFocus) {
        boton.setBackground(value);
        return this;
    }
}
