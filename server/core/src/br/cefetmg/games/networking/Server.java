package br.cefetmg.games.networking;

import br.cefetmg.games.entities.Player;
import br.cefetmg.games.networking.messages.Message;
import br.cefetmg.games.networking.messages.PlayerJoinSuccess;
import br.cefetmg.games.networking.messages.PlayerJoined;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Array;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum Server {
    INSTANCE;

    public static final int SERVER_PORT = 9028;

    private boolean started = false;
    private final HashMap<String, Player> players
            = new HashMap<String, Player>();
    private NetworkObserver observer;
    private String serverIp;
    private final Array<Vector2> spawnPoints
            = new Array<Vector2>(new Vector2[]{
        new Vector2(50, 100),
        new Vector2(100, 100),
        new Vector2(200, 100),
        new Vector2(300, 100)
    });

    public void start(NetworkObserver observer) {
        if (started) {
            return;
        }
        serverIp = NetworkUtils.getIpAddress();
        started = true;
        this.observer = observer;

        Thread receiver = new Thread(new MessageReceiver(this, observer));
        receiver.setName("Receptor de mensagens");
        receiver.start();

        Thread synchronizer = new Thread(new GameStateSynchronizer(this, observer));
        synchronizer.setName("Sincronizador de estado de jogo");
        synchronizer.start();
    }

    private Color getRandomColor() {
        return new Color(
                MathUtils.random(),
                MathUtils.random(),
                MathUtils.random(),
                1);
    }

    private Vector2 getRandomSpawnPoint() {
        return spawnPoints.random();
    }

    private Array<Player> getPlayerArray() {
        return new Array<Player>(players.values().toArray(new Player[players.size()]));
    }

    public void newPlayer(String ip, Socket socket, String name) {
        Player joiningPlayer;
        // verifica se o jogador já existe na lista do servidor
        if ((joiningPlayer = players.get(ip)) == null) {

            // se ele não existe, gera uma cor e o adiciona à lista
            joiningPlayer = new Player(ip, socket,
                    name, getRandomColor(), getRandomSpawnPoint());
            players.put(ip, joiningPlayer);
        }

        // independente se o jogador já existia para o servidor ou não, o
        // servidor envia a resposta como se ele tivesse acabado de
        // entrar (possivelmente o cliente pode ter caido e retornou ao jogo)
        // 
        // começa a montar a resposta... primeiro, monta uma lista com as
        // infos sobre todos os players; depois, monta a mensagem de 
        // resposta ao jogador que acabou de entrar
        Message joinSuccess = new PlayerJoinSuccess(serverIp,
                joiningPlayer.getPosition(), joiningPlayer.getColor(),
                getPlayerArray());

        try {
            // escreve a mensagem no socket
            socket.getOutputStream().write(
                    joinSuccess.toString().getBytes());

            // para cada jogador préexistente, monta e envia uma mensagem
            // de PlayerJoined
            for (Player p : players.values()) {
                if (p == joiningPlayer) {
                    continue;
                }
                PlayerJoined joined = new PlayerJoined(serverIp, joiningPlayer);

                try {
                    p.getSocket().getOutputStream().write(
                            joined.toString().getBytes());
                    
                    observer.messageSent(joined);
                } catch (IOException ex) {
                    String errorMessage = "Erro ao escrever avisar um novo "
                            + "jogador para o jogador " + p.getName()
                            + " escrevendo uma mensagem PlayerJoined";
                    Gdx.app.error("Server", errorMessage, ex);
                    observer.errorOcurred(errorMessage, ex);
                }
            }
        } catch (IOException ex) {
            String errorMessage = "Erro ao escrever resposta de "
                    + "PlayerJoinSuccess";
            Gdx.app.error("Server", errorMessage, ex);
            observer.errorOcurred(errorMessage, ex);
            
            // desfaz o fato de que o jogador entrou
            players.remove(joiningPlayer);
        }
    }
}
