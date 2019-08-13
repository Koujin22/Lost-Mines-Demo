/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
class MilitiaGuard extends NPC{
    
    
    private static final ImageIcon[] MILITIA_SKIN = Variables.getMILITIA_SKIN();
    
    
    MilitiaGuard(JLayeredPane canvasMap, int startTileX, int startTileY, Object synchLock, Object pauseLock, BackgroundMap bgMap, int key) {
        super(canvasMap, MILITIA_SKIN, startTileX ,startTileY, bgMap, pauseLock, synchLock, key);
        
    }

    

    @Override
    void updatePos() {
        
        super.updatePos();
        hitbox.setLocation((int)npc.getLocation().getX()+35, (int)npc.getLocation().getY()+80);
    }
    
    @Override
    JLabel setUpHitbox() {
        hitbox = new JLabel();
        hitbox.setBounds((int) npc.getLocation().getX()+30, (int) npc.getLocation().getX()+70, 25, 20);
        return hitbox;
    }
    
    @Override
    public void interaction(){

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
    
    @Override
    void updateSkin(int skinNumberSynch) {
        npc.setIcon(skins[skinNumberSynch%4]);
    }
    
    
    
}
