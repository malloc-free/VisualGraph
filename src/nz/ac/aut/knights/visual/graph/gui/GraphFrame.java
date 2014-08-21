/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import nz.ac.aut.knights.visual.graph.gui.panel.GraphVisualPanel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import nz.ac.aut.knights.visual.graph.api.GraphAbstractFrame;
import nz.ac.aut.knights.visual.graph.api.GraphControl;
import nz.ac.aut.knights.visual.graph.api.GraphControlListener;
import nz.ac.aut.knights.visual.graph.api.GraphMessage;
import static nz.ac.aut.knights.visual.graph.api.GraphViewCommands.*;
import static nz.ac.aut.knights.visual.graph.api.GraphModelCommands.*;
import nz.ac.aut.knights.visual.graph.gui.panel.GraphEdge;
import nz.ac.aut.knights.visual.graph.gui.panel.GraphVertex;
import nz.ac.aut.knights.visual.graph.gui.panel.GraphVisualPanel.MenuAction;
import nz.ac.aut.knights.visual.graph.gui.panel.VertexSelectDialog;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - 7/9/13 - Created.
 * 
 * The main frame for the GUI.
 */
public class GraphFrame extends GraphAbstractFrame{
    
    //The GraphVisualPanel used to draw the graph. 
    private GraphVisualPanel graphVisualPanel;
    //The map of commands to listeners.
    private Map<String, GraphControlListener> commands;
    //The current diameter of vertices
    private double diameter;
    //The x margin for vertices
    private static final int xMargin = 20;
    //The y margin for vertices
    private static final int yMargin = 50;
    //The minimum default distance between vertices
    private static final int vDist = 100;
    //The maximum horizontal distance between first and last vertex.
    private static final int maxVDist = 8 * 100;
 
    public GraphFrame(){
        setSize(800, 600);
        graphVisualPanel = new GraphVisualPanel(this);
        add(graphVisualPanel);
        diameter = 30d;
        setLocationRelativeTo(null);
        GraphVertex.setDefaultDiameter(30);
        setupMenuBar();
        GraphVertex.setVertexPlacer(new GraphVertex.VertexPlacer() {
            {
                nextX = 20;
                nextY = 50;
            }
            
            @Override
            public void getNextPosition() {
                nextX += vDist;
                
                if(nextX > maxVDist){
                    nextX = 20;
                    nextY += vDist;
                } 
            }
        });
        
    }
    
    /**
     * Setup the menubar for the frame.
     */
    private void setupMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Actions");
        JMenuItem loadDirected = new JMenuItem("Load directed");
        JMenuItem loadUnDirected = new JMenuItem("Loid undirected");
        JMenuItem loadRandom = new JMenuItem("Load random");
        JMenuItem clearAll = new JMenuItem("Clear all");
        
