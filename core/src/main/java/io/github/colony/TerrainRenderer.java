package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TerrainRenderer {
    private static final int TILE_SIZE = 4;
    private final float OVERDRAW = 0.02f;
    private final SpriteBatch batch = new SpriteBatch();
    private final Terrain terrain;
    private final TextureAtlas atlas;
    private final Texture selection;
    private final TextureRegion[] tileTextures;
    SelectionHandler selectionHandler;

    public TerrainRenderer(Terrain terrain, SelectionHandler selectionHandler) {
        this.terrain = terrain;
        this.selectionHandler = selectionHandler;
        selection = new Texture(Gdx.files.internal("selection.png"));
        atlas = new TextureAtlas(Gdx.files.internal("atlas2/tiles.atlas"));

        tileTextures = new TextureRegion[Terrain.TileType.values().length]; // no "TextureRegion[]" prefix — reuse the field
        tileTextures[Terrain.TileType.SNOW.ordinal()]  = atlas.findRegion("snow");
        tileTextures[Terrain.TileType.WATER.ordinal()] = atlas.findRegion("water");
        tileTextures[Terrain.TileType.SAND.ordinal()]  = atlas.findRegion("sand");
        tileTextures[Terrain.TileType.ROCK.ordinal()]  = atlas.findRegion("rock");
        tileTextures[Terrain.TileType.GRASS.ordinal()] = atlas.findRegion("grass");
        tileTextures[Terrain.TileType.DIRT.ordinal()]  = atlas.findRegion("dirt");
        tileTextures[Terrain.TileType.DEEP_WATER.ordinal()]  = atlas.findRegion("deep_water");
        tileTextures[Terrain.TileType.TROPIC.ordinal()]  = atlas.findRegion("tropics");
        tileTextures[Terrain.TileType.SAVANNA.ordinal()]  = atlas.findRegion("brown_grass");

    }

    public void render(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float offset = 15;
        batch.begin();

        float camLeft   = camera.position.x - camera.viewportWidth  / 2f * camera.zoom;
        float camRight  = camera.position.x + camera.viewportWidth  / 2f * camera.zoom;
        float camBottom = camera.position.y - camera.viewportHeight / 2f * camera.zoom;
        float camTop    = camera.position.y + camera.viewportHeight / 2f * camera.zoom;

        int startX = Math.max(0, (int) (camLeft / TILE_SIZE) - (int) offset);
        int endX   = Math.min(Terrain.WORLD_SIZE, (int) (camRight / TILE_SIZE) + (int) offset * 2);
        int startY = Math.max(0, (int) (camBottom / TILE_SIZE) - (int) offset);
        int endY   = Math.min(Terrain.WORLD_SIZE, (int) (camTop / TILE_SIZE) + (int) offset * 2);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                TextureRegion region = getRegion(terrain.get(x, y));
                if (region != null) {
                    batch.draw(region, x * TILE_SIZE - OVERDRAW, y * TILE_SIZE - OVERDRAW, TILE_SIZE + OVERDRAW*2, TILE_SIZE + OVERDRAW*2);
                }
            }
        }
        if (selectionHandler.getSelectedTile() != null) {
            batch.draw(selection, selectionHandler.getSelectedTile().x * TILE_SIZE, selectionHandler.getSelectedTile().y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }


        batch.end();
    }

    private TextureRegion getRegion(Terrain.TileType tileType) {
        return tileTextures[tileType.ordinal()];
    }

    public void dispose() {
        batch.dispose();
        atlas.dispose();
        selection.dispose();
    // this disposes the underlying packed texture too — no separate Texture fields needed
    }
}
