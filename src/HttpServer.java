import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import util.testJson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Date;

public class HttpServer {

    private static int httpport;

    static {
        httpport = 8080;
    }

    void httpServer() throws Exception {
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(httpport), 0);
        server.createContext("/", new index());
        server.setExecutor(null);
        server.start();
        System.out.println(new Date());
        System.out.println("Server listen port: " + httpport);
    }

    static class index implements HttpHandler {
        testJson json = new testJson();

        public void handle(HttpExchange t) throws IOException {
            StringBuilder response = new StringBuilder(json.readfile());
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }
}
