package br.cefetmg.games.networking;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum NetworkMessageType {
    CONNECT_REQUEST(1),
    CONNECTED_RESPONSE(2),
    NEW_PLAYER_JOINED(3),
    SEND_PLAYER_COMMAND(4),
    UPDATE_PLAYER_POSITION(5);

    public final int messageId;

    NetworkMessageType(int id) {
        messageId = id;
    }

    public String getMessage(String sourceIp, String content) {
        StringBuilder b = new StringBuilder();
        b.append(messageId)
                .append("\n")
                .append(sourceIp)
                .append("\n")
                .append(content)
                .append("\n");
        return b.toString();
    }

    public static NetworkMessageType valueOf(int id) {
        for (NetworkMessageType m : values()) {
            if (m.messageId == id) {
                return m;
            }
        }
        throw new IllegalArgumentException("tentou-se pegar um NetworkMessage "
                + "com um id desconhecido: " + id);
    }
}
