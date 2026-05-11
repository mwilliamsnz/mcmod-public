package abyssal;

import abyssal.entity.ChargedEndermite;
import abyssal.entity.Minion;
import abyssal.entity.TreeSpider;
import abyssal.init.ModAttachmentTypes;
import abyssal.init.ModBlocks;
import abyssal.init.ModEntityTypes;
import abyssal.init.ModItems;
import abyssal.items.curios.Gobbler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.theillusivec4.curios.api.*;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static abyssal.Main.rl;

@EventBusSubscriber(modid = Main.MOD_ID)
public class ModEventSubscriber {

    @SubscribeEvent
    public static void onRegisterItems(RegisterEvent event) {
        event.register(Registries.ITEM,
                itemRegisterHelper -> {
//                    Set<Block> blocks = ModBlocks.DATAGEN_MODEL.stream().map(Holder::value).collect(Collectors.toSet());
                    ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).forEach(
                            block -> {
                                Identifier key = BuiltInRegistries.BLOCK.getKey(block);
//                                if(!blocks.contains(block)) {
                                    itemRegisterHelper.register(key, new BlockItem(block, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, key))));
//                                }
                            }
                    );

                }
        );
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to existing tabs
        ModItems.itemTabs.getOrDefault(event.getTabKey(), new ArrayList<>()).forEach((itemSupplier) -> event.accept(itemSupplier.get()));
    }


    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.MINION.get(), Minion.buildBaseAttributes());
        event.put(ModEntityTypes.TREE_SPIDER.get(), TreeSpider.createTreeSpider().build());
        event.put(ModEntityTypes.CHARGED_ENDERMITE.get(), ChargedEndermite.createAttributes().build());
    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.MAGIC_RESIST);
        event.add(EntityType.PLAYER, ModAttributes.ABILITY_POWER);
        event.add(EntityType.PLAYER, ModAttributes.HEAL_RATE);
        event.add(EntityType.PLAYER, ModAttributes.REGEN);
        event.add(EntityType.PLAYER, ModAttributes.TENACITY);
    }

    @SubscribeEvent
    public static void registerCapabilities(final RegisterCapabilitiesEvent evt) {

        // Attribute curios

        evt.registerItem(CuriosCapability.ITEM, attributeCurio((builder, slot)->{
            builder.addModifier(ModAttributes.REGEN,
                    new AttributeModifier(rl("rejuv_bead"), 0.25f, AttributeModifier.Operation.ADD_VALUE));
        }), ModItems.REJUVENATION_BEAD.get());

        evt.registerItem(CuriosCapability.ITEM, ringAttributeCurio((builder, slot)->{
            builder.addModifier(ModAttributes.REGEN,
                    new AttributeModifier(rl("rejuv_bead_ring"), 0.25f, AttributeModifier.Operation.ADD_VALUE));
        }), ModItems.REJUVENATION_RING.get());

        evt.registerItem(CuriosCapability.ITEM, attributeCurio((builder, slot)->{
            builder.addModifier(ModAttributes.REGEN,
                    new AttributeModifier(rl("rejuv_bead_belt"), 0.75f, AttributeModifier.Operation.ADD_VALUE));
        }), ModItems.REJUVENATION_BELT.get());

        evt.registerItem(CuriosCapability.ITEM, attributeCurio((builder, slot)->{
            builder.addModifier(ModAttributes.REGEN,
                    new AttributeModifier(rl("rejuv_bead_necklace"), 0.75f, AttributeModifier.Operation.ADD_VALUE));
        }), ModItems.REJUVENATION_NECKLACE.get());

        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((builder, slot)->{
                    builder.addModifier(ModAttributes.MAGIC_RESIST,
                            new AttributeModifier(rl("null_mantle_mr"), 25, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.NULL_MAGIC_MANTLE.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.KNOCKBACK_RESISTANCE, 
                            new AttributeModifier(rl("anchor_belt_resist"), 0.40, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.ANCHOR_BELT.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.LUCK, 
                            new AttributeModifier(rl("clover_luck"), 1, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.FOUR_LEAF_CLOVER.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.LUCK, 
                            new AttributeModifier(rl("luck_charm_luck"), 2, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.LUCK_CHARM.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.MAX_HEALTH, 
                            new AttributeModifier(rl("ruby_hp"), 3, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.RUBY_CRYSTAL.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                ringAttributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.ATTACK_DAMAGE, 
                            new AttributeModifier(rl("fighters_ring_damage_slot0"), 1, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.DAMAGE_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                ringAttributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.ATTACK_DAMAGE, 
                            new AttributeModifier(rl("champions_ring_damage_slot0"), 2, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.CHAMPIONS_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                ringAttributeCurio((builder, slot)->{
                    builder.addModifier(ModAttributes.ABILITY_POWER,
                            new AttributeModifier(rl("dorans_ap_slot0"), 18, AttributeModifier.Operation.ADD_VALUE));
                    builder.addModifier(Attributes.MAX_HEALTH, 
                            new AttributeModifier(rl("dorans_hp_slot0"), 2, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.DORANS_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.MOVEMENT_SPEED, 
                            new AttributeModifier(rl("giant_belt_slow"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    builder.addModifier(Attributes.MAX_HEALTH, 
                            new AttributeModifier(rl("giant_belt_hp"), 7, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.GIANTS_BELT.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((builder, slot)->{
                    builder.addModifier(ModAttributes.ABILITY_POWER,
                            new AttributeModifier(rl("wisp_ap"), 30, AttributeModifier.Operation.ADD_VALUE));
                    builder.addModifier(Attributes.MOVEMENT_SPEED,
                            new AttributeModifier(rl("wisp_speed"), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                }),
                ModItems.AETHER_WISP.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                ringAttributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.MOVEMENT_SPEED,
                            new AttributeModifier(rl("travel_ring_speed_slot0"), 0.07, AttributeModifier.Operation.ADD_VALUE));
                    builder.addModifier(Attributes.MAX_HEALTH,
                            new AttributeModifier(rl("travel_ring_hp_slot0"), -8, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.LIGHT_TRAVEL_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                ringAttributeCurio((builder, slot)->{
                    builder.addModifier(Attributes.ATTACK_DAMAGE, 
                            new AttributeModifier(rl("glass_cannon_slot0"), 0.30, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    builder.addModifier(Attributes.MAX_HEALTH, 
                            new AttributeModifier(rl("glass_cannon_hp_slot0"), -6, AttributeModifier.Operation.ADD_VALUE));
                }),
                ModItems.GLASS_CANNON_RING.get());

        // Ticking curios

        evt.registerItem(
                CuriosCapability.ITEM,
                tickingCurio((ctx) -> {
                    ctx.entity().addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0, false, false));
                    ctx.entity().addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 0, false, false));
                }),
                ModItems.GLOW_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                tickingCurio((ctx) -> {
                    if (ctx.entity().isUnderWater()) {
                        ctx.entity().addEffect(new MobEffectInstance(MobEffects.HUNGER, 25, 2));
                        if (ctx.entity().tickCount % 3 != 0) { // "Un-tick" air supply on two out of every three ticks
                            int air = ctx.entity().getAirSupply();
                            ctx.entity().setAirSupply(Math.min(air + 1, ctx.entity().getMaxAirSupply()));
                        }
                    }
                }),
                ModItems.FISH_NECKLACE.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                tickingCurio((ctx) -> {
                    LivingEntity e = ctx.entity();
                    if(!e.level().isClientSide()) {
                        if(e.getData(ModAttachmentTypes.COMBAT_TIME) > 0 ) {
                            e.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 5));
                            if (!e.getActiveEffectsMap().containsKey(MobEffects.POISON)
                                    || e.getActiveEffectsMap().get(MobEffects.POISON).getAmplifier() < 1
                                    || e.getActiveEffectsMap().get(MobEffects.POISON).duration <= 2) {
                                e.addEffect(new MobEffectInstance(MobEffects.POISON, 15, 1));
                            }
                        }
                    }
                }),
                ModItems.RAGE_TOTEM.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                tickingCurio((ctx) -> {
                    LivingEntity e = ctx.entity();
                    if(e.hasEffect(MobEffects.POISON)) {
                        e.removeEffect(MobEffects.POISON);
                    }
                }),
                ModItems.CLEANSING_TOTEM.get());
    }



    private static ICapabilityProvider<ItemStack, Void, ICurio> attributeCurio(BiConsumer<CurioAttributeModifiers.Builder, String> attributeAdder) {
        return (ItemStack stack, Void v) -> new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public CurioAttributeModifiers getDefaultCurioAttributeModifiers() {
                Map<String, ISlotType> slots = CuriosSlotTypes.getItemSlotTypes(this.getStack(), true);
                CurioAttributeModifiers.Builder builder = CurioAttributeModifiers.builder();
                
                for (String slot : slots.keySet()) {
                    attributeAdder.accept(builder, slot);
//                    builder.addModifier(attributeHolder, attributeModifier, slot)
                }
                return builder.build();
            }

            @Override
            public void onUnequip(SlotContext ctx, ItemStack newStack) {
                ctx.entity().setHealth(ctx.entity().getHealth()); // Clamp
            }

            @Override
            public void onEquip(SlotContext ctx, ItemStack prevStack) {
                ctx.entity().setHealth(ctx.entity().getHealth()); // Clamp
            }
        };
    }

    private static ICapabilityProvider<ItemStack, Void, ICurio> ringAttributeCurio(BiConsumer<CurioAttributeModifiers.Builder, String> attributeAdder) {
        return (ItemStack stack, Void v) -> new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public CurioAttributeModifiers getDefaultCurioAttributeModifiers() {
                Map<String, ISlotType> slots = CuriosSlotTypes.getItemSlotTypes(this.getStack(), true);
                CurioAttributeModifiers.Builder builder = CurioAttributeModifiers.builder();

                for (String slot : slots.keySet()) {
                    attributeAdder.accept(builder, slot);
//                    builder.addModifier(attributeHolder, attributeModifier, slot)
                }
                return builder.build();
            }

            @Override
            public void onUnequip(SlotContext ctx, ItemStack newStack) {
                ctx.entity().setHealth(ctx.entity().getHealth()); // Clamp
            }

            @Override
            public void onEquip(SlotContext ctx, ItemStack prevStack) {
//                AttributeHelper.relabelCurioModifiers(getStack(), ctx, "slot" + ctx.index());
                ctx.entity().setHealth(ctx.entity().getHealth()); // Clamp
            }
        };
    }

    private static ICapabilityProvider<ItemStack, Void, ICurio> tickingCurio(Consumer<SlotContext> tickAction) {
        return (ItemStack stack, Void v) -> new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                tickAction.accept(slotContext);
            }
        };
    }

    private static ICapabilityProvider<ItemStack, Void, ICurio> rejuvCurio(float rate) {
        return (ItemStack stack, Void v) -> new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                slotContext.entity().heal(rate);
            }
        };
    }

    private static ICapabilityProvider<ItemStack, Void, ICurio> gobblerCurio(Gobbler gobbler) {
        return (ItemStack stack, Void v) -> new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                LivingEntity e = slotContext.entity();
                if(e.tickCount % gobbler.ticksPerGobble() == 0) {
                    if(e instanceof Player p) {
                        for (int i = 0; i < 36; i++) {
                            ItemStack inSlot = p.getSlot(1).get();
                            if(inSlot.is(gobbler.canGobble())) {
                                inSlot.shrink(1);
                                gobbler.gobble(p.level(), p.getInventory()::placeItemBackInInventory);
                                return;
                            }
                        }
                    }
                }
            }
        };
    }

}
