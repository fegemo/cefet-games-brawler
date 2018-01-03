package br.cefetmg.games.entities;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayerState implements Serializable {

    private final String ip;
    private final Vector2 position;
    private AvatarState state;
    private boolean facingRight;

    public PlayerState(String ip, Vector2 position, AvatarState state,
            boolean facingRight) {
        this.ip = ip;
        this.position = position;
        this.state = state;
        this.facingRight = facingRight;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(ip).append("\n")
                .append(position.toString()).append("\n")
                .append(state).append("\n")
                .append(facingRight).append("\n");
        return b.toString();
    }

    /**
     * Atualiza este jogador de acordo com as strings que o descreve.
     *
     * @param line2 linha com as coordenadas da posição.
     * @param line3 linha com o estado (AvatarState).
     * @param line4 linha com o valor de facingRight.
     */
    public void updateFromString(String line2,
            String line3, String line4) {
        String[] coords = line2.split(",");
        position.set(
                Float.parseFloat(coords[0]), Float.parseFloat(coords[1]));
        state = AvatarState.valueOf(line3);
        facingRight = Boolean.valueOf(line4);
    }

    /**
     * Cria um novo jogador a partir de strings.
     *
     * @param line1 linha com o ip do jogador.
     * @param line2 linha com as coordenadas da posição.
     * @param line3 linha com o estado (AvatarState).
     * @param line4 linha com o valor de facingRight.
     * @return o novo jogador.
     */
    public static PlayerState fromString(String line1, String line2,
            String line3, String line4) {
        String[] coords = line2.split(",");
        Vector2 position = new Vector2(
                Float.parseFloat(coords[0]), Float.parseFloat(coords[1]));
        return new PlayerState(line1,
                position, AvatarState.valueOf(line3), Boolean.valueOf(line4));
    }

    public enum AvatarState implements Serializable {
        IDLE,
        WALKING,
        JUMPING,
        CLIMBING
    }
}
