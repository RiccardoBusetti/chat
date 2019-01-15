package server;

import server.logging.Logger;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Class responsible of starting and managing the messaging service.
 */
public class MessagingService {
    private ServerSocket serverSocket;
    private int port;

    public MessagingService(int port) {
        this.port = port;
    }

    public void start() {
        try {
            Logger.logStatus(this, "Server starting on port " + port);

            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {
            Logger.logStatus(this, "Server waiting for new connection on port " + port);

            ConnectionHandler connectionHandler = new ConnectionHandler(serverSocket.accept());

            Thread thread = new Thread(connectionHandler);
            thread.start();
        }
    }

    public static class Builder {

        private int port;

        public Builder onPort(int port) {
            this.port = port;

            return this;
        }

        public MessagingService build() {
            return new MessagingService(this.port);
        }

    }
}
