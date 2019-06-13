/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import sm.image.BufferedImageOpAdapter;
import sm.image.BufferedImagePixelIterator;
import sm.image.ImageTools;
import sm.image.KernelProducer;

/**
 * Clase que define el filtro el cual usa un algoritmo detector de bordes. Recorrerá la imagen y aplicará el gradiente según las formulas respectivas vistas en teoría.
 * Primero se aplica el Kernel SOBEL que se inclute en la libreria sm.image. Se realiza dicha convolución sobre la imagen, una vez realizada la convolución, se itera sobre 
 * la imagen pixel a pixel se realiza el sumatorio de x y de y y se le aplica la raiz cuadrada de la suma de los cuadrados de los sumatorios de x y de y y esta será la 
 * magnitud aplicada como color a la imagen destino.
 * @author Raúl Ruano Narváez
 */
public class SobelOPRRN extends BufferedImageOpAdapter{

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        if (src == null) {
            throw new NullPointerException("src image is null");
        }
        
        if (dest == null) {
            dest = createCompatibleDestImage(src, null);
        }
        
        Kernel sobelX = KernelProducer.createKernel(KernelProducer.TYPE_SOBELX_3x3);
        Kernel sobelY = KernelProducer.createKernel(KernelProducer.TYPE_SOBELY_3x3);
        
        ConvolveOp copX = new ConvolveOp(sobelX);
        ConvolveOp copY = new ConvolveOp(sobelY);
        
        BufferedImage imageSobelX = copX.filter(src, null);
        BufferedImage imageSobelY = copX.filter(src, null);
        
        BufferedImagePixelIterator iteradorX = new BufferedImagePixelIterator(imageSobelX);
        BufferedImagePixelIterator iteradorY = new BufferedImagePixelIterator(imageSobelY);
        
        while(iteradorX.hasNext()){
            BufferedImagePixelIterator.PixelData pX = iteradorX.next();
            BufferedImagePixelIterator.PixelData pY = iteradorY.next();
            
            int sumatorioX = 0;
            int sumatorioY = 0;
            
            for(int i = 0; i<pX.numSamples;i++){
                sumatorioX += pX.sample[i];
                sumatorioY += pY.sample[i];
            }
            
            int magnitud = (int)Math.hypot(sumatorioX, sumatorioY);
            magnitud = ImageTools.clampRange(magnitud, 0, 255);
            
            Color c = new Color(magnitud, magnitud, magnitud);
            dest.setRGB(pX.col, pX.row, c.getRGB());
        }
        
        return dest;
    }
    
}
