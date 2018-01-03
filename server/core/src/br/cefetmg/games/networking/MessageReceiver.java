package br.cefetmg.games.networking;

import br.cefetmg.games.networking.messages.Message;
import br.cefetmg.games.networking.messages.MessageType;
import br.cefetmg.games.networking.messages.PlayerWantsToJoin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
class MessageReceiver implements Runnable {

    private final Server server;
    private final NetworkObserver observer;

    MessageReceiver(Server server, NetworkObserver observer) {
        this.server = server;
        this.observer = observer;
    }

    private void handleMessage(BufferedReader buffer, final Socket socket) {
        Message message = null;
        try {
            // olha para a primeira linha, primeiro byte, para identificar qual
            // é o tipo de mensagem sendo recebida
            String firstLine = buffer.readLine();
            MessageType messageType = MessageType.valueOf(firstLine.getBytes()[0]);

            // todas as mensagens enviam o senderIp na segunda linha
            String senderIp = buffer.readLine();

            // sabendo qual seu é o tipo, agora vamos lidar com ela
            switch (messageType) {
                case PLAYER_WANTS_TO_JOIN:
                    // monta a mensagem que está sendo recebida
                    String name = buffer.readLine();
                    message = new PlayerWantsToJoin(senderIp, name);

                    // começa a lidar com ela: (a) cor do player, (b) adiciona
                    // jogador à lista e (c) dá resposta de volta
                    server.newPlayer(senderIp, socket, name);

                    break;

//                case PLAYER_JOIN_SUCCESS:
//                    break;
//                case PLAYER_JOINED:
//                    break;
                case UPDATE_PLAYER_STATE:
                    
                    break;
                    
                case UPDATE_ALL_PLAYERS_STATE:

                    break;

                default:
                    String errorMessage = "Servidor recebeu uma "
                            + "mensagem com um id desconhecido: " + firstLine;
                    Gdx.app.log("MessageReceiver", errorMessage);
                    observer.errorOcurred(errorMessage, null);
                    break;
            }

        } catch (IOException ex) {
            Gdx.app.log("MessageReceiver", ex.getMessage(), ex);
            observer.errorOcurred(ex.getMessage(), ex);
        }

        // se a mensagem recebida estiver correta, notifica a interface
        if (message != null) {
            observer.messageReceived(message);
        }
    }

    @Override
    public void run() {
        ServerSocketHints serverSocketHint = new ServerSocketHints();
        serverSocketHint.acceptTimeout = 0;

        ServerSocket serverSocket = Gdx.net.newServerSocket(
                Net.Protocol.TCP, Server.SERVER_PORT, serverSocketHint);

        Gdx.app.log("evento", "Servidor escutando na porta "
                + Server.SERVER_PORT);

        while (true) {
            Socket socket = serverSocket.accept(null);

            // lê o que o socket está recebendo usando um BufferedReader
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            handleMessage(buffer, socket);
        }
    }

}
