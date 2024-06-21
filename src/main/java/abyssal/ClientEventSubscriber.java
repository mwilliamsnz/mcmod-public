package abyssal;

import abyssal.client.ClientCoinPurseTooltip;
import abyssal.client.renderer.ChargedEndermiteRenderer;
import abyssal.client.renderer.FishPaintingRenderer;
import abyssal.client.renderer.TreeSpiderRenderer;
import abyssal.init.ModEntityTypes;
import abyssal.init.ModMenus;
import abyssal.inventory.CoinPurseTooltip;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventSubscriber {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(ModEntityTypes.MINION.get(), SkeletonRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.FISH_PAINTING.get(), FishPaintingRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.TREE_SPIDER.get(), TreeSpiderRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CHARGED_ENDERMITE.get(), ChargedEndermiteRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientTooltipComponentFactory(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(CoinPurseTooltip.class, ClientCoinPurseTooltip::new);
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(ModMenus::registerMenuScreens);
    }

}
