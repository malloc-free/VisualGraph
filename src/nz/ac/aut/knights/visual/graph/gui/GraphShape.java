/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 11/09/2013 - Created
 * 
 * Provides an interface for all shapes to be used in the GraphPanel
 */
public abstract class GraphShape implements Comparable<GraphShape>{
    
    private int layer;
    private boolean selected;
    
    /**
     * Gets the shape of this GraphShape
     * @return 
     */
    public abstract Shape getShape();
    
    /**
     * Gets the color of this shape.
     * @return 
     */
    public abstract Color getColor();
    
    /**
     * Set the color of this shape.
     * @param color 
     */
    public abstract void setColor(Color color);
    /**
     * Get the layer that the shape exits on.S
     * @return - The layer for this shape.
     */
    public int getLayer(){
        return layer;
    }
    
    /**
     * Set the layer for this shape.
     * @param layer - The layer to set.
     */
    public void setLayer(int layer){
        this.layer = layer;
    }
    
    /**
     * Test to see if this shape is selected.
     * @return - True if this shape has been selected.
     */
    public boolean isSelected(){
        return selected;
    }
    
    /**
     * Set the value of selected to true.
     * @param selected 
     */
    public void setSelected(boolean selected){
        this.selected = selected;
    }
    
    /**
     * @see java.lang.Comparable
     */
    @Override
    public int compareTo(GraphShape o){
        return layer - o.layer;
    }
    
    public String getName(){
        return null;
    }
    
    /**
     * Test to see if this shape has a label.
     * @return 
     */
    public boolean hasLabel(){
        return false;
    }
    
    public String getLabel(){
        return null;
    }
    
    public boolean hasText(){
        return false;
    }
    
    public double getTextXLocation(){
        return 0;
    }
    
    public boolean isFilled(){
        return false;
    }
    
    public void setFilled(boolean filled){
        
    }
    
    public void setText(String text){
        
    }
    
    public String getText(){
        return "";
    }
    
    public double getTextYLocation(){
        return 0;
    }
    
    public double getLabelXLocation(){
        return 0;
    }
    
    public double getLabelYLocation(){
        return 0;
    }
    
    public void updatePoint(Point2D.Double point){
        
    }
    
    public void modifyPoint(double xMov, double yMov){
        
    }
    
    public abstract boolean containsPoint(Point point);
    
}
