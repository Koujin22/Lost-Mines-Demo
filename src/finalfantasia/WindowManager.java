/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */



public final class WindowManager implements Runnable{ 
    
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
    private static JPanel canvasPadre;
    private static JFrame window;
    private static JLayeredPane canvasMenu;
    
   
    // CAMBIAR METODO DE ACCESO A VARIABLES, METODOS Y CLASES DE ACUERDO A LOGICA.
    // Revisar que todo este relativo en layouts
    
    
    public static void main(String[] args) throws ClassNotFoundException {
        Thread main = new Thread(new WindowManager());
        main.setName("WindowManager");
        main.start();
        
    }

    static JFrame getFrame() {
        return window;
    }
    
    
    public WindowManager(){}
    
    @Override
    public void run() {
        window = crateJFrame();
        
        canvasPadre = new JPanel();
        
        canvasPadre.setDoubleBuffered(true);
        canvasPadre.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
        canvasPadre.setLayout(null);
        window.add(canvasPadre);
        
        crearMenu();
        window.setVisible(true);  
        
    }
    
    
    
    /**
     * Crea la interfase del menu agregando los distintos componentes.
     */
    private void crearMenu(){
        
        
        canvasMenu = new JLayeredPane();
        canvasMenu.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
        
        JLabel bgImage = new JLabel();
        bgImage.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        bgImage.setBackground(Color.CYAN); 
        bgImage.setOpaque(true); 
        canvasMenu.add(bgImage, new Integer(0));
        
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
        
        JLabel startBtn = new JLabel();
        startBtn.setBounds(WINDOW_WIDTH/2-BUTTON_WIDTH/2, WINDOW_HEIGHT/2-
                BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
        startBtn.setBackground(Color.YELLOW);
        startBtn.setOpaque(true);
        startBtn.setText("Nuevo Juego");
        startBtn.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  
                canvasPadre.remove(canvasMenu);
                new Thread(new Juego(canvasPadre)).start();

            }  
        }); 
        canvasMenu.add(startBtn, new Integer(1));
        
        JLabel contBtn = new JLabel();
        contBtn.setBounds(WINDOW_WIDTH/2-BUTTON_WIDTH/2, WINDOW_HEIGHT/2+
                (3*BUTTON_HEIGHT)/4, BUTTON_WIDTH, BUTTON_HEIGHT);
        contBtn.setBackground(Color.YELLOW);
        contBtn.setOpaque(true);
        contBtn.setText("Como jugar");
        contBtn.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  
               
                new PopUp(0);

            }  
        }); 
        canvasMenu.add(contBtn, new Integer(1));
        
        JLabel optionBtn = new JLabel();
        optionBtn.setBounds(WINDOW_WIDTH/2-BUTTON_WIDTH/2, WINDOW_HEIGHT/2+
                2*BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        optionBtn.setBackground(Color.YELLOW);
        optionBtn.setOpaque(true);
        optionBtn.setText("Acerca de");
        optionBtn.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  
               
               new PopUp(1);

            }  
        }); 
        canvasMenu.add(optionBtn, new Integer(1));
        
        
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
        canvasPadre.add(canvasMenu);
        
        
    }
    
    
    /**
     * Crea un <code>JFrame</code>.
     * <p>
     * Realiza la instancia de JFrame agregando los ajustes y caracteristicas:
     * <ul>
     * <li> Las dimensiones (Se usan los valores de <code>Variables</code>.</li>
     * <li> Se agrega el KeyListener <code>KeyManager</code>.</li>
     * <li> Se establece la operacion de cerrado.</li>
     * <li> No se puede cambiar el tamaño y tampoco sale el borde de la ventana.</li>
     * </ul>
     * 
     * @return Regresa la instancia de JFrame.
     */
    private static JFrame crateJFrame(){
        
        JFrame window = new JFrame();
        window.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setUndecorated(true);
        
        
        
                
        return window;
    }

    public static JPanel getCanvasPadre() {
        return canvasPadre;
    }
    
    private class PopUp {
        
        private JPanel canvasPopUp;
        
        PopUp(int type){
            setUpCanvas();
            
            switch(type){
                case 0:
                    setUpControles();
                    break;
                case 1:
                    setUpCreditos();
                    break;
                default:
                    break;
            }
            canvasMenu.add(canvasPopUp, new Integer(5));
        }
        
        private void setUpCanvas(){
            
            canvasPopUp = new JPanel();
            canvasPopUp.setLayout(null);
            canvasPopUp.setBounds(0,0,1920,1080);
            canvasPopUp.setOpaque(false);
            canvasPopUp.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    canvasMenu.remove(canvasPopUp);
                    canvasMenu.repaint();
                }
            
                
                
            });
            
        
        }
        
        
        void setUpControles(){
            
            JLabel lol = new JLabel();
            lol.setBounds(1920/2-200,1080/2-100,400,200);
            lol.setBackground(Color.white);
            lol.setOpaque(true);
            lol.setBorder(BorderFactory.createLineBorder(Color.red, 5));
            lol.setText("<html><p>Controles Mundo Abierto:</p>\n" +
                        "<ul>\n" +
                        "<li>WASD: Moverse</li>\n" +
                        "<li>Enter: Interaccion</li>\n" +
                        "</ul>\n" +
                        "<p>Controles Dialogo:</p>\n" +
                        "<ul>\n" +
                        "<li>Enter, Spacio, Click izquierdo: Siguiente dialogo.</li>\n" +
                        "</ul>\n" +
                        "<p>Controles Combate:</p>\n" +
                        "<ul>\n" +
                        "<li>WASD, Click izquierdo: Seleccion.</li>\n" +
                        "<li>Enter, Click izquierdo: Establecer accion.</li>\n" +
                        "</ul></html>");
            canvasPopUp.add(lol);
            
        }
        
        void setUpCreditos(){
            
            JLabel lol = new JLabel();
            lol.setBounds(1920/2-200,1080/2-100,400,200);
            lol.setBackground(Color.white);
            lol.setOpaque(true);
            lol.setBorder(BorderFactory.createLineBorder(Color.red, 5));
            lol.setHorizontalAlignment(SwingConstants.CENTER);
            lol.setText("<html><p>Es un demo de un videojuego basado en final fantasy.&nbsp;</p>\n" +
                        "<p>Instituto Tecnologico de Estudios Superiores de Monterrey Campus Estado de Mexico</p>\n\n" +
                        "<p>Emiliano Heredia (A01377072) ISC</p>\n" +
                        "<p>Ivan Honc (A01376466) ISC</html>");
            canvasPopUp.add(lol);
        }
        
        
        
    }
    
}
