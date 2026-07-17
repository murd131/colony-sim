package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;


public class InputHandler extends InputAdapter {
    private final Terrain terrain;

    public InputHandler(Terrain terrain) {
        this.terrain = terrain;
    }

    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.R) {
            int seed = (int) (Math.random()*10001);
            terrain.set_seed(seed);
        }
        return true;
    }
}
