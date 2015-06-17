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
//Vector
public class Vector {
    int x, y;

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Vector(){
        x = 0;
        y = 0;
    }
    
    public Vector add(Vector vec1){
        return new Vector(vec1.getX() + this.getX(), vec1.getY() + this.getY());
    }   
    
    public Vector minus(Vector vec1){
        return new Vector(this.getX() - vec1.getX(), this.getY() - vec1.getY());
    }
}
