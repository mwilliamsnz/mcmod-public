package abyssal.init;

import abyssal.Main;
import abyssal.data.ModTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Gems {
    public enum GemType {
        NONE(""),
        GARNET("garnet"),
        DIAMOND("diamond"),
        ONYX("onyx");
//        PURPLEGEM("purplegem");

        public final String name;
        GemType(String name) {
            this.name = name;
        }
    }

    public enum GemSize {
        POWDER("powder"),
        TINY("tiny"),
        SMALL("small"),
        REGULAR(""),
        LARGE("large");

        public final String name;
        GemSize(String name) {
            this.name = name;
        }
    }

    public enum GemBlockType {
        SLATE(""),
        SILVERED("silvered"),
        GILDED("gilded");

        public final String name;
        GemBlockType(String name) {
            this.name = name;
        }
    }

    private static EnumMap<GemSize, EnumMap<GemType, RegistryObject<Item>>> gemMap;
    private static EnumMap<GemBlockType, EnumMap<GemType, RegistryObject<Block>>> gemBlockMap;

    public static Item gem(GemSize size, GemType type) {
        if(type == GemType.NONE) {
            throw new IllegalArgumentException();
        }
        if(size == GemSize.REGULAR && type == GemType.DIAMOND) {
            return Items.DIAMOND;
        }
        return gemMap.get(size).get(type).get();
    }

    public static String gemName(GemSize size, GemType type) {
        if(type == GemType.NONE) {
            throw new IllegalArgumentException();
        }
        if(size == GemSize.POWDER) {
            return type.name + "_powder";
        }
        if(size == GemSize.REGULAR) {
            return type.name;
        }
        return size.name + "_" + type.name;
    }

    public static Block gemBlock(GemBlockType blockType, GemType type) {
        if(blockType == GemBlockType.SLATE && type == GemType.NONE) {
            return Blocks.COBBLED_DEEPSLATE;
        }
        return gemBlockMap.get(blockType).get(type).get();
    }

    public static String gemBlockName(GemBlockType blockType, GemType type) {
        if(type == GemType.NONE) {
            if(blockType == GemBlockType.SLATE){
                throw new IllegalArgumentException();

            }
            return blockType.name + "_slate";
        }
        if(blockType == GemBlockType.SLATE) {
            return type.name + "_slate";
        }
        return blockType.name + "_" + type.name + "_slate";
    }


    public static boolean isVanillaGem(GemSize size, GemType type) {
        return size == GemSize.REGULAR && type == GemType.DIAMOND;
    }

    public static boolean isVanillaGemBlock(GemBlockType blockType, GemType type) {
        return blockType == GemBlockType.SLATE && type == GemType.NONE;
    }

    public static void initGems() {
        gemMap = new EnumMap<GemSize, EnumMap<GemType, RegistryObject<Item>>>(GemSize.class);
        for(GemSize size : GemSize.values()) {
            EnumMap<GemType, RegistryObject<Item>> map = new EnumMap<GemType, RegistryObject<Item>>(GemType.class);
            gemMap.put(size, map);
            for(GemType type : GemType.values()) {
                if(isVanillaGem(size, type) || type == GemType.NONE) continue;
                map.put(type, ModItems.ITEMS.register(gemName(size, type), () -> new Item(ModItems.defaultItemProperties())));
            }
        }

        gemBlockMap = new EnumMap<GemBlockType, EnumMap<GemType, RegistryObject<Block>>>(GemBlockType.class);
        for(GemBlockType blockType : GemBlockType.values()) {
            EnumMap<GemType, RegistryObject<Block>> map = new EnumMap<GemType, RegistryObject<Block>>(GemType.class);
            gemBlockMap.put(blockType, map);
            for(GemType type : GemType.values()) {
                if(isVanillaGemBlock(blockType, type)) continue;
                RegistryObject<Block> r = ModBlocks.BLOCKS.register(gemBlockName(blockType, type), () -> new Block(BlockBehaviour.Properties.copy(Blocks.COBBLED_DEEPSLATE)));
                map.put(type, r);
                ModBlocks.DATAGEN_LOOT_TABLE.add(r);
            }
        }
    }

    public static void forAllGemBlocks(Consumer<Block> consumer) {
        for(GemBlockType blockType : GemBlockType.values()) {
            for(GemType type : GemType.values()) {
                if(isVanillaGemBlock(blockType, type)) continue;
                consumer.accept(gemBlock(blockType, type));
            }
        }
    }

    public static void forAllGemBlocksByTypes(BiConsumer<GemBlockType, GemType> consumer) {
        for(GemBlockType blockType : GemBlockType.values()) {
            for(GemType type : GemType.values()) {
                if(isVanillaGemBlock(blockType, type)) continue;
                consumer.accept(blockType, type);
            }
        }
    }


    public static void forAllGems(Consumer<Item> consumer) {
        for(GemSize size : GemSize.values()) {
            for(GemType type : GemType.values()) {
                if(type == GemType.NONE) continue;
                consumer.accept(gem(size, type));
            }
        }
    }

    public static void forAllGemNames(Consumer<String> consumer) {
        for(GemSize size : GemSize.values()) {
            for(GemType type : GemType.values()) {
                consumer.accept(gemName(size, type));
            }
        }
    }

    public static boolean isGemItem(ItemStack stack) {
        return stack.is(ModTags.Items.GEMS);
    }

    public static GemType getType(ItemStack stack) {
        Item i = stack.getItem();
        for(GemSize size : GemSize.values()) {
            Map<GemType, RegistryObject<Item>> map = gemMap.get(size);
            for (GemType type : GemType.values()) {
                if(type == GemType.NONE) {
                    continue;
                }
                if(type == GemType.DIAMOND && size == GemSize.REGULAR) {
                    if(Items.DIAMOND.equals(i)) {
                        return type;
                    }
                } else {
                    if(map.get(type).get().equals(i)) {
                        return type;
                    }
                }
            }
        }
        Main.LOGGER.error("Looking up non-gem with Gems.getType");
        return GemType.NONE;
    }

    public static GemSize getSize(ItemStack stack) {
        Item i = stack.getItem();
        for(GemSize size : GemSize.values()) {
            Map<GemType, RegistryObject<Item>> map = gemMap.get(size);
            for (GemType type : GemType.values()) {
                if(type == GemType.NONE) {
                    continue;
                }
                if(type == GemType.DIAMOND && size == GemSize.REGULAR) {
                    if(Items.DIAMOND.equals(i)) {
                        return size;
                    }
                } else {
                    if(map.get(type).get().equals(i)) {
                        return size;
                    }
                }
            }
        }
        Main.LOGGER.error("Looking up non-gem with Gems.getType");
        return GemSize.POWDER;
    }
}
