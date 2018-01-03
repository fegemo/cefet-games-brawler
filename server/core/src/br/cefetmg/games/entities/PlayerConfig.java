package br.cefetmg.games.entities;

import com.badlogic.gdx.graphics.Color;
import java.io.Serializable;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayerConfig implements Serializable {

    private final String ip;
    private final String name;
    private final Color color;

    public PlayerConfig(String ip, String name, Color color) {
        this.ip = ip;
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(ip).append("\n")
                .append(name).append("\n")
                .append(Color.rgba8888(color)).append("\n");
        return b.toString();
    }
}
