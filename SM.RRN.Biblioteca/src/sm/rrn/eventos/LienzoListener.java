/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sm.rrn.eventos;

import java.util.EventListener;

/**
 *
 * @author raulr
 */
public interface LienzoListener extends EventListener{
    public void shapeAdded(LienzoEvent evt);
    public void updateLienzo(LienzoEvent evt);
    public void updateCoord(LienzoEvent evt);
    public void updateCoordEditable(LienzoEvent evt);
}
