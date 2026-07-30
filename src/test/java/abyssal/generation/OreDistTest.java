package abyssal.generation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OreDistTest {

    private static final long SEED = 0x5EED_C0DEL;

    @Test
    void sameSeedAndChunkIsConsistent() {
        OreDist first = new OreDist(SEED);
        OreDist second = new OreDist(SEED);

        for (int x = -16; x <= 16; x++) {
            for (int z = -100; z <= 100; z++) {
                // make some queries on only the first dist
                first.at(x, z);
            }
        }

        for (int x = -64; x <= 64; x++) {
            for (int z = -64; z <= 64; z++) {
                int checkedX = x;
                int checkedZ = z;
                assertEquals(first.at(x, z), second.at(x, z),
                        () -> "Mismatch at " + coordStr(checkedX, checkedZ));
            }
        }
    }

    @Test
    void differentSeedsHaveDifferentDistributions() {
        long otherSeed = SEED + 1;
        OreDist firstLevel = new OreDist(SEED);
        OreDist secondLevel = new OreDist(otherSeed);
        int differingX = 0;
        int differingZ = 0;
        boolean foundDifference = false;

        for (int x = -128; x <= 128 && !foundDifference; x++) {
            for (int z = -128; z <= 128; z++) {
                if (firstLevel.at(x, z) != secondLevel.at(x, z)) {
                    differingX = x;
                    differingZ = z;
                    foundDifference = true;
                    break;
                }
            }
        }

        assertTrue(foundDifference, "Distributions for different seeds should not be identical");

        OreDist.OreChunkType firstLevelResult = firstLevel.at(differingX, differingZ);
        OreDist.OreChunkType secondLevelResult = secondLevel.at(differingX, differingZ);

        assertEquals(firstLevelResult, firstLevel.at(differingX, differingZ), "Cached value should match");
        assertEquals(secondLevelResult, secondLevel.at(differingX, differingZ), "Cached value should match");
    }

    // worldgen is multithreaded so the OreDist had better work concurrently
    // particularly must ensure that we can't hit the cache while it's being
    // cleared on another thread.
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void concurrentCallsMatchSequentialResultsAcrossCacheEviction() throws Exception {
        int sideLength = 112;
        // OreDist's cache size is 10k and we need to exceed this
        int positionCount = sideLength * sideLength;
        //assertTrue(positionCount > OreDist.CACHE_SIZE);

        OreDist reference = new OreDist(SEED);
        OreDist dist = new OreDist(SEED);
        OreDist.OreChunkType[] expected = new OreDist.OreChunkType[positionCount];
        OreDist.OreChunkType[] actual = new OreDist.OreChunkType[positionCount];

        // get the grid sequentially
        for (int index = 0; index < positionCount; index++) {
            expected[index] = reference.at(x(index, sideLength), z(index, sideLength));
        }

        int threadCount = Math.min(16, Runtime.getRuntime().availableProcessors() * 2);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch start = new CountDownLatch(1);

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int thread = 0; thread < threadCount; thread++) {
            int worker = thread;
            tasks.add(() -> {
                start.await();
                // step through the grid concurrently
                for (int index = worker; index < positionCount; index += threadCount) {
                    actual[index] = dist.at(x(index, sideLength), z(index, sideLength));
                }
                return null;
            });
        }

        // run
        try {
            List<Future<Void>> futures;
            start.countDown();
            futures = executor.invokeAll(tasks);
            for (Future<Void> future : futures) {
                future.get();
            }
        } finally {
            executor.shutdownNow();
            executor.awaitTermination(3, TimeUnit.SECONDS);
        }

        for (int i = 0; i < positionCount; i++) {
            int checkedIndex = i; // temp var for lambda
            assertNotNull(actual[i], () -> "Null result at "
                    + coordStr(x(checkedIndex, sideLength), z(checkedIndex, sideLength)));
            assertEquals(expected[i], actual[i], () -> "Concurrent mismatch at "
                    + coordStr(x(checkedIndex, sideLength), z(checkedIndex, sideLength)));
        }
    }

    private static int x(int index, int sideLength) {
        int mid = sideLength / 2;
        return index % sideLength - mid;
    }

    private static int z(int index, int sideLength) {
        int mid = sideLength / 2;
        return index / sideLength - mid;
    }

    private static String coordStr(int x, int z) {
        return "[" + x + ", " + z + "]";
    }
}
