package com.maximo.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.maximo.flappybird.network.GameServer;

public class ServerMain extends ApplicationAdapter {

    private SpriteBatch batch;
    private BitmapFont font;
    private GameServer server;

    @Override
    public void create() {

        batch = new SpriteBatch();
        font = new BitmapFont();

        server = new GameServer();
        server.start();
    }

    @Override
    public void render() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font.draw(batch, "FLAPPY BIRD SERVER", 200, 400);
        font.draw(batch, "Puerto: 5000", 200, 370);

        int players = server.getPlayerCount();

        if (players < 2) {
            font.draw(batch, "Esperando jugadores...", 200, 330);
        } else {
            font.draw(batch, "2 jugadores conectados", 200, 330);
            font.draw(batch, "Juego en curso", 200, 300);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
