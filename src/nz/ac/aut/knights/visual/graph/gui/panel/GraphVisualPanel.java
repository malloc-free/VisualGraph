/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import nz.ac.aut.knights.visual.graph.api.*;
import nz.ac.aut.knights.visual.graph.gui.GraphShape;
import nz.ac.aut.knights.visual.graph.gui.panel.GraphEdge.Arrow;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - Created 10/09/13
 * @version 0.2 - Added Color, depth.
 * 
 * This is the panel where the graph will be drawn. Uses a JLayeredPane
 * to provide multi-layers for the verticies and edges to reside on.
 */
public class GraphVisualPanel extends GraphAbstractPanel{
    
    //The layered pane used by this GraphVisualPanel.
    private final JLayeredPane layerPane;
    //The list of vertices to be painted.
    private final Map<String, GraphShape> vertices;
    //The list of edges between vertices
    private final Map<String, GraphEdge> edges;
    //The MouseAdapter used to capture mouse events
    private final GraphMouse graphMouse;
    //X co-ordinate label
    private final JLabel xOrd;
    //Y co-ordinate label
    private final JLabel yOrd;
    //The selected vertex
    private final JLabel vertex;
    //The x location currently pressed
    private final JLabel pressedX;
    //The y location currently pressed
    private final JLabel pressedY;
    //Shows if the mouse button is pressed
    private final JLabel pressed;
    //The name of the current vertex
    private final JPopupMenu popupMenu;
    //The select box
    private Rectangle2D.Double selectBox;
    //The currenly selected verticies.
    private final List<GraphVertex> selectedVertices;
    //The current selected vertex.
    private GraphVertex currentVertex;
    
