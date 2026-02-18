package com.maximo.flappybird;

import com.maximo.flappybird.network.GameServer;

import javax.swing.*;
import java.awt.*;

public class ServerLauncher {

    private static JLabel statusLabel;

    public static void main(String[] args) {

        GameServer server = new GameServer();
        server.start();

        // Crear ventana
        JFrame frame = new JFrame("Servidor Flappy Bird");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        statusLabel = new JLabel("Jugadores conectados: 0 de 2", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));

        frame.add(statusLabel);
        frame.setVisible(true);

        // Hilo que actualiza el contador cada 500ms
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);

                    int count = server.getPlayerCount();

                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText(
                            "Jugadores conectados: " + count + " de 2"
                        );
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
