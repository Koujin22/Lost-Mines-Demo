/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public class Combat implements Runnable {

    
    private final static int WINDOW_WIDTH = (int) Variables.getWINDOW_SIZE().
            getWidth();
    private final static int WINDOW_HEIGHT=  (int) Variables.getWINDOW_SIZE().
            getHeight();
    private final JPanel canvasPadre;
    private final CountDownLatch combatLock;
    private final ImageIcon COMBAT_BACKGROUND = Variables.getCOMBAT_BACKGROUND();
    private final Object pauseLock = new Object();
    private final Pause pause;
    
    private int selectionPosition = 0, subMenuIndex=0;
    private JLabel background, selection, subMenu, atkMenu, mgMenu, itemMenu, defMenu;
    private JLayeredPane canvasCombate, canvasUI, canvasCharacters;
    private boolean pauseFlag;
    private String[] partyEnemies;
    private ArrayList<CharacterCombat> enemies;
    private ArrayList<Thread> enemiesThread;
    private PlayerCombat player;
    private boolean turnoJugador =  true;
    private int playerHealth;
    private boolean vivo = true;
    private JLabel statusPanel;

    Combat(JPanel canvasPadre, CountDownLatch combatLock, String[] partyEnemies, int playerHealth){
        this.canvasPadre = canvasPadre;
        this.combatLock = combatLock;
        this.partyEnemies = partyEnemies;
        this.playerHealth = playerHealth;
        setUpLayOut();
        pause = new Pause(canvasCombate, pauseLock);
        MoveAction.setPauseLock(pauseLock);
        setUpStatusPanel();
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="SetUpMethods">
    private void setUpLayOut(){
        
        setUpCanvasComabte();  
        setUpUI();
        setUpCanvasCharacters();
        setUpCharacters();
        setUpKeyBinds();
        
    }
    
    private void setUpCanvasComabte(){
        
        canvasCombate = new JLayeredPane();
        canvasCombate.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
        canvasCombate.setDoubleBuffered(true);
        canvasCombate.requestFocus();
        
        setUpBackgroundImage();
        
        canvasPadre.add(canvasCombate);
        
    }
    
    private void setUpBackgroundImage() {
        
       background = new JLabel();
       background.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
       background.setIcon(COMBAT_BACKGROUND);
       
       canvasCombate.add(background, new Integer(0));
        
    }   
        
    private void setUpUI(){
        setUpCanvasUI();
        setUpBackgroundUI();
        setUpMenus();
    }
    
    private void setUpCanvasUI(){
        canvasUI = new JLayeredPane();
        canvasUI.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
        canvasUI.setOpaque(false);
        canvasCombate.add(canvasUI, new Integer(2));
    }
    
    private void setUpBackgroundUI(){
        
        JLabel menuBackground = new JLabel();
        
        menuBackground.setBounds(WINDOW_WIDTH/8, (3*WINDOW_HEIGHT)/4, 
                (WINDOW_WIDTH)/8,WINDOW_HEIGHT/4);
        menuBackground.setBackground(Color.black);
        menuBackground.setOpaque(true);
        menuBackground.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        
        JLabel optionsBackground = new JLabel();
        
        optionsBackground.setBounds(WINDOW_WIDTH/4, (3*WINDOW_HEIGHT)/4, WINDOW_WIDTH/8, WINDOW_HEIGHT/4);
        optionsBackground.setBackground(Color.black);
        optionsBackground.setOpaque(true);
        optionsBackground.setBorder(new MatteBorder(1, 0, 1, 0, Color.white));        
        
        JLabel panelBackground = new JLabel();
        
        panelBackground.setBounds((3*WINDOW_WIDTH)/8, (3*WINDOW_HEIGHT)/4, WINDOW_WIDTH/2, WINDOW_HEIGHT/4);
        panelBackground.setBackground(Color.black);
        panelBackground.setOpaque(true);
        panelBackground.setBorder(BorderFactory.createLineBorder(Color.white, 1)); //new MatteBorder(1, 0, 1, 1, Color.white)
        
        canvasUI.add(panelBackground, new Integer(0));
        canvasUI.add(optionsBackground, new Integer(0));
        canvasUI.add(menuBackground, new Integer(0));
        
    }
    
    private void setUpMenus(){
        
        setUpActionMenu();
        
        setUpAtackMenu();
        setUpMagicMenu();
        setUpDefenseMenu();
        setUpItemMenu();
        
        setUpSubMenu();
        
        setUpSelection();
        
    }
    
    private void setUpActionMenu(){
        
        JLabel atack = new JLabel();
        atack.setName("atack");
        atack.setBounds(WINDOW_WIDTH/8, (3*WINDOW_HEIGHT)/4, 
                (WINDOW_WIDTH)/8,WINDOW_HEIGHT/16);
        atack.setText("Atack");
        atack.setForeground(Color.white);
        atack.setHorizontalAlignment(SwingConstants.CENTER);
        atack.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                
                updateSelection(0);
                
            }
        });
        
        JLabel magic = new JLabel();
        magic.setName("magic");
        magic.setBounds(WINDOW_WIDTH/8, (13*WINDOW_HEIGHT)/16, 
                (WINDOW_WIDTH)/8,WINDOW_HEIGHT/16);
        magic.setText("Magic");
        magic.setForeground(Color.white);
        magic.setHorizontalAlignment(SwingConstants.CENTER);
        magic.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                
                updateSelection(1);
                
            }
        });
        
        JLabel defense = new JLabel();
        defense.setName("defense");
        defense.setBounds(WINDOW_WIDTH/8, (14*WINDOW_HEIGHT)/16, 
                (WINDOW_WIDTH)/8,WINDOW_HEIGHT/16);
        defense.setText("Defense");
        defense.setForeground(Color.white);
        defense.setHorizontalAlignment(SwingConstants.CENTER);
        defense.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                
                updateSelection(2);
                
            }
        });
        
        JLabel item = new JLabel();
        item.setName("item");
        item.setBounds(WINDOW_WIDTH/8, (15*WINDOW_HEIGHT)/16, 
                (WINDOW_WIDTH)/8,WINDOW_HEIGHT/16);
        item.setText("Item");
        item.setForeground(Color.white);
        item.setHorizontalAlignment(SwingConstants.CENTER);
        item.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                
                updateSelection(3);
                
            }
            
        
        });    
        
        canvasUI.add(atack, new Integer(1));
        canvasUI.add(magic, new Integer(1));
        canvasUI.add(defense, new Integer(1));
        canvasUI.add(item, new Integer(1));
        
    }
    
    private void setUpAtackMenu(){
        
        JLabel normal = setUpOption("normal", "Melee", 0, new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                
                player.setAtaque(0);
                updateSelection(10);
                
            }
            
        });
        
        JLabel special = setUpOption("special", "Combo", 1, new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                player.setAtaque(1);
                updateSelection(11);
            }
            
        });
        
        atkMenu = new JLabel();
        atkMenu.setOpaque(false);
        atkMenu.setBounds(0,0,1920, 1080);
        atkMenu.add(normal);
        atkMenu.add(special);
        
    }
    
    private void setUpMagicMenu(){
        
        JLabel normal = setUpOption("normal", "Fireball", 0, new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                updateSelection(10);
            }
            
        });
        
        
        JLabel special = setUpOption("special", "Pyroblast", 1, new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                updateSelection(11);
            }
            
        });
        
        mgMenu = new JLabel();
        mgMenu.setOpaque(false);
        
        mgMenu.setBounds(0,0,1920,1080);
        mgMenu.add(normal);
        mgMenu.add(special);
        
    }
    
    private void setUpDefenseMenu(){
        JLabel normal = setUpOption("normal", "Fisico", 0, new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                updateSelection(10);
            }
            
        });
        
        JLabel special = setUpOption("special", "Magico",1, new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                updateSelection(11);
            }
            
        });
        
        defMenu = new JLabel();
        defMenu.setOpaque(false);
        
        defMenu.setBounds(0,0,1920,1080);
        defMenu.add(normal);
        defMenu.add(special);
    }
    
    private void setUpItemMenu(){
        
        JLabel normal = setUpOption("normal", "No items", 0, new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                updateSelection(10);
            }
            
        });
        
        itemMenu = new JLabel();
        itemMenu.setOpaque(false);
        itemMenu.setBounds(0,0,1920,1080);
        itemMenu.add(normal);
        
    }
    
    private JLabel setUpOption(String name, String text, int posLista, MouseAdapter mouseClick ){
        
        int yLocation = ((12+(posLista))*WINDOW_HEIGHT)/16;
        
        JLabel normal = new JLabel();
        normal.setName(name);
        normal.setBounds(WINDOW_WIDTH/4, yLocation, 
                (WINDOW_WIDTH)/8,WINDOW_HEIGHT/16);
        normal.setText(text);
        normal.setForeground(Color.white);
        normal.setHorizontalAlignment(SwingConstants.CENTER);
        normal.addMouseListener(mouseClick);
        return normal;
    }
    
    private void setUpSelection(){
        
        selection = new JLabel();
        selection.setBounds(WINDOW_WIDTH/8, (3*WINDOW_HEIGHT)/4, 
                (WINDOW_WIDTH)/8,WINDOW_HEIGHT/16);
        selection.setBorder(BorderFactory.createLineBorder(Color.white, 5));
        
        canvasUI.add(selection, new Integer(2));
        
    }
    
    private void setUpSubMenu(){
        canvasUI.add(atkMenu, new Integer(1));
    }
        
    private void setUpKeyBinds(){
        MoveAction.setCombat(this);
        
        canvasCombate.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pause");
        canvasCombate.getActionMap().put("pause", new MoveAction(5, true));
        
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "Arriba");
        canvasCharacters.getActionMap().put("Arriba", new MoveAction(11, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "Izquierda");
        canvasCharacters.getActionMap().put("Izquierda", new MoveAction(12, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "Abajo");
        canvasCharacters.getActionMap().put("Abajo", new MoveAction(13, true));
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "Derecha");
        canvasCharacters.getActionMap().put("Derecha", new MoveAction(14, true));
        
        canvasCharacters.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "action");
        canvasCharacters.getActionMap().put("action", new MoveAction(15, true));
        
        MoveAction.setPauseLock(pauseLock);
    }
    
    private void setUpCanvasCharacters(){
        
        canvasCharacters = new JLayeredPane();
        canvasCharacters.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
        canvasCombate.add(canvasCharacters, new Integer(1));
        
    }
      
    private void setUpCharacters(){
        setUpPlayer();
        setUpEnemies();
    }    
    
    private void setUpEnemies(){
        
        enemies = new ArrayList();
        enemiesThread = new ArrayList();
        
        for (int i = 0; i < partyEnemies.length; i++) {
            switch(partyEnemies[i]){
                case"skeleton":
                    enemies.add(new SkeletonCombat(canvasCharacters, WINDOW_WIDTH/5, WINDOW_HEIGHT/2, pauseLock, this, String.valueOf(i)));
                    enemiesThread.add(new Thread(enemies.get(i)));
                    enemiesThread.get(i).setName("Enemy "+i);
                    enemiesThread.get(i).start();
            }
        }  
        
    }
    
    private void setUpPlayer(){
        
        player = new PlayerCombat(canvasCharacters, pauseLock, this, playerHealth);
        
        Thread playerThread = new Thread(player);
        playerThread.setName("player combat");
        
        playerThread.start();
        
    }
    
    private void setUpStatusPanel(){
        statusPanel = new JLabel();
        statusPanel.setBounds((3*WINDOW_WIDTH)/8, (3*WINDOW_HEIGHT)/4, WINDOW_WIDTH/2, WINDOW_HEIGHT/4);
        statusPanel.setForeground(Color.white);
        canvasUI.add(statusPanel, new Integer(1));
    }
    
    // </editor-fold>
    
    private void updateSelection(int index) {
        
        int xLocation = ((1+(index/10))*WINDOW_WIDTH)/8;
        int yLocation = ((12+(index%10))*WINDOW_HEIGHT)/16;
        
        selection.setLocation(xLocation, yLocation);
        
        if(index/10==0)
            updateSubMenu(index);
        
        
    }
    
    private void updateSubMenu(int index){
        
        canvasUI.remove(atkMenu);
        canvasUI.remove(mgMenu);
        canvasUI.remove(defMenu);
        canvasUI.remove(itemMenu);
        
        switch(index){
        
            case 0:
                canvasUI.add(atkMenu, new Integer(1));
                subMenuIndex = 0;
                break;
            case 1:
                canvasUI.add(mgMenu, new Integer(1));
                subMenuIndex = 1;
                break;
            case 2:
                canvasUI.add(defMenu, new Integer(1));
                subMenuIndex = 2;
                break;
            case 3:
                canvasUI.add(itemMenu, new Integer(1));
                subMenuIndex = 3;
                break;
                
        }
        
    }
    
    void moveUpSelection(){
        if(selectionPosition%10!=0)
            updateSelection(--selectionPosition);
    }
    
    void moveDownSelection(){
        if(selectionPosition%10!=3 && ((selectionPosition/10==0)   ||  (subMenuIndex!=3 && selectionPosition%10!=1) ) )
            updateSelection(++selectionPosition);
    }
    
    void moveLeftSelection(){
        if(selectionPosition/10!=0)
            updateSelection(selectionPosition=subMenuIndex);
        
    }
    
    void moveRightSelection(){
        if(selectionPosition/10!=1)
            updateSelection(selectionPosition=10);
        
    }
    
    void action(){
        if(turnoJugador)
            if(selectionPosition/10==1)
                switch(subMenuIndex){
                    case 0:
                        (((JLabel)atkMenu.getComponent(selectionPosition%10)).getMouseListeners()[0]).mousePressed(null);
                        turnoJugador = false;
                        canvasCombate.remove(canvasUI);
                        canvasCombate.repaint();
                        break;

                    default:
                        break;
                }
    }
    
    void enemyTurn(){
        for (int i = 0; i < enemies.size(); i++) {
            
            CountDownLatch turnLock = new CountDownLatch(1);
            
            enemies.get(i).setTurno(turnLock);
            enemies.get(i).setAtaque(0);
            
            try {
                turnLock.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(Combat.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        playerTurn();
       
    }
    
    CharacterCombat getTarget(CharacterCombat source){
        if(source instanceof PlayerCombat){
            return enemies.get(0);
        }
        else
            return player;
        
    }
    
    private void playerTurn(){
        
        turnoJugador = true;
        canvasCombate.add(canvasUI);
        
    }
    
    void endCombat(CharacterCombat character){
        if(character instanceof PlayerCombat){
            System.out.println("Perdiste");
        }else if(enemies.size()==1){
            canvasPadre.remove(canvasCombate);
            player.setDeath();
            vivo = false;
            combatLock.countDown();
            
        }else{
            enemies.remove(character);
        }
    }
    
    @Override
    public void run() {
       
        
        int skinNumberSynch = 0;
        long lastTime = System.nanoTime();
        final double ticks = 30D;
        double ns = 1000000000 / ticks;    
        double delta = 0;
        
        
        
        
        while(vivo){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
                
            
            if(pauseFlag = MoveAction.isPauseFlag()){
                pause.showMenu();
                synchronized(pauseLock){
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FirstCity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                MoveAction.setPauseFlagFalse();
                
                pause.hideMenu();
                lastTime = System.nanoTime();
            }
            
            if(delta >= 1){
                tick();
                delta--;
            }
        }
        
    }
    
    private void tick(){
        
            statusPanel.setText("Tu vida: "+player.getHealth()+" Vida enemigo: "+enemies.get(0).getHealth());
        
    }

    int getHealth() {
        return player.getHealth();
    }

}
