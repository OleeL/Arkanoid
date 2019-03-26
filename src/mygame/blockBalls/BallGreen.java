/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.blockBalls;

import com.jme3.scene.Node;

/**
 *
 * @author sgolegg
 */
public class BallGreen{
    private final static String name = "ballGreen";
    private float x;
    private float y;
    private float radius;
    private boolean dead = false;
    public Node node;
    
    
    public BallGreen(int x, int y, float radius)
    {
        this.x = (float) x;
        this.y = (float) y;
        this.radius = radius;
    }
    
    public float getRadius()
    {
        return radius;
    }
    
    public static String getName()
    {
        return name;
    }
    
    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }
    
    public void kill()
    {
        dead = true;
    }
    
    public boolean isDead()
    {
        return dead;
    }
    
    // Takes an X and Y to set the coordinates of the circle
    public void setLocation(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void setRadius(float radius)
    {
        this.radius = radius;
    }
    
    public boolean circle_collided(float bx, float by, float br)
    {
        return radius+br > Math.sqrt(Math.pow(y - by, 2)+Math.pow(x - bx, 2));
    }
}
