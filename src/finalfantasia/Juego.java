/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
final class Juego implements Runnable{
    
    /* {@code JLayeredPane} a donde se agregara la introduccion y despues el {@link OpenWorld} */
    static JLayeredPane canvasOpenWorld;
    /* The length width of the JFrame  */
    private final static int WINDOW_WIDTH = (int) Variables.getWINDOW_SIZE().
            getWidth();
    /* The length height of the JFrame */
    private final static int WINDOW_HEIGHT=  (int) Variables.getWINDOW_SIZE().
            getHeight();  
    

    /**
     * Inizialisa los valores
     * @param canvasPadre {@link Juego#canvasPadre}
     */
    public Juego(JPanel canvasPadre) {
        canvasOpenWorld = new JLayeredPane();
        
        canvasOpenWorld.setDoubleBuffered(true);
        canvasOpenWorld.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
        
        canvasPadre.add(canvasOpenWorld);
    }
    
    /**
     * Corre la {@code Introduccion} para despues correr {@code OpenWorld}
     */
    @Override
    public void run() {
        CountDownLatch lock = new CountDownLatch(1);
        
        new Thread(new Introduccion(lock, canvasOpenWorld)).start();
        
        try {
            lock.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new Thread(new OpenWorld(canvasOpenWorld)).start();        
        
    }
    /**
     * Regresa el canvasOpenWorld.
     * @return 
     */
    static JLayeredPane getCanvasOpenWorld(){
        return canvasOpenWorld;
    }
   
    
    
}
