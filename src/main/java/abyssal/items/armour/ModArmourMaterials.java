package abyssal.items.armour;

import abyssal.init.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmourMaterials implements ArmorMaterial {

    SILVER("silver", 10, new int[]{2, 3, 5, 2}, 20, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> {
        return Ingredient.of(ModItems.SILVER_INGOT.get());
    }),
    // Changes: multiplier was 5, protection 1, 2, 3, 1 (total 7 -> 10)
    MOD_LEATHER("leather", 10, new int[]{2, 2, 4, 2}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.LEATHER);
    }),
    // Changes: multiplier was 15, protection 2, 5, 6, 2
    MOD_IRON("iron", 20, new int[]{2, 4, 6, 3}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    }),
    // Changes: multiplier was 33, protection 3, 6, 8, 3
    MOD_DIAMOND("diamond", 33, new int[]{3, 5, 8, 4}, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> {
        return Ingredient.of(ModItems.DIAMOND_INGOT.get());
    }),
    CLOTH("cloth", 5, new int[]{1, 2, 3, 1}, 20, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.WHITE_WOOL);
    }),
    WARMOGS("warmogs", 20, new int[]{0,0,0,0}, 8, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
        return Ingredient.of(ItemTags.LOGS);
    }),
    MR_ITEMS("mr_items", 20, new int[]{0,0,0,0}, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
        return Ingredient.of(Items.PHANTOM_MEMBRANE);
    });

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    public final int durabilityMultiplier;
    private final int[] slotProtections;
    public final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModArmourMaterials(String name, int durabilityMult, int[] protections, int enchantability, SoundEvent sount, float toughness, float knockbackResistance, Supplier<Ingredient> repairItem) {
        this.name = name;
        this.durabilityMultiplier = durabilityMult;
        this.slotProtections = protections;
        this.enchantmentValue = enchantability;
        this.sound = sount;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairItem);
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_PER_SLOT[type.getSlot().getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.slotProtections[type.getSlot().getIndex()];
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}