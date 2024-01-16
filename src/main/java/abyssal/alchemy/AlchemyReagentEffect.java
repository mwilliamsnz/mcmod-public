package abyssal.alchemy;

public class AlchemyReagentEffect {
    final short dx;
    final short dy;
    final short dt;
    final float interEfficiency;
    final float intraEfficiency;

    public AlchemyReagentEffect(int dx, int dy, int dt, float interEfficiency, float intraEfficiency) {
        this.dx = (short) dx;
        this.dy = (short) dy;
        this.dt = (short) dt;
        this.interEfficiency = interEfficiency;
        this.intraEfficiency = intraEfficiency;
    }
}