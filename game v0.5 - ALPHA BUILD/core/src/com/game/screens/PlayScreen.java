package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.Adventure;
import com.game.scenes.Hud;
import com.game.sprites.Detlef;
import com.game.sprites.enemies.Enemy;
import com.game.tools.B2WorldCreator;
import com.game.tools.WorldContactListener;

public class PlayScreen implements Screen {

    private Adventure game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Detlef player;

    private Music music;

    public static boolean victory=false;

    public PlayScreen(Adventure game){
        atlas =new TextureAtlas("Detlef_and_enemies.pack");

        this.game=game;
        gamecam=new OrthographicCamera();
        gamePort=new FitViewport(Adventure.V_WIDTH/Adventure.PPM,Adventure.V_HEIGHT/Adventure.PPM,gamecam);
        hud=new Hud(game.batch);

        mapLoader=new TmxMapLoader();
        map=mapLoader.load("Level_One_2.0.tmx");
        renderer=new OrthogonalTiledMapRenderer(map,1/Adventure.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);

        world=new World(new Vector2(0,-10),true);
        b2dr=new Box2DDebugRenderer();

        creator=new B2WorldCreator(this);

        player=new Detlef(this);

        world.setContactListener(new WorldContactListener());

        music=Adventure.manager.get("audio/music/music.ogg",Music.class);
        music.setLooping(true);
        music.play();

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void show() {

    }

    public void handleInput(float delta)
    {
        if(player.currentState!=Detlef.State.DEAD)
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.b2body.getLinearVelocity().y==0){
                player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);

            }
            if(Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x<=2)
            {
                player.b2body.applyLinearImpulse(new Vector2(0.09f,0),player.b2body.getWorldCenter(),true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x>=-2)
            {
                player.b2body.applyLinearImpulse(new Vector2(-0.09f,0),player.b2body.getWorldCenter(),true);
            }
        }

    }

    public void update(float delta)
    {
        handleInput(delta);

        world.step(1/60f,6,2);

        player.update(delta);
        for(Enemy enemy:creator.getEnemies())
        {
            enemy.update(delta);
            if(enemy.getX() <player.getX()+224/Adventure.PPM){
                enemy.b2body.setActive(true);
            }
        }

        hud.update(delta);

        if(player.currentState!=Detlef.State.DEAD)
        {
            gamecam.position.x=player.b2body.getPosition().x;
        }

        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        //b2dr.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy:creator.getEnemies())
            enemy.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()){
            Adventure.manager.get("audio/music/music.ogg", Music.class).stop();
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if(victory){
            Adventure.manager.get("audio/music/music.ogg", Music.class).stop();
            game.setScreen(new VictoryScreen(game));
            dispose();
        }
    }

    public boolean gameOver(){
        if(player.currentState== Detlef.State.DEAD && player.getStateTimer()>2 || player.getY()<-20 || Hud.getWorldTimer()==0){
            return true;
        }
        else
        {
            return false;
        }
    }


    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        //b2dr.dispose();
        hud.dispose();
    }
}
