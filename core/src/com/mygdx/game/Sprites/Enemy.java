package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite{
    protected World world;
    protected PlayScreen screen;
    public Body b2body2;
    protected TextureAtlas atlas;
    private Hero hero;

    public Enemy(PlayScreen screen, float x, float y, TextureAtlas atlas, Hero hero) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.atlas = atlas;
      
        setPosition(x, y);
        defineEnemy();
    }

    protected abstract void defineEnemy();
    public abstract void takeDamage();

}
