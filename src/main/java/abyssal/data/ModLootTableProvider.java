package abyssal.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)
                //new LootTableProvider.SubProviderEntry(??::new, LootContextParamSets.CHEST),
                //new LootTableProvider.SubProviderEntry(??::new, LootContextParamSets.ENTITY)
        ));
    }

}
