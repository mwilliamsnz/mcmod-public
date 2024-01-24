package abyssal.spells;

import abyssal.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class PortalSpell extends Spell {
    protected PortalSpell(ResourceLocation key) {
        super(key);
    }

    @Override
    public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if(level.dimension() == Level.OVERWORLD && player.canChangeDimensions()) {// probably not End as leaving that is meant to be hard
            if(level.isClientSide()) {
                return InteractionResultHolder.consume(staff);
            }
            ServerLevel nether = level.getServer().getLevel(Level.NETHER);
            DimensionType o = level.dimensionType();
            DimensionType n = level.dimensionType();
            double scale = DimensionType.getTeleportationScale(o, n);

            int randx = level.random.nextIntBetweenInclusive(-8,8);
            int randy = level.random.nextIntBetweenInclusive(-8,8);
            int randz = level.random.nextIntBetweenInclusive(-8,8);
            float yRatio = (float) (player.getBlockY() - o.minY()) / o.height();
            int targetx = (int) (player.getX() * scale + randx);
            int targety = Mth.lerpInt(yRatio,  12, 110) + randy; // hardcoded values because bedrock ceiling is weird
            int targetz = (int) (player.getZ() * scale + randz);
            Main.LOGGER.info("targeting " + targetx + "," + targety + "," + targetz);
            level.playSound(player, BlockPos.containing(player.position()), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1.0F, 0.8f+level.random.nextFloat()*0.4f);
            player.teleportTo(nether, targetx, targety, targetz, RelativeMovement.ALL, player.getYRot(), player.getXRot());
            level.playSound(player, BlockPos.containing(player.position()), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 1.0F, 0.8f+level.random.nextFloat()*0.4f);


            BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos(targetx, targety, targety);
            boolean foundPosition = false;
            while(!foundPosition && player.getY() < 125) { // hardcoded nether ceiling value
                for(BlockPos candidate : BlockPos.MutableBlockPos.spiralAround(target, 4, Direction.EAST, Direction.NORTH)) {
                    player.setPos(candidate.getCenter());
                    if(nether.noCollision(player)) {
                        // consider also checking that the position has blocks below it, but no check is funnier
                        foundPosition = true;
                        Main.LOGGER.info("found " + targetx + "," + targety + "," + targetz);
                        break;
                    }
                }
                target.move(Direction.UP);
            }

            return InteractionResultHolder.consume(staff);
        }
        level.playLocalSound(player.getX(),player.getY(),player.getZ(), SoundEvents.FIRE_EXTINGUISH, player.getSoundSource(), 0.8f, 0.8f+level.random.nextFloat()*0.4f, false);
        return InteractionResultHolder.consume(staff);
    }
}
