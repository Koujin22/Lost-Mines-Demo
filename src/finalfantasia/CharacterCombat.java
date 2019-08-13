/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**a
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
abstract class CharacterCombat implements Runnable, CombateStatus{
    
    private boolean pauseFlag;
    private Object pauseLock;
    
    
    int health;
    
    JLabel character;
    int positionX;
    int positionY;
    final ImageIcon[] SKIN, ATK_SKIN, SIZED_SKIN, SIZED_ATK_SKIN;
    private final JLayeredPane canvasCharactersCombat;
    boolean atacando= false;
    int tipoAtk = 0;
    int contador = 0;
    int width;
    int height;
    private boolean vivo = true;
    CharacterCombat target;
    CountDownLatch turno;
    Combat combat;
    private String name;
    
    
    CharacterCombat(ImageIcon[] skin, ImageIcon[] atkSkin, JLayeredPane canvasCharactersCombat, int positionX, int positionY, Object pauseLock, int width, int height, Combat combat, int health, String name){
        this.name = name;
        this.pauseLock = pauseLock;
        this.SIZED_SKIN = skin;
        this.SIZED_ATK_SKIN = atkSkin;
        this.canvasCharactersCombat = canvasCharactersCombat;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.combat = combat;
        this.health = health;
        
        SKIN = new ImageIcon[SIZED_SKIN.length];
        ATK_SKIN = new ImageIcon[SIZED_ATK_SKIN.length];
        updateSizeSkin();
        
        
        setUpCharater();
    }
    
    private void updateSizeSkin(){
        for (int i = 0; i < SKIN.length; i++) {
            SKIN[i] = new ImageIcon(SIZED_SKIN[i].getImage().getScaledInstance(192, 192, Image.SCALE_DEFAULT));
        }
        for (int i = 0; i < ATK_SKIN.length; i++) {
            ATK_SKIN[i] = new ImageIcon(SIZED_ATK_SKIN[i].getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        }
    }
    
    String getName(){
        return name;
    }
    
    private void setUpCharater(){
        
        character = new JLabel();
        character.setBounds(positionX, positionY, width,height);
        character.setIcon(SKIN[0]);
        
        canvasCharactersCombat.add(character, new Integer(0));
        
        
        
    }
    
    void setAtaque(int tipo){
        atacando = true;
        tipoAtk = tipo;
        contador = 0;
        target = combat.getTarget(this);
    }
    
    void setTurno(CountDownLatch turno){
        this.turno = turno;
    }
    
    abstract void updateSkin(int skinNumberSynch, boolean move);
    abstract void tick(int skinNumberSynch);
    abstract void ataque();
            
    
    
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

    void setDeath(){
        this.vivo = false;
    }
    
    int tirarDados(int cantidad, int tipo){
        int resu = 0;
        Random rnd = new Random();
        for (int i = 0; i < cantidad; i++) {
            
            resu += rnd.nextInt(tipo-1)+1;
            
        }
        return resu;
    }
    
    int getHealth(){
        return health;
    }
    
    @Override
    public void setDamage(int damage) {
        health = health - damage;
        if(health<=0){
            setDeath();
            combat.endCombat(this);
        }
    }
    
    
}
