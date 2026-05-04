package abyssal;

import abyssal.alchemy.Alchemy;
import abyssal.data.ModTags;
import abyssal.generation.OreDist;
import abyssal.init.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.server.ServerLifecycleEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber(modid = Main.MOD_ID)

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
        if(event.getLevel() instanceof ServerLevel serverLevel) {
            long seed = serverLevel.getSeed();
            Alchemy.initAlchemy(seed);
        }
    }


}
