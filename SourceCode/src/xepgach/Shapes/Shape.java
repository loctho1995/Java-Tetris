/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach.Shapes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import xepgach.GBlock;
import xepgach.Rect;
import xepgach.Vector;

/**
 *
 * @author DELL
 */
public abstract class Shape extends Object{
    protected int m_rotation; // 0: default
    protected Vector m_position;      
    protected List<GBlock> m_listBlocks;  
    protected float m_timeMove; //second
    protected float m_saveTimeMove;
    protected Rect m_rectBound;
    protected boolean m_allowMove;
    protected int[][] m_arrayValue;

    public Shape(){
        initialized();
    }
    
    public Shape(Vector pos){
        initialized();
        
        m_position = pos;
    }    
    
    public void initialized(){
        m_listBlocks = new ArrayList<GBlock>();        
        m_rotation = 0;
        m_saveTimeMove = m_timeMove = 1.0f;
        m_rectBound = new Rect();
        m_allowMove = true; 
        m_arrayValue = new int[0][0];
        m_position = new Vector(0, 0);
    }
    
    public Rect getBound(){
    return new Rect(m_position.getX() + m_rectBound.getX(), m_position.getY() + m_rectBound.getY(), 
                            m_rectBound.getWidth(), m_rectBound.getHeight());
    }
    
    public void setAllowMove(boolean flag){
        m_allowMove = flag;
    }
    
    public float getTimeMove(){
        return m_timeMove;
    }
    
    public void setTimeMove(float time){
        m_timeMove = time;
    }
    
    public void refreshTimeMove(){
        if(m_allowMove)
            m_timeMove = m_saveTimeMove;
    }    
    
    public void resetTimeMove(float time){
        m_saveTimeMove = m_timeMove = time;    
    }
    
    public int getRotationValue(){
        return m_rotation;
    }
    
    public void setRotationValue(int value){
        m_rotation = value;
    }
    
    public boolean IsAllowMove(){
        return m_allowMove;
    }
    
    public Vector getPosition(){        
        return m_position;
    }
    
    public void setPosition(Vector pos){
        if(m_allowMove)
            m_position = pos;
    }
    
    public int[][] getArrayValue(){
        return m_arrayValue;
    }
    
    public List<GBlock> getListBlock(){
        return m_listBlocks;
    }
    
    public void doDraw(Graphics g){
        
        for (int i = 0; i < m_listBlocks.size(); i++) {           
            GBlock block = m_listBlocks.get(i);
            
            g.drawImage(block.getImage(), block.getPostion().getX() + m_position.getX(), 
                        block.getPostion().getY() + m_position.getY(), block.getWidth(), 
                        block.getHeight(), null);
        }
    }
    
    protected String getRandomBlockPath(){
        Random rand = new Random();
        int val = rand.nextInt(6) + 1;
        return "Resources/Block" + val + ".png";
    }
}
