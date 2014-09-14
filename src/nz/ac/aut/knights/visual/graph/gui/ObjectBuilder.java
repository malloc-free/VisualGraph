/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import nz.ac.aut.knights.visual.graph.api.GraphMessage;
import nz.ac.aut.knights.visual.graph.api.GraphViewCommand;
import static nz.ac.aut.knights.visual.graph.api.GraphViewCommand.*;
import nz.ac.aut.knights.visual.graph.gui.panel.GraphEdge;
import nz.ac.aut.knights.visual.graph.gui.panel.GraphVertex;
/**
 *
 * @author michael
 */
public enum ObjectBuilder {
    VERTEX (GraphVertex.class, VERTEX_ADD, VERTEX_NAME, VERTEX_X_POS, VERTEX_Y_POS,  VERTEX_COLOR),
    EDGE (GraphEdge.class, EDGE_ADD, EDGE_VERTEX_ONE, EDGE_VERTEX_TWO);
    
    private GraphViewCommand command;
    private GraphViewCommand[] parameters;
    private Class builderClass;
    
    private ObjectBuilder(Class builderClass, GraphViewCommand command, GraphViewCommand... parameters){
        this.command = command;
        this.parameters = parameters;
        this.builderClass = builderClass;
    }
    
    /**
     * Get the appropriate builder for the required build.
     * 
     * @param - testCommand - The command used to build the object.
     * @return - The object builder for this class.
     */
    public static ObjectBuilder getBuilder(String testCommand){
        ObjectBuilder retBuilder = null;
        
        for(ObjectBuilder b : values()){
            if(b.command.equals(GraphViewCommand.valueOf(testCommand))){
                retBuilder = b;
                break;
            }
        }
        
        return retBuilder;
        
    }
    
    /**
     * Build the class from the given message.
     * 
     * @param message - The message to build the object from.
     * @return - The created object.
     */
    public Object buildClass(GraphMessage message){
       
        Object built = null;
        
        List<Object> obList = new ArrayList<>();
        
        List<Class> classList = new ArrayList<>();
        
        for(GraphViewCommand c : parameters){
            Object o;
            if((o = message.get(c.toString())) != null){
                obList.add(o);
                classList.add(o.getClass());
            }
        }
        
        Constructor con;
        
        try {
            con = builderClass.getConstructor(classList.toArray(new Class[1]));
            built = con.newInstance(obList.toArray());
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("Cannot create this object: " +
                    ex.getMessage());
        } catch (SecurityException ex) {
            throw new IllegalArgumentException("Do not have clearance to create"
                    + "this object: " + ex.getMessage());
        }
        
        
        return built;
    }
}
