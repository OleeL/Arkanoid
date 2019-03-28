package mygame;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */

// Prints a bit of text to the screen
public class Print {
    
    public BitmapText hudText;
    public String default_text;
    
    public Print(float x, float y, String text, BitmapFont font)
    {
        default_text = text;
        
        // Takes the font passed in and sets typing from right to left to false
        hudText = new BitmapText(font, false);
        
        // position
        hudText.setLocalTranslation(
                x-(font.getLineWidth(text)/2), 
                y+(hudText.getHeight()/2), 
                1f);
        
    }
    
    // Concatenates a value on the end of the text
    // e.g. update_text(2), default_text = "Lives: " -> "Lives: 2"
    public void update_text(int number)
    {
        hudText.setText(default_text+number);
    }
    
}
