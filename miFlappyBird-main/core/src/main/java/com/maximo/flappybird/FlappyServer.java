package com.maximo.flappybird;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FlappyServer {

    private static Socket player1;
    private static Socket player2;

    private static PrintWriter out1;
    private static PrintWriter out2;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(9999);

            System.out.println("Servidor iniciado en puerto 9999");
            System.out.println("Esperando jugadores...");

            player1 = serverSocket.accept();
            out1 = new PrintWriter(player1.getOutputStream(), true);
            System.out.println("Jugador 1 conectado");

            player2 = serverSocket.accept();
            out2 = new PrintWriter(player2.getOutputStream(), true);
            System.out.println("Jugador 2 conectado");

            out1.println("START");
            out2.println("START");

            new Thread(() -> listen(player1, out2)).start();
            new Thread(() -> listen(player2, out1)).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listen(Socket player, PrintWriter otherOut) {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(player.getInputStream()));

            String message;

            while ((message = in.readLine()) != null) {

                if (message.startsWith("PLAYER:")) {
                    otherOut.println(message);
                }

                if (message.equals("GAMEOVER")) {
                    otherOut.println("GAMEOVER");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
