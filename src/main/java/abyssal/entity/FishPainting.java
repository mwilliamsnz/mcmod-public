package abyssal.entity;

import abyssal.data.ModTags;
import abyssal.init.ModItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FishPainting extends Painting {

    public FishPainting(Level level, BlockPos pos, Direction direction, Holder<PaintingVariant> variant) {
        super(level, pos, direction, variant);
    }

    public FishPainting(EntityType<? extends Painting> p_31904_, Level p_31905_) {
        super(p_31904_, p_31905_);
    }

    private void setVariant(Holder<PaintingVariant> variant) {
        this.entityData.set(Painting.DATA_PAINTING_VARIANT_ID, variant);
    }

    private static int variantArea(Holder<PaintingVariant> variant) {
        return variant.value().area();
    }

    public static Optional<FishPainting> createFish(Level level, BlockPos pos, Direction direction) {
        FishPainting painting = new FishPainting(level, pos, direction, VariantUtils.getAny(level.registryAccess(), Registries.PAINTING_VARIANT));
        List<Holder<PaintingVariant>> list = new ArrayList<>();
        level.registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(ModTags.Paintings.FISH).forEach(list::add);
        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            painting.setDirection(direction);
            list.removeIf(p_412922_ -> {
                painting.setVariant((Holder<PaintingVariant>)p_412922_);
                return !painting.survives();
            });
            if (list.isEmpty()) {
                return Optional.empty();
            } else {
                int i = list.stream().mapToInt(FishPainting::variantArea).max().orElse(0);
                list.removeIf(p_218883_ -> variantArea((Holder<PaintingVariant>)p_218883_) < i);
                Optional<Holder<PaintingVariant>> optional = Util.getRandomSafe(list, painting.random);
                if (optional.isEmpty()) {
                    return Optional.empty();
                } else {
                    painting.setVariant(optional.get());
                    painting.setDirection(direction);
                    return Optional.of(painting);
                }
            }
        }
    }

    @Override
    public void dropItem(ServerLevel p_376289_, @Nullable Entity p_31925_) {
        if (p_376289_.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
            if (!(p_31925_ instanceof Player player && player.hasInfiniteMaterials())) {
                this.spawnAtLocation(p_376289_, ModItems.FISH_PAINTING.get());
            }
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.FISH_PAINTING.get());
    }
}
