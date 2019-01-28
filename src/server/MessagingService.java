package server;

import server.logging.Logger;
import server.packets.PacketsDispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible of starting and managing the messaging service.
 */
public class MessagingService {
    private ServerSocket serverSocket;
    private List<Thread> usedThreads;
    private int port;
    private boolean isAsync;
    private boolean stop;

    public MessagingService(int port, boolean isAsync) {
        this.usedThreads = new ArrayList<>();
        this.port = port;
        this.isAsync = isAsync;
        this.stop = false;
    }

    public void start() {
        if (isAsync) {
            Thread serverThread = new Thread(this::startService);
            usedThreads.add(serverThread);
            serverThread.start();

            Thread packetsDispatcherThread = new Thread(new PacketsDispatcher());
            usedThreads.add(packetsDispatcherThread);
            packetsDispatcherThread.start();
        } else {
            startService();
        }
    }

    private void startService() {
        try {
            Logger.logStatus(this, "Server starting on port " + port + ".");

            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {
        serverSocket = new ServerSocket(port);

        while (!stop) {
            Logger.logStatus(this, "Server waiting for new connection on port " + port + ".");

            ConnectionHandler connectionHandler = new ConnectionHandler(serverSocket.accept());

            Thread connectionHandlerThread = new Thread(connectionHandler);
            usedThreads.add(connectionHandlerThread);
            connectionHandlerThread.start();
        }

        Logger.logStatus(this, "Server is shutting off...");
        serverSocket.close();
        Logger.logStatus(this, "Server has been arrested.");
    }

    // Implement stop by sending a dummy socket to accept.
    public void stop() {
        stop = true;

        for (Thread usedThread : usedThreads) {
            if (usedThread.isAlive()) usedThread.stop();
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
