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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
final class Introduccion extends DialogoLayout implements Runnable {
    
    // <editor-fold defaultstate="collapsed" desc="Variables">
    /* The length width of the JFrame  */
    private final static int WINDOW_WIDTH = (int) Variables.getWINDOW_SIZE().
            getWidth();
    /* The length height of the JFrame */
    private final static int WINDOW_HEIGHT=  (int) Variables.getWINDOW_SIZE().
            getHeight();  
    
    /* CountDownLatch que sincroniza el {@code Thread} de {@link Juego} con este para indicar que se acabo este {@code Thread}. */
    CountDownLatch threadLock;
    // </editor-fold>
    
    /**
     * Inicializa valores
     * 
     * @param threadLock {@link Introduccion#threadLock}
     * @param canvasOpenWorld {@code JLayeredPane} a donde se agregara la introduccion.
     */
    Introduccion(CountDownLatch threadLock, JLayeredPane canvasOpenWorld){
        super(canvasOpenWorld);
        this.threadLock = threadLock;
        
        setBackgroundColor(Color.BLACK);
    }

    /**
     * Corre la introduccion.
     */
    @Override
    public void run() {
        showDialogoComp();
        JLabel name = setName("Game");
        JLabel text = setText("Introduce tu nombre");
        
        String plyName;
        
        synchronized(continueLock){
            try {
                continueLock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        do{
            removeText( text, name);


            plyName = askName();

            name = setName("Game");
            text = setText(plyName+". El nombre esta correcto?");
            confirmationName();
            
            
            
        }while(!nameConf);
        
        removeText(text);
        text = setText("Perfect, aqui agregas intro o historia o alguna mamada");
        
        removeComp();
        
        playerName = plyName;
        
        threadLock.countDown();
        
    }    
}
