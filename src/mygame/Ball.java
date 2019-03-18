/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import mygame.blocks.Block;

/**
 *
 * @author sgolegg
 */
  
public class Ball {
    private float radius;
    private final static float DEFAULTSPEED = 100f;
    private float speed = DEFAULTSPEED;
    public float speed_x = DEFAULTSPEED;
    public float speed_y = DEFAULTSPEED;
    public float x;
    public float y;
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
        speed_y = -speed_y;
        
        System.out.println("running");
    }
    
    public void resetSpeed()
    {
        speed = DEFAULTSPEED;
        speed_x = DEFAULTSPEED;
        speed_y = DEFAULTSPEED;
    }
    
    public void update(float dt)
    {
        x = (x + (speed_x * dt));
        y = (y + (speed_y * dt));
    }
   
        // Circle and block euclideans collision in (lecture 16).
    public static boolean checkCollisionBlock(Ball ball, Block block ) 
    {
        // temporary variables to set edges for testing
        float testX = ball.getX();
        float testY = ball.getY();

        // which edge is closest?
        if (ball.getX() < block.getX())         
            testX = block.getX();                              // test left edge
        else if (ball.getX() > block.getX()-Block.getWidth()) 
            testX = block.getX()-Block.getWidth();             // right edge
        if (ball.getY() < block.getY())
            testY = block.getY();                              // top edge
        else if (ball.getY() > block.getY()-Block.getHeight()) 
            testY = block.getY()-Block.getHeight();            // bottom edge

        // get distance from closest edges
        double distX = Math.max(ball.getX(), testX) - Math.min(ball.getX(), testX);
        double distY = Math.max(ball.getY(), testY) - Math.min(ball.getY(), testY);
        
        // Normalise
        double distance = Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the radius, collision!
        if (distance <= ball.getRadius()) {
            return true;
        }
        return false;
    }
    
//    //     Circle and block euclideans collision in (lecture 16).
//    public static boolean checkCollisionPaddle(Ball ball, double x, double y, double w, double h) 
//    {
//        return Circle_Rect_Intersect(ball.getX(), ball.getY(), ball.getRadius(), x, y, w, h);
//    }
//    
//    //check if circle and rectangle intersect
//    public static boolean Circle_Rect_Intersect(double cX,double cY, double cR, double rX, double rY, double rW, double rH)
//    {
//            if (Point_Rect_Intersect(cX, cY, rX, rY, rW, rH))
//                return true;
//            else
//            {
//                double closestX = clamp(cX, rX, rX + rW);
//                double closestY = clamp(cY, rY, rY + rH);
//                double distX = cX - closestX;
//                double distY = cY - closestY;
//                double sqrDist = (distX * distX) + (distY * distY);
//                return sqrDist < cR * cR;
//            }
//    }
//
//    //Check if point is inside rectangle
//    public static boolean Point_Rect_Intersect(double pX, double pY, double rX, double rY, double rW, double rH)
//    {
//        return (pX > rX && pX < rX + rW && pY > rY && pY < rY + rH);
//    }
//    public static double clamp(double v, double min, double max)
//    {    
//        if (v < min)
//            return min;
//        else if (v > max) 
//            return max;
//        else 
//            return v;
//    }
    
    public static boolean checkCollisionPaddle(Ball ball, double x, double y, double w, double h) 
    {
        // temporary variables to set edges for testing
        double testX = ball.getX();
        double testY = ball.getY();

        // which edge is closest?
        if (ball.getX() < x)
            testX = x;
        else if (ball.getX() > x-w)
            testX = x-w;
        if (ball.getY() < y)
            testY = y;
        else if (ball.getY() > y-h)
            testY = y-h;
        
        // get distance from closest edges
        double distX = Math.max(ball.getX(), testX) - Math.min(ball.getX(), testX);
        double distY = Math.max(ball.getY(), testY) - Math.min(ball.getY(), testY);
        double distance = Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the radius, collision!
        if (distance <= ball.getRadius()) {
            return true;
        }
        return false;
    }
}
