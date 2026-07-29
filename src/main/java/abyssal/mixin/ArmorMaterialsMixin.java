package abyssal.mixin;

import net.minecraft.world.item.equipment.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/*
            b l c h  T  d       b l c h  T  d
Cloth                           1 2 3 1  7  5
Leather     1 2 3 1  7  5       2 2 4 1  9  8
Copper      1 3 4 2 10 11       1 3 4 2 10 11
Gold        1 3 5 2 11  7       1 3 5 2 11  7
Silver                          2 3 5 2 12 10
Chain       1 4 5 2 12 15       2 3 6 3 14 15
Iron        2 5 6 2 15 15       2 4 7 4 17 20
Diamond     3 6 8 3 20 33       3 5 8 4 20 33
Netherite   3 6 8 3 20 37       3 5 8 4 20 37
Turtle            2                   3
 */

@Mixin(ArmorMaterials.class)
public interface ArmorMaterialsMixin {
    @ModifyArgs(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/equipment/ArmorMaterials;makeDefense(IIIII)Ljava/util/Map;",
            ordinal = 0
    ))
    private static void abyssal$modifyLeatherDefence(Args args) {
        args.set(0, 2); // boots
        args.set(1, 2); // leggings
        args.set(2, 4); // chestplate
        args.set(3, 1); // helmet
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/equipment/ArmorMaterial;<init>(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)V",
                    ordinal = 0
            ),
            index = 0,
            require = 1
    )
    private static int abyssal$modifyLeatherDurabilityMultiplier(int original) {
        return 8; // was 5
    }

    // ORDINAL 1: COPPER
    // unmodified

    @ModifyArgs(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/equipment/ArmorMaterials;makeDefense(IIIII)Ljava/util/Map;",
            ordinal = 2
    ))
    private static void abyssal$modifyChainmailDefence(Args args) {
        args.set(0, 2); // boots
        args.set(1, 3); // leggings
        args.set(2, 6); // chestplate
        args.set(3, 3); // helmet
    }

    @ModifyArgs(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/equipment/ArmorMaterials;makeDefense(IIIII)Ljava/util/Map;",
            ordinal = 3
    ))
    private static void abyssal$modifyIronDefence(Args args) {
        args.set(0, 2); // boots
        args.set(1, 4); // leggings
        args.set(2, 7); // chestplate
        args.set(3, 4); // helmet
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/equipment/ArmorMaterial;<init>(ILjava/util/Map;ILnet/minecraft/core/Holder;FFLnet/minecraft/tags/TagKey;Lnet/minecraft/resources/ResourceKey;)V",
                    ordinal = 3
            ),
            index = 0,
            require = 1
    )
    private static int abyssal$modifyIronDurabilityMultiplier(int original) {
        return 20; // was 15
    }

    @ModifyArgs(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/equipment/ArmorMaterials;makeDefense(IIIII)Ljava/util/Map;",
            ordinal = 4
    ))
    private static void abyssal$modifyGoldDefence(Args args) {
        args.set(0, 3); // boots
        args.set(1, 5); // leggings
        args.set(2, 8); // chestplate
        args.set(3, 4); // helmet
    }

    @ModifyArgs(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/equipment/ArmorMaterials;makeDefense(IIIII)Ljava/util/Map;",
            ordinal = 5
    ))
    private static void abyssal$modifyDiamondDefence(Args args) {
        args.set(0, 3); // boots
        args.set(1, 5); // leggings
        args.set(2, 8); // chestplate
        args.set(3, 4); // helmet
    }

    @ModifyArgs(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/equipment/ArmorMaterials;makeDefense(IIIII)Ljava/util/Map;",
            ordinal = 6
    ))
    private static void abyssal$modifyTurtleScuteDefence(Args args) {
        args.set(3, 3); // helmet
    }

    @ModifyArgs(method = "<clinit>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/equipment/ArmorMaterials;makeDefense(IIIII)Ljava/util/Map;",
            ordinal = 7
    ))
    private static void abyssal$modifyNetheriteDefence(Args args) {
        args.set(0, 3); // boots
        args.set(1, 5); // leggings
        args.set(2, 8); // chestplate
        args.set(3, 4); // helmet
    }
}