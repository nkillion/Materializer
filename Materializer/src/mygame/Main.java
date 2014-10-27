package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.jme3.system.AppSettings;


/**
 * test
 * @author 
 */
public class Main extends SimpleApplication{

  
  Game game;
  private Spatial level_1;
  Node loadedNode;
  BulletAppState bullet;
  RigidBodyControl scenePhys;
  Material mat1, magenta;
  Geometry doorOne, doorTwo, colShape1, colShape2, colShape3;
  Geometry key1pt_1, key1pt_2, key1pt_3, key1pt_4, key2pt_1, key2pt_2, key2pt_3;
  Light alarm_light;
  App app;
  ArrayList<Shape> shapes = new ArrayList<Shape>();

  public Player player;
  int currentX, currentY, oldX, oldY;
  Geometry geomBox, geo;
  Vector3f[] vertices;
  
  String timeStr;
  float secs;
  BitmapText hudText;
    

    
    public static void main(String[] args) {

       Main app = new Main();
       AppSettings start = new AppSettings(false);
       start.setSettingsDialogImage("Interface/resources/background.png");
       app.setSettings(start);
       app.start();
    }
       
        
    

    @Override
    public void simpleInitApp() {
        
        initCrosshairs();
        initLights();
        initMaterial();
        initClock();
        
        bullet = new BulletAppState();
        stateManager.attach(bullet);
        
        initGeometries();
        player = new Player(cam, bullet, this);
        player.setUpKeys();
	
        StartScreen ss = new StartScreen();
        stateManager.attach(ss);
        
        
        }
    
    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
        updateClock(tpf);
        

            }

   
    
    public void initGeometries(){
               
        
        //level_3 Key1 Collision Shapes
//          Cylinder cyl1 = new Cylinder(15,15,.01f,.5f);
//          key1pt_1 = new Geometry("ground", cyl1);
//          key1pt_1.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key1pt_1.setMaterial(magenta);
//          key1pt_1.setLocalTranslation(20.625f, 2f,8.35f);
//          rootNode.attachChild(key1pt_1);
//          
//          Cylinder cyl2 = new Cylinder(15,15,.01f,.5f);
//          key1pt_2 = new Geometry("ground", cyl2);
//          key1pt_2.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key1pt_2.setMaterial(magenta);
//          key1pt_2.setLocalTranslation(20.625f, 2f,8.75f);
//          rootNode.attachChild(key1pt_2);
//          
//          Cylinder cyl3 = new Cylinder(15,15,.01f,.5f);
//          key1pt_3 = new Geometry("ground", cyl3);
//          key1pt_3.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key1pt_3.setMaterial(magenta);
//          key1pt_3.setLocalTranslation(20.625f, 2.35f,8.35f);
//          rootNode.attachChild(key1pt_3);
//          
//          Cylinder cyl4 = new Cylinder(15,15,.01f,.5f);
//          key1pt_4 = new Geometry("ground", cyl4);
//          key1pt_4.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key1pt_4.setMaterial(magenta);
//          key1pt_4.setLocalTranslation(20.625f, 2.35f,8.75f);
//          rootNode.attachChild(key1pt_4);
        
                //level_3 Key2 Collision Shapes 
//        
//     
//          Cylinder cyl1 = new Cylinder(15,15,.01f,.5f);
//          key2pt_1 = new Geometry("ground", cyl1);
//          key2pt_1.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key2pt_1.setMaterial(magenta);
//          key2pt_1.setLocalTranslation(4.8f, 6.2f,8.54f);
//          rootNode.attachChild(key2pt_1);
//          
//          Cylinder cyl2 = new Cylinder(15,15,.01f,.5f);
//          key2pt_2 = new Geometry("ground", cyl2);
//          key2pt_2.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key2pt_2.setMaterial(magenta);
//          key2pt_2.setLocalTranslation(4.8f, 5.92f,8.47f);
//          rootNode.attachChild(key2pt_2);
//          
//          Cylinder cyl3 = new Cylinder(15,15,.01f,.5f);
//          key2pt_3 = new Geometry("ground", cyl3);
//          key2pt_3.rotate(0f, 90.0f * FastMath.DEG_TO_RAD, 0f);
//          key2pt_3.setMaterial(magenta);
//          key2pt_3.setLocalTranslation(4.8f, 5.79f,8.69f);
//          rootNode.attachChild(key2pt_3);
    }
    
    protected void initCrosshairs() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }
        
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
        public void initLights() {
            
          //white ambient
//        AmbientLight ambient = new AmbientLight();
//        ambient.setColor(ColorRGBA.White);
//        rootNode.addLight(ambient);
        
            //white directional
            DirectionalLight sun = new DirectionalLight();
            sun.setDirection((new Vector3f(-0.3f, -0.4f, -0.5f)).normalizeLocal());
            sun.setColor(ColorRGBA.White);
            rootNode.addLight(sun);

        //shadow
//        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 2);
//        dlsr.setLight(sun);
//        viewPort.addProcessor(dlsr); 
        
    }
        
         protected static void clearJMonkey(Main m) {
         m.guiNode.detachAllChildren();
         //m.rootNode.detachAllChildren();
         m.inputManager.clearMappings();
    }
         
       
        
        public void initMaterial() {
             magenta = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        magenta.setBoolean("UseMaterialColors", true);
	magenta.setColor("Ambient", ColorRGBA.Gray);
        magenta.setColor("Diffuse", ColorRGBA.Blue);
        magenta.setColor("Specular", ColorRGBA.Red);
        magenta.setFloat("Shininess", 2f); // shininess from 1-128
        }
        
        public void initCam() {
            flyCam.setEnabled(true);
            flyCam.setMoveSpeed(40);
            cam.setLocation(new Vector3f(25, 3, 6));
            cam.lookAt(cam.getLocation(), Vector3f.UNIT_X);
        }

        public void initClock() {
        if (!(hudText == null)){
            guiNode.detachChild(hudText);
        }
        secs = 265;
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize() * 2);      // font size
        hudText.setColor(ColorRGBA.Red);                             // font color
        hudText.setText("Loading...");             // the text
        hudText.setLocalTranslation((settings.getWidth() / 2) - 50, settings.getHeight(), 0); // position
        guiNode.attachChild(hudText);
    }
    
    public void updateClock(float tpf){
        secs -= tpf;
        if (secs <= 0){
            timeStr = "Time's up!";
            game.lvl.removeFromScene(bullet, rootNode);
            game.lvl.addToScene(bullet, rootNode);
        }
        
        hudText.setText((int)secs/60 + ":" + (((int)secs%60 < 10) ? "0"+(int)secs%60 : (int)secs%60));
    }
}
