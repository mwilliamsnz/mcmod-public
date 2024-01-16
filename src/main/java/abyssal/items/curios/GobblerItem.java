package abyssal.items.curios;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import top.theillusivec4.curios.api.SlotContext;

import java.util.function.Consumer;

public class GobblerItem extends ModCurioItem {

    private final int ticksPerGobble;

    private final TagKey<Item> canGobble;
    private final ResourceLocation lootTable;
    public GobblerItem(Properties props, int ticksPerGobble, TagKey<Item> canGobble, ResourceLocation lootTable) {
        super(props);
        this.ticksPerGobble = ticksPerGobble;
        this.canGobble = canGobble;
        this.lootTable = lootTable;
    }

    @Override
    public void tickCurio(SlotContext ctx) {
        LivingEntity e = ctx.entity();
        if(e.tickCount % ticksPerGobble == 0) {
            if(e instanceof Player p) {
                for(var inSlot : p.getAllSlots()) {
                    if(inSlot.is(canGobble)) {
                        inSlot.shrink(1);
                        gobble(p.level(), p.getInventory()::placeItemBackInInventory);
                        return;
                    }
                }
            }
        }
    }

    private void gobble(Level level, Consumer<ItemStack> doWithResult) {
        if(level.isClientSide()) {
            return;
        }
        ResourceLocation lootLocation = this.getLootTable();
        LootTable table = level.getServer().getLootData().getLootTable(lootLocation);
        LootParams.Builder builder = new LootParams.Builder((ServerLevel)level);
        LootParams lootparams = builder.create(LootContextParamSets.EMPTY);
        table.getRandomItems(lootparams, 0L, doWithResult);
    }

    public ResourceLocation getLootTable() {
        return this.lootTable;
    }

}
