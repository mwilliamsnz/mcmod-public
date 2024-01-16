package abyssal.alchemy;

import java.util.List;
import java.util.Set;

public class AlchemyMaterialGroup {

//    private final int cost;
    private final Tier tier;
    private final Alchemy.Category category;

    public final String name;

    private final List<AlchemyMaterial> materials;

    public Iterable<AlchemyMaterial> deferredGetInputs() {
        return materials;
    }

    public Tier getTier() {
        return tier;
    }

    public Alchemy.Category getCategory() {
        return category;
    }

    public AlchemyMaterialGroup(String name, Tier tier, Alchemy.Category category, Set<AlchemyMaterial> materials) {
        this.name = name;
        this.tier = tier;
        this.category = category;
        this.materials = materials.stream().toList();
    }

    public AlchemyMaterial bestUnderConditions(double quantity, double purity) {
        double bestP = 0;
        double bestQforP = 0;
        AlchemyMaterial bestM = null;
        for(AlchemyMaterial m : materials) {
            double p = m.requiredPurity();
            double q = m.cost();
            if (q > quantity || p > purity) {
                continue;
            }
            if (p > bestP || (p == bestP && q > bestQforP)) {
                bestM = m;
                bestP = p;
                bestQforP = q;
            }
        }
        return bestM;
    }

    public static class Tier {

        final int base;
        final int maxAbove;
        final int maxBelow;

        public Tier(int base) {
            this(base, 0, 0);
        }

        public Tier(int base, int maxAbove, int maxBelow) {
            this.base = base;
            this.maxAbove = maxAbove;
            this.maxBelow = maxBelow;
        }

    }


}
