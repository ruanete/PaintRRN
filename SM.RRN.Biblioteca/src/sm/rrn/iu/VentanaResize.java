/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.iu;

/**
 * Clase que define una ventana para poder realizar resize o escalado de un lienzo
 * @author Raúl Ruano Narváez
 */
public class VentanaResize extends javax.swing.JDialog {
    int opcion;
    boolean edicion;
    
    /**
     * Constructor por parámetros de VentanaResize
     * @param parent Frame referencia al padre, a la ventana que la abrió 
     * @param AnchoInicial Valor ancho del lienzo actual
     * @param AlturaInicial Valor alto del lienzo actual
     */
    public VentanaResize(java.awt.Frame parent, int AnchoInicial, int AlturaInicial) {
        super(parent, true);
        initComponents();
        this.setTitle("Cambiar tamaño de la imagen");
        
        this.anchoTextField.setValue(Integer.valueOf(AnchoInicial));
        this.altoTextField.setValue(Integer.valueOf(AlturaInicial));
        
        modoEdicion(true);
    }

    /**
     * Método que desactiva botones cuando se esta modificando los valores
     * @param edicion True o false si la edición esta activada o no
     */
    
    public void modoEdicion(boolean edicion){
        this.edicion = edicion;
        this.botonRedimensionar.setVisible(edicion);
        this.botonEscalar.setVisible(edicion);
        if (edicion){
            this.botonCancelar.setText("Cancelar");
        }else{
            this.botonCancelar.setText("Aceptar");
        }
  }
    
    private void opcionElegida(int opcion){
        this.opcion = opcion;
        setVisible(false);
        dispose();
    }
    
    /**
     * Método que muestra la ventana para redimensionar o escalar el lienzo
     * @return Devuelve el valor de la opción elegida (0: Cancelar, 1: Redimensionar y 2: Escalar)
     */
    public int mostrarVentanaResize(){
        setVisible(true);
    
        return this.opcion;
    }
    
    /**
     * Método que devuelve el ancho de la imagen seteado en esta ventana
     * @return Entero con el ancho de la imagen
     */
    public int getAnchoImagen(){
        Integer i = (Integer)this.anchoTextField.getValue();
        return i.intValue();
    }
  
    /**
     * Método que devuelve la altura de la imagen seteado en esta ventana
     * @return Entero con el alto de la imagen
     */
    public int getAltoImagen(){
        Integer i = Integer.valueOf(((Integer)this.altoTextField.getValue()).intValue());
        return i.intValue();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        etiquetaTitulo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        imagen = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        etiquetaAncho = new javax.swing.JLabel();
        anchoTextField = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        etiquetaAlto = new javax.swing.JLabel();
        altoTextField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        botonEscalar = new javax.swing.JButton();
        botonRedimensionar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        etiquetaTitulo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        etiquetaTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaTitulo.setText("Tamaño de la Imagen");
        jPanel1.add(etiquetaTitulo, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        imagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sm/rrn/iu/resize.png"))); // NOI18N
        jPanel3.add(imagen);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        jPanel5.setLayout(new java.awt.GridBagLayout());

        etiquetaAncho.setText("Ancho");
        jPanel8.add(etiquetaAncho);

        anchoTextField.setText("0");
        anchoTextField.setPreferredSize(new java.awt.Dimension(50, 30));
        anchoTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                anchoTextFieldKeyTyped(evt);
            }
        });
        jPanel8.add(anchoTextField);

        jPanel5.add(jPanel8, new java.awt.GridBagConstraints());

        jPanel4.add(jPanel5);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        etiquetaAlto.setText("Alto");
        jPanel7.add(etiquetaAlto);

        altoTextField.setText("0");
        altoTextField.setPreferredSize(new java.awt.Dimension(50, 30));
        altoTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                altoTextFieldKeyTyped(evt);
            }
        });
        jPanel7.add(altoTextField);

        jPanel6.add(jPanel7, new java.awt.GridBagConstraints());

        jPanel4.add(jPanel6);

        jPanel3.add(jPanel4);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        botonEscalar.setText("Escalar");
        botonEscalar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEscalarActionPerformed(evt);
            }
        });
        jPanel2.add(botonEscalar);

        botonRedimensionar.setText("Redimensionar");
        botonRedimensionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRedimensionarActionPerformed(evt);
            }
        });
        jPanel2.add(botonRedimensionar);

        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });
        jPanel2.add(botonCancelar);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        // TODO add your handling code here:
        opcionElegida(0);
    }//GEN-LAST:event_botonCancelarActionPerformed

    private void botonRedimensionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRedimensionarActionPerformed
        // TODO add your handling code here:
        opcionElegida(1);
    }//GEN-LAST:event_botonRedimensionarActionPerformed

    private void anchoTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_anchoTextFieldKeyTyped
        // TODO add your handling code here:
        modoEdicion(true);
    }//GEN-LAST:event_anchoTextFieldKeyTyped

    private void altoTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_altoTextFieldKeyTyped
        // TODO add your handling code here:
        modoEdicion(true);
    }//GEN-LAST:event_altoTextFieldKeyTyped

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        opcionElegida(0);
    }//GEN-LAST:event_formWindowClosed

    private void botonEscalarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEscalarActionPerformed
        // TODO add your handling code here:
        opcionElegida(2);
    }//GEN-LAST:event_botonEscalarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField altoTextField;
    private javax.swing.JFormattedTextField anchoTextField;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonEscalar;
    private javax.swing.JButton botonRedimensionar;
    private javax.swing.JLabel etiquetaAlto;
    private javax.swing.JLabel etiquetaAncho;
    private javax.swing.JLabel etiquetaTitulo;
    private javax.swing.JLabel imagen;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    // End of variables declaration//GEN-END:variables
}
