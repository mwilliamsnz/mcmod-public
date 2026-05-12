package abyssal;

import abyssal.client.ClientCoinPurseTooltip;
import abyssal.client.HarmoniserScreen;
import abyssal.client.LapidaryScreen;
import abyssal.client.renderer.ChargedEndermiteRenderer;
import abyssal.client.renderer.TreeSpiderRenderer;
import abyssal.entity.ChargedEndermiteModel;
import abyssal.init.ModDataComponents;
import abyssal.init.ModEntityTypes;
import abyssal.init.ModMenus;
import abyssal.inventory.CoinPurseTooltip;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class ClientEventSubscriber {

    public static final ModelLayerLocation CHARGED_ENDERMITE_LAYER = new ModelLayerLocation(Main.rl("charged_endermite"),"charged_endermite");

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntityTypes.MINION.get(), SkeletonRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.FISH_PAINTING.get(), PaintingRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.TREE_SPIDER.get(), TreeSpiderRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CHARGED_ENDERMITE.get(), ChargedEndermiteRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(CHARGED_ENDERMITE_LAYER, ChargedEndermiteModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerClientTooltipComponentFactory(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(CoinPurseTooltip.class, ClientCoinPurseTooltip::new);
    }

    @SubscribeEvent
    public static void addTooltips(ItemTooltipEvent event) {
        TooltipDisplay tooltipDisplay = event.getItemStack().getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        event.getItemStack().addToTooltip(ModDataComponents.SPELLBOOK.get(), event.getContext(), tooltipDisplay, event.getToolTip()::add, event.getFlags());
        event.getItemStack().addToTooltip(ModDataComponents.SPELL_FUEL_RECHARGE.get(), event.getContext(), tooltipDisplay, event.getToolTip()::add, event.getFlags());
        event.getItemStack().addToTooltip(ModDataComponents.SPELL_BATTERY.get(), event.getContext(), tooltipDisplay, event.getToolTip()::add, event.getFlags());
        event.getItemStack().addToTooltip(ModDataComponents.DESC.get(), event.getContext(), tooltipDisplay, event.getToolTip()::add, event.getFlags());
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.LAPIDARY.get(), LapidaryScreen::new);
        event.register(ModMenus.HARMONISER.get(), HarmoniserScreen::new);
    }

}
