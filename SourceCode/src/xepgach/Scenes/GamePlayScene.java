/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package xepgach.Scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import xepgach.GBlock;
import xepgach.MenuItem;
import xepgach.Player;
import xepgach.Shapes.IShape;
import xepgach.Shapes.LLeftShape;
import xepgach.Shapes.LRightShape;
import xepgach.Shapes.RectShape;
import xepgach.Shapes.WShape;
import xepgach.Shapes.ZLeftShape;
import xepgach.Shapes.ZRightShape;
import xepgach.SoundManager;
import xepgach.Vector;
import xepgach.XepGach;
import static xepgach.XepGach.HEIGHT;
import static xepgach.XepGach.IsTest;
import static xepgach.XepGach.PANEL_WIDTH;
import static xepgach.XepGach.WIDTH;

/**
 *
 * @author DELL
 */
public class GamePlayScene extends Scene{
    //<editor-fold defaultstate="colapsed" desc="Variables">    
    private enum CollitionState{
        Left, Top, Right, Bottom, None, All
    }
    
    public enum GameLevel{
        Easy, Normal, Difficult
    }
    
    public static final int PANEL_WIDTH = 150;
    private final int WIDTH_COUNT = XepGach.WIDTH / GBlock.getActualWidth(); //maximum of Block in Width
    private final int HEIGHT_COUNT = XepGach.HEIGHT / GBlock.getActualHeight() - 1; //maximum of Block in Height
    private final int MAX_NAME_LENGTH = 30;
    
    
    
    private int m_nextShapeID;
    
    private boolean m_isLoaded = false,
                    m_isEndGame,
                    m_isChecking,
                    m_isSaved,
                    m_isPause;
    
    private List<GBlock> m_blocks; //use for draw GBlocks
    private int[][] m_arrayValue = new int[HEIGHT_COUNT][WIDTH_COUNT];
    private xepgach.Shapes.Shape m_shape, m_shapeTemp, m_nextShape; //m_shapeTemp use for easy
    private int m_shapeXPos = 0, m_shapeYPos = 0; //in array
    private int m_score = 0;
    private boolean m_isGameStart;
    
    private MenuItem    m_itemBack,
                        m_itemReplay,
                        m_itemOK,
                        m_itemPause,
                        m_itemResume;
    
    private float m_timeShapeMove = 1.0f;
    private GameLevel m_gameLevel;
    private List<Player> m_players;
    private Player m_currentPlayer;
    private String m_playerName;
    private BufferedImage m_imgName;
    
    public static boolean IsTest;
    //</editor-fold>
    
    public GamePlayScene(GameLevel gameLevel){
        super();
        
        m_gameLevel = gameLevel;
        m_sceneName = "GamePlayScene";
        
        init();
        
        switch(m_gameLevel){
            case Easy:
                m_timeShapeMove = 0.55f;                
                break;
                
            case Normal:
                m_timeShapeMove = 0.4f;
                break;
                
            case Difficult:
                m_timeShapeMove = 0.2f;
                break;
        }
    }    
    
    public void init(){        
        m_score = 0;
        m_shapeXPos = 0;
        m_shapeYPos = 0;
        m_isGameStart = true;
        m_isEndGame = false;
        IsTest = false;
        m_isSaved = false;
        m_blocks = new ArrayList<GBlock>();
        m_playerName = "Noname";
        
        for (int i = 0; i < HEIGHT_COUNT; i++) {
            for (int j = 0; j < WIDTH_COUNT; j++) {
                m_arrayValue[i][j] = 0;
            }
        }
        
        m_nextShapeID = (new Random()).nextInt(7);
        
        if(m_isGameStart)
            createNewShape();    
        
        SoundManager.playGameBackground();
    }
    
