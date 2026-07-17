package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;

public class SelectionHandler {
    private final TilePicker tilePicker;
    private final Terrain terrain;
    public GridPoint2 selectedTile;
    public SelectionHandler(TilePicker tilePicker, Terrain terrain) {
        this.tilePicker = tilePicker;
        this.terrain = terrain;
    }
    public void onClick(int x, int y){
        GridPoint2 tile = tilePicker.screenToTile(x, y);
        if (isInBounds(tile)){
            selectedTile = tile;
        }
    }
    private boolean isInBounds(GridPoint2 tile){
        return tile.x >= 0 && tile.x < Terrain.WORLD_SIZE && tile.y >= 0 && tile.y < Terrain.WORLD_SIZE;
    }
    public  GridPoint2 getSelectedTile(){
        return selectedTile;

    }
}
