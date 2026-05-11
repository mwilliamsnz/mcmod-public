package abyssal;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =  DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Main.MOD_ID);


    public static final DeferredHolder<Attribute, Attribute> MAGIC_RESIST = ATTRIBUTES.register("abyssal.magic_resist", () -> (new RangedAttribute("attribute.name.abyssal.magic_resist", 0.0D, -100.0D, 1024.0D)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ABILITY_POWER = ATTRIBUTES.register("abyssal.ability_power", () -> (new RangedAttribute("attribute.name.abyssal.ability_power", 0.0D, 0.0D, 4096.0D)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> HEAL_RATE = ATTRIBUTES.register("abyssal.healing_rate", () -> (new RangedAttribute("attribute.name.abyssal.healing_rate", 1.0D, 0.0D, 4096.0D)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> TENACITY = ATTRIBUTES.register("abyssal.debuff_duration", () -> (new RangedAttribute("attribute.name.abyssal.debuff_duration", 1.0D, 0.0D, 4096.0D)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> REGEN = ATTRIBUTES.register("abyssal.regen", () -> (new RangedAttribute("attribute.name.abyssal.regen", 0.0D, 0.0D, 4096.0D)).setSyncable(true));

}