package br.cefetmg.games.networking;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum NetworkMessage {
    CONNECT_REQUEST(1),
    CONNECTED_RESPONSE(2),
    SEND_CHARACTER_COMMAND(3),
    UPDATE_CHARACTER_POSITION(4);

    private final int messageId;

    NetworkMessage(int id) {
        messageId = id;
    }

    public String getMessage(String sourceIp, String content) {
        StringBuilder b = new StringBuilder();
        b.append(messageId)
                .append("\n")
                .append(sourceIp)
                .append("\n")
                .append(content);
        return b.toString();
    }

    public static NetworkMessage valueOf(int id) {
        for (NetworkMessage m : values()) {
            if (m.messageId == id) {
                return m;
            }
        }
        throw new IllegalArgumentException("tentou-se pegar um NetworkMessage "
                + "com um id desconhecido: " + id);
    }
}
