package abyssal.init;

import abyssal.Main;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPOIs {

    public static final DeferredRegister<PoiType> POI_TYPES =  DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, Main.MOD_ID);

    public static final Supplier<PoiType> WASP_PORT = POI_TYPES.register("wasp_port",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.WASP_PORT.get().getStateDefinition().getPossibleStates()), 0, 1)
    );

}
