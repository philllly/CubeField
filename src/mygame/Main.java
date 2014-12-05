package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.font.BitmapFont;
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
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Rectangle;
import com.jme3.scene.Spatial;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication implements AnalogListener {

    private ArrayList<Geometry> cubeField;
    private Node playerAndFloor;
    private boolean RUNNING;
    private ColorRGBA cubeColor;
    private float timeInterval = 0;
    private BitmapText startText;
    private BitmapText scoreText;
    private int score = 0;
    private double rollAngle;
    private LeapMotionListener listener;
    private Controller controller;
    private float turnSpeed;
    private AudioNode music;
    private Spatial futuristicPlane;
    
    public static void main(String[] args) {
        Main app = new Main();
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("CubeField");
        settings.setResolution(modes[0].getWidth(),modes[0].getHeight());
        settings.setFullscreen(true);
        app.setSettings(settings);
        app.start();
        
        
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        setupLeapMotion();
        Keys();
        setDisplayStatView(false); 
        setDisplayFps(false);
        playerAndFloor = createPlayerAndFloor();
        rootNode.attachChild(playerAndFloor);
        renderer.setBackgroundColor(ColorRGBA.Black);
        cubeColor = ColorRGBA.Red;
        createStartText();
        //createSun();
        
        music = new AudioNode(assetManager, "Sounds/Tron Legacy.wav", true);
        music.setPositional(false);
        music.play();
        rootNode.attachChild(music);
        cam.setLocation(playerAndFloor.getLocalTranslation().add(0, 2, -8));
        cam.lookAt(playerAndFloor.getLocalTranslation(), Vector3f.UNIT_Z);
               
        RUNNING = false;
        
        cubeField = new ArrayList<Geometry>();
        gameReset();


    }
    
    public void setupLeapMotion() {
        listener = new LeapMotionListener();
        controller = new Controller();
        controller.addListener(listener);
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
        floorMaterial.setColor("Color", ColorRGBA.Black);
        floorMesh.setMaterial(floorMaterial);
        floorMesh.setName("floor");
        return floorMesh;
    }
    
    public Geometry createPlayer() {
        Dome b = new Dome(Vector3f.ZERO, 10, 100, 0.5f);
        Geometry playerMesh = new Geometry("Player", b);
        Material playerMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        playerMaterial.setColor("Color", ColorRGBA.Red);
        playerMesh.setMaterial(playerMaterial);
        playerMesh.setName("player");
        return playerMesh;
        
//        futuristicPlane = assetManager.loadModel("Models/Futuristic plane/Futuristic plane.j3o");
//        futuristicPlane.scale(0.1f, 0.1f, 0.1f);    
//        futuristicPlane.rotate(0.0f, (float)Math.PI, 0.0f);
//        futuristicPlane.setLocalTranslation(0, 0, 0);
//        futuristicPlane.setName("plane");
//        futuristicPlane.setBoundRefresh();
//          
//        return futuristicPlane;
          
    }
    
    public void createSun() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);
    }
    
    public Node createPlayerAndFloor() {
        Spatial player = createPlayer();
        Geometry floor = createFloor();
        Node node = new Node();
        node.attachChild(player);
        node.attachChild(floor);
        return node;
    }
    
    public void createStartText() {
        startText = new BitmapText(guiFont, false);
        startText.setText("Press Enter to Start");
        startText.setSize(50);
        startText.setColor(ColorRGBA.Blue);
        startText.setLocalTranslation(100, startText.getLineHeight(), 0f);
        guiNode.attachChild(startText);
        
        
    }
    
    public void createScoreText() {
        scoreText = new BitmapText(guiFont, false);
        scoreText.setText("Score: ");
        scoreText.setSize(50);
        scoreText.setColor(ColorRGBA.Orange);
        scoreText.setLocalTranslation(300, scoreText.getLineHeight(), 0f);
        guiNode.attachChild(scoreText);
    }
    
    public Geometry createCube(Vector3f loc) {
        Box b = new Box(loc, 1, 1, 1);
        Geometry cubeMesh = new Geometry("Cube", b);
        Material cubeMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        cubeMaterial.setColor("Color", ColorRGBA.Red);
        cubeMaterial.getAdditionalRenderState().setWireframe(true);
        cubeMesh.setMaterial(cubeMaterial);
        cubeMesh.setName("cube");
        return cubeMesh;
    }
    
    public void addRandomCube() {
        
        int playerX = (int) playerAndFloor.getLocalTranslation().getX();
        int playerZ = (int) playerAndFloor.getLocalTranslation().getZ();
        
        float x = FastMath.nextRandomInt(playerX - 40, playerX + 40);
        float z = playerZ + 50;
        Vector3f v = new Vector3f(x, 1f, z);
        Geometry cube = createCube(v);
        rootNode.attachChild(cube);
        cubeField.add(cube);
        
    }

    public void gameReset() {
        score = 0;
        for (Geometry cube : cubeField) {
            cube.removeFromParent();
        }
        cubeField.clear();
        playerAndFloor.setLocalTranslation(0, 0, 0);
    }
    
    public void gameLost() {
        RUNNING = false;
        createStartText();
        
    }
    
    public void camBehind() {
        cam.setLocation(playerAndFloor.getLocalTranslation().add(0, 1, -3));
        Vector3f viewTarget = playerAndFloor.getLocalTranslation().add(0, 0, 5);
        cam.lookAt(viewTarget, Vector3f.UNIT_Z);
        
    }
    
    public void gameLogic(float tpf) {
        Vector3f v = new Vector3f(0.0f, 0.0f, 0.5f);
        playerAndFloor.move(v);
        score += 1;
        
        rollAngle = LeapMotionListener.getRoll();
        //System.out.println("rollAngle: " + rollAngle + " degrees");
        
        
        turnSpeed = .50f * (float)rollAngle / 90.0f;
        if (rollAngle > 0.0 && rollAngle < 179.0) {
            playerAndFloor.move(turnSpeed, 0, 0);
        } else if (rollAngle < 0.0) {
            playerAndFloor.move(turnSpeed, 0, 0);
        } 
        
//        if ((rollAngle > 0.0 && rollAngle < 90.0) || (rollAngle > -90.0) && rollAngle < 0.0) {
//            turnSpeed = .50f * (float)rollAngle / 90.0f;
//        } 
        
        timeInterval += tpf;
        if (timeInterval > 0.2) {
            addRandomCube();
            timeInterval = 0;
        }
        
        for (int i = 0; i < cubeField.size(); i++) {
            Spatial playerModel = playerAndFloor.getChild(0);
            BoundingVolume playerVolume = playerModel.getWorldBound();
            BoundingVolume cubeVolume = cubeField.get(i).getWorldBound();

            float cubeZ = cubeField.get(i).getLocalTranslation().getZ();
            float playerZ = playerAndFloor.getLocalTranslation().getZ();

            if (playerVolume.intersects(cubeVolume)) {
                gameLost();
                return;
            }
            if (cubeZ > playerZ) {
                cubeField.get(i).removeFromParent();
                cubeField.remove(cubeField.get(i));
            }
        }
        scoreText.setText("Score: " + score);
    }
    
    public void Keys() {
        inputManager.addMapping("START", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addListener(this, "START", "Left", "Right");
    }
    
    public void onAnalog(String binding, float value, float tpf) {
        if (binding.equals("START") && !RUNNING){
            RUNNING = true;
            guiNode.detachChild(startText);
            gameReset();
            System.out.println("START");
        }else if (RUNNING == true && binding.equals("Left")){
            playerAndFloor.move(.2f, 0, 0);
            //camAngle -= value*tpf;
        }else if (RUNNING == true && binding.equals("Right")){
            playerAndFloor.move(-.2f, 0, 0);
            //camAngle += value*tpf;
        }
    }
    
    public void setRollAngle(double angle) {
        rollAngle = angle;
    }
    
}
