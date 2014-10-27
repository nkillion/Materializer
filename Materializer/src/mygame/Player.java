package mygame;

import com.bulletphysics.dynamics.RigidBody;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.SafeArrayList;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

public class Player implements ActionListener {

    Main main;
    private Node pNode;
    private CharacterControl pControl;
    private CapsuleCollisionShape pShape;
    private Camera cam;
    private Vector3f walkDir, viewDir, pLoc;
    private boolean forward, backward, left, right, isCrouched, hasObj;
    InputManager inputManager;
    CollisionResults results;
    Shape currentObj;

    public Player(Camera cam, BulletAppState bullet, Main main) {
        forward = backward = left = right = isCrouched = hasObj = false;
        this.main = main;
        inputManager = main.getInputManager();

        pNode = new Node("Player Node");
        pShape = new CapsuleCollisionShape(.8f, 1.8f);
        pControl = new CharacterControl(pShape, .1f);
        pControl.setPhysicsLocation(new Vector3f(0f, 5f, 0f));
        pControl.setGravity(10f);
        pControl.setJumpSpeed(10f);
        pNode.addControl(pControl);
        main.getRootNode().attachChild(pNode);
        bullet.getPhysicsSpace().add(pControl);

        this.cam = cam;
        walkDir = new Vector3f(0f, 0f, 0f);
        pLoc = new Vector3f(0f, 0f, 0f);
        viewDir = new Vector3f(0f, 0f, 0f);

        resetPlayer();
    }

    public Vector3f getLoc() {
        return pLoc;
    }

    public Vector3f getViewDir() {
        return viewDir;
    }

    public void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Drop", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Crouch", new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("Grenade", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addMapping("Action", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	inputManager.addMapping("Pickup", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Forward");
        inputManager.addListener(this, "Backward");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Crouch");
        inputManager.addListener(this, "Grenade");
        inputManager.addListener(this, "Drop");
        inputManager.addListener(this, "Action");
	inputManager.addListener(this, "Pickup");
    }
	//change to switch?!
    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Left")) {
            if (isPressed) {
                left = true;
            } else {
                left = false;
            }
        } else if (binding.equals("Right")) {
            if (isPressed) {
                right = true;
            } else {
                right = false;
            }
        } else if (binding.equals("Forward")) {
            if (isPressed) {
                forward = true;
            } else {
                forward = false;
            }
        } else if (binding.equals("Backward")) {
            if (isPressed) {
                backward = true;
            } else {
                backward = false;
            }
        } else if (binding.equals("Jump")) {
            if (isPressed) {
                pControl.jump();
            }
        } else if (binding.equals("Drop")) {
            if (isPressed){
                drop();
                System.out.println("dropping");
            }
        } else if (binding.equals("Grenade")) {
            if (isPressed) {
                initGui();
            }
	} else if (binding.equals("Pickup")) {
	    if (isPressed) {
		SafeArrayList<Spatial> objects = (SafeArrayList) main.getRootNode().getChildren();
		CollisionResults colRes = new CollisionResults();
		Vector3f playerPos = pNode.getWorldTranslation();
		Ray ray = new Ray(this.getLoc(), cam.getDirection());
		Vector3f objPos;
		
		for (Spatial obj : objects){
		    objPos = obj.getWorldTranslation();
		    if (playerPos.distance(objPos) < 5) {
			obj.collideWith(ray, colRes);
			if(colRes.size() > 0 ){
			    System.out.println("pickup");
			}
		    }
		}
	    }
        } else if (binding.equals("Action")) {
            if (!isPressed) {
                System.out.println("dropped grenade");
                Vector3f dir = new Vector3f();

                Vector3f playerPos = pControl.getPhysicsLocation();
                Vector3f vDir = pControl.getViewDirection();
                
                Grenade test = new Grenade(main);
                main.getRootNode().attachChild(test);
                
                vDir.mult(15f);
                playerPos = playerPos.add(vDir);
               // playerPos = playerPos.add(new Vector3f(0f, 5f, 0f));
                //dir.setZ(30f);
                //dir.y = 0f;
                //dir.z = 0;
                test.controller.getRigidBodyControl().setPhysicsLocation(playerPos);
                //test.controller.getRigidBodyControl().setCollisionGroup(1);
                //test.controller.getRigidBodyControl().removeCollideWithGroup(2);
                //pControl.removeCollideWithGroup(1);
                //test.
                test.controller.getRigidBodyControl().setLinearVelocity(cam.getDirection().mult(5));
            }
        }

    }

    public void resetPlayer() {
        pControl.warp(new Vector3f(25f, 3f, 6f));
    }
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("PickUp")) {
                // Reset results list.
                CollisionResults results = new CollisionResults();
                // Aim the ray from camera location in camera direction
                // (assuming crosshairs in center of screen).
                Ray ray = new Ray(pLoc, viewDir);
                // Collect intersections between ray and all nodes in results list.
                main.getRootNode().collideWith(ray, results);
                // Print the results so we see what is going on
                for (int i = 0; i < results.size(); i++) {
                    // For each “hit”, we know distance, impact point, geometry.
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    String target = results.getCollision(i).getGeometry().getName();
                    if (dist < 10) {
                        System.out.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
                    }
                }
            }
        }
    };

    public void update(float tpf) {
        Vector3f camDir = cam.getDirection().mult(0.2f);
        Vector3f camLeft = cam.getLeft().mult(0.2f);
        camDir.y = 0;
        camLeft.y = 0;
        viewDir.set(camDir);
        walkDir.set(0f, 0f, 0f);
        if (left) {
            walkDir.addLocal(camLeft);
        } else if (right) {
            walkDir.addLocal(camLeft.negate());
        }
        if (forward) {
            walkDir.addLocal(camDir);
        } else if (backward) {
            walkDir.addLocal(camDir.negate());
        }
        pControl.setWalkDirection(walkDir);
        pControl.setViewDirection(viewDir);

        pLoc.set(pNode.getLocalTranslation());
        cam.setLocation(pLoc);
        if (pLoc.getY() < -1) {
            resetPlayer();
        }

        if (!hasObj) {
            for (Shape s : main.shapes) {
                if (pLoc.distance(s.getWorldTranslation()) < 4f) {
                    System.out.println("obj in range");
                    pickUp(s);
                    hasObj = true;
                    currentObj = s;
                }
            }
        }


    }

    public void initGui() {
        main.app = new App(main);
    }

public void pickUp(Shape s) {
        s.setLocalTranslation(pLoc);
        currentObj = s;
        main.getRootNode().detachChild(s);
        System.out.println("picking up");
    }


    public void drop() {
        main.getRootNode().attachChild(currentObj);
        currentObj.controller.getRigidBodyControl().setPhysicsLocation(pLoc);
        currentObj.setLocalTranslation(pLoc.add(10f, 0f, 10f));
       
        System.out.println("dropping");
    }
}
