/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.blocks;

/**
 *
 * @author sgolegg
 */
public abstract class Block {
    protected String name;
    protected int health;
    protected int x;
    protected int y;
    public static float width;
    public static float height;
    
    public Block(int x, int y, int health, String name)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = health;
    }
    
    public static void setDimensions(float w, float h)
    {
        width = w;
        height = h;
    }
    
    public static float getWidth()
    {
        return width;
    }
    
    public static float getHeight()
    {
        return height;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
}
