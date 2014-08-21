/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.api;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - 7/9/13 Created
 * 
 * An abstract class for graph models.
 */
public abstract class GraphAbstractModel implements GraphListener, GraphControlListener{
    
    //The set of listeners for this model.
    private Set<GraphListener> listeners;
    
    /**
     * Default constructor for GraphAbstractModel
     */
    public GraphAbstractModel(){
        listeners = new HashSet<>();
    }
    
    /**
     * @see nz.ac.aut.knights.visual.graph.api.GraphControlListener
     * @param m 
     */
    @Override
    public void alertControlListener(GraphMessage m){ }
    
    /**
     * @see nz.ac.aut.knights.visual.graph.api.GraphListener
     * @param m 
     */
    @Override
    public void alertGraphListener(GraphMessage m){ }
    
}
