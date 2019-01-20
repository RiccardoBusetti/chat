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
    private boolean isAsync;

    public MessagingService(int port, boolean isAsync) {
        this.port = port;
        this.isAsync = isAsync;
    }

    public void start() {
        if (isAsync) {
            new Thread(this::startService).start();
        } else {
            startService();
        }
    }

    private void startService() {
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
        private boolean isAsync = false;

        public Builder onPort(int port) {
            this.port = port;

            return this;
        }

        public Builder async() {
            this.isAsync = true;

            return this;
        }

        public MessagingService build() {
            return new MessagingService(this.port, this.isAsync);
        }
    }
}
