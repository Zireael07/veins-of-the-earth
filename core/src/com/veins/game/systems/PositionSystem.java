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
import com.badlogic.gdx.math.Vector3;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.SlotComponent;
import com.veins.game.components.SpriteComponent;
import com.veins.game.logic.GameLogic;

/**
 *
 * @author AdmKasia
 */
public class PositionSystem extends EntitySystem {
    
    private ComponentMapper<PositionComponent>  positionMap   = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<SpriteComponent> spriteMap = ComponentMapper.getFor(SpriteComponent.class);
    
    private ImmutableArray<Entity> actor_entities;
    private ImmutableArray<Entity> item_entities;
    
    private GameLogic g_logic;
    
    public PositionSystem(int priority, GameLogic logic){
        super.priority = priority;
        //Gdx.app.log("Position Priority", "prio is" + super.priority);
        g_logic = logic;
    }
    
    public void addedToEngine(Engine engine){
        actor_entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PositionComponent.class).exclude(SlotComponent.class).get());
        item_entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PositionComponent.class, SlotComponent.class).get());
    }
    
    public int getPositionX(Entity entity){
        PositionComponent positionCom  = positionMap.get(entity);
        return positionCom.x;
    }
    
    public int getPositionY(Entity entity){
        PositionComponent positionCom  = positionMap.get(entity);
        return positionCom.y;
    }
    
    //processing
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
    
    
    public void processEntity(Entity entity, float deltaTime, boolean item) {
        PositionComponent positionCom  = positionMap.get(entity);
        SpriteComponent spriteCom = spriteMap.get(entity);
        
        if (positionCom != null){
            Vector3 gridactorPos = new Vector3(positionCom.x, positionCom.y, 0);
            Vector3 isoTile = g_logic.IsotoWorld(gridactorPos);
            
            if (item){ 
                int object_x = (int) isoTile.x+g_logic.ISO_WIDTH/4;
                int object_y = (int) isoTile.y-g_logic.ISO_HEIGHT/2-g_logic.ISO_HEIGHT/4+g_logic.ISO_HEIGHT/16;
                
                for (Sprite sprite : spriteCom.sprites) {
                    sprite.setX(object_x);
                    sprite.setY(object_y);
                }
            }
            else
            {
                int actor_x = (int) isoTile.x+g_logic.ISO_WIDTH/4;
                int actor_y = (int) isoTile.y-g_logic.ISO_HEIGHT/4;

                for (Sprite sprite : spriteCom.sprites) {
                    sprite.setX(actor_x);
                    sprite.setY(actor_y);
                }
            }
            
        }
    }
}
