package br.cefetmg.games.screens;

import br.cefetmg.games.networking.NetworkMessageType;
import br.cefetmg.games.networking.Server;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class ConnectScreen extends BaseScreen {

    private Skin skin;
    private Stage stage;
    private Label labelDetails;
    private Label labelServerIP;
    private TextButton buttonConnect;
    private TextArea textIPAddress;
//    private TextArea textMessage;
    private TextButton buttonPlay;

    public ConnectScreen(Game game) {
        super(game);
    }

    @Override
    protected void create() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(viewport, batch);

        // Vertical group groups contents vertically.  I suppose that was probably pretty obvious
        VerticalGroup vg = new VerticalGroup().space(3).pad(5).fill();//.space(2).pad(5).fill();//.space(3).reverse().fill();
        // Set the bounds of the group to the entire virtual display
        vg.setBounds(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Create our controls
        final String ipAddress = Server.INSTANCE.getIpAddress();
        labelDetails = new Label(ipAddress, skin);
        labelServerIP = new Label("Endereço IP do Servidor", skin);
        buttonConnect = new TextButton("Conectar", skin);
        buttonPlay = new TextButton("Ir para Jogo", skin);
        textIPAddress = new TextArea("", skin);
//        textMessage = new TextArea("", skin);

        // Add them to scene
        vg.addActor(labelDetails);
        vg.addActor(labelServerIP);
        vg.addActor(textIPAddress);
//        vg.addActor(textMessage);
        vg.addActor(buttonConnect);
        vg.addActor(buttonPlay);

        // Add scene to stage
        stage.addActor(vg);

        // Wire up a click listener to our button
        buttonConnect.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                SocketHints socketHints = new SocketHints();
                // Socket will time our in 4 seconds
                socketHints.connectTimeout = 4000;
                //create the socket and connect to the server entered in the text box ( x.x.x.x format ) on port 9021
                Socket socket = Gdx.net.newClientSocket(
                        Protocol.TCP, textIPAddress.getText(), 9028, socketHints);
                try {
                    // write our entered message to the stream
                    socket.getOutputStream().write(
                            NetworkMessageType.CONNECT_REQUEST
                                    .getMessage(ipAddress, "")
                                    .getBytes());
                    System.out.println("Indo aguardar a resposta do servidor...");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = reader.readLine();
                    NetworkMessageType responseType = NetworkMessageType.valueOf(Integer.parseInt(response));
                    if (responseType == NetworkMessageType.CONNECTED_RESPONSE) {
                        String message = reader.readLine();
                        System.out.println("ip = " + message);
                        message = reader.readLine();
                        System.out.println("message = " + message);
                        if ("aew".equals(message)) {
                            System.out.println("sucesso!!!!");
                            game.setScreen(new PlayingScreen(game, socket));
                        } else {
                            System.out.println("erro!!!!!!!!");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayingScreen(game));
            }
            
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    protected void update(float dt) {
        stage.act(dt);
    }

    @Override
    protected void draw() {
        stage.draw();
    }

}
