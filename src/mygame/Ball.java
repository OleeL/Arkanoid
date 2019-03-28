package mygame;

import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import static mygame.Main.ball;
import static mygame.Main.blocks;
import static mygame.Main.greenBalls;
import static mygame.Main.paddle;
import static mygame.Main.score;
import mygame.blocks.Block;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */
  
public class Ball {
    private static final float DEFAULTSPEED = 200f;
    private static final int SPEED_INCREMENTOR = 35;
            
    private float radius;
    private float speed = DEFAULTSPEED;
    private float speed_x = DEFAULTSPEED;
    private float speed_y = DEFAULTSPEED;
    private float x;
    private float y;
    
    private float resume_speed_x;
    private float resume_speed_y;
    private final String name;
    public Node spatial;
    
    public Ball(float x, float y, float r)
    {
        this.name = "ball";
        this.x = x;
        this.y = y;
        this.radius = r;
    }
        
    // Gets the radius of the ball
    public float getRadius()
    {
        return radius;
    }

    // Gets the x coordinate of the ball
    public float getX()
    {
        return x;
    }
    
    // Gets the y coordinate of the ball
    public float getY()
    {
        return y;
    }
    
    // Sets the xSpeed of the ball
    public void setSpeedX(float spd)
    {
        speed_x = spd;
    }
    
    // Sets the ySpeed of the ball
    public void setSpeedY(float spd)
    {
        speed_y = spd;
    }
    
    // Gets the xSpeed of the ball
    public float getSpeedX()
    {
        return speed_x;
    }
    
    // Gets the ySpeed of the ball
    public float getSpeedY()
    {
        return speed_y;
    }
    
    // Gets the overall speed of the ball
    public float getSpeed()
    {
        return speed;
    }
    
    // Gets the name of the ball
    public String getName()
    {
        return name;
    }
        
