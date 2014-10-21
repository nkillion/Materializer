/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.effect.ParticleEmitter;
import com.jme3.material.Material;
import com.jme3.effect.ParticleMesh;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import java.util.ArrayList;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author tud47465
 */
public class Level extends Node {

    AssetManager asm;
    public Spatial level;
    RigidBodyControl scenePhys;
    int levelNum;
    Main main;
    Geometry doorOne, doorTwo;
    Node node, node2, node3, node4, node5;
    int sceneCollisionGroup = 1;
    int playerCollisionGroup = 2;
    PointLight alarm_light;
    boolean light;

    public Level(int lvlNum, Main main) {

        
        this.main = main;
        asm = main.getAssetManager();
        levelNum = lvlNum;

        if (lvlNum == 1) {
            loadLevel(1);
        } else if (lvlNum == 2) {
            loadLevel(2);
        } else if (lvlNum == 3) {
            loadLevel(3);
        }

    }

    public void loadLevel(int lvl) {

        if (lvl == 1) {

            level = asm.loadModel("Scenes/level_1.j3o");
            level.scale(2);

            
            //Alarm Light for level_1
            alarm_light = new PointLight();
            alarm_light.setColor(ColorRGBA.Red);
            alarm_light.setRadius(30f);
            alarm_light.setPosition(new Vector3f(1, 11.5f, 6));
            level.addLight(alarm_light);
            light = true;
            
            //level_1 Collision Shape #2
            GhostControl ghost = new GhostControl(new BoxCollisionShape(new Vector3f(.7f,.1f,1.4f)));  // a box-shaped ghost
            Node node = new Node("node1");
            node.addControl(ghost);
            node.setLocalTranslation(6.6f, 3.95f,7.8f);
            main.getRootNode().attachChild(node);
            main.bullet.getPhysicsSpace().add(ghost);
            
            //level_1 Collision Shape #2
            GhostControl ghost2 = new GhostControl(new BoxCollisionShape(new Vector3f(.7f,.1f,1.4f)));  // a box-shaped ghost
            Node node2 = new Node("node2");
            node2.addControl(ghost2);
            node2.rotate(0f, 0f, 90.0f * FastMath.DEG_TO_RAD);
            node2.setLocalTranslation(5.8f, 6.6f,4.6f);
            main.getRootNode().attachChild(node2);
            main.bullet.getPhysicsSpace().add(ghost2);
            
            //level_1 Collision Shape #3
            GhostControl ghost3 = new GhostControl(new BoxCollisionShape(new Vector3f(.7f,.1f,1.4f)));  // a box-shaped ghost
            Node node3 = new Node("node3");
            node3.addControl(ghost3);
            node3.setLocalTranslation(12.45f, -.05f, 6.1f);
            main.getRootNode().attachChild(node3);
            main.bullet.getPhysicsSpace().add(ghost3);
            

//            if (Grenade.geo_grenade.collideWith(node, true)){
//                Shape.geo.setLocalTranslation(12.45f, -.05f, 6.1f);
//            }
            
        } else if (lvl == 2) {

            main.getRootNode().detachChildNamed("mesh");
            
            level = asm.loadModel("Scenes/level_2.j3o");
            level.scale(2);

            //Alarm Light for level_2
            alarm_light = new PointLight();
            alarm_light.setColor(ColorRGBA.Red);
            alarm_light.setRadius(30f);
            alarm_light.setPosition(new Vector3f(1, 8, 6.4f));
            level.addLight(alarm_light);
            light = true;
            
            //level_2 Collision Shape
            GhostControl ghost4 = new GhostControl(new BoxCollisionShape(new Vector3f(6.5f, .1f, 2.9f)));  // a box-shaped ghost
            Node node4 = new Node("node4");
            node4.addControl(ghost4);
            node4.setLocalTranslation(14f, 12.83f, 6f);
            main.getRootNode().attachChild(node4);
            main.bullet.getPhysicsSpace().add(ghost4);

            //particles for fire level_2
            ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            fire.setLocalTranslation(new Vector3f(13, -2.9f, 6f));
            Material mat_red = new Material(asm, "Common/MatDefs/Misc/Particle.j3md");
            mat_red.setTexture("Texture", asm.loadTexture("Effects/Explosion/flame.png"));
            fire.setMaterial(mat_red);
            fire.setImagesX(2);
            fire.setImagesY(2); // 2x2 texture animation
            fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
            fire.setStartColor(new ColorRGBA(1f, .5f, 0f, 0.5f)); // yellow
            fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3, 0));
            fire.setStartSize(10f);
            fire.setEndSize(.1f);
            fire.setGravity(0, 0, 0);
            fire.setLowLife(.5f);
            fire.setHighLife(3f);
            fire.setName("fire");
            fire.getParticleInfluencer().setVelocityVariation(0.3f);
            main.getRootNode().attachChild(fire);
            
