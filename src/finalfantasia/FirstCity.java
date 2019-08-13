/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
class FirstCity extends BackgroundMap implements ThreadStop{
    
    // <editor-fold defaultstate="collapsed" desc="Variables">
    /* Valores especificos del tileset. Los primeros tres tratan de codigos para 
    colision en el suelo, con objetos y en el agua. 
    El ultimo se trata del {@code JFrame} de la ventana.*/
    private static final String[] collisionGround = {"163","208","209","210"};
    private static final String[] collisionSecondLayerObjects = new String[74];
    private static final String[] collisionOnWater = new String[21];
    private final JFrame window = WindowManager.getFrame();
   
    /*
    El primero es el {@code Object} que syncrhoniza para usar el repaint().
    El segundo es la referencia a OpenWorld. Lugar de donse se llama.
    */
    private final Object synchLock;
    private final OpenWorld openWorld;
    
    /*
    Se encarga de controlar la puasa del Thread.
    */
    private boolean stopThreadFlag = false;
    
    /*
    Se encarga de pasuar el Thread mientras esta en combate.
    */
    private boolean pauseFlag = false;
    private Object combatLock;
    // </editor-fold>
    
    /**
     * simplemente inicializa los valores de los codigos para las colisiones.
     */
    static{
        int setOff = 0;
        for (int i = 0; i < 61; i++) {
            
            String code = String.valueOf(12+16*(i/4)+i%4);
            
            if(code.equals("220") || code.equals("236") || code.equals("252")){
                continue;
            }
            
            collisionSecondLayerObjects[i] = code;
            
        }
        
        for (int i = 0; i < 3; i++) {
            collisionSecondLayerObjects[i+61] = String.valueOf(i+137);
        }
        for (int i = 0; i < 2; i++) {
            collisionSecondLayerObjects[i+64] = String.valueOf(i+153);
        }
        for (int i = 0; i < 5; i++) {
            if(i==2){
                setOff=-1;
                continue;
            }
            collisionSecondLayerObjects[i+66+setOff] = String.valueOf(i+164);
        }
        setOff =0;
        for (int i = 0; i < 4; i++) {
            if(i<2)
                collisionSecondLayerObjects[i+70] = String.valueOf(183+i);
            else
                collisionSecondLayerObjects[i+70] = String.valueOf(199+i-2);   
        }
        
        for (int i = 0; i < 3; i++) {
            
            collisionOnWater[i] = String.valueOf(233+i);
            
        }
        
        for (int i = 0; i < 12; i++) {
            collisionOnWater[i+3]=String.valueOf(179+16*(i/4)+i%4);
        }
        
        for (int i = 0; i < 6; i++) {
            if(i==2 || i==3 ){
                setOff-=1;
                continue;
            }
            collisionOnWater[i+15+setOff] = String.valueOf(225+i);
        }
        collisionOnWater[19] = "186";
        collisionOnWater[20] = "202";
        
        
        
    }
    
    /**
     * Inicializa los valores y llama al constructor de {@link BackgroundMap}
     * @param canvasMapa {@code JLayeredPane} en donde se pintara el mapa.
     * @param startTilePosX int que indica la posicion inicial de la camara en el eje X.
     * @param startTilePosY int que indica la posicion inicial de la camara en el eje Y.
     * @param synchLock {@code Object} que sincroniza el repaint.
     * @param openWorld Referencia a {@link OpenWorld} en donde se llama la clase.
     * @throws InterruptedException Si el thread llega a interrumpirse.
     */
    FirstCity(JLayeredPane canvasMapa, int startTilePosX, int startTilePosY, Object synchLock, OpenWorld openWorld, Object combatLock) throws InterruptedException{
        super(canvasMapa, startTilePosX, startTilePosY, Variables.getMAP_INDEX_IMAGES(0), Variables.getMAP_INDEX_IMAGES(1), Variables.getMAP_INDEX_IMAGES(2), Variables.getTILES(), collisionGround, collisionSecondLayerObjects, collisionOnWater);
        this.synchLock = synchLock;
        this.openWorld = openWorld;
        this.combatLock = combatLock;
        
    }  
    
    /**
     * 
     * Llama la funcion {@link FirstCity#tick()} 30 veces por segundo.
     * 
     */
    @Override
    public void run() {
        
                   
        long lastTime = System.nanoTime();
        final double ticks = 30D;
        double ns = 1000000000 / ticks;    
        double delta = 0;

            
        while(!stopThreadFlag){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            
            if(pauseFlag){
                
                synchronized(combatLock){
                    try {
                        combatLock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FirstCity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                pauseFlag = false;
                lastTime = System.nanoTime();
            }
                
            if(delta >= 1 ){
                tick();
                delta--;
            }
        }
        
        
    }

    /**
     * Funcion que se encarga de actualizar la profundidad de los personajes y hacer el llamado a {@code JFrame.repaint()}
     */
    void tick(){
        try{
            updateLayers(openWorld.getCanvasCharacters(), openWorld.getCharacters());
        }catch(ArrayIndexOutOfBoundsException | NullPointerException e){
        }

        synchronized(synchLock){
            synchLock.notifyAll();
        }
        
        
        window.repaint();
        
        
        
        
        
    }


    @Override
    public void stopThread() {
        pauseFlag = true;
    }
    
    
    
    
    
}
