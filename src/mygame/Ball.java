/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Spatial;

/**
 *
 * @author sgolegg
 */
  
public class Ball {
    private static final int PLAYING = 0;
    private static final int RESPAWNING = 1;
    private static final int PAUSED = 2;
    private static final int STARTING = 3;
    private static final float TIMER_DURATION = 2f;
    private static final float DEFAULTSPEED = 200f;
    private static final float SPEED_INCREMENTOR = 17;
    
    private float radius;
    private float speed = DEFAULTSPEED;
    private float speed_x = DEFAULTSPEED;
    private float speed_y = DEFAULTSPEED;
    private float x;
    private float y;
    
    private int state = 1;
    private int previous_state;
    private int points;
    
    private float timer = TIMER_DURATION;
    private float resume_speed_x;
    private float resume_speed_y;
    private String name;
    public Spatial spatial;
    
    public Ball(float x, float y, float r)
    {
        this.name = "ball";
        this.x = x;
        this.y = y;
        this.radius = r;
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
    
    public void setSpeedX(float spd)
    {
        speed_x = spd;
    }
    
    public void setSpeedY(float spd)
    {
        speed_y = spd;
    }
    
    public float getSpeedX()
    {
        return speed_x;
    }
    
    public float getSpeedY()
    {
        return speed_y;
    }
    
    public float getSpeed()
    {
        return speed;
    }
    
    public int getState()
    {
        return state;
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

    public void bounce_y()
    {  
        speed_y = -speed_y;
    }
    
    public void bounce_x()
    {  
        speed_x = -speed_x;
    }
    
    public void increasePoints(String brick_killed)
    {
        switch (brick_killed)
        {
            case "brick1" :
                points+=100;
                break;
            case "brick2" :
                points+=200;
                break;
            case "brick3" :
                points+=300;
                break;
            case "brick4" :
                points+=400;
                break;
            case "brick5" :
                points+=500;
                break;
        }
    }
    
    public void pause()
    {
        switch (state)
        {
            case PAUSED:
                speed_x = resume_speed_x;
                speed_y = resume_speed_y;
                state = previous_state;
                break;
            default:
                resume_speed_x = speed_x;
                resume_speed_y = speed_y;
                previous_state = state;
                state = PAUSED;
                break;
        }

    }
    
    public void increaseSpeed()
    {
        if (speed_x > 0)
            speed_x += SPEED_INCREMENTOR;
        else
            speed_x -= SPEED_INCREMENTOR;
        if (speed_y > 0)
            speed_y += SPEED_INCREMENTOR;
        else
            speed_y -= SPEED_INCREMENTOR;
        speed += SPEED_INCREMENTOR;
    }
    
    public void direction(float px)
    {
        // Distance formulae
        float d = (x - px) / 60;
        speed_x = d * speed;
    }
    
    public void resetSpeed()
    {
        speed = DEFAULTSPEED;
        speed_x = DEFAULTSPEED;
        speed_y = DEFAULTSPEED;
    }
    
    public void update(
            float dt, 
            int screenWidth, 
            int screenHeight, 
            float wallWidth, 
            float wallHeight,
            float mouse_x,
            float mouse_y)
    {
        switch (state){
            case PLAYING :
                x = (x + (speed_x * dt));
                y = (y + (speed_y * dt));

                if (x+radius >= screenWidth-wallWidth){
                    speed_x = -Math.abs(speed_x);
                }
                else if (x-radius <= wallWidth){
                   speed_x = Math.abs(speed_x);
                }
                if (y+radius >= screenHeight - wallHeight){
                    speed_y = -Math.abs(speed_y);
                    Main.paddle.hit = false;
                }
                else if (y <= 0-(radius*2))
                {
                    state = RESPAWNING;
                    resetSpeed();
                    Main.paddle.hit = false;
                }
                break;
            case RESPAWNING :
                x = Main.paddle.x;
                y = Main.paddle.y +(Main.paddle.h/ 2) + radius - 5;
                speed_x = 0;
                
                timer = timer - dt;
                if (timer < 0)
                {
                    resetTimer();
                    state = PLAYING;
                }
                break;
            case PAUSED :
                break;
                
        }
    }
    
    private void resetTimer()
    {
        timer = TIMER_DURATION;
    }
    
    private void restart_location()
    {
        setPosition(400,100);
    }
    
    public static boolean Circle_Rect_Intersect(
            float cX,
            float cY, 
            float cR, 
            float rX, 
            float rY, 
            float rW, 
            float rH){
        if (Point_Rect_Intersect(cX, cY, rX, rY, rW, rH)){
            return true;
        }
        else
        {
            float closestX = clamp(cX, rX, rX - rW);
            float closestY = clamp(cY, rY, rY - rH);
            float distX = cX - closestX;
            float distY = cY - closestY;
            float sqrDist = (distX * distX) + (distY * distY);
            return sqrDist < cR * cR;
        }
    }

    //Check if point is inside rectangle
    private static boolean Point_Rect_Intersect(float pX, float pY, float rX, float rY, float rW, float rH)
    {
        return pX < rX && pX > rX - rW && pY < rY && pY > rY - rH;
    }

    private static float clamp(float v, float min, float max)
    {
        if (v < min)
            return min;
        else 
            if (v > max)
                return max;
            else 
                return v;
    }
}
