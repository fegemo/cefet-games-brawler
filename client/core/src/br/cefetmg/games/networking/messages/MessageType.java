package br.cefetmg.games.networking.messages;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum MessageType {
    PLAYER_WANTS_TO_JOIN((byte) 0),
    PLAYER_JOIN_SUCCESS((byte) 1),
    PLAYER_JOINED((byte) 2),
    UPDATE_ALL_PLAYERS_STATE((byte) 3),
    UPDATE_PLAYER_STATE((byte) 4);

    private final byte id;

    MessageType(byte id) {
        this.id = id;
    }

    public String getName() {
        return this.name();
    }
    
    public byte getId() {
        return id;
    }
    
    /**
     * Retorna a instância de MessageType correspondente ao id passado.
     * @param id
     * @return 
     */
    public static MessageType valueOf(byte id) {
        for (MessageType t : MessageType.values()) {
            if (t.getId() == id) {
                return t;            
            }
        }
        return null;
    }
}
