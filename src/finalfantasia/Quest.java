/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalfantasia;

/**
 *
 * @author Emiliano Heredia Garcia (A01377072)
 */
public class Quest {
    
    private int objetivo;
    private int cantidad =0;
    private Class tipo;
    
    Quest(int objetivo, Class tipo){

        this.objetivo = objetivo;
        this.tipo = tipo;

    }

    void agregarKill(Class kill){
        if(isTipo(kill)){
            cantidad++;
        }
    }
    
    boolean isTipo(Class kill){
        return tipo.getClass()==kill.getClass();
    }
    
    boolean isCompleted(){
        return cantidad>=objetivo;
    }
    
}
