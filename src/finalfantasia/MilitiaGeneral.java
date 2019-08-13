/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
class MilitiaGeneral extends NPC implements QuestGiver{
    
    
    private static final ImageIcon[] MILITIA_SKIN = Variables.getMILITIA_SKIN();
    private boolean quest = false;
    
    
    MilitiaGeneral(JLayeredPane canvasMap, int startTileX, int startTileY, Object synchLock, Object pauseLock, BackgroundMap bgMap, int key) {
        super(canvasMap, MILITIA_SKIN, startTileX ,startTileY, bgMap, pauseLock, synchLock, key);
        
    }

    @Override
    void updatePos() {
        
        super.updatePos();
        hitbox.setLocation((int)npc.getLocation().getX()+35, (int)npc.getLocation().getY()+80);
    }

    
    @Override
    public void interaction(){
        if(!quest){
            giveQuest();
        }else if(player.completeQuest()){
        
            showDialogoComp();
            
            JLabel name = setName("Guard");
            JLabel text = setText("Muchas gracias por tu ayuda, "+playerName+". Por el momento esto es todo el contendio del juego. Muchas gracias por jugar.");
            
            synchronized(continueLock){
                try {
                    continueLock.wait();

                } catch (InterruptedException ex) {
                    Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            removeText(text, name);
            hideDialogoComp();
            setInteractionOff();
        
        }else{
            
            showDialogoComp();
            
            JLabel name = setName("Guard");
            JLabel text = setText("King's honor, friend");
            
            synchronized(continueLock){
                try {
                    continueLock.wait();

                } catch (InterruptedException ex) {
                    Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            removeText(text, name);
            hideDialogoComp();
            setInteractionOff();
        
            
        }
        
    }
   
    private void moveGuard() {
        
        startTileX = 35;
        startTileY = 73;
        
    }
    
    @Override
    JLabel setUpHitbox() {
        hitbox = new JLabel();
        hitbox.setBounds((int) npc.getLocation().getX()+30, (int) npc.getLocation().getX()+70, 25, 20);
        return hitbox;
    }

    @Override
    public void giveQuest() {
        
        
        showDialogoComp();
        
        JLabel name = setName("Guard");
        JLabel text = setText("Mata los 3 esqueleto que invadieron el mercado porfavor.");
        
        quest = questConfirmation(continueLock);
        removeText(text, name);
        
        if(quest){
            name = setName(playerName);
            text = setText("Si claro bro");

            synchronized(continueLock){
                try {
                    continueLock.wait();

                } catch (InterruptedException ex) {
                    Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            removeText(text, name);

            name = setName("Guard");
            text = setText("Muchas gracias");
            player.addQuest(new Quest(3, Skeleton.class));
            
        }else{
            name = setName(playerName);
            text = setText("Nel perro, estoy ocupado");
            
            synchronized(continueLock){
                try {
                    continueLock.wait();

                } catch (InterruptedException ex) {
                    Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            removeText(text, name);
            
            name = setName("Guard");
            text = setText("Entonces no puedes pasar");
        }
        synchronized(continueLock){
            try {
                continueLock.wait();

            } catch (InterruptedException ex) {
                Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(quest)
            moveGuard();
        
        removeText(text, name);
        hideDialogoComp();
        setInteractionOff();
    }

    @Override
    void updateSkin(int skinNumberSynch) {
        npc.setIcon(skins[skinNumberSynch%4]);
    }

    
    
    
    
    
    
}
