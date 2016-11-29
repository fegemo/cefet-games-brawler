package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class BaseScreen implements Screen {

    protected Game game;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected SpriteBatch batch;
    protected ShapeRenderer shapes;

    public BaseScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        camera.update();
        viewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        create();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, false);
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

    }

    @Override
    public void render(float dt) {
        handleInput();

        update(dt);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        draw();
    }

    protected abstract void create();

    protected abstract void handleInput();

    protected abstract void update(float dt);

    protected abstract void draw();
}
