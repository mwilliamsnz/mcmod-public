package abyssal.items.armour;

import abyssal.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import javax.annotation.Nullable;

public class ModDyeableArmourItem extends DyeableArmorItem implements IItemExtension, DyeableLeatherItem {

    public ModDyeableArmourItem(ArmorMaterial material, Type slot, Properties properties) {
        super(material, slot, properties);
    }

    public int getColor(ItemStack stack) {
        CompoundTag tag = stack.getTagElement("display");
        return tag != null && tag.contains("color", 99) ? tag.getInt("color") : 13421990;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {

        if(slot == EquipmentSlot.LEGS) {
            return  new ResourceLocation(Main.MOD_ID,"textures/models/armor/" + material.getName() + "_layer_2.png").toString();
        }

        else return  new ResourceLocation(Main.MOD_ID,"textures/models/armor/"  + material.getName() + "_layer_1.png").toString();
    }
}
