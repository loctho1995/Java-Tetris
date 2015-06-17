/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach.Scenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import xepgach.MenuItem;
import static xepgach.Scenes.GamePlayScene.PANEL_WIDTH;
import xepgach.SoundManager;
import xepgach.Vector;
import xepgach.XepGach;
import static xepgach.XepGach.HEIGHT;
import static xepgach.XepGach.WIDTH;

/**
 *
 * @author DELL
 */
public class AboutScene extends Scene {
    private MenuItem m_itemBack;
    
    public AboutScene(){
        
    }

    @Override
    public void doDraw(Graphics g) {
        super.doDraw(g); //To change body of generated methods, choose Tools | Templates.
        
        try{
        BufferedImage background = ImageIO.read(new File("Resources/about.png"));
        g.drawImage(background, XepGach.WIDTH / 2 - background.getWidth() / 2, XepGach.HEIGHT / 2 - background.getHeight() / 2, null);
        m_itemBack.doDraw(g);
        
        }catch (Exception ex){}
    }
    
    @Override
    public void loadContent(){
        super.loadContent();
        try{
        m_itemBack = new MenuItem("Resources/back.png");
        m_itemBack.setPosition(new Vector(WIDTH + PANEL_WIDTH / 2 - m_itemBack.getWidth() / 2, HEIGHT - 200));
        m_itemBack.setVisible(true);
        
        SoundManager.playAboutBackground();
        }catch(Exception ex) {};
    }
    
    @Override
    public void update(float dt){
        super.update(dt);
    }
    
    @Override
    public void mouseMotion(XepGach.MouseStates mouseState, Vector mouseLocation){
        if(mouseState == XepGach.MouseStates.Clicked){
            if(m_itemBack.isTouched(mouseLocation)){
                XepGach.replaceScene(new MenuScene());
            }
        }
    }
}
