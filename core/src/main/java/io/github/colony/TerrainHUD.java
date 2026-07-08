package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TerrainHUD implements Disposable {
    private  Stage stage;
    private  BitmapFont font;
    private  Label seedLabel;
    public TerrainHUD(SpriteBatch batch) {
        Viewport viewport = new ScreenViewport(); // 1px = 1 unit, resizes with window
        stage = new Stage(viewport, batch);

        font = new BitmapFont(); // swap for your own font later
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        seedLabel = new Label("Seed: ", style);

        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.add(seedLabel).left().pad(4).row();


        stage.addActor(table);
    }
    public void update(int seed){
        seedLabel.setText("Seed: " + seed);
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
    }
}
