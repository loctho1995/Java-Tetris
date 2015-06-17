/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach.Scenes;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import xepgach.Vector;
import xepgach.XepGach;

/**
 *
 * @author DELL
 */
public abstract class Scene {   
    protected String m_sceneName = "";
    
    public Scene(){
        loadContent();
    }    
    
    public String getSceneName(){
        return m_sceneName;
    }
    
    public void doDraw(Graphics g){
        
    }
    
    public void update(float dt){
        
    }
    
    public void loadContent(){        
    }
    
    public void mouseMotion(XepGach.MouseStates mouseState, Vector mouseLocation){
        
    }  
    
    public void keyBoardHandle(KeyEvent e){
        
    }
}
