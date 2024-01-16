package abyssal.data;

import abyssal.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import top.theillusivec4.curios.api.CuriosApi;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> SILVER_ORES = tag("silver_ores");
        public static final TagKey<Block> GEM_SLATES = tag("gem_slates");
        public static final TagKey<Block> CHARRING_AXE_DESTROYS = tag("charring_axe_destroys");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Main.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> GEMS = tag("gems");
        public static final TagKey<Item> INGOTS_SILVER = tag("silver_ingots");
        public static final TagKey<Item> INGOTS_GOLDLIKE = tag("goldlike_ingots");
        public static final TagKey<Item> NUGGETS_GOLDLIKE = tag("goldlike");

        public static final TagKey<Item> COIN_PURSE_ITEMS = tag("coin_purse_items");
        public static final TagKey<Item> GOBBLER_CONSUMABLE = tag("gobbler_consumable");

        public static final TagKey<Item> TENACITY_ITEMS = tag("tenacity_item");
        public static final TagKey<Item> HEAL_AMPLIFIER = tag("heal_amplifier");

        public static final TagKey<Item> RINGS = curioTag("ring");
        public static final TagKey<Item> CHARMS = curioTag("charm");
        public static final TagKey<Item> NECKLACES = curioTag("necklace");
        public static final TagKey<Item> BELTS = curioTag("belt");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Main.MOD_ID, name));
        }
        private static TagKey<Item> curioTag(String name) {
            return ItemTags.create(new ResourceLocation(CuriosApi.MODID, name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> HAS_MANY_OAKS = tag("has_many_oaks");
        public static final TagKey<Biome> HAS_MANY_BIRCHES = tag("has_many_birches");

        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(Main.MOD_ID, name));
        }
    }
}
