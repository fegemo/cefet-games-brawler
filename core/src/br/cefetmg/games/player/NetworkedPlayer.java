package br.cefetmg.games.player;

import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class NetworkedPlayer extends Player {

    public String ipAddress;

    public NetworkedPlayer(String name, Color color, String ipAddress) {
        super(name, color);
        this.ipAddress = ipAddress;
    }

    @Override
    public void jump() {
        
    }

    @Override
    public void climbDown() {
        
    }

    @Override
    public void move(float x, float y) {
        
    }
    
}
