package com.fong.game.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.fong.game.gameobjects.Bullet;
import com.fong.game.gameobjects.ScrollHandler;
import com.fong.game.InputHelpers.AssetLoader;
import com.fong.game.gameobjects.ShootWeaponBall;
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
    private ArrayList<ArrayList<WeaponBall>> weaponList;
    private BitmapFont font;

    public GameRenderer(GameWorld world, int gameHeight, int midPointY) {

        this.myWorld = world;
        bullets = myWorld.bullets;

        cam = new OrthographicCamera();
        //first argument: whether the orthographic projection is used or not
        //what the width should be
        //what the height should be
        cam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batcher = new SpriteBatch();
        //Attach batcher to camera
        batcher.setProjectionMatrix(cam.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        font = new BitmapFont(true);
        initGameObjects();
        initAssets();

    }

    public void render(float runTime) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        weaponBalls = myWorld.weaponBalls;
        weaponList = myWorld.getWeaponList();
        //begin shapeRender
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Background Color
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 60 / 255.0f, 1);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //rotation circles
        shapeRenderer.setColor(153 / 255.0f, 153 / 255.0f, 153 / 255.0f, 0.6f);
        shapeRenderer.circle(10, GameWorld.gameHeight - 100, 100, 50);
        shapeRenderer.circle(10, GameWorld.gameHeight - 300, 100, 50);

        //bullet circle
        shapeRenderer.setColor(255 / 255.0f, 255 / 255.0f, 255 / 255.0f, 0.3f);
        shapeRenderer.circle(GameWorld.gameWidth-60, GameWorld.gameHeight - 60, 60, 20);

            if (!myWorld.enemies.isEmpty()) {
                for (int i = 0; i < myWorld.enemies.size(); i++) {
                    shapeRenderer.setColor(130 / 255.0f, 30 / 255.0f, 160 / 255.0f, 1);
                    shapeRenderer.rect(myWorld.enemies.get(i).getX(), myWorld.enemies.get(i).getY(), 20, 20);
                }
            }
            if (!weaponBalls.isEmpty()) {
                for (int i = 0; i < weaponBalls.size(); i++) {

                    switch (weaponBalls.get(i).getType()) {
                        case 0:
                            shapeRenderer.setColor(215 / 255.0f, 77 / 255.0f, 17 / 255.0f, 1);
                            break;
                        case 1:
                            shapeRenderer.setColor(0 / 255.0f, 204 / 255.0f, 0 / 255.0f, 1);
                            break;
                        case 2:
                            shapeRenderer.setColor(51 / 255.0f, 153 / 255.0f, 255 / 255.0f, 1);
                            break;
                        case 3:
                            shapeRenderer.setColor(204 / 255.0f, 0 / 255.0f, 102 / 255.0f, 1);
                            break;
                        case 4:
                            shapeRenderer.setColor(102 / 255.0f, 0 / 255.0f, 102 / 255.0f, 1);
                            break;
                        default:
                            break;

                    }
                    shapeRenderer.circle(weaponBalls.get(i).getX(), weaponBalls.get(i).getY(), 25);
                }
            }
        releaseWeaponBall();

        drawWeaponCircles();

            //weapon circles
            shapeRenderer.end();

            batcher.begin();
            batcher.enableBlending();

            for (int num = 0; num < weaponList.size(); num++) {
                font.draw(batcher, "x" + weaponList.get(num).size(), GameWorld.gameWidth-120, GameWorld.gameHeight-240-num * 120, 100, 100, false);
            }

        font.draw(batcher, GameWorld.score+"",40 , 40, 10, 4, true);

            batcher.draw(cursor, tilt.getX(),
                    tilt.getY(), 35,
                    35, 70, 70,
                    1, 1, tilt.getRotation());

            if (!bullets.isEmpty()) {
                for (int i = 0; i < bullets.size(); i++) {
                    batcher.draw(bullet, bullets.get(i).getX(), bullets.get(i).getY(), 10,10, 20,20, 1, 1, 0f);
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

    private void releaseWeaponBall(){
        for(int numBall = 0; numBall<myWorld.shootWeaponBallArrayList.size();numBall++){
            ShootWeaponBall ball = myWorld.shootWeaponBallArrayList.get(numBall);
            switch (ball.getType()){
                case 0:
                    shapeRenderer.setColor(215 / 255.0f, 77 / 255.0f, 17 / 255.0f, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0 / 255.0f, 204 / 255.0f, 0 / 255.0f, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(51 / 255.0f, 153 / 255.0f, 255 / 255.0f, 1);
                    break;
                case 3:
                    shapeRenderer.setColor(204 / 255.0f, 0 / 255.0f, 102 / 255.0f, 1);
                    break;
                case 4:
                    shapeRenderer.setColor(102 / 255.0f, 0 / 255.0f, 102 / 255.0f, 1);
                    break;
                default:
                    break;
            }
            shapeRenderer.circle(ball.getX(), ball.getY(), ball.getCircle().radius,40 );
        }
    }

    private void drawWeaponCircles(){

        for (int num = 0; num < weaponList.size(); num++) {
            if (!weaponList.get(num).isEmpty()) {

                switch (weaponList.get(num).get(0).getType()) {
                    case 0:
                        shapeRenderer.setColor(215 / 255.0f, 77 / 255.0f, 17 / 255.0f, 1);
                        break;
                    case 1:
                        shapeRenderer.setColor(0 / 255.0f, 204 / 255.0f, 0 / 255.0f, 1);
                        break;
                    case 2:
                        shapeRenderer.setColor(51 / 255.0f, 153 / 255.0f, 255 / 255.0f, 1);
                        break;
                    case 3:
                        shapeRenderer.setColor(204 / 255.0f, 0 / 255.0f, 102 / 255.0f, 1);
                        break;
                    case 4:
                        shapeRenderer.setColor(102 / 255.0f, 0 / 255.0f, 102 / 255.0f, 1);
                        break;
                    default:

                }
                switch (num) {
                    case 0:
                        shapeRenderer.circle(GameWorld.gameWidth-60, GameWorld.gameHeight - 180,60, 20);
                        break;
                    case 1:
                        shapeRenderer.circle(GameWorld.gameWidth-60, GameWorld.gameHeight - 300, 60, 20);
                        break;
                    case 2:
                        shapeRenderer.circle(GameWorld.gameWidth-60, GameWorld.gameHeight - 420, 60, 20);
                        break;
                    case 3:
                        shapeRenderer.circle(GameWorld.gameWidth-60, GameWorld.gameHeight - 540, 60, 20);
                        break;
                    case 4:
                        shapeRenderer.circle(GameWorld.gameWidth-60, GameWorld.gameHeight - 660, 60, 20);
                        break;
                    default:

                }

            }

        }
    }
}
