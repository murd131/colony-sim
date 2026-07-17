package io.github.colony;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;

public class TilePicker {
    private final Camera camera;
    private final int tileSize;

    public TilePicker(Camera camera, int tileSize) {
        this.camera = camera;
        this.tileSize = tileSize;
    }

    public GridPoint2 screenToTile(int screenX, int screenY) {
        Vector3 world = camera.unproject(new Vector3(screenX, screenY, 0));
        int tileX = (int) Math.floor(world.x / tileSize);
        int tileY = (int) Math.floor(world.y / tileSize);
        return new GridPoint2(tileX, tileY);
    }
}
