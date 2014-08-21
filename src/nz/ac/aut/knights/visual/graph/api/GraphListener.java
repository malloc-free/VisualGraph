/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.api;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - Created 7/9/13
 * 
 * Interface for listeners for the GraphView.
 */
public interface GraphListener {
    
    /**
     * Alert this GraphListener
     * 
     * @param message 
     */
    public void alertGraphListener(GraphMessage message);
}
