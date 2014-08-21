/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.api;

import javax.swing.JPanel;

/**
 *
 * @author michael
 */
public abstract class GraphAbstractPanel extends JPanel implements GraphInternalListener{
    
    protected GraphAbstractFrame frame;
    
    public GraphAbstractPanel(GraphAbstractFrame frame){
        this.frame = frame;
    }
    
}
