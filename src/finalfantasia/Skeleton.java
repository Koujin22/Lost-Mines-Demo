/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public class Skeleton extends NPC implements Enemy {

    private static final ImageIcon[] SKELETON_SKIN = Variables.getSKELETON_SKINS();
    private int partySize;
            
    public Skeleton(JLayeredPane canvasMap, int startTileX, int startTileY, Object synchLock, Object pauseLock, BackgroundMap bgMap, int partySize, int key) {
        super(canvasMap, SKELETON_SKIN, startTileX, startTileY, bgMap, pauseLock, synchLock, key);
        this.partySize = partySize;
    }
    
    
    
    @Override
    void updatePos() {
        
        super.updatePos();
        hitbox.setLocation((int)npc.getLocation().getX()+10, (int)npc.getLocation().getY()+10);
    }

    @Override
    void interaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    JLabel setUpHitbox() {
        hitbox = new JLabel();
        hitbox.setBounds((int) npc.getLocation().getX()+10, (int) npc.getLocation().getX()+10, 75, 100);
        return hitbox;
    }
    
    @Override
    void updateSkin(int skinNumberSynch) {
        npc.setIcon(skins[skinNumberSynch%8]);
    }

    @Override
    public String[] getParty() {
        String[] party = new String[partySize];
        for (int i = 0; i < partySize; i++) {
            party[i] = "skeleton";
        }
        return party;
    }
    
}
