package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.VerticalSlice;
import com.mygdx.game.Sprites.Hero;
import com.mygdx.game.Sprites.Mob;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.Rumble;

public class PlayScreen implements Screen{


    // Game objects
    private Hero hero;
    private TextureAtlas atlas;
    private TextureAtlas atlas2;

    // Camera and game world
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private VerticalSlice game;
    private World world;

    // Map objects
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    // Box2D variables
    private Box2DDebugRenderer b2dr;

    private Mob mob1;
    private Mob mob2;
    private Mob mob3;
    private Mob mob4;
    private Mob mob5;
    private Mob mob6;
    private Mob mob7;

    private Music music;


    public PlayScreen(VerticalSlice game) {
        atlas = new TextureAtlas("characters/Hero/HeroTwo.atlas");
        atlas2 = new TextureAtlas("characters/Slime.atlas");
        
        this.game = game;

        // camera to follow player through the world
        gameCam = new OrthographicCamera();

        // gamePort = new FitViewport(width/ VerticalSlice.PPM, height / VerticalSlice.PPM, gameCam);

        gamePort = new FitViewport(VerticalSlice .V_WIDTH / VerticalSlice.PPM, VerticalSlice .V_HEIGHT / VerticalSlice.PPM, gameCam);


        // Load tmx map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/prototype.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / VerticalSlice.PPM);

        // gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Box2D setup
        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);


        //We create the player in our world
        hero = new Hero(world, this);
        //!hero2 = new Hero(world, this);
        
        music = VerticalSlice.manager.get("audio/music/dungeon_theme.wav", Music.class);
        music.setLooping(true);
        music.play();
        

        mob1 = new Mob(this, 650, 300, atlas2, hero);
        mob2 = new Mob(this, 1250, 450, atlas2, hero);
        mob3 = new Mob(this, 1350, 450, atlas2, hero);
        mob4 = new Mob(this, 2500, 450, atlas2, hero);
        mob5 = new Mob(this, 300, 1290, atlas2, hero);
        mob6 = new Mob(this, 300, 1300, atlas2, hero);
        mob7 = new Mob(this, 300, 1320, atlas2, hero);
    }

    public boolean gameOver(){
        if(hero.currentState == Hero.State.DEAD && hero.getStateTimer() > .5){
            return true;
        }
        return false;
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (Rumble.getRumbleTimeLeft() > 0){
            Rumble.tick(Gdx.graphics.getDeltaTime());
            gameCam.translate(Rumble.getPos());
        }

        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the TiledMap
        mapRenderer.setView(gameCam);
        mapRenderer.render();

        //Debug lines for Box2D
        // b2dr.render(world, gameCam.combined);

        // Render the player
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin(); 
        hero.draw(game.batch);
        mob1.draw(game.batch);
        mob2.draw(game.batch);
        mob3.draw(game.batch);
        mob4.draw(game.batch);
        mob5.draw(game.batch);
        mob6.draw(game.batch);
        mob7.draw(game.batch);
        //hero2.draw(game.batch);
        game.batch.end();

        if(gameOver()){
            game.setScreen(new GameOver(game));
            dispose();
        }
    }

    public void update(float dt){
        //Handle user input first
        hero.handleUserInput(dt);

        //Step the physics simulation forward at a rate of 60hz
        world.step(1/60f, 6, 2);

        //Update the player
        hero.update(dt);
        mob1.update(dt);
        mob2.update(dt);
        mob3.update(dt);
        mob4.update(dt);
        mob5.update(dt);
        mob6.update(dt);
        mob7.update(dt);

        //CameraMan is a method that follows the player and updates the camera
        CameraMan();

        //Update the map renderer
        mapRenderer.setView(gameCam);
    }

    public TiledMap getTiledMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    public void CameraMan(){
        Vector3 prevCamPos = gameCam.position;

        //Use Interpolation to smooth out the camera movement
        gameCam.position.x = prevCamPos.x + (hero.b2body.getPosition().x - prevCamPos.x) * .15f;
        gameCam.position.y = prevCamPos.y + (hero.b2body.getPosition().y - prevCamPos.y) * .15f;

        gameCam.update();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public Object getPlayer() {
        return null;
    }
}
