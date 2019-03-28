package mygame;
import com.jme3.scene.Node;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */

public class Paddle {
    
    public float x;
    public float y;
    public float w;
    public float h;
    public boolean hit = false; // Records whether the paddle is hit or not
    public Node node;
   
    public Paddle(float x, float y, float w, float h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        
        // Creates the paddle node 
        node = (Node) Main.getSpatial("paddle", 1);
    }

    // Updates the paddle every step
    public void update(float mouse_x, float wallWidth)
    {
        // Movement and collision for paddle and wall
        switch(Main.state)
        {
            case Main.RESPAWNING :
                // If respawning, it puts the paddle in the middle
                x = Main.SCREENWIDTH / 2;
                break;
            case Main.PAUSED :
                break;
            case Main.PLAYING :
                // If playing, move the paddle to the mouse. The following code
                // makes sure, it doesn't go through the walls
                float temp_x = Math.max(mouse_x, ((w-12)/2)+wallWidth);
                x = Math.min(temp_x, Main.SCREENWIDTH-((w-12)/2)-wallWidth);
            default :
                // always make sure that the x and y are updated.
                node.setLocalTranslation(x, y, 0);
                break;
        }
    }
    
    // Resets the position of the paddle to the centre of the screen
    public void resetPosition()
    {
        this.x = Main.SCREENWIDTH/2;
        node.setLocalTranslation(x, y, 0f);
    }
}
