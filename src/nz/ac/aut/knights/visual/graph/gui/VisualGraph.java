/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui;

import java.awt.Color;
import nz.ac.aut.knights.visual.graph.api.GraphControl;
import nz.ac.aut.knights.visual.graph.api.GraphControlListener;
import nz.ac.aut.knights.visual.graph.api.GraphMessage;
import nz.ac.aut.knights.visual.graph.api.GraphMessage.EnumKeyValue;
import nz.ac.aut.knights.visual.graph.api.GraphModelCommand;
import nz.ac.aut.knights.visual.graph.api.GraphViewCommand;

/**
 *
 * @author michael
 */
public class VisualGraph {

    private String currentName;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        final GraphFrame frame = new GraphFrame();
        GraphControl control = GraphControl.getControl(false);
        control.addGraphListenerMap(frame.getCommands());
        
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.createAndShow();
            }
        });
        
        addVertex("1");
        addVertex("2");
        addEdge("1", "2");
        addVertex("3");
        addEdge("2", "3");
        
        control.addGraphListener(GraphModelCommand.M_VERTEX_ADD.toString(), 
                new GraphControlListener(){

            @Override
            public void alertControlListener(GraphMessage message) {
                final String name = (String)message.get(GraphModelCommand.M_VERTEX_NAME.toString());
                final double xPos = (double)message.get(GraphModelCommand.M_VERTEX_X_POS.toString());
                final double yPos = (double)message.get(GraphModelCommand.M_VERTEX_Y_POS.toString());
                
                sendMessage(GraphViewCommand.VERTEX_ADD, new EnumKeyValue(){{
                    keyEnum = GraphViewCommand.VERTEX_NAME;
                    object = name;
                }}, new EnumKeyValue(){{
                    keyEnum = GraphViewCommand.VERTEX_X_POS;
                    object = xPos;
                }}, new EnumKeyValue() {{
                    keyEnum = GraphViewCommand.VERTEX_Y_POS;
                    object = yPos;
                }}
                
                );
            }
        
        });
        
    }
    
    public interface CallBack{
        public void modified(Enum modification, EnumKeyValue... values);
    }
    
    public static void sendMessage(Enum command, EnumKeyValue... values){
        GraphMessage messageTemp = new GraphMessage(command);
        messageTemp.add(values);
        GraphControl.getControl(false).alertGraphListener(messageTemp);
    }
    
    public static void sendMessage(Enum command, Enum mod, Object o){
        GraphMessage messageTemp = new GraphMessage(command);
        messageTemp.add(mod, o);
        GraphControl.getControl(false).alertGraphListener(messageTemp);
    }
    
    public static void modifyColor(final Color color, final String name){
        GraphMessage messageTemp = new GraphMessage(GraphViewCommand.VERTEX_MODIFY_COLOR);
        
        messageTemp.add(new EnumKeyValue[]{
            new EnumKeyValue(){{ keyEnum = GraphViewCommand.VERTEX_COLOR; 
            object = color.getColorComponents(null); }},
            
            new EnumKeyValue(){{ keyEnum = GraphViewCommand.VERTEX_NAME;
            object = name; }}
        });
        
        GraphControl.getControl(false).alertGraphListener(messageTemp);
    }
    
    public static void modifyEdgeColor(final Color color, final String vOne,
            final String vTwo){
        GraphMessage messageTemp = new GraphMessage(GraphViewCommand.EDGE_MODIFY);
        
        messageTemp.add(new EnumKeyValue[]{
            new EnumKeyValue(){{ keyEnum = GraphViewCommand.EDGE_COLOR; 
            object = color.getColorComponents(null); }},
            
            new EnumKeyValue(){{ keyEnum = GraphViewCommand.EDGE_VERTEX_ONE;
            object = vOne; }},
                
            new EnumKeyValue(){{ keyEnum = GraphViewCommand.EDGE_VERTEX_TWO;
            object = vTwo;
            }}
        });
        
        GraphControl.getControl(false).alertGraphListener(messageTemp);
    }
    
    
    public static void removeVertex(final String name){
        GraphMessage messageTemp = new GraphMessage(GraphViewCommand.VERTEX_DELETE);
        
        messageTemp.add(new EnumKeyValue(){{
            keyEnum = GraphViewCommand.VERTEX_NAME; object = name;
        }});
        
        GraphControl.getControl(false).alertGraphListener(messageTemp);
    }
    
    public static void addVertex(final String name){
        GraphMessage messageTemp = new GraphMessage(GraphViewCommand.VERTEX_ADD);
        
        messageTemp.add(new EnumKeyValue[]{
            new EnumKeyValue(){{ keyEnum = GraphViewCommand.VERTEX_NAME; object = name;}}
        });
        
        GraphControl.getControl(true).alertGraphListener(messageTemp);
    }
    
    public static void addEdge(final String nameOne, final String nameTwo){
         GraphMessage messageTemp = new GraphMessage(GraphViewCommand.EDGE_ADD);
        
        messageTemp.add(new EnumKeyValue[]{
            new EnumKeyValue(){{ keyEnum = GraphViewCommand.EDGE_VERTEX_ONE; object = nameOne;}},
            new EnumKeyValue(){{ keyEnum = GraphViewCommand.EDGE_VERTEX_TWO; object = nameTwo;}}
        });

        GraphControl.getControl(true).alertGraphListener(messageTemp);
    }
}
