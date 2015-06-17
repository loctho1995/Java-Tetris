/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach.Shapes;

import xepgach.GBlock;
import xepgach.Rect;
import xepgach.Vector;
import xepgach.XepGach;

/**
 *
 * @author DELL
 */

//  0
// 00
// 0

public class ZLeftShape extends Shape{
    private GBlock m_block1;
    private GBlock m_block2;
    private GBlock m_block3;
    private GBlock m_block4;
    
    public ZLeftShape(){
        init();
    }
    
    public ZLeftShape(Vector pos){    
        m_position = pos;
        
        init();
    }     
    
    public void init(){
        String path = getRandomBlockPath();
        
        m_block1 = new GBlock(path);
        m_block2 = new GBlock(path);
        m_block3 = new GBlock(path);
        m_block4 = new GBlock(path);
        //  0
        // 00
        // 0
        
        m_block1.setPosition(new Vector(GBlock.getActualWidth(), 0));
        m_block2.setPosition(new Vector(0, GBlock.getActualHeight()));
        m_block3.setPosition(new Vector(GBlock.getActualWidth(), GBlock.getActualHeight()));
        m_block4.setPosition(new Vector(0, GBlock.getActualHeight() * 2));
        
        m_rectBound = new Rect(0, 0, GBlock.getActualWidth() * 2, GBlock.getActualHeight() * 3);
        m_arrayValue = new int[][]
            {
                {0, 1}, {1, 1}, {1, 0}                    
            };
        
        m_listBlocks.add(m_block1);
        m_listBlocks.add(m_block2);
        m_listBlocks.add(m_block3);
        m_listBlocks.add(m_block4);
    }
    
    public void setRotationValue(int val){
        super.setRotationValue(val);
                
        if(!m_allowMove)
            return;
        
        //Rotation: 0 - 1 (nam ngang va nam doc)
        if(val > 1)
            m_rotation = 0 ;
        
        if(val < 0)
            m_rotation = 1;
        
        if(m_rotation == 0){
            // 0
            //00
            //0
        if(m_position.getX() + GBlock.getActualWidth() * 2 > XepGach.WIDTH){
            m_rotation = 1;
            return;
        }    
        
        m_block1.setPosition(new Vector(GBlock.getActualWidth(), 0));
        m_block2.setPosition(new Vector(0, GBlock.getActualHeight()));
        m_block3.setPosition(new Vector(GBlock.getActualWidth(), GBlock.getActualHeight()));
        m_block4.setPosition(new Vector(0, GBlock.getActualHeight() * 2));
        
        m_rectBound = new Rect(0, 0, GBlock.getActualWidth() * 2, GBlock.getActualHeight() * 3);
        m_arrayValue = new int[][]
            {
                {0, 1}, {1, 1}, {1, 0}                    
            };
        }
        else{            
        //00
        // 00
        if(m_position.getX() + GBlock.getActualWidth() * 3 > XepGach.WIDTH){
            m_rotation = 0;
            return;
        }    
            
        m_block1.setPosition(new Vector(0, 0));
        m_block2.setPosition(new Vector(GBlock.getActualWidth(), 0));
        m_block3.setPosition(new Vector(GBlock.getActualWidth(), GBlock.getActualHeight()));
        m_block4.setPosition(new Vector(GBlock.getActualWidth() * 2, GBlock.getActualHeight()));
        
        m_rectBound = new Rect(0, 0, GBlock.getActualWidth() * 3, GBlock.getActualHeight() * 2);  
        m_arrayValue = new int[][]
            {
                {1, 1, 0}, {0, 1, 1}                    
            };
        }
    }      
}
