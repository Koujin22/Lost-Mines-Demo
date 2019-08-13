/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;


import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;


/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
final class Variables{
    
    private Variables(){}
    
    private final static Dimension WINDOW_SIZE = new Dimension(1920,1080);
    private final static Dimension LOGO_SIZE = new Dimension(480,240);
    private final static Dimension BUTTON_SIZE = new Dimension(240, 60);
    private final static String PATH = "src/finalfantasia/Resources/";
    private final static ArrayList<int[][]> MAP_INDEX_INDEX = new ArrayList<>();
    private final static int TILE_SIZE = 32;
    
    
    private static ImageIcon[] R, L, U, D, ATK;   
    private static BufferedImage MAP_SRC;
    private static BufferedImage[] PLAYER_SKIN_SRC;
    private static BufferedImage[] SKELETON_SKIN_SRC;
    private static BufferedImage MILITA_WARRIOR_SKIN_SRC;
    private static ImageIcon[] MILITIA_SKIN, SKELETON_SKINS, SKELETON_ATK_SKINS;
    private static BufferedImage COMBAT_BACKGROUND_SRC, LOGO_SRC;
    private static ImageIcon COMBAT_BACKGROUND, LOGO;
  
    private final static ImageIcon[] TILES = new ImageIcon[1024];
    
