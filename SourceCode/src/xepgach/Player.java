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
public class Player {
    private String m_name;
    private int m_score;
    
    public Player(String name, int score){
        m_name = name;
        m_score = score;
    }
    
    public String getName(){
        return m_name;
    }
    
    public void setName(String s){
        m_name = s;
    }
    
    public int getScore(){
        return m_score;
    }
    
    public void setScore(int score){
        m_score = score;
    }
}
