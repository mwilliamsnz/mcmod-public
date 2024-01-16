package abyssal;

import abyssal.generation.OreDist;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class EventHandler {


    @SubscribeEvent
    public static void onTryCreatePortal(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void getEntityVisibilityMultiplier(LivingEvent.LivingVisibilityEvent event) {
        if(event.getEntity().isCurrentlyGlowing()) {
            event.modifyVisibility(5.0);
        }
    }

    @SubscribeEvent
    public static void worldLoad(LevelEvent.Load event) {
        Main.oreDist = new OreDist(); // To be sure all the cache is cleared
    }


}
