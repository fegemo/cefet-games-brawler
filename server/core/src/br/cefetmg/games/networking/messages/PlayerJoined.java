package br.cefetmg.games.networking.messages;

import br.cefetmg.games.entities.Player;
import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayerJoined extends Message {

    private final Player player;

    public PlayerJoined(String senderIp, Player player) {
        super(senderIp);
        this.player = player;
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_JOINED;
    }

    @Override
    public String getBody() {
        StringBuilder b = new StringBuilder();
        b.append(player.getIp()).append("\n")
                .append(player.getName()).append("\n")
                .append(Color.rgba8888(player.getColor())).append("\n")
                .append(player.getPosition()).append("\n");
        return b.toString();
    }

}
