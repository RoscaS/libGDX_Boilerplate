package com.modular.player;

import com.badlogic.gdx.utils.Array;
import com.modular.entities.base.CoreEntity;

public class Player {

    private String name;
    private Array<CoreEntity> selection;

	/*------------------------------------------------------------------*\
	|*							Constructors						    *|
	\*------------------------------------------------------------------*/

    public Player(String name) {
        this.name = name;
        selection = new Array<>();
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 						    *|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Getters		    *|
	\*------------------------------*/

    public String getName() {
        return name;
    }

    public Array<CoreEntity> getSelection() {
        return selection;
    }

	/*------------------------------*\
	|*				Setters		    *|
	\*------------------------------*/

    public void setName(String name) {
        this.name = name;
    }
}
