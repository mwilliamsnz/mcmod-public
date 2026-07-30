package abyssal.alchemy;

import abyssal.Main;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.*;

public class Alchemy {

    static final int xsize = 10;
    static final int ysize = 10;

    private static final long SALT = 495104;

    private static Long currentSeed; // null -> no seed

    private static Map<BoardPosition, AlchemyMaterialGroup> board;
    private static Map<AlchemyMaterial, BoardPosition> boardReverse;

    private static Map<Item, AlchemyMaterial> materialMap;

    public static AlchemyMaterialGroup materialGroupAt(BoardPosition pos) {
        return board.get(pos);
    }

    public static BoardPosition positionOf(Item i) {
        return positionOf(material(i));
    }

    public static BoardPosition positionOf(AlchemyMaterial m) {
        if(boardReverse.isEmpty()) { populateReverseBoard(); }
        return boardReverse.getOrDefault(m, null);
    }

    public static boolean isMaterial(Item i) {
        if(materialMap.isEmpty()) { populateReverseBoard(); }
        return materialMap.containsKey(i);
    }

    public static @Nullable AlchemyMaterial material(Item i) {
        if(materialMap.isEmpty()) { populateReverseBoard(); }
        return materialMap.get(i);
    }

    // This must be called AFTER items are registered
    private static void populateReverseBoard() {
        board.forEach((pos, group) -> {
            if(group.getCategory() != Category.BLOCKER) {
                for(AlchemyMaterial material : group.deferredGetInputs()) {
                    boardReverse.put(material, pos);
                    // registry getter called here, supplier finally unpacked
                    materialMap.put(material.item().get().getItem(), material);
                }
            }
        });
    }

    public static void initAlchemy(long seed) {
        if(currentSeed != null && currentSeed == seed) {
            return;
        }
        currentSeed = seed;
        Random r = new Random(seed + SALT);
        initAlchemyMaterials(r);
        AlchemyReagents.makeReagents(r);
    }

    private static void initAlchemyMaterials(Random r) {
        board = new HashMap<>();
        boardReverse = new HashMap<>();
        materialMap = new HashMap<>();

        List<AlchemyMaterialGroup> allMaterials = AlchemyMaterials.makeMaterials();

        int blockers = xsize*ysize - allMaterials.size();

        if(blockers < 0) {
            Main.LOGGER.error("Too many materials (" + allMaterials.size() + ") for grid size " + xsize + " * " + ysize);
        }

        // Categorise materials by level
        List<List<AlchemyMaterialGroup>> materialLevels = new ArrayList<>();
        for(int i = 0; i < AlchemyMaterials.MAX_TIER; i++) {
            materialLevels.add(new ArrayList<>());
        }
        for (AlchemyMaterialGroup m : allMaterials) {
            AlchemyMaterialGroup.Tier t = m.getTier();
            int randTier = t.base - 1;
            if (r.nextInt(2) == 0) { // more likely to stay at base tier
                randTier += r.nextInt(-t.maxBelow, t.maxAbove + 1);
            }
            if (randTier >= AlchemyMaterials.MAX_TIER) {
                throw new IllegalStateException("Alchemy material tier is too high");
            }
            materialLevels.get(randTier).add(m);
        }
        for (int i = 0; i < AlchemyMaterials.MAX_TIER; i++) {
            Collections.shuffle(materialLevels.get(i), r);
        }

        // Categorise materials by rank
        List<List<AlchemyMaterialGroup>> materialRanks = new ArrayList<>();
        int level = materialLevels.size()-1;
        int indexInLevel = 0;
        for(int rank = ysize-1; rank >= 0; rank--) {
            List<AlchemyMaterialGroup> thisRank = new ArrayList<>();
            for(int i = 0; i < blockersByRank(rank); i++) {
                thisRank.add(AlchemyMaterials.BLOCKER);
            }

            while(thisRank.size() < xsize) {
                if(level < 0) {
                    Main.LOGGER.info("Too few materials to fill space!");
                }
                List<AlchemyMaterialGroup> matList = materialLevels.get(level);
                if(matList.isEmpty()) { // levels with no materials will hit the getter before the check below
                    Main.LOGGER.info("level " + level + " is empty.");
                } else {
                    thisRank.add(materialLevels.get(level).get(indexInLevel));
                }
                indexInLevel++;
                if(indexInLevel >= materialLevels.get(level).size()) {
                    indexInLevel = 0;
                    level--;
                }
            }
            Collections.shuffle(thisRank, r);
            materialRanks.add(thisRank);
        }

        // assign board
        for(int i = ysize-1; i >= 0; i--) {
            for(int j = 0; j < xsize; j++) {
                AlchemyMaterialGroup m = materialRanks.get(i).get(j);
                BoardPosition pos = new BoardPosition(j,ysize-i-1);
                board.put(pos, m);
            }
        }
    }

    private static int blockersByRank(int rank) {
        return switch (rank) {
            case 9 -> 8; // 35
            case 8 -> 6; // 27
            case 7 -> 6; // 21
            case 6 -> 4; // 15
            case 5 -> 3; // 11
            case 4 -> 3; // 8
            case 3 -> 3; // 5
            case 2 -> 1; // 2
            case 1 -> 1; // 1
            default -> 0;
        };
    }

    public static class BoardPosition {
        private final int x;
        private final int y;
        public BoardPosition(int x, int y) {
            this.x = (x + xsize)  % xsize;
            this.y = Math.max(0, Math.min(y, ysize-1));
        }

        @Override
        public String toString() {
            return "BP{" +  x + ", " + y + '}';
        }

        public BoardPosition add(int dx, int dy) {
            return new BoardPosition(x+dx,y+dy);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BoardPosition p)) return false;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 1024 * result + y;
            return result;
        }
    }

    public enum Category {

        INFERNAL,
        STONE,
        EARTH,
        CRYSTAL,
        METAL,
        BLOCKER

    }

}
