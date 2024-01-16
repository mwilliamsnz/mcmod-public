package abyssal.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends LootTableProvider {

//    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
//    private final DataGenerator generator;

    public ModLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)
                //new LootTableProvider.SubProviderEntry(??::new, LootContextParamSets.CHEST),
                //new LootTableProvider.SubProviderEntry(??::new, LootContextParamSets.ENTITY)
        ));
//        this.generator = generator;
    }

//    @Override
//    public void run(CachedOutput cache) {
//        Path path = this.generator.getOutputFolder();
//        Map<ResourceLocation, LootTable> map = Maps.newHashMap();
//        new ModBlockLoot().accept((resourceLocation, builder) -> {
//            if (map.put(resourceLocation, builder.setParamSet(LootContextParamSets.BLOCK).build()) != null) {
//                throw new IllegalStateException("Duplicate loot table " + resourceLocation);
//            }
//        });
//
////        ValidationContext validationcontext = new ValidationContext(LootContextParamSets.ALL_PARAMS, (p_124465_) -> {
////            return null;
////        }, map::get);
////
////        validate(map, validationcontext);
////
////        Multimap<String, String> multimap = validationcontext.getProblems();
////        if (!multimap.isEmpty()) {
////            multimap.forEach((p_124446_, p_124447_) -> {
////                Main.LOGGER.warn("Found validation problem in {}: {}", p_124446_, p_124447_);
////            });
////            throw new IllegalStateException("Failed to validate loot tables, see logs");
////        } else {
//            map.forEach((p_124451_, p_124452_) -> {
//                Path path1 = createPath(path, p_124451_);
//
//                try {
//                    DataProvider.saveStable(cache, LootTables.serialize(p_124452_), path1);
//                } catch (IOException ioexception) {
//                    Main.LOGGER.error("Couldn't save loot table {}", path1, ioexception);
//                }
//
//            });
////        }
//    }
//
//    private static Path createPath(Path p_124454_, ResourceLocation p_124455_) {
//        return p_124454_.resolve("data/" + p_124455_.getNamespace() + "/loot_tables/" + p_124455_.getPath() + ".json");
//    }

}
