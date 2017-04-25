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
    //rendered map
    TiledMap map;
    Texture tiles;
    
    GameLogic g_logic;
    
    MapTile[][] map_inter;
    char[][] dungeon;
    
    //init the game logic ref
    public MapGenerator(GameLogic logic) {
        g_logic = logic;
    }
           
    
    public TiledMap createMap() {
        //init internal map representation
        map_inter = new MapTile[g_logic.MAP_WIDTH][g_logic.MAP_HEIGHT];
        
        //drawing stuff
        tiles = new Texture(Gdx.files.internal("packed/terrain.png"));
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, 54, 54);
        map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer new_layer = new TiledMapTileLayer(g_logic.MAP_WIDTH, g_logic.MAP_HEIGHT, g_logic.ISO_WIDTH, g_logic.ISO_HEIGHT);
       
        //actual generation
        for (int x = 0; x < g_logic.MAP_WIDTH; x++) 
            {
                for (int y = 0; y < g_logic.MAP_HEIGHT; y++) 
                {
                        char chara = '.';
                        int ty = 0;
                        int tx;
                        if (x == 0 || x == g_logic.MAP_WIDTH-1)
                        {
                            tx = 1;
                            chara = '#';
                        }
                        else
                        {
                            tx = 0; 
                            chara = '.';
                        }
                        
                        //set up map tiles
                        MapTile tile = new MapTile(x, y, tx, ty, chara);
                        Gdx.app.log("Map gen", "Created a map tile @" + x + "," + y + " " + tx + " " + ty + " " + chara);
                        
                        //put them in the internal map
                        putInterMap(tile, x, y);
                        
                        //set up renderer cells
                        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                        //y,x
                        cell.setTile(new StaticTiledMapTile(splitTiles[tile.getTextureY()][tile.getTextureX()]));
                        new_layer.setCell(x, y, cell);
                }
            }
            
            
            float y_off = g_logic.getYOffset();
            new_layer.setOffsetY(y_off);
            layers.add(new_layer);
            
            g_logic.setInterMap(map_inter);

    return map;
    }
    
    public void putInterMap(MapTile tile, int x, int y){
        map_inter[x][y] = tile;
    }
    
    
    
    public void put(char elem, int x, int y) {
        dungeon[x][y] = elem;
    }
    
    public char[][] createCharMap(){
        dungeon = new char[g_logic.MAP_WIDTH][g_logic.MAP_HEIGHT];
        for (int x = 0; x < g_logic.MAP_WIDTH; x++) 
            {
                for (int y = 0; y < g_logic.MAP_HEIGHT; y++) 
                {
                        MapTile tile = map_inter[x][y];
                        char draw = tile.getCharacter();
                        if (draw == '#')
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
        char[][] trans = new char[g_logic.MAP_HEIGHT][g_logic.MAP_WIDTH];
        for (int x = 0; x < g_logic.MAP_HEIGHT; x++) {
            for (int y = 0; y < g_logic.MAP_WIDTH; y++) {
                trans[y][x] = dungeon[x][y];
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int row = 0; row < g_logic.MAP_HEIGHT; row++) {
            sb.append(trans[row]);
            sb.append('\n');
        }
        return sb.toString();
    }
}
