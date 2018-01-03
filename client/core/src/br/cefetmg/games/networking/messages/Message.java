package br.cefetmg.games.networking.messages;

import java.io.Serializable;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class Message implements Serializable {

    private final String senderIp;

    public Message(String senderIp) {
        this.senderIp = senderIp;
    }

    public abstract MessageType getType();

    public abstract String getBody();

    public byte getMessageId() {
        return getType().getId();
    }

    public String getMessageTypeName() {
        return getType().getName();
    }

    public String getSenderIp() {
        return senderIp;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getMessageId()).append("\n")
                .append(senderIp).append("\n")
                .append(getBody()).append("\n");

        return b.toString();
    }
}
