package abyssal.entity;

import abyssal.Main;
import abyssal.init.ModEntityTypes;
import abyssal.init.ModItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FishPainting extends HangingEntity {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, Main.MOD_ID);

    public static final RegistryObject<PaintingVariant> JFISH1 = PAINTING_VARIANTS.register("fish_1", () -> new PaintingVariant(16, 16));
    public static final RegistryObject<PaintingVariant> JFISH2 = PAINTING_VARIANTS.register("fish_2", () -> new PaintingVariant(32, 32));
    public static final RegistryObject<PaintingVariant> JFISH3 = PAINTING_VARIANTS.register("fish_3", () -> new PaintingVariant(64, 64));
    public static final RegistryObject<PaintingVariant> JFISH4 = PAINTING_VARIANTS.register("fish_4", () -> new PaintingVariant(32, 32));
    public static final RegistryObject<PaintingVariant> JFISH5 = PAINTING_VARIANTS.register("fish_5", () -> new PaintingVariant(32, 32));
    public static final RegistryObject<PaintingVariant> JFISH6 = PAINTING_VARIANTS.register("fish_6", () -> new PaintingVariant(64, 64));
    public static final RegistryObject<PaintingVariant> JFISH7 = PAINTING_VARIANTS.register("fish_7", () -> new PaintingVariant(64, 64));
    public static final RegistryObject<PaintingVariant> JFISH8 = PAINTING_VARIANTS.register("fish_8", () -> new PaintingVariant(16, 16));

    public static final RegistryObject<PaintingVariant> RICK = PAINTING_VARIANTS.register("rick", () -> new PaintingVariant(64, 64));
    public static final RegistryObject<PaintingVariant> CRETIN_1 = PAINTING_VARIANTS.register("cretin_1", () -> new PaintingVariant(64, 64));
    public static final RegistryObject<PaintingVariant> GORF = PAINTING_VARIANTS.register("gorf", () -> new PaintingVariant(64, 32));
    public static final RegistryObject<PaintingVariant> HOUSE = PAINTING_VARIANTS.register("house", () -> new PaintingVariant(32, 32));
    public static final RegistryObject<PaintingVariant> DRICK = PAINTING_VARIANTS.register("drick", () -> new PaintingVariant(32, 32));
    public static final RegistryObject<PaintingVariant> FACE = PAINTING_VARIANTS.register("face", () -> new PaintingVariant(32, 32));
    public static final RegistryObject<PaintingVariant> FACE_2 = PAINTING_VARIANTS.register("face_2", () -> new PaintingVariant(16, 16));
    public static final RegistryObject<PaintingVariant> KEY = PAINTING_VARIANTS.register("key", () -> new PaintingVariant(64, 64));

    private static final List<RegistryObject<PaintingVariant>> FISH_PAINTINGS = List.of(JFISH1, JFISH2, JFISH3, JFISH4, JFISH5, JFISH6, JFISH7, JFISH8);
//    private static final List<RegistryObject<PaintingVariant>> FISH_PAINTINGS = List.of();

    private static final EntityDataAccessor<Holder<PaintingVariant>> DATA_PAINTING_VARIANT_ID = SynchedEntityData.defineId(FishPainting.class, EntityDataSerializers.PAINTING_VARIANT);
    private static final ResourceKey<PaintingVariant> DEFAULT_VARIANT = PaintingVariants.KEBAB;

    private static Holder<PaintingVariant> getDefaultVariant() {
        return ForgeRegistries.PAINTING_VARIANTS.getHolder(DEFAULT_VARIANT).orElseThrow();
    }

    public FishPainting(EntityType<? extends FishPainting> entity, Level level) {
        super(entity, level);
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_PAINTING_VARIANT_ID, getDefaultVariant());
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_PAINTING_VARIANT_ID.equals(accessor)) {
            this.recalculateBoundingBox();
        }
    }

    private void setVariant(Holder<PaintingVariant> variant) {
        this.entityData.set(DATA_PAINTING_VARIANT_ID, variant);
    }

    public Holder<PaintingVariant> getVariant() {
        return this.entityData.get(DATA_PAINTING_VARIANT_ID);
    }

    public static Optional<FishPainting> create(Level level, BlockPos pos, Direction dir) {

        FishPainting painting = new FishPainting(level, pos);
        List<Holder<PaintingVariant>> list = new ArrayList<>();
        for (RegistryObject<PaintingVariant> rp : FISH_PAINTINGS) {
            list.add(Holder.direct(rp.get()));
        }
        if (list.isEmpty()) {
            return Optional.empty();
        }
        painting.setDirection(dir);
        list.removeIf((variant) -> {
            painting.setVariant(variant);
            return !painting.survives();
        });
        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            int i = list.stream().mapToInt(FishPainting::variantArea).max().orElse(0);
            list.removeIf((variant) -> {
                return variantArea(variant) < i;
            });
            Optional<Holder<PaintingVariant>> optional = Util.getRandomSafe(list, painting.random);
            if (optional.isEmpty()) {
                return Optional.empty();
            } else {
                painting.setVariant(optional.get());
                painting.setDirection(dir);
                return Optional.of(painting);
            }
        }

    }


    private static int variantArea(Holder<PaintingVariant> variant) {
        return variant.value().getWidth() * variant.value().getHeight();
    }

    private FishPainting(Level level, BlockPos blockPos) {
        super(ModEntityTypes.FISH_PAINTING.get(), level, blockPos);
    }

    public FishPainting(Level level, BlockPos pos, Direction dir, Holder<PaintingVariant> var) {
        this(level, pos);
        this.setVariant(var);
        this.setDirection(dir);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("variant", this.getVariant().unwrapKey().orElse(DEFAULT_VARIANT).location().toString());
        tag.putByte("facing", (byte)this.direction.get2DDataValue());
        super.addAdditionalSaveData(tag);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        ResourceKey<PaintingVariant> resourcekey = ResourceKey.create(Registries.PAINTING_VARIANT, ResourceLocation.tryParse(tag.getString("variant")));
        Optional<Holder<PaintingVariant>> h = ForgeRegistries.PAINTING_VARIANTS.getHolder(resourcekey);
        if(h.isPresent()) {
            this.setVariant(h.get());
        } else {
            this.setVariant(FishPainting.getDefaultVariant());
        }
        this.direction = Direction.from2DDataValue(tag.getByte("facing"));
        super.readAdditionalSaveData(tag);
        this.setDirection(this.direction);
    }

    public int getWidth() {
        return this.getVariant().value().getWidth();
    }

    public int getHeight() {
        return this.getVariant().value().getHeight();
    }

    public void dropItem(@Nullable Entity breaker) {
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
            if (breaker instanceof Player player) {
                if (player.getAbilities().instabuild) {
                    return;
                }
            }

            this.spawnAtLocation(ModItems.FISH_PAINTING.get());
        }
    }

    public void playPlacementSound() {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
    }

    public void moveTo(double x, double y, double z, float p_31932_, float p_31933_) {
        this.setPos(x, y, z);
    }

    public void lerpTo(double x, double y, double z, float p_31920_, float p_31921_, int p_31922_, boolean p_31923_) {
        this.setPos(x, y, z);
    }

    public Vec3 trackingPosition() {
        return Vec3.atLowerCornerOf(this.pos);
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.direction.get3DDataValue(), this.getPos());
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDirection(Direction.from3DDataValue(packet.getData()));
    }

    public ItemStack getPickResult() {
        return new ItemStack(ModItems.FISH_PAINTING.get());
    }
}
