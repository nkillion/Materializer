/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import javax.vecmath.Vector3d;

/**
 *
 * @author owner
 */
public class TerrainMoldController extends AbstractControl implements AnalogListener  {
    
    TerrainMold shape;
    Main main;
    RigidBodyControl shapeRB;
    float timer, max_timer;
    float yOld, zOld, xOld;
    Vector3f dir;
    
    public TerrainMoldController(TerrainMold shape, Vector3f dir){
        this.shape = shape;
        this.main = shape.main;
        this.max_timer = 500;
        this.dir = dir;
        yOld = this.shape.getLocalTranslation().getY();
        xOld = this.shape.getLocalTranslation().getX();
        zOld = this.shape.getLocalTranslation().getZ();
        
        InitPhysics();
    }
    
    private void InitPhysics(){
        shapeRB = new RigidBodyControl(10f); // Dynamic: mass > 0
        
        this.shape.addControl(shapeRB);
        
        shapeRB.setKinematic(Boolean.TRUE);
        
        main.bullet.getPhysicsSpace().add(shapeRB);
    }

    @Override
    protected void controlUpdate(float tpf) {
        
        if (timer < max_timer-1){
            timer ++;
            //shapeRB.setPhysicsLocation(Vector3f.ZERO);
            
            float velocity = .1f;
            float XDir = 45;
            float YDir = 45;
            float x;
            float y;
            float z;
            float dx;
            float dy;
            float dz;
            float height;
            
            /* old pitch */
            Quaternion pitchOld = this.shape.getLocalRotation();

            /* Create a 90-degree-pitch Quaternion. */
            Quaternion pitchNew = new Quaternion();
            pitchNew.fromAngleAxis(FastMath.DEG_TO_RAD * 45, new Vector3f(1,0,0));
            
            /* get shape height */
            height = this.shape.getLocalTranslation().getY();
            
            
            /* update position */
            y = (1 * (yOld - ( dir.getY() ) + ( dir.getY() * (Math.max(timer, .001f)/max_timer))));
            x = (1 * (xOld - ( dir.getX() ) + ( dir.getX() * (Math.max(timer, .001f)/max_timer))));
            z = (1 * (zOld - ( dir.getZ() ) + ( dir.getZ() * (Math.max(timer, .001f)/max_timer))));
            
            this.shape.setLocalTranslation(x, y, z);
            
            //Vector3f t = new Vector3f();
            //t.get
                    
            pitchNew.lookAt(new Vector3f(x,y,z), Vector3f.ZERO);

            //this.shape.setLocalRotation(Quaternion.ZERO);
            this.shape.setLocalRotation(pitchNew);
            
            /* Apply the rotation to the object */
            //this.shape.setLocalRotation(pitchOld.mult(pitchNew));
            
            System.out.println(timer);
            
            /* Update the model. Now it's vertical. */
            this.shape.updateModelBound();
            this.shape.updateGeometricState();
            
        }
        else{

        }
          
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