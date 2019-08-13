    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import javafx.util.Pair;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
abstract class BackgroundMap implements Runnable {
    
    // <editor-fold defaultstate="collapsed" desc="Variables">
    /* The length width of the JFrame  */
    private final static int WINDOW_WIDTH = (int) Variables.getWINDOW_SIZE().
            getWidth();
    /* The length height of the JFrame */
    private final static int WINDOW_HEIGHT=  (int) Variables.getWINDOW_SIZE().
            getHeight();    
    /* The size of the tiles from the tilesets */
    private final static int TILE_SIZE = Variables.getTILE_SIZE();
    /* Number of tiles on the Y axis */
    private final static int numSquareY = WINDOW_HEIGHT/TILE_SIZE+1;    
    /* Number of tiles on the X axis */
    private final static int numSquareX = WINDOW_WIDTH/TILE_SIZE+1;
    
    /* 2D array indicando los tiles correspondientes en layer zero */
    private final int[][] MAP_INDEX_LAYER_ZERO;    
    /* 2D array indicando los tiles correspondientes en layer one */
    private final int[][] MAP_INDEX_LAYER_ONE;    
    /* 2D array indicando los tiles correspondientes en layer two */
    private final int[][] MAP_INDEX_LAYER_TWO;    
    /* Array con ImageIcon conteniendo los tiles del mapa */
    private final ImageIcon[] TILES;
    
    /* 2D array conteniendo los JLabels para la cuadricula del mapa en el layer zero*/
    private final JLabel[][] MAP_LAYER_ZERO_IMAGE;
    /* 2D array conteniendo los JLabels para la cuadricula del mapa en el layer one*/
    private final JLabel[][] MAP_LAYER_ONE_IMAGE;
    /* 2D array conteniendo los JLabels para la cuadricula del mapa en el layer two*/
    private final JLabel[][] MAP_LAYER_TWO_IMAGE;
    
    /* JLabel que contiene los JLabels con las cuadriculas en el layer zero*/
    private final JLabel PLACEHOLDER_LAYER_ZERO;
    /* JLabel que contiene los JLabels con las cuadriculas en el layer one*/
    private final JLabel PLACEHOLDER_LAYER_ONE;
    /* JLabel que contiene los JLabels con las cuadriculas en el layer two*/
    private final JLabel PLACEHOLDER_LAYER_TWO;
    
    /* Contiene los valores que causan colision en el suelo */
    private final String[] collisionGround;
    /* Contiene los valores que causan colision con objetos en la tierra */
    private final String[] collisionSecondLayeObject;
    /* Contiene los valores que permiten caminar por el agua */
    private final String[] collisionOnWater;
    
    /* El canvas que contiene toda la informacion del mapa */
    final JLayeredPane canvasMapa;
    
    /* Valor que indica la posicion en el eje X para la posicion de la camara */
    int startTilePosX;
    /* Valor que indica la posicion en el eje Y para la posicion de la camara */
    int startTilePosY;
    // </editor-fold>
    
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * 
     * Se encarga de inicializar las variables y crear la cuadricula para el mapa
     * 
     * @param canvasMapa Lugar donde se inserta los {@code JComponent} para pintar.
     * @param startTilePosX Posicion inicial de la camara en el eje X.
     * @param startTilePosY Posicion inicial de la camara en el eje Y.
     * @param MAP_INDEX_LAYER_ZERO Indices indicando el tile que se debe pintar en el layer zero.
     * @param MAP_INDEX_LAYER_ONE Indices indicando el tile que se debe pintar en el layer one.
     * @param MAP_INDEX_LAYER_TWO Indices indicando el tile que se debe pintar en el layer two.
     * @param TILES Array con las imagenes del tileset del mapa.
     * @param collisionGround Valores que ocacionan colisiones.
     * @param collisionSecondLayerObject Valores que ocacionan coliciones con objetos.
     * @param collisiOnWater Valores que permiten caminar sobre agua.
     * @throws InterruptedException 
     */
    BackgroundMap(JLayeredPane canvasMapa, int startTilePosX, int startTilePosY, int[][] MAP_INDEX_LAYER_ZERO, int[][] MAP_INDEX_LAYER_ONE, int[][] MAP_INDEX_LAYER_TWO,
            ImageIcon[] TILES, String[] collisionGround, String[] collisionSecondLayerObject, String[] collisiOnWater) throws InterruptedException{
        
        this.MAP_INDEX_LAYER_ZERO = MAP_INDEX_LAYER_ZERO;
        this.MAP_INDEX_LAYER_ONE = MAP_INDEX_LAYER_ONE;
        this.MAP_INDEX_LAYER_TWO = MAP_INDEX_LAYER_TWO;
        this.TILES = TILES; 
        this.canvasMapa = canvasMapa;
        this.startTilePosX = startTilePosX;
        this.startTilePosY = startTilePosY;
        this.collisionGround = collisionGround;
        this.collisionSecondLayeObject = collisionSecondLayerObject;
        this.collisionOnWater = collisiOnWater;
        
        MAP_LAYER_ZERO_IMAGE = new JLabel[numSquareY+1][numSquareX+1];
        MAP_LAYER_ONE_IMAGE = new JLabel[numSquareY+1][numSquareX+1];
        MAP_LAYER_TWO_IMAGE = new JLabel[numSquareY+1][numSquareX+1];
        
        PLACEHOLDER_LAYER_ZERO = new JLabel();
        PLACEHOLDER_LAYER_ONE = new JLabel();
        PLACEHOLDER_LAYER_TWO = new JLabel();
        
        initializeLayersJLabel();
        paintMap();
        canvasMapa.repaint();
        
    }
    
