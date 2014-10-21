/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.math.Vector3f;

/**
 *
 * @author Yaw Asabere 
 * 
 */
public class Grenade extends Node {
    
    Main main;
    static Geometry geo_grenade;
    GrenadeController controller;
    Game game;
    
    public Grenade(Main main){
        
        this.main = main;
        
        this.initGeometries();
        this.initMaterials();
        
        this.controller = new GrenadeController(this);
        this.addControl(this.controller);

    }
    
    private void initGeometries(){
        Sphere s = new Sphere(10, 10, .1f);
        geo_grenade = new Geometry("Grenade", s);
        this.attachChild(geo_grenade);
    }

    private void initMaterials() {
        Material mat = new Material(main.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        geo_grenade.setMaterial(mat);
    }

    void controlUpdate(float tpf) {

    }
    
}