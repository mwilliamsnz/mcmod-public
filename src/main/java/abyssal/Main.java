package abyssal;

import abyssal.alchemy.Alchemy;
import abyssal.capability.CombatTimeCapability;
import abyssal.data.*;
import abyssal.entity.FishPainting;
import abyssal.generation.OreDist;
import abyssal.init.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;


@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "abyssal";

    public static final Logger LOGGER = LogManager.getLogger();

    public static OreDist oreDist = new OreDist();

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public Main() {
        ModItems.ITEMS.register(modEventBus);
        ModItems.OVERRIDE_ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModFluids.FLUID_TYPES.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModGeneration.FEATURES.register(modEventBus);
        ModGeneration.TREE_DECORATOR_TYPES.register(modEventBus);
        ModGeneration.PLACEMENT_MODIFIER_TYPES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        FishPainting.PAINTING_VARIANTS.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
        ModBlockStateProviders.BSPT.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        modEventBus.addListener(CombatTimeCapability::register);
        modEventBus.addListener(this::gatherData);


        Gems.initGems();
        Alchemy.initAlchemy(358132134);
    }

    public void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        PackOutput out = generator.getPackOutput();
        if(event.includeClient()) {
            generator.addProvider(true, new ModItemModelProvider(generator, helper));
            generator.addProvider(true, new ModBlockStateProvider(generator, helper));
        }
        if(event.includeServer()) {
            ModBlockTagProvider blocktags = new ModBlockTagProvider(generator, provider, helper);
            generator.addProvider(true, blocktags);
            generator.addProvider(true, new ModItemTagProvider(generator, provider, blocktags.contentsGetter(), helper));

            generator.addProvider(true, new ModRecipeProvider(out, provider));
            generator.addProvider(true, new ModLootTableProvider(out));
            generator.addProvider(true, new ModFeatureProvider(out, provider));
        }
    }

}
