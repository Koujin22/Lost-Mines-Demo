/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
abstract class DialogoLayout {
    
    // <editor-fold defaultstate="collapsed" desc="Variables">
    /* The length width of the JFrame  */
    private final static int WINDOW_WIDTH = (int) Variables.getWINDOW_SIZE().
            getWidth();
    /* The length height of the JFrame */
    private final static int WINDOW_HEIGHT=  (int) Variables.getWINDOW_SIZE().
            getHeight();  
    /* Continue instance that calls for next dialog */
    private final Continue continueAction;
    /* JLabel que hace de background para el texto del dialogo */
    private JLabel txtBGPanel;
    /* JLabel que hace de background para el nombre del dialogo */
    private JLabel nameBGPanel;
    /* Valor que indica si acepta la quest o no */
    private boolean returnValue = false;
    /* Confirmacion del nombre insertado es correcto */
    boolean nameConf = true;
    
    /* The name of the player */
    static String playerName;
    
    /* Canvas that contains the {@code JComponent} of the dialog */
    private final JLayeredPane canvasDialogo;
    /* Object that synchronize the threads to continue to the next dialog */
    final Object continueLock;
    /* Canvas que contiene los diferentes canvas para las diferentes funciones */
    final JLayeredPane canvasTemp;
    // </editor-fold>
    
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Inicializa los valores y crea el canvas.
     * 
     * @param canvasTemp {@code JLayeredPane} a donde se insertara el dialogo
     */
    DialogoLayout(JLayeredPane canvasTemp){
        this.continueAction = new Continue();
        this.canvasDialogo = setUpLayout(canvasTemp);
        this.canvasTemp = canvasTemp;
        continueLock = new Object();
        hideDialogoComp();
    }
    
    /**
     * Crea un layout para que salga dialogo
     * 
     * @param canvasPadre Canvas donde aparecera el canvas del dialogo
     */
    private JLayeredPane setUpLayout(JLayeredPane canvasPadre){
        
        JLayeredPane canvasDialogo = new JLayeredPane();
        canvasDialogo.setBounds(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);
        canvasDialogo.addMouseListener(new MouseAdapter(){  
            public void mouseClicked(MouseEvent e)  
            {  
               
                synchronized(continueLock){
                    continueLock.notify();
                }

            }  
        });
        
        setNamePanel(canvasDialogo);
        setTextPanel(canvasDialogo); 
        
        
        
        canvasPadre.add(canvasDialogo, new Integer(2));
        
        
        canvasDialogo.getActionMap().put("Continue", continueAction);
        
        return canvasDialogo;

    }
    
    /**
     * Establece el {@code JLabel} que contiene el background para el nombre
     * 
     * @param canvasDialogo El {@code JLayeredPane} en donde se agregara.
     */
    private void setNamePanel(JLayeredPane canvasDialogo){
        
        JLabel namePanel = new JLabel();
        namePanel.setBounds(WINDOW_WIDTH/8, (3*WINDOW_HEIGHT)/4-WINDOW_HEIGHT/13
                , WINDOW_WIDTH/6, WINDOW_HEIGHT/15);
        namePanel.setBackground(Color.WHITE);
        namePanel.setOpaque(true);
        
        nameBGPanel = namePanel;
        
        canvasDialogo.add(namePanel, new Integer(1));
        
    }
    
    /**
     * Establece el {@code JLabel} que contiene el background para el texto.
     * 
     * @param canvasDialogo El {@code JLayeredPane} en donde se agregara.
     */
    private void setTextPanel(JLayeredPane canvasDialogo){
        
        JLabel bgPanelTxt =  new JLabel();
        bgPanelTxt.setBounds(WINDOW_WIDTH/8, (3*WINDOW_HEIGHT)/4, 
                (3*WINDOW_WIDTH)/4,WINDOW_HEIGHT/4);
        bgPanelTxt.setBackground(Color.WHITE);
        bgPanelTxt.setOpaque(true);
        
        txtBGPanel = bgPanelTxt;
        
        canvasDialogo.add(bgPanelTxt, new Integer(1));
        
    }    
    
