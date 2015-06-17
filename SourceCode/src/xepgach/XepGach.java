/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach;

import java.awt.AWTEventMulticaster;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import xepgach.Scenes.*;
import xepgach.Shapes.*;

/**
 *
 * @author DELL
 */
public class XepGach extends JPanel implements ActionListener, Runnable {
    
    //<editor-fold defaultstate="colapsed" desc="Variables">       
    public enum MouseStates{
        Pressed, Clicked, Released
    }
    
    public static final int WIDTH = 480;
    public static final int HEIGHT = 690;
    public static final int PANEL_WIDTH = 150;    
    public static boolean IsTest;    
    
    private static final float SECOND_PER_FRAME = 1.0f / 60;   
    private static Scene m_currentScene;       
    
    private long m_lastTime;
    private float m_currentFPS = 60;    
    private long m_deltaTime;    
    private BufferedImage m_backGround, m_panel;
    
    
    public boolean IsClosed;
    public Thread m_thread;    
    
    public static void replaceScene(Scene scene){
        m_currentScene = scene;    
    }
    
    public static Scene getCurrentScene(){
        return m_currentScene;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="colapsed" desc="main/Constructor/init">
    public static void main(String[] args){
        //MainGame mainGame;
        
       MainGame mainGame = new MainGame();
                mainGame.setVisible(true);
                mainGame.runGame(); 
    }        
    
    public XepGach() {    
        SoundManager.loadData();
        m_currentScene = new MenuScene();
        init();
    }
    
    //</editor-fold> 
    
    //<editor-fold defaultstate="colapsed" desc="Run Thread/Game">
    public void runGame(){                   
        m_thread = new Thread(this);
        m_thread.start();
    }
    
    public void init(){
        try{
        m_backGround = ImageIO.read(new File("Resources/BackGround.png"));
        m_panel = ImageIO.read(new File("Resources/panel.png"));
        
        }catch (Exception ex){}
    }
    
    @Override
    public void run() {   
        m_lastTime = 0;            
                
        while(true){
            if(IsClosed)
                break;
            
            long m_currentTime =(Calendar.getInstance()).getTimeInMillis();            
            m_deltaTime = m_currentTime - m_lastTime;                       
            
            update((float)m_deltaTime / 1000);         
                                 
            m_lastTime = m_currentTime;
            
            try {
                Thread.sleep((long)(SECOND_PER_FRAME * 1000)); //sleep
            } catch (InterruptedException ex) {
                Logger.getLogger(XepGach.class.getName()).log(Level.SEVERE, null, ex);
            }
        }              
    }
    //</editor-fold>
    
    public void onKeyPressed(KeyEvent e){
        m_currentScene.keyBoardHandle(e);
    }
    
    public void mouseMotion(MouseStates mouseState, Vector mouseLocation){
        m_currentScene.mouseMotion(mouseState, mouseLocation);
    }
    
    //don vi milisecond
    private void update(float dt){  
                
        m_currentFPS = 1000 / m_deltaTime;  //get current FPS
        m_currentScene.update(dt); 
        repaint();                
    }
        
    private void doDrawing(Graphics g) {               
        //<editor-fold defaultstate="collapsed" desc="background">
        if(!XepGach.IsTest){
            g.drawImage(m_backGround,0,0, WIDTH, HEIGHT, null);
            g.drawImage(m_panel, WIDTH, -5, null);
        }else{
            //fill background
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.black);
        }
        
        m_currentScene.doDraw(g);
        
        if(IsTest){
            g.drawString("delta: " + (float)m_deltaTime / 1000 + " - Last: " + m_lastTime + "- FPS:" + m_currentFPS + " - SPF: " + SECOND_PER_FRAME, 0, 20);
        }
        
        System.gc();
        //</editor-fold>                        
    }            
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="colapsed" desc="JFrame/GUI">
    public static class MainGame extends JFrame{
        XepGach xepGach;
        
        public MainGame(){
            initGUI();
        }
        
        void initGUI(){
            xepGach = new XepGach();
            this.add(xepGach);
            
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e){
                }
            });        
            
            this.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    xepGach.mouseMotion(MouseStates.Clicked, 
                            new Vector(e.getPoint().x, e.getPoint().y - 25));
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    xepGach.mouseMotion(MouseStates.Pressed, 
                            new Vector(e.getPoint().x, e.getPoint().y - 25));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    xepGach.mouseMotion(MouseStates.Released, 
                            new Vector(e.getPoint().x, e.getPoint().y - 25));
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
            });
            
            this.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                    
                }
                               
                @Override
                public void keyPressed(KeyEvent e) {                                        
                    xepGach.onKeyPressed(e);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    
                }
            });
            
            this.addWindowListener(new WindowListener() {
                @Override
                public void windowClosing(WindowEvent e) {
                    xepGach.IsClosed = true;
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    
                }

                @Override
                public void windowOpened(WindowEvent e) {
                    
                }

                @Override
                public void windowIconified(WindowEvent e) {
                    
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                    
                }

                @Override
                public void windowActivated(WindowEvent e) {
                    
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    
                }
            });
                                   
            setTitle("Tetris - XẾP GẠCH - ");
            setSize(XepGach.WIDTH + XepGach.PANEL_WIDTH, XepGach.HEIGHT);
            setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        public void runGame(){
            xepGach.runGame();
        }
    }
    //</editor-fold>
}