    @Override
    public void loadContent(){        
        super.loadContent();
        try{
            m_itemBack = new MenuItem("Resources/back.png");
            m_itemBack.setPosition(new Vector(WIDTH + PANEL_WIDTH / 2 - m_itemBack.getWidth() / 2, HEIGHT - 200));
            m_itemBack.setVisible(true);
            
            m_itemReplay = new MenuItem("Resources/replay.png");
            m_itemReplay.setPosition(new Vector(m_itemBack.getPosition().getX(), m_itemBack.getPosition().getY() - m_itemReplay.getHeight() - 20));
            m_itemReplay.setVisible(true);
            
            m_itemPause = new MenuItem("Resources/pause.png");
            m_itemPause.setPosition(new Vector(m_itemReplay.getPosition().getX(), m_itemReplay.getPosition().getY() - m_itemPause.getHeight() - 20));
            m_itemPause.setVisible(true);
            
            m_itemResume = new MenuItem("Resources/resume.png");
            m_itemResume.setPosition(new Vector(WIDTH / 2 - m_itemResume.getWidth() / 2, HEIGHT / 2 - m_itemResume.getHeight() / 2));
            m_itemResume.setVisible(false);
            
            m_itemOK = new MenuItem("Resources/ok.png");
            m_itemOK.setVisible(false);
            
            m_imgName = ImageIO.read(new File("Resources/name.png"));
            
            m_isLoaded = true;
            m_players = new ArrayList<>();
            
            loadPlayers();
            sortScore();
            
            createNewShape();
        }catch (Exception ex){}
    }
    
    @Override
    public void update(float dt){
        super.update(dt);

        //System.out.println("update");
        
        if(m_isGameStart && !m_isEndGame){
            
            for (int i = 0; i < m_arrayValue[0].length; i++) {
                if(m_arrayValue[0][i] != 0){
                    m_isEndGame = true;
                }
            }
            
            if(m_isPause){
                return;
            }
            
            
            if(m_shape.getTimeMove() >= dt){
                m_shape.setTimeMove(m_shape.getTimeMove() - dt);
            }
            else{
                if(m_shape.getBound().getMaxY() < HEIGHT - GBlock.getActualHeight()
                        && m_shape.IsAllowMove()){
                    //cho them 1 nuoc nua roi moi xet collide
                    refreshShapeXYPos();
                    if(m_shapeXPos >= 0 && m_shapeYPos >= 0)
                        checkCollidedUnder();
                    
                    m_shape.setPosition(new Vector(m_shape.getPosition().getX(),
                            m_shape.getPosition().getY() + GBlock.getActualHeight()));
                    
                    m_shape.refreshTimeMove();
                    refreshShapeXYPos();
                }
                else{
                    refreshShapeXYPos();
                    m_isChecking = true;
                    
                    if(copyBlocks() && addArrayValue()){
                        checkEat();
                        createNewShape();
                    }
                }
            }
        }
    }
    
