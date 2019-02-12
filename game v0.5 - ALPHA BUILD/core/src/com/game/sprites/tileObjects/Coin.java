package com.game.sprites.tileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.game.Adventure;
import com.game.scenes.Hud;
import com.game.screens.PlayScreen;

public class Coin extends InteractiveTileObject{

    private final TiledMapTileSet tileset;
    private final int BLANK_COIN=449;

    public Coin(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        tileset=map.getTileSets().getTileSet("tileset");
        fixture.setUserData(this);
        setCategoryFilter(Adventure.COIN_BIT);
    }

    @Override
    public void onHeadHit() {

        if(getCell().getTile().getId()==BLANK_COIN){
            Adventure.manager.get("audio/sounds/blank_coin.mp3", Sound.class).play();
        }else {
            Adventure.manager.get("audio/sounds/coin.mp3", Sound.class).play();
            getCell().setTile(tileset.getTile(BLANK_COIN));
            Hud.addScore(300);
        }
    }
}