    /**
     *  Inicializa la cuadricula que contendra las imagenes del tileset.
     */
    private void initializeLayersJLabel(){
        
        for (int y = 0; y <= numSquareY; y++) {
            for (int x = 0; x <= numSquareX; x++) {
             
                MAP_LAYER_ZERO_IMAGE[y][x] = new JLabel();
                MAP_LAYER_ZERO_IMAGE[y][x].setBounds(TILE_SIZE*x, TILE_SIZE*y, TILE_SIZE, TILE_SIZE);
                PLACEHOLDER_LAYER_ZERO.add(MAP_LAYER_ZERO_IMAGE[y][x]);
                
                MAP_LAYER_ONE_IMAGE[y][x] = new JLabel();
                MAP_LAYER_ONE_IMAGE[y][x].setBounds(TILE_SIZE*x, TILE_SIZE*y, TILE_SIZE, TILE_SIZE);
                PLACEHOLDER_LAYER_ONE.add(MAP_LAYER_ONE_IMAGE[y][x]);
                
                MAP_LAYER_TWO_IMAGE[y][x] = new JLabel();
                MAP_LAYER_TWO_IMAGE[y][x].setBounds(TILE_SIZE*x, TILE_SIZE*y, TILE_SIZE, TILE_SIZE);
                PLACEHOLDER_LAYER_TWO.add(MAP_LAYER_TWO_IMAGE[y][x]);
                
            }            
        }
        canvasMapa.add(PLACEHOLDER_LAYER_ZERO, new Integer(0));
        canvasMapa.add(PLACEHOLDER_LAYER_ONE, new Integer(1));
        canvasMapa.add(PLACEHOLDER_LAYER_TWO, new Integer(2));
        
    }
    
    /**
     * 
     * Regresa el {@code ImageIcon} que representa por el codigo dado.
     * 
     * @param code valor que representa un tile dentro del tileset.
     * @return ImageIcon con el subimage que representa el codigo.
     */
    private ImageIcon getTileImage(int code) {
        return TILES[code];
    }
    
    /**
     * 
     *  Inicia 3 {@code Thread} de {@code PaintLayer}. Cada una se encarga de cambiar
     *  las imagenes de los {@code JLabel} con los nuevos valores de cada layer.
     * 
     * @throws InterruptedException 
     */
    private void paintMap() throws InterruptedException{
        CountDownLatch painting = new CountDownLatch(3);
        
        new Thread(new PaintLayer(startTilePosX, startTilePosY, PLACEHOLDER_LAYER_ZERO, MAP_INDEX_LAYER_ZERO, MAP_LAYER_ZERO_IMAGE, painting)).start();
        new Thread(new PaintLayer(startTilePosX, startTilePosY, PLACEHOLDER_LAYER_ONE, MAP_INDEX_LAYER_ONE, MAP_LAYER_ONE_IMAGE, painting)).start();
        new Thread(new PaintLayer(startTilePosX, startTilePosY, PLACEHOLDER_LAYER_TWO, MAP_INDEX_LAYER_TWO, MAP_LAYER_TWO_IMAGE, painting)).start();
        
        painting.await();
        
    }
    
