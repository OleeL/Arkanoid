package mygame;

import java.util.ArrayList;
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
import mygame.blockBalls.*;

/**
 *
 * @author sgolegg
 */
public class Main extends SimpleApplication {
    private static ArrayList<Block> blocks = new ArrayList<Block>();
    private static ArrayList<BallGreen> greenBalls = new ArrayList<BallGreen>();
    private static Wall[] walls = new Wall[3];
    public static Paddle paddle;
    private static Ball ball;
    public static boolean paused = false;
    public static Main app;
    public static final int screenWidth = 800;
    public static final int screenHeight = 600;
    public int score = 0;
    
    //public BitmapFont myFont = assetManager.loadFont("Interface/pixelart.ttf");
    //public BitmapText win = new BitmapText(myFont, false);
    
    public static void main(String[] args) {
        app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setWidth(screenWidth);
        settings.setHeight(screenHeight);
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
        
        // Adding the green ball to the game
        ball = new Ball(screenWidth/2, 100, getWidth("ball", 1)/2f);
        ball.spatial = getSpatial(ball.getName(), 1f);
        
        // Adding the walls to the game
        walls[0] = new Wall(
                33.5f, 
                300f, 
                "wall_left", 
                (Node) getSpatial("wall_left", 1));
        walls[1] = new Wall(
                766.5f, 
                300f, 
                "wall_right", 
                (Node) getSpatial("wall_right", 1));
        walls[2] = new Wall(
                400f, 
                566.5f, 
                "wall_top", 
                (Node) getSpatial("wall_top", 1));
        
        for (Wall wall : walls) {
            wall.setDimensions(getWidth(wall.getName(), 1), getHeight(wall.getName(), 1));
            guiNode.attachChild(wall.node);  
        }
        
        // Setup the bricks
        final int margin_x_between = 64;
        final int margin_x_left = 100;
        final int margin_y = settings.getHeight()-100;
        final int margin_y_between = 25;
        int increment_y = 25;
        Block.setDimensions(getWidth("brick1", 1), getHeight("brick1", 1));        
                        
        // Adding the bricks to the game
        for (int i = 0; i < 10; i++)            //Brick5
            blocks.add(new Brick5(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        increment_y = increment_y + margin_y_between;
        for (int i = 0; i < 10; i++)            //Brick4
            blocks.add(new Brick4(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        increment_y = increment_y + margin_y_between;
        for (int i = 0; i < 3; i++)            //Brick3
            blocks.add(new Brick3(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        for (int i = 3; i < 7; i++)            // Red balls
            greenBalls.add(new BallGreen(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y, getHeight(BallGreen.getName(),1)/2f));
        for (int i = 7; i < 10; i++)            //Brick3
            blocks.add(new Brick3(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        increment_y = increment_y + margin_y_between;
        for (int i = 0; i < 10; i++)            //Brick2
            blocks.add(new Brick2(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        increment_y = increment_y + margin_y_between;
        for (int i = 0; i < 10; i++)            //Brick1
            blocks.add(new Brick1(margin_x_left+(i*margin_x_between),
                    margin_y-increment_y));
        
        // Adding the bricks to the game
        for (int i = 0; i < blocks.size(); i++)
        {
            blocks.get(i).node = (Node) getSpatial(blocks.get(i).getName(), 1);
            blocks.get(i).node.setLocalTranslation(
                    blocks.get(i).getX(), 
                    blocks.get(i).getY(), 
                    0f);
            guiNode.attachChild(blocks.get(i).node);
        }
        for (int i = 0; i < greenBalls.size(); i++)
        {
            greenBalls.get(i).node = (Node) getSpatial(BallGreen.getName(), 1);
            greenBalls.get(i).node.setLocalTranslation(
                    greenBalls.get(i).getX(), 
                    greenBalls.get(i).getY(), 
                    0);
            guiNode.attachChild(greenBalls.get(i).node);
        }
        paddle = new Paddle(0, 
                            50, 
                            getWidth("paddle", 1)+12, 
                            getHeight("paddle", 1)+12);
        paddle.node = (Node) getSpatial("paddle", 1);
        guiNode.attachChild(paddle.node);
        guiNode.attachChild(ball.spatial);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        
        ball.update(
                tpf, 
                settings.getWidth(), 
                settings.getHeight(), 
                walls[0].getWidth(), 
                walls[2].getHeight(),
                inputManager.getCursorPosition().x,
                inputManager.getCursorPosition().y);
        
        paddle.update(inputManager.getCursorPosition().x, walls[0].getWidth());
        ball.spatial.setLocalTranslation(ball.getX(), ball.getY(), 0);
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
                paddle.hit = false;
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
                blocks.get(i).node.removeFromParent();
                blocks.remove(i);
                score += 1;
            }
        }
        // Collision between red ball and green ball
        for (int i = 0; i < greenBalls.size(); i++)
        {
            if (greenBalls.get(i).circle_collided(ball.getX(), 
                    ball.getY(), 
                    ball.getRadius())
            && !greenBalls.get(i).isDead())
            {                    
                float direction = (float) Math.toDegrees(
                        Math.atan2(
                            (ball.getY()-greenBalls.get(i).getY()),
                            (ball.getX()-greenBalls.get(i).getX())));

                ball.setSpeedX(ball.getSpeed() * (float) Math.cos( 
                        direction * Math.PI / 180));
                ball.setSpeedY(ball.getSpeed() * (float) Math.sin( 
                        direction * Math.PI / 180));

                paddle.hit = false;
                greenBalls.get(i).node.removeFromParent();
                greenBalls.get(i).kill();
                greenBalls.remove(i);
                score += 500;
            }
            if (Math.abs(ball.getSpeedX()) < 0.2f)
            {
                ball.setSpeedX(ball.getSpeedX() * 2);
            }
        }
        
        // Collision for paddle and ball
        if ( Ball.Circle_Rect_Intersect(
                ball.getX(), 
                ball.getY(), 
                ball.getRadius(), 
                paddle.x+(paddle.w/2), 
                paddle.y+(paddle.h/2), 
                paddle.w, paddle.h) 
                && !paddle.hit )
        {
            ball.bounce_y();
            paddle.hit = true;
            ball.increaseSpeed();
            ball.direction(paddle.x);
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
    
    // makes a spatial type with an image. This can be casted to a Node type.
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
        //System.out.println(picMat.getTextureParam("Textures/"+name+".png"));
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
                if (!paused) paused = true;
                else paused = false;
            }
        }
    };
}
