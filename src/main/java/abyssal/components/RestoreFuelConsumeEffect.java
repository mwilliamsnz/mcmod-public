package abyssal.components;

import abyssal.init.ModDataComponents;
import abyssal.spells.SpellFuelQuantity;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public record RestoreFuelConsumeEffect() implements ConsumeEffect {
    public static final RestoreFuelConsumeEffect INSTANCE = new RestoreFuelConsumeEffect();
    public static final MapCodec<RestoreFuelConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, RestoreFuelConsumeEffect> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public ConsumeEffect.Type<RestoreFuelConsumeEffect> getType() {
        return ModDataComponents.CONSUME_REFUEL.get();
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        if(user instanceof Player player) {
            SpellFuelQuantity q = stack.getOrDefault(ModDataComponents.SPELL_FUEL_RECHARGE, new SpellRefuelComponent(SpellFuelQuantity.NONE)).fuel();
            return q.topUp(player);
        }
        return false;
    }
}