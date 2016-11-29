package br.cefetmg.games.networking;

import br.cefetmg.games.player.NetworkedPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum Server {
    INSTANCE;

    private boolean started = false;
    private final HashMap<String, NetworkedPlayer> players
            = new HashMap<String, NetworkedPlayer>();
    private NetworkObserver observer;

    private void handleMessage(BufferedReader buffer, final Socket socket) {
        try {
            final String playerIp = socket.getRemoteAddress();
            StringBuilder b = new StringBuilder();
            switch (NetworkMessageType.valueOf(
                    Integer.parseInt(buffer.readLine()))) {
                case CONNECT_REQUEST:
                    System.out.println("Servidor recebeu um CONNECT_REQUEST!");
                    final String playerName = buffer.readLine();

                    // recebe a requisição e cria um novo jogador
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            NetworkedPlayer newChar = observer.newPlayerJoined(
                                    playerIp,
                                    playerName,
                                    socket
                            );
                            players.putIfAbsent(socket.getRemoteAddress(), newChar);
                        }

                    });
                    
                    // escreve a resposta
                    socket.getOutputStream().write(
                            NetworkMessageType.CONNECTED_RESPONSE
                            .getMessage(getIpAddress(), "aew")
                            .getBytes());

                    // envia este jogador para os players que já estavam
                    // conectados
                    b.append(playerIp).append("\n")
                            .append(playerName).append("\n");

                    for (Entry<String, NetworkedPlayer> entry
                            : players.entrySet()) {
                        if (entry.getKey().equals(socket.getRemoteAddress())) {
                            continue;
                        }

                        String message = NetworkMessageType.NEW_PLAYER_JOINED
                                .getMessage(getIpAddress(), b.toString());
                        entry.getValue().socket.getOutputStream().write(message.getBytes());
                    }

                    break;

                case SEND_PLAYER_COMMAND:
                    break;

                case UPDATE_PLAYER_POSITION:
                    int newX = Integer.parseInt(buffer.readLine());
                    int newY = Integer.parseInt(buffer.readLine());
                    b.append(playerIp)
                            .append("\n")
                            .append(newX)
                            .append("\n")
                            .append(newY)
                            .append("\n");

                    for (Entry<String, NetworkedPlayer> entry
                            : players.entrySet()) {
                        if (entry.getKey().equals(socket.getRemoteAddress())) {
                            continue;
                        }

                        entry.getValue().socket.getOutputStream().write(NetworkMessageType.UPDATE_PLAYER_POSITION
                                .getMessage(getIpAddress(), b.toString())
                                .getBytes()
                        );
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(NetworkObserver observer) {
        if (started) {
            return;
        }
        this.observer = observer;
        new Thread(new Runnable() {

            @Override
            public void run() {
                started = true;
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                serverSocketHint.acceptTimeout = 0;

                ServerSocket serverSocket = Gdx.net.newServerSocket(
                        Protocol.TCP, 9028, serverSocketHint);

                System.out.println("Escutando na porta 9028");
                while (true) {
                    Socket socket = serverSocket.accept(null);

                    // Read data from the socket into a BufferedReader
                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    System.out.println("Server recebeu uma mensagem!");
                    handleMessage(buffer, socket);
                }
            }
        }).start(); // And, start the thread running        
    }

    public static String getIpAddress() {
        List<String> addresses = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface ni : Collections.list(interfaces)) {
                for (InetAddress address : Collections.list(ni.getInetAddresses())) {
                    if (address instanceof Inet4Address) {
                        addresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        for (String str : addresses) {
            if (!"127.0.0.1".equals(str)) {
                return str;
            }
        }
        return "";
    }
}
