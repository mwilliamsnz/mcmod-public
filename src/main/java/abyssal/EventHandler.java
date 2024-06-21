package abyssal;

import abyssal.generation.OreDist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

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