        mainMenu.add(loadDirected);
        loadDirected.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getFile(DIRECTED.toString());
            }
        });
        
        mainMenu.add(loadUnDirected);
        loadUnDirected.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getFile(UNDIRECTED.toString());
            }
        });
        
        mainMenu.add(loadRandom);
        loadRandom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getFile(RANDOM.toString());
            }
        });
        
        mainMenu.add(clearAll);
        clearAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                modelClearAll();
            }
        });
        
        menuBar.add(mainMenu);
        this.setJMenuBar(menuBar);
        
    }
    
    private void getFile(String type){
        
        String s = null;
        
        if(!type.equals(RANDOM.toString())){ 
              s  = (String)JOptionPane.showInputDialog(this, 
                "Please enter the name of the file to open");
        }
        
        boolean pass = (s != null && !s.equals(""));
        pass = pass || type.equals(RANDOM.toString());
        
        if(pass){
            GraphMessage message = new GraphMessage(OPEN_FILE);
            message.add(OPEN_FILE_NAME, s);
            message.add(OPEN_FILE_TYPE, type);
        
            GraphControl.getControl(false).alertGraphListener(message);
        }
    }
    
    /**
     * Create and show the frame.
     */
    public void createAndShow(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /**
     * @see nz.ac.aut.knights.visual.graph.api.GraphControlListener
     * @param message 
     */
    @Override
    public void alertControlListener(GraphMessage message) {
        
    }
    
    /**
     * Get the map of commands for the graph frame.
     * 
     * @return - The map of commands. 
     */
    @Override
    public Map<String, GraphControlListener> getCommands(){
        if(commands == null){
            commands = new HashMap<>();
            setUpCommands();
        }
        
        return commands;
    }
    
    /**
     * Setup the commands that will be passed when getCommands is called.
     */
    private void setUpCommands(){
        commands.put(VERTEX_ADD.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                createVertex(message);
                //ddVertexReflective(message);
            }
        });
        
        commands.put(VERTEX_DELETE.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                graphVisualPanel.removeVertex((String)message.get(VERTEX_NAME.toString()));
            }
        });
        
        commands.put(EDGE_DELETE.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        commands.put(VERTEX_MOVE.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                moveVertex(message);
            }
        });
        
        commands.put(VERTICES_MOVE.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                moveVertices(message);
            }
        });
        
        commands.put(EDGE_ADD.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                addEdge(message);
            }
        });
        
        commands.put(VERTEX_MODIFY_COLOR.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                modifyVertexColor(message);
            }
        });
        
        commands.put(EDGE_MODIFY.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                modifyEdgeColor(message);
            }
        });
        
        commands.put(VERTEX_MODIFY_LABEL.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                modifyVertexLabel(message);
            }
        });
        
        commands.put(SET_MODEL_COMMAND.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                addModelCommand(message);
            }
        });
        
        commands.put(VERTEX_MODIFY_FILL.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                modifyVertexFill(message);
            }
        });
        
        commands.put(EDGE_MODIFY_TEXT.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                modifyEdgeText(message);
            }

            
        });
        
        commands.put(EDGE_MODIFY_DIR.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                modifyEdgeDir(message);
            }

          
        });
        
        commands.put(CLEAR_ALL_VIEW.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                clearAll();
            }
        });
        
        commands.put(DISPLAY_MESSAGE.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                displayMessage(message);
            }
        });
        
        commands.put(GET_SELECTION.toString(), new GraphControlListener() {

            @Override
            public void alertControlListener(GraphMessage message) {
                makeSelection(message);
            }
        });
    }
    
    /**
     * Creates a new selection dialog.
     * @param message 
     */
    private void makeSelection(GraphMessage message){
        List<String> exclusion = (List<String>)message.get(EXCLUSION_LIST.toString());
        String retCommand = (String)message.get(RETURN_COMMAND.toString());
        List<GraphShape> shape = new ArrayList<>(graphVisualPanel.getVertices());
        String title = (String)message.get(MESSAGE.toString());
        
        for(String s : exclusion){
            shape.remove(graphVisualPanel.getVertex(s));
        }
        
        VertexSelectDialog.display(this, shape, retCommand, title);
    }
    
    /**
     * Send a clear command to the model.
     */
    private void modelClearAll(){
        GraphMessage message = new GraphMessage(CLEAR_ALL_MODEL);
        GraphControl.getControl(false).alertGraphListener(message);
    }
    
    private void clearAll(){
        graphVisualPanel.clearAll();
        GraphVertex.resetPlacement();
    }
    
    /**
     * Modify the edge directionality.
     * 
     * @param message 
     */
    private void modifyEdgeDir(GraphMessage message) {
        String name = getEdgeName(message);
        boolean dir = (boolean)message.get(EDGE_DIRECTION.toString());
        graphVisualPanel.modifyEdgeDir(name, dir);
    }
    
    /**
     * Extract the name of the edge from the message.
     * @param message
     * @return 
     */
    private String getEdgeName(GraphMessage message){
        String vOne = (String)message.get(EDGE_VERTEX_ONE.toString());
        String vTwo = (String)message.get(EDGE_VERTEX_TWO.toString());
       return GraphEdge.generateName(vOne, vTwo);
    }
    
    /**
     * Modify the text for the edge.
     * @param message 
     */
    private void modifyEdgeText(GraphMessage message) {
        String name = getEdgeName(message);
        String label = (String)message.get(EDGE_TEXT.toString());
        graphVisualPanel.modifyEdgeText(name, label);
    }
    /**
     * 
     * @param message 
     */
    private void modifyVertexFill(GraphMessage message){
        String name = (String)message.get(VERTEX_NAME.toString());
        boolean filled = (boolean)message.get(VERTEX_FILL.toString());
        graphVisualPanel.modifyVertexFill(name, filled);
    }
    
    /**
     * Add a command to one of the menus in the visual panel.
     * @param message 
     */
    private void addModelCommand(GraphMessage message){
        
        String command = (String)message.get(R_CLICK_MENU.toString());
        
        graphVisualPanel.addGraphAction(command, new MenuAction(){
            
            @Override
            public void action(String command, String vertexName) {
                alertModel(command, vertexName);
            }
        });
    }
    
    /**
     * Alert the model of events.
     * @param command
     * @param vertexName 
     */
    private void alertModel(String command, String vertexName){
        
        final GraphMessage messageTwo = new GraphMessage(command);
        messageTwo.add(VERTEX_NAME, vertexName);
        GraphControl.getControl(false).alertGraphListener(messageTwo);
    }
    
    private void modifyVertexLabel(GraphMessage message){
        String name = (String)message.get(VERTEX_NAME.toString());
        String label = (String)message.get(VERTEX_LABEL.toString());
        
        graphVisualPanel.modifyVertexLabel(name, label);
    }
    
    /**
     * Add an edge according to the specifications in the message.
     * 
     * @param message - The message with the information. 
     */
    private void addEdge(GraphMessage message){
        String nameOne = (String)message.get(EDGE_VERTEX_ONE.toString());
        String nameTwo = (String)message.get(EDGE_VERTEX_TWO.toString());
//        
//        GraphVertex vOne = (GraphVertex)graphVisualPanel.getVertex(nameOne);
//        GraphVertex vTwo = (GraphVertex)graphVisualPanel.getVertex(nameTwo);
//        
//        GraphEdge edge = new GraphEdge(vOne, vTwo);
        
        //graphVisualPanel.addEdge(edge);
        graphVisualPanel.addEdge(nameOne, nameTwo);
    }
    
    /**
     * Remove an edge using the given message.
     * 
     * @param message 
     */
    private void removeEdge(GraphMessage message){
        String nameOne = (String)message.get(EDGE_VERTEX_ONE.toString());
        String nameTwo = (String)message.get(EDGE_VERTEX_TWO.toString());
        
        String edgeName = GraphEdge.generateName(nameOne, nameTwo);
        
        graphVisualPanel.removeEdge(edgeName);
    }
    /**
     * Add a vertex using reflection.
     * @param message - The message to get the information from.
     */
    private void addVertexReflective(GraphMessage message){
        ObjectBuilder builder = ObjectBuilder.getBuilder(message.getCommand());
        GraphVertex v = (GraphVertex)builder.buildClass(message);
        graphVisualPanel.addVertex(v);
    }
    
    /**
     * Move all vertices in the specified direction.
     * 
     * @param message - The message with all of the specified information. 
     */
    private void moveVertices(GraphMessage message){
        double x = (double)message.get(VERTEX_X_POS.toString());
        double y = (double)message.get(VERTEX_Y_POS.toString());
        
        graphVisualPanel.moveAllVertices(x, y);
    }
    
    /**
     * Move a vertex to the given point.
     * 
     * @param message - The message with all of the required information.
     */
    private void moveVertex(GraphMessage message){
        String name = (String)message.get(VERTEX_NAME.toString());
        double x = (double)message.get(VERTEX_X_POS.toString());
        double y = (double)message.get(VERTEX_Y_POS.toString());
        
        Point2D.Double point = new Point2D.Double(x, y);
        
        graphVisualPanel.moveShape(name, point);
    }
    
    /**
     * Called if the message received asks for vertex color modification.
     * @param message 
     */
    private void modifyVertexColor(GraphMessage message){
        String name = (String)message.get(VERTEX_NAME.toString());
        float[] colorVals = (float[])message.get(VERTEX_COLOR.toString());
        Color color = new Color(colorVals[0], colorVals[1], colorVals[2]);
        graphVisualPanel.modifyVertexColor(name, color);
    }
    
    /**
     * Called to modify an edge color.
     * 
     * @param message 
     */
    private void modifyEdgeColor(GraphMessage message){
        String nameOne = (String)message.get(EDGE_VERTEX_ONE.toString());
        String nameTwo = (String)message.get(EDGE_VERTEX_TWO.toString());
        float[] colorVals = (float[])message.get(EDGE_COLOR.toString());
        Color color = new Color(colorVals[0], colorVals[1], colorVals[2]);
        String edgeName = GraphEdge.generateName(nameOne, nameTwo);
        graphVisualPanel.modifyEdgeColor(edgeName, color);
    }
    /**
     * Create and add a vertex to the graph panel.
     * 
     * @param message - The message from which to create the vertex. 
     */
    private void createVertex(GraphMessage message){
        String name = (String)message.get(VERTEX_NAME.toString());
        Object xOb = message.get(VERTEX_X_POS.toString());
        Object yOb = message.get(VERTEX_Y_POS.toString());
        
        GraphVertex vertex;
        if(xOb != null && yOb != null)
            vertex = new GraphVertex(name, (double)xOb, (double)yOb, diameter);
        else
            vertex = new GraphVertex(name);
        
        float[] colorVals;
        
        if((colorVals = (float[])message.get(VERTEX_COLOR.toString())) != null){
            Color color = new Color(colorVals[0], colorVals[1], colorVals[2]);
            vertex.setColor(color);
            
        }
        
        graphVisualPanel.addVertex(vertex);
    }
    
    /**
     * Displays a plain dialog with the specified title and message.
     * @param message 
     */
    private void displayMessage(GraphMessage message){
        String diaMessage = (String)message.get(MESSAGE.toString());
        String diaTitle = (String)message.get(TITLE.toString());
        JOptionPane.showMessageDialog(this, diaMessage, diaTitle, JOptionPane.PLAIN_MESSAGE);
    }
    
    private void displayErrorMessage(GraphMessage message){
        
    }
    
    /**
     * Dialog to create a random graph.
     */
    private class RandomDialog extends JDialog{
        JTextField maxValue;
        JTextField minValue;
        JTextField maxOutgoing;
        JTextField minOutgoing;
        
        RandomDialog(JFrame parent){
            super(parent);
            maxValue = new JTextField();
            minValue = new JTextField();
            JComboBox directed = new JComboBox(new String[]{"Directed", "Undirected"});
        }
    }
}
