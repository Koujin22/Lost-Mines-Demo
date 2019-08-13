/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public class OpenWorld implements Runnable {

    
    private final static int WINDOW_WIDTH = (int) Variables.getWINDOW_SIZE().
            getWidth();
    private final static int WINDOW_HEIGHT=  (int) Variables.getWINDOW_SIZE().
            getHeight();
    private final JLayeredPane canvasOpenWorld;
    private final Object synchLock;
    private JLayeredPane canvasMapa;
    private JLayeredPane canvasChracters;
    private static final Object pauseLock = new Object();
    private final  ConcurrentHashMap<String, NPC> npcDictionary;
    private NPC guardGeneral, guardLeft, guardRight, skeleton1, skeleton2, skeleton3;
    private FirstCity firstCityBacground;
    private PlayerWorld player;
    private List<ThreadStop> threadList;
    private Object combatLockPause;
    
    OpenWorld(JLayeredPane canvasOpenWorld){
        this.canvasOpenWorld = canvasOpenWorld;
        this.synchLock = new Object();
        
        threadList = new ArrayList<>();
        
        MoveAction.setPauseLock(pauseLock);
        npcDictionary = new ConcurrentHashMap<>();
    }
    
    @Override
    public void run() {
        
        setUpCanvasCharacters();
        setUpCanvasMapa();
        
        
        try {
            
            
            FirstCity firstCityBackground = setUpCity();
            setUpCharacters(firstCityBackground);
            setUpEnemies(firstCityBackground);
            
            
        } catch (InterruptedException ex) {
            Logger.getLogger(OpenWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }    
    
    private void setUpCharacters(FirstCity firstCityBackground){
        
        Pause pause = new Pause(canvasOpenWorld, pauseLock);
        
        player = new PlayerWorld(canvasChracters, synchLock, firstCityBackground, pauseLock, this, pause);
        
        guardGeneral = new MilitiaGeneral(canvasChracters, 37, 76, synchLock, pauseLock, firstCityBackground, 1);
        guardLeft = new MilitiaGuard(canvasChracters, 36, 75, synchLock, pauseLock, firstCityBackground, 2);
        guardRight = new MilitiaGuard(canvasChracters, 38, 75, synchLock, pauseLock, firstCityBackground, 3);
        
        npcDictionary.put("1general", guardGeneral);
        npcDictionary.put("2guardRight", guardLeft);
        npcDictionary.put("3guardLeft", guardRight);
        
        threadList.add((ThreadStop)player);
        threadList.add((ThreadStop)guardGeneral);
        threadList.add((ThreadStop)guardLeft);
        threadList.add((ThreadStop)guardRight);
             
        Thread playerThread = new Thread(player);
        Thread guardGeneralThread = new Thread(guardGeneral);
        Thread guardLeftThread = new Thread(guardLeft);
        Thread guardRightThread = new Thread(guardRight);
        
        playerThread.setName("Player");
        guardGeneralThread.setName("General");
        guardLeftThread.setName("Guard Left");
        guardRightThread.setName("Guard Right");
        
        playerThread.start();
        guardGeneralThread.start();
        guardLeftThread.start();
        guardRightThread.start();
        
        
    }
    
    private void setUpEnemies(FirstCity firstCityBackground) {
        
        skeleton1 = new Skeleton(canvasChracters, 30, 50, synchLock, pauseLock, firstCityBackground, 1, 4);
        skeleton2 = new Skeleton(canvasChracters, 25, 15, synchLock, pauseLock, firstCityBackground, 1, 5);
        skeleton3 = new Skeleton(canvasChracters, 70, 23, synchLock, pauseLock, firstCityBackground, 1, 6);
        
        
        npcDictionary.put("4Skeleton", skeleton1);
        npcDictionary.put("5Skeleton", skeleton2);
        npcDictionary.put("6Skeleton", skeleton3);
        
        threadList.add((ThreadStop)skeleton1);
        threadList.add((ThreadStop)skeleton2);
        threadList.add((ThreadStop)skeleton3);
        
        Thread skeletonThread1 = new Thread(skeleton1);
        Thread skeletonThread2 = new Thread(skeleton2);
        Thread skeletonThread3 = new Thread(skeleton3);
        
        skeletonThread1.setName("Skeleton1");
        skeletonThread2.setName("Skeleton2");
        skeletonThread3.setName("Skeleton3");        
        
        skeletonThread1.start();
        skeletonThread2.start();
        skeletonThread3.start();
        
    }
    
    
    private void setUpCanvasCharacters(){
        canvasChracters = new JLayeredPane();
        canvasChracters.setDoubleBuffered(true);
        canvasChracters.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
        canvasChracters.setName("canvasCharacters");
        
        canvasOpenWorld.add(canvasChracters, new Integer(1));
    }
    
    private FirstCity setUpCity() throws InterruptedException {
        
        
        combatLockPause = new Object();
        firstCityBacground = new FirstCity(canvasMapa, 28,260, synchLock, this, combatLockPause);//28 260
        
        threadList.add((ThreadStop)firstCityBacground);
        
        Thread firstCityThread = new Thread(firstCityBacground);
        firstCityThread.setName("First City");
        firstCityThread.start();
        return firstCityBacground;
        
    }
    
    private void setUpCanvasMapa(){
        
        
        canvasMapa = new JLayeredPane();
        canvasMapa.setDoubleBuffered(true);
        canvasMapa.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
        canvasMapa.setName("canvasMapa");
        
        canvasOpenWorld.add(canvasMapa, new Integer(0));
        
    }
    
    static Object getPauseLock(){
        return pauseLock;
    }
    
    
    
    Pair<ArrayList<NPC>, PlayerWorld> getCharacters() throws ArrayIndexOutOfBoundsException {
        ArrayList<NPC> characters = new ArrayList<>();
        npcDictionary.forEach((k,v)->{
            characters.add(v);
        });
        
        return new Pair(characters, player);
    }

    JLayeredPane getCanvasCharacters() {
        return canvasChracters;
    }
    
    void startCombat(String[] enemies, NPC enemy){
        
        Iterator iterator = threadList.iterator();
        while(iterator.hasNext()) {
            ((ThreadStop)iterator.next()).stopThread();
        }
        
        WindowManager.getCanvasPadre().remove(canvasOpenWorld);
        
        CountDownLatch combatLock = new CountDownLatch(1);
        Combat combat = new Combat(WindowManager.getCanvasPadre(), combatLock, enemies, player.getHealth() );
        
        Thread combatThread = new Thread(combat);
        
        combatThread.setName("combatThread");
        
        combatThread.start();
        
        try {
            combatLock.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(OpenWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(enemy instanceof Skeleton){
            npcDictionary.remove(enemy.getKey()+"Skeleton", enemy);
        }
        
        ArrayList<Quest> playerQuests = player.getQuests();
        
        for (int i = 0; i < playerQuests.size(); i++) {
            
            playerQuests.get(i).agregarKill(enemy.getClass());
            
        }
                
        enemy.death();
        
        MoveAction.setPauseLock(pauseLock);
        player.setHealth(combat.getHealth());
        combat = null;
        combatThread = null;
        synchronized(pauseLock){
            pauseLock.notifyAll();
        }
        synchronized(combatLockPause){
            combatLockPause.notifyAll();
        }
        WindowManager.getCanvasPadre().add(canvasOpenWorld);
        MoveAction.resetValues();
        canvasOpenWorld.repaint();
         
        
    }
    
    
}
