package abyssal.spells;

import abyssal.Main;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Spells {

    private static final Map<Identifier, Spell> SPELLS = new HashMap<>();

    public static final Spell NO_OP = createSpell(new Spell(key("no_op")) {
        public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {return InteractionResult.FAIL;}
    });
    public static final Spell INVISIBILITY = createSpell(new PotionEffectSpell(key("invisibility"), new SpellFuelQuantity(SpellFuelTypes.FUEL_LIGHT, 5), MobEffects.INVISIBILITY, 100, 1));
    public static final Spell LUCK = createSpell(new PotionEffectSpell(key("luck"), new SpellFuelQuantity(SpellFuelTypes.FUEL_LIGHT, 10), MobEffects.LUCK, 100, 10));
    public static final Spell HEAL = createSpell(new HealSpell(key("heal"), new SpellFuelQuantity(SpellFuelTypes.FUEL_LIGHT, 15), 3, 0.05));

    // 5% AP scaling: 0 AP r=5, 100 AP r=10, 200 AP r=15, 500 AP r=30, 1200 AP r=65
    public static final Spell AREA_GLOW = createSpell(new AreaPotionEffectSpell(key("area_glow"), new SpellFuelQuantity(SpellFuelTypes.FUEL_LIGHT, 10), MobEffects.GLOWING, 160, 0, 5, 0.05, false, ParticleTypes.GLOW));
    public static final Spell FEATHER_FALL = createSpell(new AreaPotionEffectSpell(key("feather_fall"), new SpellFuelQuantity(SpellFuelTypes.FUEL_FORCE, 5), MobEffects.SLOW_FALLING, 100, 0, 5, 0.05, true, ParticleTypes.CLOUD));
    public static final Spell AREA_HEAL = createSpell(new AreaPotionEffectSpell(key("area_heal"), new SpellFuelQuantity(SpellFuelTypes.FUEL_LIGHT, 10), MobEffects.INSTANT_HEALTH, 1, 0, 5, 0.05, true, ColorParticleOption.create(ParticleTypes.FLASH, 0, 1, 0)));
    public static final Spell EXTINGUISH = createSpell(new ExtinguishSpell(key("extinguish"), new SpellFuelQuantity(SpellFuelTypes.FUEL_FORCE, 2)));
    public static final Spell BANISH = createSpell(new PortalSpell(key("banish"), new SpellFuelQuantity(SpellFuelTypes.FUEL_FIRE, 20)));
    public static final Spell LEAP = createSpell(new LeapSpell(key("leap"), new SpellFuelQuantity(SpellFuelTypes.FUEL_FORCE, 3)));
    public static final Spell SUMMON_SKELETON = createSpell(new SkeletonSummonSpell(key("summon_skeleton"), new SpellFuelQuantity(SpellFuelTypes.FUEL_FIRE, 5)));
    public static final Spell ENCHANT = createSpell(new EnchantSpell(key("enchant"), new SpellFuelQuantity(SpellFuelTypes.FUEL_LIGHT, 20)));
    public static final Spell DOWSING = createSpell(new DowsingSpell(key("dowsing"), new SpellFuelQuantity(SpellFuelTypes.FUEL_FORCE, 5)));

    public static Spell getSpell(String name) {
        return getSpell(key(name));
    }

    public static Spell getSpell(Identifier rl) {
        return SPELLS.getOrDefault(rl, getFallbackSpell());
    }

    public static Spell getSpell(Optional<Identifier> rl) {
        return getSpell(rl.orElse(NO_OP.key));
    }

    public static <T extends Spell> T createSpell(T spell) {
        if(SPELLS.containsKey(spell.key)) {
            throw new IllegalArgumentException("duplicate spell key " + spell.key);
        }
        SPELLS.put(spell.key, spell);
        return spell;
    }

    private static Identifier key(String name) {
        return Main.rl(name);
    }

    public static Spell getFallbackSpell() {
        return NO_OP;
    }
}
