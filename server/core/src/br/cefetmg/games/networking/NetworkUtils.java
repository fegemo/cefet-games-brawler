package br.cefetmg.games.networking;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class NetworkUtils {

    public static String getIpAddress() {
        List<String> addresses = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface
                    .getNetworkInterfaces();
            for (NetworkInterface ni : Collections.list(interfaces)) {
                for (InetAddress address
                        : Collections.list(ni.getInetAddresses())) {
                    if (address instanceof Inet4Address) {
                        addresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Logger.getLogger(NetworkUtils.class.getSimpleName()).log(
                    Level.SEVERE, "Não foi possível recuperar o "
                            + "endereço IP do computador");
        }

        // retorna o primeiro IP que não seja o de loopback
        for (String str : addresses) {
            if (!"127.0.0.1".equals(str)) {
                return str;
            }
        }
        return "127.0.0.1";
    }

}
