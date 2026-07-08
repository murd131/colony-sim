package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    public CameraHandler cameraHandler;
    private Terrain terrain;
    private TerrainRenderer renderer;
    private SpriteBatch hudBatch = new SpriteBatch();
    private TerrainHUD HUD;
    OrthographicCamera camera = new OrthographicCamera();
    @Override
    public void show() {
        Viewport viewport = new ScreenViewport(camera);
        HUD = new TerrainHUD(hudBatch);

        cameraHandler = new CameraHandler(camera,viewport);
        cameraHandler.setWorldBounds(500*3, 500*3);
        terrain = new Terrain(500);
        int seed = 454933;
        terrain.generate(seed);
        Gdx.app.log("Screen", "show() called");
        renderer = new TerrainRenderer(terrain);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(cameraHandler);
        multiplexer.addProcessor(new InputHandler(terrain));
        multiplexer.addProcessor(HUD.getStage());
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void render(float delta) {
        Gdx.app.log("Memory", "Java heap: " + (Gdx.app.getJavaHeap() / 1024 / 1024) + " MB");
        Gdx.app.log("Memory", "Native heap: " + (Gdx.app.getNativeHeap() / 1024 / 1024) + " MB");
        renderer.render(camera);
        camera.update();
        HUD.update(terrain.get_seed());
        HUD.render();

    }

    @Override
    public void resize(int width, int height) {
        cameraHandler.resize(width, height);
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
        renderer.dispose();
        hudBatch.dispose();
        HUD.dispose();

    }
}
