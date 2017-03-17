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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.SpriteComponent;

/**
 *
 * @author AdmKasia
 */
public class RenderingSystem extends EntitySystem {
    private SpriteBatch batch;
    
    private ImmutableArray<Entity> entities;
    
    
    private ComponentMapper<SpriteComponent> spriteMap = ComponentMapper.getFor(SpriteComponent.class);

    public RenderingSystem(int priority, SpriteBatch batch) {
        super.priority = priority;
        //Gdx.app.log("Render Priority", "prio is" + super.priority);
        this.batch = batch;
    }
    
    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PositionComponent.class).get());
    }
    
    public void update(float deltaTime){
        for (int i = 0; i < entities.size(); i++)
        {
            Entity entity = entities.get(i);
            processEntity(entity, deltaTime);
        }
    }
    

    protected void processEntity(Entity entity, float deltaTime) {
        for (Sprite sprite : spriteMap.get(entity).sprites) {
            batch.setShader(null);
            sprite.draw(batch);
}       }
}
