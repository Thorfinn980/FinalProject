package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.VerticalSlice;
import com.mygdx.game.Sprites.Enemy;
import com.mygdx.game.Sprites.Hero;

public class WorldContactListener implements ContactListener{

    private boolean isOnGround;
    public int i = 0;
    private static boolean inRange;

    public Fixture fixtureA;
    public Fixture fixtureB;

    public static boolean getRange() {
        return inRange;
    }

    @Override
    public void beginContact(Contact contact) {
        fixtureA = contact.getFixtureA();
        fixtureB = contact.getFixtureB();

        int collisionDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

            if (collisionDef == (VerticalSlice.BIT_PLAYER | VerticalSlice.BIT_ENEMY)) {
                System.out.println("Player hit enemy");

                if (fixtureA.getFilterData().categoryBits == VerticalSlice.BIT_PLAYER) {
                    System.out.println("Player hit enemy again");
                    ((Hero) fixtureA.getUserData()).hit((Enemy)fixtureB.getUserData());
                } else {
                    // ((Hero) fixtureB.getUserData()).hit((Enemy)fixtureA.getUserData());
                }
            }

            if (collisionDef == (VerticalSlice.BIT_PLAYER | VerticalSlice.BIT_EXIT)) {
                System.out.println("Exit");

                Hero.exit();

                if (fixtureA.getFilterData().categoryBits == VerticalSlice.BIT_PLAYER) {
                    System.out.println("Exit");
                    ((Hero) fixtureA.getUserData()).hit((Enemy)fixtureB.getUserData());
                } else {
                    // ((Hero) fixtureB.getUserData()).hit((Enemy)fixtureA.getUserData());
                }
            }

            if (fixtureA.getUserData() != null && fixtureB.getUserData().equals("enemy")) {

                System.out.println("Foot on enemy: " + isOnGround);
            }

            if (fixtureA.getUserData() != null && fixtureB.getUserData().equals("spike")) {

                System.out.println("Foot on enemy: " + isOnGround);
            }

            if (fixtureA.getUserData() != null && fixtureB.getUserData().equals("foot")) {

                if (contact.isTouching()) {
                    // Handle the collision
                    isOnGround = true;
                }
                System.out.println("Foot on ground: " + isOnGround);
            }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        
        if (fixtureA.getUserData() != null && fixtureB.getUserData().equals("foot")) {
            isOnGround = false;
            System.out.println("Foot on ground: " + isOnGround);
        }
       
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    //GroundChecker
    public boolean isPlayerOnGround() {
        return isOnGround;
    }
}
