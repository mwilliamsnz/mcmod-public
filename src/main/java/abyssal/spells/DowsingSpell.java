package abyssal.spells;

import abyssal.Main;
import abyssal.generation.OreDist;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DowsingSpell extends Spell {
    protected DowsingSpell(Identifier key, SpellFuelQuantity cost) {
        super(key, cost);
    }

    @Override
    public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if (level instanceof ServerLevel serverLevel) {
            long seed = serverLevel.getSeed();
            OreDist.OreChunkType here = Main.oreDist.at(ChunkPos.containing(player.blockPosition()), seed);
            SoundEvent sound = SoundEvents.DEEPSLATE_HIT;
            float r = 0, g = 0, b = 0;
            switch (here) {
                case COAL -> {
                    sound = SoundEvents.CREAKING_HEART_HURT;
                    g = 1;
                    break;
                }
                case COPPER -> {
                    sound = SoundEvents.BELL_RESONATE;
                    g = 1;
                    break;
                }
                case SILVER -> {
                    sound = SoundEvents.BELL_RESONATE;
                    g = 1;
                    break;
                }
                case IRON -> {
                    sound = SoundEvents.BELL_RESONATE;
                    g = 1;
                    break;
                }
                case POOR_IRON -> {
                    sound = SoundEvents.BELL_RESONATE;
                    g = 1;
                    break;
                }
                case GARNET -> {
                    sound = SoundEvents.AMETHYST_BLOCK_CHIME;
                    g = 1;
                    break;
                }
                case GOLD -> {
                    sound = SoundEvents.BELL_RESONATE;
                    g = 1;
                    break;
                }
                case GEMS -> {
                    sound = SoundEvents.AMETHYST_BLOCK_CHIME;
                    g = 1;
                    break;
                }
                case EMERALD -> {
                    sound = SoundEvents.AMETHYST_BLOCK_CHIME;
                    g = 1;
                    break;
                }
                case NONE -> {
                    r = 1;
                    break;
                }
            }
            level.playSound(player, player.blockPosition(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
            produceColourParticles(level, player.position(), 1.5f, r, g, b);
            player.sendSystemMessage(Component.translatable(here.name()));
        }
        return InteractionResult.SUCCESS;
    }

    public void produceColourParticles(Level level, Vec3 centre, float radius, float r, float g, float b) {
        for (int i = 0; i < radius*radius; i++) {
            double rx = (level.getRandom().nextDouble() - 0.5) * 2;
            double ry = (level.getRandom().nextDouble() - 0.5) * 2;
            double rz = (level.getRandom().nextDouble() - 0.5) * 2;
            level.addParticle(
                    ColorParticleOption.create(ParticleTypes.FLASH, 0, 1, 0),
                    centre.x + rx * radius,
                    centre.y + ry * radius,
                    centre.z + rz * radius,
                    rx,
                    ry,
                    rz
            );
        }
    }
}
