/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Matt Arbiz, Nick
 */
public class App implements MouseListener {
    
    
    JFrame frame;
    JPanel panel, panel2, panel3;
    JLabel label1;
    JButton send, backspace, delete;
    Main main;
    CustomMesh mesh;
    boolean meshOn = false;
    int radius = 3;
    ArrayList verts = new ArrayList();
    
    public App(Main main) {
	this.main = main;
	initApp();
    }
    
    public void initApp() {
	
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(320, 520));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //background panel
        panel = new JPanel();
        panel.setBackground(Color.gray);
        //panel that collects points
        panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(285, 400));
        panel2.setBackground(Color.black);
        panel2.addMouseListener(this);
        //cracked screen panel
        panel3 = new JPanel();
        panel3.setPreferredSize(new Dimension(285, 400));
        BufferedImage myPicture;
        try {
            myPicture = ImageIO.read(new File("assets\\Images\\crack.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            panel3.add(picLabel);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        panel3.setOpaque(false);
        panel2.add(panel3);
        
        verts = new ArrayList();
        
        JButton send = new JButton();
        send.setIcon(new ImageIcon("assets\\Images\\send.gif"));
        send.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //initMesh();
                frame.dispose();
            }
        });
        JButton delete = new JButton();
        delete.setIcon(new ImageIcon("assets\\Images\\delete.gif"));
        delete.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                 verts.clear();
                 panel2.repaint();
                 
            }
        });
        JButton backspace = new JButton();
        backspace.setIcon(new ImageIcon("assets\\Images\\backspace.gif"));
        backspace.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                 verts.remove(verts.size()-1);
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 4;
        panel.add(panel2,c);
        c.gridx = 1;
        c.gridy = 1;
        panel.add(backspace, c);
        c.gridx = 1;
        c.gridy = 2;
        panel.add(send,c);
        c.gridx = 1;
        c.gridy = 3;
        panel.add(delete,c);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        
        
        main.setDisplayFps(true);
        main.setDisplayStatView(false);
        frame.setLocationRelativeTo(null);
	
	//temp fix for issue in which first point clicked does not display; first call to drawCircle draws nothing
	drawCircle(panel2.getX(), panel2.getY(), panel2, false);
	
    }
    
     private void initMesh() {
	
        Vector3f[] vertices = new Vector3f[verts.size()];
        
        for(int i = 0; i < verts.size(); i++)
            vertices[i] = (Vector3f)verts.get(i);
            
        
        mesh = new CustomMesh(vertices, .5f, true);
        
        Geometry geo = new Geometry("mesh", mesh); 
        //geo.scale(.5f);
        geo.setLocalTranslation(7, 3, 3);
	Material mat = new Material(main.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
	mat.setBoolean("VertexColor", true);
//        mat.getAdditionalRenderState().setWireframe(true); 

        geo.setMaterial(mat);
	geo.setShadowMode(RenderQueue.ShadowMode.Cast);
                
        geo.addControl(new RigidBodyControl(2f));
        geo.getControl(RigidBodyControl.class).setRestitution(1);
        main.getRootNode().attachChild(geo);
        main.bullet.getPhysicsSpace().add(geo);
        meshOn = true;

    }
      public void drawCircle(int x, int y, Object source, boolean fill) {
        if(source instanceof JPanel) {
            Graphics g = ((JComponent) source).getGraphics();
            g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.setColor(Color.red);
            if (fill) {
                g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            }
        } // else ignore
    }
    
    
    public void mouseExited(MouseEvent m) {
        
    }
    public void mouseEntered(MouseEvent m) {
        
    }
    public void mouseReleased(MouseEvent m) {
        verts.add(new Vector3f(m.getX() / 100f, (frame.getHeight()- m.getY() - 40) / 100f, 0));
        drawCircle(m.getX(), m.getY(), panel2, true);
 
        }
    
    
    public void mousePressed(MouseEvent m) {
      
    }
    public void mouseClicked(MouseEvent m) {
        
        
    }
}
