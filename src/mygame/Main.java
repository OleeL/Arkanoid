package mygame;

import java.util.ArrayList;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioData.DataType;
import mygame.blocks.*;

/**
 * @author sgolegg
 * 
 * Name: Oliver Legg
 * Student ID: 201244658
 * 
 */
public class Main extends SimpleApplication {
    // Game settings
    public static Main app;
    public static final int SCREENWIDTH = 800;
    public static final int SCREENHEIGHT = 600;
    
    // Destroyable objects
    public static ArrayList<Block> blocks = new ArrayList<Block>();
    public static ArrayList<BallGreen> greenBalls = new ArrayList<BallGreen>();
    private static ArrayList<Print> HUD = new ArrayList<Print>();
    
    //Objects
    public static Wall[] walls = new Wall[3];
    public static Paddle paddle;
    public static Ball ball;
    public static Arrow arrow;
    
    // Attributes used monitor gameplay levels and stats
    public static int level = 1;
    public static int score = 0;
    public static int lives = 4;
    
    // Font to type
    public static BitmapFont font;
    
    // States of the game
    public static final int PLAYING = 0;
    public static final int RESPAWNING = 1;
    public static final int PAUSED = 2;
    public static final int WINNING = 3;
    public static final int LOSING = 4;
    public static final int LEVEL_TRANSITION = 5;
    public static int state = 5;
    public static int previousState = 1;
    
    // Helps to to move to level states
    public static boolean clicked = false;
    
    // Images and text for winning states and losing states
    public static Node winningImage;
    public static Node losingImage;
    public static Node pausedImage;
    public static Node blankImage;
    public static Print levelTransitionText;
    
    // Audio attributes
    public static AudioNode hit;
    public static AudioNode hitBreak;
    public static AudioNode music;
    
    // The program starts here
    public static void main(String[] args) {
        app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setWidth(SCREENWIDTH);
        settings.setHeight(SCREENHEIGHT);
        settings.setTitle("Oliver Legg - COMP222 Arkanoid A1 - 201244658");
        settings.setSamples(8);
        settings.setResizable(false);
        
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setDisplayFps(false);
        app.setDisplayStatView(false);
        app.start();
    }

    // Runs at startup of the program
    @Override
    public void simpleInitApp() {
        // Stopping pressing Esc to end the program
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        
        // Adding a pause button
        inputManager.addMapping("Pause", 
                new KeyTrigger(KeyInput.KEY_ESCAPE), 
                new KeyTrigger(KeyInput.KEY_P), 
                new KeyTrigger(KeyInput.KEY_PAUSE));
        
        // Firing the ball in a direction on restart
        inputManager.addMapping("Fire", 
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT), 
                new KeyTrigger(KeyInput.KEY_SPACE));
        
