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
import com.veins.game.components.PositionComponent;
import com.veins.game.components.RemoveComponent;
import com.veins.game.components.SlotComponent;
import com.veins.game.components.SpriteComponent;

/**
 *
 * @author AdmKasia
 */
public class RemovalSystem extends EntitySystem {
    private ComponentMapper<RemoveComponent>  removeMap   = ComponentMapper.getFor(RemoveComponent.class);
    
    private ImmutableArray<Entity> entities;
    
    private Engine g_engine;
    
    public RemovalSystem(int priority){
        super.priority = priority;
    }
    
    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(RemoveComponent.class).get());
        g_engine = engine;
    }
    
    public void update(float deltaTime){
        for (int i = 0; i < entities.size(); i++)
        {
            Entity entity = entities.get(i);
            processEntity(entity, deltaTime);
        }
    }
    
    public void processEntity(Entity entity, float deltaTime){
        g_engine.removeEntity(entity);
    }
}
