/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
import com.jme3.scene.Node;
import static mygame.Main.paused;

/**
 *
 * @author Olee
 */
public class Paddle {
   public float x;
   public float y;
   public float w;
   public float h;
   public boolean hit = false;
   public Node node;
   
   public Paddle(float x, float y, float w, float h)
   {
       this.x = x;
       this.y = y;
       this.w = w;
       this.h = h;
   }

    public void update(float mouse_x, float wallWidth)
    {
        // Movement and collision for paddle and wall
        float temp_x = Math.max(mouse_x, ((w-12)/2)+wallWidth);
        x = Math.min(temp_x, Main.screenWidth-((w-12)/2)-wallWidth);
        if (!Main.paused)
            node.setLocalTranslation(x, y, 0);
    }
}
