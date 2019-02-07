package client.handlers;

import client.controller.ChatController;
import server.entities.packets.AccessResultPacket;
import server.entities.packets.MulticastMessagePacket;
import server.entities.packets.Packet;
import server.entities.packets.UnicastMessagePacket;
import server.packets.PacketsDecoder;

import java.io.IOException;

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (line != null) {
                    System.out.println(line);
                    Packet output = packetsDecoder.decode(line);
                    System.out.println(output instanceof AccessResultPacket);
                    if (output instanceof MulticastMessagePacket)
                        chatController.showMessage(((MulticastMessagePacket) output).getSenderUsername(), null, ((MulticastMessagePacket) output).getContent());
                    else if (output instanceof UnicastMessagePacket)
                        chatController.showMessage(((UnicastMessagePacket) output).getSenderUsername(), ((UnicastMessagePacket) output).getRecipientUsername(), ((UnicastMessagePacket) output).getContent());
                    else
                        System.out.println(line);
                } else {
                    break;
                }
            }
        }
    }
}
