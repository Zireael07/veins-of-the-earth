/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.veins.game.MyVeinsGame;

/**
 *
 * @author AdmKasia
 */
public class MenuScreen extends DefaultScreen {
    
    Skin skin;
    Stage stage;
    ScreenViewport viewport;
    
    SpriteBatch batch;
    float heightratio;
    float widthratio;
    
    public MenuScreen(MyVeinsGame _game) {
        super(_game);
        
            batch = new SpriteBatch();
            stage = new Stage();
            
            //set viewport
           viewport = new ScreenViewport();
           stage.setViewport(viewport);
           
           heightratio = Gdx.graphics.getHeight()/704f;
           Gdx.app.log("Background", "Height ratio is: " + heightratio + " window height is " + Gdx.graphics.getHeight());
           widthratio = 1088*heightratio;
           Gdx.app.log("Background", "Width ratio is: " + widthratio);
            
            Gdx.input.setInputProcessor(stage);
            
            VisUI.load();
            
            // Create a table that fills the screen. Everything else will go inside this table.
            Table table = new Table();
            table.setFillParent(true);
            stage.addActor(table);

            // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
            final VisTextButton button = new VisTextButton("New game");
            table.add(button);

            // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
            // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
            // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
            // revert the checked state.
            button.addListener(new ChangeListener() {
                    public void changed (ChangeEvent event, Actor actor) {
                            System.out.println("Clicked! Is checked: " + button.isChecked());
                            button.setText("Good job!");
                            //open game
                            game.setScreen(new GameScreen(game));
                            dispose();
                    }
            });

            // Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
           // table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);
           
           table.setDebug(true);
    }
    
        @Override
	public void render (float f) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                
                batch.begin();
                //show background picture resized to match window
                batch.draw(game.res.menu_bg_tex, 0, 0, widthratio, Gdx.graphics.getHeight());
                batch.end();
                
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose () {
		stage.dispose();
//		skin.dispose();
    }
}
