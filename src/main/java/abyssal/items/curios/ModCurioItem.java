package abyssal.items.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class ModCurioItem extends Item {
    public ModCurioItem(Properties props) {
        super(props);
    }



    public Multimap<Attribute, AttributeModifier> getCurioAttributes(SlotContext slotContext, UUID uuid) {
        return HashMultimap.create();
    }

    public void tickCurio(SlotContext ctx) {}

    public void onEquipCurio(SlotContext ctx){}

    public void onUnequipCurio(SlotContext ctx){}

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> lazyCurio = LazyOptional.of(()-> new ICurio() {

                @Override
                public ItemStack getStack() {
                    return stack;
                }

                @Override
                public void curioTick(SlotContext ctx) {
                    tickCurio(ctx);
                }

                @Override
                public void onEquip(SlotContext ctx, ItemStack newStack) {
                    ModCurioItem.this.onEquipCurio(ctx);
                }

                @Nonnull
                @Override
                public DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit) {
                    return DropRule.DEFAULT;
                }

                @Override
                public void onUnequip(SlotContext ctx, ItemStack prevStack) {
                    ModCurioItem.this.onUnequipCurio(ctx);
                }

                @Override
                public boolean canEquipFromUse(SlotContext slotContext) {
                    return true;
                }

                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
                    return ModCurioItem.this.getCurioAttributes(slotContext, uuid);
                }

            });

            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
                return CuriosCapability.ITEM.orEmpty(capability, lazyCurio);
            }
        };
    }
}
