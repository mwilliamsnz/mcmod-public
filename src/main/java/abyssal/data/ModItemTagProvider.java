package abyssal.data;

import abyssal.Main;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import abyssal.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> tagLookupCompletableFuture) {
        super(output, provider, Main.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Gems.forAllGems((gem) -> {
            this.tag(ModTags.Items.GEMS).add(gem);
            this.tag(ModTags.Items.COIN_PURSE_ITEMS).add(gem);
        });

        this.tag(ItemTags.DIAMOND_TOOL_MATERIALS).replace().add(ModItems.DIAMOND_INGOT.get());
        this.tag(ItemTags.REPAIRS_DIAMOND_ARMOR).replace().add(ModItems.DIAMOND_INGOT.get());

        this.tag(Tags.Items.INGOTS).add(ModItems.SILVER_INGOT.get());
        this.tag(ModTags.Items.INGOTS_SILVER).add(ModItems.SILVER_INGOT.get());
        this.tag(Tags.Items.NUGGETS).add(ModItems.SILVER_NUGGET.get());
        this.tag(Tags.Items.INGOTS).add(ModItems.DIAMOND_INGOT.get());
        this.tag(Tags.Items.INGOTS).add(ModItems.NETHER_BRASS_INGOT.get());
        this.tag(Tags.Items.NUGGETS).add(ModItems.NETHER_BRASS_NUGGET.get());
        this.tag(Tags.Items.INGOTS).add(ModItems.ALCHEMICAL_GOLD_INGOT.get());
        this.tag(ModTags.Items.INGOTS_GOLDLIKE).add(ModItems.ALCHEMICAL_GOLD_INGOT.get());
        this.tag(ModTags.Items.INGOTS_GOLDLIKE).addTag(Tags.Items.INGOTS_GOLD);
        this.tag(Tags.Items.NUGGETS).add(ModItems.ALCHEMICAL_GOLD_NUGGET.get());
        this.tag(ModTags.Items.NUGGETS_GOLDLIKE).add(ModItems.ALCHEMICAL_GOLD_NUGGET.get());
        this.tag(ModTags.Items.NUGGETS_GOLDLIKE).addTag(Tags.Items.NUGGETS_GOLD);

        this.tag(ModTags.Items.NETHER_BRASS_TOOL_MATERIALS).add(ModItems.NETHER_BRASS_INGOT.get());
        this.tag(ModTags.Items.REPAIRS_MR_ARMOR).add(Items.PHANTOM_MEMBRANE);

        this.tag(Tags.Items.GUNPOWDERS).add(ModItems.CREEPER_JELLY.get());

        this.tag(ItemTags.PLANKS).add(ModBlocks.ELDER_PINE_PLANKS.get().asItem());
        this.tag(ItemTags.LOGS).add(ModBlocks.ELDER_PINE_LOG.get().asItem());
        this.tag(ItemTags.LOGS_THAT_BURN).add(ModBlocks.ELDER_PINE_LOG.get().asItem());
        this.tag(ItemTags.WOODEN_DOORS).add(ModBlocks.ELDER_PINE_DOOR.get().asItem());

        this.tag(ItemTags.SWORDS).add(ModItems.TIAMAT.get());
        this.tag(ItemTags.SWORDS).add(ModItems.TITANIC_HYDRA.get());
        this.tag(ItemTags.SWORDS).add(ModItems.NASHORS_TOOTH.get());

        this.tag(ItemTags.AXES).add(ModItems.NETHER_BRASS_AXE.get());
        this.tag(ItemTags.AXES).add(ModItems.CHARRING_AXE.get());

        this.tag(ItemTags.HEAD_ARMOR).add(ModItems.SILVER_HELMET.get());
        this.tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(ModItems.SILVER_HELMET.get());
        this.tag(ItemTags.CHEST_ARMOR).add(ModItems.SILVER_CHESTPLATE.get());
        this.tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(ModItems.SILVER_CHESTPLATE.get());
        this.tag(ItemTags.LEG_ARMOR).add(ModItems.SILVER_LEGGINGS.get());
        this.tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(ModItems.SILVER_LEGGINGS.get());
        this.tag(ItemTags.FOOT_ARMOR).add(ModItems.SILVER_BOOTS.get());
        this.tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(ModItems.SILVER_BOOTS.get());

        this.tag(ItemTags.CHEST_ARMOR).add(ModItems.WARMOGS.get());
        this.tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(ModItems.WARMOGS.get());
        this.tag(ItemTags.CHEST_ARMOR).add(ModItems.SPIRIT_VISAGE.get());
        this.tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(ModItems.SPIRIT_VISAGE.get());
        this.tag(ModTags.Items.HEAL_AMPLIFIER).add(ModItems.SPIRIT_VISAGE.get());
        this.tag(ItemTags.FOOT_ARMOR).add(ModItems.MOBI_BOOTS.get());
        this.tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(ModItems.MOBI_BOOTS.get());
        this.tag(ItemTags.FOOT_ARMOR).add(ModItems.MERC_TREADS.get());
        this.tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(ModItems.MERC_TREADS.get());
        this.tag(ModTags.Items.TENACITY_ITEMS).add(ModItems.MERC_TREADS.get());
        this.tag(ItemTags.HEAD_ARMOR).add(ModItems.RABADONS.get());
        this.tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(ModItems.RABADONS.get());

        this.tag(ModTags.Items.COIN_PURSE_ITEMS).add(ModItems.COIN_PURSE.get());
        this.tag(ModTags.Items.COIN_PURSE_ITEMS).add(ModItems.COPPER_COIN.get());
        this.tag(ModTags.Items.COIN_PURSE_ITEMS).add(ModItems.SILVER_COIN.get());
        this.tag(ModTags.Items.COIN_PURSE_ITEMS).add(ModItems.GOLD_COIN.get());
//        this.tag(ModTags.Items.COIN_PURSE_ITEMS).add(Items.GOLD_NUGGET);
        this.tag(ModTags.Items.COIN_PURSE_ITEMS).add(Items.EMERALD);

        this.tag(ModTags.Items.CHARMS).add(ModItems.RUBY_CRYSTAL.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.SAPPHIRE_CRYSTAL.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.RESONATING_STAR.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.REVERBERATING_STAR.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.SMOLDERING_TABLET.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.REJUVENATION_BEAD.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.FOUR_LEAF_CLOVER.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.LUCK_CHARM.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.TOXIC_TOTEM.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.RAGE_TOTEM.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.CLEANSING_TOTEM.get());
        this.tag(ModTags.Items.CHARMS).add(ModItems.AETHER_WISP.get());
        this.tag(ModTags.Items.BELTS).add(ModItems.BELT.get());
        this.tag(ModTags.Items.BELTS).add(ModItems.SKULL_BELT.get());
        this.tag(ModTags.Items.BELTS).add(ModItems.REJUVENATION_BELT.get());
        this.tag(ModTags.Items.BELTS).add(ModItems.COIN_PURSE.get());
        this.tag(ModTags.Items.BELTS).add(ModItems.GIANTS_BELT.get());
        this.tag(ModTags.Items.BELTS).add(ModItems.ANCHOR_BELT.get());
        this.tag(ModTags.Items.NECKLACES).add(ModItems.REJUVENATION_NECKLACE.get());
        this.tag(ModTags.Items.NECKLACES).add(ModItems.NULL_MAGIC_MANTLE.get());
        this.tag(ModTags.Items.NECKLACES).add(ModItems.FISH_NECKLACE.get());
        this.tag(ModTags.Items.NECKLACES).add(ModItems.CLOCKWORK_AMULET.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.GLOW_RING.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.SILVER_RING.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.GOLD_RING.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.DAMAGE_RING.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.CHAMPIONS_RING.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.DORANS_RING.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.REJUVENATION_RING.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.LIGHT_TRAVEL_RING.get());
        this.tag(ModTags.Items.RINGS).add(ModItems.GLASS_CANNON_RING.get());

        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ModItems.AMP_TOME.get());
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ModItems.LOST_CHAPTER.get());
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ModItems.FIENDISH_CODEX.get());

        this.tag(ModTags.Items.SHELF_AMPLIFIERS).add(ModItems.AMP_TOME.get());
        this.tag(ModTags.Items.SHELF_AMPLIFIERS).add(ModItems.LOST_CHAPTER.get());
        this.tag(ModTags.Items.SHELF_AMPLIFIERS).add(ModItems.FIENDISH_CODEX.get());
        this.tag(ModTags.Items.SHELF_AMPLIFIERS).add(Items.ENCHANTED_BOOK);

        this.tag(ItemTags.BIRCH_LOGS).add(ModBlocks.MOSSY_BIRCH.get().asItem());
        this.tag(ItemTags.OAK_LOGS).add(ModBlocks.MOSSY_OAK.get().asItem());

//        this.tag(ItemTags.DECORATED_POT_SHERDS).add(ModItems.FISH_SHERD.get(), ModItems.GEM_SHERD.get());

    }
}