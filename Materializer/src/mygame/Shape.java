/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author tub97573
 */
public class Shape extends Node {
    
    Main main;
    static Geometry geo;
    Material mat1;
    ShapeController controller;
    
    public Shape(Main main){
     this.main = main;
    
     initGeometries();
     initMaterials();
     
     this.controller = new ShapeController(this);
     this.addControl(this.controller);
     
     this.main.shapes.add(this);
     
    }
    
    private void initMaterials() {
        mat1 = new Material(main.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");


       // mat1.setColor("Color", ColorRGBA.Blue);
        //mat1.setFloat("Shininess", 4f); // shininess from 1-128
        
        mat1.setBoolean("VertexColor", true);

        geo.setMaterial(mat1);
    }
    
    private void initGeometries(){
        
        Vector3f[] vertices = new Vector3f[main.app.verts.size()];
        
        for(int i = 0; i < main.app.verts.size(); i++)
            vertices[i] = (Vector3f)main.app.verts.get(i);
        
        CustomMesh box = new CustomMesh(vertices, .5f, true);
        geo = new Geometry("shape", box);
        
        
        
        this.attachChild(geo);
    }
    
}
