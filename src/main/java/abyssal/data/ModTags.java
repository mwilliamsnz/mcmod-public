package abyssal.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import top.theillusivec4.curios.api.CuriosResources;

import static abyssal.Main.rl;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> SILVER_ORES = tag("silver_ores");
        public static final TagKey<Block> GEM_SLATES = tag("gem_slates");
        public static final TagKey<Block> CHARRING_AXE_DESTROYS = tag("charring_axe_destroys");
        public static final TagKey<Block> GRASS_SPREADERS = tag("grass_spreaders");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(rl(name));
        }
    }

    public static class Items {
        public static final TagKey<Item> GEMS = tag("gems");
        public static final TagKey<Item> INGOTS_SILVER = tag("silver_ingots");
        public static final TagKey<Item> INGOTS_GOLDLIKE = tag("goldlike_ingots");
        public static final TagKey<Item> NUGGETS_GOLDLIKE = tag("goldlike");

        public static final TagKey<Item> NETHER_BRASS_TOOL_MATERIALS = tag("nether_brass_tool_materials");
        public static final TagKey<Item> REPAIRS_MR_ARMOR = tag("repairs_mr_armour");

        public static final TagKey<Item> COIN_PURSE_ITEMS = tag("coin_purse_items");
        public static final TagKey<Item> GOBBLER_CONSUMABLE = tag("gobbler_consumable");
        public static final TagKey<Item> SHELF_AMPLIFIERS = tag("shelf_amplifiers");

        public static final TagKey<Item> TENACITY_ITEMS = tag("tenacity_item");
        public static final TagKey<Item> HEAL_AMPLIFIER = tag("heal_amplifier");

        public static final TagKey<Item> RINGS = curioTag("ring");
        public static final TagKey<Item> CHARMS = curioTag("charm");
        public static final TagKey<Item> NECKLACES = curioTag("necklace");
        public static final TagKey<Item> BELTS = curioTag("belt");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(rl(name));
        }
        private static TagKey<Item> curioTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CuriosResources.MOD_ID, name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> HAS_MANY_OAKS = tag("has_many_oaks");
        public static final TagKey<Biome> HAS_MANY_BIRCHES = tag("has_many_birches");
        public static final TagKey<Biome> SPAWNS_TOWERS = tag("spawns_towers");

        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, rl(name));
        }
    }

    public static class Paintings {
        public static final TagKey<PaintingVariant> FISH = tag("fish");

        private static TagKey<PaintingVariant> tag(String name) {
            return TagKey.create(Registries.PAINTING_VARIANT, rl(name));
        }
    }
}
