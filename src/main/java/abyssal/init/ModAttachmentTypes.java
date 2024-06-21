package abyssal.init;

import abyssal.Main;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Main.MOD_ID);

    public static final Supplier<AttachmentType<Integer>> COMBAT_TIME = ATTACHMENT_TYPES.register(
            "combat_time", ()-> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
    public static final Supplier<AttachmentType<Integer>> NO_COMBAT_TIME = ATTACHMENT_TYPES.register(
            "no_combat_time", ()-> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());

}
