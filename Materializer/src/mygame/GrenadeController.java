/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.bulletphysics.dynamics.RigidBody;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Yaw Asabere
 */
public class GrenadeController  extends AbstractControl implements AnalogListener  {
    
    private Main main;
    private Grenade grenade;
    private RigidBodyControl grenadeRB;
    private int timer = 50;
    private boolean live = true;
    private Vector3f dir = new Vector3f();
    
    public GrenadeController(Grenade grenade){
        this.main = grenade.main;
        this.grenade = grenade;
        InitPhysics();
    }
    
    private PhysicsSpace getPhysicsSpace(){
        return main.bullet.getPhysicsSpace();
    }
    
    private void InitPhysics(){
        grenadeRB = new RigidBodyControl(10.0f); // Dynamic: mass > 0
        
       //grenadeRB.setCcdSweptSphereRadius(.5f);
        this.grenade.geo_grenade.addControl(grenadeRB);
        main.bullet.getPhysicsSpace().add(grenadeRB);
    }
    
    protected void controlUpdate(float tpf) {
        
        if (live){
            if (timer > 0){
                timer -= 1;
            }
            else {
                live = false;
                //this.grenade = null;
                //this = null;
                
                main.getRootNode().detachChild(grenade);
                
                makeShape();
                
                System.out.println("explosion");
            }
        }
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onAnalog(String name, float value, float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void makeShape(){
       
       grenadeRB.destroy();
       grenadeRB.setEnabled(false);
        
       Vector3f grenadePos = grenadeRB.getPhysicsLocation();  
        
       
       if (true){
           Shape shape = new Shape(main);
           shape.rotate(90 * FastMath.DEG_TO_RAD, 0, 0);
           
           main.getRootNode().attachChild(shape);
           main.shapes.add(shape);

           shape.controller.getRigidBodyControl().setPhysicsLocation(grenadePos);
           shape.setLocalTranslation(grenadePos);
           shape.setLocalTranslation(new Vector3f(0, 1, 0));
       }
       else{
           TerrainMold shape = new TerrainMold(main, dir);
       }
       
    }
    
    public RigidBodyControl getRigidBodyControl(){
        return grenadeRB;
    }
    
    public void setDir(Vector3f newDir){
        this.dir.set(newDir.getX(), newDir.getY(), newDir.getZ());
    }
    
}
