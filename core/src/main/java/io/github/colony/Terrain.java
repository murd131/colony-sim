package io.github.colony;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.LinkedList;

class TileStat{
    int height;
    Terrain.TileType type;
    float temperature;
    protected TileStat(){
    }
    void setTemperature(float temperature){
        this.temperature = temperature;
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
    public void generate(int seed) {
        this.seed = seed;

        for (int y = 0; y < WORLD_SIZE; y++) {
            for (int x = 0; x < WORLD_SIZE; x++) {
                tileStats[x][y] = new TileStat();

                // 1. Continent shape: very low frequency, few octaves -> big coherent landmasses
                float continent = perlin.fbm(x, y, 2, 0.5f, 2.0f, 0.006f, seed);

                // 2. Detail: big frequency, a lot of octaves
                float detail = perlin.fbm(x, y, 4, 0.5f, 2.0f, 0.06f, seed + 1000);

                // combine: continent dominates large-scale shape, detail perturbs it
                float height = continent * 0.8f + detail * 0.2f;
                height = height / 0.5f;

                tileStats[x][y].setHeight((int)(height * 1000));

                generate_temperature(x, y, height, perlin);
                float temperature = tileStats[x][y].temperature;

                // 3. Moisture: separate low-frequency noise for sand vs grass vs dirt distinction
                float moisture = perlin.fbm(x, y, 3, 1f, 2.0f, 0.004f, seed + 2000);

                tileStats[x][y].setType(classify(height, temperature, moisture));
            }
        }
    }

    private TileType classify(float height, float temperature, float moisture) {
        if (height < -0.08f) return temperature < 0.85f ? TileType.DEEP_WATER : TileType.SNOW;
        if (height < 0f)    return temperature < 0.85f ? TileType.WATER      : TileType.SNOW;
        if (temperature > 0.85f) return TileType.SNOW;

        if (height < 0.08f && moisture < 0.3f && temperature < 0.2f) return TileType.SAND;
        if (height < 0.5f && moisture > 0f && temperature < 0.3f) return TileType.TROPIC;
        if (height < 0.5f  && temperature < 0.4f && moisture<-0.3f) return TileType.SAVANNA;
        if (height < 0.5f  && temperature < 0.7f) return TileType.GRASS;
        if (height < 0.65f  || temperature > 0.8f) return TileType.DIRT;
        if (height < 0.9f) return TileType.ROCK;
        return TileType.SNOW;
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
        int currentY;
        // stop when hits water
        // TODO
    }
    private void generate_temperature(int x, int y, float height, Perlin2D perlin){
        float median = (float) WORLD_SIZE/2f;
        float latitudeGradient = ((Math.abs(y - median)/median));
        float tempNoise = perlin.fbm(x,y,1,2.0f,2.0f,0.15f,seed+1);
        float temperatue = latitudeGradient + (height * 0.5f)+(tempNoise*0.15f);
        tileStats[x][y].setTemperature(temperatue);
        //Gdx.app.log("Terrain", "temperature: " + tileStats[x][y].temperature);
    }
    private void generate_moisture(int x, int y, float height, Perlin2D perlin){
        // TODO
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
        GRASS,
        TROPIC,
        SAVANNA
    }
    }
