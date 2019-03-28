package mygame;

import static mygame.Main.getHeight;
import mygame.blocks.*;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */
public class Level {
    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;
    public static final int LEVEL_THREE = 3;
    public static final int LEVELS = 3;
    
    public static final void load(int level)
    {
        clearBlocks();
        
        final int margin_x_between = 64; // space between each block x axis
        int margin_x_left;      // margin from left of the screen
        int margin_y;           // Margin from the top of the screen
        int margin_y_between;   // Space between each block y axis
        int increment_y;        // How much space to increment for the space
        switch (level)
        {
            case LEVEL_ONE:
                
                margin_x_left = 111; // Margin from the left side of the screen
                margin_y = Main.SCREENHEIGHT-100; // Margin from top of screen
                margin_y_between = 25; // Margin To increment y for rows
                increment_y = 25;      // Increments on the margin_y_between

                // Adding the bricks to the game
                //Brick4
                for (int i = 0; i < 10; i++)
                    Main.blocks.add(new Brick4(
                            margin_x_left+(i*margin_x_between),
                            margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                //Brick3
                for (int i = 0; i < 10; i++)
                    Main.blocks.add(new Brick3(
                            margin_x_left+(i*margin_x_between),
                            margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                //Brick2
                for (int i = 0; i < 3; i++)
                    Main.blocks.add(new Brick2(
                            margin_x_left+(i*margin_x_between),
                            margin_y-increment_y));
                
                // Red balls
                for (int i = 3; i < 7; i++)
                    Main.greenBalls.add(new BallGreen(
                            margin_x_left+(i*margin_x_between),
                            margin_y-increment_y, 
                            getHeight(BallGreen.getName(),1)/2f));
                //Brick2
                for (int i = 7; i < 10; i++)
                    Main.blocks.add(new Brick2(
                            margin_x_left+(i*margin_x_between),
                            margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                //Brick1
                for (int i = 0; i < 10; i++)
                    Main.blocks.add(new Brick1(
                            margin_x_left+(i*margin_x_between),
                            margin_y-increment_y));
                break;
            case LEVEL_TWO:
                margin_x_left = 111;
                margin_y_between = 25;
                increment_y = 25;  
                // Bricks closer than last level to make it more difficult 
                // (Harder to react fast)
                margin_y = Main.SCREENHEIGHT-150;   

                // Adding the bricks to the game
                // Brick5
                for (int i = 0; i < 10; i++)            
                    Main.blocks.add(new Brick5(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                // Brick4
                for (int i = 0; i < 10; i++)            
                    Main.blocks.add(new Brick4(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                // Brick3 
                Main.blocks.add(new Brick3(
                        margin_x_left+(0*margin_x_between),
                        margin_y-increment_y)); 
                
                // Green balls
                for (int i = 1; i < 9; i++)            
                    Main.greenBalls.add(new BallGreen(
                        margin_x_left+(i*margin_x_between), 
                        margin_y-increment_y, 
                        getHeight(BallGreen.getName(),1)/2f));
                
                //Brick3
                Main.blocks.add(new Brick3(
                        margin_x_left+(9*margin_x_between),
                        margin_y-increment_y)); 
                increment_y = increment_y + margin_y_between;
                
                //Brick2
                for (int i = 0; i < 10; i++)
                    Main.blocks.add(new Brick2(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                //Brick1
                for (int i = 0; i < 10; i++)
                    Main.blocks.add(new Brick1(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                // Another layer of brick1
                Main.blocks.add(new Brick1(margin_x_left+(0*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick1(margin_x_left+(2*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick1(margin_x_left+(4*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick1(margin_x_left+(5*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick1(margin_x_left+(7*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick1(margin_x_left+(9*margin_x_between),
                        margin_y-increment_y));
                break;
            case LEVEL_THREE:
                margin_x_left = 111;
                margin_y_between = 25;
                increment_y = 25;  
                // Bricks closer than last level to make it more difficult 
                // (Harder to react fast)
                margin_y = Main.SCREENHEIGHT-200;     

                // Adding the bricks to the game
                // Brick5
                for (int i = 0; i < 10; i++)            
                    Main.blocks.add(new Brick5(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                // Brick4
                for (int i = 0; i < 10; i++)            
                    Main.blocks.add(new Brick4(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                // Brick3 
                Main.blocks.add(new Brick3(
                        margin_x_left+(0*margin_x_between),
                        margin_y-increment_y)); 
                
                // Green balls
                for (int i = 1; i < 9; i++)            
                    Main.greenBalls.add(new BallGreen(
                        margin_x_left+(i*margin_x_between), 
                        margin_y-increment_y, 
                        getHeight(BallGreen.getName(),1)/2f));
                
                //Brick3
                Main.blocks.add(new Brick3(
                        margin_x_left+(9*margin_x_between),
                        margin_y-increment_y)); 
                increment_y = increment_y + margin_y_between;
                
                //Brick2
                for (int i = 0; i < 10; i++)
                    Main.blocks.add(new Brick2(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                //Brick1
                for (int i = 0; i < 10; i++)
                    Main.blocks.add(new Brick1(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                // Another layer of brick1
                Main.blocks.add(new Brick2(margin_x_left+(0*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick2(margin_x_left+(2*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick2(margin_x_left+(4*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick2(margin_x_left+(5*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick2(margin_x_left+(7*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick2(margin_x_left+(9*margin_x_between),
                        margin_y-increment_y));
                
                // An extra layer of green balls
                Main.greenBalls.add(new BallGreen(
                    margin_x_left+(1*margin_x_between), 
                    margin_y-increment_y, 
                    getHeight(BallGreen.getName(),1)/2f));
                Main.greenBalls.add(new BallGreen(
                    margin_x_left+(3*margin_x_between), 
                    margin_y-increment_y, 
                    getHeight(BallGreen.getName(),1)/2f));
                Main.greenBalls.add(new BallGreen(
                    margin_x_left+(6*margin_x_between), 
                    margin_y-increment_y, 
                    getHeight(BallGreen.getName(),1)/2f));
                Main.greenBalls.add(new BallGreen(
                    margin_x_left+(8*margin_x_between), 
                    margin_y-increment_y, 
                    getHeight(BallGreen.getName(),1)/2f));
                increment_y = increment_y + margin_y_between;
                
                // An extra layer of bricks
                Main.blocks.add(new Brick3(margin_x_left+(1*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick3(margin_x_left+(3*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick2(margin_x_left+(6*margin_x_between),
                        margin_y-increment_y));
                Main.blocks.add(new Brick2(margin_x_left+(8*margin_x_between),
                        margin_y-increment_y));
                increment_y = increment_y + margin_y_between;
                
                //Brick2
                for (int i = 0; i < 10; i++)
                    Main.blocks.add(new Brick2(
                        margin_x_left+(i*margin_x_between),
                        margin_y-increment_y));
        }
        // Adds all of the bricks just created to the game
        Main.app.addBricksToGame();
    }
    
    // Removes all of the blocks from the game
    public static void clearBlocks()
    {
        Main.app.removeBricksFromGame();
        for (int i = Main.blocks.size()-1; i >= 0; i-- )
            Main.blocks.remove(i);
        for (int i = Main.greenBalls.size()-1; i >= 0; i-- )
            Main.greenBalls.remove(i);
    }
}
