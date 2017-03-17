/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.veins.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author AdmKasia
 */
public class SpriteComponent implements Component {
    public Array<Sprite> sprites = new Array<Sprite>();
    
    public SpriteComponent() {}

    public SpriteComponent(TextureRegion...textures) {
        addTextures(textures);
    }

    public void addTextures(TextureRegion...textures) {
        for (TextureRegion texture : textures)
            sprites.add(new Sprite(texture));
}
}
