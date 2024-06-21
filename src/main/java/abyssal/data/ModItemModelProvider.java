package abyssal.data;

import abyssal.Main;
import abyssal.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Set<Item> handheldItems = new HashSet<>();
        for(Supplier<Item> registryObject : ModItems.HANDHELD_ITEMS) {
            handheldItems.add(registryObject.get());
        }
        for(DeferredHolder<Item, ? extends Item> regOb : ModItems.ITEMS.getEntries()) {
            Item item = regOb.get();
            Main.LOGGER.info(regOb.getKey());
            if(item instanceof BlockItem) continue;
            String name = BuiltInRegistries.ITEM.getKey(item).getPath();
            if(handheldItems.contains(item)) {
                singleTexture(name, new ResourceLocation("item/handheld"), "layer0", modLoc("item/" + name));
            } else {
                singleTexture(name, new ResourceLocation("item/generated"), "layer0", modLoc("item/" + name));
            }
        }
    }
}
