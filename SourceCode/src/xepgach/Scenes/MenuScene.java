/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach.Scenes;

import java.awt.Graphics;
import xepgach.MenuItem;
import xepgach.SoundManager;
import xepgach.Vector;
import xepgach.XepGach;
import static xepgach.XepGach.WIDTH;

/**
 *
 * @author DELL
 */
public class MenuScene extends Scene{
    private MenuItem    m_itemEasy,
                        m_itemNormal,
                        m_itemDifficult,
                        m_itemHighScore,
                        m_itemAbout;
    
    public MenuScene(){
        super();
        m_sceneName = "MenuScene";
    }   
    
    @Override
    public void loadContent(){
        super.loadContent();
        try{
        int distanceItem = 40;
        
        m_itemEasy = new MenuItem("Resources/easy.png");
        m_itemEasy.setPosition(new Vector(WIDTH / 2 - m_itemEasy.getWidth() / 2, 30));
        
        m_itemNormal = new MenuItem("Resources/normal.png");
        m_itemNormal.setPosition(new Vector(WIDTH / 2 - m_itemNormal.getWidth() / 2, 
                                    m_itemEasy.getBound().getMaxY() + distanceItem));
        
        m_itemDifficult = new MenuItem("Resources/difficult.png");
        m_itemDifficult.setPosition(new Vector(WIDTH / 2 - m_itemDifficult.getWidth() / 2,
                                    m_itemNormal.getBound().getMaxY() + distanceItem));
                
        m_itemHighScore = new MenuItem("Resources/highscore.png");
        m_itemHighScore.setPosition(new Vector(WIDTH / 2 - m_itemHighScore.getWidth() / 2,
                                    m_itemDifficult.getBound().getMaxY() + distanceItem));
        
        m_itemAbout = new MenuItem("Resources/aboutitem.png");
        m_itemAbout.setPosition(new Vector(WIDTH / 2 - m_itemAbout.getWidth() / 2, 
                                    m_itemHighScore.getBound().getMaxY() + distanceItem));
        
        SoundManager.playMenuBackground();
        }catch(Exception ex){System.out.println(ex.toString());}
    }
    
    @Override
    public void mouseMotion(XepGach.MouseStates mouseState, Vector mouseLocation){
        
        if (mouseState == XepGach.MouseStates.Clicked) {
            
            if(m_itemEasy.isTouched(mouseLocation)){
                XepGach.replaceScene(new GamePlayScene(GamePlayScene.GameLevel.Easy));
            }
            
            if(m_itemNormal.isTouched(mouseLocation)){
                XepGach.replaceScene(new GamePlayScene(GamePlayScene.GameLevel.Normal));
            }
            
            if(m_itemDifficult.isTouched(mouseLocation)){
                XepGach.replaceScene(new GamePlayScene(GamePlayScene.GameLevel.Difficult));
            }
            
            if(m_itemHighScore.isTouched(mouseLocation)){
                XepGach.replaceScene(new HighscoreScene());
            }
            
            if(m_itemAbout.isTouched(mouseLocation)){
                XepGach.replaceScene(new AboutScene());
            }
        }
    } 
    
    @Override
    public void update(float dt){
        super.update(dt);
    }   
    
    @Override
    public void doDraw(Graphics g) {        
        super.doDraw(g); //To change body of generated methods, choose Tools | Templates.
        
        m_itemEasy.doDraw(g);
        m_itemNormal.doDraw(g);
        m_itemDifficult.doDraw(g);
        m_itemHighScore.doDraw(g);
        m_itemAbout.doDraw(g);
        
        if(XepGach.IsTest){
            //draw bound of shape
            g.drawRect(m_itemEasy.getBound().getX(), m_itemEasy.getBound().getY(),
                    m_itemEasy.getBound().getWidth(),m_itemEasy.getBound().getHeight());
            
            g.drawRect(m_itemDifficult.getBound().getX(), m_itemDifficult.getBound().getY(),
                    m_itemDifficult.getBound().getWidth(),m_itemDifficult.getBound().getHeight());
            
            g.drawRect(m_itemNormal.getBound().getX(), m_itemNormal.getBound().getY(),
                    m_itemNormal.getBound().getWidth(),m_itemNormal.getBound().getHeight());
            
             g.drawRect(m_itemAbout.getBound().getX(), m_itemAbout.getBound().getY(),
                    m_itemAbout.getBound().getWidth(),m_itemAbout.getBound().getHeight());
        }
    }
}
