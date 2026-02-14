package com.maximo.flappybird.network;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer {

    private static final int PORT = 5000;
    private static final int MAX_PLAYERS = 2;

    private List<PlayerConnection> players = new ArrayList<>();

    public void start() {

        Thread thread = new Thread(() -> {

            try {
                DatagramSocket socket = new DatagramSocket(PORT);
                byte[] buffer = new byte[1024];

                while (true) {

                    DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);

                    socket.receive(packet);

                    String message =
                        new String(packet.getData(), 0, packet.getLength());

                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();

                    PlayerConnection player =
                        getOrCreatePlayer(address, port);

                    String[] parts = message.split("\\|");

                    player.y = Integer.parseInt(parts[0].split(":")[1]);
                    player.alive = parts[1].split(":")[1].equals("1");
                    player.score = Integer.parseInt(parts[2].split(":")[1]);

                    sendGameState(socket);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }


    private PlayerConnection getOrCreatePlayer(InetAddress address, int port) {

        for (PlayerConnection p : players) {
            if (p.address.equals(address) && p.port == port) {
                return p;
            }
        }

        if (players.size() >= MAX_PLAYERS) {
            return players.get(0);
        }

        String color = players.size() == 0 ? "BLUE" : "RED";

        PlayerConnection newPlayer =
            new PlayerConnection(address, port, color);

        players.add(newPlayer);

        System.out.println("Jugador conectado con color: " + color);

        return newPlayer;
    }

    private void sendGameState(DatagramSocket socket) throws Exception {

        boolean gameReady = players.size() == 2;

        StringBuilder data = new StringBuilder();
        data.append("START:").append(gameReady ? "1" : "0").append("|");

        for (int i = 0; i < players.size(); i++) {

            PlayerConnection p = players.get(i);

            data.append("P").append(i)
                .append(":")
                .append(p.y).append(",")
                .append(p.alive ? "1" : "0").append(",")
                .append(p.score).append(",")
                .append(p.color)
                .append("|");
        }

        byte[] sendData = data.toString().getBytes();

        for (PlayerConnection p : players) {

            DatagramPacket packet =
                new DatagramPacket(sendData, sendData.length, p.address, p.port);

            socket.send(packet);
        }
    }
    public int getPlayerCount() {
        return players.size();
    }

}
