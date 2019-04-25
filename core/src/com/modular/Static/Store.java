package com.modular.Static;

import com.badlogic.gdx.utils.Array;
import com.modular.entities.base.CoreEntity;
import com.modular.map.TilemapEntities;
import com.modular.player.Player;

public abstract class Store {

    public static Array<CoreEntity> selected = new Array<>();

    public static Array<Player> players = new Array<>();

    public static TilemapEntities map;

    /*------------------------------------------------------------------*\
	|*							Getters         						*|
	\*------------------------------------------------------------------*/

    public static Player getPlayer(String playerName) {

        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        // Terrible idea but will do for now
        return players.get(0);
    }
}
