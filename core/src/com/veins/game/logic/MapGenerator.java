/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

/**
 *
 * @author AdmKasia
 */
public class MapGenerator {
    
    TiledMap map;
    Texture tiles;
    GameLogic g_logic;
    
    char[][] dungeon;
    
    //init the game logic ref
    public MapGenerator(GameLogic logic) {
        g_logic = logic;
    }
           
    
    public TiledMap createMap() {
        tiles = new Texture(Gdx.files.internal("packed/terrain.png"));
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, 54, 54);
        map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer new_layer = new TiledMapTileLayer(g_logic.MAP_WIDTH, g_logic.MAP_HEIGHT, g_logic.ISO_WIDTH, g_logic.ISO_HEIGHT);
        for (int x = 0; x < 10; x++) 
            {
                for (int y = 0; y < 10; y++) 
                {
                        int ty = 0;
                        int tx;
                        if (x == 0 || x == 9)
                        {
                            tx = 1;
                        }
                        else
                        {
                            tx = 0; 
                        }
                        
                        //set up cells
                        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                        cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
                        new_layer.setCell(x, y, cell);
                }
            }
            
            
            float y_off = g_logic.getYOffset();
            new_layer.setOffsetY(y_off);
            layers.add(new_layer);

    return map;
    }
    
    
    public void put(char elem, int x, int y) {
        dungeon[x][y] = elem;
    }
    
    public char[][] createCharMap(){
        dungeon = new char[10][10];
        for (int x = 0; x < 10; x++) 
            {
                for (int y = 0; y < 10; y++) 
                {
                        if (x == 0 || x == 9)
                        {
                            put('#', x, y);
                        }
                        else
                        {
                            put('.', x, y); 
                        }
                }
        }
        return dungeon;
    }
    
    public String toString() {
        char[][] trans = new char[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                trans[y][x] = dungeon[x][y];
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int row = 0; row < 10; row++) {
            sb.append(trans[row]);
            sb.append('\n');
        }
        return sb.toString();
    }
}
