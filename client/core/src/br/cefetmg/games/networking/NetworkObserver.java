package br.cefetmg.games.networking;

import br.cefetmg.games.player.NetworkedPlayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface NetworkObserver {
    public NetworkedPlayer newPlayerJoined(String ip, String name, Socket s);
    public void playerCommandReceived(NetworkedPlayer p, String command);
    public void playerUpdatedPositionReceived(NetworkedPlayer p, Vector2 pos);
}
