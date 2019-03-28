package mygame.blocks;

import com.jme3.scene.Node;
import mygame.Main;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */
public abstract class Block{
    protected String name;
    protected float x;
    protected float y;
    private final int score;
    public final static float WIDTH = Main.getWidth("brick1", 1);
    public final static float HEIGHT = Main.getHeight("brick1", 1);
    public Node node;
    
    // Abstract class block made to store data for the different types of block
    public Block(float x, float y, int score, String name)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.score = score;
    }
    
    // Gets the width of the block
    public static float getWidth()
    {
        return WIDTH;
    }
    
    // Gets the height of the block
    public static float getHeight()
    {
        return HEIGHT;
    }
    
    // Gets the name of the block
    public String getName()
    {
        return name;
    }
    
    // Gets the x coordinate
    public float getX()
    {
        return x;
    }
    
    // Gets the y coordinate
    public float getY()
    {
        return y;
    }
    
    // Gets the rewarded number of points for destroying the block
    public float getScore()
    {
        return score;
    }
}
