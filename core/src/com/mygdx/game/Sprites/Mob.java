package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.VerticalSlice;
import com.mygdx.game.Screens.PlayScreen;

public class Mob extends Enemy{

    private float stateTime;
    private Animation<TextureRegion> idle;
    private TextureRegion region;
    private boolean setToDestroy;
    private boolean Destroyed;

    public Hero hero;
    public BodyDef bdef2;

    public Body b2body2;


    public Mob(PlayScreen screen, float x, float y, TextureAtlas atlas, Hero hero) {
        super(screen, x, y, atlas, hero);

        region = atlas.findRegion("Slime");

        Array<TextureRegion> frames2 = new Array<TextureRegion>();

        for (int i = 0; i < 6; i++){
            frames2.add(new TextureRegion(region, i*32, 0, 32, 32));
        }
        idle = new Animation<TextureRegion>(0.1f, frames2);
        frames2.clear();
        stateTime = 0;
        setBounds(getX(), getY(), 64/VerticalSlice.PPM, 64/VerticalSlice.PPM);
        setToDestroy = false;
        Destroyed = false;

        this.hero = hero;
    }


    

    public void update(float dt){
        stateTime += dt;

        if(setToDestroy && !Destroyed){
            world.destroyBody(b2body2);
            Destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("Slime"), 32, 0, 32, 32));
        }
        else if(!Destroyed){
            
            setPosition(b2body2.getPosition().x-getWidth()/2, b2body2.getPosition().y-getHeight()/2); 
            setRegion(idle.getKeyFrame(stateTime, true));
        }

        setPosition(b2body2.getPosition().x-getWidth()/2, b2body2.getPosition().y-getHeight()/2);
        setRegion(idle.getKeyFrame(stateTime, true));

        missileInput(dt);

    }

    private void missileInput(float dt) {
        // Get the position of the player's body
        Vector2 playerPosition = hero.b2body.getPosition();
    
        // Define the missile's initial velocity
        float maxMissileSpeed = 6f;
        Vector2 missileVelocity = new Vector2(playerPosition.x - b2body2.getPosition().x, playerPosition.y - b2body2.getPosition().y).nor().scl(maxMissileSpeed);
    
        // Check if the magnitude of the missile velocity exceeds the maximum speed
        if (missileVelocity.len() > maxMissileSpeed) {
            missileVelocity.setLength(maxMissileSpeed);
        }
    
        // Apply the linear impulse to the missile's body
        b2body2.applyLinearImpulse(missileVelocity.scl(dt), b2body2.getWorldCenter(), true);

    }
    

    @Override
    protected void defineEnemy() {
        //!Create body
        bdef2 = new BodyDef();
        bdef2.type = BodyDef.BodyType.DynamicBody;
        bdef2.position.set(getX()/VerticalSlice.PPM, getY()/VerticalSlice.PPM);
        bdef2.fixedRotation = true;
        b2body2 = screen.getWorld().createBody(bdef2);

        FixtureDef fdef2 = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15 / VerticalSlice.PPM, 8 / VerticalSlice.PPM, new Vector2(0,-20/VerticalSlice.PPM), 0);
        fdef2.shape = shape;
        //Zero bounce
        fdef2.restitution = 1f;
        fdef2.friction = 1f;
        //Filter for collision for player body
        fdef2.filter.categoryBits =  VerticalSlice.BIT_ENEMY;
        fdef2.filter.maskBits = VerticalSlice.BIT_GROUND | VerticalSlice.BIT_PLAYER | VerticalSlice.BIT_WALL | VerticalSlice.BIT_ENEMY;
        b2body2.createFixture(fdef2).setUserData(this);
        System.out.println("Mob created");
    

        //Controls the gravity of the mob
        // b2body2.setGravityScale(0.0f);
    }

    @Override
    public void takeDamage() {
        setToDestroy = true;
    }
}
