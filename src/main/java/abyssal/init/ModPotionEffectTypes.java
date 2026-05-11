package abyssal.init;

import abyssal.Main;
import abyssal.ModAttributes;
import abyssal.mobeffects.AttributeOnlyEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPotionEffectTypes {

    public static final DeferredRegister<MobEffect> POTION_EFFECT_TYPES = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Main.MOD_ID);
    public static final Supplier<MobEffect> WOUNDED = POTION_EFFECT_TYPES.register(
            "wounded", () -> new AttributeOnlyEffect(MobEffectCategory.HARMFUL, 4738376)
                    .addAttributeModifier(
                            ModAttributes.HEAL_RATE, Identifier.fromNamespaceAndPath(Main.MOD_ID, "effect.wounded"), -0.20F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    ));
}
