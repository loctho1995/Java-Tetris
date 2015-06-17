/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author DELL
 */
public class SoundManager {
    static AudioInputStream m_highScore,
                            m_gamePlay,
                            m_menu,
                            m_about;

    static Clip m_audio, m_effect;
    static boolean m_isLoaded;
    
    public static boolean loadData(){
        try{
        m_audio = AudioSystem.getClip();
        m_effect = AudioSystem.getClip();
        
        m_highScore = AudioSystem.getAudioInputStream(new File("Resources/Sounds/highscore.wav"));
        m_gamePlay = AudioSystem.getAudioInputStream(new File("Resources/Sounds/gameplay.wav"));        
        m_about = AudioSystem.getAudioInputStream(new File("Resources/Sounds/about.wav"));
        m_menu = AudioSystem.getAudioInputStream(new File("Resources/Sounds/menu.wav"));        
                
        }catch(Exception ex){}
        
        m_isLoaded = true;
        return true;
    }           
    public static void stop(){
        if(!m_isLoaded)
            return;
        
        
    } 
    
    public static void playHighScoreBackground(){
        if(!m_isLoaded)
            return;
                
        try{         
            m_audio.close();
            m_highScore = AudioSystem.getAudioInputStream(new File("Resources/Sounds/highscore.wav"));
            m_audio.open(m_highScore);
            m_audio.start();             
            
        }catch(Exception ex){System.out.println(ex.toString());}
    }                  
    
    public static void playGameBackground(){
        if(!m_isLoaded)
            return;
        
        try{            
            m_audio.close();
            m_gamePlay = AudioSystem.getAudioInputStream(new File("Resources/Sounds/gameplay.wav"));
            m_audio.open(m_gamePlay);
            m_audio.loop(Clip.LOOP_CONTINUOUSLY);
            m_audio.start();
        }catch (Exception ex){}
    } 
    
    public static void playAboutBackground(){
        if(!m_isLoaded)
            return;
        
        try{            
            m_audio.close();
            m_about = AudioSystem.getAudioInputStream(new File("Resources/Sounds/about.wav"));
            m_audio.open(m_about);
            m_audio.loop(Clip.LOOP_CONTINUOUSLY);
            m_audio.start();
        }catch (Exception ex){}
    }
    
    public static void playMenuBackground(){
        if(!m_isLoaded)
            return;
        
        try{      
            m_audio.close();
            m_menu = AudioSystem.getAudioInputStream(new File("Resources/Sounds/menu.wav"));
            m_audio.open(m_menu);
            m_audio.loop(Clip.LOOP_CONTINUOUSLY);
            m_audio.start();
        }catch (Exception ex){}
    }  
}
