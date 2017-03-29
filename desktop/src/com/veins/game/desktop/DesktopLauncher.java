package com.veins.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.veins.game.MyVeinsGame;

public class DesktopLauncher {
	public static void main (String[] args) {
        Pack();
    	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    	new LwjglApplication(new MyVeinsGame(), config);
    }
        
        static void Pack()
        {
            TexturePacker.Settings settings = new TexturePacker.Settings();
            settings.maxHeight = 2048;
            settings.maxWidth = 2048;
            settings.pot = true;
            TexturePacker.process(settings, "../assets/unpacked", "packed", "game");
            TexturePacker.process(settings, "../assets/unpacked/terrain", "packed", "terrain");
            TexturePacker.process(settings, "../assets/unpacked/inventory", "packed", "inventory");
        }
}
