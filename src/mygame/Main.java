package mygame;

import com.jme3.app.SimpleApplication;
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
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private ArrayList<Node> node_blocks = new ArrayList<Node>();
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private Ball ball;
    private Spatial S_ball;

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setWidth(800);
        settings.setHeight(600);
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Camera setup for 2D games
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0,0,0.5f));
        getFlyByCamera().setEnabled(false);
        final float SCALE = 0.5f;
        
        // Adding the background to the game
        Spatial background = getSpatial("background", 1);
        background.move(getWidth("background", 1.0f)/2, getHeight("background", 1.0f)/2, 0);
        guiNode.attachChild(background);
        
        // Adding the ball to the game
        ball = new Ball(settings.getWidth()/2, 100);
        ball.setRadius(getHeight(ball.getName(), SCALE)/40f);
        S_ball = getSpatial(ball.getName(), 0.2f);
        S_ball.move(ball.getX(), ball.getY(), 0);
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
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        ball.update(tpf);
        S_ball.setLocalTranslation(ball.getX(), ball.getY(), 0);
        
        // Calculate detection results
        for (int i=0; i < blocks.size(); i++) {
            if (checkCollision(ball, blocks.get(i))) {
                ball.bounce();
            }
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
    
    // Circle and block euclideans collision in (lecture 16).
    private boolean checkCollision(Ball ball, Block block ) 
    {
        float deltaX = ball.getX() - Math.max(block.getX(), Math.min(ball.getX(), block.getX() + Block.getWidth()));
        float deltaY = ball.getY() - Math.max(block.getY(), Math.min(ball.getY(), block.getY() + Block.getHeight()));
        return (deltaX * deltaX + deltaY * deltaY) < (ball.getRadius() * ball.getRadius());
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
}