            //audio for fire
            AudioNode fireAudio = new AudioNode(asm, "Sounds/fire.ogg");
            fireAudio.setPositional(true); 
            fireAudio.setLocalTranslation(13,-2.9f,6f);
            fireAudio.setDirectional(false);
            fireAudio.setVolume(1);
            fireAudio.setLooping(true);
            main.getRootNode().attachChild(fireAudio);
            fireAudio.play();

            
        } else {
            main.getRootNode().detachChildNamed("mesh");

            level = asm.loadModel("Scenes/level_3.j3o");
            level.scale(2);
            


            //Alarm Light for level_3 ***POSITION
            alarm_light = new PointLight();
            alarm_light.setColor(ColorRGBA.Red);
            alarm_light.setRadius(30f);
            alarm_light.setPosition(new Vector3f(1, 8, 4f));
            level.addLight(alarm_light);
            light = true;

            //Second Alarm Light for level_3  ***POSITION
            PointLight alarm_light2 = new PointLight();
            alarm_light2.setColor(ColorRGBA.Red);
            alarm_light2.setRadius(30f);
            alarm_light2.setPosition(new Vector3f(25f, 3.7f, 8.8f));
            level.addLight(alarm_light2);
            
            //level_3 Collision Shape 
            GhostControl ghost5 = new GhostControl(new BoxCollisionShape(new Vector3f(.9f, .1f, .5f)));  // a box-shaped ghost
            Node node5 = new Node("node5");
            node5.addControl(ghost5);
            node5.setLocalTranslation(23.3f, -.05f, 2.65f);
            main.getRootNode().attachChild(node5);
            main.bullet.getPhysicsSpace().add(ghost5);
            
//                    level_3 door #1
        Box box = new Box(.1f, 1.8f, 1.7f);
        doorOne = new Geometry("ground", box);
        doorOne.setMaterial(main.magenta);
        doorOne.setLocalTranslation(20.4f, 1.8f,6.2f);
        main.getRootNode().attachChild(doorOne);
        //level_3 door #1 physics
        doorOne.addControl(new RigidBodyControl(.001f));
        doorOne.getControl(RigidBodyControl.class).setRestitution(1);
        main.getRootNode().attachChild(doorOne);
        main.bullet.getPhysicsSpace().add(doorOne);
        
//        level_3 door #2
        Box box2 = new Box(.1f, 2.2f, 1.7f);
        doorTwo = new Geometry("ground", box2);
        doorTwo.setMaterial(main.magenta);
        doorTwo.setLocalTranslation(4.8f, 5.4f,6.2f);
        main.getRootNode().attachChild(doorTwo);
        //level_3 door #2 physics
        doorTwo.addControl(new RigidBodyControl(.001f));
        doorTwo.getControl(RigidBodyControl.class).setRestitution(1);
        main.getRootNode().attachChild(doorTwo);
        main.bullet.getPhysicsSpace().add(doorTwo);

        }

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) level);
        scenePhys = new RigidBodyControl(sceneShape, 0);
        scenePhys.setCollisionGroup(sceneCollisionGroup);
        level.addControl(scenePhys);
    }

    public void addToScene(BulletAppState bas, Node root) {

        root.attachChild(level);
        bas.getPhysicsSpace().add(scenePhys);

    }

    public void removeFromScene(BulletAppState bas, Node root) {

        
        for (Shape shape : main.shapes)
            main.getRootNode().detachChild(shape);
        main.shapes.clear();
        
        root.detachChild(level);
        bas.getPhysicsSpace().remove(scenePhys);
        
        if (levelNum == 2)
            root.detachChildNamed("fire");
    }
    
    public ArrayList<Node> getGhosts() {
        ArrayList<Node> ghosts = new ArrayList<Node>();
        
        if (levelNum == 1) {
            ghosts.add((Node) main.getRootNode().getChild("node1"));
            ghosts.add((Node) main.getRootNode().getChild("node2"));
            ghosts.add((Node) main.getRootNode().getChild("node3"));
        }
        
        else if (levelNum == 2) {
            ghosts.add((Node) main.getRootNode().getChild("node4"));
        }
        else {
            ghosts.add((Node) main.getRootNode().getChild("node5"));
        }
        
        return ghosts;
    }
    
    public void updateLight(float tpf) {
        if ((main.secs % 1) > 0.5f && light) {
            level.removeLight(alarm_light);
            light = false;
        }
        else if ((main.secs % 1) < 0.5f && !light) {
            level.addLight(alarm_light);
            light = true;
        }
    }
}
