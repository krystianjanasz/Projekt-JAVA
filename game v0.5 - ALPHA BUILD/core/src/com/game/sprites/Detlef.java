package com.game.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.game.Adventure;
import com.game.screens.PlayScreen;

public class Detlef extends Sprite {
    public enum State{FALLING,JUMPING,STANDING,RUNNING,DEAD};
    public State currentState;
    public State previouState;
    public World world;
    public Body b2body;

    private TextureRegion DetlefStand;
    private TextureRegion DetlefDead;
    private TextureRegion DetlafFalling;

    private Animation DetlefRun;
    private Animation DetlefJump;

    private float stateTimer;
    private boolean runningRight;
    private boolean detlefIsDead;

    public Detlef(PlayScreen screen){
        super(screen.getAtlas().findRegion("adventurer"));
        this.world=screen.getWorld();
        currentState=State.STANDING;
        previouState=State.STANDING;
        stateTimer=0;
        runningRight=true;

        Array<TextureRegion> frames=new Array<TextureRegion>();
        for(int i=1; i<6;i++){
            frames.add(new TextureRegion(getTexture(),i*23,0,23,29));
        }
        DetlefRun=new Animation(0.1f,frames);
        frames.clear();

        for(int i=8;i<11;i++){
            frames.add(new TextureRegion(getTexture(),i*23,0,23,29));
        }
        DetlefJump=new Animation(0.1f,frames);

        defineCharacter();
        DetlefStand=new TextureRegion(getTexture(),0,0,23,29);
        DetlefDead=new TextureRegion(getTexture(),184,0,23,29);
        DetlafFalling=new TextureRegion(getTexture(),253,0,23,29);
        setBounds(0,0,21/Adventure.PPM,32/Adventure.PPM);
        setRegion(DetlefStand);
    }

    public void update(float delta){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta){
        currentState=getState();

        TextureRegion region;
        switch (currentState){
            case DEAD:
                region=DetlefDead;
                break;
            case JUMPING:
                region= (TextureRegion) DetlefJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region= (TextureRegion) DetlefRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
                region=DetlafFalling;
                break;
            case STANDING:
                default:
                    region=DetlefStand;
                    break;
        }
        if((b2body.getLinearVelocity().x<0 || !runningRight)&& !region.isFlipX()){
            region.flip(true,false);
            runningRight=false;
        }
        else if((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight=true;
        }

        stateTimer=currentState==previouState ? stateTimer + delta: 0;
        previouState=currentState;
        return region;
    }

    public State getState(){
        if(detlefIsDead)
            return State.DEAD;
        else if(b2body.getLinearVelocity().y>0 || (b2body.getLinearVelocity().y<0 && previouState== State.JUMPING)){
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y<0){
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x!=0){
            return State.RUNNING;
        }
        else{
            return State.STANDING;
        }
    }

    public void defineCharacter(){
        BodyDef bdef=new BodyDef();
        bdef.position.set(700/ Adventure.PPM,32/Adventure.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body=world.createBody(bdef);

        FixtureDef fdef=new FixtureDef();
        PolygonShape shape=new PolygonShape();

        Vector2[] vertice=new Vector2[4];
        vertice[0]=new Vector2(-9,15).scl(1/Adventure.PPM);
        vertice[1]=new Vector2(9,15).scl(1/Adventure.PPM);
        vertice[2]=new Vector2(-9,-13).scl(1/Adventure.PPM);
        vertice[3]=new Vector2(9,0-13).scl(1/Adventure.PPM);
        shape.set(vertice);

        fdef.filter.categoryBits=Adventure.DETLEF_BIT;
        fdef.filter.maskBits=Adventure.GROUND_BIT |
                Adventure.BRICK_BIT |
                Adventure.COIN_BIT|
                Adventure.OBJECT_BIT|
                Adventure.ENEMY_BIT|
                Adventure.ENEMY_HEAD_BIT|
                Adventure.VICTORY_BIT;

        fdef.shape=shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head=new EdgeShape();
        head.set(new Vector2(-4/Adventure.PPM,15/Adventure.PPM),new Vector2(4/Adventure.PPM,15/Adventure.PPM));
        fdef.shape=head;

        fdef.isSensor=true;
        b2body.createFixture(fdef).setUserData("head");
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void hit(){
        Adventure.manager.get("audio/sounds/died.mp3", Sound.class).play();
        detlefIsDead=true;
        Filter filter=new Filter();
        filter.maskBits=Adventure.NOTHING_BIT;
        for(Fixture fixture:b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0,4f),b2body.getWorldCenter(),true);
    }
}
