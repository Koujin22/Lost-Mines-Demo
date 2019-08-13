/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
abstract class NPC extends DialogoLayout implements Runnable, Comparable<NPC>, ThreadStop {
    
    private final JLayeredPane canvasCharacters;
    private boolean pauseFlag =  false;
    private boolean isInteracting = false;
    private final Object synchLock;
    private final Object pauseLock;
    private BackgroundMap bgMap;
    private boolean stopThreadFlag = false;
    private int key;
    
    
    PlayerWorld player;
    
    final ImageIcon[] skins;
    JLabel npc, hitbox;
    int startTileX;
    int startTileY;
    
    
    NPC(JLayeredPane canvasCharacters, ImageIcon[] skins, int startTileX, int startTileY, BackgroundMap bgMap, Object pauseLock, Object synchLock, int key){
        super(Juego.getCanvasOpenWorld());
        this.key = key;
        this.bgMap = bgMap;
        this.canvasCharacters = canvasCharacters;
        this.skins = skins;
        this.startTileX = startTileX;
        this.startTileY = startTileY;
        this.pauseLock = pauseLock;
        this.synchLock = synchLock;
        
        
        setUpNPC();
    }
    
    private void setUpNPC(){
        
        npc = new JLabel();
        
        int mapPosX = bgMap.getStartTilePosX();
        int mapPosY = bgMap.getStartTilePosY();
        
        int tilePosX = mapPosX/4;
        int tilePosY = mapPosY/4;
        
        int dispX = (mapPosX%4)*8;
        int dispY = (mapPosY%4)*8;
        
        npc.setBounds((startTileX-tilePosX)*32+dispX, (startTileY-tilePosY)*32+dispY, 96, 96);
        npc.setIcon(skins[0]);
        
        hitbox = setUpHitbox();
        
        canvasCharacters.add(npc, new Integer(0));
        
        canvasCharacters.repaint();
        
    }
    
    private void tick(int skinNumberSynch) {
        
        synchronized(synchLock){
            try {
                synchLock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(MilitiaGuard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        updateSkin(skinNumberSynch/4);
        updatePos();
        
    }
    
    final void setInteractionOff(){
        isInteracting = false;
    }
    
    final NPC getNPC(){
        return this;
    }
    
    final JLabel getNpcJLabel() {
        return npc;
    }
    
    final void startInteraction(CountDownLatch lock, PlayerWorld player) {
        this.player = player;
        if(!isInteracting){
            isInteracting = true;
            
            
            new Thread(new DialogManager(lock)).start();
        }
        
    }
    
    final JLabel getHitbox() {
        return hitbox;
    }
    
    void updatePos(){
        int mapPosX = bgMap.getStartTilePosX();
        int mapPosY = bgMap.getStartTilePosY();
        
        int tilePosX = mapPosX/4;
        int tilePosY = mapPosY/4;
        
        int dispX = (mapPosX%4)*8;
        int dispY = (mapPosY%4)*8;
        
        npc.setBounds((startTileX-tilePosX)*32-dispX, (startTileY-tilePosY)*32-dispY, 96, 96);
    }
    
    void addHitboxForDebugg(JLabel hitbox){
        canvasCharacters.add(hitbox, new Integer(0));
    }
    
    void death(){
        
        stopThreadFlag = true;
        canvasCharacters.remove(npc);
        hitbox.setLocation(-100, -100);
    }
    
    
    abstract void interaction();
    abstract JLabel setUpHitbox();
    abstract void updateSkin(int skinNumberSynch);

    @Override
    public int compareTo(NPC o) {
        return this.hitbox.getY() - o.getHitbox().getY();
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
            
            if(pauseFlag=MoveAction.isPauseFlag()){
                
                synchronized(pauseLock){
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FirstCity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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

    @Override
    public void stopThread() {
        pauseFlag = true;
    }

    int getKey() {
        return key;
    }
    
    
    private class DialogManager implements Runnable{

        private final CountDownLatch lock;
        
        DialogManager(CountDownLatch lock){
            this.lock = lock;
        }
        
        @Override
        public void run() {
            
            interaction();
            lock.countDown();
        }
        
    }
    
}
