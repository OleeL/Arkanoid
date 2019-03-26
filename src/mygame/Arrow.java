package mygame;

import com.jme3.scene.Node;

/**
 *
 * @author Olee
 */
public class Arrow {
    protected String name;
    protected float x;
    protected float y;
    public float width;
    public float height;
    public Node node;
    
    public Arrow(float x, float y, String name, Node node)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.node = node;
        node.setLocalTranslation(x, y, 0);
    }
    
    public void setDimensions(float w, float h)
    {
        width = w;
        height = h;
    }
    
    public float getWidth()
    {
        return width;
    }
    
    public float getHeight()
    {
        return height;
    }
    
    public String getName()
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
    
    public void setX(float x)
    {
        this.x = x;
        node.setLocalTranslation(x, y, 0f);
    }
    
    public void setY(float y)
    {
        this.y = y;
        node.setLocalTranslation(x, y, 0f);
    }
}