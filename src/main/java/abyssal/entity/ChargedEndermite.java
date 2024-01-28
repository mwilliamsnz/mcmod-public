package abyssal.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class ChargedEndermite extends Endermite implements PowerableMob {

    public ChargedEndermite(EntityType<? extends Endermite> p_33570_, Level p_33571_) {
        super(p_33570_, p_33571_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.35D).add(Attributes.ATTACK_DAMAGE, 14.0D);
    }

    @Override
    public boolean isPowered() {
        return true;
    }
}