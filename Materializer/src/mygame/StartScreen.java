/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.ui.Picture;

/**
 *
 * @author Nick
 */
public class StartScreen extends AbstractAppState  {
    
    Main main;
    AppStateManager asm;
    BitmapText text;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
	main = (Main) app;
        asm = stateManager;
	Game game = new Game();
        stateManager.attach(game);
    }
    
    @Override
    public void update(float tpf) {
    
    }
}
