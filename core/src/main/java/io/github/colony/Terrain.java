package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Terrain {
    static int WORLD_SIZE;
    private int seed;
    TileType[][] tileTypes;

    public Terrain(int worldSize) {
        WORLD_SIZE = worldSize;
        tileTypes = new TileType[WORLD_SIZE][WORLD_SIZE];
    }
    public TileType get(int x, int y){
        return tileTypes[x][y];
    }
    public void generate(int seed){
        this.seed = seed;
        Gdx.app.log("Screen", "generate() called");
        float deepwaterFrequency = -0.2f;
        float waterFrequency = -0.05f;
        float sandFrequency = 0f;
        float grassFrequency = 0.2f;
        float dirtFrequency = 0.3f;
        float rockFrequency = 0.5f;
        Perlin2D perlin = new Perlin2D();
        for (int y = 0; y < WORLD_SIZE; y++){
            for(int x = 0; x < WORLD_SIZE; x++){
                float value = perlin.fbm(x, y,5,1.0f,2.0f,0.01f,seed);
                value = value / 0.5f;
                Texture tile;
                if (value < deepwaterFrequency) {
                    tileTypes[x][y] = TileType.DEEP_WATER;
                }
                else if (value < waterFrequency) {
                    tileTypes[x][y] = TileType.WATER;
                }
                else if (value < sandFrequency){
                    tileTypes[x][y] = TileType.SAND;
                }
                else if(value < grassFrequency){
                    tileTypes[x][y] = TileType.GRASS;
                }
                 else if (value < dirtFrequency) {
                    tileTypes[x][y] = TileType.DIRT;
                }
                else if (value < rockFrequency){
                    tileTypes[x][y] = TileType.ROCK;
                }
                else
                {
                    tileTypes[x][y] = TileType.SNOW;
                }
            }
        }
    }
    public void set_seed(int seed){
        generate(seed);
    }
    public int get_seed(){
        return seed;
    }
    public enum TileType {
        DEEP_WATER,
        WATER,
        DIRT,
        ROCK,
        SNOW,
        SAND,
        GRASS
    }
    }
