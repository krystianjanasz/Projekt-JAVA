package com.game.sprites.tileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.game.Adventure;
import com.game.scenes.Hud;
import com.game.screens.PlayScreen;

public class Brick extends InteractiveTileObject{
    public Brick(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(Adventure.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(Adventure.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(50);
        Adventure.manager.get("audio/sounds/brick_hit.mp3", Sound.class).play();
    }
}