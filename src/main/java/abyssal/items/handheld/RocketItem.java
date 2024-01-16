package abyssal.items.handheld;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class RocketItem extends Item {

    private final float strength;
    private static final float JUMP_FACTOR = 0.25f;

    public RocketItem(Properties properties, float strength) {
        super(properties);
        this.strength = strength;
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player p = ctx.getPlayer();
        if(p == null) return InteractionResult.FAIL;
        Vec3 pos = ctx.getClickLocation();
        double force = strength * (2.0D - p.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        if (!(force <= 0.0D)) {
            Vec3 spd = p.getDeltaMovement();
            Vec3 delta = pos.subtract(p.position().add(0,1,0));
            delta = delta.normalize().scale(force);
            p.setDeltaMovement(spd.x / 2.0D - delta.x, spd.y / 2.0D - delta.y*JUMP_FACTOR, spd.z / 2.0D - delta.z);
        }
        return InteractionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        double force = strength * (1.0D - player.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        if (!(force <= 0.0D)) {
            Vec3 spd = player.getDeltaMovement();
            Vec3 delta = entity.position().subtract(player.position()).normalize().scale(force);
            player.setDeltaMovement(spd.x / 2.0D - delta.x, spd.y / 2.0D - delta.y*JUMP_FACTOR, spd.z / 2.0D - delta.z);
        }
        force = strength * (1.0D - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        if (!(force <= 0.0D)) {
            Vec3 spd = entity.getDeltaMovement();
            Vec3 delta = entity.position().subtract(player.position()).normalize().scale(-force);
            entity.setDeltaMovement(spd.x / 2.0D - delta.x, spd.y / 2.0D - delta.y*JUMP_FACTOR, spd.z / 2.0D - delta.z);
        }
        return InteractionResult.SUCCESS;
    }
}
