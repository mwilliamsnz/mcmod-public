package abyssal;

import abyssal.capability.CombatTimeCapability;
import abyssal.capability.CombatTimeCapabilityInterface;
import abyssal.data.ModTags;
import abyssal.init.ModItems;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;


@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InCombatHandler {

    @SubscribeEvent
    public static void combatChecker(LivingHurtEvent event) {
        if(event.getEntity() instanceof Player defender) {
            defender.getCapability(CombatTimeCapability.INSTANCE).ifPresent(CombatTimeCapabilityInterface::recogniseCombat);
            if(isMagic(event.getSource())) { // and not bypassMagic?
                event.setAmount(event.getAmount() * getMagicDamageMultiplier(defender));
            }
        }
        if(event.getSource().getEntity() instanceof Player attacker) {
            attacker.getCapability(CombatTimeCapability.INSTANCE).ifPresent(CombatTimeCapabilityInterface::recogniseCombat);
        }
    }

    private static boolean isMagic(DamageSource source) {
        return source.is(DamageTypeTags.WITCH_RESISTANT_TO);
    }


    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            event.player.getCapability(CombatTimeCapability.INSTANCE).ifPresent(CombatTimeCapabilityInterface::tickCombat);
        }
    }


    @SubscribeEvent
    public static void healAmplifiers(LivingHealEvent event) {
        if(event.getEntity() instanceof Player p) {
            float amp = 1;
            for(ItemStack s : p.getArmorSlots()) {
                if(s.is(ModTags.Items.HEAL_AMPLIFIER)) {
                    amp += 0.25F;
                }
            }
            float heal = event.getAmount();
            event.setAmount(heal * amp);
        }
    }


    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if(event.getTarget() instanceof LivingEntity e) {
            CuriosApi.getCuriosInventory(event.getEntity()).ifPresent((itemHandler)-> {
                itemHandler.findFirstCurio(ModItems.TOXIC_TOTEM.get()).ifPresent((result) -> {
                    e.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                });
            });
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player p = event.getPlayer();
        CuriosApi.getCuriosInventory(p).ifPresent((itemHandler)-> {
            itemHandler.findFirstCurio(ModItems.CLOCKWORK_AMULET.get()).ifPresent((result) -> {
                p.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0, false, false));
                p.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 0, false, false));
            });
        });
    }

    @SubscribeEvent
    public static void potionEffectAdded(MobEffectEvent.Added event) {
        if(event.getEntity() instanceof Player p) {
            boolean tenacity = false;
            for(ItemStack s : p.getArmorSlots()) {
                if(s.is(ModTags.Items.TENACITY_ITEMS)) {
                    tenacity = true;
                    break;
                }
            }
            if(tenacity) {
                MobEffectInstance unmodified = event.getEffectInstance();
                MobEffectInstance existing = event.getOldEffectInstance();


                MobEffectInstance shorter = new MobEffectInstance(
                        unmodified.getEffect(),
                        100, // (int) (unmodified.getDuration() * 0.65),
                        unmodified.getAmplifier(),
                        unmodified.isAmbient(),
                        unmodified.isVisible(),
                        unmodified.showIcon(),
                        existing,
                        unmodified.getEffect().createFactorData()
                );
                // This whole process doesn't actually work, because the effect is not yet on the mob to be removed.
                // The addition of the original effect extends the duration of the new effect
                // MobEffectEvents are uncancellable, and the original effect itself is unmodifiable. Mixins likely needed.
                p.removeEffect(unmodified.getEffect());
                p.forceAddEffect(shorter, event.getEffectSource());
            }
        }
    }



    private static float getMagicDamageMultiplier(Player p) {
        return 100f/ (100f + (float) p.getAttributeValue(ModAttributes.MAGIC_RESIST.get()));
    }
}
