package br.cefetmg.games.networking.messages;

import br.cefetmg.games.entities.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayerJoinSuccess extends Message {

    private final Vector2 position;
    private final Color color;
    private final Array<Player> players;

    public PlayerJoinSuccess(String senderIp, Vector2 position,
            Color color, Array<Player> players) {
        super(senderIp);
        this.position = position;
        this.color = color;
        this.players = players;
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_JOIN_SUCCESS;
    }

    @Override
    public String getBody() {
        StringBuilder b = new StringBuilder();
        b.append(position).append("\n")
                .append(color).append("\n")
                .append(players.size).append("\n");
        for (Player p : players) {
            b.append(p.toString());
        }

        return b.toString();
    }
}
