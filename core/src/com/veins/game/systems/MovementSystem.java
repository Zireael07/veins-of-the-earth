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
import com.veins.game.components.NameComponent;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.TurnsComponent;
import com.veins.game.logic.GameLogic;

/**
 *
 * @author AdmKasia
 */
public class MovementSystem extends EntitySystem {
    private ComponentMapper<PositionComponent>  positionMap   = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<TurnsComponent> turnsMap = ComponentMapper.getFor(TurnsComponent.class);
    
    
    private ImmutableArray<Entity> entities;
    
    private GameLogic g_logic;
    private Engine g_engine;
    
    public MovementSystem(int priority, GameLogic logic){
        super.priority = priority;
        g_logic = logic;
    }
    
    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class).get());
        g_engine = engine;
    }
    
    public void update(float deltaTime){
        for (int i = 0; i < entities.size(); i++)
        {
            Entity entity = entities.get(i);
            processEntity(entity, deltaTime);
        }
    }
    
    public int getPositionX(Entity entity){
        PositionComponent positionCom  = positionMap.get(entity);
        return positionCom.x;
    }
    
    public int getPositionY(Entity entity){
        PositionComponent positionCom  = positionMap.get(entity);
        return positionCom.y;
    }
    
    public void setPositionX(Entity entity, int x){
        PositionComponent positionCom  = positionMap.get(entity);
        positionCom.x = x;
    }
    
    public void setPositionY(Entity entity, int y){
        PositionComponent positionCom  = positionMap.get(entity);
        positionCom.y = y;
    }
    
    public void setPosition(Entity entity, int x, int y)
    {
        setPositionX(entity, x);
        setPositionY(entity, y);
    }
    
    public boolean checkMove(Entity entity, int fx, int fy){
        return (fx >= 0 &&
                fx <= g_logic.MAP_WIDTH-1 && fy >= 0 &&
                fy <= g_logic.MAP_HEIGHT-1);
    }
    
    public void attemptMove(Entity entity, int dx, int dy){
        if (checkMove(entity, getPositionX(entity) + dx, getPositionY(entity) + dy)){
            setPosition(entity, getPositionX(entity) + dx, getPositionY(entity) + dy);
        }
    }
    
    
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionCom  = positionMap.get(entity);
        TurnsComponent turnsCom = turnsMap.get(entity);
        
       // test NPC movement
       String str = entity.getComponent(NameComponent.class).string;
       
       
       if (str == "Player"){       
       }
       else
       {
           if (turnsCom.blocking){
               attemptMove(entity, 1, 0);
               g_engine.getSystem(TurnTimeSystem.class).UnblockTurns(entity);
           }
           
       }     
    }
    
}
