/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.api;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - Created 7/09/13
 */
public interface GraphInternalListener {
    
    //Alert the internal listener.
    public void alert(GraphMessage message);
}
