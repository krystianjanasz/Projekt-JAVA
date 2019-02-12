package com.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.Adventure;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    public static Integer worldTimer;
    private float timeCout;
    private static Integer score;

    private Label coutdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label characterLabel;

    public Hud(SpriteBatch sb)
    {
        worldTimer =300;
        timeCout=0;
        score=0;

        viewport=new FitViewport(Adventure.V_WIDTH,Adventure.V_HEIGHT,new OrthographicCamera());
        stage=new Stage(viewport,sb);

        Table table=new Table();
        table.top();
        table.setFillParent(true);

        coutdownLabel=new Label(Integer.toString(worldTimer),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel=new Label(Integer.toString(score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel=new Label("TIME",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel=new Label("1",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel=new Label("WORLD",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        characterLabel=new Label("DETLEF",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(characterLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(coutdownLabel).expandX();

        stage.addActor(table);
    }

    public void update(float delta){
        timeCout+=delta;
        if(timeCout>=1){
            worldTimer--;
            coutdownLabel.setText(Integer.toString(worldTimer));
            timeCout=0;
        }
    }

    public static Integer getWorldTimer(){
        return worldTimer;
    }

    public static void addScore(int value){
        score+=value;
        scoreLabel.setText(Integer.toString(score));
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
