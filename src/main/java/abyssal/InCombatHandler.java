package abyssal;

import abyssal.init.ModAttachmentTypes;
import abyssal.init.ModItems;
import abyssal.init.ModPotionEffectTypes;
import abyssal.items.AttributeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosSlotTypes;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.Optional;


@EventBusSubscriber(modid = Main.MOD_ID)
public class InCombatHandler {

    private static final long TICKS_BEFORE_LEAVING_COMBAT = 100;

    public static void recogniseCombat(Entity e) {
        e.setData(ModAttachmentTypes.NO_COMBAT_TIME, 0);
        if(e.getData(ModAttachmentTypes.COMBAT_TIME) == 0) {
            e.setData(ModAttachmentTypes.COMBAT_TIME, 1);
        }
    }

    public static void tickCombat(Entity e) {
        int ooc = e.getData(ModAttachmentTypes.NO_COMBAT_TIME);
        e.setData(ModAttachmentTypes.NO_COMBAT_TIME,  ooc + 1);
        if(ooc >= TICKS_BEFORE_LEAVING_COMBAT) {
            e.setData(ModAttachmentTypes.COMBAT_TIME, 0);
        }
        int ic = e.getData(ModAttachmentTypes.COMBAT_TIME);
        if(ic > 0) {
            e.setData(ModAttachmentTypes.COMBAT_TIME, ic + 1);
        }
    }

    private static boolean isMagic(DamageSource source) {
        return source.is(DamageTypeTags.WITCH_RESISTANT_TO) || source.is(DamageTypeTags.IS_LIGHTNING);
    }

    @SubscribeEvent
    public static void combatChecker(LivingIncomingDamageEvent event) {
        if(event.getEntity() instanceof Player defender) {
            recogniseCombat(defender);
            if(isMagic(event.getSource())) { // and not bypassMagic?
                event.setAmount(event.getAmount() * getMagicDamageMultiplier(defender));
            }
        }
        if(event.getSource().getEntity() instanceof Player attacker) {
            recogniseCombat(attacker);
        }
    }

    @SubscribeEvent
    public static void onGetHit(LivingIncomingDamageEvent event) {
        LivingEntity defender = event.getEntity();
        if(defender.level() instanceof ServerLevel level) {
            Entity srcE = event.getSource().getEntity();
            if(srcE != null && srcE instanceof LivingEntity attacker) {
                if(defender.getItemBySlot(EquipmentSlot.BODY).is(ModItems.THORNMAIL)) {
                    double armour = defender.getAttributeValue(Attributes.ARMOR);
                    float damage = (float) (1 + armour / 10);
                    DamageSource s = level.damageSources().cactus();
                    Optional<Holder.Reference<DamageType>> thornsDamageType = level.damageSources().damageTypes.get(Main.rl("abyssal_thorns"));
                    if(thornsDamageType.isPresent()) {
                        s = new DamageSource(thornsDamageType.get());
                    }
                    attacker.hurtServer(level, s, damage);
                    attacker.addEffect(new MobEffectInstance(Holder.direct(ModPotionEffectTypes.WOUNDED.get()), 200, 1), defender);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerNameEvent(PlayerEvent.NameFormat event) {
        CuriosApi.getCuriosInventory(event.getEntity()).ifPresent((itemHandler)-> {
            itemHandler.findFirstCurio(ModItems.GLOWING_CARD.get()).ifPresent((result) -> {
                event.setDisplayname(Component.translatable("abyssal.glowing_card.playername").withStyle(ChatFormatting.GREEN));
            });
        });
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event) {
        Player p = event.getEntity();
        tickCombat(p);
        double regen = p.getAttributeValue(ModAttributes.REGEN);
        if(regen > 0) {
            p.heal((float) regen / 100);
        }
    }


    @SubscribeEvent
    public static void healAmplifiers(LivingHealEvent event) {
        if (event.getEntity() instanceof Player p) {
            float heal = event.getAmount();
            double amp = p.getAttributeValue(ModAttributes.HEAL_RATE);
            event.setAmount((float) (heal * amp));
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
    public static void onBlockBreak(BreakBlockEvent event) {
        Player p = event.getPlayer();
        CuriosApi.getCuriosInventory(p).ifPresent((itemHandler) -> {
            itemHandler.findFirstCurio(ModItems.CLOCKWORK_AMULET.get()).ifPresent((result) -> {
                p.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 0, false, false));
                p.addEffect(new MobEffectInstance(MobEffects.HASTE, 200, 0, false, false));
            });
        });
    }

    @SubscribeEvent
    public static void potionEffectAdded(MobEffectEvent.Added event) {
        if(event.getEntity() instanceof Player p) {
            double tenacity = p.getAttributeValue(ModAttributes.TENACITY);
            if(tenacity != 1) {
                MobEffectInstance unmodified = event.getEffectInstance();

                if(!unmodified.getEffect().value().isBeneficial()) {
                    unmodified.duration = (int) (unmodified.getDuration() / tenacity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent.Item event) {
        if(event.getIdentifier().equals("ring") && !event.getTo().isEmpty()) {
            ISlotType slot = CuriosSlotTypes.getSlotTypes().get("ring");
            AttributeHelper.relabelCurioModifiers(event.getTo(), slot,"slot" + event.getSlotIndex());
        }
    }

    private static float getMagicDamageMultiplier(Player p) {
        return 100f/ (100f + (float) p.getAttributeValue(ModAttributes.MAGIC_RESIST));
    }
}
