package com.maximo.flappybird.network;

import java.net.InetAddress;

public class PlayerConnection {

    public InetAddress address;
    public int port;

    public boolean alive = true;
    public int y = 300;
    public int score = 0;


    public String color;

    public PlayerConnection(InetAddress address, int port, String color) {
        this.address = address;
        this.port = port;
        this.color = color;
    }
}
