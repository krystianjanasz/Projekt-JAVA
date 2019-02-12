package com.game.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.game.Adventure;
import com.game.scenes.Hud;
import com.game.screens.PlayScreen;

public class Bandit extends Enemy{

    public float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Bandit(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames=new Array<TextureRegion>();
        for(int i=0; i<5;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("bandit"),i*23,0,23,29));
        }
        walkAnimation=new Animation(0.2f,frames);

        setBounds(0,0,21/ Adventure.PPM,32/Adventure.PPM);
        velocity.x=1;

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef=new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();

        PolygonShape shape=new PolygonShape();

        Vector2[] vertice=new Vector2[4];
        vertice[0]=new Vector2(-9,13).scl(1/Adventure.PPM);
        vertice[1]=new Vector2(9,13).scl(1/Adventure.PPM);
        vertice[2]=new Vector2(-9,-13).scl(1/Adventure.PPM);
        vertice[3]=new Vector2(9,0-13).scl(1/Adventure.PPM);
        shape.set(vertice);

        fdef.filter.categoryBits=Adventure.ENEMY_BIT;
        fdef.filter.maskBits=Adventure.GROUND_BIT|
                Adventure.BRICK_BIT|
                Adventure.COIN_BIT|
                Adventure.ENEMY_BIT|
                Adventure.OBJECT_BIT|
                Adventure.DETLEF_BIT;

        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head=new PolygonShape();
        vertice[0]=new Vector2(-9,16).scl(1/Adventure.PPM);
        vertice[1]=new Vector2(9,16).scl(1/Adventure.PPM);
        vertice[2]=new Vector2(-7,5).scl(1/Adventure.PPM);
        vertice[3]=new Vector2(7,5).scl(1/Adventure.PPM);
        head.set(vertice);

        fdef.shape=head;
        fdef.restitution=0.5f;
        fdef.filter.categoryBits=Adventure.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float delta) {
        TextureRegion region=(TextureRegion) walkAnimation.getKeyFrame(stateTime,true);
        if(velocity.x>0 && region.isFlipX()==true){
            region.flip(true,false);
        }
        else if(velocity.x<0 && region.isFlipX()==false){
            region.flip(true,false);
        }

        return region;
    }

    @Override
    public void update(float delta) {
        stateTime+=delta;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed=true;
            setRegion((TextureRegion)walkAnimation.getKeyFrame(0));
            stateTime=0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(delta));
        }
    }

    public void draw(Batch batch){
        if(!destroyed|| stateTime<1){
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        setToDestroy=true;
        Hud.addScore(200);
        Adventure.manager.get("audio/sounds/stomp.mp3", Sound.class).play();
    }
}
