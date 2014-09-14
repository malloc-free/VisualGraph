/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static java.lang.System.out;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - created 7/9/13 
 * 
 * Provides a message hub between components in the GraphPackage.
 */
public abstract class GraphControl implements GraphListener{
    public enum ControlType{
        ASYNC, SYNC;
    }
    
    //The map of the command-listeners.
    Map<String, Set<GraphControlListener>> viewListeners;
    //The instance of GraphControl.
    static GraphControl control;
    //Display information if required
    boolean verbose;
    //Constant for printing listener info
    public static final String LISTENER_ALERT = "\nListerer:\n%s\nnotified of "
            + "command:\n%s";
    public static final String LISTENER_INFO = "\nListener:\n%s\nhas been %s\n"
            + "to command: %s";
    
    private GraphControl(){
        verbose = false;
        viewListeners = new HashMap<>();
    }
    
    /**
     * Get the current instance of GraphControl.
     * @param verbose
     * @return 
     */
    public static GraphControl getControl(boolean verbose){
        if(control == null){
            control = new SynchronousControl();
            control.verbose = verbose;
        }
     
        return control;
    }
    
    public static GraphControl getControl(ControlType type, boolean verbose){
        if(control == null && type == ControlType.ASYNC){
            control = new AsynchronousHandler();
            Thread thread = new Thread((AsynchronousHandler)control);
            thread.start();
            
        }
        else if(control == null && type == ControlType.SYNC){
            control = new SynchronousControl();
            
        }
        else if(control == null)
            throw new IllegalArgumentException("Not correct type of control");
        
        return control;
    }
   
    
    /**
     * Add a GraphControlListener. Returns true if added.
     * 
     * @param command - The command to listen for.
     * @param l - The listener to add.
     * @return - True if added.
     */
    public boolean addGraphListener(String command, GraphControlListener l){
        boolean added = false;
        
        Set<GraphControlListener> set = viewListeners.get(command);
        
        if(set == null){
            set = new HashSet<>();
            viewListeners.put(command, set);  
        }
        
         added = set.add(l);
        
         if(verbose && added){
                out.printf(LISTENER_INFO, l, "added", command);
        }
        
        return added;
    }
    
    /**
     * Add a map of commands to listeners.
     * 
     * @param map - The map to add.
     */
    public void addGraphListenerMap(Map<String, GraphControlListener> map){
        for(Entry<String, GraphControlListener> m :map.entrySet()){
            addGraphListener(m.getKey(), m.getValue());
        }
    }
    
    
   
    private static class SynchronousControl extends GraphControl{
        /**
         * 
         * @param message 
         */
        @Override
        public void alertGraphListener(GraphMessage message) {
            Set<GraphControlListener> set = viewListeners.get(message.getCommand());

            if(set != null){
                for(GraphControlListener l : set){
                    l.alertControlListener(message);

                    if(verbose){
                        out.printf(LISTENER_ALERT, l, message.getCommand());
                    }
                }
            }
        }
    }
    
    private static class AsynchronousHandler extends SynchronousControl implements Runnable{
        
        private Queue<GraphMessage> queue = new ConcurrentLinkedQueue<>();
        
        @Override
        public void alertGraphListener(GraphMessage message) {
            queue.add(message);
            synchronized(this){ notifyAll(); }
        }

        @Override
        public void run() {
            while(!Thread.interrupted()){
                work();
            }
        }
        
        private void work(){
            synchronized(this){
                while(queue.isEmpty()){
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        System.out.println("Interrupted exception thrown!");
                    }
                }
            }

            while(!queue.isEmpty()){
                super.alertGraphListener(queue.poll());
            }
                
        }
        
    }

}
