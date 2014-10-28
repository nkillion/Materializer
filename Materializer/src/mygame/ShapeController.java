/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author owner
 */
public class ShapeController extends AbstractControl implements AnalogListener  {
    
    Shape shape;
    Main main;
    RigidBodyControl shapeRB;
    
    public ShapeController(Shape shape){
        this.shape = shape;
        this.main = shape.main;
        
        InitPhysics();
    }
    
    private void InitPhysics(){
        shapeRB = new RigidBodyControl(3f); // Dynamic: mass > 0
        this.shape.addControl(shapeRB);
        
        shapeRB.setCollisionGroup(3);
        //shapeRB.
        
        main.bullet.getPhysicsSpace().add(shapeRB);
    }

    @Override
    protected void controlUpdate(float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onAnalog(String name, float value, float tpf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public RigidBodyControl getRigidBodyControl(){
        return shapeRB;
    }
}
