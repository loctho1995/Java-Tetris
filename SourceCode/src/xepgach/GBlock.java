/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author DELL
 */
//Block for Shapes

public class GBlock {
    // rectangle 30 x 30
    private static final int BLOCK_WIDTH = 30;
    private static final int BLOCK_HEIGHT = 30;
    
    Vector m_position;
    int m_width, m_height;
    BufferedImage m_image;
    boolean m_isVisible;
    String m_imagePath;

    public Vector getPostion(){
        return m_position;
    }
    
    public static int getActualWidth(){
        return BLOCK_WIDTH;
    }
    
    public static int getActualHeight(){
        return BLOCK_HEIGHT;
    }  
    
    public void setPosition(Vector pos){
        m_position = pos;
    }
    
    public int getWidth(){
        return m_width;
    }
    
    public int getHeight(){
        return m_height;
    }
    
    public void setWidth(int width){
        this.m_width = width;
    }
    
    public void setHeight(int height){
        this.m_height = height;
    }    
    
    public Image getImage(){
        return m_image;
    }
    
    public String getImagePath(){
        return m_imagePath;
    }
    
    public void setVisible(boolean flag){
        m_isVisible = flag;
    }
    
    public boolean getVisible(){
        return m_isVisible;
    }
    
    public GBlock clone(){
        GBlock block = new GBlock(m_imagePath, m_position);
        
        return block;
    }
    
    public GBlock(String imagePath){
        try{
            m_image = ImageIO.read(new File(imagePath));
            m_position = new Vector();
            m_isVisible = true;
            m_imagePath = imagePath;

            if(BLOCK_WIDTH == 0)
                m_width = m_image.getWidth(null);
            else
                m_width = BLOCK_WIDTH;

            if(BLOCK_HEIGHT == 0)
                m_height = m_image.getHeight(null);
            else
                m_height = BLOCK_HEIGHT;
        }catch(Exception ex){}
    }
    
    public GBlock(String imagePath, Vector pos){
        try{
            m_image = ImageIO.read(new File(imagePath));
            m_position = pos;
            m_imagePath = imagePath;
             m_isVisible = true;
            
            if(BLOCK_WIDTH == 0)
                m_width = m_image.getWidth(null);
            else
                m_width = BLOCK_WIDTH;

            if(BLOCK_HEIGHT == 0)
                m_height = m_image.getHeight(null);
            else
                m_height = BLOCK_HEIGHT;
        }catch(Exception ex){}
    }
}
