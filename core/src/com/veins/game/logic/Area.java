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
        //spawn some monsters
        for (int x = 0; x < g_logic.NUM_NPC; x++)
        {
            spawnActor(x);
        }
        
        //spawn an item
        int item_x = g_logic.rng.between(1, g_logic.MAP_WIDTH-1);
        int item_y = g_logic.rng.between(1, g_logic.MAP_HEIGHT-1);
        g_logic.factory.CreateItem("longsword", _game.res.sword_tex, "main_hand", item_x, item_y);
        
        item_x = g_logic.rng.between(1, g_logic.MAP_WIDTH-1);
        item_y = g_logic.rng.between(1, g_logic.MAP_HEIGHT-1);
        g_logic.factory.CreateItem("leather armor", _game.res.armor_tex, "body", item_x, item_y);
        
        //test
        g_logic.factory.testLoading();
        g_logic.factory.itemtestLoading();
    }
    
    public void spawnActor(int x){
        int act_x = g_logic.rng.between(0, g_logic.MAP_WIDTH-1);
        int act_y = g_logic.rng.between(0, g_logic.MAP_HEIGHT-1);
        
        while (inter_map[act_x][act_y].getActor() != null){
            Gdx.app.log("Spawn", "Rejecting location because existing actor");
            act_x = g_logic.rng.between(0, g_logic.MAP_WIDTH-1);
            act_y = g_logic.rng.between(0, g_logic.MAP_HEIGHT-1);
        }
        
        Entity actor = g_logic.factory.CreateActor("kobold" + "#" + x, _game.res.kobold_tex, "enemy", act_x, act_y);
        inter_map[act_x][act_y].setActor(actor);
    }
}