    /**
     * Revisa que los characters esten en la posicion z (profundiad) en base a la
     * posicion en el eje Y.
     * 
     * @param canvasCharacters El {@code JLayeredPane} que contiene todos los characters/
     * @param characters Un pair que contiene los NPCs y el jugador.
     */
    final void updateLayers(JLayeredPane canvasCharacters, Pair<ArrayList<NPC>, PlayerWorld> characters){
        Collections.sort(characters.getKey());
        
        if(!characters.getKey().isEmpty()){
        
            int offset = 0;

            int characterYPos = characters.getValue().getHitbox().getY();
            if(characters.getValue().getHitbox().getY()<characters.getKey().get(0).getHitbox().getY()){
                canvasCharacters.setLayer(characters.getValue().getPlayerJLabel(), 0);
                canvasCharacters.setLayer(characters.getKey().get(0).getNpcJLabel(), 1);
                offset = 1;
            }else{
                canvasCharacters.setLayer(characters.getKey().get(0).getNpcJLabel(), 0);
            }

            if(offset==1){
                for (int i = 2; i <= characters.getKey().size(); i++) {
                    canvasCharacters.setLayer(characters.getKey().get(i-1).getNpcJLabel(), i);
                }
            }else{
                for (int i = 1; i < characters.getKey().size(); i++) {
                    if(characters.getKey().get(i-1).getHitbox().getY()<characterYPos && characterYPos<characters.getKey().get(i).getHitbox().getY()){
                        canvasCharacters.setLayer(characters.getValue().getPlayerJLabel(), i);
                        canvasCharacters.setLayer(characters.getKey().get(i).getNpcJLabel(), i+1);
                        offset++;
                    }else{
                        canvasCharacters.setLayer(characters.getKey().get(i).getNpcJLabel(), i+offset);
                    }


                }
            }

            if(offset==0){
                canvasCharacters.setLayer(characters.getValue().getPlayerJLabel(), characters.getKey().size());
            }
        }
        
    }
    
    /**
     * 
     * Dado la siguiente posicion de la camara, determina si es posible la futura
     * posicion. Despues si es posible pinta el mapa con los nuevos valores.
     * 
     * @param nextTilePosX El cambio en terminos de 1/4 de tile (8) en el eje X.
     * @param nextTilePosY El cambio en terminos de 1/4 de tile (8) en el eje Y.
     * @return Si es posible mover la camara a la posicion dada.
     * @throws outOfBoundriesException Regresa una excepcion si es que la camara
     * terminaria fuera de rango.
     * @throws InterruptedException 
     */
    final boolean update(int nextTilePosX, int nextTilePosY) throws outOfBoundriesException, InterruptedException{
        startTilePosX+= nextTilePosX;
        startTilePosY+= nextTilePosY;
        if(!(startTilePosX < 0 || startTilePosY < 0 || startTilePosX > 38*4 || startTilePosY > 65*4)){
            paintMap();
            return true;
        }else{
            startTilePosX -= nextTilePosX;
            startTilePosY -= nextTilePosY;
            return false;
        }
    }

    /**
     * Regresa la posicion de la camara en el eje X.
     * 
     * @return Posicion de la camara en el eje X en terminos de numero de tiles.
     */
    final int getStartTilePosX() {
        return startTilePosX;
    }

    /**
     * Regresa la posicion de la camara en el eje Y.
     * 
     * @return Posicion de la camara en el eje Y en terminos de numero de tiles.
     */
    final int getStartTilePosY() {
        return startTilePosY;
    }

