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

    private Map<ChunkPos, Float> masses;
    private Map<ChunkPos, OreChunkType> seeds;
    private Map<ChunkPos, OreChunkType> ores;


    public OreDist() {
        for(OreChunkType ore : OreChunkType.values()) {
            totalWeight += ore.weight;
            ore.cumulativeSum = totalWeight;
        }
        clearCache();
    }

    private void clearCache() {
        masses = new HashMap<ChunkPos, Float>();
        seeds = new HashMap<ChunkPos, OreChunkType>();
        ores = new HashMap<ChunkPos, OreChunkType>();
    }

    public OreChunkType at(ChunkPos pos, long seed) {
        if(!ores.containsKey(pos)) {
            ores.put(pos, findOre(pos, seed));
        }
        return ores.get(pos);
    }

    private OreChunkType findOre(ChunkPos pos, long seed) {
        OreChunkType oreNear = OreChunkType.NONE;
        float massNear = 0;
        OreChunkType centre = findSeedType(pos, seed);
        if(centre != OreChunkType.NONE) {
            return centre;
        }
        for(int x = -RADIUS; x <= RADIUS; x++) {
            for(int z = -RADIUS; z <= RADIUS; z++) {
                ChunkPos there = new ChunkPos(pos.x()+x,pos.z()+z);
                OreChunkType oreThere = findSeedType(there, seed);
                if(oreNear == OreChunkType.NONE) {
                    oreNear = oreThere;
                } else if(oreThere != OreChunkType.NONE && oreNear != oreThere) {
                    return OreChunkType.NONE;
                }
                massNear += massAt(there, seed) / (2*Math.abs(x) + 2*Math.abs(z) + 1);
            }
        }
        if(massNear >= oreNear.threshold) {
            return oreNear;
        }
        if(ores.size() > CACHE_SIZE) {
            clearCache();
        }
        return OreChunkType.NONE;
    }

    private OreChunkType seedType(ChunkPos pos, long seed) {
        if(!seeds.containsKey(pos)) {
            seeds.put(pos, findSeedType(pos, seed));
        }
        return seeds.get(pos);
    }

    private OreChunkType findSeedType(ChunkPos pos, long seed) {
        Random r = new Random(seed + pos.hashCode());
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

    private float massAt(ChunkPos pos, long seed) {
        if(!masses.containsKey(pos)) {
            masses.put(pos, findMass(pos, seed));
        }
        return masses.get(pos);
    }

    private float findMass(ChunkPos pos, long seed) {
        OreChunkType ore = seedType(pos, seed);
        if(ore == OreChunkType.NONE) {
            Random r = new Random(seed + 1 + pos.hashCode());
            return r.nextFloat();
        }
        return MASS_OF_ORE;
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
