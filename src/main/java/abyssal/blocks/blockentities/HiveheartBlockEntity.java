package abyssal.blocks.blockentities;

import abyssal.Main;
import abyssal.init.ModBlockEntityTypes;
import abyssal.init.ModBlocks;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HiveheartBlockEntity extends BlockEntity {

    private static final int MAX_STORED_ENERGY = 10000;
    private static final int PULSE_COOLDOWN = 10;
    private int energy = 3000;
    private int estimatedBiomass = 10;
    private int cooldown = 0;
    private Set<BlockPos> slaves = new HashSet<>();
    private Set<BlockPos> organs = new HashSet<>();
    // TODO spatial hash for distance queries

    private static final int WASP_SPAWN_COST = 150;
    private static final int WAX_COST = 50;
    private static final Map<Block, Integer> COSTS = Map.of(
            ModBlocks.HELLWAX.get(), WAX_COST,
            ModBlocks.CARAPACE.get(), 100,
            ModBlocks.HIVEHEART_DUMMY.get(), 800,
            ModBlocks.WASP_PORT.get(), 250
    );

    public HiveheartBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.HIVEHEART.get(), pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.slaves.clear();
        input.read("slaves", BlockPos.CODEC.listOf()).orElse(List.of()).forEach(this::addSlave);
        input.read("organs", BlockPos.CODEC.listOf()).orElse(List.of()).forEach(this::addOrgan);
        this.energy = input.read("energy", Codec.INT).orElse(100);
        this.estimatedBiomass = input.read("biomass", Codec.INT).orElse(10);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.store("slaves", BlockPos.CODEC.listOf(), List.copyOf(slaves));
        output.store("organs", BlockPos.CODEC.listOf(), List.copyOf(organs));
        output.storeNullable("energy", Codec.INT, this.energy);
        output.storeNullable("biomass", Codec.INT, this.estimatedBiomass);
    }

    private static float dsq(BlockPos rootPos, BlockPos pos) {
        float dy = pos.getY() - rootPos.getY();
        float dx = pos.getX() - rootPos.getX();
        float dz = pos.getZ() - rootPos.getZ();
        return  dx*dx + dz*dz + dy*dy;
    }

    private float expandValue(BlockPos pos, BlockPos localHeartPos, ServerLevel level) {
        float dy = Mth.abs(pos.getY() - localHeartPos.getY());
        float dx = pos.getX() - localHeartPos.getX();
        float dz = pos.getZ() - localHeartPos.getZ();
        float d = dx*dx + dz*dz + dy*dy/9.0f;

        int air = 0;
        for (Direction probeDir : Direction.values()) {
            BlockPos probePos = pos.relative(probeDir);
            BlockState probeState = level.getBlockState(probePos);
            BlockState distantProbeState = level.getBlockState(probePos.relative(probeDir));
            if(probeState.isAir()) {
                air += 1;
            } else if(probeState.is(Blocks.GLOWSTONE)) {
                return -1;
            } else if(distantProbeState.is(Blocks.GLOWSTONE)) {
                return -1;
            } else if(probeState.is(ModBlocks.WASP_PORT.get()) || distantProbeState.is(ModBlocks.WASP_PORT.get())) {
                int airAroundPort = 0;
                for (Direction probe2Dir : Direction.values()) {
                    BlockPos probe2Pos = probePos.relative(probe2Dir);
                    BlockState probe2State = level.getBlockState(probe2Pos);
                    airAroundPort += probe2State.isAir()? 1 : 0;
                }
                if (airAroundPort < 2) { // 1 air block is this current block
                    return -1;
                }
            }
        }

        if(d < 3.5*3.5) {
            return 70 - 10*d - 5*air; // fill out surroundings first
        }
        float value = 3*dy*dy - d + 5 + 2*(5-air);
        return value * Mth.clamp(3 - estimatedBiomass/3000, 0, 1);
    }

    private float heartPositionalValue(BlockPos pos, BlockPos rootPos, ServerLevel level, int airExposure) {
        float d = dsq(pos, rootPos);
        BlockPos closest = rootPos;
        // TODO spatial hash
        for (BlockPos heartPos : slaves) {
            float d2 = dsq(pos, heartPos);
            if (d < d2) {
                d = d2;
                closest = heartPos;
            }

            if (d < 3.5*3.5) break;
        }
        if (d < 3.5*3.5) {
            return 0;
        }

        int dy = Mth.abs(closest.getY() - pos.getY());
        if(dy <= 1) {
            return 0;
        }
        int dx = Mth.abs(closest.getX() - pos.getX());
        int dz = Mth.abs(closest.getZ() - pos.getZ());
        return Math.abs(dx*dx + dz*dz - 2.3f*2.3f)*3 + dy*2 + airExposure;
    }

    private float portPositionalValue(BlockPos pos, BlockPos rootPos, ServerLevel level, int airExposure) {
        if(airExposure == 0) {
            return -1;
        }
        float d = dsq(pos, rootPos);
        // TODO spatial hash
        for (BlockPos organPos : organs) {
            d = Math.min(d, dsq(pos, organPos));
            if (d < 2.5*2.5) break;
        }
        if (d < 2.5*2.5) {
            return -1;
        }

        int near = 0;
        for (BlockPos neighbour : BlockPos.withinManhattan(pos, 1, 1, 1)) {
            BlockState s = level.getBlockState(neighbour);
            if (s.is(ModBlocks.CARAPACE)) {
                near += 4;
            } if (s.is(ModBlocks.HELLWAX)) {
                near += 2;
            } else if (s.is(ModBlocks.WASP_PORT)) {
                near -= 10;
            } else if (s.is(Blocks.LAVA)) {
                near -= 15;
            }
        }

        return 5 + near + d;
    }

    private float carapacePositionalValue(BlockPos pos, BlockPos rootPos, ServerLevel level, int airExposure) {
        if(airExposure == 0 || level.getBlockState(pos).is(ModBlocks.CARAPACE)) {
            return 0;
        }
        int near = 0;
        for (BlockPos neighbour : BlockPos.withinManhattan(pos, 1, 1, 1)) {
            BlockState s = level.getBlockState(neighbour);
            if (s.is(ModBlocks.CARAPACE)) {
                near += 2;
            } else if (s.is(ModBlocks.WASP_PORT)) {
                near += 5;
            } else if (s.is(ModBlocks.HIVEHEART) || s.is(ModBlocks.HIVEHEART_DUMMY)) {
                near -= 20;
            } else if (s.is(Blocks.LAVA)) {
                near += 10;
            }
        }

        int airValue = airExposure - Math.abs(airExposure - 2);

        return near + airValue * 5;
    }

    private Pair<Float, BlockState> differentiateValue(BlockPos pos, BlockPos rootPos, ServerLevel level, boolean wantsHeart, boolean wantsPort) {
        float dy = Mth.abs(pos.getY() - rootPos.getY());
        float dx = pos.getX() - rootPos.getX();
        float dz = pos.getZ() - rootPos.getZ();
        float d = dx*dx + dz*dz + dy*dy;

        if(d < 2*2) {
            return Pair.of(-1.f, null);
        }

        int air = 0;
        for (Direction probeDir : Direction.values()) {
            BlockPos probePos = pos.relative(probeDir);
            BlockState probeState = level.getBlockState(probePos);
            if(probeState.isAir()) {
                air += 1;
            }
        }
        float heartV = -1;
        if (wantsHeart) {
            heartV = heartPositionalValue(pos, rootPos, level, air);
        }
        float portV = -1;
        if (wantsPort) {
            portV = portPositionalValue(pos, rootPos, level, air);
        }
        float carapaceV = carapacePositionalValue(pos, rootPos, level, air);

        if(carapaceV >= portV && carapaceV >= heartV) {
            return Pair.of(carapaceV + 15, ModBlocks.CARAPACE.get().defaultBlockState());
        } else if (portV >= heartV && portV > 0) {
            return Pair.of(portV + 100, ModBlocks.WASP_PORT.get().defaultBlockState());
        }

        return Pair.of(heartV > 0 ? heartV + 100 : -1, ModBlocks.HIVEHEART_DUMMY.get().defaultBlockState());
    }

    private boolean canExpandTo(BlockState state) {
        return state.isAir();
    }

    private boolean canPropagateThrough(BlockState state) {
        return state.is(ModBlocks.HELLWAX) || state.is(ModBlocks.CARAPACE);
    }

    private int pulse(BlockState rootState, ServerLevel level, BlockPos masterPos, RandomSource rand) {

        AtomicBoolean createdWasp = new AtomicBoolean(false);
        for (BlockPos organPos : organs) {
            level.getBlockEntity(organPos, ModBlockEntityTypes.WASP_PORT.get()).ifPresent(be -> {
                if(be.getEstimatedOccupancy() < 0.2f) {
                    be.storeWasp(WaspPortBlockEntity.Occupant.create(rand.nextInt(100)));
                    be.setEstimatedOccupancy(1);
                    createdWasp.set(true);
                }
            });
            if(createdWasp.get()) {
                return WASP_SPAWN_COST;
            }
        }

        List<Direction> dirs = List.of(Direction.UP,Direction.DOWN,Direction.UP,Direction.DOWN,
                Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

        float bestExpandV = 0;
        BlockPos bestExpandPos = null;
        float bestDiffV = 0;
        BlockPos bestDiffPos = null;
        BlockState bestDiffRes = null;

        boolean wantsHeart = organs.size()*5 + estimatedBiomass > (slaves.size() + 1) * 80;
        boolean wantsPort = organs.size() < 2*slaves.size() + 3 && organs.size()*10 < estimatedBiomass;
        ArrayList<BlockPos> heartsList = new ArrayList<>(slaves);
        heartsList.add(worldPosition);

        for (int i = 0; i < 10; i++) {
            BlockPos currentRoot;
            while(true) {
                int r = rand.nextInt(heartsList.size());
                currentRoot = heartsList.get(r);
                BlockState s = level.getBlockState(currentRoot);
                if(s.is(ModBlocks.HIVEHEART) || s.is(ModBlocks.HIVEHEART_DUMMY)) {
                    break;
                }
                slaves.remove(heartsList.get(r));
                heartsList.remove(r);
            }
            BlockPos.MutableBlockPos here = currentRoot.mutable();
            Direction lastDir = dirs.get(rand.nextInt(2));
            int slaveContribution = slaves.size() > 12 ? 9 + slaves.size()/4 : slaves.size();
            int trajectories = 5 + slaveContribution; // 1 per slave up to 12, then 1 per 4 slaves
            for (int j = 0; j < trajectories; j++) {
                WeightedList.Builder<Direction> candidates = WeightedList.builder();
                for (Direction probeDir : Direction.values()) {
                    BlockPos probePos = here.relative(probeDir);
                    BlockState probeState = level.getBlockState(probePos);
                    if (canExpandTo(probeState)) {
                        float v = expandValue(here, currentRoot, level);
                        if(v > bestExpandV) {
                            bestExpandPos = probePos.immutable();
                            bestExpandV = v;
                        }
                    } else if(canPropagateThrough(probeState)) {
                        int w = 3; // 15% * 4
                        if(probeDir.equals(lastDir)) {
                            w = 6; // 30%
                        } else if(probeDir.equals(lastDir.getOpposite())) {
                            w = 2; // 10%
                        }
                        candidates.add(probeDir, w);
                    }
                }
                var r = candidates.build();
                if(r.isEmpty()) {
                    break;
                }
                Direction dir = r.getRandomOrThrow(rand);
                here.move(dir);

                var res = differentiateValue(here, masterPos, level, wantsHeart, wantsPort);
                float v = res.getFirst();
                if(v > bestDiffV) {
                    bestDiffPos = here.immutable();
                    bestDiffV = v;
                    bestDiffRes = res.getSecond();
                }
            }
        }

        if (bestDiffV > bestExpandV && bestDiffV > 0) {
            level.setBlockAndUpdate(bestDiffPos, bestDiffRes);
            if(bestDiffRes.is(ModBlocks.HIVEHEART_DUMMY)) {
                slaves.add(bestDiffPos);
                level.getBlockEntity(bestDiffPos, ModBlockEntityTypes.HIVE_ORGAN.get()).ifPresent(be -> {
                    be.linkToHeart(masterPos);
                });
            } else if (bestDiffRes.is(ModBlocks.WASP_PORT)) {
                organs.add(bestDiffPos);
                level.getBlockEntity(bestDiffPos, ModBlockEntityTypes.WASP_PORT.get()).ifPresent(be -> {
                    for (int i = 0; i < 2; i++) {
                        be.storeWasp(WaspPortBlockEntity.Occupant.create(rand.nextInt(100)));
                    }
                    be.linkToHeart(masterPos);
                });
            }
            estimatedBiomass += 1;
            return COSTS.getOrDefault(bestDiffRes.getBlock(), 200);
        } else if (bestExpandV > 0) {
            estimatedBiomass += 1;
            level.setBlockAndUpdate(bestExpandPos, ModBlocks.HELLWAX.get().defaultBlockState());
            return WAX_COST;
        }
        return 1; // failed pulse depletes energy
    }

    public void addEnergy(int energy) {
        this.energy += energy;
        if(this.energy > MAX_STORED_ENERGY) {
            this.energy = MAX_STORED_ENERGY;
        }
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState state, HiveheartBlockEntity hive) {
        if(level instanceof ServerLevel sl) {
            if(hive.cooldown > 0) {
                hive.cooldown--;
            } else if(hive.energy > 20 && sl.getGameRules().get(GameRules.SPREAD_VINES)) {
                hive.energy -= hive.pulse(state, sl, blockPos, level.getRandom());
                hive.cooldown = PULSE_COOLDOWN;
            }
            if(level.getRandom().nextInt(20) == 0) {
                hive.addEnergy(2); // ~1 wasp per 25s
            }

        }
        if(level.getRandom().nextDouble() < 0.005) {
            double x = blockPos.getX() + 0.5;
            double y = blockPos.getY();
            double z = blockPos.getZ() + 0.5;
            level.playSound(null, x, y, z, SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public void removeOrgan(BlockPos worldPosition) {
        organs.remove(worldPosition);
        estimatedBiomass -= 1;
    }

    public void addSlave(BlockPos worldPosition) {
        slaves.add(worldPosition);
    }

    public void addOrgan(BlockPos worldPosition) {
        organs.add(worldPosition);
    }

    public void findSuccessor() {
        Iterator<BlockPos> iterator = slaves.iterator();
        while(iterator.hasNext()) {
            BlockPos newMasterPos = iterator.next();
            BlockState s = level.getBlockState(newMasterPos);
            if(s.is(ModBlocks.HIVEHEART_DUMMY)) {
                level.setBlockAndUpdate(newMasterPos, ModBlocks.HIVEHEART.get().defaultBlockState());
                level.getBlockEntity(newMasterPos, ModBlockEntityTypes.HIVEHEART.get()).ifPresentOrElse(be -> {
                        be.estimatedBiomass = estimatedBiomass - 5;
                        be.energy = energy - 10;
                        be.organs = organs;
                        iterator.remove();
                        be.slaves = slaves;
                        for(BlockPos organPos : be.organs) {
                            level.getBlockEntity(organPos, ModBlockEntityTypes.WASP_PORT.get()).ifPresentOrElse(organ -> {
                                organ.linkToHeart(newMasterPos);
                            }, () -> { // else
                                Main.LOGGER.info("HiveheartBlockEntity.findSuccessor: Hive organ blockentity missing");
                            });
                        }
                        for(BlockPos newSlavePos : be.slaves) {
                            level.getBlockEntity(newSlavePos, ModBlockEntityTypes.HIVE_ORGAN.get()).ifPresentOrElse(organ -> {
                                organ.master = newMasterPos;
                            }, () -> { // else
                                Main.LOGGER.info("HiveheartBlockEntity.findSuccessor: Hive slave blockentity missing");
                            });
                        }
                    }, () -> { // else
                        Main.LOGGER.info("HiveheartBlockEntity.findSuccessor: Hive blockentity missing");
                    });
                return;
            }
        }
    }

    public String debugString() {
        return "e=" + energy + ", slaves: " + slaves.size() + ", ports: " + organs.size() + ", biomass approx " + estimatedBiomass;
    }
}