        // Used to transition between states when necessary
        inputManager.addMapping("Any",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT), 
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT), 
                new KeyTrigger(KeyInput.KEY_SPACE),
                new KeyTrigger(KeyInput.KEY_ESCAPE));
        
        // Used to test the game
        inputManager.addMapping("Cheat", new KeyTrigger(KeyInput.KEY_C));
        
        // Adding listeners to the game
        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(actionListener, "Fire");
        inputManager.addListener(actionListener, "Any");
        
        // Uncomment this line if you want to skip levels for testing
        //inputManager.addListener(actionListener, "Cheat");
        
        // Camera setup for 2D games
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0,0,0.5f));
        getFlyByCamera().setEnabled(false);
        
        // Adding the background to the game
        Spatial background = getSpatial("background", 1);
        background.move(getWidth("background", 1.0f)/2, 
                getHeight("background", 1.0f)/2, -2f);
        guiNode.attachChild(background);
                
        // Adding the green ball to the game
        ball = new Ball(SCREENWIDTH/2, 100, getWidth("ball", 1)/2f);
        ball.spatial = (Node) getSpatial(ball.getName(), 1f);
        
        // Adding the arrow that points in the direction that the ball is going
        arrow = new Arrow(400, 75);
        
        // Adding the walls to the game
        walls[0] = new Wall(
                33.5f,       //x
                300f,        //y 
                "wall_left", //filename
                (Node) getSpatial("wall_left", 1));
        walls[1] = new Wall(
                766.5f,      //x
                300f,        //y
                "wall_right",//filename 
                (Node) getSpatial("wall_right", 1));
        walls[2] = new Wall( 
                400f,        //x
                566.5f,      //y
                "wall_top",  //filename
                (Node) getSpatial("wall_top", 1));
        
        // Setting dimensions of the walls and attaching them to the guiNode
        for (Wall wall : walls) {
            wall.setDimensions(
                    getWidth(wall.getName(), 1), 
                    getHeight(wall.getName(), 1));
            guiNode.attachChild(wall.node);  
        }
        
        // Setup the bricks       
        Level.load(level);
        
        // Setting up the paddle
        paddle = new Paddle(SCREENWIDTH/2, 
                            50, 
                            getWidth("paddle", 1)+12, 
                            getHeight("paddle", 1)+12);
        
        // Adding HUD to the game
        font = assetManager.loadFont("Interface/consolasWhite.fnt");
        HUD.add(new Print(38,SCREENHEIGHT-19, "Lives: ", font));
        HUD.add(new Print(38,SCREENHEIGHT-38, "Level: ", font));
        HUD.add(new Print(38,SCREENHEIGHT-57, "Score: ", font));
        HUD.add(new Print(SCREENWIDTH-135,SCREENHEIGHT-19, 
                "Green Balls Remaining: ", font));
        
        
        // Adds all of the HUD to the game
        for (int i = 0; i < HUD.size(); i++)
            guiNode.attachChild(HUD.get(i).hudText);
        
        // Shows the level, when you move to the next level
        levelTransitionText = new Print(SCREENWIDTH/2, SCREENHEIGHT/2+12,
                "Destroy all the green balls!\nLevel: ", font);
        
        // Adding the end game states to the game
        winningImage = (Node) getSpatial("winning",1); 
        losingImage  = (Node) getSpatial("losing", 1); 
        pausedImage  = (Node) getSpatial("paused", 1);
        blankImage   = (Node) getSpatial("blank",  1);
        
        // Adjusing placement of the images to the centre of the screen
        winningImage.setLocalTranslation(
            SCREENWIDTH/2, 
            SCREENHEIGHT/2, 0f);
        losingImage.setLocalTranslation(
            SCREENWIDTH/2, 
            SCREENHEIGHT/2, 0f);
        pausedImage.setLocalTranslation(
            SCREENWIDTH/2, 
            SCREENHEIGHT/2, 0f);
        blankImage.setLocalTranslation(
            SCREENWIDTH/2,
            SCREENHEIGHT/2,
            0f);
        
        // Initialises the sound within the game
        // Plays when you hit the walls and paddle
        hit = new AudioNode(assetManager, "Sounds/hit.wav", 
                DataType.Buffer);
        hit.setPositional(false); // Makes it mono, not surround.
        hit.setVolume(7);         // This sfx is quieter so we turn it up so we
                                  // can hear it
        
        // Plays when you hit and break a block
        hitBreak = new AudioNode(assetManager, "Sounds/break.wav", 
                DataType.Buffer);
        hitBreak.setPositional(false); // Makes it mono, not surround
        hitBreak.setVolume(5);         // Average volume
        
        // Plays all the time 
        music = new AudioNode(assetManager, "Sounds/music.wav", 
                DataType.Buffer);
        music.setPositional(false); // Makes it mono, not surround
        music.setLooping(true);     // Makes the music loop over and over
        music.setVolume(2f);        // Makes the music quiter than the sfx
        
        // Adding sound effects to the game
        rootNode.attachChild(hit);
        rootNode.attachChild(hitBreak);
        rootNode.attachChild(music);
               
        // Attaching objects to the game
        guiNode.attachChild(paddle.node);
        guiNode.attachChild(arrow.pivot);
        guiNode.attachChild(ball.spatial);
        
        arrow.toggleHideNode();
        
        music.play(); // Finished intialisation, therefore we start the music!
    }
    
    // Adding the bricks to the game
    public void addBricksToGame()
    {
        // Loops over all of the blocks
        for (int i = 0; i < blocks.size(); i++)
        {
            blocks.get(i).node = (Node) getSpatial(blocks.get(i).getName(), 1);
            blocks.get(i).node.setLocalTranslation(
                    blocks.get(i).getX(), 
                    blocks.get(i).getY(), 
                    0f);
            guiNode.attachChild(blocks.get(i).node);
        }
        
        // Loops over all of the green balls
        for (int i = 0; i < greenBalls.size(); i++)
        {
            greenBalls.get(i).node = (Node) getSpatial(BallGreen.getName(), 1);
            greenBalls.get(i).node.setLocalTranslation(
                    greenBalls.get(i).getX(), 
                    greenBalls.get(i).getY(), 
                    0);
            guiNode.attachChild(greenBalls.get(i).node);
        }
    }
    
    // Remove bricks from the game
    public void removeBricksFromGame()
    {
        for (int i = 0; i < blocks.size(); i++)
        {
            guiNode.detachChild(blocks.get(i).node);
        }
        for (int i = 0; i < greenBalls.size(); i++)
        {
            guiNode.detachChild(greenBalls.get(i).node);
        }
    }
    
    // Loads the next level of the game
    public void nextLevel()
    {
        // Resets the position of the paddle
        paddle.resetPosition();
        
        // Respawns the ball and sets the game up for the respawn state
        ball.respawn();
        
        // Loads the next level in the game
        Level.load(++level);
        
        // Puts the game into the transition state
        state = LEVEL_TRANSITION;
        
        // Hides The arrow as we dont need it in the transition state
        arrow.toggleHideNode();
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        // Quicker access to mouse positions
        float mouse_x = inputManager.getCursorPosition().x;
        float mouse_y = inputManager.getCursorPosition().y;
        
        // Updates these objects as they will always need to be updated
        ball.update(tpf, mouse_x,mouse_y);
        paddle.update(mouse_x, walls[0].getWidth());
        
        switch (state)
        {
            case PLAYING:
                // If there are no balls left, win or go to next level
                if (greenBalls.isEmpty())
                {
                    // If on last level and you got all balls you won the game
                    if (level == Level.LEVELS){
                        state = WINNING;
                        guiNode.attachChild(winningImage); // Shows winning pic
                    }
                    else
                    {
                        nextLevel(); // Goes to next level
                    }
                }
                break;
            case RESPAWNING:
                // Puts paddle in the middle
                paddle.resetPosition();
                // If no lives left, lose
                if (lives == 0){
                    ball.respawn();
                    state = LOSING;
                    app.guiNode.attachChild(losingImage); // Show the lose state
                }
                //Only update the arrow when you're respawning
                arrow.update(mouse_x, mouse_y);
                break;
            case LEVEL_TRANSITION:
                // Sets the pos of the paddle and ball to the centre of screen
                ball.setPosition(
                        (float) SCREENWIDTH/2f, 
                        (float) paddle.y +(paddle.h/ 2) + ball.getRadius() - 5);
                levelTransitionText.update_text(level);
                guiNode.attachChild(blankImage);
                guiNode.attachChild(levelTransitionText.hudText);
                break;
        }
        
        // Updating the text on the HUD
        HUD.get(0).update_text(lives);
        HUD.get(1).update_text(level);
        HUD.get(2).update_text(score);
        HUD.get(3).update_text(greenBalls.size());
        
    }

    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    // Gets the width of an image
    public static float getWidth(String filename, float scale)
    {   
        Texture2D tex; 
        tex = (Texture2D) app.assetManager.loadTexture("Textures/"+filename+".png");
        return tex.getImage().getWidth() * scale;
    }
    
    // Gets the height of an image
    public static float getHeight(String filename, float scale)
    {   
        Texture2D tex;
        tex = (Texture2D) app.assetManager.loadTexture("Textures/"+filename+".png");
        return tex.getImage().getHeight() * scale;
    }
    
    // makes a spatial type with an image. This can be casted to a Node type.
    public static Spatial getSpatial(String name, float scale) {
        Node node = new Node(name);
        
        // load picture
        Picture pic = new Picture(name);
        Texture2D tex;
        tex = (Texture2D) app.assetManager.loadTexture("Textures/"+name+".png");
        pic.setTexture(app.assetManager,tex,true);
        
        // adjust picture
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();
        pic.setWidth(width*scale);
        pic.setHeight(height*scale);
        pic.move(-width/2f,-height/2f,0);
        
        // add a material to the picture
        Material picMat;
        picMat = new Material(app.assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
        node.setMaterial(picMat);
        
        // attach the picture to the node and return it
        node.attachChild(pic);
        return node;
    }
    
    // Allows to add other models to the gui from other files
    public static void guiNodeAttachChild(Node n)
    {
        app.guiNode.attachChild(n);
    }
    
    // Listens to key inputs into the game
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            
            // starts game between transition states of levels, winning & losing 
            if (name.equals("Any") && !keyPressed)
            {
                if (state == LEVEL_TRANSITION) // Sends player to next level
                {
                    // Respawns ball
                    state = RESPAWNING;
                    guiNode.detachChild(blankImage);
                    guiNode.detachChild(levelTransitionText.hudText);
                    arrow.toggleHideNode(); // creates the arrow to aim ball
                }
                else if (state == WINNING || state == LOSING) // Restarts game
                {
                    lives = 5;
                    level = 1;
                    score = 0;
                    state = LEVEL_TRANSITION;
                    Level.load(level);
                    guiNode.detachChild(winningImage); // Hides win menu
                    guiNode.detachChild(losingImage);  // Hides losing menu
                }
            }
            // Pauses and unpauses the game
            if (name.equals("Pause") && keyPressed && state != LEVEL_TRANSITION)
            {
                if (state == PLAYING ||state == RESPAWNING || state == PAUSED)
                {
                    if (state != PAUSED)
                    {
                        previousState = state;
                        guiNode.attachChild(pausedImage); // Shows pause menu
                    }
                    else
                    {
                        guiNode.detachChild(pausedImage); // Hides pause menu
                    }
                    ball.pause(); // Stops or resumes the ball
                }
            } // Fires the ball in a direction and then hides the arrow
            else if (name.equals("Fire") && keyPressed && state == RESPAWNING)
            {
                clicked = true;
                arrow.toggleHideNode();
            }
            // Skips the game to the next level
            if (name.equals("Cheat") && !keyPressed)
            {
                Level.clearBlocks(); 
            }
        }
    };
    
}
