package br.cefetmg.games.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class Player {

    private final String ip;
    private final Socket socket;
    private final String name;
    private final Color color;
    private final Vector2 position;
    private PlayerState.AvatarState state;
    private boolean facingRight;

    public Player(String ip, Socket socket, String name, Color color,
            Vector2 position) {
        this.ip = ip;
        this.socket = socket;
        this.name = name;
        this.color = color;
        this.position = new Vector2(position);
    }

    public Color getColor() {
        return color;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }
}