    /**
     * Constructor for the panel. 
     * @param frame - The parent frame for this Panel.
     */
    public GraphVisualPanel(GraphAbstractFrame frame){
        super(frame);
        layerPane = new JLayeredPane();
        setSize(frame.getSize());
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 500));
        vertices = new HashMap<>();
        edges = new HashMap<>();
        add(layerPane, BorderLayout.CENTER);
        graphMouse = new GraphMouse();
        addMouseListener(graphMouse);
        addMouseMotionListener(graphMouse);
        
        xOrd = new JLabel("xOrd = 0");
        yOrd = new JLabel("yOrd = 0");
        vertex = new JLabel("vertex = null");
        pressedX = new JLabel("prx = 0");
        pressedY = new JLabel("pry = 0");
        pressed = new JLabel("pressed = false");
        JPanel labelPanel = new JPanel();
        labelPanel.add(xOrd);
        labelPanel.add(yOrd);
        labelPanel.add(vertex);
        labelPanel.add(pressedX);
        labelPanel.add(pressedY);
        labelPanel.add(pressed);
        add(labelPanel, BorderLayout.PAGE_START);
        popupMenu = new GraphPopupMenu();
        selectBox = null;
        selectedVertices = new ArrayList<>();
    }
    
    public Collection<GraphShape> getVertices(){
        return vertices.values();
    }
    
    /**
     * Clear all of the vertices, edges and menu options from the Panel.
     */
    public void clearAll(){
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                edges.clear();
                vertices.clear();
                ((GraphPopupMenu)popupMenu).actions.clear();
                popupMenu.removeAll();
                repaint();
            }
        });
        
    }
    /**
     * Paints the graphics for this panel.
     * 
     * @param g - The graphics object to paint on. 
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D graphics = (Graphics2D)g;
        
        for(GraphEdge e : edges.values()){
            graphics.setColor(e.getColor());
            //graphics.draw(e.getLine());
            Arrow a = e.getArrow();
            graphics.draw(a.lineOne);
            if(e.getDirected()){
                graphics.draw(a.headOne);
                graphics.draw(a.headTwo);
                graphics.draw(a.point);
            }
            
            if(e.hasText()){
                graphics.setColor(Color.black);
                graphics.drawString(e.getText(), (int)e.getTextXLocation(),
                        (int)e.getTextYLocation());
            }
        }
        
        List<GraphShape> queue = new ArrayList<>(vertices.values());
        
        Collections.sort(queue);
        
        for(GraphShape v : queue){
            graphics.setColor(v.getColor());
            if(v.isFilled())
                graphics.fill(v.getShape());
            else
                graphics.draw(v.getShape());
          
            if(v.hasText()){
                graphics.setColor(Color.BLACK);
                graphics.drawString(v.getName(), (int)v.getTextXLocation(), 
                        (int)v.getTextYLocation());
            }
            if(v.isSelected()){
                graphics.setColor(GraphVertex.getSelectedColor());
                graphics.draw(v.getShape());
            }
            if(v.hasLabel()){
                graphics.setColor(Color.BLACK);
                graphics.drawString(v.getLabel(), (int)v.getLabelXLocation(), 
                        (int)v.getLabelYLocation());
            }
            
            if(selectBox != null){
                graphics.setColor(Color.BLACK);
                graphics.draw(selectBox);
            }
        }   
    }

    /**
     * Remove the specified vertex.
     * 
     * @param name - The vertex to be removed.
     */
    public void removeVertex(String name){
        vertices.remove(name);
        this.repaint();
    }
    
    /**
     * Modify the color of the given vertex.
     * 
     * @param name - The name of the vertex to modify.
     * @param color - The color to change it to.
     */
    public void modifyVertexColor(final String name, final Color color){
        
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                GraphVertex v = (GraphVertex)vertices.get(name);
                v.setColor(color);
                repaint();
            }
        });
    }
    
    public void modifyVertexLabel(final String name, final String label){
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                GraphVertex v = (GraphVertex)vertices.get(name);
                v.setLabel(label);
                repaint();
            }
        });
    }
    
    public void modifyEdgeText(final String name, final String text){
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                GraphEdge e = edges.get(name);
                e.setText(text);
                repaint();
            }
        });
    }
    
    public void modifyEdgeDir(final String name, final boolean dir){
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                GraphEdge e = edges.get(name);
                e.setDirected(dir);
                repaint();
            }
        });
    }
    
    /**
     * Modify the color of the given edge.
     * 
     * @param name - The name of the edge to modify.
     * @param color - The color to change it to.
     */
    public void modifyEdgeColor(final String name, final Color color){
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                edges.get(name).setColor(color);
                repaint();
            }
        });
    }
    
    /**
     * Add a vertex to this Panel.
     * 
     * @param vertex - The vertex to add. 
     */
    public void addVertex(final GraphVertex vertex){
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                vertices.put(vertex.getName(), vertex);

                repaint();            }
        });
      
    }
    
    /**
     * Modify a vertex in this panel.
     * 
     * @param name - The name of the vertex to change.
     * @param point - The point to move the vertex to.
     */
    public void moveShape(String name, Point2D.Double point){
        
        GraphShape v = vertices.get(name);
        
        if(v != null){
            v.updatePoint(point);     
        }
        
        this.repaint();
    }
    
    /**
     * Add an edge to this panel.
     * 
     * @param edge - The edge to add. 
     */
    public void addEdge(final GraphEdge edge){
        
        edges.put(edge.getName(), edge);
       
    }
    
    public void addEdge(final String vOne, final String vTwo){
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                GraphVertex vertexOne = (GraphVertex)vertices.get(vOne);
                GraphVertex vertexTwo = (GraphVertex)vertices.get(vTwo);
                GraphEdge edge = new GraphEdge(vertexOne, vertexTwo);
                edges.put(edge.getName(), edge);
            }
        });
    }
    
    /**
     * Get a vertex from the supplied name.
     * 
     * @param name - The name of the vertex to get.
     * @return - The vertex - if it exists.
     */
    public GraphShape getVertex(String name){
        return vertices.get(name);
    }
    
    /**
     * Remove the edge by the given name.
     * 
     * @param name 
     */
    public void removeEdge(String name){
        edges.remove(name);
        
        repaint();
    }
    
    /**
     * Move all vertices the specified amount.
     * 
     * @param xMov - The distance along the x axis to move.
     * @param yMov - The distance along the y axis to move.
     */
    public void moveAllVertices(double xMov, double yMov){
        for(GraphShape v : vertices.values()){
            v.modifyPoint(xMov, yMov);
        }
        
        this.repaint();
    }
    
    /**
     * Test to see if the given point is within any of the 
     * vertices. If so, return the name of the first vertex 
     * found.
     * 
     * @param point - The point to test.
     * @return - A vertex or null.
     */
    private GraphVertex selectedVertex(Point point){
        GraphVertex retV = null;
        
        for(GraphShape v : vertices.values()){
            if(v.containsPoint(point)){
                retV = (GraphVertex)v;
                break;
            }
        }
        
        return retV;
    }
   
    /**
     * Set the vertices within the select box to selected.
     */
    private void selectedVertices(){   
        for(GraphShape s : vertices.values()){
            GraphVertex v = (GraphVertex)s;
            if(selectBox.contains(v.getCircle().getBounds2D())){
                selectedVertices.add(v);
                setVertexSelected(v);
            }
        }
    }
    
    /**
     * Move the selected vertices.
     * @param point - The point of reference to move by.
     */
    private void moveSelectedVerticies(double x, double y){
        
        for(GraphVertex v : selectedVertices){
            
            v.modifyPoint(x, y);
        }
        
        this.repaint();
    }
    
    /**
     * Deselect all currently selected vertices.
     */
    private void deselectVertices(){
        for(GraphVertex v : selectedVertices){
            resetVertex(v);
        }
        
        selectedVertices.clear();
    }
    
    /**
     * Reset the supplied vertex.
     * 
     * @param v - The vertex to reset.
     */
    private void resetVertex(GraphVertex v){
        //HACK ALERT!!!!! Still have to find out why this breaks stuff.
        if(v != null){
            v.setSelected(false);
            v.setLayer(5);
        }
    }
   
    /**
     * Set the supplied vertex selected.
     * 
     * @param v - The vertex to set.
     */
    private void setVertexSelected(GraphVertex v){
        v.setLayer(10);
        v.setSelected(true);
    }
  
    /**
     * 
     * @param message 
     */
    @Override
    public void alert(GraphMessage message) {
        
    }
    
    public void addGraphAction(String command, MenuAction action){
        ((GraphPopupMenu)popupMenu).addMenuAction(command, action);
    }

    public void modifyVertexFill(final String name, final boolean filled) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                vertices.get(name).setFilled(filled);
                repaint();
            }
        });
        
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    private class GraphPopupMenu extends JPopupMenu implements ActionListener{
        
        Map<String, MenuAction> actions = new HashMap<>();
        
        public GraphPopupMenu() {
            JMenuItem delete = new JMenuItem("Delete");
            add(delete);
            
            delete.addActionListener(this);
        }
        
        public void addMenuAction(final String command, final MenuAction action){
            
            
            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    actions.put(command, action);
                    JMenuItem temp = new JMenuItem(command);
                    temp.addActionListener(GraphPopupMenu.this);
                    temp.setActionCommand(command);
                    add(temp);
                }
            });
        }
        
        private void setAllEnabled(boolean set){
            for(Component c : this.getComponents()){
                    if(c instanceof JMenuItem)
                        c.setEnabled(set);
                }
        }

       
        @Override
        public void actionPerformed(ActionEvent e) {
            MenuAction action = actions.get(e.getActionCommand());
            if(action != null) {
                action.action(e.getActionCommand(), currentVertex.getName());
            }
            else {
                System.out.println("No action set for command");
            }
        }
        
        @Override
        public void show(Component compontent, int x , int y){
            if(currentVertex == null){
                setAllEnabled(false);
            }
            else
                setAllEnabled(true);
            
            super.show(compontent, x, y);
        }
    }
    
    public interface MenuAction{
        public void action(String command, String vertexName);
    }
    
    /**
     * This internal class handles the mouse events for this Panel
     */
    private class GraphMouse extends MouseAdapter{
        //Determine if the mouse is pressed.
        boolean mousePressed;
        //The x value where the selected vertex was grabbed
        double selectBoxX;
        double selectBoxY;
        
        Point lastPoint;
        int mouseButton;
        boolean groupSelection;
        boolean groupAction;
        /**
         * Constructor sets up fields.
         */
        GraphMouse(){
            currentVertex = null;
            mousePressed = false;
            mouseButton = 0;
            groupSelection = false;
            groupAction = false;
        }
        
        /**
         * @see java.awt.event.MouseAdapter
         * @param e 
         */
        @Override
        public void mousePressed(MouseEvent e){
            mouseButton = e.getButton();
            
            switch(mouseButton){
                case MouseEvent.BUTTON3 : button2Pressed(e); break;
                case MouseEvent.BUTTON2 : 
                case MouseEvent.BUTTON1 : buttonPressed(e); break;
            }
        }
        
        /**
         * Perform this if a mouse button is clicked.
         * 
         * @param e - The mouse event 
         */
        public void buttonPressed(MouseEvent e){
            lastPoint = e.getPoint();  
           
            currentVertex = selectedVertex(lastPoint);
            
            if(currentVertex == null) {
                groupSelection = false;
                groupAction = false;
                deselectVertices();
                selectBox = new Rectangle2D.Double(lastPoint.x, lastPoint.y, 1, 1);
                selectBoxX = lastPoint.x;
                selectBoxY = lastPoint.y;
            }
            else if(!groupSelection || !selectedVertices.contains(currentVertex)) {
                deselectVertices();
                setVertexSelected(currentVertex);
                selectedVertices.add(currentVertex);
            }
          
            mousePressed = true;
            updatePressedLabels(lastPoint);
            repaint();
        }
        
        /**
         * Perform this if the right mouse button is pressed.
         * 
         * @param e - The mouse event 
         */
        public void button2Pressed(MouseEvent e){
            buttonPressed(e);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
            groupAction = groupSelection;
        }
      
        /**
         * @see java.awt.event.MouseAdapter
         * @param e 
         */
        @Override
        public void mouseReleased(MouseEvent e){
            mousePressed = false;
            mouseButton = 0;
            updatePressedLabels(new Point(0,0));
            
            if(selectBox != null){
                if(!selectedVertices.isEmpty()) {
                    groupSelection = true;
                }
                
                selectBox = null;
            }
            else if(!groupAction){
                deselectVertices();
                
                if(currentVertex != null) {
                    setVertexSelected(currentVertex);
                    selectedVertices.add(currentVertex);
                }
            }
            
            groupAction = false;
            currentVertex = null;
            
            repaint();
        }
        
        /**
         * @see java.awt.event.MouseAdapter
         * @param e 
         */
        @Override
        public void mouseMoved(MouseEvent e){
            Point p = e.getPoint();
            updateLabels(p);
            
            if(mousePressed){
                updatePressedLabels(p);
            }
        }
        
        /**
         * @see java.awt.event.MouseAdapter
         * @param e 
         */
        @Override
        public void mouseDragged(MouseEvent e){
            Point p = e.getPoint();
            double xTrans = p.x - lastPoint.x;
            double yTrans = p.y - lastPoint.y;
            lastPoint = p;
            
            if(currentVertex != null && mouseButton == MouseEvent.BUTTON1){            
                moveSelectedVerticies(xTrans, yTrans);
                groupAction = groupSelection;
            }     
            else if(selectBox != null){
                changeSelectBox(p);
                deselectVertices();
                selectedVertices();
                repaint();
            }
            
            updatePressedLabels(p);
        }
        
        /**
         * Change the size of the selection box.
         * 
         * @param p - The point to change it to. 
         */
        private void changeSelectBox(Point p){
            double leftX;
            double rightX;
            double leftY;
            double rightY;
            
            if(selectBox.x > p.x){
                leftX = selectBoxX;
                rightX = p.x;
            } else {
                leftX = p.x;
                rightX = selectBoxX;
            }
            
            if(selectBox.y > p.y){
                leftY = selectBoxY;
                rightY = p.y;
            } else {
                leftY = p.y;
                rightY = selectBoxY;
            }
            
            selectBox.setFrameFromDiagonal(leftX, leftY, 
                        rightX, rightY);
        }
        
        /**
         * Update the various labels in the gui with the latest
         * information.
         * 
         * @param update - The current point. 
         */
        private void updatePressedLabels(final Point update){
            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    pressedX.setText("prX = " + update.x);
                    pressedY.setText("prY = " + update.y);
                    pressed.setText("pressed = " + mousePressed);
                    if(currentVertex != null){
                        vertex.setText("vertex = " + currentVertex.getName());
                    }
                }
            });
        }
        
        /**
         * Update the various labels in the gui with the latest information.
         * 
         * @param update - The current point.
         */
        private void updateLabels(final Point update){
            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    xOrd.setText("xOrd = " + update.x);
                    yOrd.setText("yOrd = " + update.y);       
                }
            });
        }
    }
}
