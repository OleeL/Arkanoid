/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author sgolegg
 */
  
public class Ball {
    private float radius;
    private final static float DEFAULTSPEED = 100f;
    private float speed = DEFAULTSPEED;
    private float x;
    private float y;
    private String name;
    
    public Ball(int x, int y)
    {
        this.name = "ball";
        this.x = x;
        this.y = y;
    }
        
    public float getRadius()
    {
        return radius;
    }

    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }
    
    public String getName()
    {
        return name;
    }
        
    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void setRadius(float r)
    {
        this.radius = r;
    }

    public void bounce()
    {  
        speed = -speed;
    }
    
    public void resetSpeed()
    {
        speed = DEFAULTSPEED;
    }
    
    public void update(float dt)
    {
        x = x + (speed * dt);
        y = y + (speed * dt);
    }
   
}
