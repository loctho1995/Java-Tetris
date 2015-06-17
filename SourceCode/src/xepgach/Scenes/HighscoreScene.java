/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach.Scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import xepgach.MenuItem;
import xepgach.Player;
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
public class HighscoreScene extends Scene{
    private List<Player> m_players;
    private MenuItem m_itemBack;
            
    public HighscoreScene(){
        m_sceneName = "Highscore Scene";
    }
    
    @Override
    public void loadContent(){
        super.loadContent();
        m_players = new ArrayList<>();
        loadPlayers();
        
        try{
        m_itemBack = new MenuItem("Resources/back.png");
        m_itemBack.setPosition(new Vector(WIDTH + PANEL_WIDTH / 2 - m_itemBack.getWidth() / 2, HEIGHT - 200));
        m_itemBack.setVisible(true);
        }catch(Exception ex) {};
        
        //SoundManager.stop();
        SoundManager.playHighScoreBackground();
    }    
    
    @Override
    public void update(float dt){
        super.update(dt);
    }
    
    
    @Override
    public void doDraw(Graphics g) {
        super.doDraw(g); //To change body of generated methods, choose Tools | Templates.
        drawHighScore(g);
        m_itemBack.doDraw(g);
    }
    
    private void drawHighScore(Graphics g){
        
        for (int i = 0; i < m_players.size(); i++) {
            Player player = m_players.get(i);
            
            g.setColor(Color.white);
            g.setFont(new Font("Agency FB", Font.BOLD , 50));
            FontMetrics fm = g.getFontMetrics(g.getFont());
            
            g.drawString(player.getName() + ":", 10 ,  (i + 1) * 60);
            g.drawString("" + player.getScore(), 30 + fm.stringWidth(player.getName()),(i + 1) * 60);
        }
    }
    
    private void loadPlayers(){            
        try{
        
        FileReader reader = new FileReader("Data/save.sav");
        BufferedReader buffer = new BufferedReader(reader);
        Scanner scan = new Scanner(buffer);
        
        while(scan.hasNext()){
            String name = scan.nextLine();
            String score = scan.nextLine();
            
            Player player = new Player(name, Integer.parseInt(score));
            m_players.add(player);
        }
        
        }catch(Exception ex){}
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
