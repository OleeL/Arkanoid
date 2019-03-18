package mygame;

import com.jme3.app.SimpleApplication;
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


public class Main extends SimpleApplication {
    private ArrayList<Node> node_blocks = new ArrayList<Node>();
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private Node paddle = new Node();
    private Ball ball;
    private Spatial S_ball;
    private final float SCALE = 0.5f;

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Oliver Legg - COMP222 Arkanoid A1 - 201244658");
        settings.setSamples(8);
        settings.setResizable(false);
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Camera setup for 2D games
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0,0,0.5f));
        getFlyByCamera().setEnabled(false);
        
        // Adding the background to the game
        Spatial background = getSpatial("background", 1);
        background.move(getWidth("background", 1.0f)/2, getHeight("background", 1.0f)/2, 0);
        guiNode.attachChild(background);
        
        // Adding the ball to the game
        ball = new Ball(settings.getWidth()/2, 100);
        ball.setRadius(getWidth("ball",SCALE));
        S_ball = getSpatial(ball.getName(), 0.2f);
        guiNode.attachChild(S_ball);
        
        // Setup the bricks
        final int margin_x_between = 68;
        final int margin_x_left = 125;
        final int margin_y = settings.getHeight();
        int increment_y = 25;
        Block.setDimensions(getWidth("brick1", SCALE), getHeight("brick1", SCALE));        
                        
        // Adding the bricks to the game
        for (int i = 0; i < 10; i++)            //Brick5
            blocks.add(new Brick5(margin_x_left+(i*margin_x_between),margin_y-increment_y));
        increment_y = increment_y + 50;
        for (int i = 0; i < 10; i++)            //Brick4
            blocks.add(new Brick4(margin_x_left+(i*margin_x_between),margin_y-increment_y));
        increment_y = increment_y + 50;
        for (int i = 0; i < 10; i++)            //Brick3
            blocks.add(new Brick3(margin_x_left+(i*margin_x_between),margin_y-increment_y));
        increment_y = increment_y + 50;
        for (int i = 0; i < 10; i++)            //Brick2
            blocks.add(new Brick2(margin_x_left+(i*margin_x_between),margin_y-increment_y));
        increment_y = increment_y + 50;
        for (int i = 0; i < 10; i++)            //Brick1
            blocks.add(new Brick1(margin_x_left+(i*margin_x_between),margin_y-increment_y));
        
        // Adding the bricks to the game
        for (int i = 0; i < blocks.size(); i++)
        {
            node_blocks.add((Node) getSpatial(blocks.get(i).getName(), SCALE));
            node_blocks.get(i).setUserData("alive",true);
            node_blocks.get(i).setLocalTranslation(blocks.get(i).getX(), blocks.get(i).getY(), 0);
            guiNode.attachChild(node_blocks.get(i));
        }
        
        paddle = (Node) getSpatial("paddle", SCALE);
        paddle.setUserData("alive", true);
        guiNode.attachChild(paddle);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        ball.update(tpf);
        S_ball.setLocalTranslation(ball.getX()+ball.getRadius(), ball.getY()+ball.getRadius(), 0);
        // Calculate detection results
        for (int i=0; i < blocks.size(); i++) {
            if ( Ball.checkCollisionBlock(ball, blocks.get(i))) {
                ball.bounce();
                node_blocks.get(i).removeFromParent();
                node_blocks.remove(i);
                blocks.remove(i);
            }
        }
        
        // redefining to save time
        float paddle_w = getWidth("paddle", SCALE);
        float paddle_h = getHeight("paddle", SCALE);
        float paddle_x = inputManager.getCursorPosition().x;
        paddle_x = paddle_x + (getWidth("paddle", SCALE)/2);
        float paddle_y = 50;
        
        // Movement and collision for paddle and wall
        int screenWidth = settings.getWidth();
        int screenHeight = settings.getHeight();
        
        float temp = Math.max(paddle_x, paddle_w);
        paddle_x = Math.min(temp, screenWidth);
        paddle.setLocalTranslation(paddle_x, paddle_y, 0);
        
        if (ball.getX() >= screenWidth || (ball.getX()-(ball.getRadius()*2) <= 0))
        {
            ball.speed_x = -ball.speed_x;
        }
        
        // Collision for paddle and ball
        if ( Ball.checkCollisionPaddle(ball, paddle_x, paddle_y, paddle_w, paddle_h) )
        {
            System.out.println(paddle_x +" - "+ paddle_y + " - " + paddle_w + " - " + paddle_h);
            ball.bounce();
        }
    }

    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    // Gets the width of an image
    public float getWidth(String filename, float scale)
    {   
        Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/"+filename+".png");
        return tex.getImage().getWidth() * scale;
    }
    
    // Gets the height of an image
    public float getHeight(String filename, float scale)
    {   
        Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/"+filename+".png");
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
        Texture2D tex = (Texture2D) assetManager.loadTexture("Textures/"+name+".png");
        pic.setTexture(assetManager,tex,true);
 
        // adjust picture
        float width = tex.getImage().getWidth();
        float height = tex.getImage().getHeight();
        pic.setWidth(width*scale);
        pic.setHeight(height*scale);
        pic.move(-width/2f,-height/2f,0);
 
        // add a material to the picture
        Material picMat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md");
        picMat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
        node.setMaterial(picMat);
 
        // set the radius of the spatial
        // (using width only as a simple approximation)
        node.setUserData("radius", width/2);
 
        // attach the picture to the node and return it
        node.attachChild(pic);
        return node;
    }
    
    private final ActionListener actionListener = new ActionListener() {
        
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("teleport")) {
                ball.x = 800;
                ball.y = 600;
            }
        }
        
    };
}
