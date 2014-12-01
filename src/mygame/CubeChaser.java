package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
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
import com.jme3.math.Ray;

/**
 * test
 * @author normenhansen
 */
public class CubeChaser extends SimpleApplication {

    private Ray ray = new Ray();
    
    public static void main(String[] args) {
        CubeChaser app = new CubeChaser();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        makeCubes(40);
        
        
    }
    private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);
    public void makeCubes(int num) {
        for (int i = 0; i < num; i++) {
            Vector3f loc = new Vector3f(
                    FastMath.nextRandomInt(-20, 20),
                    FastMath.nextRandomInt(-20, 20),
                    FastMath.nextRandomInt(-20, 20));
            rootNode.attachChild(myBox("Box" + i, loc, ColorRGBA.randomColor()));
        }
    }
    
    public Geometry myBox(String name, Vector3f loc, ColorRGBA color) {
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);
        return geom;
    }

    @Override
    public void simpleUpdate(float tpf) {
        CollisionResults results = new CollisionResults();
        ray.setOrigin(cam.getLocation());
        ray.setDirection(cam.getDirection());
        rootNode.collideWith(ray, results);
        if (results.size() > 0) {
            Geometry target = results.getClosestCollision().getGeometry();
            Vector3f camLoc = cam.getLocation();
            Vector3f cubeLoc = target.getLocalTranslation();
            float dist = camLoc.distance(cubeLoc);
            if (dist < 10) {
                target.move(cam.getDirection());
            }
        }
    }
       

    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
