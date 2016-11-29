package br.cefetmg.games.networking;

import br.cefetmg.games.player.NetworkedPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum Server {
    INSTANCE;

    private boolean started = false;
    private HashMap<String, NetworkedPlayer> players = new HashMap<String, NetworkedPlayer>();
    private NetworkObserver observer;

    private void handleMessage(BufferedReader buffer, Socket socket) {
//        BufferedWriter writer = new BufferedWriter(
//                new OutputStreamWriter(socket.getOutputStream()));
        try {
            switch (NetworkMessage.valueOf(Integer.parseInt(buffer.readLine()))) {
                case CONNECT_REQUEST:
                    // recebe a requisição e cria um novo jogador
                    NetworkedPlayer newChar = observer.newPlayerJoined(
                            buffer.readLine(),  // endereço ip
                            buffer.readLine()   // nome
                    );
                    players.putIfAbsent(socket.getRemoteAddress(), newChar);
                    
                    // escreve a resposta
                    socket.getOutputStream().write(
                            NetworkMessage.CONNECTED_RESPONSE
                                    .getMessage(getIpAddress(), "aew")
                                    .getBytes());
                    
                    break;
                case SEND_CHARACTER_COMMAND:
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

                    handleMessage(buffer, socket);
                }
            }
        }).start(); // And, start the thread running        
    }

    public String getIpAddress() {
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
