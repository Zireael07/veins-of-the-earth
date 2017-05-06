/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.ui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.veins.game.MyVeinsGame;
import com.veins.game.components.InventoryComponent;
import java.util.ArrayList;

/**
 *
 * @author AdmKasia
 */
public class InventoryWindow extends VisWindow {
    GridGroup group_eq;
    GridGroup group_inv;
    MyVeinsGame _game;
    Engine _engine;
    Stage _stage;
    
    public InventoryWindow(Stage stage, Entity player, MyVeinsGame game, Engine engine) {
        super("Inventory");
        
        _game = game;
        _engine = engine;
        _stage = stage;
        
        addWidgets(player, _stage);
        //pack();
    }
    
    public void addWidgets(Entity player, Stage stage){
        ArrayList<String> slots = player.getComponent(InventoryComponent.class).slots;
        int slot_w = 42;
        int spacing = 4;
        group_eq = new GridGroup(slot_w, spacing);
        group_inv = new GridGroup(slot_w, spacing);  //item size 42 px, spacing 4px
        
        //set size for entire equipment grid
        int numGridWeq = 4;
        int gridWidthEq = (slot_w+spacing)*numGridWeq;
        group_eq.setWidth(gridWidthEq+5);
        group_eq.setHeight((slot_w+spacing)*5);
        
        //set size for entire inventory grid
        int numGridW = 10;
        int gridWidth = (slot_w+spacing)*numGridW;
        group_inv.setWidth(gridWidth+5);
        group_inv.setHeight((slot_w+spacing)*2);

        for (int i = 0; i < slots.size(); i++) {
        final String slot = slots.get(i);
        
        Drawable slot_image = new TextureRegionDrawable(_game.res.slot_tex);
        
        if (player.getComponent(InventoryComponent.class) != null){ 
            //Gdx.app.log("Inventory", "Player has inventory");
            //needs a ton of refs to work :(
            InventorySlotButton slot_button = new InventorySlotButton(_engine, stage, player, slot_image, slot);
        
        if (slot.contains("inven")){
            group_inv.addActor(slot_button);
        }
        else{
            group_eq.addActor(slot_button);
            }
        } //end for
        
        }//end if item
        
        //set up window
        add(group_eq).row();
        //padding
        add(new Separator()).padTop(3).padBottom(3).height(20).width(gridWidth).fillY().row();
        add(group_inv).row();
        
        setCenterOnAdd(true);
        //inven_window.pack();
        setHeight(400);
        setWidth(gridWidth+5);
        setModal(true);
        closeOnEscape();
    }
    
}
