/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui.panel;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import nz.ac.aut.knights.visual.graph.gui.GraphShape;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - 10/9/13 Created.
 * @version 0.2 - 11/9/13 Added Color.
 * 
 * Represents a graph vertex.
 */
public class GraphVertex extends GraphShape{
    private Point2D.Double point;
    private String name;
    private String label;
    private double diameter;
    private boolean selected;
    private Color color;
    private static Color selectedColor = Color.RED;
    private static Color defaultColor = Color.BLACK;
    private static Double defaultDiameter = 10d;
    private static double defaultX = 0;
    private static double defaultY = 0;
    private static double defaultXOff = 10;
    private static double defaultYOff = 10;
    private static VertexPlacer placer = new DefaultPlacer();
    private boolean isFilled;
    
    /**
     * Constructor takes values for the x and y co-ordinates of the 
     * vertex, and the diameter of the vertex.
     * 
     * @param x - The x co-ordinate for this vertex;
     * @param y - The y co-ordinate for this vertex;
     * @param diameter - The diameter of the vertex;
     */
    public GraphVertex(String name, Double x, Double y, Double diameter){
        this(name, new Point2D.Double(x, y), diameter);
    }
    
    /**
     * The most minimal of the constructors, relies on default values to 
     * construct the vertices.
     * 
     * @param name - The name of the vertex. 
     */
    public GraphVertex(String name){
        this(name, placer.nextX, placer.nextY, defaultDiameter);
     
        placer.getNextPosition();
        
    }
    
    private static class DefaultPlacer extends VertexPlacer{

        @Override
        public void getNextPosition() {
            nextX += defaultXOff;
            nextY += defaultYOff;
        }
        
    }
    
    public static void resetPlacement(){
        placer.nextX = 20;
        placer.nextY = 50;
    }
    
    /**
     * Constructor takes name, point values.
     * 
     * @param name - The name of the vertex.
     * @param x - The x value of the point.
     * @param y - The y value of the point.
     */
    public GraphVertex(String name, Double x, Double y){
        this(name, x, y, defaultDiameter);
    }
    
    /**
     * This constructor takes a Point2D object, and the diameter to 
     * create the vertex.
     * 
     * @param point - The point for this vertex.
     * @param diameter - The diameter of the vertex.
     */
    public GraphVertex(String name, Point2D.Double point, Double diameter){
        label = "";
        isFilled = false;
        this.point = point;
        this.diameter = diameter;
        this.name = name;
        color = defaultColor;
    }
    
    /**
     * Create the vertex using a name, x and y values, and color parameters.
     * @param name
     * @param x
     * @param y
     * @param colorParams 
     */
    public GraphVertex(String name, Double x, Double y, float[] colorParams){
        this.name = name;
        point = new Point2D.Double(x, y);
        color = new Color(colorParams[0], colorParams[1], colorParams[2]);
        diameter = defaultDiameter;
        label = "";
        isFilled = false;
    }
    
    /**
     * An interface that can specify the default placement of vertices.
     */
    public static abstract class VertexPlacer{
        protected double nextX;
        protected double nextY;
        
        public abstract void getNextPosition();
    }
    
    /**
     * Set the default placer for vertices.
     * 
     * @param vPlacer - The placer to set. 
     */
    public static void setVertexPlacer(VertexPlacer vPlacer){
        placer = vPlacer;
    }
    
    /////////////// Static Methods for GraphVertex /////////////
    
    /**
     * Sets the default color for all GraphVertices
     * @param color - The default color to set.
     */
    public static void setDefaultColor(Color color){
        defaultColor = color;
    }
    
    /**
     * Set the color to be used when a vertex is selected.
     * @param color 
     */
    public static void setSelectedColor(Color color){
        selectedColor = color;
    }
    
    /**
     * Set the default diameter for all GraphVertex's
     * @param diameter - The default diameter to set.
     */
    public static void setDefaultDiameter(double diameter){
        defaultDiameter = diameter;
    }
    
    /**
     * Get the selected color for vertices.
     * 
     * @return - The selected color. 
     */
    public static Color getSelectedColor(){
        return selectedColor;
    }
    /**
     * Sets this vertex to be selected.
     * 
     * @param selected - True if the vertex is selected. 
     */
    @Override
    public void setSelected(boolean selected){
        this.selected = selected;
    }
    
