package abyssal.init;

import abyssal.Main;
import abyssal.generation.OreDist;
import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Main.MOD_ID);

    // Entity attachments

    public static final Supplier<AttachmentType<Integer>> COMBAT_TIME = ATTACHMENT_TYPES.register(
            "combat_time", ()-> AttachmentType.builder(() -> 0).build());
    public static final Supplier<AttachmentType<Integer>> NO_COMBAT_TIME = ATTACHMENT_TYPES.register(
            "no_combat_time", ()-> AttachmentType.builder(() -> 0).build());

    // Level attachments

    public static final Supplier<AttachmentType<OreDist>> ORE_DIST = ATTACHMENT_TYPES.register(
            "ore_dist", () -> AttachmentType.builder(holder -> {
                        if (holder instanceof ServerLevel level) {
                            return new OreDist(level.getSeed());
                        }

                        throw new IllegalStateException("OreDist can only be attached to a ServerLevel");
                    }).build()
            );

}
