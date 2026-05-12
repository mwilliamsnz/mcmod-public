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

    public static final ArmorMaterial CLOTH = new ArmorMaterial(
            CLOTH_DURABILITY_MULTIPLIER, makeDefence(1, 2, 3, 1, 3),
            20, SoundEvents.ARMOR_EQUIP_LEATHER, 0f, 0f,
            ItemTags.WOOL,
            createId("cloth")
    );
    public static final ArmorMaterial SILVER = new ArmorMaterial(
            SILVER_DURABILITY_MULTIPLIER, makeDefence(2, 3, 5, 2, 4),
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
    public static final ArmorMaterial THORNMAIL = new ArmorMaterial(
            33, makeDefence(0,0,6,0,0),
            1, SoundEvents.ARMOR_EQUIP_CHAIN, 0f, 0f,
            ModTags.Items.REPAIRS_THORNMAIL,
            createId("thornmail")
    );

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