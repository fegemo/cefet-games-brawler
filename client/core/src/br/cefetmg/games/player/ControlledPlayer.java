package br.cefetmg.games.player;

import br.cefetmg.games.networking.NetworkMessageType;
import br.cefetmg.games.networking.Server;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class ControlledPlayer extends Player {

    private final Socket socket;
//    private final BufferedReader reader;
    private float timeSinceLastPositionUpdate = 0;

    public ControlledPlayer(String name, Color color, Socket socket) {
        super(name, color);
        this.socket = socket;
//        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void jump() {
        avatar.jump();
    }

    @Override
    public void climbDown() {
        avatar.climbDown();
    }

    @Override
    public void move(float x, float y) {
        avatar.move(x, y);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        timeSinceLastPositionUpdate += dt;
        if (timeSinceLastPositionUpdate > 3) {
            System.out.println("tentando enviar minha posição para server");
            try {
                socket.getOutputStream().write(
                        NetworkMessageType.UPDATE_PLAYER_POSITION
                                .getMessage(Server.getIpAddress(), new Vector2(avatar.sprite.getX(), avatar.sprite.getY()).toString())
                                .getBytes()
                );
                timeSinceLastPositionUpdate = 0;
            } catch (IOException ex) {
                Logger.getLogger(ControlledPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