    @Override
    public void doDraw(Graphics g) {
        super.doDraw(g); //To change body of generated methods, choose Tools | Templates.
        
        //<editor-fold defaultstate="collapsed" desc="Game Start">
        if(m_isGameStart){            
            m_nextShape.setPosition(new Vector(WIDTH + PANEL_WIDTH / 2 - m_nextShape.getBound().getWidth() / 2, m_nextShape.getPosition().getY()));
            
            drawBlocks(g);
            drawScore(g);
            
            m_nextShape.doDraw(g);            
            m_shape.doDraw(g);
            m_itemReplay.doDraw(g);
            m_itemBack.doDraw(g);
            m_itemPause.doDraw(g);
            m_itemResume.doDraw(g);
            
            if(m_gameLevel == GameLevel.Easy){
                m_shapeTemp.setRotationValue(m_shape.getRotationValue());
                resetPositionShapeTemp();
                g.setColor(Color.white);
                for (int i = 0; i < m_shapeTemp.getListBlock().size(); i++) {
                    GBlock block = m_shapeTemp.getListBlock().get(i);
                    g.drawRect(m_shapeTemp.getPosition().getX() + block.getPostion().getX(), 
                            m_shapeTemp.getPosition().getY() + block.getPostion().getY(), 
                                block.getWidth(), block.getHeight());
                }
                //m_shapeTemp.doDraw(g);
            }
            
            try{
            BufferedImage score = ImageIO.read(new File("Resources/score.png"));
            g.drawImage(score, WIDTH, 50, null);
            }catch (Exception ex){}
        }
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Game Over">
        if(m_isEndGame){
            try{
            BufferedImage endGame = ImageIO.read(new File("Resources/gameover.png"));
            g.drawImage(endGame, WIDTH / 2 - endGame.getWidth() / 2, HEIGHT / 2 - endGame.getHeight() / 2, null);
            
            if(!m_isSaved){
                Vector namePos = new Vector(WIDTH / 2 - m_imgName.getWidth() / 2, HEIGHT / 2  - endGame.getHeight());
                m_itemOK.setPosition(new Vector(namePos.getX() + m_imgName.getWidth() + 5, namePos.getY()));
                m_itemOK.setVisible(true);                
                g.drawImage(m_imgName, namePos.getX(), namePos.getY() , null);
                
                g.setColor(Color.white);
                g.setFont(new Font("Agency FB", Font.BOLD, 25));
                g.drawString(m_playerName, namePos.getX() + 70, namePos.getY() + 30);
                
                m_itemOK.doDraw(g);
            }
            }catch(Exception ex){}            
        }
        //</editor-fold>        
        
        //<editor-fold defaultstate="collapsed" desc="Test Mode">
        if(XepGach.IsTest){            
            g.drawString("timeMove: " + m_shape.getTimeMove(), 0, 10);            
            
            g.drawRect(m_shape.getBound().getX(), m_shape.getBound().getY(),
                    m_shape.getBound().getWidth(), m_shape.getBound().getHeight());
            
            g.drawRect(m_itemReplay.getBound().getX(), m_itemReplay.getBound().getY(), 
                        m_itemReplay.getBound().getWidth(), m_itemReplay.getBound().getHeight());
            
            g.drawRect(m_itemBack.getBound().getX(), m_itemBack.getBound().getY(), 
                        m_itemBack.getBound().getWidth(), m_itemBack.getBound().getHeight());
            //draw Screen matrix
            for (int i = 0; i < m_arrayValue.length; i++) {
                g.drawString("" + Arrays.toString(m_arrayValue[i]), 0, 150 + i * 20);
            }
            
            //draw shape matrix
            for (int i = 0; i < m_shape.getArrayValue().length; i++) {
                for (int j = 0; j < m_shape.getArrayValue()[i].length; j++) {
                    g.drawString("" + m_shape.getArrayValue()[i][j],WIDTH + j * 10, 250 + i * 20);
                }
            }
            
            //shape info
            g.drawString("ShapeX: " + m_shapeXPos, WIDTH + 20, 350);
            g.drawString("ShapeY: " + m_shapeYPos, WIDTH + 20, 370);
            g.drawString("Length: " + m_arrayValue[0].length, WIDTH + 20, 390);
        }
        //</editor-fold>        
    }
    
    //<editor-fold defaultstate="colapsed" desc="draw components">
    private void drawBlocks(Graphics g){
        for (int i = 0; i < m_blocks.size(); i++) {
            GBlock block = m_blocks.get(i);
            if(!block.getVisible())
                continue;
            
            g.drawImage(block.getImage(), block.getPostion().getX(), block.getPostion().getY(),
                    block.getWidth(), block.getHeight(), null);
        }
    }
    
