package abyssal;

import abyssal.alchemy.Alchemy;
import abyssal.data.*;
import abyssal.generation.OreDist;
import abyssal.init.*;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;


@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "abyssal";

    public static final Logger LOGGER = LogManager.getLogger();

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        ModItems.OVERRIDE_ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModGeneration.FEATURES.register(modEventBus);
        ModGeneration.TREE_DECORATOR_TYPES.register(modEventBus);
        ModGeneration.PLACEMENT_MODIFIER_TYPES.register(modEventBus);
        ModGeneration.STRUCTURE_PROCESSORS.register(modEventBus);
        ModGeneration.FOLIAGE_TYPES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
        ModBlockStateProviders.BSPT.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModPotionEffectTypes.POTION_EFFECT_TYPES.register(modEventBus);
        modEventBus.addListener(this::gatherData);
        ModPOIs.POI_TYPES.register(modEventBus);


        Gems.initGems();
    }

    public void gatherData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModFeatureProvider::new);
        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModEquipmentInfoProvider::new);
        event.createBlockAndItemTags(ModBlockTagProvider::new, ModItemTagProvider::new);
        event.createProvider(ModPaintingTagProvider::new);
        event.createProvider((output, lookupProvider) -> new LootTableProvider(output, Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)
                ),
                lookupProvider));
    }

    public static Identifier rl(String path) {
        return Identifier.fromNamespaceAndPath(Main.MOD_ID, path);
    }

}
