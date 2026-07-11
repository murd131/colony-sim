package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

class TileStat{
    int height;
    Terrain.TileType type;
    protected TileStat(){
    }
    void setHeight(int height){
        this.height = height;
    }
    void setType(Terrain.TileType type){
        this.type = type;
    }

}
public class Terrain {
    static int WORLD_SIZE;
    private int seed;
    TileStat[][] tileStats;
    Perlin2D perlin;
    public Terrain(int worldSize) {
        WORLD_SIZE = worldSize;
        tileStats = new TileStat[WORLD_SIZE][WORLD_SIZE];
        perlin = new Perlin2D();
    }

    public TileType get(int x, int y){
        return tileStats[x][y].type;
    }
    public void generate(int seed){
        this.seed = seed;
        float deepwaterFrequency = -0.2f;
        float waterFrequency = -0.05f;
        float sandFrequency = 0f;
        float grassFrequency = 0.2f;
        float dirtFrequency = 0.3f;
        float rockFrequency = 0.5f;
        for (int y = 0; y < WORLD_SIZE; y++){
            for(int x = 0; x < WORLD_SIZE; x++){
                float value = perlin.fbm(x, y,5,1.0f,2.0f,0.01f,seed);
                value = value / 0.5f;
                tileStats[x][y] = new TileStat();
                tileStats[x][y].setHeight((int)(value*1000));
                Texture tile;
                if (value < deepwaterFrequency) {
                    tileStats[x][y].setType(TileType.DEEP_WATER);
                }
                else if (value < waterFrequency) {
                    tileStats[x][y].setType(TileType.WATER);
                }
                else if (value < sandFrequency){
                    tileStats[x][y].setType(TileType.SAND);
                }
                else if(value < grassFrequency){
                    tileStats[x][y].setType(TileType.GRASS);
                }
                 else if (value < dirtFrequency) {
                    tileStats[x][y].setType(TileType.DIRT);
                }
                else if (value < rockFrequency){
                    tileStats[x][y].setType(TileType.ROCK);
                }
                else
                {
                    tileStats[x][y].setType(TileType.SNOW);
                }
            }
        }
        //generate_rivers(2);
    }
    private LinkedList<Integer> find_peaks(int riverNumber) {
        LinkedList<Integer> peaks = new LinkedList();
        for (int x = 0; x < WORLD_SIZE; x++) {
            int largest = 0;
            int largestY = 0;
            for (int y = 0; y < WORLD_SIZE; y++) {
                if (tileStats[x][y].height > largest) {
                    largest = tileStats[x][y].height;
                    largestY = y;
                }
            }
            peaks.add(largestY);
        }
        ArrayList<Integer> finalPeaks = new ArrayList<Integer>();
        for (int i = 0; i <= riverNumber; i++){
            finalPeaks.set(i, (int) Math.random());
        }
            return peaks;

    }
    public void generate_rivers(int riverNumber){
        LinkedList<Integer> peaks = find_peaks(riverNumber);
        int currentX;
        int currenyY;
        // stop when hits water
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
