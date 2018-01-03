package br.cefetmg.games;

import br.cefetmg.games.entities.Player;
import br.cefetmg.games.networking.NetworkObserver;
import br.cefetmg.games.networking.Server;
import br.cefetmg.games.networking.messages.Message;
import br.cefetmg.games.networking.messages.PlayerJoined;
import br.cefetmg.games.ui.PlayerInfoActor;
import br.cefetmg.games.ui.LogEventActor;
import br.cefetmg.games.ui.MiniMapActor;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Date;

public class BrawlerServerApplication extends ApplicationAdapter
        implements NetworkObserver {

    private Stage stage;
    private VerticalGroup playerListVerticalGroup;
    private Skin skin;
    private VerticalGroup logScrollVerticalGroup;

    @Override
    public void create() {
        // servidor
        Server.INSTANCE.start(this);

        // opengl
        Gdx.gl20.glClearColor(1, 1, 1, 1);

        // skin e fontes
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("ui/UbuntuMono-R.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        skin.add("bodyFont", generator.generateFont(parameter), BitmapFont.class);
        generator.dispose();

        // elementos da interface:
        // 1 outerHorizontalSplitPane: leftSideTable + playersTable
        // 1.1 leftSideTable: serverInfoLabel + leftVerticalSplitPane
        // 1.1.1 serverInfoLabel
        // 1.1.2 leftVerticalSplitPane: miniMap + logTable
        // 1.1.2.1 miniMap
        // 1.1.2.2 logTable: logTitleLabel + logScrollPane
        // 1.1.2.2.1 logTitleLabel
        // 1.1.2.2.2 logScrollPane: logScrollVerticalGroup
        // 1.1.2.2.2.1 logScrollVerticalGroup: logEventActor...
        // 1.2 playersTable: playersTitle + playersScrollPane
        // 1.2.1 playersTitleLabel
        // 1.2.2 playersScrollPane: playerListVerticalGroup
        // 1.2.2.1 playerListVerticalGroup: playerInfo...
        // cenário
        stage = new Stage(new ScreenViewport());

        // lado esquerdo
        Label serverInfoLabel = new Label("Endereço IP: 127.0.0.1", skin);
        MiniMapActor miniMap = new MiniMapActor();
        miniMap.setDebug(true);

        Label logTitleLabel = new Label("Log de Eventos", skin);
        logScrollVerticalGroup = new VerticalGroup();
        LogEventActor sampleLogEvent = new LogEventActor(
                new Date(), "192.168.1.32", "SEND_COMMAND", "djakldfja", skin);
        logScrollVerticalGroup.addActor(sampleLogEvent);
//        LogEventActor sampleLogEvent2 = new LogEventActor(
//                new Date(), "192.168.1.32", "SEND_COMMAND", "djakldfja", skin);        
//        logScrollVerticalGroup.addActor(sampleLogEvent);
//        LogEventActor sampleLogEvent3 = new LogEventActor(
//                new Date(), "192.168.1.32", "SEND_COMMAND", "djakldfja", skin);        
//        logScrollVerticalGroup.addActor(sampleLogEvent);
//        LogEventActor sampleLogEvent4 = new LogEventActor(
//                new Date(), "192.168.1.32", "SEND_COMMAND", "djakldfja", skin);        
//        logScrollVerticalGroup.addActor(sampleLogEvent);
//        LogEventActor sampleLogEvent5 = new LogEventActor(
//                new Date(), "192.168.1.32", "SEND_COMMAND", "djakldfja", skin);        
//        logScrollVerticalGroup.addActor(sampleLogEvent);
//        logScrollVerticalGroup.addActor(sampleLogEvent2);
//        logScrollVerticalGroup.addActor(sampleLogEvent3);
//        logScrollVerticalGroup.addActor(sampleLogEvent4);
//        logScrollVerticalGroup.addActor(sampleLogEvent5);

        ScrollPane logScrollPane = new ScrollPane(logScrollVerticalGroup);

        Table logTable = new Table();
        logTable.add(logTitleLabel).row();
        logTable.add(logScrollPane).expand().fill();

        // o split pane vertical que contém o mapa e o log de eventos
        SplitPane leftVerticalSplitPane = new SplitPane(
                miniMap, logTable, true, skin);

        Table leftSideTable = new Table();
        leftSideTable.add(serverInfoLabel).row();
        leftSideTable.add(leftVerticalSplitPane).expand().fill();

        // lado direito
        playerListVerticalGroup = new VerticalGroup();
//        PlayerInfoActor samplePlayerInfo = new PlayerInfoActor(
//                "192.123.11.1", "fegemo", Color.FOREST, skin);
//        playerListVerticalGroup.addActor(samplePlayerInfo);
//        PlayerInfoActor samplePlayerInfo2 = new PlayerInfoActor(
//                "192.123.11.1", "fegemo", Color.FOREST, skin);
//        playerListVerticalGroup.addActor(samplePlayerInfo2);
//        PlayerInfoActor samplePlayerInfo3 = new PlayerInfoActor(
//                "192.123.11.1", "fegemo", Color.FOREST, skin);
//        playerListVerticalGroup.addActor(samplePlayerInfo3);
//        PlayerInfoActor samplePlayerInfo4 = new PlayerInfoActor(
//                "192.123.11.1", "fegemo", Color.FOREST, skin);
//        playerListVerticalGroup.addActor(samplePlayerInfo4);
//        PlayerInfoActor samplePlayerInfo5 = new PlayerInfoActor(
//                "192.123.11.1", "fegemo", Color.FOREST, skin);
//        playerListVerticalGroup.addActor(samplePlayerInfo5);
        ScrollPane playersScrollPane = new ScrollPane(playerListVerticalGroup);
        Label playersTitleLabel = new Label("Jogadores", skin);
        Table playersTable = new Table();
        playersTable.add(playersTitleLabel).row();
        playersTable.add(playersScrollPane).expand().fill();

        // o split pane horizontal que é o mais externo
        SplitPane outerHorizontalSplitPane = new SplitPane(
                leftSideTable, playersTable, false, skin);
        outerHorizontalSplitPane.setFillParent(true);
        outerHorizontalSplitPane.setSplitAmount(0.8f);
        stage.addActor(outerHorizontalSplitPane);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void messageReceived(Message message) {
        LogEventActor messageLogEvent = new LogEventActor(
                new Date(), message.getSenderIp(),
                message.getMessageTypeName(), message.getBody(), skin);
        logScrollVerticalGroup.addActor(messageLogEvent);
    }

    @Override
    public void messageSent(Message message) {
        LogEventActor messageLogEvent = new LogEventActor(
                new Date(), message.getSenderIp(),
                message.getMessageTypeName(), message.getBody(), skin);
        logScrollVerticalGroup.addActor(messageLogEvent);
    }

    @Override
    public void newPlayerJoined(Player player) {
        PlayerInfoActor joiningPlayerInfo = new PlayerInfoActor(
                player.getIp(), player.getName(), player.getColor(), skin);
        playerListVerticalGroup.addActor(joiningPlayerInfo);
    }

    @Override
    public void errorOcurred(String message, Throwable error) {
        Label errorLabel = new Label(
                message + error.getMessage(), skin);
        logScrollVerticalGroup.addActor(errorLabel);
    }

}
