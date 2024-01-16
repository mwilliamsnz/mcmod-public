package abyssal;

import abyssal.entity.Minion;
import abyssal.entity.TreeSpider;
import abyssal.init.ModBlocks;
import abyssal.init.ModEntityTypes;
import abyssal.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

    @SubscribeEvent
    public static void onRegisterItems(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS,
                itemRegisterHelper -> {
                    ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(
                        block -> {
                            final Item.Properties properties = new Item.Properties();
                            final BlockItem blockItem = new BlockItem(block, properties);
                            itemRegisterHelper.register(ForgeRegistries.BLOCKS.getKey(block), blockItem);
                        }
                    );
                }
        );
    }

    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to existing tabs
        ModItems.itemTabs.getOrDefault(event.getTab(), new ArrayList<>()).forEach(event::accept);
    }


    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.MINION.get(), Minion.buildBaseAttributes());
        event.put(ModEntityTypes.TREE_SPIDER.get(), TreeSpider.createTreeSpider().build());
    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.MAGIC_RESIST.get());
        event.add(EntityType.PLAYER, ModAttributes.ABILITY_POWER.get());
    }

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            Minecraft.getInstance().getItemColors().register((itemlike, layer) -> {
                return layer > 0 ? -1 : ((DyeableLeatherItem)itemlike.getItem()).getColor(itemlike);
            }, ModItems.CLOTH_HELMET.get(), ModItems.CLOTH_CHESTPLATE.get(), ModItems.CLOTH_LEGGINGS.get(), ModItems.CLOTH_BOOTS.get());
        });
    }

}
