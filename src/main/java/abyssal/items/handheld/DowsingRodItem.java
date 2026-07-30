package abyssal.items.handheld;

import abyssal.Main;
import abyssal.generation.OreDist;
import abyssal.init.ModAttachmentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;

public class DowsingRodItem extends Item {


    public DowsingRodItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (!ctx.getLevel().isClientSide()) {
            Player p = ctx.getPlayer();
            Block block = ctx.getLevel().getBlockState(ctx.getClickedPos()).getBlock();
            if(block == Blocks.STONE || block == Blocks.DEEPSLATE) {
                OreDist.OreChunkType here = ctx.getLevel().getData(ModAttachmentTypes.ORE_DIST).at(ChunkPos.containing(ctx.getClickedPos()));
                p.sendSystemMessage(Component.translatable(here.name()));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

}
