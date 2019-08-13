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
public class outOfBoundriesException extends RuntimeException {

    private final int tilePosX;
    private final int tilePosY;
    
    public outOfBoundriesException(int tilePosX, int tilePosY) {
        this.tilePosX = tilePosX;
        this.tilePosY = tilePosY;
        
    }

    @Override
    public String toString() {
        return "outOfBoundriesException{" + "tilePosX=" + tilePosX + ", tilePosY=" + tilePosY + "} are out of boundries";
    }
}
