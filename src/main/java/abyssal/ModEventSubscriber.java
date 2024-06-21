package abyssal;

import abyssal.entity.ChargedEndermite;
import abyssal.entity.Minion;
import abyssal.entity.TreeSpider;
import abyssal.init.ModAttachmentTypes;
import abyssal.init.ModBlocks;
import abyssal.init.ModEntityTypes;
import abyssal.init.ModItems;
import abyssal.items.curios.Gobbler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

    @SubscribeEvent
    public static void onRegisterItems(RegisterEvent event) {
        event.register(Registries.ITEM,
                itemRegisterHelper -> {
                    ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).forEach(
                        block -> {
                            final Item.Properties properties = new Item.Properties();
                            final BlockItem blockItem = new BlockItem(block, properties);
                            itemRegisterHelper.register(BuiltInRegistries.BLOCK.getKey(block), blockItem);
                        }
                    );
                }
        );
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to existing tabs
        ModItems.itemTabs.getOrDefault(event.getTab(), new ArrayList<>()).forEach((itemSupplier) -> event.accept(itemSupplier.get()));
    }


    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.MINION.get(), Minion.buildBaseAttributes());
        event.put(ModEntityTypes.TREE_SPIDER.get(), TreeSpider.createTreeSpider().build());
        event.put(ModEntityTypes.CHARGED_ENDERMITE.get(), ChargedEndermite.createAttributes().build());
    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.MAGIC_RESIST.get());
        event.add(EntityType.PLAYER, ModAttributes.ABILITY_POWER.get());
    }

    @SubscribeEvent
    public static void registerCapabilities(final RegisterCapabilitiesEvent evt) {

        // Rejuvenation beads

        evt.registerItem(
                CuriosCapability.ITEM,
                rejuvCurio(0.0025f),
                ModItems.REJUVENATION_BEAD.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                rejuvCurio(0.0025f),
                ModItems.REJUVENATION_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                rejuvCurio(0.0075f),
                ModItems.REJUVENATION_BELT.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                rejuvCurio(0.0075f),
                ModItems.REJUVENATION_NECKLACE.get());

        // Attribute curios

        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(ModAttributes.MAGIC_RESIST.get(), new AttributeModifier(uuid, "Magic resistance", 25, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.NULL_MAGIC_MANTLE.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Knockback Resistance", 0.40, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.ANCHOR_BELT.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.LUCK, new AttributeModifier(uuid, "Luck", 1, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.FOUR_LEAF_CLOVER.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.LUCK, new AttributeModifier(uuid, "Luck", 2, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.LUCK_CHARM.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Max HP", 3, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.RUBY_CRYSTAL.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage", 1, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.DAMAGE_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage", 2, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.CHAMPIONS_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(ModAttributes.ABILITY_POWER.get(), new AttributeModifier(uuid, "Ability Power", 15, AttributeModifier.Operation.ADDITION));
                    modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Max Health", 1, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.DORANS_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Movement speed", -0.15, AttributeModifier.Operation.MULTIPLY_BASE));
                    modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Max Health", 7, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.GIANTS_BELT.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(ModAttributes.ABILITY_POWER.get(), new AttributeModifier(uuid, "Ability Power", 30, AttributeModifier.Operation.ADDITION));
                    modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Movement Speed", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
                }),
                ModItems.AETHER_WISP.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Movement speed", 0.07, AttributeModifier.Operation.ADDITION));
                    modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Max Health", -8, AttributeModifier.Operation.ADDITION));
                }),
                ModItems.LIGHT_TRAVEL_RING.get());
        evt.registerItem(
                CuriosCapability.ITEM,
                attributeCurio((modifiers, uuid)->{
                    modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Attack Damage", 0.30, AttributeModifier.Operation.MULTIPLY_BASE));
                    modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Max Health", -6, AttributeModifier.Operation.ADDITION));
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
                            // Work out best way to directly hurt without giving invulnerability frames
                            e.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 15));
                            e.addEffect(new MobEffectInstance(MobEffects.POISON, 15, 1));
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



    private static ICapabilityProvider<ItemStack, Void, ICurio> attributeCurio(BiConsumer<Multimap<Attribute, AttributeModifier>, UUID> attributeAdder) {
        return (ItemStack stack, Void v) -> new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                                UUID uuid) {
                Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
                attributeAdder.accept(modifiers, uuid);
                return modifiers;
            }

            @Override
            public void onUnequip(SlotContext ctx, ItemStack newStack){
                ctx.entity().setHealth(ctx.entity().getHealth()); // Clamp
            }

            @Override
            public void onEquip(SlotContext ctx, ItemStack newStack){
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
                        for(var inSlot : p.getAllSlots()) {
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
