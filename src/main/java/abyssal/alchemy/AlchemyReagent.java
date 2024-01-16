package abyssal.alchemy;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class AlchemyReagent {

    private final Map<Alchemy.Category, AlchemyReagentEffect> map = new HashMap<>();

    public void setEffect(AlchemyReagentEffect e, Alchemy.Category c) {
        map.put(c,e);
    }

    @Nonnull
    public AlchemyReagentEffect getEffect(int temperature, Alchemy.Category c) {
        if(!map.containsKey(c)) {
            return NOOP;
        }
        return map.get(c);
    }

    private final AlchemyReagentEffect NOOP = new AlchemyReagentEffect(0,0,0,1, 1);

}
