package abyssal;

import abyssal.data.ModTags;
import abyssal.init.ModAttachmentTypes;
import abyssal.init.ModItems;
import abyssal.items.AttributeHelper;
import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosSlotTypes;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.type.ISlotType;


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
    public static void tick(PlayerTickEvent.Post event) {
//        if(event == LogicalSide.SERVER) {
            tickCombat(event.getEntity());
//        }
    }


    @SubscribeEvent
    public static void healAmplifiers(LivingHealEvent event) {
        if(event.getEntity() instanceof Player p) {
            float amp = 1;
            EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.BODY};
            for(EquipmentSlot slot : slots) {
                ItemStack s = p.getItemBySlot(slot);
                if(s.is(ModTags.Items.HEAL_AMPLIFIER)) { // TODO datamap/attribute this?
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
                p.addEffect(new MobEffectInstance(MobEffects.HASTE, 200, 0, false, false));
            });
        });
    }

    @SubscribeEvent
    public static void potionEffectAdded(MobEffectEvent.Added event) {
        if(event.getEntity() instanceof Player p) {
            boolean tenacity = false;

            EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.BODY};
            for(EquipmentSlot slot : slots) {
                ItemStack s = p.getItemBySlot(slot);
                if(s.is(ModTags.Items.TENACITY_ITEMS)) {
                    tenacity = true;
                    break;
                }
            }
            if(tenacity) {
                MobEffectInstance unmodified = event.getEffectInstance();

                if(!unmodified.getEffect().value().isBeneficial()) {
                    unmodified.duration = (int) (unmodified.getDuration() * 0.65);
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
        return 100f/ (100f + (float) p.getAttributeValue(Holder.direct(ModAttributes.MAGIC_RESIST.get())));
    }
}
