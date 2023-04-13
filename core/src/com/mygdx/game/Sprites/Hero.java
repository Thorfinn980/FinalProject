package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.VerticalSlice;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Rumble;
import com.mygdx.game.Tools.WorldContactListener;
// import com.mygdx.game.Tools.myInputProcessor;

public class Hero extends Sprite{

    public World world;
    public Body b2body;
    public Body hitboxBody;
    private Animation<TextureRegion> idle;
    private Animation<TextureRegion> run;
    private Animation<TextureRegion> jump;
    private Animation<TextureRegion> fall;

    public State currentState = State.STANDING;
    public State previousState = State.STANDING;
    private float stateTimer = 0;
    private boolean runningRight;

    private int jumpCounter;
    private WorldContactListener contactListener;
    private int dashCounter;
    private float maxSpeed;
    private static boolean isDead;
    private Animation<TextureRegion> dead;


    public static BodyDef bdef;

    public enum State{
        FALLING, JUMPING, STANDING, RUNNING, ATTACKING, MAXJUMP, DEAD;
    }

    public Hero(World world, PlayScreen screen){
        this.world = screen.getWorld();
        this.jumpCounter = 0;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        maxSpeed = 2f;
        runningRight = true;
        isDead = false;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //! Dead
        for (int i = 0; i < 10; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("jump 48x48"), i*48, 0, 48, 48));
        }
        dead = new Animation<TextureRegion>(0.06f, frames);
        frames.clear();

        //! Idle
        for (int i = 0; i < 10; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Idle Rig 48x48"), i*48, 0, 48, 48));
        }
        idle = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        //! Run
        for (int i = 0; i < 8; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("run cycle 48x48"), i*48, 0, 48, 48));
        }
        run = new Animation<TextureRegion>(0.06f, frames);
        frames.clear();

        //! Jump
        for (int i = 0; i < 1; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("jump 48x48"), i*48, 0, 48, 48));
        }
        jump = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        //! Fall
        for (int i = 2; i < 3; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("jump 48x48"), i*48, 0, 48, 48));
        }
        fall = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        defineHero();
        //heroStand = new TextureRegion(getTexture(), 4,286,45,45);
        setBounds(0, 0, 64/VerticalSlice.PPM, 64/VerticalSlice.PPM);

        setRegion(idle.getKeyFrame(stateTimer, true));

        //Contact Listener & Collision Detector
        contactListener = new WorldContactListener();
        world.setContactListener(contactListener);
    }

    public void update(float dt){
        // Limit maximum speed
        if (b2body.getLinearVelocity().x > maxSpeed) {
            b2body.setLinearVelocity(maxSpeed, b2body.getLinearVelocity().y);
        } else if (b2body.getLinearVelocity().x < -maxSpeed) {
            b2body.setLinearVelocity(-maxSpeed, b2body.getLinearVelocity().y);
        }
        
        setPosition(b2body.getPosition().x -getWidth()/2, b2body.getPosition().y -getWidth()/2 );
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case DEAD:
                region = dead.getKeyFrame(stateTimer,true);;
                break;
            case JUMPING:
                region = jump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = run.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = fall.getKeyFrame(stateTimer,true);
                break;
            default:
                region = idle.getKeyFrame(stateTimer, true);
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    public BodyDef getBdef(){
        return bdef;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && (previousState == State.JUMPING || !contactListener.isPlayerOnGround()))){
            return State.JUMPING;
        }

        if(isDead)
            return State.DEAD;
        else if(b2body.getLinearVelocity().y == 0 && previousState == State.MAXJUMP){
            return State.MAXJUMP;
        }
        else if(b2body.getLinearVelocity().y < 0 && previousState == State.FALLING){
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            return State.ATTACKING;
        }
        else{
            return State.STANDING;
        }
    }

    
    public void defineHero(){
        //!Create body
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(260 / VerticalSlice.PPM, 400 /VerticalSlice.PPM);
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12 / VerticalSlice.PPM, 20 / VerticalSlice.PPM);
        fdef.shape = shape;
        //Zero bounce
        fdef.restitution = 0f;
        fdef.friction = 0f;
        //Filter for collision for player body
        fdef.filter.categoryBits =  VerticalSlice.BIT_PLAYER;
        fdef.filter.maskBits = VerticalSlice.BIT_GROUND | VerticalSlice.BIT_ENEMY | VerticalSlice.BIT_WALL | VerticalSlice.BIT_EXIT;
        b2body.createFixture(fdef).setUserData(this);

        //! Create a foot sensor
        shape.setAsBox(5/VerticalSlice.PPM, 5/VerticalSlice.PPM,new Vector2(0,-20/VerticalSlice.PPM),0);
        fdef.shape = shape;
        fdef.filter.categoryBits = VerticalSlice.BIT_PLAYER;
        fdef.filter.maskBits = VerticalSlice.BIT_GROUND;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("foot");
    }

    public void handleUserInput(float dt){

        if(currentState != State.DEAD){
            if (contactListener.isPlayerOnGround()) {
                jumpCounter = 0;
            }

            if (contactListener.isPlayerOnGround()) {
                dashCounter = 0;
            }

            if(dashCounter == 1 && !contactListener.isPlayerOnGround() && Gdx.input.isKeyPressed(Input.Keys.SPACE)  && jumpCounter <= 3){
                b2body.applyLinearImpulse(new Vector2(0,1f), b2body.getWorldCenter(), true);
                System.out.println(jumpCounter);
                VerticalSlice.manager.get("audio/sounds/Step_grass.wav", Sound.class).play();
                VerticalSlice.manager.get("audio/sounds/Jump.wav", Sound.class).play();
                jumpCounter++;
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashCounter < 1 && runningRight  && !contactListener.isPlayerOnGround() ){
                // System.out.println("Jump is pressed");
                Rumble.rumble(.1f, .2f);
                VerticalSlice.manager.get("audio/sounds/Dive.wav", Sound.class).play();
                b2body.applyLinearImpulse(new Vector2(100f,0.5f), b2body.getWorldCenter(), true);
                dashCounter++;
                // jumpCounter = -2;
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashCounter < 1 && !runningRight  && !contactListener.isPlayerOnGround()){
                // System.out.println("Jump is pressed");
                Rumble.rumble(.1f, .2f);
                VerticalSlice.manager.get("audio/sounds/Dive.wav", Sound.class).play();
                b2body.applyLinearImpulse(new Vector2(-100f,0.5f), b2body.getWorldCenter(), true);
                dashCounter++;
                // jumpCounter = -2;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && jumpCounter < 1) {
                // System.out.println("Jump is pressed");a
                b2body.applyLinearImpulse(new Vector2(0,5f), b2body.getWorldCenter(), true);
                System.out.println(jumpCounter);
                VerticalSlice.manager.get("audio/sounds/Jump.wav", Sound.class).play();
                jumpCounter++;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) && b2body.getLinearVelocity().x >= -2) {
                b2body.applyLinearImpulse(new Vector2(-2f, 0), b2body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) && b2body.getLinearVelocity().x <= 2) {
                b2body.applyLinearImpulse(new Vector2(2f, 0), b2body.getWorldCenter(), true);
            }
        
            if (((!Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) && contactListener.isPlayerOnGround())) {
                b2body.setLinearVelocity(0f, b2body.getLinearVelocity().y);
            }
        }
        else
        {
            b2body.setLinearVelocity(0f, b2body.getLinearVelocity().y);
        }

    }

    public boolean playerIsDead(){
        return isDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public static void exit(){
        isDead = true;
    }

    public void hit(Enemy enemy){
        isDead = true;
        Rumble.rumble(3f, .2f);
        VerticalSlice.manager.get("audio/sounds/Slash.wav", Sound.class).play();
    }
}
