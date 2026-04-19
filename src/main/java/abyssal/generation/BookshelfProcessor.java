package abyssal.generation;

import abyssal.Main;
import abyssal.init.ModDataComponents;
import abyssal.init.ModItems;
import abyssal.items.spells.DualSpellBook;
import abyssal.spells.Spell;
import abyssal.spells.SpellComponent;
import abyssal.spells.Spells;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class BookshelfProcessor extends StructureProcessor {
    public static final MapCodec<BookshelfProcessor> CODEC = Codec.FLOAT.fieldOf("loot_ratio")
            .xmap(BookshelfProcessor::new, p_74023_ -> p_74023_.lootRatio);

    private static final float EMPTY_CHANCE = 0.25f;
    private static final float WRITTEN_BOOK_CHANCE = 0.1f;

    private final float lootRatio;

    private static WeightedList SPELL_OPTIONS;

    public BookshelfProcessor(float lootRatio) {
        this.lootRatio = lootRatio;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, BlockPos pos1, BlockPos pos2, StructureTemplate.StructureBlockInfo blockInfo1, StructureTemplate.StructureBlockInfo blockInfoIn, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource rand = settings.getRandom(blockInfoIn.pos());
        BlockState inState = blockInfoIn.state();
        BlockPos pos = blockInfoIn.pos();
        if (inState.is(Blocks.CHISELED_BOOKSHELF)) {
            return populatedBookshelf(levelReader, rand, this.lootRatio, pos, inState);
        }
        return blockInfoIn;
    }

    public static StructureTemplate.StructureBlockInfo populatedBookshelf(LevelReader levelReader, RandomSource rand, float lootChance, BlockPos pos, BlockState state) {
        CompoundTag t = new CompoundTag();
        ListTag l = new ListTag();
        for(int i = 0; i < 6; i++) {
            if(rand.nextFloat() >= EMPTY_CHANCE) {
                l.add(generateItemTag(levelReader, i, rand, lootChance));
                state = state.setValue(ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(i), true);
            }
        }
        t.put("Items", l);
        return new StructureTemplate.StructureBlockInfo(pos, state, t);
    }

    private static Tag generateItemTag(LevelReader levelReader, int slot, RandomSource rand, float lootChance) {
        CompoundTag itemTag = new CompoundTag();
        itemTag.putByte("Slot", (byte) slot);
        itemTag.putByte("Count", (byte) 1);
        String id = "minecraft:book";
        if(rand.nextFloat() < lootChance) {
            float r = rand.nextFloat();
            if(r < 0.5) {
                id = "minecraft:enchanted_book";
                Tag tag;
                if(rand.nextFloat() < 0.2) {
                    tag = enchantedBookNBT(levelReader, rand, 30, true);
                } else {
                    tag = enchantedBookNBT(levelReader, rand, 10, false);
                }
                if(tag instanceof CompoundTag compoundTag) {
                    compoundTag.putByte("Slot", (byte) slot);
                }
                return tag;
            } else if(r < 0.7) {
                id = BuiltInRegistries.ITEM.getKey(ModItems.LOST_CHAPTER.get()).toString();
//                itemTag.put("tag", spellbookNBT(rand));
                Tag tag = spellbookNBT(levelReader, rand);
                if(tag instanceof CompoundTag compoundTag) {
                    compoundTag.putByte("Slot", (byte) slot);
                }
                return tag;
            } else {
                id = BuiltInRegistries.ITEM.getKey(ModItems.AMP_TOME.get()).toString();
            }
        } else if(rand.nextFloat() < WRITTEN_BOOK_CHANCE) {
            id = "minecraft:writable_book";
        }
        itemTag.putString("id", id);

        return itemTag;
    }

    private static Tag enchantedBookNBT(LevelReader levelReader, RandomSource rand, int level, boolean treasure) {
        Registry<Enchantment> reg = levelReader.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Stream<Holder<Enchantment>> options = reg.getOrThrow(EnchantmentTags.IN_ENCHANTING_TABLE).stream();
        if(treasure) {
            Stream<Holder<Enchantment>> treasureOptions = reg.getOrThrow(EnchantmentTags.TREASURE).stream();
            options = Stream.concat(options, treasureOptions);
        }
        List<EnchantmentInstance> enchants = EnchantmentHelper.selectEnchantment(rand, Items.BOOK.getDefaultInstance(), level,
                options);
        ItemStack stack = Items.ENCHANTED_BOOK.getDefaultInstance();
        for(EnchantmentInstance instance : enchants) {
            stack.enchant(instance.enchantment(), instance.level());
        }
        return stack.save(levelReader.registryAccess());
    }

    private static Tag spellbookNBT(LevelReader levelReader, RandomSource rand) {
        ItemStack stack = ModItems.LOST_CHAPTER.toStack();
        getSpellOptions().getRandom(rand).ifPresent((spell) -> {
            SpellComponent component = new SpellComponent(spell);
            stack.set(ModDataComponents.SPELLBOOK, component);
        });
        return stack.save(levelReader.registryAccess());
    }


    private static WeightedList<Spell> getSpellOptions() {
        if(SPELL_OPTIONS == null) {
            SPELL_OPTIONS = new WeightedList.Builder<Spell>()
                    .add(Spells.ENCHANT, 25)
                    .add(Spells.FEATHER_FALL, 25)

                    .add(Spells.AREA_GLOW, 15)
                    .add(Spells.BANISH, 15)
                    .add(Spells.EXTINGUISH, 15)
                    .add(Spells.LUCK, 15)

                    .add(Spells.INVISIBILITY, 10)
                    .add(Spells.LEAP, 10)
                    .add(Spells.SUMMON_SKELETON, 10)
                    .build();
        }
        return SPELL_OPTIONS;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLACKSTONE_REPLACE; // ModGeneration.BOOKSHELF_FILLER.get(); TODO
    }
}