/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent; 
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair; 
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.KeyStroke;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public class PlayerWorld implements Runnable, ThreadStop, QuestTracker {

   
    
    private final Object pauseLock;
    private final Pause pause;
    private boolean stopThreadFlag = false;
    
    private boolean pauseFlag = false;
    
    private final OpenWorld openWorld;
    
    private ArrayList<Quest> quests = new ArrayList<>();
    private int health = 20;
    
    JLabel hitbox;
    JLayeredPane canvasCharacters;
    Object synchLock;
    JLabel player;
    ImageIcon[] R, L, U, D;
    BackgroundMap bgMap;
    boolean forward, back, left, right, shift;
    boolean move = false;
    int lastDirection = 0;
    int playerPositionX = 960;
    int playerPositionY = 750;
    int velocity = 8;
    
    
    PlayerWorld(JLayeredPane canvasCharacters, Object synchLock, BackgroundMap bgMap, Object pauseLock, OpenWorld openWorld, Pause pause){
        this.canvasCharacters = canvasCharacters;
        this.synchLock = synchLock;
        this.player = setUpPlayer();
        this.bgMap = bgMap;
        this.pauseLock = pauseLock;
        this.pause = pause;
        this.openWorld = openWorld;
        setUpSkins();      
        setUpBindings();
        
        
    }
    
    PlayerWorld getPlayer(){
        return this;
    }
    
    private void setUpSkins(){
        R = Variables.getR();
        U = Variables.getU();
        L = Variables.getL();
        D = Variables.getD();
    }
    
    private JLabel setUpPlayer(){
        
        JLabel player = new JLabel();
        player.setBounds(playerPositionX, playerPositionY, 96, 96);
        canvasCharacters.add(player, new Integer(1));
        
        
        hitbox = new JLabel();
        hitbox.setBounds((int)player.getBounds().getCenterX()-8, playerPositionY+65, 24,10);
        
        return player;
    } 
    
    
    @Override
    public void run() {
        int skinNumberSynch = 0;
        long lastTime = System.nanoTime();
        final double ticks = 30D;
        double ns = 1000000000 / ticks;    
        double delta = 0;
        
        
        
        while(!stopThreadFlag){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
                
            
            if(pauseFlag){ 
                pause.showMenu();
                synchronized(pauseLock){
                    try { 
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FirstCity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
                pause.hideMenu();
                pauseFlag = false;
                lastTime = System.nanoTime(); 
            }
            
            if(delta >= 1){
                tick(skinNumberSynch);
                skinNumberSynch++;
                delta--;
            }
        }
        
    }
    
    void hidePause(){
        pause.hideMenu();
    }
    
    void updateSkins(int skinNumberSynch){
        int limite = (move)? 6 : 5;
        int suma = (move)? 5:0;
        switch(lastDirection){
            case 0:
                player.setIcon(U[skinNumberSynch%limite + suma]);
                break;
            case 1:
                player.setIcon(D[skinNumberSynch%limite + suma]);
                break;
            case 2:
                player.setIcon(L[skinNumberSynch%limite + suma]);
                break;
            case 3:
                player.setIcon(R[skinNumberSynch%limite + suma]);
                break;
                
        }
    }
    
    public void updatePos() throws outOfBoundriesException, InterruptedException{
        int deltaX = 0;
        int deltaY = 0;
        int updateFactor = 1;
        int updateX = 0;
        int updateY = 0;
                  
        if(forward){
            deltaY-=velocity;
            lastDirection = 0;
        }
        if(back){
            deltaY+=velocity;
            lastDirection = 1;
        }
        if(left){
            deltaX-=velocity;
            lastDirection = 2;
        }
        if(right){
            deltaX+=velocity;
            lastDirection = 3;
        }
        
        move = (deltaX!=0 || deltaY!=0);
        
        if(deltaY != 0 && deltaX != 0){
            deltaX = (deltaX/5)*4;
            deltaY = (deltaY/5)*4;
        }
        
        if(shift){
            deltaX*=2;
            deltaY*=2;
            updateFactor = 2;
            
        }
        
        int dir = (deltaX!=0)? Math.abs(deltaX)/deltaX: 0;
        
        if(bgMap.collision(new Rectangle(playerPositionX+deltaX, playerPositionY,96,96), dir)){
            deltaX=0;
            
        }
        if(bgMap.collision(new Rectangle(playerPositionX, playerPositionY+deltaY,96,96), dir)){
            deltaY=0;
        }
        
        if(playerPositionX+deltaX<200){
            updateX = -1;
        }else if( playerPositionX+deltaX>1720){
            updateX = 1;
        }else{
            updateX = 0;
        }
        
        
        Pair<Boolean, NPC> collisionNPC = npcCollision(deltaX, deltaY);
        
        if(collisionNPC.getKey()){
            if(collisionNPC.getValue() instanceof Enemy){
                openWorld.startCombat(((Enemy)collisionNPC.getValue()).getParty(),collisionNPC.getValue());
                pauseFlag = false;
            }
            deltaX = 0;
            deltaY = 0;
        }
        
        if(playerPositionY+deltaY<100){
            updateY = -1;
        }else if(playerPositionY+deltaY>880){
            updateY = 1;
        }else{
            updateY = 0;
        }
        
        if(updateX!=0 || updateY!=0)
            if(!bgMap.update(updateX*updateFactor, updateY*updateFactor)){
                if(updateX!=0 && bgMap.update(updateX*updateFactor, 0)){
                    deltaX = 0;
                    
                }
                else if(updateY!=0 && bgMap.update(0, updateY*updateFactor)){
                    deltaY = 0;
                }
                
                    
            }else{
                deltaX = (updateX!=0)? 0: deltaX;
                deltaY = (updateY!=0)? 0: deltaY; 
            }
        
        if(playerPositionX+deltaX<0 || playerPositionX+deltaX>1850){
            deltaX=0;
        }
        if(playerPositionY+deltaY<0 || playerPositionY+deltaY>1020){
            deltaY=0;
        }
        
        player.setBounds(playerPositionX += deltaX, playerPositionY += deltaY, 96,96);
        
        updateHitBox(deltaX, deltaY);
        
        
    }
    
    private Pair<Boolean, NPC> npcCollision(int deltaX, int deltaY){
        ArrayList<NPC> characters = openWorld.getCharacters().getKey();
        boolean flag = false;
        
        Rectangle playerHitbox = new Rectangle(hitbox.getBounds());
        playerHitbox.setLocation(hitbox.getX()+deltaX, hitbox.getY()+deltaY);
        for (int i = 0; i < characters.size(); i++) {
            flag = playerHitbox.getBounds().intersects(characters.get(i).getHitbox().getBounds());
            if(flag){
                
                
                return new Pair(flag, characters.get(i));
                
            }
            
        }
        
        return new Pair(flag, null);
        
    }
    
    final void setUpBindings() {
        MoveAction.setPlayerWorld(this);
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "Forward");
        canvasCharacters.getActionMap().put("Forward", new MoveAction(0, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "Left");
        canvasCharacters.getActionMap().put("Left", new MoveAction(1, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "Back");
        canvasCharacters.getActionMap().put("Back", new MoveAction(2, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "Right");
        canvasCharacters.getActionMap().put("Right", new MoveAction(3, true));
        
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift W"), "SForward");
        canvasCharacters.getActionMap().put("SForward", new MoveAction(6, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift A"), "SLeft");
        canvasCharacters.getActionMap().put("SLeft", new MoveAction(7, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift S"), "SBack");
        canvasCharacters.getActionMap().put("SBack", new MoveAction(8, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift D"), "SRight");
        canvasCharacters.getActionMap().put("SRight", new MoveAction(9, true));
        
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released W"), "RForward");
        canvasCharacters.getActionMap().put("RForward", new MoveAction(0, false));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"), "RLeft");
        canvasCharacters.getActionMap().put("RLeft", new MoveAction(1, false));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released S"), "RBack");
        canvasCharacters.getActionMap().put("RBack", new MoveAction(2, false));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released D"), "RRight");
        canvasCharacters.getActionMap().put("RRight", new MoveAction(3, false));
        
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift released W"), "RForward");
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift released A"), "RLeft");
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift released S"), "RBack");
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift released D"), "RRight");
        
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pause");
        canvasCharacters.getActionMap().put("pause", new MoveAction(5, true));
        
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "interaction");
        canvasCharacters.getActionMap().put("interaction", new MoveAction(10, true));
        
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK, false), "shift");
        canvasCharacters.getActionMap().put("shift", new MoveAction(4, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released SHIFT"), "Rshift");
        canvasCharacters.getActionMap().put("Rshift", new MoveAction(4, false));
        
        //Dialog
        
        
        
    }
    
    void tick(int skinNumberSynch){
        
        synchronized(synchLock){
            try {
                synchLock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(PlayerWorld.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        updateVariables();
        updateSkins(skinNumberSynch/3);
        try {
            updatePos();
        } catch (outOfBoundriesException ex) {
            Logger.getLogger(PlayerWorld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PlayerWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
            
       
        
    }

    private void updateVariables() {
        forward = MoveAction.isForward();
        left = MoveAction.isLeft();
        back = MoveAction.isBack();
        right = MoveAction.isRight();
        shift = MoveAction.isShift();
        pauseFlag = MoveAction.isPauseFlag();
    }

    JLabel getPlayerJLabel() {
        return player;
    }
    
    JLabel getHitbox(){
        return hitbox;
    }

    private void updateHitBox(int deltaX, int deltaY) {
        
        hitbox.setLocation(hitbox.getX()+deltaX, hitbox.getY()+deltaY);
        
    }

    void interaction() {
        int deltaX = 0;
        int deltaY = 0;
        switch(lastDirection){
            
            case 0:
                deltaY = -16;
                break;
            case 1:
                deltaY = 16;
                break;
            case 2:
                deltaX = -16;
                break;
            case 3:
                deltaX = 16;
                break;
        }
        
        Pair<Boolean, NPC> coll = npcCollision(deltaX, deltaY);
        
        if(coll.getKey()){
            if(coll.getValue() instanceof Enemy){
                openWorld.startCombat(((Enemy)coll.getValue()).getParty(), coll.getValue());
                pauseFlag = false;
                
            }else{
                canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke("ENTER"));
                CountDownLatch lock = new CountDownLatch(2);          
                ((NPC)coll.getValue()).startInteraction(lock, this);


                lock.countDown();

                canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "interaction");
                canvasCharacters.requestFocus();
            }
        }
    }

    
    @Override
    public void stopThread() {
        pauseFlag = true;
    }

    @Override
    public void addQuest(Quest quest) {
        quests.add(quest);
    }

    @Override
    public boolean completeQuest() {
        for (int i = 0; i < quests.size(); i++) {
            if(quests.get(i).isCompleted()){
                quests.remove(quests.get(i));
                return true;
            }
        }
        return false;
    }
    
    @Override
    public ArrayList<Quest> getQuests() {
        return quests;
    }

    int getHealth() {
        return health;
    }
    
    void setHealth(int health){
        this.health = health;
    }
    
}