    private void drawScore(Graphics g){
        
        String strScore = Integer.toString(m_score);
        int i = 0;
        
        do{
            try{                
                int temp = Integer.parseInt(Character.toString(strScore.charAt(i)));
                
                BufferedImage image = ImageIO.read(new File("Resources/numbers.png"));
                int width = image.getWidth() / 10;
                BufferedImage imgScore = image.getSubimage(temp * width, 0, width, image.getHeight());
                
                g.drawImage(imgScore, WIDTH + 70 + i * (width + 3), 50, null);
                i++;
                
            }catch(Exception ex){}
        }while (i < strScore.length());
    }        
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Keyboard/Mouse Handle">
    @Override
    public void mouseMotion(XepGach.MouseStates mouseState, Vector mouseLocation){
        if(mouseState == XepGach.MouseStates.Clicked){
            if(m_itemBack.isTouched(mouseLocation)){
                    XepGach.replaceScene(new MenuScene());
                }
            
            if(m_itemOK.isTouched(mouseLocation)){
                if(!m_isSaved){
                    saveScore();
                    m_itemOK.setVisible(false);
                }
            }
            
            if(m_itemPause.isTouched(mouseLocation)){
                if(!m_isEndGame){
                    m_isPause = true;
                    m_itemResume.setVisible(true);
                }
            }
            
            if(m_itemReplay.isTouched(mouseLocation)){
                XepGach.replaceScene(new GamePlayScene(m_gameLevel));
            }
            
            if(m_itemResume.isTouched(mouseLocation)){
                m_itemResume.setVisible(false);
                m_isPause = false;
            }
        }
    }
    
    @Override
    public void keyBoardHandle(KeyEvent e){
        if(m_isPause)
            return;
        
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                onKeyUpPressed();
                break;
                
            case KeyEvent.VK_DOWN:
                onKeyDownPressed();
                break;
                
            case KeyEvent.VK_LEFT:
                onKeyLeftPressed();
                break;
                
            case KeyEvent.VK_RIGHT:
                onKeyRightPressed();
                break;
        }
        
