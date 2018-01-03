package br.cefetmg.games.networking.messages;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayerWantsToJoin extends Message {

    private final String name;

    public PlayerWantsToJoin(String senderIp, String name) {
        super(senderIp);
        this.name = name;
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_WANTS_TO_JOIN;
    }

    @Override
    public String getBody() {
        return name;
    }

}
