package com.fong.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.fong.game.gameworld.GameRenderer;
import com.fong.game.gameworld.GameWorld;
import com.fong.game.InputHelpers.InputHandler;

/**
 * Created by wing on 6/3/15.
 */
public class GameScreen implements Screen {
    private GameWorld world;
    private GameRenderer renderer;

    private float runTime = 0;
    private static final String TAG = "GameScreen";

    public GameScreen(){

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float gameWidth = 136;
        float gameHeight = screenHeight/(screenWidth/gameWidth);

        int midPointY = (int)gameHeight/2;

        world = new GameWorld(midPointY);
        renderer = new GameRenderer(world, (int)gameHeight, midPointY);

        Gdx.input.setInputProcessor(new InputHandler(world));

    }
    @Override
    public void show() {
        Gdx.app.log(TAG, "show called");
    }

    @Override
    public void render(float delta) {
        delta/=2;
        runTime += delta;
        world.update(delta);
        renderer.render(runTime);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resizing");
    }

    @Override
    public void pause() {
        Gdx.app.log(TAG, "pause called");

    }

    @Override
    public void resume() {
        Gdx.app.log(TAG, "resume called");
    }

    @Override
    public void hide() {
        Gdx.app.log(TAG, "hide called");
    }

    @Override
    public void dispose() {

    }
}
