package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
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
import mygame.blocks.*;
import java.util.ArrayList;

/**
 *
 * @author sgolegg
 */
public class Main extends SimpleApplication {
    private static ArrayList<Node> node_blocks = new ArrayList<Node>();
    private static ArrayList<Block> blocks = new ArrayList<Block>();
    public static Node paddle;
    private static Ball ball;
    public static boolean hit_paddle = false;
    public static boolean paused = false;
    public static Main app;
    private Spatial S_ball;
    //public BitmapFont myFont = assetManager.loadFont("Interface/pixelart.ttf");
    //public BitmapText win = new BitmapText(myFont, false);
    
    public static void main(String[] args) {
        app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Oliver Legg - COMP222 Arkanoid A1 - 201244658");
        settings.setSamples(8);
        settings.setResizable(false);
        
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setDisplayFps(false);
        app.setDisplayStatView(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Stopping pressing Esc to end the program
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        
        //Adding a pause button
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE), 
                new KeyTrigger(KeyInput.KEY_P), 
                new KeyTrigger(KeyInput.KEY_PAUSE));
        inputManager.addListener(actionListener, "Pause");
        
        // Camera setup for 2D games
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0,0,0.5f));
        getFlyByCamera().setEnabled(false);
        
        // Adding the background to the game
        Spatial background = getSpatial("background", 1);
        background.move(getWidth("background", 1.0f)/2, 
                getHeight("background", 1.0f)/2, 0);
        guiNode.attachChild(background);
        
        // Adding the ball to the game
        ball = new Ball(settings.getWidth()/2, 100, 
                getWidth("ball", 1)/2f);
        S_ball = getSpatial(ball.getName(), 1f);
        
        // Setup the bricks
        final int margin_x_between = 68;
        final int margin_x_left = 100;
        final int margin_y = settings.getHeight();
        int increment_y = 25;
        Block.setDimensions(getWidth("brick1", 1), getHeight("brick1", 1));        
                        
        // Adding the bricks to the game
        for (int i = 0; i < 10; i++)            //Brick5
            blocks.add(new Brick5(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        increment_y = increment_y + 50;
        for (int i = 0; i < 10; i++)            //Brick4
            blocks.add(new Brick4(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        increment_y = increment_y + 50;
        for (int i = 0; i < 10; i++)            //Brick3
            blocks.add(new Brick3(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        increment_y = increment_y + 50;
        for (int i = 0; i < 10; i++)            //Brick2
            blocks.add(new Brick2(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        increment_y = increment_y + 50;
        for (int i = 0; i < 10; i++)            //Brick1
            blocks.add(new Brick1(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        
        // Adding the bricks to the game
        for (int i = 0; i < blocks.size(); i++)
        {
            node_blocks.add((Node) getSpatial(blocks.get(i).getName(), 1));
            node_blocks.get(i).setUserData("alive",true);
            node_blocks.get(i).setLocalTranslation(
                    blocks.get(i).getX(), 
                    blocks.get(i).getY(), 
                    0f);
            guiNode.attachChild(node_blocks.get(i));
        }
        
        paddle = (Node) getSpatial("paddle", 1);
        paddle.setUserData("alive", true);
        paddle.setUserData("height", getHeight("paddle", 1));
        guiNode.attachChild(paddle);
        guiNode.attachChild(S_ball);
        initKeys();
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        
        ball.update(tpf, settings.getWidth(), settings.getHeight());
        S_ball.setLocalTranslation(ball.getX(), ball.getY(), 0);
        
        // Calculate detection results
        for (int i=0; i < blocks.size(); i++) {
            float block_x = blocks.get(i).getX();
            float block_y = blocks.get(i).getY();
            float block_w = Block.getWidth()+12;
            float block_h = Block.getHeight()+12;
            if ( Ball.Circle_Rect_Intersect(
                    ball.getX(), 
                    ball.getY(), 
                    ball.getRadius(), 
                    block_x+(block_w/2), 
                    block_y+(block_h/2), 
                    block_w, 
                    block_h)) {
                ball.increasePoints(blocks.get(i).getName());
                hit_paddle = false;
                if ( !Ball.Circle_Rect_Intersect(
                        ball.getX(), 
                        ball.getY()-(ball.getSpeedY()*tpf), 
                        ball.getRadius(), 
                        block_x+(block_w/2), 
                        block_y+(block_h/2), 
                        block_w, 
                        block_h)) {
                    ball.bounce_y();
                }
                if ( !Ball.Circle_Rect_Intersect(
                        ball.getX()-(ball.getSpeedX()*tpf), 
                        ball.getY(), 
                        ball.getRadius(), 
                        block_x+(block_w/2), 
                        block_y+(block_h/2), 
                        block_w, 
                        block_h)) {
                    ball.bounce_x();
                }
                node_blocks.get(i).removeFromParent();
                node_blocks.remove(i);
                blocks.remove(i);
            }
        }
        
        // redefining to save time
        float paddle_w = getWidth("paddle", 1)+12;
        float paddle_h = getHeight("paddle", 1)+12;
        float mouse_x = inputManager.getCursorPosition().x;
        //float paddle_x = mouse_x + (getWidth("paddle", 1)/2);
        float paddle_x = mouse_x;
        float paddle_y = 50;
        
        // Movement and collision for paddle and wall
        float temp = Math.max(paddle_x, (paddle_w-12)/2);
        paddle_x = Math.min(temp, settings.getWidth()-((paddle_w-12)/2));
        if (!paused)
            paddle.setLocalTranslation(paddle_x, paddle_y, 0);
        
        // Collision for paddle and ball
        if ( Ball.Circle_Rect_Intersect(
                ball.getX(), 
                ball.getY(), 
                ball.getRadius(), 
                paddle_x+(paddle_w/2), 
                paddle_y+(paddle_h/2), 
                paddle_w, paddle_h) 
                && !hit_paddle )
        {
            ball.bounce_y();
            hit_paddle = true;
            ball.increaseSpeed();
            ball.direction(paddle_x);
        }
    }

    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    // Gets the width of an image
    public float getWidth(String filename, float scale)
    {   
        Texture2D tex; 
        tex = (Texture2D) assetManager.loadTexture("Textures/"+filename+".png");
        return tex.getImage().getWidth() * scale;
    }
    
    // Gets the height of an image
    public float getHeight(String filename, float scale)
    {   
        Texture2D tex;
        tex = (Texture2D) assetManager.loadTexture("Textures/"+filename+".png");
        return tex.getImage().getHeight() * scale;
    }
    
    private void initKeys()
    {
        inputManager.addMapping("teleport", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "teleport");
    }
   
    
    // Generates a spatial type with an image. This can be casted to a Node type.
    private Spatial getSpatial(String name, float scale) {
        Node node = new Node(name);
        
        // load picture
        Picture pic = new Picture(name);
        Texture2D tex;
        tex = (Texture2D) assetManager.loadTexture("Textures/"+name+".png");
        pic.setTexture(assetManager,tex,true);
 
        // adjust picture
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();
        pic.setWidth(width*scale);
        pic.setHeight(height*scale);
        pic.move(-width/2f,-height/2f,0);
 
        // add a material to the picture
        Material picMat;
        picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
        node.setMaterial(picMat);
        
        // attach the picture to the node and return it
        node.attachChild(pic);
        return node;
    }
    
    private final ActionListener actionListener = new ActionListener() {
        
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && keyPressed)
            {
                ball.pause();
                if (!paused)
                    paused = true;
                else 
                    paused = false;
            }
        }
    };
}
