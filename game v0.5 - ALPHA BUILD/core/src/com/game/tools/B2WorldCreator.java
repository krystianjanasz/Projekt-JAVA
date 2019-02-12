package com.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.game.Adventure;
import com.game.screens.PlayScreen;
import com.game.sprites.enemies.Bandit;
import com.game.sprites.enemies.Enemy;
import com.game.sprites.tileObjects.Brick;
import com.game.sprites.tileObjects.Coin;
import com.game.sprites.enemies.Slime;

public class B2WorldCreator {
    private Array<Slime> slimes;
    private Array<Bandit> bandits;

    public B2WorldCreator(PlayScreen screen) {

        World world=screen.getWorld();
        TiledMap map=screen.getMap();
        BodyDef bdef=new BodyDef();
        PolygonShape shape=new PolygonShape();
        FixtureDef fdef= new FixtureDef();
        Body body;

        for(MapObject object:map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject)object).getRectangle();

            bdef.type=BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ Adventure.PPM,(rect.getY()+rect.getHeight()/2)/Adventure.PPM);
            body=world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/Adventure.PPM,(rect.getHeight()/2)/Adventure.PPM);
            fdef.shape=shape;
            body.createFixture(fdef);
        }

        for(MapObject object:map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect=((RectangleMapObject)object).getRectangle();

            bdef.type=BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/Adventure.PPM,(rect.getY()+rect.getHeight()/2)/Adventure.PPM);
            body=world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/Adventure.PPM,(rect.getHeight()/2)/Adventure.PPM);
            fdef.shape=shape;
            fdef.filter.categoryBits=Adventure.OBJECT_BIT;
            body.createFixture(fdef);
        }

        for(MapObject object:map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect=((RectangleMapObject)object).getRectangle();
            new Coin(screen,rect);
        }
        for(MapObject object:map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect=((RectangleMapObject)object).getRectangle();
            new Brick(screen,rect);
        }

        slimes=new Array<Slime>();
        for(MapObject object:map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect=((RectangleMapObject)object).getRectangle();
            slimes.add(new Slime(screen,rect.getX()/Adventure.PPM,rect.getY()/Adventure.PPM));
        }
        bandits=new Array<Bandit>();
        for(MapObject object:map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect=((RectangleMapObject)object).getRectangle();
            bandits.add(new Bandit(screen,rect.getX()/Adventure.PPM,rect.getY()/Adventure.PPM));
        }
        for(MapObject object:map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect=((RectangleMapObject)object).getRectangle();
            bdef.type=BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/Adventure.PPM,(rect.getY()+rect.getHeight()/2)/Adventure.PPM);
            body=world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/Adventure.PPM,(rect.getHeight()/2)/Adventure.PPM);
            fdef.shape=shape;
            fdef.filter.categoryBits=Adventure.VICTORY_BIT;
            body.createFixture(fdef);
        }

    }

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies=new Array<Enemy>();
        enemies.addAll(slimes);
        enemies.addAll(bandits);
        return enemies;
    }
}
