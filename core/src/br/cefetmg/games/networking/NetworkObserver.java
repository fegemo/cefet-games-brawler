package br.cefetmg.games.networking;

import br.cefetmg.games.player.NetworkedPlayer;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface NetworkObserver {
    public NetworkedPlayer newPlayerJoined(String ip, String name);
}
