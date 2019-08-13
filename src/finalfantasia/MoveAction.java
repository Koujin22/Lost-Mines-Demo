/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
class MoveAction extends AbstractAction{
        
        int key;
        boolean pressed;
        private static boolean forward=false, left=false, back=false, right=false, shift=false, pauseFlag=false;
        static Combat combate;
        static Object pauseLock ;
        static PlayerWorld player;
        
        private static int debugg = 0;
        
        MoveAction(int key, boolean pressed){
            this.key = key;
            this.pressed = pressed;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(key){
                
                case 0:
                    forward = pressed;
                    break;
                case 1:
                    left = pressed;
                    break;
                case 2:
                    back = pressed;
                    break;
                case 3:   
                    right = pressed;
                    break;
                case 4:
                    shift = pressed;
                    break;
                case 5:
                    synchronized(pauseLock){
                        pauseLock.notifyAll();
                    }
                    pauseFlag = !pauseFlag;
                    break;
                case 6:
                    forward = true;
                    shift = true;
                    break;
                case 7:
                    left = true;
                    shift = true;
                    break;
                case 8:
                    back = true;
                    shift = true;
                    break;
                case 9:
                    right = true;
                    shift = true;
                    break;
                case 10:
                    player.interaction();
                    break;
                case 11:
                    combate.moveUpSelection();
                    break;
                case 12:
                    combate.moveLeftSelection();
                    break;
                case 13:
                    combate.moveDownSelection();
                    break;
                case 14:
                    combate.moveRightSelection();
                    break;
                case 15:
                    combate.action();
                    break;
                    
                    
            }
        }

        
    static void resetValues(){
        forward = false;
        left = false;
        back = false;
        right = false;
        shift = false;
        
    }
        
    static boolean isForward() {
        return forward;
    }

    static boolean isLeft() {
        return left;
    }

    static boolean isBack() {
        return back;
    }

    static boolean isRight() {
        return right;
    }

    static boolean isShift() {
        return shift;
    }

    synchronized static boolean isPauseFlag() {
        return pauseFlag;
    }
        
    static void setPauseLock(Object pauseLock){
        MoveAction.pauseLock = pauseLock;
    }
    
    synchronized public static void setPauseFlagFalse(){
        pauseFlag = false;
    }
        
    static void setPlayerWorld(PlayerWorld player){
        MoveAction.player = player;
    }
    
    static void setCombat(Combat combate){
        MoveAction.combate = combate;
    }
    
    }