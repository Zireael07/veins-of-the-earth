/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.veins.game.components.InventoryComponent;
import com.veins.game.components.NameComponent;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.RemoveComponent;
import com.veins.game.components.SlotComponent;
import com.veins.game.components.SpriteComponent;
import com.veins.game.logic.GameLogic;
import java.util.HashMap;

/**
 *
 * @author AdmKasia
 */
public class InventorySystem extends EntitySystem {
    private ComponentMapper<PositionComponent>  positionMap   = ComponentMapper.getFor(PositionComponent.class);
    
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> item_entities;
    
    private GameLogic g_logic;
    private Engine g_engine;
    
    public InventorySystem(int priority, GameLogic logic){
        super.priority = priority;
        g_logic = logic;
    }
    
    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(InventoryComponent.class, PositionComponent.class).get());
        item_entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PositionComponent.class, SlotComponent.class).get());
        g_engine = engine;
    }
    
    public Entity checkPickupItem(Entity entity, int fx, int fy){
        Entity res = null;
        //Gdx.app.log("Pickup", "checking items at" + fx + ", " + fy);
        
        for (int i = 0; i < item_entities.size(); i++)
        {
            Entity entity_check = item_entities.get(i);
            if (entity_check != entity && fx == g_engine.getSystem(PositionSystem.class).getPositionX(entity_check) && fy == g_engine.getSystem(PositionSystem.class).getPositionY(entity_check))
            {
                Gdx.app.log("Pickup", "Detected an item at " + fx + ", " + fy);
                res = entity_check;
                break;
            }
            else {
                res = null;
            }
        }
        return res;
    }
    
    public void attemptPickup(Entity entity, int sx, int sy){
        Entity item = checkPickupItem(entity, sx, sy);
        if (item != null){
            if (entity.getComponent(InventoryComponent.class) != null)
            {
            //add to inventory
            String added = tryaddObject(entity, "inven_1", item);

                if (added != null){
                    //test
                    if (getObject(entity, added) != null){
                        String inven_string = getObject(entity, added).getComponent(NameComponent.class).string;
                        Gdx.app.log("Inventory", "Name of object in slot " + added +  " is " + inven_string);
                    }
                    else
                    {
                        Gdx.app.log("Inventory", "No item in slot " + added + "?!");
                    }
                    //remove from world
                    item.add(new RemoveComponent());
                }
            }
            else
            {
                Gdx.app.log("Inventory", "entity missing inventory component");
            }  
        }
    }
    
    public boolean addObject(Entity entity, String slot, Entity item){
        Gdx.app.log("Inventory", "Added entity to slot " + slot);
        
        //if (entity.getComponent(InventoryComponent.class) != null)
        {
            HashMap items = entity.getComponent(InventoryComponent.class).items_map;
            if (items.get(slot) == null){
                items.put(slot, item);
                String name = entity.getComponent(NameComponent.class).string;
                String item_name = item.getComponent(NameComponent.class).string;
                g_logic.addLog(name + " picked up " + item_name);
                return true;
            }
            else
            {
                Gdx.app.log("Inventory", "There's an item in the slot " + slot);
                return false;
            }
        }
        /*else{
            Gdx.app.log("Inventory", "entity missing inventory component");
        }*/
    }
    
    public String tryaddObject(Entity entity, String slot, Entity item){
        String res = null;
        if (slot.contains("inven")){
            //20 because of 20 inventory slots (see Inventory Component)
            int i = 0;
            HashMap items = entity.getComponent(InventoryComponent.class).items_map;
            while (i < 20 && items.get("inven_"+(i+1)) != null) {
                i++;
            }
                boolean added = addObject(entity, "inven_"+(i+1), item);
                
                if (added){
                    res = "inven_"+(i+1);
                }
        }    
        else{
            res = null;
        }
        boolean test = (res !=null);
        Gdx.app.log("Inventory", "try add to inven result " + test + " slot: " + res);
       return res;
    }
    
    public void doWear(Entity entity, String old_slot, Entity item){
        String item_name = item.getComponent(NameComponent.class).string;
        String slot = item.getComponent(SlotComponent.class).string;
        Gdx.app.log("Inventory", "trying to wear item " + item_name + " in slot " + slot);
        
        boolean added = addObject(entity, slot, item);
        
        if (added){
            removeObject(entity, old_slot, item);
        }
        
    }
    
    public Entity getObject(Entity entity, String slot){
        Entity res;
        if (entity.getComponent(InventoryComponent.class).items_map.containsKey(slot)){
            res = entity.getComponent(InventoryComponent.class).items_map.get(slot);
        }
        else
        {
            res = null;
        }
        return res;
    }
    
    public void attemptDrop(Entity entity, int sx, int sy){
        if (entity.getComponent(InventoryComponent.class) != null){
            Entity item = getObject(entity, "inven_1");
            if (item != null){
                removeObject(entity, "inven_1", item);
                g_logic.factory.CreateItem(
                    item.getComponent(NameComponent.class).string, 
                    item.getComponent(SpriteComponent.class).sprites.get(0),
                    item.getComponent(SlotComponent.class).string,
                    sx, sy);
            }
        }
        
    }
    
    public void removeObject(Entity entity, String slot, Entity item){
        Gdx.app.log("Inventory", "Removed entity from slot " + slot);
        entity.getComponent(InventoryComponent.class).items_map.remove(slot);
    }
    
    //processing?
    public void update(float deltaTime){
        for (int i = 0; i < item_entities.size(); i++)
        {
            Entity entity = item_entities.get(i);
            processEntity(entity, deltaTime);
        }
    }
    
    public void processEntity(Entity entity, float deltaTime){
        
    }
    
}
