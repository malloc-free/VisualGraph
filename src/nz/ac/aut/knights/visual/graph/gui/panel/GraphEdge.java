/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui.panel;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import static java.lang.Math.*;
import nz.ac.aut.knights.visual.graph.gui.GraphShape;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 - Created 10/9/2013
 * @version 0.2 - 11/09/13 Added Color, line trimming.
 */
public class GraphEdge extends GraphShape{
    
    //The first vertex
    private GraphVertex vertexOne;
    //The second vertex
    private GraphVertex vertexTwo;
    //The name for this edge - derived from the names of the vertices
    private String name; 
    //The color for this edge
    private Color color;
    //The layer for this shape
    private int layer;
    //Specifies if this edge is directed or not.
    private boolean isDirected;
    //The text for the edge
    private String text;
    //Determines if this edge should be directed
    private boolean directed;
    
    /**
     * Constructor for the edge.
     * 
     * @param vertexOne - The first vertex for the edge.
     * @param vertexTwo - The second vertex for the edge.
     */
    public GraphEdge(GraphVertex vertexOne, GraphVertex vertexTwo){
        this.vertexOne = vertexOne;
        this.vertexTwo = vertexTwo;
        name = generateName(vertexOne.getName(), vertexTwo.getName());
        color = Color.BLACK;
        layer = 0;
        text = "";
        directed = true;
    }
   
    /**
     * Generate the edge name from the given vertices.
     * @param nameOne - Name of the first vertex.
     * @param nameTwo - Name of the second vertex.
     * @return - The name of the edge.
     */
    public static String generateName(String nameOne, String nameTwo){
        return nameOne + " - " + nameTwo;
    }
    
    /**
     * Get the color for this edge.
     * @return - The color of this edge.
     */
    @Override
    public Color getColor(){
        return color;
    }
    
    /**
     * Set the edge to directed or not.
     * @param directed - True if the edge is to be directed.
     */
    public void setDirected(boolean directed){
        this.directed = directed;
    }
    
    /**
     * True if the vertex is directed.
     * @return 
     */
    public boolean getDirected(){
        return directed;
    }
    
    /**
     * Set the color for this edge.
     * @param color - The color to set.
     */
    @Override
    public void setColor(Color color){
        this.color = color;
    }
    
    /**
     * Get the name for this edge.
     * 
     * @return - The name of this edge. 
     */
    @Override
    public String getName(){
        return name;
    }
    
    /**
     * 
     * @param text 
     */
    @Override
    public void setText(String text){
        this.text = text;
    }
    
    @Override
    public String getText(){
        return text;
    }
    
    @Override
    public boolean hasText(){
       boolean retVal = true;
       if(text.equals("")){
           retVal = false;
       }
       
       return retVal;
    }
    
    public Arrow getArrow(){
        Arrow arrow = new Arrow();
        
        double xDirV1 = 1;
        double yDirV1 = 1;
        double xDirV2 = 1;
        double yDirV2 = 1;
        double compressX = 1;
        double compressY = 1;
        double diaV1 = vertexOne.getDiameter();
        double diaV2 = vertexTwo.getDiameter();
        
        VertexCoords co = getVertexCoords();
        
        co.xV1 += (diaV1 / 2);
        co.yV1 += (diaV1 / 2);
        co.xV2 += (diaV2 / 2);
        co.yV2 += (diaV2 / 2);
        
        if(co.xV1> co.xV2){
            xDirV1 = -1;
        }
        else{
            xDirV2 = -1;
        }
        
        if(co.yV1 > co.yV2){
            yDirV1 = -1;
        }
        else{
            yDirV2 *= -1;
        }
        
        double x = (co.xV2 * xDirV2) + (co.xV1 * xDirV1);
        double y = (co.yV2 * yDirV2) + (co.yV1 * yDirV1);
        double theta = atan(y / x);
        
        co.yV2 += (sin(theta) * (diaV2 / 2)) * yDirV2;
        co.yV1 += (sin(theta) * (diaV1 / 2)) * yDirV1;
        co.xV2 += (cos(theta) * (diaV2 / 2)) * xDirV2; 
        co.xV1 += (cos(theta) * (diaV1 / 2)) * xDirV1;
        
        double dX = co.xV2 - co.xV1;
        double dY = co.yV2 - co.yV1;
        double dzMod = sqrt(pow(dX, 2) + pow(dY, 2)) / 10;
        double xpt = co.xV2 - (dX / dzMod);
        double ypt = co.yV2 - (dY / dzMod);
        
        dY /= compressY;
        dX /= compressX;
        
        double hp1X = (xpt + (dY / dzMod));
        double hp1Y = (ypt - (dX / dzMod));
        double hp2X = (xpt - (dY / dzMod));
        double hp2Y = (ypt + (dX / dzMod));
        
        arrow.lineOne = new Line2D.Double(co.xV1, co.yV1, co.xV2, co.yV2);
        arrow.headOne = new Line2D.Double(hp1X, hp1Y, co.xV2, co.yV2);
        arrow.headTwo = new Line2D.Double(hp2X, hp2Y, co.xV2, co.yV2);
        arrow.point = new Ellipse2D.Double(xpt, ypt, 2, 2);
        
        return arrow;
    }
    
    /**
     * Small class that represents an arrow for this edge.
     */
    public static class Arrow{
        Line2D.Double lineOne;
        Line2D.Double headOne;
        Line2D.Double headTwo;
        Ellipse2D.Double point;
    }
    
    public VertexCoords getVertexCoords(){
        return  new VertexCoords(){{
            xV1 = vertexOne.getPoint().x;
            yV1 = vertexOne.getPoint().y;
            xV2 = vertexTwo.getPoint().x;
            yV2 = vertexTwo.getPoint().y;
            dY = yV2 - yV1;
            dX = xV2 - xV1;
        }};
    }
    
    public class VertexCoords{
        double xV1;
        double xV2;
        double yV1;
        double yV2;
        double dY;
        double dX;
    }
    
    @Override
    public double getTextXLocation(){
        VertexCoords co = getVertexCoords();
        return (co.xV1 + (co.dX / 2)) + (vertexOne.getDiameter() / 2);
    }
    
    @Override
    public double getTextYLocation(){
        VertexCoords co = getVertexCoords();
        return (co.yV1 + (co.dY / 2)) + (vertexTwo.getDiameter() / 2);
    }

    /**
     * @see nz.ac.aut.knights.visual.graph.gui.GraphShape
     */
    @Override
    public Shape getShape() {
        return getArrow().lineOne;
    }

    /**
     * @see nz.ac.aut.knights.visual.graph.gui.GraphShape
     */
    @Override
    public boolean containsPoint(Point point) {
        return getShape().contains(point);
    }

}
