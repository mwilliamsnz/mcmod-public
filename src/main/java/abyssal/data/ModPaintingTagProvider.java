package abyssal.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PaintingVariantTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModPaintingTagProvider extends PaintingVariantTagsProvider {

    public ModPaintingTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
//        ModPaintings.PLACEABLE_PAINTINGS.forEach(rl -> this.tag(PaintingVariantTags.PLACEABLE).add(TagEntry.element(rl)));
    }
}
