package abyssal.generation;

import abyssal.init.ModGeneration;
import abyssal.init.ModItems;
import abyssal.items.spells.DualSpellBook;
import abyssal.spells.Spell;
import abyssal.spells.Spells;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.List;

public class BookshelfProcessor extends StructureProcessor {
    public static final Codec<BookshelfProcessor> CODEC = Codec.FLOAT.fieldOf("loot_ratio").xmap(BookshelfProcessor::new, (p_74023_) -> {
        return p_74023_.lootRatio;
    }).codec();

    private static final float EMPTY_CHANCE = 0.25f;
    private static final float WRITTEN_BOOK_CHANCE = 0.1f;

    private final float lootRatio;

    private static WeightedRandomList SPELL_OPTIONS;

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
            CompoundTag newNBT = populatedTag(rand, this.lootRatio);
            return new StructureTemplate.StructureBlockInfo(pos, inState, newNBT);
        }
        return blockInfoIn;
    }

    public static CompoundTag populatedTag(RandomSource rand, float lootChance) {
        CompoundTag t = new CompoundTag();
        ListTag l = new ListTag();
        for(int i = 0; i < 6; i++) {
            if(rand.nextFloat() >= EMPTY_CHANCE) {
                l.add(generateItemTag(i, rand, lootChance));
            }
        }
        t.put("Items", l);
        return t;
    }

    private static CompoundTag generateItemTag(int slot, RandomSource rand, float lootChance) {
        CompoundTag itemTag = new CompoundTag();
        itemTag.putByte("Slot", (byte) slot);
        itemTag.putByte("Count", (byte) 1);
        String id = "minecraft:book";
        if(rand.nextFloat() < lootChance) {
            float r = rand.nextFloat();
            if(r < 0.5) {
                id = "minecraft:enchanted_book";
                if(rand.nextFloat() < 0.2) {
                    itemTag.put("tag", enchantedBookNBT(rand, 30, true));
                } else {
                    itemTag.put("tag", enchantedBookNBT(rand, 10, false));
                }
            } else if(r < 0.7) {
                id = ModItems.LOST_CHAPTER.getId().toString();
                itemTag.put("tag", spellbookNBT(rand));
            } else {
                id = ModItems.AMP_TOME.getId().toString();
            }
        } else if(rand.nextFloat() < WRITTEN_BOOK_CHANCE) {
            id = "minecraft:writable_book";
        }
        itemTag.putString("id", id);

        return itemTag;
    }

    private static CompoundTag enchantedBookNBT(RandomSource rand, int level, boolean treasure) {
        CompoundTag t = new CompoundTag();
        ListTag l = new ListTag();
        List<EnchantmentInstance> enchants = EnchantmentHelper.selectEnchantment(rand, Items.BOOK.getDefaultInstance(), level, treasure);
        for(EnchantmentInstance instance : enchants) {
            l.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(instance.enchantment), instance.level));
        }
        t.put("StoredEnchantments", l);
        return t;
    }

    private static CompoundTag spellbookNBT(RandomSource rand) {
        CompoundTag spellbookTag = new CompoundTag();
        getSpellOptions().getRandom(rand).ifPresent((spell) -> {
            CompoundTag spellTag = Spells.toTag(spell.getData());
            spellbookTag.put(DualSpellBook.TAG_PRIMARY_SPELL, spellTag);
        });
        return spellbookTag;
    }


    private static WeightedRandomList<WeightedEntry.Wrapper<Spell>> getSpellOptions() {
        if(SPELL_OPTIONS == null) {
            SPELL_OPTIONS = new SimpleWeightedRandomList.Builder<Spell>()
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
        return ModGeneration.BOOKSHELF_FILLER.get();
    }
}