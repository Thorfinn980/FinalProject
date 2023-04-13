package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.VerticalSlice;

public class GameOver implements Screen {

    private Viewport viewport;
    private Game game;

    TextureRegion intro;
	SpriteBatch batch;
	float time = 0;

	BitmapFont font;

    public GameOver(Game game){
        this.game = game;
        viewport = new FitViewport(VerticalSlice.V_WIDTH, VerticalSlice.V_HEIGHT, new OrthographicCamera());
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
		font = new BitmapFont();
        font.getData().setScale(2);
        intro = new TextureRegion(new Texture(Gdx.files.internal("GameOver.jpg")), 0, 0, 800, 600 );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.0f, .0f, .0f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(intro, (Gdx.graphics.getWidth() - intro.getRegionWidth()) / 2f, (Gdx.graphics.getHeight() - intro.getRegionHeight()) / 2f);
        font.draw(batch, "Press any key to exit", (Gdx.graphics.getWidth() - 220)/2, (Gdx.graphics.getHeight()/2)-30);
        batch.end();

		time += delta;
		if (time > 1) {
			if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched()) {
                Gdx.app.exit(); // Exit the game
			}
		}
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
		batch.dispose();
		intro.getTexture().dispose();
    }

    @Override
    public void dispose() {
    }

    
}
