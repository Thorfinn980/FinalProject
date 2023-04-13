package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.VerticalSlice;
import com.mygdx.game.Screens.PlayScreen;

public class B2WorldCreator {

    //Take in world and a map
    public B2WorldCreator(PlayScreen screen){
        //Create body and fixture
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        World world = screen.getWorld();
        TiledMap map = screen.getTiledMap();

        //Ground
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / VerticalSlice.PPM, (rect.getY() + rect.getHeight() / 2) / VerticalSlice.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / VerticalSlice.PPM, rect.getHeight() / 2 / VerticalSlice.PPM);
            fdef.shape = shape;

            //Filter for ground
            fdef.filter.categoryBits = VerticalSlice.BIT_GROUND;
            fdef.filter.maskBits = VerticalSlice.BIT_PLAYER | VerticalSlice.BIT_ENEMY;
            body.createFixture(fdef).setUserData("ground");;
        }

        //Wall
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / VerticalSlice.PPM, (rect.getY() + rect.getHeight() / 2) / VerticalSlice.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / VerticalSlice.PPM, rect.getHeight() / 2 / VerticalSlice.PPM);
            fdef.shape = shape;

            //Filter for ground
            fdef.filter.categoryBits = VerticalSlice.BIT_GROUND;
            fdef.filter.maskBits = VerticalSlice.BIT_PLAYER | VerticalSlice.BIT_ENEMY;
            body.createFixture(fdef).setUserData("wall");;
        }

        //Exit
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / VerticalSlice.PPM, (rect.getY() + rect.getHeight() / 2) / VerticalSlice.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / VerticalSlice.PPM, rect.getHeight() / 2 / VerticalSlice.PPM);
            fdef.shape = shape;
            fdef.isSensor = true;

            //Filter for ground
            fdef.filter.categoryBits = VerticalSlice.BIT_EXIT;
            fdef.filter.maskBits = VerticalSlice.BIT_PLAYER;
            body.createFixture(fdef).setUserData("wall");;
            System.out.println("Exit Created");
        }
    }
}
