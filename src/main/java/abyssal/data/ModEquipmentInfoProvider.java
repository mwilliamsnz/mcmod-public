package abyssal.data;

import abyssal.Main;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModEquipmentInfoProvider implements DataProvider {
    private final PackOutput.PathProvider path;

    public ModEquipmentInfoProvider(PackOutput output) {
        this.path = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    private void add(BiConsumer<ResourceLocation, EquipmentClientInfo> registrar) {
        registrar.accept(
                // Must match Equippable#assetId
                Main.rl("silver"),
                EquipmentClientInfo.builder()
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID, new EquipmentClientInfo.Layer(
                                        // Points to assets/<namespace>/textures/entity/equipment/humanoid/<path>.png
                                        Main.rl("silver"), Optional.empty(), false
                                )
                        )
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, new EquipmentClientInfo.Layer(
                                        // Points to assets/<namespace>/textures/entity/equipment/humanoid_leggings/<path>.png
                                        Main.rl("silver"), Optional.empty(), false
                                )
                        )
                        .build()
        );
        registrar.accept(
                Main.rl("cloth"),
                EquipmentClientInfo.builder()
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID,
                                // Base texture
                                new EquipmentClientInfo.Layer(
                                        Main.rl("cloth_base"), Optional.empty(), false
                                ),
                                // Overlay texture
                                new EquipmentClientInfo.Layer(
                                        Main.rl("cloth_overlay"),
                                        Optional.of(new EquipmentClientInfo.Dyeable(Optional.of(0xCCCCAA))), false
                                )
                        )
                        // For humanoid legs
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS,
                                new EquipmentClientInfo.Layer(
                                        Main.rl("cloth_base"), Optional.empty(), false
                                ),
                                new EquipmentClientInfo.Layer(
                                        Main.rl("cloth_overlay"),
                                        Optional.of(new EquipmentClientInfo.Dyeable(Optional.of(0xCCCCAA))), false
                                )
                        )
                        .build()
        );
        registrar.accept(
                // Must match Equippable#assetId
                Main.rl("warmogs"),
                EquipmentClientInfo.builder()
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID, new EquipmentClientInfo.Layer(
                                        // Points to assets/<namespace>/textures/entity/equipment/humanoid/<path>.png
                                        Main.rl("warmogs"), Optional.empty(), false
                                )
                        )
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, new EquipmentClientInfo.Layer(
                                        // Points to assets/<namespace>/textures/entity/equipment/humanoid_leggings/<path>.png
                                        Main.rl("warmogs"), Optional.empty(), false
                                )
                        )
                        .build()
        );
        registrar.accept(
                // Must match Equippable#assetId
                Main.rl("mr_items"),
                EquipmentClientInfo.builder()
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID, new EquipmentClientInfo.Layer(
                                        // Points to assets/<namespace>/textures/entity/equipment/humanoid/<path>.png
                                        Main.rl("mr_items"), Optional.empty(), false
                                )
                        )
                        .addLayers(
                                EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, new EquipmentClientInfo.Layer(
                                        // Points to assets/<namespace>/textures/entity/equipment/humanoid_leggings/<path>.png
                                        Main.rl("mr_items"), Optional.empty(), false
                                )
                        )
                        .build()
        );
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceLocation, EquipmentClientInfo> map = new HashMap<>();
        this.add((name, info) -> {
            if (map.putIfAbsent(name, info) != null) {
                throw new IllegalStateException("Tried to register equipment client info twice for id: " + name);
            }
        });
        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.path, map);
    }

    @Override
    public String getName() {
        return "Equipment Client Infos: " + Main.MOD_ID;
    }
}
