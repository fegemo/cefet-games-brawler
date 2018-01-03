package br.cefetmg.games.networking;

import br.cefetmg.games.entities.Player;
import br.cefetmg.games.networking.messages.Message;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface NetworkObserver {

    public void messageReceived(Message message);

    public void messageSent(Message message);

    public void newPlayerJoined(Player player);
    
    public void errorOcurred(String message, Throwable error);

}
