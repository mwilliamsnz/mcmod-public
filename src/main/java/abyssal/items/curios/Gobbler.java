package abyssal.items.curios;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.function.Consumer;

public record Gobbler(int ticksPerGobble, TagKey<Item> canGobble, ResourceLocation lootTable) {

    public void gobble(Level level, Consumer<ItemStack> doWithResult) {
        if(level.isClientSide()) {
            return;
        }
        ResourceLocation lootLocation = this.getLootTable();
//        LootTable table = level.getServer().getLootData().getLootTable(lootLocation);
//        LootParams.Builder builder = new LootParams.Builder((ServerLevel)level);
//        LootParams lootparams = builder.create(LootContextParamSets.EMPTY);
//        table.getRandomItems(lootparams, 0L, doWithResult);
    }

    public ResourceLocation getLootTable() {
        return this.lootTable;
    }

}
