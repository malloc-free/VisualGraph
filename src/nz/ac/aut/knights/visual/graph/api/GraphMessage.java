/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.api;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - Created 7/9/13
 * 
 * The messages used by the model-control-view for communication.
 */
public class GraphMessage {
    
    //Contains the command - object map for this message.
    private Map<String, Object> messages;
    private String command;
    
    /**
     * Constructor for GraphMessage takes enum for the command.
     * @param command 
     */
    public GraphMessage(Enum command){
        this(command.toString());
    }
    
    /**
     * Constructor takes string command.
     * @param command
     */
    public GraphMessage(String command){
        messages = new HashMap<>();
        this.command = command;
        messages = new HashMap<>();
    };
    
    public GraphMessage(String command, KeyValue keyValue){
        this(command);
        messages.put(keyValue.key, keyValue.object);
    }
    
    public GraphMessage(String command, EnumKeyValue keyValue){
        this(command);
        messages.put(keyValue.keyEnum.toString(), keyValue.object);
    }
    
    /**
     * Get the map in this message.
     * 
     * @return - The map of command-objects. 
     */
    public Map<String, Object> getMap(){
        return messages;
    }
    
    /**
     * Return the command for this message.
     * @return - The command for this message.
     */
    public String getCommand(){
        return command;
    }
    
    /**
     * A quick way to add to the message using an Enum.
     * 
     * @param command - The enum to add.
     * @param object - The object to map it to.
     */
    public void add(Enum command, Object object){
        messages.put(command.toString(), object);
    }
    
    /**
     * Gets the specified object for the given command.
     * 
     * @param command
     * @return 
     */
    public Object get(String command){
        return messages.get(command);
    }
    
    /**
     * Add command pairs from an array.
     * 
     * @param pairs - The pairs to add. 
     */
    public void add(KeyValue... pairs){
        for(KeyValue c : pairs){
            messages.put(c.key, c.object);
        }
    }
    
    public void add(EnumKeyValue... enumPairs){
        for(EnumKeyValue p : enumPairs){
            messages.put(p.keyEnum.toString(), p.object);
        }
    }
    /**
     * A tiny class to enable the addition of command-object pairs 
     * quickly.
     */
    public static class KeyValue{
        public String key;
        public Object object;
    }
    
    /**
     * A tiny class to enable the addition of Enum command-object pairs
     * quickly.
     */
    public static class EnumKeyValue{
        public Enum keyEnum;
        public Object object;
    }
    
    /**
     * Add a command using a string and the associated object.
     * @param command
     * @param object 
     */
    public void add(String command, Object object){
        messages.put(command, object);
    }
}
