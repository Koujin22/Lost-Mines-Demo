/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public class SkeletonCombat extends CharacterCombat {

    public SkeletonCombat(JLayeredPane canvasCharactersCombat, int positionX, int positionY, Object pauseLock, Combat combat, String name) {
        super(Variables.getSKELETON_SKINS(), Variables.getSKELETON_ATK_SKINS(), canvasCharactersCombat, positionX, positionY, pauseLock, 296, 216, combat, 13, name);
    }
    
   

    @Override
    void updateSkin(int skinNumberSynch, boolean move) {
        if(contador ==14){
            contador = 0;
            character.setLocation(positionX, positionY);
        }
        if(!atacando)
            character.setIcon(SKIN[skinNumberSynch%8]);
        else if(skinNumberSynch%2==0){
            
            ataque();
            
        }
    }

    @Override
    void tick(int skinNumberSynch) {
        
        updateSkin(skinNumberSynch/2, false);
        
    }

    @Override
    void ataque() {
        character.setIcon(ATK_SKIN[contador]);
        character.setLocation(positionX+7, positionY-28);
        if(contador++ == 13){
            atacando = false;
            target.setDamage(tirarDados(1, 6)+2);
            turno.countDown();
        }

    }
    
}
