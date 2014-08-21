/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui;

import java.awt.Color;
import nz.ac.aut.knights.visual.graph.api.GraphControl;
import nz.ac.aut.knights.visual.graph.api.GraphMessage;
import nz.ac.aut.knights.visual.graph.api.GraphMessage.EnumKeyValue;
import static nz.ac.aut.knights.visual.graph.api.GraphViewCommands.*;

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
        
//        Board board = new Board(7, 7);
//        Knight k = new Knight();
//        board.generateMoves(k);
//        
//        k.setFirstSquare(board.getSquare(new Position(0, 0)));
//        
//        addVertex(k.getCurrentSquare().toString());
//        
//        CallBack back = new CallBack(){
//
//            @Override
//            public void modified(Enum modification, EnumKeyValue... values) {
//                sendMessage(modification, values);
//            }  
//        };
//        
//        while(true){
//            Thread.sleep(1000);
//        }
//       
        
        addVertex("1");
        addVertex("2");
        addEdge("1", "2");
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
        GraphMessage messageTemp = new GraphMessage(VERTEX_MODIFY_COLOR);
        
        messageTemp.add(new EnumKeyValue[]{
            new EnumKeyValue(){{ keyEnum = VERTEX_COLOR; 
            object = color.getColorComponents(null); }},
            
            new EnumKeyValue(){{ keyEnum = VERTEX_NAME;
            object = name; }}
        });
        
        GraphControl.getControl(false).alertGraphListener(messageTemp);
    }
    
    public static void modifyEdgeColor(final Color color, final String vOne,
            final String vTwo){
        GraphMessage messageTemp = new GraphMessage(EDGE_MODIFY);
        
        messageTemp.add(new EnumKeyValue[]{
            new EnumKeyValue(){{ keyEnum = EDGE_COLOR; 
            object = color.getColorComponents(null); }},
            
            new EnumKeyValue(){{ keyEnum = EDGE_VERTEX_ONE;
            object = vOne; }},
                
            new EnumKeyValue(){{ keyEnum = EDGE_VERTEX_TWO;
            object = vTwo;
            }}
        });
        
        GraphControl.getControl(false).alertGraphListener(messageTemp);
    }
    
    
    public static void removeVertex(final String name){
        GraphMessage messageTemp = new GraphMessage(VERTEX_DELETE);
        
        messageTemp.add(new EnumKeyValue(){{
            keyEnum = VERTEX_NAME; object = name;
        }});
        
        GraphControl.getControl(false).alertGraphListener(messageTemp);
    }
    
    public static void addVertex(final String name){
        GraphMessage messageTemp = new GraphMessage(VERTEX_ADD);
        
        messageTemp.add(new EnumKeyValue[]{
            new EnumKeyValue(){{ keyEnum = VERTEX_NAME; object = name;}}
        });
        
        GraphControl.getControl(true).alertGraphListener(messageTemp);
    }
    
    public static void addEdge(final String nameOne, final String nameTwo){
         GraphMessage messageTemp = new GraphMessage(EDGE_ADD);
        
        messageTemp.add(new EnumKeyValue[]{
            new EnumKeyValue(){{ keyEnum = EDGE_VERTEX_ONE; object = nameOne;}},
            new EnumKeyValue(){{ keyEnum = EDGE_VERTEX_TWO; object = nameTwo;}}
        });

        GraphControl.getControl(true).alertGraphListener(messageTemp);
    }
}
