package abyssal.spells;

import abyssal.Main;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class SphericalParticleSpell extends Spell {

    public final ParticleOptions particle;

    protected SphericalParticleSpell(ResourceLocation key, SpellFuelQuantity baseCost, ParticleOptions particle) {
        super(key, baseCost);
        this.particle = particle;
    }

    public void produceParticleInRadius(Level level, Vec3 centre, float radius) {
        Main.LOGGER.debug(radius * radius + " particles");
        for (int i = 0; i < radius*radius; i++) {
            double rx = (level.random.nextDouble() - 0.5) * 2;
            double ry = (level.random.nextDouble() - 0.5) * 2;
            double rz = (level.random.nextDouble() - 0.5) * 2;
            level.addParticle(
                    particle,
                    centre.x + rx * radius,
                    centre.y + ry * radius,
                    centre.z + rz * radius,
                    rx,
                    ry,
                    rz
            );
            if (i == 0) {
                Main.LOGGER.debug("x: " + (centre.x + rx * radius) +
                        ", y: " + (centre.y + ry * radius) +
                        ", z: " + (centre.z + rz * radius));
            }
        }
    }

}
