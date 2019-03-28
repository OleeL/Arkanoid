package mygame;
import com.jme3.scene.Node;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */
public class Wall{
    protected String name;
    protected float x;
    protected float y;
    public float width;
    public float height;
    public Node node;
    
    public Wall(float x, float y, String name, Node node)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.node = node;
        
        // Moves the wall to the desired location
        node.setLocalTranslation(x, y, 0);
    }
    
    // Sets the width and height of the wall
    public void setDimensions(float w, float h)
    {
        width = w;
        height = h;
    }
    
    // Gets the width of the wall
    public float getWidth()
    {
        return width;
    }
    
    // Gets the height of the wall
    public float getHeight()
    {
        return height;
    }
    
    // Gets the name of the wall
    public String getName()
    {
        return name;
    }
    
    // Gets the x coordinate of the wall
    public float getX()
    {
        return x;
    }
    
    // Gets the y coordinate of the wall
    public float getY()
    {
        return y;
    }
    
    // Moves the wall to coordinate on the x axis
    public void setX(float x)
    {
        this.x = x;
        node.setLocalTranslation(x, y, 0f);
    }
    
    // Moves the wall to a coordinate on the y axis
    public void setY(float y)
    {
        this.y = y;
        node.setLocalTranslation(x, y, 0f);
    }
}
