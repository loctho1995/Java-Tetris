/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach;

/**
 *
 * @author DELL
 */
public class Rect {
    Vector m_position;
    int m_width, m_height;
    
    public Rect(){
        m_position = new Vector();
        m_width = m_height = 0;
    }
    
    public Rect(int x, int y, int width, int height){
        m_position = new Vector(x , y);
        m_width = width;
        m_height = height;
    }
    
    public Rect(Vector pos, int width, int height){
        m_position = pos;
        m_width = width;
        m_height = height;
    }
    
    public int getX(){
        return m_position.getX();
    }
    
    public int getY(){
        return m_position.getY();
    }
    
    public int getWidth(){
        return m_width;
    }
    
    public int getHeight(){
        return m_height;
    }
    
    public int getMaxX(){
        return m_position.getX() + m_width;
    }
    
    public int getMaxY(){
        return m_position.getY() + m_height;
    }
    
    public int getMinX(){
        return m_position.getX();
    }
    
    public int getMinY(){
        return m_position.getY();
    }
    
    public boolean isTouched(Vector touch){
        int x = touch.getX() - m_position.getX();
        int y = touch.getY() - m_position.getY();
        
        if(x > 0 && x < m_width && y > 0 && y < m_height)
            return true;
        
        return false;
    }
}
