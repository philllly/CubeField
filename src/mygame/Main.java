package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingVolume;
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

    private ArrayList<Geometry> cubeField;
    private Node playerAndFloor;
    private boolean RUNNING;
    private ColorRGBA cubeColor;
    
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
        
        playerAndFloor = createPlayerAndFloor();
        rootNode.attachChild(playerAndFloor);
        cubeColor = ColorRGBA.Red;
        
        cam.setLocation(playerAndFloor.getLocalTranslation().add(0, 2, -8));
        cam.lookAt(playerAndFloor.getLocalTranslation(), Vector3f.UNIT_Z);
               
        RUNNING = true;
        
        cubeField = new ArrayList<Geometry>();


    }

    @Override
    public void simpleUpdate(float tpf) {
        if (RUNNING) {
            gameLogic(tpf);
        }
        camBehind();
     
    }
    
    public Geometry createFloor() {
        Vector3f v = new Vector3f(0.0f, 0.0f, 50.0f);
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
    
    public Node createPlayerAndFloor() {
        Geometry player = createPlayer();
        Geometry floor = createFloor();
        Node node = new Node();
        node.attachChild(player);
        node.attachChild(floor);
        return node;
    }
    
    public Geometry createCube(Vector3f loc) {
        Box b = new Box(loc, 1, 1, 1);
        Geometry cubeMesh = new Geometry("Cube", b);
        Material cubeMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        cubeMaterial.setColor("Color", ColorRGBA.Red);
        cubeMesh.setMaterial(cubeMaterial);
        cubeMesh.setName("cube");
        return cubeMesh;
    }
    
    public void addRandomCube() {
        
        int playerX = (int) playerAndFloor.getLocalTranslation().getX();
        int playerZ = (int) playerAndFloor.getLocalTranslation().getZ();
        
        float x = FastMath.nextRandomInt(playerX - 20, playerX + 20);
        float z = playerZ + 20;
        Vector3f v = new Vector3f(x, 0.0f, z);
        Geometry cube = createCube(v);
        try {
            rootNode.attachChild(cube);
        } catch (NullPointerException e) {
            System.out.println("Null Pointer Exception");
        }
        cubeField.add(cube);
        
    }

    public void gameReset() {
  
    }
    
    public void gameLost() {
        RUNNING = false;
        gameReset();
        
    }
    
    public void camBehind() {
        cam.setLocation(playerAndFloor.getLocalTranslation().add(0, 2, -8));
        cam.lookAt(playerAndFloor.getLocalTranslation(), Vector3f.UNIT_Z);
        
    }
    
    public void gameLogic(float tpf) {
        Vector3f v = new Vector3f(0.0f, 0.0f, 0.1f);
        playerAndFloor.move(v);
        
        
        addRandomCube();
        
//        for (Geometry c : cubeField) {
//            Geometry playerModel = (Geometry) playerAndFloor.getChild(0);
//            BoundingVolume playerVolume = playerModel.getWorldBound();
//            BoundingVolume cubeVolume = c.getWorldBound();
//            if (playerVolume.intersects(cubeVolume)) {
//                gameLost();
//                return;
//            }
//           
//            float cubeZ = c.getLocalTranslation().getZ();
//            float playerZ = playerAndFloor.getLocalTranslation().getZ();
//            if (cubeZ + 2 < playerZ) {
//                c.removeFromParent();
//                cubeField.remove(c);
//            }
//        }
    }
    
    
}
