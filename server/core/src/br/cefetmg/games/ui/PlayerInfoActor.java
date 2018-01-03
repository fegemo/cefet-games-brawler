package br.cefetmg.games.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayerInfoActor extends HorizontalGroup {
//public class PlayerInfoActor extends Actor {

    private final String ipAdress;
    private final String name;
    private final Color playerColor;

//    private final HorizontalGroup contentHorizontalGroup;

    public PlayerInfoActor(String ipAddress, String name, Color playerColor,
            Skin skin) {
        this.ipAdress = ipAddress;
        this.name = name;
        this.playerColor = playerColor;

        LabelStyle playerLabelStyle = new LabelStyle(skin.getFont("bodyFont"), Color.GRAY);
//        contentHorizontalGroup = new HorizontalGroup();
//        contentHorizontalGroup.addActor(new Label(String.format("%s: %s", ipAddress, name), playerLabelStyle));
        addActor(new Label(String.format("%s: %s", ipAddress, name), playerLabelStyle));
    }

//    @Override
//    public void setDebug(boolean enabled) {
//        contentHorizontalGroup.setDebug(enabled);
//    }
//
//    
//    @Override
//    protected void drawDebugBounds(ShapeRenderer shapes) {
//        contentHorizontalGroup.drawDebug(shapes);
//    }
//
//    @Override
//    public void drawDebug(ShapeRenderer shapes) {
//        contentHorizontalGroup.drawDebug(shapes);
//    }
//
//    @Override
//    public void act(float delta) {
//        contentHorizontalGroup.act(delta);
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        contentHorizontalGroup.draw(batch, parentAlpha);
//    }
}
