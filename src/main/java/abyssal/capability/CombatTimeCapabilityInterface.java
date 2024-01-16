package abyssal.capability;


import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface CombatTimeCapabilityInterface extends INBTSerializable<CompoundTag> {

    long getTicksInCombat();
    long getTicksOutOfCombat();

    void recogniseCombat();
    void tickCombat();
}