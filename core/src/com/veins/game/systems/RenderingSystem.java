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
import com.veins.game.MyVeinsGame;
import com.veins.game.components.FactionComponent;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.SlotComponent;
import com.veins.game.components.SpriteComponent;
import com.veins.game.logic.GameLogic;

/**
 *
 * @author AdmKasia
 */
public class RenderingSystem extends EntitySystem {
    MyVeinsGame game;
    GameLogic g_logic;
    
    private SpriteBatch batch;
    
    //private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> actor_entities;
    private ImmutableArray<Entity> item_entities;
    
    private ComponentMapper<SpriteComponent> spriteMap = ComponentMapper.getFor(SpriteComponent.class);

    public RenderingSystem(int priority, SpriteBatch batch, GameLogic logic, MyVeinsGame _game) {
        super.priority = priority;
        //Gdx.app.log("Render Priority", "prio is" + super.priority);
        this.batch = batch;
        g_logic = logic;
        game = _game;
    }
    
    public void addedToEngine(Engine engine){
       actor_entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PositionComponent.class).exclude(SlotComponent.class).get());
        item_entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PositionComponent.class, SlotComponent.class).get());
    }
    
    public void update(float deltaTime){
         //items first
        for (int i = 0; i < item_entities.size(); i++)
        {
            Entity entity = item_entities.get(i);
            processEntity(entity, deltaTime, true);
        }
        //actors second
        for (int i = 0; i < actor_entities.size(); i++)
        {
            Entity entity = actor_entities.get(i);
            processEntity(entity, deltaTime, false);
        }
    }
    

    protected void processEntity(Entity entity, float deltaTime, boolean item) {
        if (!item){
            float marker_x = spriteMap.get(entity).sprites.get(0).getX();
            float marker_y = spriteMap.get(entity).sprites.get(0).getY();
            marker_y = marker_y - g_logic.ISO_HEIGHT/4-g_logic.ISO_HEIGHT/8;
            marker_x = marker_x - g_logic.ISO_WIDTH/4;
            
            game.res.unit_marker.setPosition(marker_x, marker_y);
            
            //tint
            if (entity.getComponent(FactionComponent.class).string == "enemy"){
                game.res.unit_marker.setColor(1,0,0,1);
            }
            else{
                game.res.unit_marker.setColor(0,1,1,1);
            }
            
            game.res.unit_marker.draw(batch);
        }
        
        
        for (Sprite sprite : spriteMap.get(entity).sprites) {
            batch.setShader(null);
            sprite.draw(batch);
}       }
}
