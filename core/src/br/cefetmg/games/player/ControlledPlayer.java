package br.cefetmg.games.player;

import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class ControlledPlayer extends Player {

    public ControlledPlayer(String name, Color color) {
        super(name, color);
    }

    @Override
    public void jump() {
        avatar.jump();
    }

    @Override
    public void climbDown() {
        avatar.climbDown();
    }

    @Override
    public void move(float x, float y) {
        avatar.move(x, y);
    }
}