    /**
     * Get the point for this vertex.
     * @return 
     */
    public Point2D.Double getPoint(){
        return point;
    }
    
    /**
     * Get the color for this vertex.
     * 
     * @return - The color of this vertex.
     */
    @Override
    public Color getColor(){
        return color;
    }
    
    /**
     * Set the color for this vertex.
     * @param color - The color to set.
     */
    public void setColor(Color color){
        this.color = color;
    }
    
    /**
     * Move the point in this vertex by the specified amount.
     * 
     * @param xMov - The amount to move along the x axis.
     * @param yMov - The amount to move along the y axis.
     */
    @Override
    public void modifyPoint(double xMov, double yMov){
        point.x = point.x + xMov;
        point.y = point.y + yMov;
    }
    
    /**
     * Sets the diameter of the vertex.
     * 
     * @param diameter - The new diameter of the vertex;
     */
    public void setDiameter(Double diameter){
        this.diameter = diameter;
    }
    
    /**
     * Get the diameter for this vertex;
     * 
     * @return 
     */
    public double getDiameter(){
        return diameter;
    }
    /**
     * Updates the point for this vertex.
     * 
     * @param point - The new point. 
     */
    @Override
    public void updatePoint(Point2D.Double point){
        this.point = point;
    }
    
    /**
     * Returns true if this vertex is currently selected.
     * @return - True if selected.
     */
    @Override
    public boolean isSelected(){
        return selected;
    }
    
    /**
     * Updates the point for this vertex. 
     * 
     * @param x - The new x value.
     * @param y - The new y value.
     */
    public void updatePoint(double x, double y){
        point.x = x;
        point.y = y;
    }
    
    /**
     * Test if the given point is within the circle.
     * 
     * @param point - The point to test.
     * @return - True if the point is within this vertex.
     */
    @Override
    public boolean containsPoint(Point point){
        return getCircle().contains(point);
    }
    
    /**
     * Get the name of this vertex.
     * @return - The vertex name.
     */
    @Override
    public String getName(){
        return name;
    }
    
    /**
     * Get the label for this edge.
     * @return - The label for this edge.
     */
    @Override
    public String getLabel(){
        return label;
    }
    
    /**
     * If the label for this vertex has been set, return true.
     * @return - True if a label has been set.
     */
    @Override
    public boolean hasLabel(){
        return (label.equals("")) ? false : true;
        
    }
    
    /**
     * Set the label for this vertex.
     * @param label 
     */
    public void setLabel(String label){
        this.label = label;
    }
    
    /**
     * Get the shape of this vertex.
     * 
     * @return - Ellipse2D representation of this vertex. 
     */
    public Ellipse2D.Double getCircle(){
        return new Ellipse2D.Double(point.x, point.y, diameter, diameter);
    }

    /**
     * Get the x position for the vertex text.
     * @return - The x position.
     */
    @Override
    public double getTextXLocation(){
        return point.x + diameter;
    }
    
    /**
     * Get the y position for the vertex text.
     * @return - The y position.
     */
    @Override
    public double getTextYLocation(){
        return point.y + diameter;
    }
    
    /**
     * Get the x position for the vertex label;
     * @return 
     */
    @Override
    public double getLabelXLocation(){
        return point.x + diameter;
    }
    
    /**
     * Get the y position for the vertex label;
     * @return 
     */
    @Override
    public double getLabelYLocation(){
        return point.y;
    }

    /**
     * @see nz.ac.aut.knights.visual.graph.gui.GraphShape
     */
    @Override
    public Shape getShape() {
        return getCircle();
    }
    
    @Override
    public boolean isFilled(){
        return isFilled;
    }
    
    /**
     * Set this vertex to be rendered as filled.
     * @param filled 
     */
    public void setFilled(boolean filled){
        isFilled = true;
    }
    
    /**
     * 
     * @see nz.ac.aut.knights.visual.graph.gui.GraphShape
     */
    @Override
    public boolean hasText(){
        return true;
    }
    
}
