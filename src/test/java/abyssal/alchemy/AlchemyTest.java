package abyssal.alchemy;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AlchemyTest {

    private static final long SEED = 0x21CA83L;
    private static final int[] EXPECTED_BLOCKERS_BY_ROW = {0, 1, 1, 3, 3, 3, 4, 6, 6, 8};

    @Test
    void seedZero() {
        Alchemy.initAlchemy(0);
        assertCompleteBoard();
    }

    @Test
    void allMaterialsAndExpectedBlockers() {
        for (long seed : List.of(1L, SEED, -1L, Long.MAX_VALUE, Long.MIN_VALUE)) {
            Alchemy.initAlchemy(seed);

            Set<String> materialGroupNames = new HashSet<>();
            int materialGroups = 0;
            int blockers = 0;

            for (int y = 0; y < Alchemy.ysize; y++) {
                int blockersInRow = 0;
                for (int x = 0; x < Alchemy.xsize; x++) {
                    AlchemyMaterialGroup group = materialAt(x, y);
                    if (group.getCategory() == Alchemy.Category.BLOCKER) {
                        blockers++;
                        blockersInRow++;
                    } else {
                        materialGroups++;
                        materialGroupNames.add(group.name);
                    }
                }
                assertEquals(EXPECTED_BLOCKERS_BY_ROW[y], blockersInRow,
                        "Unexpected blocker count in row " + y + " for seed " + seed);
            }

            assertEquals(35, blockers, "Unexpected total blocker count for seed " + seed);
            assertEquals(65, materialGroups, "Unexpected material group count for seed " + seed);
            assertEquals(65, materialGroupNames.size(), "A material group was missing or duplicated for seed " + seed);
        }
    }

    @Test
    void seedReproducible() {
        Alchemy.initAlchemy(SEED);
        List<String> firstBoard = boardSnapshot();
        List<String> firstReagents = reagentSnapshot();

        // init another seed so the second init doesn't early-out
        Alchemy.initAlchemy(SEED + 1);
        Alchemy.initAlchemy(SEED);

        assertEquals(firstBoard, boardSnapshot());
        assertEquals(firstReagents, reagentSnapshot());
    }

    @Test
    void differentSeedsProduceDifferentLayoutsAndReagents() {
        Alchemy.initAlchemy(SEED);
        List<String> firstBoard = boardSnapshot();
        List<String> firstReagents = reagentSnapshot();

        Alchemy.initAlchemy(SEED + 1);

        assertNotEquals(firstBoard, boardSnapshot());
        assertNotEquals(firstReagents, reagentSnapshot());
    }

    @Test
    void reagentPropertyDistribution() {
        for (int i = 0; i < 500; i++) {
            // not many reagents so init a bunch of different boards
            Alchemy.initAlchemy(SEED + i);

            for (AlchemyReagent reagent : AlchemyReagents.list) {
                Set<Integer> deltas = new HashSet<>();
                for (Alchemy.Category cat : Alchemy.Category.values()) {
                    if(cat == Alchemy.Category.BLOCKER) {
                        continue;
                    }
                    AlchemyReagentEffect effect = reagent.getEffect(1, cat);
                    int delta = 1000*effect.dx + effect.dy;
                    // no-op doesn't count towards unique effects
                    if (delta != 0) {
                        deltas.add(delta);
                    }
                }
                assertTrue(deltas.size() <= 3, "Reagent should have at most 3 distinct movement effects");
                assertFalse(deltas.isEmpty(), "Reagent should have an effect on at least one category");
            }
        }

    }

    @Test
    void boardPositionWrapsAndClamps() {
        assertEquals(new Alchemy.BoardPosition(9, 0), new Alchemy.BoardPosition(-1, -5));
        assertEquals(new Alchemy.BoardPosition(0, 9), new Alchemy.BoardPosition(10, 20));
        assertEquals(new Alchemy.BoardPosition(1, 5), new Alchemy.BoardPosition(9, 5).add(2, 0));
        assertEquals(new Alchemy.BoardPosition(8, 5), new Alchemy.BoardPosition(0, 5).add(-2, 0));
    }

    @Test
    void purityMaterialSelection() {
        AlchemyMaterial smallImpure = material(10, 0.5, 0.25);
        AlchemyMaterial largeImpure = material(20, 0.5, 0.25);
        AlchemyMaterial pure = material(15, 0.9, 0.85);
        AlchemyMaterialGroup group = new AlchemyMaterialGroup("test", new AlchemyMaterialGroup.Tier(1),
                Alchemy.Category.METAL, Set.of(smallImpure, largeImpure, pure));

        assertSame(largeImpure, group.bestUnderConditions(20, 0.5));
        assertSame(pure, group.bestUnderConditions(20, 0.9));
        assertSame(smallImpure, group.bestUnderConditions(10, 0.5));
        assertNull(group.bestUnderConditions(9, 1));
    }

    @Test
    void reagentApplicationAndEfficiencyConversion() {
        Alchemy.initAlchemy(SEED);
        PositionedMaterial[] pair = findPairWithDifferentCategories();
        PositionedMaterial source = pair[0];
        PositionedMaterial destination = pair[1];
        AlchemyReagent reagent = reagentMoving(source, destination, 2, 0.75f, 0.5f);
        AlchemyState state = new AlchemyState(source.position(), 100, 4, 20);

        state.apply(reagent);

        assertEquals(destination.position(), state.position);
        assertEquals(75, state.quantity, 0.00001);
        assertEquals(45, state.impurityQuantity, 0.00001);
        assertEquals(6, state.temperature);
    }

    @Test
    void blockerBlocksButChangesTemp() {
        Alchemy.initAlchemy(SEED);
        PositionedMaterial source = findFirstMaterial(false);
        PositionedMaterial blocker = findFirstMaterial(true);
        AlchemyReagent reagent = reagentMoving(source, blocker, -3, 0.5f, 0.5f);
        AlchemyState state = new AlchemyState(source.position(), 100, 4, 20);

        state.apply(reagent);

        assertEquals(source.position(), state.position);
        assertEquals(100, state.quantity, 0.00001);
        assertEquals(20, state.impurityQuantity, 0.00001);
        assertEquals(1, state.temperature);
    }

    private static void assertCompleteBoard() {
        for (int y = 0; y < Alchemy.ysize; y++) {
            for (int x = 0; x < Alchemy.xsize; x++) {
                assertNotNull(materialAt(x, y), "Missing board entry at [" + x + ", " + y + "]");
            }
        }
        assertEquals(22, AlchemyReagents.list.size(), "Unexpected reagent count");
    }

    private static AlchemyMaterialGroup materialAt(int x, int y) {
        return Alchemy.materialGroupAt(new Alchemy.BoardPosition(x, y));
    }

    private static AlchemyMaterial material(int cost, double inherentPurity, double requiredPurity) {
        return new AlchemyMaterial(cost, inherentPurity, requiredPurity, () -> null);
    }

    private static AlchemyReagent reagentMoving(PositionedMaterial source, PositionedMaterial destination,
                                                int temperatureChange, float interEfficiency, float intraEfficiency
    ) {
        AlchemyReagent reagent = new AlchemyReagent();
        reagent.setEffect(new AlchemyReagentEffect(destination.x - source.x, destination.y - source.y,
                temperatureChange, interEfficiency, intraEfficiency),
                source.group.getCategory()
        );
        return reagent;
    }

    private static PositionedMaterial[] findPairWithDifferentCategories() {
        PositionedMaterial source = findFirstMaterial(false);
        for (int y = 0; y < Alchemy.ysize; y++) {
            for (int x = 0; x < Alchemy.xsize; x++) {
                AlchemyMaterialGroup group = materialAt(x, y);
                if (group.getCategory() != Alchemy.Category.BLOCKER
                        && group.getCategory() != source.group.getCategory()) {
                    return new PositionedMaterial[]{source, new PositionedMaterial(x, y, group)};
                }
            }
        }
        throw new AssertionError("Board has no pair of different non-blocker categories");
    }

    private static PositionedMaterial findFirstMaterial(boolean blocker) {
        for (int y = 0; y < Alchemy.ysize; y++) {
            for (int x = 0; x < Alchemy.xsize; x++) {
                AlchemyMaterialGroup group = materialAt(x, y);
                if ((group.getCategory() == Alchemy.Category.BLOCKER) == blocker) {
                    return new PositionedMaterial(x, y, group);
                }
            }
        }
        throw new AssertionError("Board has no " + (blocker ? "blocker" : "non-blocker"));
    }

    private static List<String> boardSnapshot() {
        List<String> snapshot = new ArrayList<>(Alchemy.xsize * Alchemy.ysize);
        for (int y = 0; y < Alchemy.ysize; y++) {
            for (int x = 0; x < Alchemy.xsize; x++) {
                AlchemyMaterialGroup group = materialAt(x, y);
                snapshot.add(group.name + ":" + group.getCategory());
            }
        }
        return snapshot;
    }

    private static List<String> reagentSnapshot() {
        List<String> snapshot = new ArrayList<>();
        for (AlchemyReagent reagent : AlchemyReagents.list) {
            for (Alchemy.Category category : Alchemy.Category.values()) {
                if (category == Alchemy.Category.BLOCKER) {
                    continue;
                }
                AlchemyReagentEffect effect = reagent.getEffect(0, category);
                snapshot.add(category + ":" + effect.dx + ":" + effect.dy + ":" + effect.dt
                        + ":" + effect.interEfficiency + ":" + effect.intraEfficiency);
            }
        }
        return snapshot;
    }

    private record PositionedMaterial(int x, int y, AlchemyMaterialGroup group) {
        private Alchemy.BoardPosition position() {
            return new Alchemy.BoardPosition(x, y);
        }
    }
}
