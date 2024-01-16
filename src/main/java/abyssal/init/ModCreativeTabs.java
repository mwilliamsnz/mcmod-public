package abyssal.init;

import abyssal.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB_1 = TABS.register("tab1",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group." + Main.MOD_ID + ".misc"))
                    .icon(() -> new ItemStack(Items.INK_SAC))
                    .displayItems((parameters, output) -> {
                        ModItems.tab1Items.forEach(reg -> output.accept(reg.get()));
                        Gems.forAllGems(output::accept);
                        ModBlocks.BLOCKS.getEntries().forEach(reg -> output.accept(reg.get()));
                    })
//                    .withSearchBar()
                    .build());

    public static final RegistryObject<CreativeModeTab> TAB_2 = TABS.register("tab2",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group." + Main.MOD_ID + ".items"))
                    .icon(() -> new ItemStack(ModItems.GOLD_RING.get()))
                    .displayItems((parameters, output) ->
                            ModItems.tab2Items.forEach(reg -> output.accept(reg.get()))
                    )
//                    .withSearchBar()
                    .build());
}