package com.maximo.flappybird.network;

import java.net.InetAddress;

public class PlayerConnection {

    public InetAddress address;
    public int port;

    public int y;
    public boolean alive;
    public int score;

    public String color;

    public PlayerConnection(InetAddress address, int port, String color) {
        this.address = address;
        this.port = port;
        this.color = color;
    }
}
