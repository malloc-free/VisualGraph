/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.api;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - Created 7/9/13
 */
public abstract class GraphAbstractFrame extends JFrame implements GraphControlListener,
        GraphListener{
    
    //Set for GraphViewListeners
    private Set<GraphListener> listeners;
    //Set for GraphInternalListeners
    private Set<GraphInternalListener> internalListeners;
    
    /**
     * Constructor for GraphAbstractFrame.
     */
    public GraphAbstractFrame(){
        listeners = new HashSet<>();
        internalListeners = new HashSet<>();
    }
    
    /**
     * Add a GraphViewListener.
     * @param l - The listener to add.
     * @return - True if added.
     */
    public boolean addViewListener(GraphListener l){
        return listeners.add(l);
    }
    
    /**
     * Remove a GraphViewLister.
     * @param l - The listener to remove.
     * @return - True if the listener was removed.
     */
    public boolean removeViewListener(GraphListener l){
        return listeners.remove(l);
    }
    
    /**
     * Alert all GraphViewListeners
     * @param m - The message to pass.
     */
    protected void alertViewListeners(GraphMessage m){
        for(GraphListener l : listeners){
            l.alertGraphListener(m);
        }
    }
    
    /**
     * Add an internal listener - used for dialogs/panels.
     * 
     * @param l - The listener to add.
     * @return - True if the listener was added.
     */
    public boolean addInternalListener(GraphInternalListener l){
        return internalListeners.add(l);
    }
    
    /**
     * Remove an internal listener - used for dialogs/panels.
     * @param l - The listener to remove.
     * @return - True if the listener was removed.
     */
    public boolean removeInternalListener(GraphInternalListener l){
        return internalListeners.remove(l);
    }
    
    /**
     * Alert all listeners.
     * @param m 
     */
    protected void alertInternalListeners(GraphMessage m){
        for(GraphInternalListener l : internalListeners){
            l.alert(m);
        }
    }
    
    /**
     * Receive messages from control.
     * 
     * @param message 
     */
    @Override
    public void alertControlListener(GraphMessage message) {
        
    }

    /**
     * Receive messages from 
     * @param message 
     */
    @Override
    public void alertGraphListener(GraphMessage message) {
        
    }
    
    /**
     * Provides a mechanism for the bulk retrieval of commands from 
     * the gui.
     * 
     * @return - A mad of commands to listeners. 
     */
    public Map<String, GraphControlListener> getCommands(){
        return null;
    };
    
}
