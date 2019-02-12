package com.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.game.Adventure;
import com.game.screens.PlayScreen;
import com.game.sprites.Detlef;
import com.game.sprites.enemies.Enemy;
import com.game.sprites.tileObjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA=contact.getFixtureA();
        Fixture fixB=contact.getFixtureB();

        int cDef=fixA.getFilterData().categoryBits|fixB.getFilterData().categoryBits;

        if(fixA.getUserData()=="head" || fixB.getUserData()=="head"){
            Fixture head=fixA.getUserData()=="head" ? fixA : fixB;
            Fixture object=head ==fixA ? fixB :fixA;

            if(object.getUserData()!= null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        switch (cDef){
            case Adventure.ENEMY_HEAD_BIT | Adventure.DETLEF_BIT:
                if(fixA.getFilterData().categoryBits==Adventure.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case Adventure.ENEMY_BIT|Adventure.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits==Adventure.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelociy(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelociy(true,false);
                break;
            case Adventure.DETLEF_BIT|Adventure.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits==Adventure.DETLEF_BIT)
                {
                    ((Detlef)fixA.getUserData()).hit();
                }
                else
                {
                    ((Detlef)fixB.getUserData()).hit();
                }
                break;
            case Adventure.DETLEF_BIT|Adventure.VICTORY_BIT:
                    PlayScreen.victory=true;
                break;
            case Adventure.ENEMY_BIT|Adventure.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelociy(true,false);
                ((Enemy)fixB.getUserData()).reverseVelociy(true,false);
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
