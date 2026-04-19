package abyssal.init;

import abyssal.Main;
import abyssal.data.ModTags;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.*;

import java.util.Map;

public class ModArmourMaterials {

    public static final int SILVER_DURABILITY_MULTIPLIER = 10;
    public static final int CLOTH_DURABILITY_MULTIPLIER = 5;

    // Changes: multiplier was 5 now 10, protection was 1, 2, 3, 1 (total 7 -> 10)
    public static final ArmorMaterial MOD_LEATHER = new ArmorMaterial(
            10, makeDefence(2, 2, 4, 2, 3),
            15, SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f,
            ItemTags.REPAIRS_LEATHER_ARMOR,
            // The relative location of the EquipmentModel JSON at assets/<namespace>/models/equipment/<path>.json
            createId("mod_leather")
    );

    // Changes: durability multiplier to 20 from 15, protection was 2, 5, 6, 2, 3
    public static final ArmorMaterial MOD_IRON = new ArmorMaterial(
            20, makeDefence(2, 4, 6, 3, 5),
            9, SoundEvents.ARMOR_EQUIP_IRON, 0f, 0f,
            ItemTags.REPAIRS_IRON_ARMOR,
            createId("mod_iron")
    );

    // Changes: multiplier still 33, protection was 3, 6, 8, 3, 11
    public static final ArmorMaterial MOD_DIAMOND = new ArmorMaterial(
            33, makeDefence(3, 5, 8, 4, 11),
            10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2f, 0f,
            ItemTags.REPAIRS_DIAMOND_ARMOR,
            createId("mod_diamond")
    );

    public static final ArmorMaterial CLOTH = new ArmorMaterial(
            5, makeDefence(1, 2, 3, 1, 3),
            20, SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f,
            ItemTags.WOOL,
            createId("cloth")
    );
    public static final ArmorMaterial SILVER = new ArmorMaterial(
            10, makeDefence(2, 3, 5, 2, 4),
            20, SoundEvents.ARMOR_EQUIP_GOLD, 0f, 0f,
            ModTags.Items.INGOTS_SILVER,
            createId("silver")
    );
    public static final ArmorMaterial WARMOGS = new ArmorMaterial(
            30, makeDefence(0,0,0,0,0),
            8, SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f,
            ItemTags.LOGS,
            createId("warmogs")
    );
    public static final ArmorMaterial MR_ITEMS = new ArmorMaterial(
            20, makeDefence(0,0,0,0,0),
            1, SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f,
            ModTags.Items.REPAIRS_MR_ARMOR,
            createId("mr_items")
    );

    public static void replaceArmourMaterials() {
//        ArmorMaterials.LEATHER = MOD_LEATHER;
//        ArmorMaterials.IRON = MOD_IRON;
//        ArmorMaterials.DIAMOND = MOD_DIAMOND;
    }

    private static Map<ArmorType, Integer> makeDefence(int b, int l, int c, int h, int o) {
        return Maps.newEnumMap(
                Map.of(
                        ArmorType.BOOTS, b,
                        ArmorType.LEGGINGS, l,
                        ArmorType.CHESTPLATE, c,
                        ArmorType.HELMET, h,
                        ArmorType.BODY, o
                )
        );
    }

    private static ResourceKey<EquipmentAsset> createId(String id) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Main.rl(id));
    }
}