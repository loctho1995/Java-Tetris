/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author DELL
 */
public class MenuItem {
    Vector m_position;
    Rect m_bound;
    BufferedImage m_image;
    int m_width, m_height;
    boolean m_isVisible;
    
    
    public MenuItem(String imagePath) throws InterruptedException, IOException{
        m_image = ImageIO.read(new File(imagePath));
               
        m_position = new Vector(0, 0);
        m_width = m_image.getWidth(null);
        m_height = m_image.getHeight(null);
        m_isVisible = true;
        
        m_bound = new Rect(m_position.getX(), m_position.getY(), m_width, m_height);
    }
    
    public void setVisible(boolean flag){
        m_isVisible = flag;
    }
    
    public void setWidth(int width){
        m_width = width;
        m_bound = new Rect(m_position.getX(), m_position.getY(), m_width, m_height);
    }
    
    public void setHeight(int height){
        m_height = height;
        m_bound = new Rect(m_position.getX(), m_position.getY(), m_width, m_height);
    }
    
    public int getWidth(){
        return m_width;
    }
    
    public int getHeight(){
        return m_height;
    }
    
    public Rect getBound(){
        return m_bound;
    }
    
    
    public Vector getPosition(){
        return m_position;
    }
    
    public void setPosition(Vector pos){
        m_position = pos;
        m_bound = new Rect(m_position.getX(), m_position.getY(), m_width, m_height);
    }

    public boolean isTouched(Vector touch){    
        if(!m_isVisible)
            return false;
        
        if(m_bound.isTouched(touch))
           return true;
        
        return false;
    }
    
    public void doDraw(Graphics g){
        if(!m_isVisible)
            return;

        g.drawImage(m_image, m_position.getX(), m_position.getY(), m_width, m_height, null);
    }
}
