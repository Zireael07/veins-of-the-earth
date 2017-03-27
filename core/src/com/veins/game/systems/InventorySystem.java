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
import com.veins.game.components.PositionComponent;
import com.veins.game.components.RemoveComponent;
import com.veins.game.components.SlotComponent;
import com.veins.game.components.SpriteComponent;

/**
 *
 * @author AdmKasia
 */
public class InventorySystem extends EntitySystem {
    private ComponentMapper<PositionComponent>  positionMap   = ComponentMapper.getFor(PositionComponent.class);
    
    private ImmutableArray<Entity> item_entities;
    
    private Engine g_engine;
    
    public InventorySystem(int priority){
        super.priority = priority;
    }
    
    public void addedToEngine(Engine engine){
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
            item.add(new RemoveComponent());
            Gdx.app.log("Inventory", "Added a remove component to item");
        }
    }
    
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
