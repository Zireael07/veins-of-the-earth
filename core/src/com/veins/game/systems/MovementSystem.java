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
import com.veins.game.components.NameComponent;
import com.veins.game.components.PositionComponent;
import com.veins.game.components.TurnsComponent;
import com.veins.game.logic.GameLogic;
import java.util.ArrayList;
import squidpony.squidmath.Coord;

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
    
    public void moveTo(Entity entity, int tx, int ty){
        int self_x = getPositionX(entity);
        int self_y = getPositionY(entity);
        
        int dx = tx-self_x;
        int dy = ty-self_y;
        
        Gdx.app.log("AI path", "move to " + tx + ", " + ty + " result " + dx + ", " + dy);
        
        attemptMove(entity, dx, dy);
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
               Coord start = Coord.get(getPositionX(entity), getPositionY(entity));
               Coord target = Coord.get(getPositionX(g_logic.getPlayer()), getPositionY(g_logic.getPlayer()));
               
               if (getPositionX(entity) == getPositionX(g_logic.getPlayer()) && getPositionY(entity) == getPositionY(g_logic.getPlayer())) 
               {
                   Gdx.app.log("AI path", "Do nothing because same position as player");
               } 
               else
               {
                   /*char[][] dunmap = g_logic.getDungeon();
                   if (dunmap[start.x][start.y] == '#'){
                       Gdx.app.log("AI path", "Do nothing because we're on wall tile");
                   }
                   else*/
                   {
                   
                   g_logic.getAIMap().findPath(20, null, null, start, target);

                //debug
                for (Coord c: g_logic.getAIMap().path){
                    Gdx.app.log("AI path", Integer.toString(c.x) + ", " + Integer.toString(c.y));
                }

                if (g_logic.getAIMap().path != null){
                    ArrayList<Coord> path = g_logic.getAIMap().path;
                    if (path.size() > 0){
                        moveTo(entity, path.get(0).x, path.get(0).y);
                    }
                }
               }
               }
               
               //old stuff
               g_engine.getSystem(TurnTimeSystem.class).UnblockTurns(entity);
           }
           
       }     
    }
    
}
