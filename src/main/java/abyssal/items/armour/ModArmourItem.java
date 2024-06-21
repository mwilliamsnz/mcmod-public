package abyssal.items.armour;

import abyssal.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import javax.annotation.Nullable;

public class ModArmourItem extends ArmorItem implements IItemExtension {

    public ModArmourItem(ArmorMaterial material, Type slot, Properties properties) {
        super(material, slot, properties);
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
