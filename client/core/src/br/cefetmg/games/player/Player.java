package br.cefetmg.games.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Align;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class Player {

    public String name;
    public Color color;
    public Avatar avatar;

    protected BitmapFont font;
    private static final float NAME_WIDTH_MULTIPLIER = 5f;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.font = new BitmapFont();
    }

    public Player attachToMap(Texture texture, TiledMap map, int x, int y) {
        avatar = new Avatar(texture, map);
        avatar.setPosition(x, y);

        return this;
    }

    public void update(float dt) {
        avatar.update(dt);
    }

    private void drawName(SpriteBatch batch) {
        font.draw(batch, name,
                avatar.sprite.getX() - avatar.sprite.getWidth()
                * (NAME_WIDTH_MULTIPLIER / 2f - 0.5f),
                avatar.sprite.getY() + avatar.sprite.getHeight()
                + font.getLineHeight(),
                avatar.sprite.getWidth() * NAME_WIDTH_MULTIPLIER,
                Align.center,
                false);
    }

    public void draw(SpriteBatch batch) {
        avatar.draw(batch);
        drawName(batch);
    }

    public abstract void jump();

    public abstract void climbDown();

    public abstract void move(float x, float y);
}
