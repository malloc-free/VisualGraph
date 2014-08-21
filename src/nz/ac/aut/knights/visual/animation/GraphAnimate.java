/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.animation;

import java.util.LinkedList;
import java.util.Queue;
import static java.lang.Math.*;

/**
 *
 * @author Michael Holmwood
 * @version 0.1 16/09/2013 - Created
 * 
 * This is a runnable class that can be used to animate vertices in the 
 * graphView.
 */
public class GraphAnimate implements Runnable{
    //The time it takes to move between positions.
    private double delay;
    //The list of points to visit.
    private Queue<Point> points;
    //The name of the vertex to animate.
    private String vertexName;
    //The current point for the vertex.
    private Point currentPoint;
    //The next point the vertex will move to.
    private Point nextPoint;
    
    private double dx;
    private double dy;
    
    private double distance;
    /**
     * Constructor for GraphAnimate. 
     * 
     * @param vertexName - The name of the vertex to animate.
     * @param delay - The time it takes to move between positions.
     */
    public GraphAnimate(String vertexName, double delay){
        this.vertexName = vertexName;
        points = new LinkedList<>();
        this.delay = delay;
    }
    
    /**
     * @see java.lang.Runnable
     */
    @Override
    public void run() {
        
    }
    
    /**
     * Add a point to this GraphAnimate
     */
    public void addPoint(Point p){
        points.add(p);
    }
    
    /**
     * The point class used in GraphAnimate
     */
    public static class Point{
        double x;
        double y;
        
        @Override
        public boolean equals(Object o){
            if(o instanceof Point)
                return (x == ((Point)o).x && y == ((Point)o).y);
            
            return false;
        }
    }
    
    /**
     * Move to the next position.
     */
    public void moveNext(){
        if(currentPoint == null){
            currentPoint = points.poll();
            points.add(currentPoint);
            nextPoint = points.poll();
            points.add(nextPoint);
        }
        else if(currentPoint.equals(nextPoint)){
            currentPoint = nextPoint;
            nextPoint = points.poll();
            points.add(nextPoint);
            
            dx = nextPoint.x - currentPoint.x;
            dy = nextPoint.y - currentPoint.y;
            
            distance = sqrt((dx *= dx) + (dy *= dy));
        }
    }
}
