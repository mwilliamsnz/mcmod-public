package abyssal.generation;

import abyssal.Main;
import net.minecraft.world.level.ChunkPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OreDist {

    private static final float MASS_OF_ORE = 4;
    private static final int RADIUS = 2;
    private static final int CACHE_SIZE = 10000;

    private static final float ABUNDANT = 3.5f;
    private static final float COMMON = 3.7f;
    private static final float UNCOMMON = 3.8f;
    private static final float SPARSE = 4f;
    private static final float RARE = 4.2f;

    private int totalWeight = 0;

    private final long levelSeed;
    private Map<OrePos, Float> masses;
    private Map<OrePos, OreChunkType> seeds;
    private Map<OrePos, OreChunkType> ores;


    public OreDist(long seed) {
        levelSeed = seed;
        for(OreChunkType ore : OreChunkType.values()) {
            totalWeight += ore.weight;
            ore.cumulativeSum = totalWeight;
        }
        clearCache();
    }

    private void clearCache() {
        masses = new HashMap<OrePos, Float>();
        seeds = new HashMap<OrePos, OreChunkType>();
        ores = new HashMap<OrePos, OreChunkType>();
    }

    public OreChunkType at(ChunkPos pos) {
        return at(pos.x(), pos.z());
    }

    // This is synchronised because worldgen is multithreaded and so it is
    // possible hit this while the cache is being cleared on another thread
    // See OreDistTest, gives NPE if not synchronised. Shouldn't be too much
    // contention as this is pretty fast while worldgen is very slow.
    synchronized OreChunkType at(int x, int z) {
        OrePos pos = new OrePos(x, z);
        if(!ores.containsKey(pos)) { // cache miss
            if(ores.size() >= CACHE_SIZE) {
                clearCache();
            }
            ores.put(pos, findOre(pos));
        }

        return ores.get(pos);
    }

    private OreChunkType findOre(OrePos pos) {
        OreChunkType oreNear = OreChunkType.NONE;
        float massNear = 0;
        OreChunkType centre = findSeedType(pos);
        if(centre != OreChunkType.NONE) {
            return centre;
        }
        for(int x = -RADIUS; x <= RADIUS; x++) {
            for(int z = -RADIUS; z <= RADIUS; z++) {
                OrePos there = new OrePos(pos.x()+x,pos.z()+z);
                OreChunkType oreThere = findSeedType(there);
                if(oreNear == OreChunkType.NONE) {
                    oreNear = oreThere;
                } else if(oreThere != OreChunkType.NONE && oreNear != oreThere) {
                    return OreChunkType.NONE;
                }
                massNear += massAt(there) / (2*Math.abs(x) + 2*Math.abs(z) + 1);
            }
        }
        if(massNear >= oreNear.threshold) {
            return oreNear;
        }
        return OreChunkType.NONE;
    }

    private OreChunkType seedType(OrePos pos) {
        if(!seeds.containsKey(pos)) {
            seeds.put(pos, findSeedType(pos));
        }
        return seeds.get(pos);
    }

    private OreChunkType findSeedType(OrePos pos) {
        Random r = new Random(levelSeed + pos.hashCode());
        if(r.nextFloat() < 0.95) {
            return OreChunkType.NONE;
        }
        int draw = r.nextInt(totalWeight);
        for(OreChunkType ore : OreChunkType.values()) {
            if(ore.cumulativeSum > draw) {
                return ore;
            }
        }
        Main.LOGGER.error("Ore distribution draw failed!");
        return OreChunkType.NONE;
    }

    private float massAt(OrePos pos) {
        if(!masses.containsKey(pos)) {
            masses.put(pos, findMass(pos));
        }
        return masses.get(pos);
    }

    private float findMass(OrePos pos) {
        OreChunkType ore = seedType(pos);
        if(ore == OreChunkType.NONE) {
            Random r = new Random(levelSeed + 1 + pos.hashCode());
            return r.nextFloat();
        }
        return MASS_OF_ORE;
    }

    private record OrePos(int x, int z) {
        @Override
        public int hashCode() {
            int xTransform = 1664525 * x + 1013904223;
            int zTransform = 1664525 * (z ^ -559038737) + 1013904223;
            return xTransform ^ zTransform;
        }
    }

    public enum OreChunkType {
        COAL(15, 'c', ABUNDANT),
        COPPER(10, 'u', COMMON),
        SILVER(4, 's', SPARSE),
        IRON(6, 'i', UNCOMMON),
        POOR_IRON(10, 'p', COMMON),
        GARNET(4, 'r', SPARSE),
        GOLD(2, 'g', RARE),
        GEMS(3, 'm', SPARSE),
        EMERALD(4, 'e', SPARSE),
        NONE(0, '.', ABUNDANT);


        final int weight;
        final float threshold;
        int cumulativeSum;
        public final char debugSymbol;
        OreChunkType(int weight, char debugOut, float threshold) {
            this.weight = weight;
            this.debugSymbol = debugOut;
            this.threshold = threshold;
        }

        public static OreChunkType fromOrdinal(int i) {
            return switch (i) {
                case 0 -> COAL;
                case 1 -> COPPER;
                case 2 -> SILVER;
                case 3 -> IRON;
                case 4 -> POOR_IRON;
                case 5 -> GARNET;
                case 6 -> GOLD;
                case 7 -> GEMS;
                case 8 -> EMERALD;
                case 9 -> NONE;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
        }
    }

}
