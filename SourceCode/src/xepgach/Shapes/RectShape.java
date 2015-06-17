/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xepgach.Shapes;

import xepgach.GBlock;
import xepgach.Rect;
import xepgach.Vector;

/**
 *
 * @author DELL
 */

// 00
// 00

public class RectShape extends Shape{
    private GBlock m_block1;
    private GBlock m_block2;
    private GBlock m_block3;
    private GBlock m_block4;
    
    public RectShape(){
        init();
    }   
    
    public RectShape(Vector pos){
        init();
        m_position = pos;
    }
    
    public void init(){
        String path = getRandomBlockPath();
        
        m_block1 = new GBlock(path);
        m_block2 = new GBlock(path);
        m_block3 = new GBlock(path);
        m_block4 = new GBlock(path);
                
        m_block1.setPosition(new Vector(0, 0));
        m_block2.setPosition(new Vector(GBlock.getActualWidth(), 0));
        m_block3.setPosition(new Vector(0, GBlock.getActualHeight()));
        m_block4.setPosition(new Vector(GBlock.getActualWidth(), GBlock.getActualHeight()));
        
        m_rectBound = new Rect(0, 0, GBlock.getActualWidth() * 2, GBlock.getActualHeight() * 2);
        
        m_listBlocks.add(m_block1);
        m_listBlocks.add(m_block2);
        m_listBlocks.add(m_block3);
        m_listBlocks.add(m_block4);
        m_arrayValue = new int[][]
            {
                {1, 1}, {1, 1}
                    
            };
    }
    
    public void setRotationValue(int val){
        super.setRotationValue(val);                
    }
}
