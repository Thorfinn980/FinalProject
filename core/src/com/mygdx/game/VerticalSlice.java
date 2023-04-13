package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class VerticalSlice extends Game {

	public static final int V_WIDTH = 600;
	public static final int V_HEIGHT = 410;
	// public static final int V_WIDTH = Gdx.graphics.getWidth(); ;
	// public static final int V_HEIGHT = Gdx.graphics.getHeight();
	public static final float PPM = 64f;

	//Collision Filters
	public static final int BIT_GROUND = 2;
	public static final int BIT_PLAYER = 4;
	public static final int BIT_GROUND_BOX = 8;
	public static final int BIT_ENEMY = 16;

	//! TO be determnined later.
	public static final int BIT_SWORD = 32;
	public static final int BIT_PIT = 64;
	public static final int BIT_EXIT = 128;
	public static final int BIT_WALL = 256;
	public static final int BIT_JUMP = 512;


	public static AssetManager manager;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/dungeon_theme.wav", Music.class);
		manager.load("audio/sounds/Slash.wav", Sound.class);
		manager.load("audio/sounds/Dive.wav", Sound.class);
		manager.load("audio/sounds/Jump.wav", Sound.class);
		manager.load("audio/sounds/Step_grass.wav", Sound.class);
		manager.finishLoading();
		

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
		
	}
}