package util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ScanPort {
    public boolean portAccess(String addr, Integer openport, int to) {
        return (testip(addr, openport, to));
    }
    private static boolean testip(String addr, int openport, int to) {
        try {
            try (Socket soc = new Socket()) {
                int timeout = 5000;
                soc.connect(new InetSocketAddress(addr, openport), timeout);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
