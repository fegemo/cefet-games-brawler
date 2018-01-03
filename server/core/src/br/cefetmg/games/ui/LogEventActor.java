package br.cefetmg.games.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.Date;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class LogEventActor extends HorizontalGroup {
//public class LogEventActor extends Actor {

    private final Date eventTimestamp;
    private final String clientIp;
    private final String messageType;
    private final String message;

//    private final HorizontalGroup contentHorizontalGroup;

    public LogEventActor(Date eventTimestamp, String clientIp,
            String messageType, String message, Skin skin) {
        this.eventTimestamp = eventTimestamp;
        this.clientIp = clientIp;
        this.messageType = messageType;
        this.message = message;

        LabelStyle styleGray = new LabelStyle(skin.getFont("bodyFont"), Color.GRAY);
        LabelStyle styleBlue = new LabelStyle(skin.getFont("bodyFont"), Color.BLUE);
        LabelStyle styleGreen = new LabelStyle(skin.getFont("bodyFont"), Color.GREEN);
        LabelStyle styleBlack = new LabelStyle(skin.getFont("bodyFont"), Color.BLACK);

//        contentHorizontalGroup = new HorizontalGroup();
//        contentHorizontalGroup.addActor(new Label(this.eventTimestamp.toString(), styleGray));
//        contentHorizontalGroup.addActor(new Label(this.clientIp, styleBlue));
//        contentHorizontalGroup.addActor(new Label(this.messageType, styleGreen));
//        contentHorizontalGroup.addActor(new Label(this.message, styleBlack));
        addActor(new Label(this.eventTimestamp.toString(), styleGray));
        addActor(new Label(this.clientIp, styleBlue));
        addActor(new Label(this.messageType, styleGreen));
        addActor(new Label(this.message, styleBlack));
    }

//    @Override
//    public void setDebug(boolean enabled) {
//        contentHorizontalGroup.setDebug(enabled);
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
