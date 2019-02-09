package client.handlers;

import client.controller.ChatController;
import server.entities.packets.*;
import server.packets.PacketsDecoder;

import java.io.IOException;
import java.net.SocketException;

public class ClientReader extends Thread {

    private ClientSupporter clientSupporter;
    private ChatController chatController;

    public ClientReader(ClientSupporter client, ChatController controller) {
        clientSupporter = client;
        chatController = controller;
    }

    @Override
    public void run() {
        PacketsDecoder packetsDecoder = new PacketsDecoder();
        while (true) {
            while (true) {
                String line = null;
                try {
                    line = clientSupporter.readLine();
                } catch (SocketException e) {
                    System.out.println("Closing the thread...");
                    System.exit(-1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (line != null) {
                    System.out.println(line);
                    Packet output = packetsDecoder.decode(line);
                    if (output instanceof MulticastMessagePacket)
                        chatController.showMessage(((MulticastMessagePacket) output).getSenderUsername(), null, ((MulticastMessagePacket) output).getContent());
                    else if (output instanceof UnicastMessagePacket)
                        chatController.showMessage(((UnicastMessagePacket) output).getSenderUsername(), ((UnicastMessagePacket) output).getRecipientUsername(), ((UnicastMessagePacket) output).getContent());
                    else if (output instanceof OnlineUsersPacket)
                        chatController.changeOnlineUserList(((OnlineUsersPacket) output).getUsers());
                    else if (output instanceof ErrorPacket)
                        Dialogs.showErrorDialog("Communication error", ((ErrorPacket) output).getErrorMessage());
                    else
                        System.out.println("Unknown message: " + line);
                } else {
                    break;
                }
            }
        }
    }

    public void close() {
        try {
            clientSupporter.closeSocket();
        } catch (Exception e) {
            System.out.println("Impossible close manually the connection...");
            System.exit(-1);
        }
    }
}
