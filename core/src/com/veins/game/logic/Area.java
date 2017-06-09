/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.logic;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.veins.game.MyVeinsGame;
import com.veins.game.components.NameComponent;
import squidpony.squidai.DijkstraMap;

/**
 *
 * @author AdmKasia
 */
public class Area {
    
    GameLogic g_logic;
    MyVeinsGame _game;
    MapGenerator mapgen;
    public TiledMap map;
    DijkstraMap AIMap;
    MapTile[][] inter_map;
    
    public Area (GameLogic logic, MyVeinsGame game){
        g_logic = logic;
        _game = game;
        
        //map gen
        mapgen = new MapGenerator(g_logic);
        map = mapgen.createMap();
        
        
        //char dungeon
        logic.setDungeon(mapgen.createCharMap());
        Gdx.app.log("Char dungeon", '\n' + mapgen.toString());
        AIMap = new DijkstraMap(logic.getDungeon(), DijkstraMap.Measurement.CHEBYSHEV);
        g_logic.setAIMap(AIMap);
        
        //inter map
        inter_map = g_logic.getInterMap();
    }
    
    public void spawnStuff(){
        //load data
        g_logic.factory.testLoading("data/test.json");
        g_logic.factory.itemtestLoading();

        //spawn some monsters
        for (int x = 0; x < g_logic.NUM_NPC; x++)
        {
            spawnActor(x);
        }
        
        //spawn an item
        int item_x = g_logic.rng.between(1, g_logic.MAP_WIDTH-1);
        int item_y = g_logic.rng.between(1, g_logic.MAP_HEIGHT-1);
        //g_logic.factory.CreateItem("longsword", _game.res.sword_tex, "main_hand", item_x, item_y);
        g_logic.factory.CreateItem("longsword", item_x, item_y);
        
        item_x = g_logic.rng.between(1, g_logic.MAP_WIDTH-1);
        item_y = g_logic.rng.between(1, g_logic.MAP_HEIGHT-1);
        //g_logic.factory.CreateItem("leather armor", _game.res.armor_tex, "body", item_x, item_y);
        g_logic.factory.CreateItem("leather armor", item_x, item_y);
        
        //spawn some money
        item_x = g_logic.rng.between(1, g_logic.MAP_WIDTH-1);
        item_y = g_logic.rng.between(1, g_logic.MAP_HEIGHT-1);
        g_logic.factory.CreateMoney("silver coin", item_x, item_y);
        
        item_x = g_logic.rng.between(1, g_logic.MAP_WIDTH-1);
        item_y = g_logic.rng.between(1, g_logic.MAP_HEIGHT-1);
        g_logic.factory.CreateMoney("silver coin", item_x, item_y);
    }
    
    public void spawnActor(int x){
        int id_spawn = g_logic.rng.between(0, 4);
        int act_x = g_logic.rng.between(0, g_logic.MAP_WIDTH-1);
        int act_y = g_logic.rng.between(0, g_logic.MAP_HEIGHT-1);
        
        while (inter_map[act_x][act_y].getActor() != null){
            Gdx.app.log("Spawn", "Rejecting location because existing actor");
            act_x = g_logic.rng.between(0, g_logic.MAP_WIDTH-1);
            act_y = g_logic.rng.between(0, g_logic.MAP_HEIGHT-1);
        }
        
        //random pick
        if (id_spawn == 0){
            Entity actor = g_logic.factory.CreateActor("kobold", act_x, act_y);
            //named in order
            actor.getComponent(NameComponent.class).string = "kobold" + "#" + x;
            inter_map[act_x][act_y].setActor(actor);
        }
        if (id_spawn == 1){
            Entity actor = g_logic.factory.CreateActor("human", act_x, act_y);
            //named in order
            actor.getComponent(NameComponent.class).string = "human" + "#" + x;
            inter_map[act_x][act_y].setActor(actor);
        }
        if (id_spawn == 2){
            Entity actor = g_logic.factory.CreateActor("goblin", act_x, act_y);
            //named in order
            actor.getComponent(NameComponent.class).string = "goblin" + "#" + x;
            inter_map[act_x][act_y].setActor(actor);
        }
        if (id_spawn == 3){
            Entity actor = g_logic.factory.CreateActor("drow", act_x, act_y);
            //named in order
            actor.getComponent(NameComponent.class).string = "drow" + "#" + x;
            inter_map[act_x][act_y].setActor(actor);
        }
    }
}
