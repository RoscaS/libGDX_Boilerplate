package com.modular.components;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.modular.Static.Store;
import com.modular.base.CoreEntity;

public class SelectionComponent {

    protected CoreEntity entity;

	/*------------------------------------------------------------------*\
	|*							Constructors						  *|
	\*------------------------------------------------------------------*/

    public SelectionComponent(CoreEntity entity) {
        this.entity = entity;

        inputHandler();
    }

	/*------------------------------------------------------------------*\
	|*							Private Methods 				      *|
	\*------------------------------------------------------------------*/

    private void inputHandler() {
        entity.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (button == Input.Buttons.LEFT && !Store.selected.contains(entity, false)) {
                    Store.selected.add(entity);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}
