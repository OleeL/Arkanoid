package mygame;

import com.jme3.scene.Node;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */
public class BallGreen{
    private final static String NAME = "ballGreen";
    private float x;
    private float y;
    private float radius;
    public Node node;
    private static final int SCORE = 500; // Destroying wins you 500 points
    
    
    public BallGreen(float x, float y, float radius)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    
    // Returns the radius of the ball 
    public float getRadius()
    {
        return radius;
    }
    
    // Returns the name of the ball 
    public static String getName()
    {
        return NAME;
    }
    
    // Returns the x coordinate 
    public float getX()
    {
        return x;
    }
    
    // Returns the y coordinate
    public float getY()
    {
        return y;
    }
    
    // Takes an X and Y to set the coordinates of the circle
    public void setLocation(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    // Sets the radius of the ball
    public void setRadius(float radius)
    {
        this.radius = radius;
    }
    
    // Returns whether the red ball and the green ball have collided
    public boolean circle_collided(float bx, float by, float br)
    {
        return radius+br > Math.sqrt(Math.pow(y - by, 2)+Math.pow(x - bx, 2));
    }
    
    // Returns the number of points the green ball is worth for destroying
    public static int getScore()
    {
        return SCORE;
    }
}
