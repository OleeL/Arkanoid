package mygame;

import com.jme3.math.FastMath;
import com.jme3.scene.Node;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */
public class Arrow {
    protected final String name = "arrow";
    public Node node;
    public Node pivot;
    public float last_mouse_x = 0;
    private boolean hidden = false;
    
    // Creates an aiming arrow at a specific x and y coordinate
    public Arrow(float x, float y)
    {
        // Arrow picture is created
        this.node = (Node) Main.getSpatial(name, 1f);
        
        // A pivot is created in order for the arrow to be rotated
        this.pivot = new Node("pivot");
        this.pivot.setLocalTranslation(x, y, 0f);
        this.pivot.attachChild(node);
        
        // The arrow is moved 50 pixels above the pivot node
        // (The arrows length is around 50 pixels)
        node.setLocalTranslation(0, 50, -1f);
    }
    
    // The is updated every second and in order for the arrow to 'aim', we need
    // the mouse_x and mouse_y
    public void update(float mouse_x, float mouse_y)
    {
        // Rotates back to it's initial state
        pivot.rotate(0f, 0f, -last_mouse_x);
        
        // Gets the angle of the rotation in radians
        float direction = (float) FastMath.atan2(
                    (mouse_y-pivot.getLocalTranslation().y),
                    (mouse_x-pivot.getLocalTranslation().x));
        
        direction-= 1.5708; // rotates 90 degrees in radians to get it right
        
        // Makes sure the arrow isn't aimed in any directions where the ball
        // would be unable to come back to the paddle
        if ((direction > 1.2 && direction < 1.7) || 
                (direction < -FastMath.PI && direction > -5) )
            direction = 1.3f;
        else if (direction < -1.3)
            direction = -1.3f;
        
        // The pivot is then rotated in the direction
        pivot.rotate(0f,0f,direction);
        
        // For the next update, it is then rotated back to it's starting 
        // position of of 0 degrees position
        last_mouse_x = direction;
    }
    
    // Returns the name of the arrow
    public String getName()
    {
        return name;
    }
    
    // Hides and shows the arrow when it does or doesnt need to be shown
    public void toggleHideNode()
    {
        if (!hidden)
        {
            pivot.getParent().detachChild(pivot);
            hidden = true;
        }
        else
        {
            Main.guiNodeAttachChild(pivot);
            hidden = false;
        }
    }
}