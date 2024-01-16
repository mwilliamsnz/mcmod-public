package abyssal.capability;

import net.minecraft.nbt.CompoundTag;

public class CombatTimeCapabilityImplementation implements CombatTimeCapabilityInterface {

    private static final String NBT_KEY_TIME_OUT_OF_COMBAT = "timeOutOfCombat";
    private static final String NBT_KEY_TIME_IN_COMBAT = "timeInCombat";

    private long ticksInCombat = 0;
    private long ticksOutOfCombat = 0;

    private static final long TICKS_BEFORE_LEAVING_COMBAT = 100;

    @Override
    public long getTicksInCombat() {
        return this.ticksInCombat;
    }

    @Override
    public long getTicksOutOfCombat() {
        return this.ticksOutOfCombat;
    }

    @Override
    public void recogniseCombat() {
        this.ticksOutOfCombat = 0;
        if(ticksInCombat == 0) {
            ticksInCombat = 1;
        }
    }

    @Override
    public void tickCombat() {
        this.ticksOutOfCombat++;
        if(ticksOutOfCombat > TICKS_BEFORE_LEAVING_COMBAT) {
            this.ticksInCombat = 0;
        }
        if(ticksInCombat > 0) {
            this.ticksInCombat++;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putLong(NBT_KEY_TIME_OUT_OF_COMBAT, this.ticksOutOfCombat);
        tag.putLong(NBT_KEY_TIME_IN_COMBAT, this.ticksInCombat);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.ticksInCombat = nbt.getLong(NBT_KEY_TIME_IN_COMBAT);
        this.ticksOutOfCombat = nbt.getLong(NBT_KEY_TIME_OUT_OF_COMBAT);
    }
}