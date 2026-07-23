package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class TerrainHUD implements Disposable {
    private final Stage stage;
    private final BitmapFont font;
    private final Label seedLabel;
    private final Label typeLabel;
    private final Label heightLabel;
    private final Terrain terrain;
    private Texture panelTexture; // keep a handle so we can dispose it
    SelectionHandler selectionHandler;

    public TerrainHUD(SpriteBatch batch, Terrain terrain, SelectionHandler selectionHandler) {
        // Reference "design resolution" for the UI. Pick whatever your HUD was designed at.
        Viewport viewport = new FitViewport(960, 540);
        stage = new Stage(viewport, batch);
        this.selectionHandler = selectionHandler;
        this.terrain = terrain;

        font = generateFont(24); // generated at 2x the size you plan to display for crispness

        // Initialising labels
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        seedLabel = new Label("Seed: ", style);
        typeLabel = new Label("", style);
        heightLabel = new Label("", style);

        // Modifying labels

        heightLabel.setFontScale(0.7f);

        // --- Selection panel with visible background ---
        Table selectionTable = new Table();
        selectionTable.add(typeLabel).pad(6);
        selectionTable.row();
        selectionTable.add(heightLabel).left().pad(6);


        Table panel = new Table();
        panel.setBackground(solidColor(new Color(0.1f, 0.1f, 0.1f, 0.65f)));
        panel.add(selectionTable).pad(8);

        Table selectionRoot = new Table();
        selectionRoot.setFillParent(true);
        selectionRoot.bottom().right();
        selectionRoot.add(panel).pad(12);

        Table seedTable = new Table();
        seedTable.top().left();
        seedTable.setFillParent(true);
        seedTable.add(seedLabel).left().pad(4).row();

        stage.addActor(seedTable);
        stage.addActor(selectionRoot);
    }

    private BitmapFont generateFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BigBlueTermPlusNerdFont-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.genMipMaps = true;
        BitmapFont f = generator.generateFont(param);
        generator.dispose();
        return f;
    }

    private Drawable solidColor(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        panelTexture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(panelTexture));
    }

    public void update(int seed){
        seedLabel.setText("Seed: " + seed);
        if (selectionHandler.getSelectedTile() != null) {
            typeLabel.setText(terrain.getName(selectionHandler.getSelectedX(), selectionHandler.getSelectedY()));
            heightLabel.setText("Height:"+ terrain.getHeight(selectionHandler.getSelectedX(), selectionHandler.getSelectedY())+"m");
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() { return stage; }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        if (panelTexture != null) panelTexture.dispose();
    }
}