package abyssal;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =  DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Main.MOD_ID);


    public static final Supplier<Attribute> MAGIC_RESIST = ATTRIBUTES.register("generic.magic_resist", () -> (new RangedAttribute("attribute.name.generic.magic_resist", 0.0D, -100.0D, 1024.0D)).setSyncable(true));
    public static final Supplier<Attribute> ABILITY_POWER = ATTRIBUTES.register("generic.ability_power", () -> (new RangedAttribute("attribute.name.generic.ability_power", 0.0D, 0.0D, 4096.0D)).setSyncable(true));



}