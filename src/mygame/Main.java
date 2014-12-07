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
import com.jme3.math.Quaternion;
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
    private float secondsElapsed = 0;
    private BitmapText startText;
    private BitmapText scoreText;
    private int score = 0;
    private double rollAngle;
    private LeapMotionListener listener;
    private Controller controller;
    private float turnSpeed;
    private AudioNode music;
    private AudioNode collisionSound;
    private double spawnInterval;
    private Spatial player;
    private Geometry floor;
    
    private double currentRollAngle;
    private double previousRollAngle;
    private double changeInRollAngle;
            
    
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
        //playerAndFloor = createPlayerAndFloor();
        player = createFuturisticPlane();
        floor = createFloor();
        rootNode.attachChild(player);
        rootNode.attachChild(floor);
        //rootNode.attachChild(playerAndFloor);
        cubeColor = ColorRGBA.Red;
        createStartText();
        createScoreText();
        createSun();
        previousRollAngle = 0;
        
        renderer.setBackgroundColor(ColorRGBA.Black);
        createSounds();
        cam.setLocation(player.getLocalTranslation().add(0, 2, -8));
        cam.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Z);
               
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
        if (listener.handAbove() && RUNNING) {
            gameLogic(tpf);
        }
        camBehind();
     
    }
    
    public Geometry createFloor() {
        Vector3f v = new Vector3f(0.0f, -2.0f, 50.0f);
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
    }
    
    public Spatial createFuturisticPlane() {
        Spatial futuristicPlane = assetManager.loadModel("Models/Futuristic plane.j3o");
        futuristicPlane.rotate(0.0f, (float)Math.PI, 0.0f);
        futuristicPlane.setLocalTranslation(0, 0, 0);
        futuristicPlane.scale(0.1f, 0.1f, 0.1f);
        return futuristicPlane;
    }
    
    public Node createPlayerAndFloor() {
        //Geometry player = createPlayer();
        Spatial player = createFuturisticPlane();
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
        
        int playerX = (int) player.getLocalTranslation().getX();
        int playerZ = (int) player.getLocalTranslation().getZ();
        
        float x = FastMath.nextRandomInt(playerX - 40, playerX + 40);
        float z = playerZ + 50;
        Vector3f v = new Vector3f(x, 1f, z);
        Geometry cube = createCube(v);
        rootNode.attachChild(cube);
        cubeField.add(cube);
    }

    public void createSounds() {
        music = new AudioNode(assetManager, "Sounds/Tron Legacy.wav", false);
        music.setPositional(false);
        music.setLooping(true);
        rootNode.attachChild(music);
        music.playInstance();
        collisionSound = new AudioNode(assetManager, "Sounds/Smashing Sound.wav", false);
        collisionSound.setPositional(false);
        rootNode.attachChild(collisionSound);
    }
    
    public void createSun() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);
    }
    
    public void gameReset() {
        score = 0;
        secondsElapsed = 0;
        previousRollAngle = 0.0;
        for (Geometry cube : cubeField) {
            cube.removeFromParent();
        }
        cubeField.clear();
        player.setLocalTranslation(0, 0, 0);
        floor.setLocalTranslation(0, 0, 0);
    }
    
    //gameLost() stops game and animates a collision with sound effects.
    public void gameLost() {
        collisionSound.play();
        RUNNING = false;
        createStartText();
        
    }
    
    public void camBehind() {
        cam.setLocation(player.getLocalTranslation().add(0, 1, -3));
        Vector3f viewTarget = player.getLocalTranslation().add(0, 0, 5);
        cam.lookAt(viewTarget, Vector3f.UNIT_Z);
        
    }
    
    public void gameLogic(float tpf) {
        Vector3f v = new Vector3f(0.0f, 0.0f, 0.5f);
        player.move(v);
        floor.move(v);
        score += 1;
        previousRollAngle = currentRollAngle;
        currentRollAngle = LeapMotionListener.getRoll();
        System.out.println("previousRollAngle: " + previousRollAngle);
        System.out.println("currentRollAngle: " + currentRollAngle);
        //Quaternion quat = new Quaternion(0f, (float)Math.PI, (float) (rollAngle * -1.0 * Math.PI / 180.0), 1);
        //System.out.println("rollAngle: " + rollAngle + " degrees");
        changeInRollAngle = currentRollAngle - previousRollAngle;
        
        turnSpeed = .50f * (float)currentRollAngle / 90.0f;
        if (currentRollAngle > 0.0 && currentRollAngle < 179.0) {
            player.move(turnSpeed, 0, 0);
            floor.move(turnSpeed, 0, 0);
            player.rotate(0f, 0f, (float)(changeInRollAngle / 180.0 * 3.0));
        } else if (currentRollAngle < 0.0) {
            player.move(turnSpeed, 0, 0);
            floor.move(turnSpeed, 0, 0);
            player.rotate(0f, 0f, (float)(changeInRollAngle / 180.0 * 3.0));
        } 
        secondsElapsed += tpf;
        spawnInterval = -1.312 * Math.pow(10, -7) * Math.pow(secondsElapsed, 3) + 
                3.958 * Math.pow(10, -5) * Math.pow(secondsElapsed, 2) - 
                .0044 * secondsElapsed + .2;
        timeInterval += tpf;
        if (timeInterval > spawnInterval) {
            addRandomCube();
            timeInterval = 0;
        }
        
        for (int i = 0; i < cubeField.size(); i++) {
            //Geometry playerModel = (Geometry) playerAndFloor.getChild(0);
            BoundingVolume playerVolume = player.getWorldBound();
            BoundingVolume cubeVolume = cubeField.get(i).getWorldBound();

            float cubeZ = cubeField.get(i).getLocalTranslation().getZ();
            float playerZ = player.getLocalTranslation().getZ();

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
            player.move(.2f, 0, 0);
            floor.move(.2f, 0, 0);
            //camAngle -= value*tpf;
        }else if (RUNNING == true && binding.equals("Right")){
            player.move(-.2f, 0, 0);
            floor.move(-.2f, 0, 0);
            //camAngle += value*tpf;
        }
    }
    
}
