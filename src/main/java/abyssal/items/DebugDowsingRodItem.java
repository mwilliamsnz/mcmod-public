package abyssal.items;

import abyssal.Main;
import abyssal.generation.OreDist;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

public class DebugDowsingRodItem extends Item {


    public DebugDowsingRodItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (!ctx.getLevel().isClientSide()) {
            Player p = ctx.getPlayer();
            Block block = ctx.getLevel().getBlockState(ctx.getClickedPos()).getBlock();
            long seed = ((ServerLevel)ctx.getLevel()).getSeed();
            ChunkPos pos = new ChunkPos(ctx.getClickedPos());
            for(int i = -5; i <= 5; i++) {
                StringBuilder s = new StringBuilder();
                for(int j = -5; j <= 5; j++) {
                    OreDist.OreChunkType here = Main.oreDist.at(new ChunkPos(pos.x - i, pos.z + j), seed);
                    s.append(here.debugSymbol);
                    s.append(" ");
                }
                p.displayClientMessage(Component.translatable(s.toString()), false);
            }
            Map<OreDist.OreChunkType, Integer> count = new EnumMap<>(OreDist.OreChunkType.class);
            StringBuilder s = new StringBuilder();
            for(int i = -50; i <= 50; i++) {
                s.append("\n");
                for(int j = -50; j <= 50; j++) {
                    OreDist.OreChunkType here = Main.oreDist.at(new ChunkPos(pos.x - i, pos.z + j), seed);
                    if(here == null) {
                        here = OreDist.OreChunkType.NONE;
                    }
                    count.put(here, count.getOrDefault(here, 0)+1);
                    s.append(here.debugSymbol);
                    s.append(" ");
                }
            }
            Main.LOGGER.info(s.toString());
            for(OreDist.OreChunkType t : OreDist.OreChunkType.values()) {
                Main.LOGGER.info(t + ": " + count.get(t) * 100 / (101.0f * 101.0f));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}