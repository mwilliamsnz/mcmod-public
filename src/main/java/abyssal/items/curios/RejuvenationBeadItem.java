package abyssal.items.curios;

import top.theillusivec4.curios.api.SlotContext;

public class RejuvenationBeadItem extends ModCurioItem {

    private final float rate;

    public RejuvenationBeadItem(Properties props, float rate) {
        super(props);
        this.rate = rate;
    }

    @Override
    public void tickCurio(SlotContext ctx) {
        ctx.entity().heal(rate);
    }
    // Single bead:
    // 0.004/t = 0.080/s = 0.1 per 25t = 1 per 12.5s
    // 0.1/s = 1/2 heart per 10s = full heal 200s
    // Full set
    // 0.9/s = 1/2 heart per 1.1s = full heal 22s

}
