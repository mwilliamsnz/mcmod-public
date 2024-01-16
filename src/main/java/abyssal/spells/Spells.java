package abyssal.spells;

import abyssal.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class Spells {

    private static final Map<ResourceLocation, Spell> SPELLS = new HashMap<>();

    public static final String TAG_SPELL_KEY = "key";

    public static final Spell NO_OP = createSpell(new Spell(key("no_op")) {
        public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {return InteractionResultHolder.fail(staff);}
    });
    public static final Spell INVISIBILITY = createSpell(new PotionEffectSpell(key("invisibility"), MobEffects.INVISIBILITY, 100, 1));
    public static final Spell EXTINGUISH = createSpell(new ExtinguishSpell(key("extinguish")));
    public static final Spell BANISH = createSpell(new PortalSpell(key("banish")));
    public static final Spell LEAP = createSpell(new LeapSpell(key("leap")));
    public static final Spell SUMMON_SKELETON = createSpell(new SkeletonSummonSpell(key("summon_skeleton")));
    public static final Spell ENCHANT = createSpell(new EnchantSpell(key("enchant")));

    public static Spell getSpell(String name) {
        return getSpell(key(name));
    }

    public static Spell getSpell(ResourceLocation rl) {
        return SPELLS.getOrDefault(rl, getFallbackSpell());
    }

    public static <T extends Spell> T createSpell(T spell) {
        if(SPELLS.containsKey(spell.key)) {
            throw new IllegalArgumentException("duplicate spell key " + spell.key);
        }
        SPELLS.put(spell.key, spell);
        return spell;
    }

    private static ResourceLocation key(String name) {
        return new ResourceLocation(Main.MOD_ID, name);
    }


    public static Spell getFallbackSpell() {
        return NO_OP;
    }

    public static Spell fromTag(CompoundTag tag) {
        if(tag.contains(TAG_SPELL_KEY)) {
            String s = tag.getString(TAG_SPELL_KEY);
            return getSpell(new ResourceLocation(s));
        }
        return NO_OP;
    }

    public static CompoundTag toTag(Spell spell) {
        CompoundTag tag = new CompoundTag();
        tag.put(TAG_SPELL_KEY, StringTag.valueOf(spell.key.toString()));
        return tag;
    }
}
