/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.ui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.veins.game.systems.InventorySystem;

/**
 *
 * @author AdmKasia
 */
public class InventorySlotButton extends VisImageButton {
    
    final Stage stage_ref;
    final Entity player_ref;
    final Entity item_ref;
    String slot;
    Engine engine_ref;
    
    public InventorySlotButton(Engine engine, Stage stage, Entity player, Entity item, Drawable imageUp, String tooltipText) {
        super(imageUp, tooltipText);
        this.slot = tooltipText;
        stage_ref = stage;
        engine_ref = engine;
        player_ref = player;
        item_ref = item;
        
        this.addListener(new ChangeListener() 
        {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log("Button", "Clicked the button");
                Gdx.app.log("Button", "Our slot is " + slot);
                
                if (item_ref != null)
                {
                    //create a menu
                    final VisDialog menu_window = new VisDialog("Item menu");
                    VisTextButton wear_button = new VisTextButton("Wear");
                    wear_button.addListener(new ChangeListener()
                    {
                        @Override
                        public void changed (ChangeEvent event, Actor actor) {
                        Gdx.app.log("Button", "Clicked the inner button");
                        menu_window.hide();
                        //try to wear the item

                        engine_ref.getSystem(InventorySystem.class).doWear(player_ref, slot, item_ref);
                        }
                    });
                
                            
                    //Gdx.app.log("Item menu", "Item menu pos " + x + " ," + y);
                    menu_window.setPosition(50, 50);
                    menu_window.add(wear_button).row();
                    menu_window.pack();
                    menu_window.setModal(true);
                    menu_window.closeOnEscape();

                    stage_ref.addActor(menu_window);
                }//end of if item
            }
        });
    }
    
}
