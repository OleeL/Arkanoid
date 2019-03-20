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
    private final static float DEFAULTSPEED = 200f;
    private float speed = DEFAULTSPEED;
    private float speed_incrementor = 14;
    public float speed_x = DEFAULTSPEED;
    public float speed_y = DEFAULTSPEED;
    private float starting_x;
    private float starting_y;
    private int points;
    public float x;
    public float y;
    
    private int state = 1;
    private int previous_state;
    private final int PLAYING = 0;
    private final int RESPAWNING = 1;
    private final int PAUSED = 2;
    private final int STARTING = 3;
    
    private final float TIMER_DURATION = 2f;
    private float timer = TIMER_DURATION;
    private int PAUSE_SPEED = 0;
    private float resume_speed_x;
    private float resume_speed_y;

    private String name;
    
    public Ball(float x, float y, float r)
    {
        this.name = "ball";
        this.x = x;
        this.y = y;
        this.starting_x = x;
        this.starting_y = y;
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
    
    public float getSpeedX()
    {
        return speed_x;
    }
    
    public float getSpeedY()
    {
        return speed_y;
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
            speed_x += speed_incrementor;
        else
            speed_x -= speed_incrementor;
        if (speed_y > 0)
            speed_y += speed_incrementor;
        else
            speed_y -= speed_incrementor;
    }
    
    public void direction(float px)
    {
        // Distance formulae
        float d = (x - px)*5;
        if ( speed_x > 0 )
            d = Math.min(d, Math.abs(speed_y+DEFAULTSPEED));
        else
            d = Math.max(d, -Math.abs(speed_y-DEFAULTSPEED));
        System.out.println(d +" = "+DEFAULTSPEED+" + " + speed_y);
        speed_x = speed_x + d;
    }
    
    public void resetSpeed()
    {
        speed = DEFAULTSPEED;
        speed_x = DEFAULTSPEED;
        speed_y = DEFAULTSPEED;
    }
    
    public void update(float dt, int w, int h)
    {
        switch (state){
            case PLAYING :
                x = (x + (speed_x * dt));
                y = (y + (speed_y * dt));

                if (x+radius >= w){
                    speed_x = -Math.abs(speed_x);
                    Main.hit_paddle = false;
                }
                else if (x-radius <= 0){
                   speed_x = Math.abs(speed_x);
                   Main.hit_paddle = false;
                }
                if (y+radius >= h){
                    speed_y = -Math.abs(speed_y);
                    Main.hit_paddle = false;
                }
                else if (y <= 0-(radius*2))
                {
                    state = RESPAWNING;
                    resetSpeed();
                    Main.hit_paddle = false;
                }
                break;
            case RESPAWNING :
                x = Main.paddle.getLocalTranslation().x;
                y = Main.paddle.getLocalTranslation().y + 
                        ((float) Main.paddle.getUserData("height") / 2f) +
                        radius;
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
