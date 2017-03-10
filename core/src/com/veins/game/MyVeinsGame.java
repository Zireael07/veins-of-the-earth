package com.veins.game;

import com.badlogic.gdx.Game;
import com.veins.game.screens.GameScreen;
import com.veins.game.screens.MenuScreen;

public class MyVeinsGame extends Game {
        public Resources res;
	
	
	@Override
	public void create () {
                res = new Resources();
                setScreen(new MenuScreen(this));
                //setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose () {
		res.dispose();
	}
}
