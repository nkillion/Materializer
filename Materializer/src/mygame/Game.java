/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
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
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Logger;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.FilterPostProcessor;

/**
 * test
 * @author 
 */
public class Game extends AbstractAppState {

  

  public Level lvl;
  Node loadedNode;
  private BulletAppState bullet;
  RigidBodyControl scenePhys;
  Material mat1, magenta;
  Geometry doorOne, doorTwo;
  Light alarm_light;
  FogFilter fog;
  Main main;
  AppStateManager asm;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        
        main = (Main) app;
        asm = stateManager;
        bullet = main.bullet;
        
        initFog();
        initAudio();
        
        lvl = new Level(1, main);
        lvl.addToScene(bullet, main.getRootNode());
    }
    
    @Override
    public void update(float tpf) {
        main.player.update(tpf);
        System.out.println(main.player.getLoc().x);
        
        lvl.updateLight(tpf);
        
        if (main.player.getLoc().x < 0.5f) {
            
            lvl.removeFromScene(bullet, main.getRootNode());
            main.player.resetPlayer();
            
            if (lvl.levelNum == 1)
                lvl = new Level(2, main);
            else
                    lvl = new Level(3, main);
            
            lvl.addToScene(bullet, main.getRootNode());
        }
        
        
    }
    
    public void initFog() {
        
        //fog effect
        FilterPostProcessor fpp=new FilterPostProcessor(main.getAssetManager());
        FogFilter fog=new FogFilter();
        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
        fog.setFogDistance(155);
        fog.setFogDensity(2f);
        fpp.addFilter(fog);
        main.getViewPort().addProcessor(fpp);
    }
    
        
    public void initAudio() {
        AudioNode alarmAudio = new AudioNode(main.getAssetManager(), "Sounds/alarm.ogg");
        alarmAudio.setPositional(false); 
        alarmAudio.setDirectional(false);
        alarmAudio.setVolume(75f);
        alarmAudio.setLooping(true);
        main.getRootNode().attachChild(alarmAudio);
        alarmAudio.play();
        
        AudioNode musicAudio = new AudioNode(main.getAssetManager(), "Sounds/music.ogg");
        musicAudio.setPositional(false); 
        musicAudio.setDirectional(false);
        musicAudio.setVolume(.6f);
        musicAudio.setLooping(true);
        main.getRootNode().attachChild(musicAudio);
        musicAudio.play();
    }
    
        
}
