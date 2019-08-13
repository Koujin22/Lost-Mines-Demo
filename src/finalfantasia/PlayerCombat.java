/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.util.Random;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public class PlayerCombat extends CharacterCombat{

     private final static int WINDOW_WIDTH = (int) Variables.getWINDOW_SIZE().
            getWidth();
    private final static int WINDOW_HEIGHT=  (int) Variables.getWINDOW_SIZE().
            getHeight();
    private final int[] atkSequence = {0,1,2,6,7,8,12,13,14,15,18,19,20,21,22,23,26,27,28,29,30};
    private int despla = 0;
    
    public PlayerCombat(JLayeredPane canvasCharactersCombat, Object pauseLock, Combat combat, int health) {
        super(Variables.getL(), Variables.getATK(), canvasCharactersCombat, (3*WINDOW_WIDTH)/4, WINDOW_HEIGHT/2, pauseLock, 192, 192, combat, health, "Player");
        
    }

    

    @Override
    void updateSkin(int skinNumberSynch, boolean move) {
        int limite = (move)? 6 : 5;
        int suma = (move)? 5:0;
        if(!atacando){
            character.setIcon(SKIN[skinNumberSynch%limite + suma]);
        }
        else if(skinNumberSynch%3==0){
            ataque();
            
        }
        
                
    }
    
    
    @Override
    void tick(int skinNumberSynch) {
        updateSkin(skinNumberSynch/2, false);
        
    }

    @Override
    void ataque() {
        switch(tipoAtk){
                
            case 0:
                character.setIcon(ATK_SKIN[contador]);
                if(contador++ == 5){
                    atacando = false;
                    target.setDamage(tirarDados(1, 6)+4);
                    
                    combat.enemyTurn();
                }
                break;
            case 1:
                
                character.setIcon(ATK_SKIN[atkSequence[contador]]);
                
                if(contador==1||contador == 4 || contador == 7 || contador == 17)
                    despla += 30;
                if(contador == 13)
                    despla += 90;
                
                character.setLocation(positionX-despla, positionY);
                
                if(contador++==20){
                    character.setLocation(positionX, positionY);
                    atacando = false;
                    target.setDamage(tirarDados(5, 6)+4);
                    combat.enemyTurn();
                    despla = 0;
                }
                
                break;
        }
        
    }
    
}
