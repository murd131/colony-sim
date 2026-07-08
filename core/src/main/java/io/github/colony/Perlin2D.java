package io.github.colony;

public class Perlin2D {
    public static class Vector2D{

        float x;

        float y;

        public Vector2D(float x,float y){

            this.x = x;

            this.y = y;

        }

        public Vector2D add(Vector2D other){

            return new Vector2D(this.x + other.x, this.y + other.y);

        }

        public float dotProduct(Vector2D other){

            return (this.x*other.x)+(this.y*other.y);

        }

    }

    public float sample(float x, float y){

        // Determine grid cell corner coordinates

        int x0 = (int)x;

        int y0 = (int)y;

        int x1 = x0+1;

        int y1 = y0+1;

        // Compute interpolation weights

        float sx = x - (float)x0;

        float sy = y - (float)y0;

        // Compute and interpolate top 2 corners

        float n0 = dotGridGradient(x0, y0, x, y);

        float n1 = dotGridGradient(x1, y0, x, y);

        float ix0 = interpolate(n0,n1,sx);

        // Compute and interpolate bottom 2 corners

        n0 = dotGridGradient(x0, y1, x, y);

        n1 = dotGridGradient(x1, y1, x, y);

        float ix1 = interpolate(n0,n1,sx);

        return interpolate(ix0,ix1,sy);

    }

    public float fbm(float coordinateX, float coordinateY, int octaves, float persistence, float lacunarity, float scale, float seed){

        float total = 0f;

        float frequency = 1f;

        float amplitude = 1f;

        float maxValue = 0f;

        coordinateX = coordinateX+seed;

        coordinateY = coordinateY+seed;

        for (int i = 0; i<octaves; i++){

            total += sample(coordinateX*scale*frequency, coordinateY*scale*frequency)*amplitude;

            maxValue += amplitude;

            amplitude *= persistence;

            frequency *= lacunarity;

        }

        return total/maxValue;

    }

    public Vector2D randomGradient(int ix, int iy){

        final int w = 32;

        final int s = w/2;

        int a = ix;

        int b = iy;

        a *= 1284157433;

        // unsigned right shift (>>>) is essential here, matching C's unsigned >>

        b ^= (a << s) | (a >>> (w - s));

        b *= 1911520717;

        a ^= (b << s) | (b >>> (w - s));

        a *= 2048419325;

        // Map the full int range to [0, 2*Pi]

        // ~(~0 >>> 1) == Integer.MIN_VALUE, but we want the magnitude 2^31,

        // so use unsigned division via a long or Integer.toUnsignedLong

        float random = (float) (Integer.toUnsignedLong(a) * (Math.PI / 0x80000000L * 2));

        // Actually simplest: scale directly using unsigned interpretation

        // random = a interpreted as unsigned, scaled to [0, 2*Pi)

        double unsignedA = Integer.toUnsignedLong(a);

        random = (float) (unsignedA * (2.0 * Math.PI / 4294967296.0)); // 4294967296 = 2^32

        Vector2D v = new Vector2D(

            (float) Math.sin(random),

            (float) Math.cos(random)

        );

        return v;

    }

    public float dotGridGradient(int ix, int iy, float x, float y){

        // Get gradient from int coordinates

        Vector2D gradient = randomGradient(ix, iy);

        // Compute the distance vector

        Vector2D distanceVector = new Vector2D(x - (float)ix, y- (float)iy);

        return gradient.dotProduct(distanceVector);

    }

    public float interpolate(float a0, float a1, float w){

        return (float) ((a1 - a0) * (3.0 - 2.0*w) * w * w + a0);

    }

}