    /**
     * Determina si la posicion del {@code Rectangle} dado colisionaria con algun
     * terreno u objeto del mapa.
     * 
     * @param player {@code Rectangle} a verificar la colision.
     * @param dir direccion en la que se dirije el {@code Rectangle}
     * @return 
     */
    final boolean collision(Rectangle player, int dir) {
        int xDispl = startTilePosX%4*8;
        int yDispl = startTilePosY%4*8;
        String nextTileZero;
        String nextTileOne;
        
        switch(dir){
            
            case 1:
                nextTileZero = PLACEHOLDER_LAYER_ZERO.getComponentAt(new Point((int)player.getBounds().getCenterX()+xDispl+10,(int) player.getBounds().getCenterY()+30+yDispl)).getName();
                nextTileOne = PLACEHOLDER_LAYER_ONE.getComponentAt(new Point((int)player.getBounds().getCenterX()+xDispl+10,(int) player.getBounds().getCenterY()+30+yDispl)).getName();
                break;
            case -1:
                nextTileZero = PLACEHOLDER_LAYER_ZERO.getComponentAt(new Point((int)player.getBounds().getCenterX()+xDispl-10,(int) player.getBounds().getCenterY()+30+yDispl)).getName();
                nextTileOne = PLACEHOLDER_LAYER_ONE.getComponentAt(new Point((int)player.getBounds().getCenterX()+xDispl-10,(int) player.getBounds().getCenterY()+30+yDispl)).getName();
                break;
            default:
                nextTileZero = PLACEHOLDER_LAYER_ZERO.getComponentAt(new Point((int)player.getBounds().getCenterX()+xDispl,(int) player.getBounds().getCenterY()+30+yDispl)).getName();
                nextTileOne = PLACEHOLDER_LAYER_ONE.getComponentAt(new Point((int)player.getBounds().getCenterX()+xDispl,(int) player.getBounds().getCenterY()+30+yDispl)).getName();
                break;
                
        }
        
        
        
        
        if(Arrays.asList(collisionSecondLayeObject).contains(nextTileOne)){
            return true;
        }
        else if(Arrays.asList(collisionGround).contains(nextTileZero)&&!Arrays.asList(collisionOnWater).contains(nextTileOne)){
            return true;
        }
        
        
        
        return false;
    }
    
    /**
     * Clase que se encarga de actualizar las imagenes de los {@code JLabel} 
     * con respecto al cambio de posicion de la camara.
     */
    private class PaintLayer implements Runnable{
        
        /* posicion de la camara en el eje X.*/
        private int tilePosX;
        /* posicion de la camara en el eje Y.*/
        private int tilePosY;
        /* {@code JLabel} que se encarga de controlar la cuadricula de cada capa. */
        private final JLabel placeHolder;
        /* 2D array que contiene los valores del tile map. */
        private final int[][] mapIndex;
        /* 2D array conteniendo {@code JLabel} que pintan la cuadricula. */
        private final JLabel[][] imageLabel;
        /* CountDownLatch encargado de synchronizar threads. */
        private final CountDownLatch painting;

        /**
         * 
         * Constructor encargado de establecer valores
         * 
         * @param tilePosX {@link PaintLayer#tilePosX}
         * @param tilePosY {@link PaintLayer#tilePosy}
         * @param placeHolder {@link PaintLayer#placeHolder}
         * @param mapIndex {@link PaintLayer#mapIndex}
         * @param imageLabel {@link PaintLayer#imageLabel}
         */
        public PaintLayer(int tilePosX, int tilePosY, JLabel placeHolder, int[][] mapIndex, JLabel[][] imageLabel, CountDownLatch painting) {
            this.tilePosX = tilePosX;
            this.tilePosY = tilePosY;
            this.placeHolder = placeHolder;
            this.mapIndex = mapIndex;
            this.imageLabel = imageLabel;
            this.painting = painting;
        }
        
        
        /**
         * Se encarga de actualizar a la cuadricula y sus imagenes.
         */
        @Override
        public void run() {
            
            int xDispl = tilePosX%4;
            int yDispl = tilePosY%4;

            tilePosX/=4;
            tilePosY/=4;

            placeHolder.setBounds(0-(TILE_SIZE/4)*xDispl,0-(TILE_SIZE/4)*yDispl,WINDOW_WIDTH+32, WINDOW_HEIGHT+32);
            for (int y = 0; y <= numSquareY; y++) { 
                for (int x = 0; x <= numSquareX; x++) {
                    int code = mapIndex[tilePosY+y][tilePosX+x];
                    if(code == -1)
                        imageLabel[y][x].setIcon(null);
                    else
                        imageLabel[y][x].setIcon(getTileImage(code));
                    imageLabel[y][x].setName(String.valueOf(code));
                }
            }
            
            painting.countDown();
        }
        
    }
    
    
    
}
