package abyssal.alchemy;

import abyssal.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.function.Supplier;

public class AlchemyReagents {


    static List<AlchemyReagent> list;
    private static final Map<Item, AlchemyReagent> reagentMap = new HashMap<>();
    private static final Map<AlchemyReagent, Supplier<Item>> reagentToItem = new HashMap<>();

    public static AlchemyReagent getReagentForItem(Item item) {
        if(reagentMap.isEmpty()) {
            for(AlchemyReagent reagent : list) {
                reagentMap.put(reagentToItem.get(reagent).get(), reagent);
            }
        }
        return reagentMap.getOrDefault(item, null);
    }

    public static List<AlchemyReagent> makeReagents(Random rand) {
        reagentMap.clear();
        reagentToItem.clear();
        list = new ArrayList<>();

        // Declare reagents
        // Do NOT use .get on modded items, just give the reg obj
        reagent( "Redstone", Items.REDSTONE);
        reagent( "Glowstone", Items.GLOWSTONE_DUST);
        reagent( "Ghast tear", Items.GHAST_TEAR);
        reagent( "Sulfur", ModItems.SULFUR);
        reagent( "Nitre", ModItems.SALTPETRE);
        reagent( "Prismatic powder", ModItems.PRISMATIC_POWDER);
        reagent( "Charcoal", Items.CHARCOAL);
        reagent( "Blaze powder", Items.BLAZE_POWDER);
        reagent( "Creeper jelly", ModItems.CREEPER_JELLY);
        reagent( "Slime ball", Items.SLIME_BALL);
        // above this line: can be made through alchemy directly
        reagent( "Magma cream", Items.MAGMA_CREAM);
        // below this line: cannot be made through alchemy alone
        reagent( "Ink sac", Items.INK_SAC);
        reagent( "Glow ink", Items.GLOW_INK_SAC);
        reagent( "Sugar", Items.SUGAR );
        reagent( "Honeycomb", Items.HONEYCOMB);
        reagent( "Cocoa beans", Items.COCOA_BEANS);
        reagent( "Fermented spider eye", Items.FERMENTED_SPIDER_EYE);
        reagent( "Lichen", Items.GLOW_LICHEN);
        reagent( "Moss", Items.MOSS_BLOCK);
        reagent( "Nether wart", Items.NETHER_WART);
        reagent( "Red mushroom", Items.RED_MUSHROOM);
        reagent( "Brown mushroom", Items.BROWN_MUSHROOM);

        List<AlchemyReagentEffect> effects = generateEffectsList(list.size()*3, rand);

        // Allocate effects to reagents
        ArrayList<Integer> fixedEffectNums = new ArrayList<>(List.of(0,1,2));
        List<Integer> randEffectNums = new ArrayList<>(List.of(0,0,1,99,99)); //
        int i = 0;
        for(AlchemyReagent r : list) {
            Collections.shuffle(randEffectNums, rand);
            List<Integer> effectNums = new ArrayList<>(fixedEffectNums);
            effectNums.add(randEffectNums.get(0));
            effectNums.add(randEffectNums.get(1));
            Collections.shuffle(effectNums, rand);

            for (int j = 0; j < 5; j++) {
                int effectNum = effectNums.get(j);
                if(effectNum == 99) {
                    continue;
                }
                Alchemy.Category category = Alchemy.Category.values()[j];
                r.setEffect(effects.get(i + effectNum), category);
            }
            i += 3;
        }

        return list;
    }


    private static void reagent(String name, Item vanillaItem) {
        reagent(name, ()->vanillaItem);
    }
    private static void reagent(String name, Supplier<Item> item) {
        AlchemyReagent r = new AlchemyReagent(); // effects assigned later
        reagentToItem.put(r, item);
        list.add(r);
    }

    private static List<AlchemyReagentEffect> generateEffectsList(int size, Random r) {
        List<AlchemyReagentEffect> veryCommonEffects = new ArrayList<>();
        List<AlchemyReagentEffect> commonEffects = new ArrayList<>();
        List<AlchemyReagentEffect> uncommonEffects = new ArrayList<>();
        List<AlchemyReagentEffect> rareEffects = new ArrayList<>();
        List<AlchemyReagentEffect> legendaryEffects = new ArrayList<>();

        veryCommonEffects.add(new AlchemyReagentEffect(0,-1,0,0.95f, 1f));
        veryCommonEffects.add(new AlchemyReagentEffect(1,0,0,0.95f, 1f));
        veryCommonEffects.add(new AlchemyReagentEffect(-1,0,0,0.95f, 1f));

        commonEffects.add(new AlchemyReagentEffect(1,-1,0,0.95f, 1f));
        commonEffects.add(new AlchemyReagentEffect(-1,-1,0,0.95f, 1f));

        uncommonEffects.add(new AlchemyReagentEffect(0,1,0,0.95f, 1f));
        uncommonEffects.add(new AlchemyReagentEffect(1,1,0,0.95f, 1f));
        uncommonEffects.add(new AlchemyReagentEffect(-1,1,0,0.95f, 1f));

        rareEffects.add(new AlchemyReagentEffect(-2,0,0,0.95f, 1f));
        rareEffects.add(new AlchemyReagentEffect(2,0,0,0.95f, 1f));

        legendaryEffects.add(new AlchemyReagentEffect(0,2,0,0.95f, 1f));

        List<AlchemyReagentEffect> allEffects = new ArrayList<>();
        allEffects.addAll(veryCommonEffects);
        allEffects.addAll(veryCommonEffects);
        allEffects.addAll(veryCommonEffects);
        allEffects.addAll(veryCommonEffects);
        allEffects.addAll(veryCommonEffects);
        allEffects.addAll(commonEffects);
        allEffects.addAll(commonEffects);
        allEffects.addAll(commonEffects);
        allEffects.addAll(commonEffects);
        allEffects.addAll(uncommonEffects);
        allEffects.addAll(uncommonEffects);
        allEffects.addAll(uncommonEffects);
        allEffects.addAll(rareEffects);
        allEffects.addAll(rareEffects);
        allEffects.addAll(legendaryEffects);

        // 22 reagents = 66 effects

        List<AlchemyReagentEffect> effects = new ArrayList<>(allEffects);
        //effects.addAll(allEffects); // min 2 instances of each effect
        while(effects.size() < size) {
            effects.add(allEffects.get(r.nextInt(allEffects.size())));
        }
        Collections.shuffle(effects, r);
        return effects;
    }
}
