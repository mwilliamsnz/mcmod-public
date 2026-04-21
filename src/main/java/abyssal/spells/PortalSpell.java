package abyssal.spells;

import abyssal.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class PortalSpell extends Spell {
    protected PortalSpell(Identifier key, SpellFuelQuantity cost) {
        super(key);
    }

    @Override
    public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if(level.dimension() == Level.OVERWORLD) {// probably not End as leaving that is meant to be hard
            if(level.isClientSide()) {
                return InteractionResult.CONSUME;
            }
            ServerLevel nether = level.getServer().getLevel(Level.NETHER);
            DimensionType o = level.dimensionType();
            DimensionType n = level.dimensionType();
            double scale = DimensionType.getTeleportationScale(o, n);

            int randx = level.getRandom().nextIntBetweenInclusive(-8,8);
            int randy = level.getRandom().nextIntBetweenInclusive(-8,8);
            int randz = level.getRandom().nextIntBetweenInclusive(-8,8);
            float yRatio = (float) (player.getBlockY() - o.minY()) / o.height();
            int targetx = (int) (player.getX() * scale + randx);
            int targety = Mth.lerpInt(yRatio,  12, 110) + randy; // hardcoded values because bedrock ceiling is weird
            int targetz = (int) (player.getZ() * scale + randz);
            Main.LOGGER.debug("targeting " + targetx + "," + targety + "," + targetz);
            level.playSound(player, BlockPos.containing(player.position()), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1.0F, 0.8f+level.getRandom().nextFloat()*0.4f);
            player.teleportTo(nether, targetx, targety, targetz, Relative.ALL, player.getYRot(), player.getXRot(), true);
            level.playSound(player, BlockPos.containing(player.position()), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 1.0F, 0.8f+level.getRandom().nextFloat()*0.4f);


            BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos(targetx, targety, targety);
            boolean foundPosition = false;
            while(!foundPosition && player.getY() < 125) { // hardcoded nether ceiling value
                for(BlockPos candidate : BlockPos.MutableBlockPos.spiralAround(target, 4, Direction.EAST, Direction.NORTH)) {
                    player.setPos(candidate.getCenter());
                    if(nether.noCollision(player)) {
                        // consider also checking that the position has blocks below it, but no check is funnier
                        foundPosition = true;
                        Main.LOGGER.debug("found " + targetx + "," + targety + "," + targetz);
                        break;
                    }
                }
                target.move(Direction.UP);
            }

            return InteractionResult.CONSUME;
        }
        level.playLocalSound(player.getX(),player.getY(),player.getZ(), SoundEvents.FIRE_EXTINGUISH, player.getSoundSource(), 0.8f, 0.8f+level.getRandom().nextFloat()*0.4f, false);
        return InteractionResult.CONSUME;
    }
}
