/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author crisscrosskao
 */
public class Cube extends Geometry {
    
    private Vector3f loc;
    private ColorRGBA col;
    private AssetManager assetManager;
  
    public Cube(Vector3f inLoc, ColorRGBA col, AssetManager manager) {
        super("Box");
        loc = inLoc;
        assetManager = manager;
        Box b = new Box(loc, 1, 1, 1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", col);
        this.setMesh(b);
        this.setMaterial(mat);
    }
    
    public void setColor(ColorRGBA color) {
        col = color;
    }
    
}
