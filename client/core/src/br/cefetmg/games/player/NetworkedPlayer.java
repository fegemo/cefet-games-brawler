package br.cefetmg.games.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.Socket;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class NetworkedPlayer extends Player {

    public String ipAddress;
    public Socket socket;

    public NetworkedPlayer(String name, Color color, String ipAddress, Socket s) {
        super(name, color);
        this.ipAddress = ipAddress;
        this.socket = s;
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