        onKeyPressed(e);
    }
    
    public void onKeyLeftPressed(){
        if(m_isEndGame || !m_isGameStart)
            return;
        
        if(checkCollideLeftRightTop(CollitionState.Left))
            return;
        
        if(m_shape.getBound().getMinX() > 0){
            m_shape.setPosition(new Vector(m_shape.getPosition().getX() - GBlock.getActualWidth(),
                    m_shape.getPosition().getY()));
            
            if(m_gameLevel == GameLevel.Easy)
                resetPositionShapeTemp();
            
            refreshShapeXYPos();
        }
    }
    
    public void onKeyRightPressed(){
        if(m_isEndGame || !m_isGameStart)
            return;
        
        if(checkCollideLeftRightTop(CollitionState.Right))
            return;
        
        if(m_shape.getBound().getMaxX() < WIDTH){
            m_shape.setPosition(new Vector(m_shape.getPosition().getX() + GBlock.getActualWidth(), m_shape.getPosition().getY()));
            
            if(m_gameLevel == GameLevel.Easy)
                resetPositionShapeTemp();
            
            refreshShapeXYPos();
        }
    }
    
    public void onKeyUpPressed(){
        if(m_isEndGame || !m_isGameStart)
            return;
        m_shape.setRotationValue(m_shape.getRotationValue() + 1);
        refreshShapeXYPos();
        
        checkCollideLeftRightTop(CollitionState.Top);
    }
    
    public void onKeyDownPressed(){
        if(m_isEndGame || !m_isGameStart || m_isChecking || !m_shape.IsAllowMove())
            return;
        
        if(m_shape.getBound().getMaxY() + GBlock.getActualHeight() < HEIGHT){
            //kiem tra vi position cung move theo update
            refreshShapeXYPos();
            if(!checkCollidedUnder()){
                m_shape.setPosition(new Vector(m_shape.getPosition().getX(),
                        m_shape.getPosition().getY() + GBlock.getActualHeight()));
            }
        }
    }
    
    public void onKeyPressed(KeyEvent e){
        if(m_isEndGame){
            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                if(m_playerName.length() > 0)
                    m_playerName =  m_playerName.substring(0, m_playerName.length() - 1);
            }
            
            if(m_playerName.length() < MAX_NAME_LENGTH){
                if(e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z' || e.getKeyChar() >= 'A'
                        && e.getKeyChar() <= 'Z' || e.getKeyChar() == ' ')
                    m_playerName += e.getKeyChar();
            }
        }
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="colapsed" desc="components/function">
    private void loadPlayers(){            
        try{
        
        FileReader reader = new FileReader("Data/save.sav");
        BufferedReader buffer = new BufferedReader(reader);
        Scanner scan = new Scanner(buffer);
        
        while(scan.hasNext()){
            String name = scan.nextLine();
            String score = scan.nextLine();
            
            Player player = new Player(name, Integer.parseInt(score));
            m_players.add(player);
        }
        
        }catch(Exception ex){}
    }
    
    private void saveScore(){
        if(m_score <= 0){
             m_isSaved = true;
             return;
        }
        
        if(m_players.size() >= 10){
            //cho phep luu 10 player co diem cao nhat
            if(m_players.get(m_players.size() - 1).getScore() < m_score){
                m_players.get(m_players.size() - 1).setScore(m_score);
                m_players.get(m_players.size() - 1).setName(m_playerName);
                sortScore();
            }
        }
        else{
            m_currentPlayer = new Player(m_playerName, m_score);
            m_players.add(m_currentPlayer);   
        } 
        
        try{
        FileOutputStream output = new FileOutputStream("Data/save.sav");        
        PrintWriter writter = new PrintWriter(output);
        
        for (int i = 0; i < m_players.size(); i++) {
            writter.println(m_players.get(i).getName());
            writter.println(m_players.get(i).getScore());
        } 
        
        writter.close();
        m_isSaved = true;
        
        }catch (Exception ex){}
    }
    
    private void sortScore(){
        for (int i = 0; i < m_players.size() - 1; i++) {
            for (int j = i + 1; j < m_players.size(); j++) {
                if(m_players.get(i).getScore() < m_players.get(j).getScore()){
                    
                    String name = m_players.get(i).getName();
                    int score = m_players.get(i).getScore();
                    
                    m_players.get(i).setName(m_players.get(j).getName());
                    m_players.get(i).setScore(m_players.get(j).getScore());
                    
                    m_players.get(j).setName(name);
                    m_players.get(j).setScore(score);
                }
            }
        }
    }
    
    private void createNewShape(){     
        m_shape = getShape(m_nextShapeID);
        m_shapeTemp = getShape(m_nextShapeID);
        
        if(m_gameLevel == GameLevel.Easy)
            resetPositionShapeTemp();
        
        m_nextShapeID = (new Random()).nextInt(7);
        
        m_nextShape = getShape(m_nextShapeID);
        m_nextShape.setPosition(new Vector(WIDTH + 50, 100));
        refreshShapeXYPos();
        m_isChecking = false;
    }
    
    private xepgach.Shapes.Shape getShape(int value){
        xepgach.Shapes.Shape shape = new IShape();
        
        switch(value){
            case 0:
                shape = new IShape(new Vector(WIDTH / 2, 0));
                break;
                
            case 1:
                shape = new LLeftShape(new Vector(WIDTH / 2, 0));
                break;
                
            case 2:
                shape = new LRightShape(new Vector(WIDTH / 2, 0));
                break;
                
            case 3:
                shape = new RectShape(new Vector(WIDTH / 2, 0));
                break;
                
            case 4:
                shape = new WShape(new Vector(WIDTH / 2, 0));
                break;
                
            case 5:
                shape = new ZLeftShape(new Vector(WIDTH / 2, 0));
                break;
                
            case 6:
                shape = new ZRightShape(new Vector(WIDTH / 2, 0));
                break;       
        }
        
        if(m_gameLevel == GameLevel.Difficult){
            if(m_timeShapeMove >= 0.1f)
                m_timeShapeMove-=0.001f;
            
            shape.resetTimeMove(m_timeShapeMove);
            shape.setTimeMove(m_timeShapeMove);
        }
        else{
            shape.resetTimeMove(m_timeShapeMove);
            shape.setTimeMove(m_timeShapeMove);
        }
        
        shape.setRotationValue(0);
        return shape;
    }
    
    private void refreshShapeXYPos(){
        m_shapeXPos = m_shape.getPosition().getX() / GBlock.getActualWidth();
        m_shapeYPos = m_shape.getPosition().getY() / GBlock.getActualHeight();                
    }
    
    private void resetPositionShapeTemp(){
        //dung cho man easy cho phep nguoi choi thay hinh sap roi xuong
        int rowCollide = -1, lsave = 0;
        boolean isHaveCollided = false;
        
        refreshShapeXYPos();
         //quet tung cot nam trong khoang array shape
        for (int k = m_shapeXPos; k < m_shapeXPos + m_shape.getArrayValue()[0].length; k++) {
            //quet tung hang
            for (int l = m_shapeYPos + m_shape.getArrayValue().length; l < m_arrayValue.length; l++) {
                if(k < WIDTH_COUNT){ 
                    if(m_arrayValue[l][k] == 1){
                        //quet tung hang nam trong mang shape
                        for (int i = m_shape.getArrayValue().length - 1; i >= 0 ; i--) {
                            //kiem tra o hang i cot k - mshapeXPos
                            if(m_shape.getArrayValue()[i][k - m_shapeXPos] == 1){
                                isHaveCollided = true;

                                int val = l - i - 1;
                                
                                if(rowCollide == -1)
                                    rowCollide = val;
                                else if(rowCollide > val){
                                    rowCollide = val;
                                }
                            }
                        }
                        
                        //break;
                    }
                }                
            }
        }
        
        if(isHaveCollided){
            m_shapeTemp.setPosition(new Vector(m_shape.getPosition().getX(),
                        (rowCollide) * GBlock.getActualHeight()));
        }
        else{
            m_shapeTemp.setPosition(new Vector(m_shape.getPosition().getX(), 
                    HEIGHT  - (m_shape.getArrayValue().length + 1) * GBlock.getActualHeight()));
        }
    }
    
    private boolean checkEat(){
        //kiem tra xem coi co cai nao duoc an khong
        for (int i = m_arrayValue.length - 1; i >= 0; i--) {
            
            for (int j = 0; j < m_arrayValue[i].length; j++) {
                //co 1 o = 0 tuc la chua an duoc
                if(m_arrayValue[i][j] == 0)
                    break;
                
                if(j == m_arrayValue[i].length - 1){
                    m_score++;
                    
                    //<editor-fold defaultstate="collapsed" desc="Xoa phan tu bi an">
                    //xoa di cac phan tu bi an
                    for (int k = 0; k < m_blocks.size(); k++) {
                        GBlock block = m_blocks.get(k);
                        
                        if(block.getPostion().getY() == i * GBlock.getActualHeight()){
                            m_blocks.remove(block);
                            k--;
                        }
                    }
                    //</editor-fold>
                    
                    for (int k = 0; k < m_arrayValue[i].length; k++) {
                        m_arrayValue[i][k] = 0;
                    }
                    
                    //<editor-fold defaultstate="collapsed" desc="dich chuyen cac phan tu ben tren xuong">
                    for (int k = 0; k < m_blocks.size(); k++) {
                        GBlock block = m_blocks.get(k);
                        
                        if(block.getPostion().getY() < i * GBlock.getActualHeight()){
                            block.setPosition((block.getPostion().add(new Vector(0, GBlock.getActualHeight()))));
                        }
                    }                   
                    //</editor-fold>  
                    
                    //dich chuyen gia tri cua mang
                    boolean isAllZero = true; //kiem tra neu hang do toan so 0 thi ngung
                    
                    //<editor-fold defaultstate="collapsed" desc="swap array value" >
                    for (int k = i; k - 1>= 0; k--) {
                        isAllZero = true;
                        
                        for (int l = 0; l < m_arrayValue[i].length; l++) {
                            
                            if(m_arrayValue[k - 1][l] != 0)
                                isAllZero = false;
                            
                            int temp = m_arrayValue[k][l];
                            m_arrayValue[k][l] = m_arrayValue[k - 1][l];
                            m_arrayValue[k - 1][l] = temp;
                        }
                        
                        if(isAllZero)
                            break;
                    }
                    //</editor-fold>   
                    
                    i++;
                    break;
                }                                
            }
        }
        
        return false;        
    }
        
    private boolean checkCollidedUnder(){    
       //kiem tra xem ben duoi co cham vao o nao khong
        for (int i = 0; i < m_shape.getArrayValue().length; i++) {
            for (int j = 0; j < m_shape.getArrayValue()[i].length; j++) {
                //kiem tra ben duoi  
                //System.out.println("X: " + (m_shapeXPos + j) + "width count: " + WIDTH_COUNT);
                
                if(m_shapeYPos + i + 1 >= HEIGHT_COUNT){
                    //checkEat();
                    return false;
                }  
                
                if((m_arrayValue[m_shapeYPos + i + 1][m_shapeXPos + j] != 0
                        && m_shape.getArrayValue()[i][j] != 0)){  
                    
                    m_shape.setAllowMove(false);
                    m_isChecking = true;
                    
                    return true;
                }                
            }                    
        }
        
        return false;
    }
        
    private boolean checkCollideLeftRightTop(CollitionState state){     
        refreshShapeXYPos();
        
        for (int i = 0; i < m_shape.getArrayValue().length; i++) {
            for (int j = 0; j < m_shape.getArrayValue()[i].length; j++) {
                
                if(state == CollitionState.Top){
                    
                    if(m_shapeXPos + j >= WIDTH_COUNT){
                        m_shape.setRotationValue(m_shape.getRotationValue() - 1);
                        refreshShapeXYPos();
                        
                        return true;
                    }
                    
                    if(m_shapeYPos + i < m_arrayValue.length && m_shapeXPos + j < m_arrayValue[0].length){
                        if(m_arrayValue[m_shapeYPos + i][m_shapeXPos + j] != 0
                                && m_shape.getArrayValue()[i][j] != 0){
                            m_shape.setRotationValue(m_shape.getRotationValue() - 1);
                            refreshShapeXYPos();
                            
                            return true;
                        }
                    }
                }
                
                if(state == CollitionState.Left || state == CollitionState.All){
                    //kiem tra ben trai
                    if(m_shapeXPos + j - 1 >= 0 && m_shapeYPos + i >= 0){
                        if(m_arrayValue[m_shapeYPos + i][m_shapeXPos + j - 1] != 0
                                && m_shape.getArrayValue()[i][j] != 0){
                            return true;
                        }
                    }
                }
                
                if(state == CollitionState.Right || state == CollitionState.All){
                    //kiem tra ben phai
                    if(m_shapeXPos + j + 1 < m_arrayValue[0].length){
                        if(m_arrayValue[m_shapeYPos + i][m_shapeXPos + j + 1] != 0
                                && m_shape.getArrayValue()[i][j] != 0){
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    private boolean copyBlocks(){       
        //System.out.println("size: " + m_blocks.size());
        
        for (int i = 0; i < m_shape.getListBlock().size(); i++) {
            //System.out.println("pos: " + m_shape.getPosition().getX() + " - Y: " + m_shape.getPosition().getY());
            GBlock block = m_shape.getListBlock().get(i).clone();
            
            block.setPosition(block.getPostion().add(m_shape.getPosition()));           
            m_blocks.add(block);
        }
        
        return true;
    }
    
    private boolean addArrayValue(){
        for (int i = 0; i < m_shape.getArrayValue().length; i++) {
            for (int j = 0; j < m_shape.getArrayValue()[i].length; j++) {
                if(m_shapeYPos + i < 0 || m_shapeXPos + j < 0)
                    return false;
                
                m_arrayValue[m_shapeYPos + i][m_shapeXPos + j] += m_shape.getArrayValue()[i][j];                
            }
        }
        
        return true;
    }         
    //</editor-fold>  
}
