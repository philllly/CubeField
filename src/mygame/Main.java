package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.scene.shape.Dome;
import java.util.ArrayList;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private ArrayList<Cube> cubeField;
    Geometry player;
    private boolean START;
    
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("CubeField");
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        Geometry floorMesh = createFloor();
        rootNode.attachChild(floorMesh);
        player = createPlayer();
        rootNode.attachChild(player);
        Vector3f v = new Vector3f(2.0f, 1.0f, -3.0f);
        Geometry cubeMesh = createCube(v, ColorRGBA.Blue);
        rootNode.attachChild(cubeMesh);
        
        
        cubeField = new ArrayList<Cube>();
    }

    @Override
    public void simpleUpdate(float tpf) {
        gameLogic(1);
        camBehind();
    }
    
    public Geometry createFloor() {
        Vector3f v = new Vector3f(0.0f, -1.0f, 1.0f);
        Box floor = new Box(v, 100, 0, 100);
        Geometry floorMesh = new Geometry("Floor", floor);
        Material floorMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMaterial.setColor("Color", ColorRGBA.LightGray);
        floorMesh.setMaterial(floorMaterial);
        floorMesh.setName("floor");
        return floorMesh;
    }
    
    public Geometry createPlayer() {
        Dome b = new Dome(Vector3f.ZERO, 10, 100, 1);
        Geometry playerMesh = new Geometry("Player", b);
        Material playerMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        playerMaterial.setColor("Color", ColorRGBA.Red);
        playerMesh.setMaterial(playerMaterial);
        playerMesh.setName("player");
        return playerMesh;
    }
    
    public Geometry createCube(Vector3f loc, ColorRGBA col) {
        Box b = new Box(loc, 1, 1, 1);
        Geometry cubeMesh = new Geometry("Cube", b);
        Material cubeMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        cubeMaterial.setColor("Color", col);
        cubeMesh.setMaterial(cubeMaterial);
        return cubeMesh;
    }

    public void gameReset() {
  
    }
    
    public void gameLost() {
        
    }
    
    public void camBehind() {
        cam.setLocation(player.getLocalTranslation().add(0, 2, -8));
        cam.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Z);
        
    }
    
    public void gameLogic(float tpf) {
        player.move(0, 0, 1);
    }
    
    public void createRandomCubes(ColorRGBA col) {
        
    }
}
