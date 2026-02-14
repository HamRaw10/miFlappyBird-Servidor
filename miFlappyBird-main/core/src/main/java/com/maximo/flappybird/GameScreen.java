package com.maximo.flappybird;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.maximo.flappybird.states.PlayState;
import com.maximo.flappybird.states.MenuState;
import com.maximo.flappybird.states.GameOverState;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.maximo.flappybird.utils.Constants;


public class GameScreen implements Screen {

    private Main game;

    private MenuState menuState;
    private PlayState playState;
    private GameOverState gameOverState;
    private OrthographicCamera camera;
    private Viewport viewport;
    private boolean gameOver;


    private boolean playing;

    public GameScreen(Main game) {

        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        viewport.apply();

        camera.position.set(Constants.WORLD_WIDTH / 2f,
            Constants.WORLD_HEIGHT / 2f,
            0);
        camera.update();

        menuState = new MenuState();

        playing = false;
        gameOver = false;
    }



    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();

        if(!playing && !gameOver) {

            menuState.update();
            menuState.render(game.batch);

            if(menuState.shouldStartGame()) {

                playState = new PlayState();
                playing = true;
            }
        }
        else if(playing) {

            playState.update(delta);
            playState.render(game.batch);

            if(playState.isGameOver()) {

                gameOverState = new GameOverState(playState.getScore());

                playing = false;
                gameOver = true;
            }
        }
        else if(gameOver) {

            gameOverState.update();
            gameOverState.render(game.batch);

            if(gameOverState.shouldRestart()) {

                menuState = new MenuState();
                gameOver = false;
            }
        }

        game.batch.end();
    }


    @Override public void show() {}
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {

        menuState.dispose();

        if(playState != null)
            playState.dispose();
    }
}
