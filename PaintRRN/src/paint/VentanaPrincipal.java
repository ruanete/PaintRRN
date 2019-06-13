/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import sm.rrn.iu.VentanaResize;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.UnsupportedDataTypeException;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import sm.image.BlendOp;
import sm.image.EqualizationOp;
import sm.image.KernelProducer;
import sm.image.LookupTableProducer;
import sm.image.SubtractionOp;
import sm.image.TintOp;
import sm.rrn.graficos.ShapeRRN;
import sm.rrn.image.GoldFilterRRN;
import sm.rrn.image.ImageFunctionRRN;
import sm.rrn.image.KernelProducerRRN;
import sm.rrn.image.MirrorFilterRRN;
import sm.rrn.image.SepiaOPRRN;
import sm.rrn.image.SobelOPRRN;
import sm.rrn.image.UmbralizacionOPRRN;
import sm.rrn.iu.FormatValidator;
import sm.rrn.iu.Lienzo2D;
import sm.rrn.iu.LienzoImagen2D;
import sm.rrn.iu.ListaDesplegable;
import sm.rrn.iu.ModoPintado;
import sm.rrn.iu.TipoRelleno;
import sm.rrn.iu.TipoTrazo;
import sm.sound.SMClipPlayer;
import sm.sound.SMPlayer;
import sm.sound.SMRecorder;
import sm.sound.SMSoundRecorder;