    static{
               
        try {
            setUpSources();
        } catch (IOException ex) {
            Logger.getLogger(Variables.class.getName()).log(Level.SEVERE, null, ex);
        }
        setUpPlayerSkins();
        setUpMapIndex();
        setUpTiles();
        setUpMilitiaSkins();
        setUpEnemySkins();
        
        
         
    }
  
    
    private static void setUpEnemySkins(){
        
        SKELETON_SKINS = new ImageIcon[]{new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(0, 0, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(192, 0, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(384, 0, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(576, 0, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(768, 0, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(384, 192, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(576, 192, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(768, 192, 192, 192)),
                            
                            
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(0, 384, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(192, 384, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(384, 384, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(576, 384, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(768, 384, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(0, 576, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(192, 576, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(384, 576, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(576, 576, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(768, 576, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(576, 768, 192, 192)),
                            new ImageIcon(SKELETON_SKIN_SRC[0].getSubimage(768, 768, 192, 192))};  
        
        SKELETON_ATK_SKINS = new ImageIcon[]{
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(592, 0, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(296, 0, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(0, 0, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(592, 216, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(296, 216, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(0, 216, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(592, 432, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(296, 432, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(0, 432, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(592, 648, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(296, 648, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(0, 648, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(592, 864, 296, 216)),
                            new ImageIcon(SKELETON_SKIN_SRC[1].getSubimage(296, 864, 296, 216))};
        
        for (int i = 0; i < SKELETON_SKINS.length; i++) {
            
            SKELETON_SKINS[i] = new ImageIcon(SKELETON_SKINS[i].getImage().getScaledInstance(96, 96, Image.SCALE_DEFAULT));
            
        }
    }
    
    private static void setUpMilitiaSkins(){
        
        MILITIA_SKIN = new ImageIcon[]{ new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(0, 0, 36, 36)), //idle
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(36, 0, 36, 36)),
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(72, 0, 36, 36)),
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(108, 0, 36, 36)),
                                        
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(0, 36, 36, 36)), //Walking
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(36, 36, 36, 36)),
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(72, 36, 36, 36)),
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(108, 36, 36, 36)),
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(144, 36, 36, 36)),
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(180, 36, 36, 36)),
                                        
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(0, 72, 36, 36)), //atack
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(36, 72, 36, 36)),
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(72, 72, 36, 36)),
                                        new ImageIcon(MILITA_WARRIOR_SKIN_SRC.getSubimage(108, 72, 36, 36))};
        
        for (int i = 0; i < MILITIA_SKIN.length; i++) {
            MILITIA_SKIN[i] = new ImageIcon(MILITIA_SKIN[i].getImage().getScaledInstance(96, 96, Image.SCALE_DEFAULT));
            
        }
        
    }    
    
    private static void setUpPlayerSkins(){
        
        R = new ImageIcon[]{new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(192, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(224, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(256, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(288, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(192, 32, 32, 32)),

                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(224, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(256, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(288, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(192, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(224, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[0].getSubimage(256, 64, 32, 32))};
        
        L = new ImageIcon[]{new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(192, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(224, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(256, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(288, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(192, 32, 32, 32)),

                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(224, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(256, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(288, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(192, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(224, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(256, 64, 32, 32))};
        
        U = new ImageIcon[]{new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(192, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(224, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(256, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(288, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(194, 32, 32, 32)),

                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(226, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(258, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(290, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(192, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(224, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[2].getSubimage(256, 64, 32, 32))};
        
        D = new ImageIcon[]{new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(192, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(224, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(256, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(288, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(192, 32, 32, 32)),

                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(224, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(256, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(288, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(192, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(224, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[3].getSubimage(256, 64, 32, 32))};
        
        ATK = new ImageIcon[]{new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(0, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(32, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(64, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(96, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(128, 0, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(160, 0, 32, 32)),
                            
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(0, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(32, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(64, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(96, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(128, 32, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(160, 32, 32, 32)),

                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(0, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(32, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(64, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(96, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(128, 64, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(160, 64, 32, 32)),
                            
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(0, 96, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(32, 96, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(64, 96, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(96, 96, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(128, 96, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(160, 96, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(192, 96, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(224, 96, 32, 32)),

                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(0, 128, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(32, 128, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(64, 128, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(96, 128, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(128, 128, 32, 32)),
                            new ImageIcon(PLAYER_SKIN_SRC[1].getSubimage(160, 128, 32, 32))};
         
        for (int i = 0; i < R.length; i++) {
            R[i] = new ImageIcon(R[i].getImage().getScaledInstance(96,96, Image.SCALE_DEFAULT));
            L[i] = new ImageIcon(L[i].getImage().getScaledInstance(96,96, Image.SCALE_DEFAULT)); 
            U[i] = new ImageIcon(U[i].getImage().getScaledInstance(96,96, Image.SCALE_DEFAULT)); 
            D[i] = new ImageIcon(D[i].getImage().getScaledInstance(96,96, Image.SCALE_DEFAULT));
        } 
        for (int i = 0; i < ATK.length; i++) {
            ATK[i] = new ImageIcon(ATK[i].getImage().getScaledInstance(96,96, Image.SCALE_DEFAULT));
        }
        
    }
    
    private static void setUpSources() throws IOException{
        MAP_SRC = ImageIO.read(new File(PATH+"map1.png"));
        PLAYER_SKIN_SRC = new BufferedImage[]{ImageIO.read(new File(PATH+"R.png")),
                                            ImageIO.read(new File(PATH+"L.png")),
                                            ImageIO.read(new File(PATH+"U.png")),
                                            ImageIO.read(new File(PATH+"D.png"))};
        MILITA_WARRIOR_SKIN_SRC = ImageIO.read(new File(PATH+"militaWarrior.png"));
        SKELETON_SKIN_SRC = new BufferedImage[]{ImageIO.read(new File(PATH+"SIR.png")),
                                            ImageIO.read(new File(PATH+"SR_Atk.png"))};
        
        COMBAT_BACKGROUND_SRC = ImageIO.read(new File(PATH+"combatBackground.jpg"));
        COMBAT_BACKGROUND = new ImageIcon(new ImageIcon(COMBAT_BACKGROUND_SRC).getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT));
        LOGO_SRC = ImageIO.read(new File(PATH+"Logo.png"));
        LOGO = new ImageIcon(new ImageIcon(LOGO_SRC).getImage().getScaledInstance(LOGO_SIZE.width, LOGO_SIZE.height, Image.SCALE_DEFAULT));
    }
    
    private static void setUpMapIndex(){
        MAP_INDEX_INDEX.add(setMapIndex("StarterCity_Tile_Layer_1.csv"));
        MAP_INDEX_INDEX.add(setMapIndex("StarterCity_Tile_Layer_2.csv"));
        MAP_INDEX_INDEX.add(setMapIndex("StarterCity_Tile_Layer_3.csv"));
    }
    
    private static void setUpTiles(){
        for (int i = 0; i < 256; i++) {
            TILES[i] = new ImageIcon(MAP_SRC.getSubimage(TILE_SIZE*(i%16), TILE_SIZE*(i/16), TILE_SIZE, TILE_SIZE));
        }
    }

    public static ImageIcon[] getMILITIA_SKIN() {
        return MILITIA_SKIN;
    }

    public static BufferedImage getMILITA_WARRIOR_SKIN_SRC() {
        return MILITA_WARRIOR_SKIN_SRC;
    }

    public static ImageIcon[] getR() {
        return R;
    }

    public static ImageIcon[] getL() {
        return L;
    }

    public static ImageIcon[] getU() {
        return U;
    }

    public static ImageIcon[] getD() {
        return D;
    }

    public static BufferedImage[] getPLAYER_SKIN_SRC() {
        return PLAYER_SKIN_SRC;
    }

    public static ArrayList<int[][]> getMAP_INDEX_INDEX() {
        return MAP_INDEX_INDEX;
    }

    public static ImageIcon[] getTILES() {
        return TILES;
    }

    public static int getTILE_SIZE() {
        return TILE_SIZE;
    }
    
    private static int[][] setMapIndex(String name){
        List<String[]> rowList = new ArrayList<String[]>();
        try (BufferedReader br = new BufferedReader(new FileReader(PATH+name))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                rowList.add(lineItems);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Variables.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Variables.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int[][] mapIndex = new int[rowList.size()][];
        for (int i = 0; i < rowList.size(); i++) {
            String[] row = rowList.get(i);
            mapIndex[i] = StringArrToIntArr(row);
        }
        return mapIndex;
    }
    

    static int[][] getMAP_INDEX_IMAGES(int index) {
        return MAP_INDEX_INDEX.get(index);
    }
    
    static int[] StringArrToIntArr(String[] s) {
        int[] result = new int[s.length];
        for (int i = 0; i < s.length; i++) {
           result[i] = Integer.parseInt(s[i]);
        }
        return result;
    }

    static String getPATH() {
        return PATH;
    }

    static BufferedImage getMAP_SRC() {
        return MAP_SRC;
    }

    static Dimension getLOGO_SIZE() {
        return LOGO_SIZE;
    }

    static Dimension getWINDOW_SIZE() {
        return WINDOW_SIZE;
    }

    static Dimension getBUTTON_SIZE() {
        return BUTTON_SIZE;
    }

    public static BufferedImage[] getSKELETON_SKIN_SRC() {
        return SKELETON_SKIN_SRC;
    }

    public static ImageIcon[] getSKELETON_SKINS() {
        return SKELETON_SKINS;
    }

    public static BufferedImage getCOMBAT_BACKGROUND_SRC() {
        return COMBAT_BACKGROUND_SRC;
    }

    public static ImageIcon getCOMBAT_BACKGROUND() {
        return COMBAT_BACKGROUND;
    }

    public static ImageIcon[] getATK() {
        return ATK;
    }

    public static ImageIcon[] getSKELETON_ATK_SKINS() {
        return SKELETON_ATK_SKINS;
    }

    public static BufferedImage getLOGO_SRC() {
        return LOGO_SRC;
    }

    public static ImageIcon getLOGO() {
        return LOGO;
    }

    
    
    
    
}

