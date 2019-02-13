package client.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//Server Packets

public class ClientSupporter {

    private String host;
    private int port;

    private Socket clientSocket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public ClientSupporter(String address, int port) throws Exception {
        host = address;
        this.port = port;
        this.createConnection();
    }

    public ClientSupporter(){

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private void createConnection() throws IOException {
        clientSocket = new Socket(host, port);
        printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }

    public String makeRequest(String request_data) throws IOException {
        printWriter.println(request_data);
        return (bufferedReader.readLine());
    }

    public void sendLine(String data) {
        printWriter.println(data);
    }

    public void closeSocket() throws IOException {
        clientSocket.close();
    }

    public String readLine() throws IOException {
        return (bufferedReader.readLine());
    }
}