/**
 * Clase que define una VentanaPrincipal que es la ventana de toda la aplicación
 * @author Raul
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    private Color colores[] = {Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.YELLOW, Color.GREEN};
    private final String[] fuentesSistema = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    private int anchoImagen, altoImagen;
    private BufferedImage imgSource;
    SMPlayer player;
    SMRecorder recorder;
    int minutes=0, seconds=0, hours=0;
    Thread hiloTimer;
    Boolean finGrabacion=false;
    

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        this.setTitle("Paint Básico - Raúl Ruano Narváez");
        listaFuentes.setVisible(false);
        botonTexto.setVisible(false);
        botonPause.setVisible(false);
        anchoImagen=altoImagen=300;
        player = null;
        recorder = null;
        coordenadaX.setValue(0);
        coordenadaY.setValue(0);
        
        inicializarListaColores();
    }
  
    private void inicializarListaColores(){
        listaDesplegableColoresContorno.addItem(Color.BLACK);
        listaDesplegableColoresContorno.addItem(Color.RED);
        listaDesplegableColoresContorno.addItem(Color.BLUE);
        listaDesplegableColoresContorno.addItem(Color.WHITE);
        listaDesplegableColoresContorno.addItem(Color.YELLOW);
        listaDesplegableColoresContorno.addItem(Color.GREEN);
        listaDesplegableColoresContorno.setRenderer(new ListaDesplegable());
        
        listaDesplegableColoresRelleno.addItem(Color.BLACK);
        listaDesplegableColoresRelleno.addItem(Color.RED);
        listaDesplegableColoresRelleno.addItem(Color.BLUE);
        listaDesplegableColoresRelleno.addItem(Color.WHITE);
        listaDesplegableColoresRelleno.addItem(Color.YELLOW);
        listaDesplegableColoresRelleno.addItem(Color.GREEN);
        listaDesplegableColoresRelleno.setRenderer(new ListaDesplegable());   
    }
    
    /**
     * Método que realiza una cuenta en segundos del tiempo que se esta grabando, ejecutandose en una hebra.
     */
    public void timeTick(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        seconds = seconds + 1; 

        if(seconds == 60){
            minutes++;
            seconds = 0; 
            if(minutes == 60){
                hours++;
                minutes = 0; 
            }
        }
                        
        if(String.valueOf(hours).length()==1 && String.valueOf(minutes).length()==1 && String.valueOf(seconds).length()==1){
            tiempoGrabado.setText("0" + hours + ":" + "0" + minutes + ":" + "0" + seconds);
        }else if(String.valueOf(hours).length()==1 && String.valueOf(minutes).length()==1 && String.valueOf(seconds).length()!=1){
            tiempoGrabado.setText("0" + hours + ":0" + minutes + ":" + seconds);
        }else{
            tiempoGrabado.setText(hours + ":" + minutes + ":" + seconds);
        }    
    }
     
    /**
     * Método para setear la coordenada en el lienzo actual
     * @param p Punto con la coordenada
     */
    public static void setCoordenadas(Point p){
        coordenadasLienzo.setText("(" + (int)p.getX() + "," + (int)p.getY() + ")");
    }
    
    /**
     * Método que realiza una actualización de los botones de la ventana principal con los valores respectivos al lienzo de la ventana interna actual
     * @param vi VentanaInterna con la ventana interna actual
     */
    public void updateButtons(VentanaInterna vi){
        ModoPintado modo = null;
        Color colorStroke = null;
        Color colorRelleno = null;
        int tamañoTrazo=0;
        TipoTrazo tipoTrazo = null;
        boolean relleno=false;
        TipoRelleno tipoRelleno = null;
        boolean transparencia=false;
        float nivelTransparencia=0;
        boolean alisar=false;
       
        if(botonPunto.isSelected()){
            modo = ModoPintado.Punto;
        }else if(botonLinea.isSelected()){
            modo = ModoPintado.Linea;
        }else if(botonRectangulo.isSelected()){
            modo = ModoPintado.Rectangulo;
        }else if(botonElipse.isSelected()){
            modo = ModoPintado.Elipse;
        }else if(botonEditar.isSelected()){
            modo = ModoPintado.Edicion;
        }else if(botonArco.isSelected()){
            modo = ModoPintado.Arco;
        }else if(botonRoundedRectangle.isSelected()){
            modo = ModoPintado.RoundedRectangle;
        }else if(botonQuadCurve.isSelected()){
            modo = ModoPintado.QuadCurve;
        }else if(botonCubicCurve.isSelected()){
            modo = ModoPintado.CubicCurve;
        }else if(botonTrazoLibre.isSelected()){
            modo = ModoPintado.TrazoLibre;
        }else if(botonPoligono.isSelected()){
            modo = ModoPintado.Poligono;
        }else if(botonFormaPersonalizada.isSelected()){
            modo = ModoPintado.FormaPersonalizada;
        }else if(botonTexto.isSelected()){
            modo = ModoPintado.Texto;
        }
         
        colorStroke = (Color)listaDesplegableColoresContorno.getSelectedItem();
        colorRelleno = (Color)listaDesplegableColoresRelleno.getSelectedItem();
        tamañoTrazo = (int) spinnerGrosor.getValue();
        
        switch(listaDesplegableTipoTrazo.getSelectedIndex()){
            case 0:
                tipoTrazo = TipoTrazo.LINEA_CONTINUA;
                break;
            case 1:
                tipoTrazo = TipoTrazo.LINEA_DISCONTINUA;
                break;
        }
        
        relleno = checkRelleno.isSelected();
        
        switch(listaDesplegableTipoRelleno.getSelectedIndex()){
            case 0:
                tipoRelleno = TipoRelleno.COLOR_SOLIDO;
                break;
            case 1:
                tipoRelleno = TipoRelleno.DEGRADADO_HORIZONTAL;
                break;
            case 2:
                tipoRelleno = TipoRelleno.DEGRADADO_VERTICAL;
                break;
        }
        
        transparencia = checkTransparencia.isSelected();
        nivelTransparencia = (float) spinnerNivelTransparencia.getValue();
        
        alisar = checkAlisar.isSelected();
                
        vi.getLienzo().setModoPintado(modo);
        vi.getLienzo().setColorStroke(colorStroke);
        vi.getLienzo().setColorRelleno(colorRelleno);  
        vi.getLienzo().setTamañoTrazo(tamañoTrazo);
        vi.getLienzo().setTrazo(tipoTrazo);
        vi.getLienzo().setRelleno(relleno);
        vi.getLienzo().setTipoRelleno(tipoRelleno);
        vi.getLienzo().setTransparencia(transparencia);
        vi.getLienzo().setNivelTransparencia(nivelTransparencia);
        vi.getLienzo().setAlisar(alisar);
        
        listaDesplegableFiguras.removeAllItems();
        ((DefaultComboBoxModel) listaDesplegableFiguras.getModel()).addElement(new String("Figura no seleccionada"));
        for(int i=0;i<vi.getLienzo().getvShape().size();i++){
            ((DefaultComboBoxModel) listaDesplegableFiguras.getModel()).addElement(vi.getLienzo().getvShape().get(i));
        }        
    }
    
    /**
     * Método que actualiza los atributos de una figura cuando esta siendo editada
     * @param vi VentanInterna actual
     */
    public void actualizarFiguraSeleccionada(VentanaInterna vi){
        if(vi.getLienzo().getModoPintado()==ModoPintado.Edicion && vi.getLienzo().getFigura_seleccionada()!=null){
            vi.getLienzo().updateAtributes(vi.getLienzo().getFigura_seleccionada());
        }
    }
    
    /**
     * Metodo que aplica contraste a una imagen
     * @param opcion Opcion de contraste 0: Normal, 1: Iluminación y 2: Oscurecimiento
     */
    public void contraste(int opcion){
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {            
            LookupTable lt;
            lt=LookupTableProducer.createLookupTable(opcion);
            LookupOp lop = new LookupOp(lt, null);
            
            lop.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
            vi.repaint();
            escritorio.repaint();
        }
    }
    
    /**
     * Método que aplica una función de las creadas en la biblioteca de tipo ImageFunctionRRN
     * @param opcion Opcion de la función elegida
     */
    public void aplicarFuncion(int opcion){
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {            
            LookupTable lt = ImageFunctionRRN.createFunction(opcion);
            
            LookupOp lop = new LookupOp(lt, null);
            
            lop.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
            vi.repaint();
            escritorio.repaint();
        }
    }
    
    /**
     * Método que aplica rotación a una imagen
     * @param grados Entero con los grados de la rotación
     */
    public void rotacion(int grados){
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {  
            double rotationRequired = Math.toRadians (grados);
            double locationX = vi.getLienzo().getImage(false).getWidth() / 2;
            double locationY = vi.getLienzo().getImage(false).getHeight() / 2;
            
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);         
            
            BufferedImage newImage = op.filter(vi.getLienzo().getImage(false), null);
            
            vi.getLienzo().setImage(newImage);
            vi.repaint();
        }
    }
    
    /**
     * Método que escala a una imagen
     * @param escala Double con el valor de la escala
     */
    public void escalar(double escala){
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {  
            AffineTransform tx = AffineTransform.getScaleInstance(escala, escala);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);         
            
            BufferedImage newImage = op.filter(vi.getLienzo().getImage(false), null);
            
            vi.getLienzo().setImage(newImage);
            vi.getLienzo().repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoBotonesHerramientas = new javax.swing.ButtonGroup();
        grupoBotonesColores = new javax.swing.ButtonGroup();
        barraHerramientas = new javax.swing.JToolBar();
        botonNuevo = new javax.swing.JButton();
        botonAbrir = new javax.swing.JButton();
        botonGuardar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        botonPunto = new javax.swing.JToggleButton();
        botonLinea = new javax.swing.JToggleButton();
        botonRectangulo = new javax.swing.JToggleButton();
        botonElipse = new javax.swing.JToggleButton();
        botonRoundedRectangle = new javax.swing.JToggleButton();
        botonArco = new javax.swing.JToggleButton();
        botonQuadCurve = new javax.swing.JToggleButton();
        botonCubicCurve = new javax.swing.JToggleButton();
        botonPoligono = new javax.swing.JToggleButton();
        botonTrazoLibre = new javax.swing.JToggleButton();
        botonFormaPersonalizada = new javax.swing.JToggleButton();
        botonTexto = new javax.swing.JToggleButton();
        botonEditar = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        listaDesplegableColoresContorno = new javax.swing.JComboBox<>();
        listaDesplegableColoresRelleno = new javax.swing.JComboBox<>();
        botonSelectorColor = new javax.swing.JButton();
        listaFuentes = new javax.swing.JComboBox(fuentesSistema);
        jSeparator3 = new javax.swing.JToolBar.Separator();
        spinnerGrosor = new javax.swing.JSpinner();
        listaDesplegableTipoTrazo = new javax.swing.JComboBox<>();
        checkRelleno = new javax.swing.JToggleButton();
        listaDesplegableTipoRelleno = new javax.swing.JComboBox<>();
        checkTransparencia = new javax.swing.JToggleButton();
        spinnerNivelTransparencia = new javax.swing.JSpinner();
        checkAlisar = new javax.swing.JToggleButton();
        listaDesplegableFiguras = new javax.swing.JComboBox<>();
        listaDesplegableFiguras.setModel(new DefaultComboBoxModel());
 ((DefaultComboBoxModel) listaDesplegableFiguras.getModel()).addElement("Figura no seleccionada");
        jLabel1 = new javax.swing.JLabel();
        coordenadaX = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        coordenadaY = new javax.swing.JFormattedTextField();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        botonPlay = new javax.swing.JButton();
        botonPause = new javax.swing.JButton();
        listaDesplegableAudio = new javax.swing.JComboBox<>();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        botonGrabar = new javax.swing.JToggleButton();
        tiempoGrabado = new javax.swing.JLabel();
        escritorio = new javax.swing.JDesktopPane();
        panelInferior = new javax.swing.JPanel();
        barraHerramientasInferior = new javax.swing.JToolBar();
        sliderBrillo = new javax.swing.JSlider();
        listaDesplegableFiltro = new javax.swing.JComboBox<>();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        botonContrasteNormal = new javax.swing.JButton();
        botonContrasteIluminacion = new javax.swing.JButton();
        botonContrasteOscurecimiento = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        botonFuncionSeno = new javax.swing.JButton();
        botonFuncionNegativo = new javax.swing.JButton();
        botonSepia = new javax.swing.JButton();
        botonTintado = new javax.swing.JButton();
        botonEcualizacion = new javax.swing.JButton();
        botonSobel = new javax.swing.JButton();
        botonFuncionRRN = new javax.swing.JButton();
        botonGoldFilter = new javax.swing.JButton();
        botonMirrorFilter = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        botonMuestraBandas = new javax.swing.JButton();
        listaDesplegableBandas = new javax.swing.JComboBox<>();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        sliderRotacion = new javax.swing.JSlider();
        botonRotacion90 = new javax.swing.JButton();
        botonRotacion180 = new javax.swing.JButton();
        botonRotacion270 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        botonAumentaEscala = new javax.swing.JButton();
        botonDisminuyeEscala = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        sliderUmbralizacion = new javax.swing.JSlider();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        botonSuma = new javax.swing.JButton();
        botonResta = new javax.swing.JButton();
        panelFormaActiva = new javax.swing.JPanel();
        formaActiva = new javax.swing.JLabel();
        coordenadasLienzo = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        submenuNuevo = new javax.swing.JMenuItem();
        submenuAbrir = new javax.swing.JMenuItem();
        submenuGuardar = new javax.swing.JMenuItem();
        menuVer = new javax.swing.JMenu();
        checkBarraEstado = new javax.swing.JCheckBoxMenuItem();
        checkBarraFormas = new javax.swing.JCheckBoxMenuItem();
        checkBarraImagen = new javax.swing.JCheckBoxMenuItem();
        menuImagen = new javax.swing.JMenu();
        submenuResize = new javax.swing.JMenuItem();
        submenuDuplicar = new javax.swing.JMenuItem();
        menuAyuda = new javax.swing.JMenu();
        submenuAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1650, 900));

        barraHerramientas.setRollover(true);
        barraHerramientas.setMargin(new java.awt.Insets(5, 0, 5, 0));

        botonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/nuevo.png"))); // NOI18N
        botonNuevo.setToolTipText("Nuevo");
        botonNuevo.setFocusable(false);
        botonNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNuevoActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonNuevo);

        botonAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/abrir.png"))); // NOI18N
        botonAbrir.setToolTipText("Abrir");
        botonAbrir.setFocusable(false);
        botonAbrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonAbrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAbrirActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonAbrir);

        botonGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/guardar.png"))); // NOI18N
        botonGuardar.setToolTipText("Guardar");
        botonGuardar.setFocusable(false);
        botonGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonGuardar);
        barraHerramientas.add(jSeparator2);

        grupoBotonesHerramientas.add(botonPunto);
        botonPunto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/punto.png"))); // NOI18N
        botonPunto.setSelected(true);
        botonPunto.setToolTipText("Punto");
        botonPunto.setFocusable(false);
        botonPunto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonPunto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonPunto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPuntoActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonPunto);

        grupoBotonesHerramientas.add(botonLinea);
        botonLinea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/linea.png"))); // NOI18N
        botonLinea.setToolTipText("Linea");
        botonLinea.setFocusable(false);
        botonLinea.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonLinea.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonLinea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonLineaActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonLinea);

        grupoBotonesHerramientas.add(botonRectangulo);
        botonRectangulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rectangulo.png"))); // NOI18N
        botonRectangulo.setToolTipText("Rectangulo");
        botonRectangulo.setFocusable(false);
        botonRectangulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRectangulo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRectangulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRectanguloActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonRectangulo);

        grupoBotonesHerramientas.add(botonElipse);
        botonElipse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/elipse.png"))); // NOI18N
        botonElipse.setToolTipText("Elipse");
        botonElipse.setFocusable(false);
        botonElipse.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonElipse.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonElipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonElipseActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonElipse);

        grupoBotonesHerramientas.add(botonRoundedRectangle);
        botonRoundedRectangle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/roundedrectangle.png"))); // NOI18N
        botonRoundedRectangle.setToolTipText("Rectangulo bordes redondeados");
        botonRoundedRectangle.setFocusable(false);
        botonRoundedRectangle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRoundedRectangle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRoundedRectangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRoundedRectangleActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonRoundedRectangle);

        grupoBotonesHerramientas.add(botonArco);
        botonArco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/arco.png"))); // NOI18N
        botonArco.setToolTipText("Arco");
        botonArco.setFocusable(false);
        botonArco.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonArco.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonArco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonArcoActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonArco);

        grupoBotonesHerramientas.add(botonQuadCurve);
        botonQuadCurve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/quadcurve.png"))); // NOI18N
        botonQuadCurve.setToolTipText("Curva un punto de control");
        botonQuadCurve.setFocusable(false);
        botonQuadCurve.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonQuadCurve.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonQuadCurve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonQuadCurveActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonQuadCurve);

        grupoBotonesHerramientas.add(botonCubicCurve);
        botonCubicCurve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/cubiccurve.png"))); // NOI18N
        botonCubicCurve.setToolTipText("Curva dos punto de control");
        botonCubicCurve.setFocusable(false);
        botonCubicCurve.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonCubicCurve.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonCubicCurve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCubicCurveActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonCubicCurve);

        grupoBotonesHerramientas.add(botonPoligono);
        botonPoligono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/poligono.png"))); // NOI18N
        botonPoligono.setToolTipText("Poligono");
        botonPoligono.setFocusable(false);
        botonPoligono.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonPoligono.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonPoligono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPoligonoActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonPoligono);

        grupoBotonesHerramientas.add(botonTrazoLibre);
        botonTrazoLibre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/trazolibre.png"))); // NOI18N
        botonTrazoLibre.setToolTipText("Trazo libre");
        botonTrazoLibre.setFocusable(false);
        botonTrazoLibre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonTrazoLibre.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonTrazoLibre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTrazoLibreActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonTrazoLibre);

        grupoBotonesHerramientas.add(botonFormaPersonalizada);
        botonFormaPersonalizada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/estrella.png"))); // NOI18N
        botonFormaPersonalizada.setToolTipText("Estrella");
        botonFormaPersonalizada.setFocusable(false);
        botonFormaPersonalizada.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonFormaPersonalizada.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonFormaPersonalizada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonFormaPersonalizadaActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonFormaPersonalizada);

        grupoBotonesHerramientas.add(botonTexto);
        botonTexto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/texto.png"))); // NOI18N
        botonTexto.setToolTipText("Texto");
        botonTexto.setFocusable(false);
        botonTexto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonTexto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTextoActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonTexto);

        grupoBotonesHerramientas.add(botonEditar);
        botonEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/seleccion.png"))); // NOI18N
        botonEditar.setToolTipText("Editar");
        botonEditar.setFocusable(false);
        botonEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEditarActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonEditar);
        barraHerramientas.add(jSeparator1);

        listaDesplegableColoresContorno.setToolTipText("Selector de color borde");
        listaDesplegableColoresContorno.setName(""); // NOI18N
        listaDesplegableColoresContorno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaDesplegableColoresContornoItemStateChanged(evt);
            }
        });
        barraHerramientas.add(listaDesplegableColoresContorno);

        listaDesplegableColoresRelleno.setToolTipText("Selector de color relleno");
        listaDesplegableColoresRelleno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaDesplegableColoresRellenoItemStateChanged(evt);
            }
        });
        barraHerramientas.add(listaDesplegableColoresRelleno);

        botonSelectorColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/selector_colores.png"))); // NOI18N
        botonSelectorColor.setFocusable(false);
        botonSelectorColor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonSelectorColor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonSelectorColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSelectorColorActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonSelectorColor);

        listaFuentes.setToolTipText("Lista de fuentes");
        barraHerramientas.add(listaFuentes);
        barraHerramientas.add(jSeparator3);

        spinnerGrosor.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        spinnerGrosor.setToolTipText("Grosor");
        spinnerGrosor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerGrosorStateChanged(evt);
            }
        });
        barraHerramientas.add(spinnerGrosor);

        listaDesplegableTipoTrazo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Continua", "Discontinua" }));
        listaDesplegableTipoTrazo.setToolTipText("Tipo de trazo");
        listaDesplegableTipoTrazo.setMinimumSize(new java.awt.Dimension(30, 20));
        listaDesplegableTipoTrazo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaDesplegableTipoTrazoItemStateChanged(evt);
            }
        });
        barraHerramientas.add(listaDesplegableTipoTrazo);

        checkRelleno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rellenar.png"))); // NOI18N
        checkRelleno.setToolTipText("Activar/Desactivar relleno");
        checkRelleno.setFocusable(false);
        checkRelleno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkRelleno.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        checkRelleno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRellenoActionPerformed(evt);
            }
        });
        barraHerramientas.add(checkRelleno);

        listaDesplegableTipoRelleno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Color solido", "Degradado horizontal", "Degradado vertical" }));
        listaDesplegableTipoRelleno.setToolTipText("Tipo de relleno");
        listaDesplegableTipoRelleno.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        listaDesplegableTipoRelleno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaDesplegableTipoRellenoItemStateChanged(evt);
            }
        });
        barraHerramientas.add(listaDesplegableTipoRelleno);

        checkTransparencia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/transparencia.png"))); // NOI18N
        checkTransparencia.setToolTipText("Transparencia");
        checkTransparencia.setFocusable(false);
        checkTransparencia.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkTransparencia.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        checkTransparencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkTransparenciaActionPerformed(evt);
            }
        });
        barraHerramientas.add(checkTransparencia);

        spinnerNivelTransparencia.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.05f)));
        spinnerNivelTransparencia.setToolTipText("Nivel de transparencia");
        spinnerNivelTransparencia.setPreferredSize(new java.awt.Dimension(60, 30));
        spinnerNivelTransparencia.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerNivelTransparenciaStateChanged(evt);
            }
        });
        barraHerramientas.add(spinnerNivelTransparencia);

        checkAlisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/alisar.png"))); // NOI18N
        checkAlisar.setToolTipText("Post-procesado");
        checkAlisar.setFocusable(false);
        checkAlisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        checkAlisar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        checkAlisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAlisarActionPerformed(evt);
            }
        });
        barraHerramientas.add(checkAlisar);

        listaDesplegableFiguras.setPreferredSize(new java.awt.Dimension(170, 30));
        listaDesplegableFiguras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaDesplegableFigurasActionPerformed(evt);
            }
        });
        barraHerramientas.add(listaDesplegableFiguras);

        jLabel1.setText("X:");
        barraHerramientas.add(jLabel1);

        coordenadaX.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        coordenadaX.setPreferredSize(new java.awt.Dimension(60, 30));
        coordenadaX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coordenadaXActionPerformed(evt);
            }
        });
        barraHerramientas.add(coordenadaX);

        jLabel2.setText("Y:");
        barraHerramientas.add(jLabel2);

        coordenadaY.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        coordenadaY.setPreferredSize(new java.awt.Dimension(60, 30));
        coordenadaY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coordenadaYActionPerformed(evt);
            }
        });
        barraHerramientas.add(coordenadaY);
        barraHerramientas.add(jSeparator11);

        botonPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/play24x24.png"))); // NOI18N
        botonPlay.setToolTipText("Play/Pausa sonido");
        botonPlay.setFocusable(false);
        botonPlay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonPlay.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPlayActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonPlay);

        botonPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/pausa24x24.png"))); // NOI18N
        botonPause.setToolTipText("Stop sonido");
        botonPause.setFocusable(false);
        botonPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPauseActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonPause);

        listaDesplegableAudio.setToolTipText("Lista de sonidos cargados");
        barraHerramientas.add(listaDesplegableAudio);
        barraHerramientas.add(jSeparator12);

        botonGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/record24x24.png"))); // NOI18N
        botonGrabar.setToolTipText("Boton grabar");
        botonGrabar.setFocusable(false);
        botonGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGrabarActionPerformed(evt);
            }
        });
        barraHerramientas.add(botonGrabar);

        tiempoGrabado.setText("00:00:00");
        barraHerramientas.add(tiempoGrabado);

        getContentPane().add(barraHerramientas, java.awt.BorderLayout.PAGE_START);

        escritorio.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                escritorioMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout escritorioLayout = new javax.swing.GroupLayout(escritorio);
        escritorio.setLayout(escritorioLayout);
        escritorioLayout.setHorizontalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1179, Short.MAX_VALUE)
        );
        escritorioLayout.setVerticalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );

        getContentPane().add(escritorio, java.awt.BorderLayout.CENTER);

        panelInferior.setLayout(new java.awt.BorderLayout());

        barraHerramientasInferior.setRollover(true);
        barraHerramientasInferior.setMargin(new java.awt.Insets(5, 0, 5, 0));

        sliderBrillo.setMaximum(255);
        sliderBrillo.setMinimum(-255);
        sliderBrillo.setToolTipText("Control de brillo");
        sliderBrillo.setValue(0);
        sliderBrillo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderBrilloStateChanged(evt);
            }
        });
        sliderBrillo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sliderBrilloFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sliderBrilloFocusLost(evt);
            }
        });
        barraHerramientasInferior.add(sliderBrillo);

        listaDesplegableFiltro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ninguno", "Media3x3", "Media5x5", "Media 7x7", "Binomial", "Enfoque", "Relieve", "Laplaciano" }));
        listaDesplegableFiltro.setToolTipText("Filtros");
        listaDesplegableFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaDesplegableFiltroActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(listaDesplegableFiltro);
        barraHerramientasInferior.add(jSeparator4);

        botonContrasteNormal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/contraste.png"))); // NOI18N
        botonContrasteNormal.setToolTipText("Contraste normal");
        botonContrasteNormal.setFocusable(false);
        botonContrasteNormal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonContrasteNormal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonContrasteNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonContrasteNormalActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonContrasteNormal);

        botonContrasteIluminacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/iluminar.png"))); // NOI18N
        botonContrasteIluminacion.setToolTipText("Contraste iluminación");
        botonContrasteIluminacion.setFocusable(false);
        botonContrasteIluminacion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonContrasteIluminacion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonContrasteIluminacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonContrasteIluminacionActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonContrasteIluminacion);

        botonContrasteOscurecimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/oscurecer.png"))); // NOI18N
        botonContrasteOscurecimiento.setToolTipText("Contraste oscurecimiento");
        botonContrasteOscurecimiento.setFocusable(false);
        botonContrasteOscurecimiento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonContrasteOscurecimiento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonContrasteOscurecimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonContrasteOscurecimientoActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonContrasteOscurecimiento);
        barraHerramientasInferior.add(jSeparator5);

        botonFuncionSeno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sinusoidal.png"))); // NOI18N
        botonFuncionSeno.setToolTipText("Función seno");
        botonFuncionSeno.setFocusable(false);
        botonFuncionSeno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonFuncionSeno.setPreferredSize(new java.awt.Dimension(30, 30));
        botonFuncionSeno.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonFuncionSeno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonFuncionSenoActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonFuncionSeno);

        botonFuncionNegativo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/invertir_colores.png"))); // NOI18N
        botonFuncionNegativo.setToolTipText("Función negativo");
        botonFuncionNegativo.setFocusable(false);
        botonFuncionNegativo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonFuncionNegativo.setMaximumSize(new java.awt.Dimension(65, 33));
        botonFuncionNegativo.setMinimumSize(new java.awt.Dimension(65, 33));
        botonFuncionNegativo.setPreferredSize(new java.awt.Dimension(30, 30));
        botonFuncionNegativo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonFuncionNegativo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonFuncionNegativoActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonFuncionNegativo);

        botonSepia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/sepia.png"))); // NOI18N
        botonSepia.setToolTipText("Filtro sepia");
        botonSepia.setFocusable(false);
        botonSepia.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonSepia.setPreferredSize(new java.awt.Dimension(30, 30));
        botonSepia.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonSepia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSepiaActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonSepia);

        botonTintado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/tintado.png"))); // NOI18N
        botonTintado.setToolTipText("Tintado");
        botonTintado.setFocusable(false);
        botonTintado.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonTintado.setPreferredSize(new java.awt.Dimension(30, 30));
        botonTintado.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonTintado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTintadoActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonTintado);

        botonEcualizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/ecualizador.png"))); // NOI18N
        botonEcualizacion.setToolTipText("Ecualización");
        botonEcualizacion.setFocusable(false);
        botonEcualizacion.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonEcualizacion.setPreferredSize(new java.awt.Dimension(30, 30));
        botonEcualizacion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonEcualizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEcualizacionActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonEcualizacion);

        botonSobel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconosV2/umbralizacion.png"))); // NOI18N
        botonSobel.setToolTipText("Filtro sobel");
        botonSobel.setFocusable(false);
        botonSobel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonSobel.setPreferredSize(new java.awt.Dimension(30, 30));
        botonSobel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonSobel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSobelActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonSobel);

        botonFuncionRRN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/funcion.png"))); // NOI18N
        botonFuncionRRN.setToolTipText("Función propia RRN");
        botonFuncionRRN.setFocusable(false);
        botonFuncionRRN.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonFuncionRRN.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonFuncionRRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonFuncionRRNActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonFuncionRRN);

        botonGoldFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/goldfilter.png"))); // NOI18N
        botonGoldFilter.setToolTipText("Filtro efecto dorado");
        botonGoldFilter.setFocusable(false);
        botonGoldFilter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonGoldFilter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonGoldFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGoldFilterActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonGoldFilter);

        botonMirrorFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/mirrorfilter.png"))); // NOI18N
        botonMirrorFilter.setToolTipText("Filtro efecto espejo");
        botonMirrorFilter.setFocusable(false);
        botonMirrorFilter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonMirrorFilter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonMirrorFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMirrorFilterActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonMirrorFilter);
        barraHerramientasInferior.add(jSeparator6);

        botonMuestraBandas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/bandas.png"))); // NOI18N
        botonMuestraBandas.setToolTipText("Muestra las bandas de color");
        botonMuestraBandas.setFocusable(false);
        botonMuestraBandas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonMuestraBandas.setPreferredSize(new java.awt.Dimension(30, 30));
        botonMuestraBandas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonMuestraBandas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMuestraBandasActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonMuestraBandas);

        listaDesplegableBandas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "sRGB", "YCC", "GREY" }));
        listaDesplegableBandas.setToolTipText("Conversión banda color");
        listaDesplegableBandas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaDesplegableBandasActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(listaDesplegableBandas);
        barraHerramientasInferior.add(jSeparator7);

        sliderRotacion.setMajorTickSpacing(90);
        sliderRotacion.setMaximum(180);
        sliderRotacion.setMinimum(-180);
        sliderRotacion.setPaintTicks(true);
        sliderRotacion.setToolTipText("Control de rotación");
        sliderRotacion.setValue(0);
        sliderRotacion.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderRotacionStateChanged(evt);
            }
        });
        sliderRotacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sliderRotacionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sliderRotacionFocusLost(evt);
            }
        });
        barraHerramientasInferior.add(sliderRotacion);

        botonRotacion90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion90.png"))); // NOI18N
        botonRotacion90.setToolTipText("Rotación 90º");
        botonRotacion90.setFocusable(false);
        botonRotacion90.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRotacion90.setPreferredSize(new java.awt.Dimension(30, 30));
        botonRotacion90.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRotacion90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotacion90ActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonRotacion90);

        botonRotacion180.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion180.png"))); // NOI18N
        botonRotacion180.setToolTipText("Rotación 180º");
        botonRotacion180.setFocusable(false);
        botonRotacion180.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRotacion180.setPreferredSize(new java.awt.Dimension(30, 30));
        botonRotacion180.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRotacion180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotacion180ActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonRotacion180);

        botonRotacion270.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/rotacion270.png"))); // NOI18N
        botonRotacion270.setToolTipText("Rotación 270º");
        botonRotacion270.setFocusable(false);
        botonRotacion270.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonRotacion270.setPreferredSize(new java.awt.Dimension(30, 30));
        botonRotacion270.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonRotacion270.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRotacion270ActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonRotacion270);
        barraHerramientasInferior.add(jSeparator8);

        botonAumentaEscala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconosV2/zoom_+.png"))); // NOI18N
        botonAumentaEscala.setToolTipText("Aumentar escala");
        botonAumentaEscala.setFocusable(false);
        botonAumentaEscala.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonAumentaEscala.setPreferredSize(new java.awt.Dimension(30, 30));
        botonAumentaEscala.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonAumentaEscala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAumentaEscalaActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonAumentaEscala);

        botonDisminuyeEscala.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconosV2/zoom_-.png"))); // NOI18N
        botonDisminuyeEscala.setToolTipText("Disminuir escala");
        botonDisminuyeEscala.setFocusable(false);
        botonDisminuyeEscala.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonDisminuyeEscala.setPreferredSize(new java.awt.Dimension(30, 30));
        botonDisminuyeEscala.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonDisminuyeEscala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDisminuyeEscalaActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonDisminuyeEscala);
        barraHerramientasInferior.add(jSeparator9);

        sliderUmbralizacion.setMaximum(255);
        sliderUmbralizacion.setToolTipText("Control de umbralización");
        sliderUmbralizacion.setValue(128);
        sliderUmbralizacion.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderUmbralizacionStateChanged(evt);
            }
        });
        sliderUmbralizacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sliderUmbralizacionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sliderUmbralizacionFocusLost(evt);
            }
        });
        barraHerramientasInferior.add(sliderUmbralizacion);
        barraHerramientasInferior.add(jSeparator10);

        botonSuma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/aumentar.png"))); // NOI18N
        botonSuma.setToolTipText("Suma de imagenes");
        botonSuma.setFocusable(false);
        botonSuma.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonSuma.setPreferredSize(new java.awt.Dimension(30, 30));
        botonSuma.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonSuma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSumaActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonSuma);

        botonResta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/disminuir.png"))); // NOI18N
        botonResta.setToolTipText("Resta de imagenes");
        botonResta.setFocusable(false);
        botonResta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botonResta.setPreferredSize(new java.awt.Dimension(30, 30));
        botonResta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botonResta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRestaActionPerformed(evt);
            }
        });
        barraHerramientasInferior.add(botonResta);

        panelInferior.add(barraHerramientasInferior, java.awt.BorderLayout.PAGE_START);

        panelFormaActiva.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        panelFormaActiva.setLayout(new java.awt.BorderLayout());

        formaActiva.setText("Punto");
        panelFormaActiva.add(formaActiva, java.awt.BorderLayout.LINE_START);
        panelFormaActiva.add(coordenadasLienzo, java.awt.BorderLayout.LINE_END);

        panelInferior.add(panelFormaActiva, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelInferior, java.awt.BorderLayout.PAGE_END);

        menuArchivo.setText("Archivo");

        submenuNuevo.setText("Nuevo");
        submenuNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuNuevoActionPerformed(evt);
            }
        });
        menuArchivo.add(submenuNuevo);

        submenuAbrir.setText("Abrir");
        submenuAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuAbrirActionPerformed(evt);
            }
        });
        menuArchivo.add(submenuAbrir);

        submenuGuardar.setText("Guardar");
        submenuGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuGuardarActionPerformed(evt);
            }
        });
        menuArchivo.add(submenuGuardar);

        barraMenu.add(menuArchivo);

        menuVer.setText("Ver");

        checkBarraEstado.setSelected(true);
        checkBarraEstado.setText("Ver barra de estado");
        checkBarraEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBarraEstadoActionPerformed(evt);
            }
        });
        menuVer.add(checkBarraEstado);

        checkBarraFormas.setSelected(true);
        checkBarraFormas.setText("Ver barra de formas");
        checkBarraFormas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBarraFormasActionPerformed(evt);
            }
        });
        menuVer.add(checkBarraFormas);

        checkBarraImagen.setSelected(true);
        checkBarraImagen.setText("Ver barra de imagen");
        checkBarraImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBarraImagenActionPerformed(evt);
            }
        });
        menuVer.add(checkBarraImagen);

        barraMenu.add(menuVer);

        menuImagen.setText("Imagen");

        submenuResize.setText("Tamaño de imagen");
        submenuResize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuResizeActionPerformed(evt);
            }
        });
        menuImagen.add(submenuResize);

        submenuDuplicar.setText("Duplicar");
        submenuDuplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuDuplicarActionPerformed(evt);
            }
        });
        menuImagen.add(submenuDuplicar);

        barraMenu.add(menuImagen);

        menuAyuda.setText("Ayuda");

        submenuAcercaDe.setText("Acerca de");
        submenuAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submenuAcercaDeActionPerformed(evt);
            }
        });
        menuAyuda.add(submenuAcercaDe);

        barraMenu.add(menuAyuda);

        setJMenuBar(barraMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonPuntoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPuntoActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.Punto);
            formaActiva.setText("Punto");
        } 
    }//GEN-LAST:event_botonPuntoActionPerformed

    private void botonLineaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonLineaActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.Linea);
            formaActiva.setText("Linea");
        }
    }//GEN-LAST:event_botonLineaActionPerformed

    private void botonRectanguloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRectanguloActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.Rectangulo);
            formaActiva.setText("Rectangulo");
        }
    }//GEN-LAST:event_botonRectanguloActionPerformed

    private void botonElipseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonElipseActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.Elipse);
            formaActiva.setText("Elipse");
        }
    }//GEN-LAST:event_botonElipseActionPerformed

    private void spinnerGrosorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerGrosorStateChanged
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setTamañoTrazo((int)spinnerGrosor.getValue());
            actualizarFiguraSeleccionada(vi);
        }
    }//GEN-LAST:event_spinnerGrosorStateChanged

    private void checkTransparenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkTransparenciaActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setTransparencia(checkTransparencia.isSelected());
            actualizarFiguraSeleccionada(vi);
        }
    }//GEN-LAST:event_checkTransparenciaActionPerformed

    private void checkAlisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAlisarActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setAlisar(checkAlisar.isSelected());
            actualizarFiguraSeleccionada(vi);
        }
    }//GEN-LAST:event_checkAlisarActionPerformed

    private void botonEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEditarActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.Edicion);
        }
    }//GEN-LAST:event_botonEditarActionPerformed

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi=(VentanaInterna) escritorio.getSelectedFrame();
        if (vi != null) {
            JFileChooser dlg = new JFileChooser();
            
            String sFiltro = "Formatos " + Arrays.toString(ImageIO.getWriterFileSuffixes());
            FileNameExtensionFilter filtro = new FileNameExtensionFilter(sFiltro, ImageIO.getWriterFormatNames());
            dlg.setFileFilter(filtro);
            
            int resp = dlg.showSaveDialog(this);
            if (resp == JFileChooser.APPROVE_OPTION) {
                File f = dlg.getSelectedFile();
                        
                int i = f.getName().lastIndexOf('.');
                String extension = i > 0 ? f.getName().substring(i + 1) : "";
                
                try{
                    LienzoImagen2D lienzo = (LienzoImagen2D)vi.getLienzo();
                    BufferedImage img = lienzo.getImage(true);
                    if (img != null){
                        boolean ok = ImageIO.write(img, extension, f);
                        if(ok){
                            vi.setTitle(f.getName());
                        }else{
                            throw new UnsupportedDataTypeException();
                        }
                    }
                }catch (UnsupportedDataTypeException ex){
                    JOptionPane.showMessageDialog(this, "Formato no soportado", null, 0);
                }catch (FileNotFoundException ex){
                    JOptionPane.showMessageDialog(this, "No se encuentra la ruta especificada", null, 0);
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(this, "Fallo en el guardado de la imagen", "Fallo", 0);
                }
            }
        }
    }//GEN-LAST:event_botonGuardarActionPerformed

    private void botonAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAbrirActionPerformed
        // TODO add your handling code here:
        JFileChooser dlg = new JFileChooser();
        
        String sFiltro = "Formatos imagen: " + Arrays.toString(ImageIO.getReaderFileSuffixes());
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(sFiltro, ImageIO.getReaderFormatNames());
        dlg.addChoosableFileFilter(filtro);
        dlg.setFileFilter(filtro);
        
        String[] formatosAudio = new String[8];
        formatosAudio[0]="wav";
        formatosAudio[1]="WAV";
        formatosAudio[2]="au";
        formatosAudio[3]="AU";
        formatosAudio[4]="aiff";
        formatosAudio[5]="AIFF";
        formatosAudio[6]="wave";
        formatosAudio[7]="WAVE";
        String Audio = "Formatos audio: " + Arrays.toString(formatosAudio);
        
        FileNameExtensionFilter filtroAudio = new FileNameExtensionFilter(Audio, formatosAudio);
        dlg.addChoosableFileFilter(filtroAudio);
       
        int resp = dlg.showOpenDialog(this);
        
        if( resp == JFileChooser.APPROVE_OPTION) {
            try{
                File f = dlg.getSelectedFile();
                FormatValidator fvImage = new FormatValidator(FormatValidator.IMAGE_PATTERN);
                FormatValidator fvSound = new FormatValidator(FormatValidator.SOUND_PATTERN);
                
                if(fvImage.validate(f.getName())){
                    BufferedImage img = ImageIO.read(f);
                    if (img == null) {
                        throw new IOException();
                    }

                    VentanaInterna vi = new VentanaInterna(this);
                    LienzoImagen2D lienzo = (LienzoImagen2D)vi.getLienzo();
                    lienzo.setImage(img);

                    VentanaInterna ventanaElegida = (VentanaInterna)this.escritorio.getSelectedFrame();
                    if (ventanaElegida != null) {
                      vi.setLocation(ventanaElegida.getX() + 20, ventanaElegida.getY() + 20);
                    }
                    this.escritorio.add(vi);
                    
                    if (img.getColorModel().getColorSpace().isCS_sRGB()) 
                        vi.setTitle(f.getName() + " [RGB]");
                    else if (!img.getColorModel().getColorSpace().isCS_sRGB() && img.getRaster().getNumBands()==1) 
                        vi.setTitle(f.getName() + " [GREY]");
                    else if (!img.getColorModel().getColorSpace().isCS_sRGB() && img.getRaster().getNumBands()!=1) 
                        vi.setTitle(f.getName() + " [YCC]");
                    
                    ((Lienzo2D)vi.getLienzo()).addLienzoListener(new ManejadorLienzo(this));
                    vi.setVisible(true);
                }else if(fvSound.validate(f.getName())){
                    File archivoSonido = new File(f.getAbsolutePath()){
                        @Override
                        public String toString(){
                            return this.getName();
                        }
                    };
                    
                    listaDesplegableAudio.addItem(archivoSonido);
                    listaDesplegableAudio.setSelectedItem(archivoSonido);
                    File you = (File)listaDesplegableAudio.getSelectedItem();
                }else{
                    JOptionPane.showMessageDialog(this, "Ha ocurrido un error o no se puede abrir ese tipo de fichero", "Error", 0);
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Error al leer el fichero", "Error", 0);
            }
        }
    }//GEN-LAST:event_botonAbrirActionPerformed

    private void botonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNuevoActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = new VentanaInterna(this);
        escritorio.add(vi);
        ModoPintado modo = null;
        Color colorStroke = null;
        Color colorRelleno = null;
        int tamañoTrazo=0;
        TipoTrazo tipoTrazo = null;
        boolean relleno=false;
        TipoRelleno tipoRelleno = null;
        boolean transparencia=false;
        float nivelTransparencia=0;
        boolean alisar=false;
       
        if(botonPunto.isSelected()){
            modo = ModoPintado.Punto;
        }else if(botonLinea.isSelected()){
            modo = ModoPintado.Linea;
        }else if(botonRectangulo.isSelected()){
            modo = ModoPintado.Rectangulo;
        }else if(botonElipse.isSelected()){
            modo = ModoPintado.Elipse;
        }else if(botonEditar.isSelected()){
            modo = ModoPintado.Edicion;
        }else if(botonArco.isSelected()){
            modo = ModoPintado.Arco;
        }else if(botonRoundedRectangle.isSelected()){
            modo = ModoPintado.RoundedRectangle;
        }else if(botonQuadCurve.isSelected()){
            modo = ModoPintado.QuadCurve;
        }else if(botonCubicCurve.isSelected()){
            modo = ModoPintado.CubicCurve;
        }else if(botonTrazoLibre.isSelected()){
            modo = ModoPintado.TrazoLibre;
        }else if(botonPoligono.isSelected()){
            modo = ModoPintado.Poligono;
        }else if(botonFormaPersonalizada.isSelected()){
            modo = ModoPintado.FormaPersonalizada;
        }else if(botonTexto.isSelected()){
            modo = ModoPintado.Texto;
        }
         
        colorStroke = (Color)listaDesplegableColoresContorno.getSelectedItem();
        colorRelleno = (Color)listaDesplegableColoresRelleno.getSelectedItem();
        tamañoTrazo = (int) spinnerGrosor.getValue();
        
        switch(listaDesplegableTipoTrazo.getSelectedIndex()){
            case 0:
                tipoTrazo = TipoTrazo.LINEA_CONTINUA;
                break;
            case 1:
                tipoTrazo = TipoTrazo.LINEA_DISCONTINUA;
                break;
        }
        
        relleno = checkRelleno.isSelected();
        
        switch(listaDesplegableTipoRelleno.getSelectedIndex()){
            case 0:
                tipoRelleno = TipoRelleno.COLOR_SOLIDO;
                break;
            case 1:
                tipoRelleno = TipoRelleno.DEGRADADO_HORIZONTAL;
                break;
            case 2:
                tipoRelleno = TipoRelleno.DEGRADADO_VERTICAL;
                break;
        }
        
        transparencia = checkTransparencia.isSelected();
        nivelTransparencia = (float) spinnerNivelTransparencia.getValue();
        
        alisar = checkAlisar.isSelected();
                
        vi.getLienzo().setModoPintado(modo);
        vi.getLienzo().setColorStroke(colorStroke);
        vi.getLienzo().setColorRelleno(colorRelleno);  
        vi.getLienzo().setTamañoTrazo(tamañoTrazo);
        vi.getLienzo().setTrazo(tipoTrazo);
        vi.getLienzo().setRelleno(relleno);
        vi.getLienzo().setTipoRelleno(tipoRelleno);
        vi.getLienzo().setTransparencia(transparencia);
        vi.getLienzo().setNivelTransparencia(nivelTransparencia);
        vi.getLienzo().setAlisar(alisar);

        vi.setVisible(true);
        BufferedImage img;
        img = new BufferedImage(anchoImagen,altoImagen,BufferedImage.TYPE_INT_ARGB);
        vi.getLienzo().setImage(img);
        vi.getLienzo().setImagenAbierta(false);
        ((Lienzo2D)vi.getLienzo()).addLienzoListener(new ManejadorLienzo(this));
    }//GEN-LAST:event_botonNuevoActionPerformed

    private void checkBarraFormasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBarraFormasActionPerformed
        // TODO add your handling code here:
        if(checkBarraFormas.isSelected())
            barraHerramientas.setVisible(true);
        else
            barraHerramientas.setVisible(false);
    }//GEN-LAST:event_checkBarraFormasActionPerformed

    private void checkBarraEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBarraEstadoActionPerformed
        // TODO add your handling code here:
        if(checkBarraEstado.isSelected())
            panelFormaActiva.setVisible(true);
        else
            panelFormaActiva.setVisible(false);
    }//GEN-LAST:event_checkBarraEstadoActionPerformed

    private void escritorioMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_escritorioMouseMoved
        // TODO add your handling code here:
        coordenadasLienzo.setText("");
    }//GEN-LAST:event_escritorioMouseMoved

    private void submenuAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuAbrirActionPerformed
        // TODO add your handling code here:
        botonAbrirActionPerformed(evt);
    }//GEN-LAST:event_submenuAbrirActionPerformed

    private void submenuGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuGuardarActionPerformed
        // TODO add your handling code here:
        botonGuardarActionPerformed(evt);
    }//GEN-LAST:event_submenuGuardarActionPerformed

    private void submenuNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuNuevoActionPerformed
        // TODO add your handling code here:
        botonNuevoActionPerformed(evt);
    }//GEN-LAST:event_submenuNuevoActionPerformed

    private void botonArcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonArcoActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.Arco);
            formaActiva.setText("Arco");
        }
    }//GEN-LAST:event_botonArcoActionPerformed

    private void botonRoundedRectangleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRoundedRectangleActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.RoundedRectangle);
            formaActiva.setText("Rectangulo con bordes redondeados");
        }
    }//GEN-LAST:event_botonRoundedRectangleActionPerformed

    private void botonQuadCurveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonQuadCurveActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.QuadCurve);
            formaActiva.setText("Curva con un punto de control");
        }
    }//GEN-LAST:event_botonQuadCurveActionPerformed

    private void botonCubicCurveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCubicCurveActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.CubicCurve);
            formaActiva.setText("Curva con dos puntos de control");
        }
    }//GEN-LAST:event_botonCubicCurveActionPerformed

    private void botonTrazoLibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTrazoLibreActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.TrazoLibre);
            formaActiva.setText("Trazo libre");
        }
    }//GEN-LAST:event_botonTrazoLibreActionPerformed

    private void botonPoligonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPoligonoActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.Poligono);
            formaActiva.setText("Polígono");
        }
    }//GEN-LAST:event_botonPoligonoActionPerformed

    private void botonFormaPersonalizadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonFormaPersonalizadaActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.FormaPersonalizada);
            formaActiva.setText("Forma personalizada");
        }
    }//GEN-LAST:event_botonFormaPersonalizadaActionPerformed

    private void botonTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTextoActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setModoPintado(ModoPintado.Texto);
            formaActiva.setText("Texto");
        }
    }//GEN-LAST:event_botonTextoActionPerformed

    private void spinnerNivelTransparenciaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerNivelTransparenciaStateChanged
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setNivelTransparencia((float)spinnerNivelTransparencia.getValue());
            actualizarFiguraSeleccionada(vi);
        }
    }//GEN-LAST:event_spinnerNivelTransparenciaStateChanged

    private void listaDesplegableColoresContornoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaDesplegableColoresContornoItemStateChanged
        // TODO add your handling code here:
        int opcionColor = listaDesplegableColoresContorno.getSelectedIndex();
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        Color colorElegido = (Color)listaDesplegableColoresContorno.getSelectedItem();
        
        if(vi!=null){
            vi.getLienzo().setColorStroke(colorElegido);
            actualizarFiguraSeleccionada(vi);
        }
    }//GEN-LAST:event_listaDesplegableColoresContornoItemStateChanged

    private void listaDesplegableColoresRellenoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaDesplegableColoresRellenoItemStateChanged
        // TODO add your handling code here:
        int opcionColor = listaDesplegableColoresRelleno.getSelectedIndex();
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        Color colorElegido = (Color)listaDesplegableColoresRelleno.getSelectedItem();
        
        if(vi!=null){
            vi.getLienzo().setColorRelleno(colorElegido);
            actualizarFiguraSeleccionada(vi);
        }
    }//GEN-LAST:event_listaDesplegableColoresRellenoItemStateChanged

    private void listaDesplegableTipoTrazoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaDesplegableTipoTrazoItemStateChanged
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            if(listaDesplegableTipoTrazo.getSelectedIndex() == 0){
                vi.getLienzo().setTrazo(TipoTrazo.LINEA_CONTINUA);
            }else if(listaDesplegableTipoTrazo.getSelectedIndex() == 1){
                vi.getLienzo().setTrazo(TipoTrazo.LINEA_DISCONTINUA);
            }
            actualizarFiguraSeleccionada(vi);            
        }
    }//GEN-LAST:event_listaDesplegableTipoTrazoItemStateChanged

    private void checkRellenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkRellenoActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            vi.getLienzo().setRelleno(checkRelleno.isSelected());
            actualizarFiguraSeleccionada(vi);
        }
    }//GEN-LAST:event_checkRellenoActionPerformed

    private void submenuResizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuResizeActionPerformed
        // TODO add your handling code here:
        LienzoImagen2D lienzo = null;
        BufferedImage img = null;
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        
        if (vi != null){
            lienzo = (LienzoImagen2D)vi.getLienzo();
            img = lienzo.getImage(false);
            if (img != null)
            {
              this.anchoImagen = img.getWidth();
              this.altoImagen = img.getHeight();
            }
        }
        
        VentanaResize dlg = new VentanaResize(this, this.anchoImagen, this.altoImagen);
        int resp = dlg.mostrarVentanaResize();
        if ((resp == 1) || (resp == 2)){
            this.anchoImagen = dlg.getAnchoImagen();
            this.altoImagen = dlg.getAltoImagen();
            if ((vi != null) && (img != null)){
                BufferedImage imgOut = new BufferedImage(this.anchoImagen, this.altoImagen, 1);
                Graphics2D g2D = imgOut.createGraphics();
                g2D.setPaint(Color.WHITE);
                g2D.fillRect(0, 0, this.anchoImagen, this.altoImagen);
                if (resp == 2){
                    g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2D.drawImage(img, 0, 0, this.anchoImagen, this.altoImagen, null);
                }else{
                    g2D.drawImage(img, 0, 0, null);
                }
                lienzo.setImage(imgOut);
                lienzo.repaint();
            }
        }
    }//GEN-LAST:event_submenuResizeActionPerformed

    private void sliderBrilloFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderBrilloFocusGained
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            ColorModel cm = vi.getLienzo().getImage(false).getColorModel();
            WritableRaster raster = vi.getLienzo().getImage(false).copyData(null);
            boolean alfaPre = vi.getLienzo().getImage(false).isAlphaPremultiplied();
            imgSource = new BufferedImage(cm,raster,alfaPre,null);
        }
    }//GEN-LAST:event_sliderBrilloFocusGained

    private void sliderBrilloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderBrilloFocusLost
        // TODO add your handling code here:
        imgSource=null;
        sliderBrillo.setValue(0);
    }//GEN-LAST:event_sliderBrilloFocusLost

    private void sliderBrilloStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderBrilloStateChanged
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            if(imgSource!=null){
                try{
                    RescaleOp rop = null;
                    if(!imgSource.getColorModel().hasAlpha()){
                        rop = new RescaleOp(1.0f, sliderBrillo.getValue(), null);
                    }else{
                        float valorBrillo = this.sliderBrillo.getValue();
                        rop = new RescaleOp(new float[]{1.0f, 1.0f, 1.0f, 1.0f}, new float[]{valorBrillo, valorBrillo, valorBrillo, 0.0f}, null);
                    }
                    
                    rop.filter(imgSource, vi.getLienzo().getImage(false));
                    //vi.getLienzo().setImage(rop.filter(imgSource, null));
                    //lop.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
                    escritorio.revalidate();
                    escritorio.repaint();
                } catch(IllegalArgumentException e){
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
    }//GEN-LAST:event_sliderBrilloStateChanged

    private void listaDesplegableFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listaDesplegableFiltroActionPerformed
        // TODO add your handling code here:
        Kernel k = null;
                
        switch(listaDesplegableFiltro.getSelectedIndex()){
            case 0:
                k=null;
                break;
            case 1:
                k = KernelProducer.createKernel(KernelProducer.TYPE_MEDIA_3x3);
                break;
            case 2:
                k = KernelProducer.createKernel(KernelProducerRRN.TYPE_MEDIA_5x5);
                break;
            case 3:
                k = KernelProducer.createKernel(KernelProducerRRN.TYPE_MEDIA_7x7);
                break;
            case 4:
                k = KernelProducer.createKernel(KernelProducer.TYPE_BINOMIAL_3x3);
                break;
            case 5:
                k = KernelProducer.createKernel(KernelProducer.TYPE_ENFOQUE_3x3);
                break;
            case 6:
                k = KernelProducer.createKernel(KernelProducer.TYPE_RELIEVE_3x3);
                break;
            case 7:
                k = KernelProducer.createKernel(KernelProducer.TYPE_LAPLACIANA_3x3);
                break;
        }
        
        if(k!=null){
            ConvolveOp cop = new ConvolveOp(k);
            VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
            if (vi != null) {
                vi.getLienzo().getImage(false).setData(cop.filter(vi.getLienzo().getImage(false), null).getRaster());
                vi.repaint();
                escritorio.repaint();
            }    
        }
        
    }//GEN-LAST:event_listaDesplegableFiltroActionPerformed

    private void botonContrasteNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonContrasteNormalActionPerformed
        // TODO add your handling code here:
        contraste(LookupTableProducer.TYPE_SFUNCION);
    }//GEN-LAST:event_botonContrasteNormalActionPerformed

    private void botonContrasteIluminacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonContrasteIluminacionActionPerformed
        // TODO add your handling code here:
        contraste(LookupTableProducer.TYPE_ROOT);
    }//GEN-LAST:event_botonContrasteIluminacionActionPerformed

    private void botonContrasteOscurecimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonContrasteOscurecimientoActionPerformed
        // TODO add your handling code here:
        contraste(LookupTableProducer.TYPE_POWER);
    }//GEN-LAST:event_botonContrasteOscurecimientoActionPerformed

    private void botonFuncionSenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonFuncionSenoActionPerformed
        // TODO add your handling code here:
        aplicarFuncion(ImageFunctionRRN.TYPE_SENO);
    }//GEN-LAST:event_botonFuncionSenoActionPerformed

    private void botonFuncionNegativoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonFuncionNegativoActionPerformed
        // TODO add your handling code here:
        aplicarFuncion(ImageFunctionRRN.TYPE_INVERTIR);
    }//GEN-LAST:event_botonFuncionNegativoActionPerformed

    private void sliderRotacionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderRotacionStateChanged
        // TODO add your handling code here:    
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {  
            if(imgSource == null){
                imgSource = vi.getLienzo().getImage(false);
            }else{
                double rotationRequired = Math.toRadians (sliderRotacion.getValue());
                double locationX = imgSource.getWidth() / 2;
                double locationY = imgSource.getHeight() / 2;

                AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);         

                BufferedImage newImage = op.filter(imgSource, null);

                vi.getLienzo().setImage(newImage);
                vi.repaint();
            }    
        }
    }//GEN-LAST:event_sliderRotacionStateChanged

    private void botonRotacion90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotacion90ActionPerformed
        // TODO add your handling code here:
        rotacion(90);
    }//GEN-LAST:event_botonRotacion90ActionPerformed

    private void botonRotacion180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotacion180ActionPerformed
        // TODO add your handling code here:
        rotacion(180);
    }//GEN-LAST:event_botonRotacion180ActionPerformed

    private void botonRotacion270ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRotacion270ActionPerformed
        // TODO add your handling code here:
        rotacion(270);
    }//GEN-LAST:event_botonRotacion270ActionPerformed

    private void botonAumentaEscalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAumentaEscalaActionPerformed
        // TODO add your handling code here:
        escalar(1.25);
    }//GEN-LAST:event_botonAumentaEscalaActionPerformed

    private void botonDisminuyeEscalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDisminuyeEscalaActionPerformed
        // TODO add your handling code here:
        escalar(0.75);
    }//GEN-LAST:event_botonDisminuyeEscalaActionPerformed

    private void sliderRotacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderRotacionFocusGained
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {  
            imgSource = vi.getLienzo().getImage(false);
        }
    }//GEN-LAST:event_sliderRotacionFocusGained

    private void sliderRotacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderRotacionFocusLost
        // TODO add your handling code here:
        imgSource = null;
        sliderRotacion.setValue(0);
    }//GEN-LAST:event_sliderRotacionFocusLost

    private void submenuDuplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuDuplicarActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            VentanaInterna nueva = new VentanaInterna(this);
            ColorModel cm = vi.getLienzo().getImage(true).getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = vi.getLienzo().getImage(true).copyData(vi.getLienzo().getImage(true).getRaster().createCompatibleWritableRaster());
            BufferedImage copiaImagen = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
            nueva.getLienzo().setImage(copiaImagen);
            escritorio.add(nueva);
            nueva.setVisible(true);
        } 
    }//GEN-LAST:event_submenuDuplicarActionPerformed

    private void botonMuestraBandasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMuestraBandasActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {
            BufferedImage src = vi.getLienzo().getImage(false);
            if(src!=null){
                //Creamos el modelo de color de la nueva imagen basado en un espcio de color GRAY
                ColorSpace cs = new sm.image.color.GreyColorSpace();
                ComponentColorModel cm = new ComponentColorModel(cs, false, false,Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
                
                for(int i = 0; i<src.getRaster().getNumBands();i++){
                    //Creamos el nuevo raster a partir del raster de la imagen original
                    int bandList[] = {i};
                    WritableRaster bandRaster = (WritableRaster)src.getRaster().createWritableChild(0,0,src.getWidth(), src.getHeight(), 0, 0, bandList);
                    //Creamos una nueva imagen que contiene como raster el correspondiente a la banda
                    BufferedImage imgBanda = new BufferedImage(cm, bandRaster, false, null);
                    VentanaInterna nueva = new VentanaInterna(this);
                    nueva.getLienzo().setImage(imgBanda);
                    escritorio.add(nueva);
                    
                    if (src.getColorModel().getColorSpace().isCS_sRGB()){
                        if(i==0)
                            nueva.setTitle(vi.getTitle() + " Banda [R]");
                        else if(i==1)
                            nueva.setTitle(vi.getTitle() + " Banda [G]");
                        else if(i==2)
                            nueva.setTitle(vi.getTitle() + " Banda [B]");
                    }else if (!src.getColorModel().getColorSpace().isCS_sRGB() && src.getRaster().getNumBands()==3){
                        if(i==0)
                            nueva.setTitle(vi.getTitle() +  " Banda [Y]");
                        else if(i==1)
                            nueva.setTitle(vi.getTitle() +  " Banda [C]");
                        else if(i==2)
                            nueva.setTitle(vi.getTitle() +  " Banda [C]");
                    }else if (!src.getColorModel().getColorSpace().isCS_sRGB() && src.getRaster().getNumBands()==1){
                        nueva.setTitle(vi.getTitle() +  " Banda [GREY]");
                    }
               
                    nueva.setVisible(true);
                }
            }
            
        }
    }//GEN-LAST:event_botonMuestraBandasActionPerformed

    private void listaDesplegableBandasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listaDesplegableBandasActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {  
            BufferedImage src = vi.getLienzo().getImage(false);
            
            if(src!=null){
                ColorSpace color_space = null;
                                                        
                switch (listaDesplegableBandas.getSelectedIndex()) {
                    case 0:
                        if (!src.getColorModel().getColorSpace().isCS_sRGB()) 
                            color_space = ColorSpace.getInstance(ColorSpace.CS_sRGB);    
                        break;
                    case 1:
                        if (src.getColorModel().getColorSpace().isCS_sRGB() || src.getRaster().getNumBands()==1) 
                            color_space = ColorSpace.getInstance(ColorSpace.CS_PYCC);                 
                        break;
                    case 2:
                        if (src.getColorModel().getColorSpace().isCS_sRGB() || src.getRaster().getNumBands()!=1) 
                            color_space = ColorSpace.getInstance(ColorSpace.CS_GRAY);    
                        break;
                    default:
                        break;
                }
                
                if(color_space!=null){
                    ColorConvertOp cop = new ColorConvertOp(color_space, null);
                    BufferedImage imgOut = cop.filter(src, null);
                    VentanaInterna nueva = new VentanaInterna(this);
                    nueva.getLienzo().setImage(imgOut);
                    escritorio.add(nueva);
                    
                    if (imgOut.getColorModel().getColorSpace().isCS_sRGB()){
                        nueva.setTitle(vi.getTitle().substring(0, vi.getTitle().length()-6) + " [RGB]");
                    }else if (!imgOut.getColorModel().getColorSpace().isCS_sRGB() && imgOut.getRaster().getNumBands()==3){
                        nueva.setTitle(vi.getTitle().substring(0, vi.getTitle().length()-6) +  " [YCC]");
                    }else if (!imgOut.getColorModel().getColorSpace().isCS_sRGB() && imgOut.getRaster().getNumBands()==1){
                        nueva.setTitle(vi.getTitle().substring(0, vi.getTitle().length()-7) +  " [GREY]");
                    }
                    
                    nueva.setVisible(true);
                }
            }
            
        }     
    }//GEN-LAST:event_listaDesplegableBandasActionPerformed

    private void botonTintadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTintadoActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {  
            TintOp tintado = new TintOp(vi.getLienzo().getColorStroke(), 0.5f);
            tintado.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
            vi.repaint();
            escritorio.repaint();
        }
    }//GEN-LAST:event_botonTintadoActionPerformed

    private void botonEcualizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEcualizacionActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {  
            EqualizationOp ecualizacion = new EqualizationOp();
            ecualizacion.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
            vi.repaint();
            escritorio.repaint();
        }
    }//GEN-LAST:event_botonEcualizacionActionPerformed

    private void botonSepiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSepiaActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {  
            SepiaOPRRN sepia = new SepiaOPRRN();
            sepia.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
            vi.repaint();
            escritorio.repaint();
        }
    }//GEN-LAST:event_botonSepiaActionPerformed

    private void sliderUmbralizacionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderUmbralizacionStateChanged
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {  
            UmbralizacionOPRRN umbralizacion = new UmbralizacionOPRRN(sliderUmbralizacion.getValue());
            vi.getLienzo().setImage(umbralizacion.filter(imgSource, null));
            vi.repaint();
            escritorio.repaint();
        }
    }//GEN-LAST:event_sliderUmbralizacionStateChanged

    private void sliderUmbralizacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderUmbralizacionFocusLost
        // TODO add your handling code here:
        sliderUmbralizacion.setValue(128);
        imgSource=null;
        
    }//GEN-LAST:event_sliderUmbralizacionFocusLost

    private void botonSumaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSumaActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            VentanaInterna viNext = (VentanaInterna) escritorio.selectFrame(false);
            if (viNext != null) {
                BufferedImage imgRight = vi.getLienzo().getImage(false);
                BufferedImage imgLeft = viNext.getLienzo().getImage(false);
                
                if (imgRight != null && imgLeft != null) {
                    try {
                        BlendOp op = new BlendOp(imgLeft);
                        BufferedImage imgdest = op.filter(imgRight, null);
                        vi = new VentanaInterna(this);
                        vi.getLienzo().setImage(imgdest);
                        this.escritorio.add(vi);
                        vi.setVisible(true);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: "+e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_botonSumaActionPerformed

    private void botonRestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRestaActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            VentanaInterna viNext = (VentanaInterna) escritorio.selectFrame(false);
            if (viNext != null) {
                BufferedImage imgRight = vi.getLienzo().getImage(false);
                BufferedImage imgLeft = viNext.getLienzo().getImage(false);
                
                if (imgRight != null && imgLeft != null) {
                    try {
                        SubtractionOp op = new SubtractionOp(imgLeft);
                        BufferedImage imgdest = op.filter(imgRight, null);
                        vi = new VentanaInterna(this);
                        vi.getLienzo().setImage(imgdest);
                        this.escritorio.add(vi);
                        vi.setVisible(true);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: "+e.getLocalizedMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_botonRestaActionPerformed

    private void botonSobelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSobelActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {  
            SobelOPRRN sobel = new SobelOPRRN();
            sobel.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
            vi.repaint();
            escritorio.repaint();
        }
    }//GEN-LAST:event_botonSobelActionPerformed

    private void botonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPlayActionPerformed
        // TODO add your handling code here:
        if(player!=null){
            player.stop();
            botonPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/play24x24.png")));
            player=null;
        }else{
            File f = (File)listaDesplegableAudio.getSelectedItem();
            if(f!=null){
                player = new SMClipPlayer(f);
                if (player != null) {
                    player.play();
                    botonPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/stop24x24.png")));
                }
            }
        }
        
    }//GEN-LAST:event_botonPlayActionPerformed

    private void botonPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPauseActionPerformed
        // TODO add your handling code here:
        if (player != null) {
            player.stop();
        }
    }//GEN-LAST:event_botonPauseActionPerformed

    private void botonGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGrabarActionPerformed
        // TODO add your handling code here:
        if(recorder==null){
            File f = (File)listaDesplegableAudio.getSelectedItem();
            if(f!=null){
                recorder = new SMSoundRecorder(f);
                if (recorder != null) {
                    tiempoGrabado.setText("00:00:00");
                    recorder.record();
                    hiloTimer = new Thread(new Runnable() {
                        public void run(){
                            while (!finGrabacion) {                                
                                timeTick();
                            }
                        }});  
                        hiloTimer.start();
                    }
            }else{
                JOptionPane.showMessageDialog(this, "No hay ningun fichero elegido de audio", "Error", 0);
            }
        }else{
            recorder.stop();
            finGrabacion=true;
            JOptionPane.showMessageDialog(this, "Audio grabado con exito!!");
            recorder=null;
        }
    }//GEN-LAST:event_botonGrabarActionPerformed

    private void sliderUmbralizacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sliderUmbralizacionFocusGained
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        if (vi != null) {
            imgSource = vi.getLienzo().getImage(false);
        }
    }//GEN-LAST:event_sliderUmbralizacionFocusGained

    private void checkBarraImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBarraImagenActionPerformed
        // TODO add your handling code here:
        if(checkBarraImagen.isSelected())
            barraHerramientasInferior.setVisible(true);
        else
            barraHerramientasInferior.setVisible(false);
    }//GEN-LAST:event_checkBarraImagenActionPerformed

    private void submenuAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submenuAcercaDeActionPerformed
        // TODO add your handling code here:
        VentanaAyuda vi = new VentanaAyuda(this, true);
        if (vi!=null){
            vi.setVisible(true);
        }
    }//GEN-LAST:event_submenuAcercaDeActionPerformed

    private void listaDesplegableTipoRellenoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaDesplegableTipoRellenoItemStateChanged
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            if(listaDesplegableTipoRelleno.getSelectedIndex() == 0){
                vi.getLienzo().setTipoRelleno(TipoRelleno.COLOR_SOLIDO);
            }else if(listaDesplegableTipoRelleno.getSelectedIndex() == 1){
                vi.getLienzo().setTipoRelleno(TipoRelleno.DEGRADADO_HORIZONTAL);
            }else if(listaDesplegableTipoRelleno.getSelectedIndex() == 2){
                vi.getLienzo().setTipoRelleno(TipoRelleno.DEGRADADO_VERTICAL);
            }
            actualizarFiguraSeleccionada(vi);
        }
    }//GEN-LAST:event_listaDesplegableTipoRellenoItemStateChanged

    private void listaDesplegableFigurasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listaDesplegableFigurasActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            if(listaDesplegableFiguras.getSelectedIndex()>0){
                botonEditar.setSelected(true);
                vi.getLienzo().setFigura_seleccionada((ShapeRRN)listaDesplegableFiguras.getSelectedItem());
                vi.getLienzo().setModoPintado(ModoPintado.Edicion);
                vi.getLienzo().setLienzoEdit();
                vi.repaint();
            }else if(listaDesplegableFiguras.getSelectedIndex()==0){
                botonEditar.setSelected(false);
                vi.getLienzo().setFigura_seleccionada(null);
                vi.getLienzo().setModoPintado(ModoPintado.Punto);
                vi.repaint();
            }
        }
    }//GEN-LAST:event_listaDesplegableFigurasActionPerformed

    private void coordenadaXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coordenadaXActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            int x=0,y=0;
            
            if(coordenadaX.getValue() instanceof Long)
                x = ((Long)coordenadaX.getValue()).intValue();
            else if(coordenadaX.getValue() instanceof Double)
                x = ((Double)coordenadaX.getValue()).intValue();
            
            if(coordenadaY.getValue() instanceof Long)
                y = ((Long)coordenadaY.getValue()).intValue();
            else if(coordenadaY.getValue() instanceof Double)
                y = ((Double)coordenadaY.getValue()).intValue();
            
            Point p = new Point(x,y);
            vi.getLienzo().getFigura_seleccionada().setLocation(p);
            this.repaint();
        }
    }//GEN-LAST:event_coordenadaXActionPerformed

    private void coordenadaYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coordenadaYActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna)escritorio.getSelectedFrame();
        if(vi!=null){
            int x=0,y=0;
            
            if(coordenadaX.getValue() instanceof Long)
                x = ((Long)coordenadaX.getValue()).intValue();
            else if(coordenadaX.getValue() instanceof Double)
                x = ((Double)coordenadaX.getValue()).intValue();
            
            if(coordenadaY.getValue() instanceof Long)
                y = ((Long)coordenadaY.getValue()).intValue();
            else if(coordenadaY.getValue() instanceof Double)
                y = ((Double)coordenadaY.getValue()).intValue();
            
            Point p = new Point(x,y);
            vi.getLienzo().getFigura_seleccionada().setLocation(p);
            this.repaint();
        }
    }//GEN-LAST:event_coordenadaYActionPerformed

    private void botonSelectorColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSelectorColorActionPerformed
        // TODO add your handling code here:
        SelectorColores dlg = new SelectorColores(this, true);
        dlg.setVisible(true);
        
        if(dlg.getOpcion()==0){
            listaDesplegableColoresContorno.addItem(dlg.getColorElegido());
            listaDesplegableColoresRelleno.addItem(dlg.getColorElegido());
        }
        
    }//GEN-LAST:event_botonSelectorColorActionPerformed

    private void botonFuncionRRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonFuncionRRNActionPerformed
        // TODO add your handling code here:
        aplicarFuncion(ImageFunctionRRN.TYPE_FUNCTIONRRN);
    }//GEN-LAST:event_botonFuncionRRNActionPerformed

    private void botonGoldFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGoldFilterActionPerformed
        // TODO add your handling code here:        
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {  
            GoldFilterRRN goldFilter = new GoldFilterRRN();
            goldFilter.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
            vi.repaint();
            escritorio.repaint();
        }
    }//GEN-LAST:event_botonGoldFilterActionPerformed

    private void botonMirrorFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMirrorFilterActionPerformed
        // TODO add your handling code here:
        VentanaInterna vi = (VentanaInterna) (escritorio.getSelectedFrame());
        
        if (vi != null) {  
            MirrorFilterRRN mirrorFilter = new MirrorFilterRRN();
            mirrorFilter.filter(vi.getLienzo().getImage(false), vi.getLienzo().getImage(false));
            vi.repaint();
            escritorio.repaint();
        }
    }//GEN-LAST:event_botonMirrorFilterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramientas;
    private javax.swing.JToolBar barraHerramientasInferior;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JButton botonAbrir;
    protected javax.swing.JToggleButton botonArco;
    private javax.swing.JButton botonAumentaEscala;
    private javax.swing.JButton botonContrasteIluminacion;
    private javax.swing.JButton botonContrasteNormal;
    private javax.swing.JButton botonContrasteOscurecimiento;
    protected javax.swing.JToggleButton botonCubicCurve;
    private javax.swing.JButton botonDisminuyeEscala;
    private javax.swing.JButton botonEcualizacion;
    protected javax.swing.JToggleButton botonEditar;
    protected javax.swing.JToggleButton botonElipse;
    protected javax.swing.JToggleButton botonFormaPersonalizada;
    private javax.swing.JButton botonFuncionNegativo;
    private javax.swing.JButton botonFuncionRRN;
    private javax.swing.JButton botonFuncionSeno;
    private javax.swing.JButton botonGoldFilter;
    private javax.swing.JToggleButton botonGrabar;
    private javax.swing.JButton botonGuardar;
    protected javax.swing.JToggleButton botonLinea;
    private javax.swing.JButton botonMirrorFilter;
    private javax.swing.JButton botonMuestraBandas;
    private javax.swing.JButton botonNuevo;
    private javax.swing.JButton botonPause;
    private javax.swing.JButton botonPlay;
    protected javax.swing.JToggleButton botonPoligono;
    protected javax.swing.JToggleButton botonPunto;
    protected javax.swing.JToggleButton botonQuadCurve;
    protected javax.swing.JToggleButton botonRectangulo;
    private javax.swing.JButton botonResta;
    private javax.swing.JButton botonRotacion180;
    private javax.swing.JButton botonRotacion270;
    private javax.swing.JButton botonRotacion90;
    protected javax.swing.JToggleButton botonRoundedRectangle;
    private javax.swing.JButton botonSelectorColor;
    private javax.swing.JButton botonSepia;
    private javax.swing.JButton botonSobel;
    private javax.swing.JButton botonSuma;
    protected javax.swing.JToggleButton botonTexto;
    private javax.swing.JButton botonTintado;
    protected javax.swing.JToggleButton botonTrazoLibre;
    protected javax.swing.JToggleButton checkAlisar;
    private javax.swing.JCheckBoxMenuItem checkBarraEstado;
    private javax.swing.JCheckBoxMenuItem checkBarraFormas;
    private javax.swing.JCheckBoxMenuItem checkBarraImagen;
    protected javax.swing.JToggleButton checkRelleno;
    protected javax.swing.JToggleButton checkTransparencia;
    protected javax.swing.JFormattedTextField coordenadaX;
    protected javax.swing.JFormattedTextField coordenadaY;
    private static javax.swing.JLabel coordenadasLienzo;
    private javax.swing.JDesktopPane escritorio;
    private javax.swing.JLabel formaActiva;
    private javax.swing.ButtonGroup grupoBotonesColores;
    private javax.swing.ButtonGroup grupoBotonesHerramientas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JComboBox<File> listaDesplegableAudio;
    private javax.swing.JComboBox<String> listaDesplegableBandas;
    protected javax.swing.JComboBox<Color> listaDesplegableColoresContorno;
    protected javax.swing.JComboBox<Color> listaDesplegableColoresRelleno;
    protected javax.swing.JComboBox<Object> listaDesplegableFiguras;
    private javax.swing.JComboBox<String> listaDesplegableFiltro;
    protected javax.swing.JComboBox<String> listaDesplegableTipoRelleno;
    protected javax.swing.JComboBox<String> listaDesplegableTipoTrazo;
    protected javax.swing.JComboBox listaFuentes;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuAyuda;
    private javax.swing.JMenu menuImagen;
    private javax.swing.JMenu menuVer;
    private javax.swing.JPanel panelFormaActiva;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JSlider sliderBrillo;
    private javax.swing.JSlider sliderRotacion;
    private javax.swing.JSlider sliderUmbralizacion;
    protected javax.swing.JSpinner spinnerGrosor;
    protected javax.swing.JSpinner spinnerNivelTransparencia;
    private javax.swing.JMenuItem submenuAbrir;
    private javax.swing.JMenuItem submenuAcercaDe;
    private javax.swing.JMenuItem submenuDuplicar;
    private javax.swing.JMenuItem submenuGuardar;
    private javax.swing.JMenuItem submenuNuevo;
    private javax.swing.JMenuItem submenuResize;
    private javax.swing.JLabel tiempoGrabado;
    // End of variables declaration//GEN-END:variables
}
