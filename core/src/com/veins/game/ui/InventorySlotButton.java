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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.veins.game.components.SlotComponent;
import com.veins.game.components.SpriteComponent;
import com.veins.game.systems.InventorySystem;

/**
 *
 * @author AdmKasia
 */
public class InventorySlotButton extends VisImageButton {
    
    //references
    final Stage stage_ref;
    final Entity player_ref;
    Engine engine_ref;
    
    Entity item_ref;
    String slot;
    static Drawable slot_image;
    static Drawable item_image;
    
    public InventorySlotButton(Engine engine, Stage stage, Entity player, Drawable imageSlot, String tooltipText) {
        super(imageSlot, tooltipText);
        this.slot = tooltipText;
        stage_ref = stage;
        engine_ref = engine;
        player_ref = player;
        item_ref = engine_ref.getSystem(InventorySystem.class).getObject(player_ref, slot);  //item;
        slot_image = imageSlot;
        item_image = null;
                
        //set item
        setItemImage(item_ref);
        
        //update image
        VisImageButtonStyle style = createStyle(slot);
        if (style != null)
        {
            setStyle(style);
        }
        
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
                        //force unset item image to prevent weirdness
                        unsetItemImage();
                        //update the slot
                        VisImageButtonStyle style = createStyle(slot);
                        if (style != null)
                        {
                            setStyle(style);
                        }
                        //find and try to update target slot
                        String check_string = item_ref.getComponent(SlotComponent.class).string;
                        Array<Actor> groups = getParent().getParent().getChildren();
                        //group 0 is window label
                        Group group = (Group) groups.get(1);
                        if (group != null){
                            //Gdx.app.log("Group", "We have a group" + group.getName());
                        
                        for (int i = 0; i < group.getChildren().size; i++) {
                            //Gdx.app.log("Inventory slot", "Looping over group's children #" + i);
                            //paranoia
                            if ((group.getChildren().get(i) instanceof InventorySlotButton)){
                            
                            InventorySlotButton check_slot = (InventorySlotButton) group.getChildren().get(i);
                                
                            if (check_slot.slot.equals(check_string)){
                                Gdx.app.log("Inventory slot", "Found slot with string " + check_string);
                                //Gdx.app.log("Inventory slot", "Slot is " + check_slot.slot);
                                
                                //provide necessary data to the target slot
                                check_slot.item_ref = item_ref;
                                check_slot.setItemImage(item_ref);
                                
                                //actually update the slot
                                VisImageButtonStyle style_check = check_slot.createStyle(check_slot.slot);
                                if (style_check !=null){
                                    
                                    check_slot.setStyle(style_check);
                                    //Gdx.app.log("Inventory slot", "We have a style for target");
                                    break;
                                }
                                
                            }
                            }
                        }
                        }
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
    
    void setItemImage(Entity item_ref){
        if (item_ref != null){
            item_image = new SpriteDrawable(item_ref.getComponent(SpriteComponent.class).sprites.get(0)); //imageItem;
        }
    }
    
    void unsetItemImage(){
        item_image = null;
    }
    
    
    private VisImageButtonStyle createStyle(String slot) {
        Drawable image;
        
        if (item_ref != null && item_image !=null){
            Gdx.app.log("Inventory slot", "Drawing item image");
            image = item_image;
        } else {
            //draw slot
            image = slot_image;
        }
        VisImageButtonStyle style = new VisImageButtonStyle(VisUI.getSkin().get(VisImageButtonStyle.class));
        style.imageUp = image;
        style.imageDown = image;
        
        //Gdx.app.log("Inventory slot", "Creating style for " + slot);
        
        return style;
    }
}
