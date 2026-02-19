package com.maximo.flappybird.network;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer {

    private static final int PORT = 5000;
    private static final int MAX_PLAYERS = 2;
    //Lista de jugadores conectados en el server
    private List<PlayerConnection> players = new ArrayList<>();

    public void start() {
        //Se corre en hilos independientes, esto para que el renderizado de la interfaz no de bloquee
        Thread thread = new Thread(() -> {

            try {
                //Abre un socket en el puerto 5000 declarado arriba
                DatagramSocket socket = new DatagramSocket(PORT);
                byte[] buffer = new byte[1024];

                while (true) {
                    //queda escuchando, hasta que recibe un paquete lo recible en DatagramPacket
                    DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);
                    //Y aca "traduce" el paquete a un mensaje para que se entienda
                    socket.receive(packet);

                    String message =
                        new String(packet.getData(), 0, packet.getLength());
                    //Aca identifica a cada cliente por la IP y puerto de onexion
                    InetAddress address = packet.getAddress();
                    //y el puerto lo guarda aca
                    int port = packet.getPort();

                    PlayerConnection player =
                        //Aca actualiza el PlayerConnection
                        getOrCreatePlayer(address, port);
                    /*Aca el servidor recibe el estado, posicion y score del cliente, y entoncecs
                    * se lo pasa al otro cliente, para que este al tanto de lo mismo y pueda
                    * tambien ver al jugador, de esta manera el servidor muestra en la pantalla de los
                    * clientes al otro cliente*/
                    if (message.contains(",")) {

                        String[] parts = message.split(",");

                        player.y = Integer.parseInt(parts[0]);
                        player.alive = parts[1].equals("1");
                        player.score = Integer.parseInt(parts[2]);
                    }


                    sendGameState(socket);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    /*aca lo que hago es verificar si el cliente ya se encuentra en la
    lista o no, para agregarlo o dejarlo, y si hay espacio (deben se maximo 2)*/
    private PlayerConnection getOrCreatePlayer(InetAddress address, int port) {

        for (PlayerConnection p : players) {
            if (p.address.equals(address) && p.port == port) {
                return p;
            }
        }

        if (players.size() >= MAX_PLAYERS) {
            return players.get(0);
        }
        //Aca aclaro que el primero en conectarse es azul y el segundo es rojo
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
        //Aca el server envia el estado global de cada cliente
        for (PlayerConnection p : players) {

            String personalData = "YOU:" + p.color + "|" + data.toString();
            byte[] sendData = personalData.getBytes();

            DatagramPacket packet =
                new DatagramPacket(sendData, sendData.length, p.address, p.port);

            socket.send(packet);
        }
    }



    public int getPlayerCount() {
        return players.size();
    }

}
