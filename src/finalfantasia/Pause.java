/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CountDownLatch;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public class Pause implements Runnable {

    
    private final static int WINDOW_WIDTH = (int) Variables.getWINDOW_SIZE().
            getWidth();
    private final static int WINDOW_HEIGHT=  (int) Variables.getWINDOW_SIZE().
            getHeight();
    private final static int LOGO_WIDTH = (int) Variables.getLOGO_SIZE().
            getWidth();
    private final static int LOGO_HEIGHT = (int) Variables.getLOGO_SIZE().
            getHeight();
    private final static int BUTTON_WIDTH = (int) Variables.getBUTTON_SIZE().
            getWidth();
    private final static int BUTTON_HEIGHT = (int) Variables.getBUTTON_SIZE().
            getHeight();
    
    private final Object pause;
    private final JLayeredPane canvasTemp;
   
    private JLayeredPane canvasMenu;
    
    public Pause(JLayeredPane canvasTemp, Object pause) {
        this.pause = pause;
        this.canvasTemp = canvasTemp;
        crearMenu();
    }

    private void crearMenu(){
        
        
        canvasMenu = new JLayeredPane();
        canvasMenu.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
        
        JLabel logoImg = new JLabel();
        logoImg.setBounds(WINDOW_WIDTH/2-LOGO_WIDTH/2, WINDOW_HEIGHT/8, 
                LOGO_WIDTH, LOGO_HEIGHT);
        
        logoImg.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
        logoImg.setIcon(Variables.getLOGO());
//        logoImg.setBackground(Color.DARK_GRAY);
//        logoImg.setOpaque(true);
//        logoImg.setForeground(Color.WHITE);
//        logoImg.setText("Logo");
        canvasMenu.add(logoImg, new Integer(1));
        
        
        JLabel contBtn = new JLabel();
        contBtn.setBounds(WINDOW_WIDTH/2-BUTTON_WIDTH/2, WINDOW_HEIGHT/2-
                BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
        contBtn.setBackground(Color.YELLOW);
        contBtn.setOpaque(true);
        contBtn.setText("Continuar");
        contBtn.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  
                synchronized(pause){
                    pause.notifyAll();
                }
                MoveAction.setPauseFlagFalse();

            }  
        }); 
        canvasMenu.add(contBtn, new Integer(1));
        
        JLabel saveBtn = new JLabel();
        saveBtn.setBounds(WINDOW_WIDTH/2-BUTTON_WIDTH/2, WINDOW_HEIGHT/2+
                (3*BUTTON_HEIGHT)/4, BUTTON_WIDTH, BUTTON_HEIGHT);
        saveBtn.setBackground(Color.YELLOW);
        saveBtn.setOpaque(true);
        saveBtn.setText("Guardar");
        saveBtn.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  
               
               

            }  
        }); 
        canvasMenu.add(saveBtn, new Integer(1));
        
        
        JLabel optBtn = new JLabel();
        optBtn.setBounds(WINDOW_WIDTH/2-BUTTON_WIDTH/2, WINDOW_HEIGHT/2+
                2*BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        optBtn.setBackground(Color.YELLOW);
        optBtn.setOpaque(true);
        optBtn.setText("Opciones");
        optBtn.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  
               

            }  
        }); 
        canvasMenu.add(optBtn, new Integer(1));
        
        
        JLabel exitBtn = new JLabel();
        exitBtn.setBounds(WINDOW_WIDTH/2-BUTTON_WIDTH/2, WINDOW_HEIGHT/2+
                (13*BUTTON_HEIGHT)/4, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitBtn.setBackground(Color.YELLOW);
        exitBtn.setOpaque(true);
        exitBtn.setText("Salir");
        exitBtn.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  
               
                System.exit(0);

            }  
        }); 
        canvasMenu.add(exitBtn, new Integer(1));
        
    }
    
    
    void showMenu(){
        canvasTemp.add(canvasMenu, new Integer(3));
        canvasMenu.requestFocus();
    }
    
    void hideMenu(){
        canvasTemp.remove(canvasMenu);
        canvasTemp.repaint();
    }
    

    @Override
    public void run() {
        
    }
    
    
    
    
}