    /**
     * Agrega los keybinds para continuar al siguiente dialog.
     * 
     * @param canvasDialogo El {@code JLayeredPane} en donde se agregara.
     */
    private void setContinueBinds(JLayeredPane canvasDialogo){
        canvasDialogo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "Continue");
        canvasDialogo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "Continue");
    }
    
    /**
     * Quita los keybinds para continuar al siguiente dialogo.
     * 
     * @param canvasDialogo El {@code JLayeredPane} en donde se agregara.
     */
    private void removeContinueBinds(JLayeredPane canvasDialogo){
        canvasDialogo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke("ENTER"));
        canvasDialogo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).remove(KeyStroke.getKeyStroke("SPACE"));
    }
    
    /**
     * Quita el {@link DialogoLayout#canvasDialogo} del {@link DialogoLayout#canvasTemp}
     */
    final void removeComp(){
        
        canvasTemp.remove(canvasDialogo);
        canvasTemp.repaint();
        
    }
    
    /**
     * Esconde la UI y libera los keybinds para otras funciones.
     */
    final void hideDialogoComp(){
        canvasDialogo.remove(txtBGPanel);
        canvasDialogo.remove(nameBGPanel);
        canvasDialogo.repaint();
        removeContinueBinds(canvasDialogo);
    }
    
    /**
     * Esconde la UI pero no libera los keybinds para otras funciones.
     */
    final void hideDialogoComp2(){
        canvasDialogo.remove(txtBGPanel);
        canvasDialogo.remove(nameBGPanel);
        canvasDialogo.repaint();
    }
    
    /**
     * Muestra la UI agregando los keybinds para continuar usando {@link DialogoLayout#showDialogoComp() }
     */
    final void showDialogoComp(){
        setContinueBinds(canvasDialogo);
        canvasDialogo.requestFocus();
        canvasDialogo.add(txtBGPanel, new Integer(1));
        canvasDialogo.add(nameBGPanel, new Integer(1));
        canvasDialogo.repaint();
    }
    
    /**
     * regresa el {@code JLayeredPane} de {@link DialogoLayout#canvasDialogo}.
     * 
     * @return El {@code JLayeredPane} de {@link DialogoLayout#canvasDialogo}.
     */
    final JLayeredPane getCanvasDialogo(){
        return canvasDialogo;
    }
    
    /**
     * Regresa el {@code Object} de {@link DialogoLayout#continueLock}.
     * @return El {@code Object} de {@link DialogoLayout#continueLock}.
     */
    final Object getContinueLock(){
        return continueLock;
    }
    
    /**
     * Establece el nombre del que dice el dialogo.
     * 
     * @param name Nombre del que esta hablando.
     * @return El {@code JLabel} que contiene el texto, para luego eliminarlo.
     */
    final JLabel setName(String name){
    
        JLabel namePanel = new JLabel();
        namePanel.setBounds(WINDOW_WIDTH/8, (3*WINDOW_HEIGHT)/4-WINDOW_HEIGHT/13
                , WINDOW_WIDTH/6, WINDOW_HEIGHT/15);
        namePanel.setText(name);
        canvasDialogo.add(namePanel, new Integer(2));
        
        return namePanel;
    
    }
    
    /**
     * Establece el texto del dialogo.
     * 
     * @param text Lo que esta diciendo.
     * @return El {@code JLabel} que contiene el texto.
     */
    final JLabel setText(String text){
        
        JLabel bgPanelTxt =  new JLabel();
        bgPanelTxt.setBounds(WINDOW_WIDTH/8, (3*WINDOW_HEIGHT)/4, 
                (3*WINDOW_WIDTH)/4,WINDOW_HEIGHT/4);        
        bgPanelTxt.setVerticalAlignment(SwingConstants.TOP);        
        bgPanelTxt.setText(text);
        canvasDialogo.add(bgPanelTxt, new Integer(2));
        
        
        return bgPanelTxt;
        
    }
    
    /**
     * Remueve dos {@code JLabel} de {@link DialogoLayout#canvasDialogo}.
     * 
     * @param text El {@code JLabel} que contiene el texto del dialogo. {@link DialogoLayout#setText(java.lang.String)}
     * @param name El {@code JLabel} que contiene el nombre del dialogo. {@link DialogoLayout#setName(java.lang.String)}
     */
    final void removeText(JLabel text, JLabel name){
        canvasDialogo.remove(name);
        canvasDialogo.remove(text);
    }
    
    /**
     *  Remueve {@code JLabel} de {@link DialogoLayout#canvasDialogo}.
     * 
     * @param text El {@code JLabel} que contiene el texto del dialogo. {@link DialogoLayout#setText(java.lang.String)}
     */
    final void removeText(JLabel text){
        canvasDialogo.remove(text);
    }
    
    /**
     * Establece la UI para la confirmacion de aceptar una quest.
     * 
     * @param continueLock El {@code Object} que se encarga de pausar el {@code Thread} para el siguiente dialogo.
     * @return La respuesta del jugador.
     */
    final boolean questConfirmation(Object continueLock){
        
        JLabel yes = new JLabel();
        yes.setBounds(WINDOW_WIDTH/4, (WINDOW_HEIGHT*7)/16, WINDOW_WIDTH/8, WINDOW_HEIGHT/8);
        yes.setBackground(Color.GREEN);
        yes.setOpaque(true);
        yes.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  

                returnValue = true;

                synchronized(continueLock){
                    continueLock.notify();
                }

            }  
        }); 
        canvasDialogo.add(yes, new Integer(2));

        JLabel no = new JLabel();
        no.setBounds((5*WINDOW_WIDTH)/8, (WINDOW_HEIGHT*7)/16, WINDOW_WIDTH/8, WINDOW_HEIGHT/8);
        no.setBackground(Color.RED);
        no.setOpaque(true);
        no.addMouseListener(new MouseAdapter(){  
            public void mousePressed(MouseEvent e)  
            {  

                returnValue = false;

                synchronized(continueLock){
                    continueLock.notify();
                }

            }  
        }); 
        canvasDialogo.add(no, new Integer(2));
        synchronized(continueLock){
            try {
                continueLock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        removeText(yes, no);
        return returnValue;
    }
    
    /**
     * Establece el color del background.
     * @param color El color de fondo de la introduccion.
     * @return {@code JLabel} que contiene el fondo de la introduccion. 
     */
    final JLabel setBackgroundColor(Color color){
        JLabel bgColor = new JLabel();
        bgColor.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
        bgColor.setBackground(color);
        bgColor.setOpaque(true);
        canvasDialogo.add(bgColor, new Integer(0));
        
        return bgColor;
    }
    
    /**
     * Establece la UI para preguntare al jugador su nombre.
     * @return El nombre del jugador.
     */
    final String askName(){
        
        hideDialogoComp2();
        
        JLabel nameText = new JLabel();
        nameText.setBounds(WINDOW_WIDTH/2-100, WINDOW_HEIGHT/2-50, 200,20);
        nameText.setText("Ingresa tu nombre: ");
        canvasDialogo.add(nameText, new Integer(2));
        
        JTextField nameInput = new JTextField(20);
        nameInput.setBounds(WINDOW_WIDTH/2-100, WINDOW_HEIGHT/2-20, 200,40);
        canvasDialogo.add(nameInput, new Integer(2));
        nameInput.requestFocus();
        
        JLabel nameInputPanel = new JLabel();
        nameInputPanel.setBounds( WINDOW_WIDTH/3,(3*WINDOW_HEIGHT)/8, WINDOW_WIDTH/3, WINDOW_HEIGHT/4);
        nameInputPanel.setBackground(Color.CYAN);
        nameInputPanel.setOpaque(true);        
        
        canvasDialogo.add(nameInputPanel, new Integer(1));
               
        synchronized(continueLock){
            try {
                continueLock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        canvasDialogo.remove(nameInput);
        canvasDialogo.remove(nameText);
        canvasDialogo.remove(nameInputPanel);
        showDialogoComp();
        
        return nameInput.getText();
        
            
    }
    
    /**
     * Establece la UI para preguntarle al jugador si el nombre esta correcto. 
     */
    final void confirmationName(){
        
            JLabel yes = new JLabel();
            yes.setBounds(WINDOW_WIDTH/4, (WINDOW_HEIGHT*7)/16, WINDOW_WIDTH/8, WINDOW_HEIGHT/8);
            yes.setBackground(Color.GREEN);
            yes.setOpaque(true);
            yes.addMouseListener(new MouseAdapter(){  
                public void mousePressed(MouseEvent e)  
                {  
                    
                    nameConf = true;
                    
                    synchronized(continueLock){
                        continueLock.notify();
                    }
                    
                }  
            }); 
            canvasDialogo.add(yes, new Integer(2));
            
            JLabel no = new JLabel();
            no.setBounds((5*WINDOW_WIDTH)/8, (WINDOW_HEIGHT*7)/16, WINDOW_WIDTH/8, WINDOW_HEIGHT/8);
            no.setBackground(Color.RED);
            no.setOpaque(true);
            no.addMouseListener(new MouseAdapter(){  
                public void mousePressed(MouseEvent e)  
                {  
                    
                    nameConf = false;
                    
                    synchronized(continueLock){
                        continueLock.notify();
                    }
                    
                }  
            }); 
            canvasDialogo.add(no, new Integer(2));
            synchronized(continueLock){
                try {
                    continueLock.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Introduccion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            removeText(yes, no);
            
        
    }
    
    /**
     * Clase que se llama para continuar el siguiente dialogo.
     */
    private class Continue extends AbstractAction{
        
        @Override
        public void actionPerformed(ActionEvent e) {
            synchronized(continueLock){
                continueLock.notifyAll();            }
        }
        
    }
    
    
    
    
}