    // Sets the position of the ball
    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
        spatial.setLocalTranslation(x, y, 0f);
    }
    
    // Sets the radius of the ball, this can be useful for enlarging the ball
    // on power up.
    public void setRadius(float r)
    {
        this.radius = r;
    }

    // Flips the ySpeed of the ball
    public void bounce_y()
    {  
        speed_y = -speed_y;
    }
    
    // Flips the xSpeed of the ball
    public void bounce_x()
    {  
        speed_x = -speed_x;
    }
    
    // Stops the ball if the game is paused
    public void pause()
    {
        switch (Main.state)
        {
            case Main.PAUSED:
                Main.state = Main.previousState;
                speed_x = resume_speed_x;
                speed_y = resume_speed_y;
                break;
            default:
                Main.state = Main.PAUSED;
                resume_speed_x = speed_x;
                resume_speed_y = speed_y;
                break;
        }
    }
    
    // Increases the speed of the ball
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
    
    // Fires the ball in a direction after hitting the paddle
    public void direction(float px)
    {
        // The further away from the ball from the centre, the more xSpeed
        // meaning that the player will have control in the direction they're
        // firing the ball
        float d = (x - px) / 100;
        speed_x = d * speed; // checks that the ball is relevant to the speed
        
        // if the ball is not moving up and down, speed up the x
        if (speed_y == 0)
            speed_y++;
        // if the ball is slowly moving up and down, speed it up 
        if (FastMath.abs(speed_y) < speed)
            speed_y = (speed_y * 1.1f);
    }
    
    // Resets the speed of the ball
    public void resetSpeed()
    {
        speed = DEFAULTSPEED;
        speed_x = DEFAULTSPEED;
        speed_y = DEFAULTSPEED;
    }
    
    // Updates the ball every frame
    public void update(float tpf, float mouse_x, float mouse_y)
    {
        switch (Main.state){
            case Main.PLAYING :
                
                // Checks if the ball is going too slow
                if (FastMath.abs(speed_y) <= DEFAULTSPEED/2)
                {
                    speed_y+=speed_y*1.1;
                }
                
                // Updates the speed of the ball
                x = (x + (speed_x * tpf));
                y = (y + (speed_y * tpf));
                
                // Makes sure that the ball is sitting in the right place
                spatial.setLocalTranslation(x, y, 0f);
                
                // Checks if the ball is colliding with other objects
                collisionBlocks(tpf);
                collisionBounds();
                collisionGreenBalls();
                collisionPaddle();
                
                // Break out of the switch state as the other conditions wont
                // Need to be met 
                break;
            case Main.RESPAWNING :
                // Makes sure that the ball is sitting in the right place
                spatial.setLocalTranslation(x, y, 0f);
                
                // Resets the position of ball, just above the paddles position
                x = Main.paddle.x;
                y = Main.paddle.y +(Main.paddle.h/ 2) + radius - 5;
                
                // If the player clicks, they fire in the direction of mouse
                if (Main.clicked)
                {
                    // Sets clicked to false again resuming to the playing state
                    Main.clicked = false;
                    Main.state = Main.PLAYING;
                    
                    // Ball is fired towards the mouse's direction
                    float direction = (float) Math.toDegrees(
                        FastMath.atan2((mouse_y-y),(mouse_x-x)));
                    
                    // Makes sure that the ball isn't fired below the paddle
                    // and makes sure that the ball can't be fired hoizontally
                    if (direction < 16 && direction > -90)
                        direction = 16f;
                    else if (direction < -90 || direction > 162)
                        direction = 162;
                    
                    // Sets the speed to the direction that they're firing
                    setSpeedX(speed * (float) Math.cos( 
                            direction * Math.PI / 180));
                    setSpeedY(speed * (float) Math.sin( 
                            direction * Math.PI / 180));
                }
                break;
            case Main.LEVEL_TRANSITION :
                // Makes sure that the ball is sitting in the right place
                spatial.setLocalTranslation(x, y, 0f);
                
        }
    }
    
    // Checks for collisions with the walls
    private void collisionBounds()
    {
        // Used to check if a wall was hit, to play a sfx
        boolean hit = false;
        
        // gets the wall widths and heights
        float wallWidth = Main.walls[0].getWidth();
        float wallHeight = Main.walls[2].getHeight();
        
        if (x+radius >= Main.SCREENWIDTH-wallWidth){ // If hit right wall
            speed_x = -Math.abs(speed_x);
            hit = true;
        }
        else if (x-radius <= wallWidth){ // If hit left wall
           speed_x = Math.abs(speed_x);
           hit = true;
        }
        if (y+radius >= Main.SCREENHEIGHT - wallHeight){ // If hit top wall
            speed_y = -Math.abs(speed_y);
            Main.paddle.hit = false;
            hit = true;
        }
        else if (y <= 0-(radius*2)) // If dead
        {
            Main.lives -= 1;
            
            // Respawns the ball and sets the game up for the respawn state
            respawn();
        }
        
        // If a wall was hit, play a sfx
        if (hit)
            Main.hit.playInstance();
    }
    
    // Checks if the ball collides with the blocks
    private void collisionBlocks(float tpf)
    {
        // Calculate collision detection for ALL the block
        for (int i=0; i < blocks.size(); i++) {
            
            // data is in this format to make it more readable
            float block_x = blocks.get(i).getX();
            float block_y = blocks.get(i).getY();
            float block_w = Block.getWidth()+12;
            float block_h = Block.getHeight()+12;
            
            // Checks for collision here 
            if ( Ball.circleRectangleCollision(
                    ball.getX(), 
                    ball.getY(), 
                    ball.getRadius(), 
                    block_x+(block_w/2), 
                    block_y+(block_h/2), 
                    block_w, 
                    block_h)) {
                
                // Checks to see if collision came from above (or below)
                // If it was, flip the ySpeed
                if ( !Ball.circleRectangleCollision(
                        ball.getX(), 
                        ball.getY()-(ball.getSpeedY()*tpf), 
                        ball.getRadius(), 
                        block_x+(block_w/2), 
                        block_y+(block_h/2), 
                        block_w, 
                        block_h)) {
                    ball.bounce_y();
                }
                // Checks to see if collision came from the sides
                // If it was, flip the xSpeed
                if ( !Ball.circleRectangleCollision(
                        ball.getX()-(ball.getSpeedX()*tpf), 
                        ball.getY(), 
                        ball.getRadius(), 
                        block_x+(block_w/2), 
                        block_y+(block_h/2), 
                        block_w, 
                        block_h)) {
                    ball.bounce_x();
                }
                
                // This runs when the object is hit
                Main.hitBreak.playInstance(); // Plays a breaking sfx
                score += blocks.get(i).getScore(); // Increments the score
                paddle.hit = false; // The paddle can now hit the ball
                
                // Removes the block from the game
                blocks.get(i).node.removeFromParent();
                blocks.remove(i);
            }
        }
    }
    
    // Checks collision between red ball and green ball
    private void collisionGreenBalls()
    {
        
        // Collision between red ball and all the green balls
        for (int i = 0; i < greenBalls.size(); i++)
        {
            if (greenBalls.get(i).circle_collided(ball.getX(), 
                    ball.getY(), 
                    ball.getRadius()))
            {               
                // Fires the red ball in the direction that is perpenicular to
                // the ball
                float direction = (float) Math.toDegrees(
                        Math.atan2(
                            (ball.getY()-greenBalls.get(i).getY()),
                            (ball.getX()-greenBalls.get(i).getX())));
                
                // Sends the ball in the correct direction given the right speed
                float direction_x = (float) Math.cos(direction * Math.PI / 180);
                float direction_y = (float) Math.sin(direction * Math.PI / 180);
                ball.setSpeedX(ball.getSpeed() * direction_x);
                ball.setSpeedY(ball.getSpeed() * direction_y);
                
                paddle.hit = false;            // The paddle can now hit the ball
                Main.hitBreak.playInstance();  // Plays a breaking sfx
                score += BallGreen.getScore(); // Increments the score
                
                // Removes the block from the game
                greenBalls.get(i).node.removeFromParent();
                greenBalls.remove(i);
            }
        }
    }
    
    // Checks for collision with paddle and ball
    private void collisionPaddle()
    {
        /*
         *  If collides, bounce the ball, increase the speed, hit in a direction
         *  play a sound effect, set paddle to hit, so it can't hit again before
         *  it hits any other object
        */        
        if ( Ball.circleRectangleCollision(
                ball.getX(), 
                ball.getY(), 
                ball.getRadius(), 
                paddle.x+(paddle.w/2), 
                paddle.y+(paddle.h/2), 
                paddle.w, paddle.h) 
                && !paddle.hit )
        {
            ball.bounce_y();
            paddle.hit = true; // The paddle cannot hit the ball now
            ball.increaseSpeed();
            ball.direction(paddle.x);
            Main.hit.playInstance();
        }
    }
    
    // Checks if circle is in rectangles
    public static boolean circleRectangleCollision(
            float cX,
            float cY, 
            float cR, 
            float rX, 
            float rY, 
            float rW, 
            float rH){
        if (pointInRectangle(cX, cY, rX, rY, rW, rH)){
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

    //returns true if a point is inside rectangle
    private static boolean pointInRectangle(
            float pX, //point x
            float pY, //point y
            float rX, //rectangle x
            float rY, //recangle y
            float rW, //rectangle width
            float rH) //rectangle height
    {
        return pX < rX && pX > rX - rW && pY < rY && pY > rY - rH;
    }

    // Useful clamp function for circle rectangle collision detection
    // If x < min then ans = min 
    // If x > max then ans = max
    // otherwise, x is fine as it is
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
    
    // Runs the necessary actions to 'respawn' the player
    public void respawn()
    {
        // Sets the state to respawning
        Main.state = Main.RESPAWNING;
        
        // Sets the default speed back to it's original speed
        resetSpeed();
        
        // Makes sure that the ball can hit the paddle.
        Main.paddle.hit = false;
        
        // If the lives are greater than 0, then display the firing arrow
        if (Main.lives > 0)
            Main.arrow.toggleHideNode();
        
    }
}
