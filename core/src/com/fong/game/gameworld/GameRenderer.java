package com.fong.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.fong.game.gameobjects.Bullet;
import com.fong.game.gameobjects.ScrollHandler;
import com.fong.game.InputHelpers.AssetLoader;
import com.fong.game.gameobjects.Tilt;
import com.fong.game.gameobjects.Weapon;
import com.fong.game.gameobjects.WeaponBall;

import java.util.ArrayList;

/**
 * Created by wing on 6/4/15.
 */
public class GameRenderer {

    private GameWorld myWorld;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera cam;
    private SpriteBatch batcher;

    private Tilt tilt;

    private TextureRegion bg, grass;
    private Animation birdAnimation;
    private TextureRegion cursor,bullet;
    private ScrollHandler scroller;

    private ArrayList<Bullet> bullets;
    private ArrayList<WeaponBall> weaponBalls;
    private int gameHeight, midPointY;

    public GameRenderer(GameWorld world, int gameHeight, int midPointY) {

        this.myWorld = world;
        bullets = myWorld.bullets;

        cam = new OrthographicCamera();
        //first argument: whether the orthographic projection is used or not
        //what the width should be
        //what the height should be
        cam.setToOrtho(true, 136, gameHeight);

        batcher = new SpriteBatch();
        //Attach batcher to camera
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        initGameObjects();
        initAssets();

    }

    public void render(float runTime) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        weaponBalls = myWorld.weaponBalls;
        //begin shapeRender
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Background Color
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //input circles
        shapeRenderer.setColor(153/255.0f, 153/255.0f, 153/255.0f, 0.6f);
        shapeRenderer.circle(10, GameWorld.gameHeight - 10, 10, 50);
        shapeRenderer.circle(10, GameWorld.gameHeight-30, 10,50);
        shapeRenderer.setColor(255/255.0f, 255/255.0f, 255/255.0f, 0.3f);
        shapeRenderer.circle(GameWorld.gameWidth-7, GameWorld.gameHeight-7, 7, 20);


        if(!myWorld.enemies.isEmpty()){
            for(int i = 0;i < myWorld.enemies.size();i++) {
                shapeRenderer.setColor(130 / 255.0f, 30 / 255.0f, 160 / 255.0f, 1);
                shapeRenderer.rect(myWorld.enemies.get(i).getX(), myWorld.enemies.get(i).getY(), 3,3);
            }
        }
        if(!weaponBalls.isEmpty()){
            for(int i = 0; i<weaponBalls.size();i++){
                shapeRenderer.setColor(215/255.0f, 77/255.0f, 17/255.0f, 1);
                shapeRenderer.circle(weaponBalls.get(i).getX(), weaponBalls.get(i).getY(), 3);
            }
        }
        shapeRenderer.end();
        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(241 / 255.0f, 131 / 255.0f, 30 / 255.0f, 1);
        shapeRenderer.rect(tilt.getX(), tilt.getY(), 20, 20);
        //Gdx.app.log("Game Renderer", tilt.getX()+" "+ tilt.getY());
        */
        shapeRenderer.end();


        batcher.begin();
        batcher.enableBlending();
        batcher.draw(cursor, tilt.getX(),
                tilt.getY(), 4,
                4, 8, 8,
                1, 1, tilt.getRotation());

        if(!bullets.isEmpty()){
            for(int i = 0; i<bullets.size(); i++){

                batcher.draw(bullet, bullets.get(i).getX(), bullets.get(i).getY(), 1.5f, 1.5f, 3, 3, 1, 1, 0f);
            }
        }
        batcher.end();


    }

    private void initGameObjects(){
        tilt = myWorld.getTilt();
    }

    private void initAssets(){
        //bg = AssetLoader.bg;
        cursor = AssetLoader.cursor;
        bullet = AssetLoader.bullet;
    }
}