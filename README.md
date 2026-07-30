
## Gameplay

Gameplay, recipes, etc. are documented on [the wiki](https://isfmods.miraheze.org/) (private access)

## Version and Dependencies

The mod currently targets:

- Minecraft `26.1.2`
- NeoForge `26.1.2.93`
- Curios `15.0.0+26.1.2`

The mod also has an optional dependency on `callofthecorn-26.1.2-1.*`, which is not
publicly distributed. The "Haunted Temperate" (`temperate_compat`) world preset
requires that mod, but it is not required by anything else. The build script checks
for the dependency so does not need adjustment in either case.

## Building

Use Neoforge's gradle wrapper to run the
following tasks:
```shell
./gradlew runClientData
./gradlew build
```
or on Windows:
```shell
.\gradlew.bat runClientData
.\gradlew.bat build
```

`runClientData` performs data generation, necessary once for a clean clone and again
whenever the generated JSON must change (changes to recipes, tags, models, etc.)
before building or running.
`build` builds the mod jar and writes it to `build/libs`. Use this when running
the mod from an external launcher.

The `runClient` task can be used to launch the game in the development environment
without needing to build the jar first.

## Compatibility

Technically, this mod should be broadly compatible with most other neoforge mods.
This mod is rather opinionated, so from a gameplay perspective, it may not pair
well with certain other mods that have overlapping goals.

Particularly, this mod intentionally overrides the following vanilla objects:

- These block loot tables: `minecraft:blocks/deepslate_diamond_ore`, `minecraft:blocks/diamond_ore`, `minecraft:blocks/nether_gold_ore`, `minecraft:blocks/spawner`, 
- These structure loot tables (these are intentionally overrides and not additive modifiers): `minecraft:chests/abandoned_mineshaft`, `minecraft:chests/buried_treasure`, `minecraft:chests/desert_pyramid`, `minecraft:chests/jungle_temple`, `minecraft:chests/nether_bridge`, `minecraft:chests/ruined_portal`, `minecraft:chests/shipwreck_supply`, `minecraft:chests/shipwreck_treasure`, `minecraft:chests/simple_dungeon`, 
- These entity loot tables: `minecraft:entities/bat`, `minecraft:entities/blaze`, `minecraft:entities/cow`, `minecraft:entities/creeper`, `minecraft:entities/pig`, `minecraft:entities/zombified_piglin`,
- These recipes: `minecraft:diamond_boots`, `minecraft:diamond_chestplate`, `minecraft:diamond_from_blasting_deepslate_diamond_ore`, `minecraft:diamond_from_blasting_diamond_ore`, `minecraft:diamond_from_smelting_deepslate_diamond_ore`, `minecraft:diamond_from_smelting_diamond_ore`, `minecraft:diamond_helmet`, `minecraft:diamond_leggings`, `minecraft:iron_ingot_from_smelting_deepslate_iron_ore`, `minecraft:iron_ingot_from_smelting_iron_ore`, `minecraft:iron_ingot_from_smelting_raw_iron`, `minecraft:jukebox`, `minecraft:netherite_ingot`, `minecraft:powered_rail`, `minecraft:tnt`
- These noise functions: `minecraft:overworld/factor`, `minecraft:overworld/jaggedness`, `minecraft:overworld/offset`,
- The ruined portal structure NBT files: `minecraft:ruined_portal/giant_portal_[1-10]`.

This may cause load-order dependencies with mods that make similar overrides.

## Architecture & Integration

### Ore Distribution

The mod reduces the generation rate of most vanilla ores and supplements them with a 
regionally-varying chunk-based generator, that generates large quantities of ore but
only in isolated clusters of chunks.

At present, there is no way for other mods to associate their `PlacedFeature`s with the
chunk-based distribution. The standard vanilla chunk-insensitive generation still runs
so other modded ores are unaffected. Adding more ore chunk types in the source of this
mod is also very simple.

### Spells

Casting spells requires spell fuel. Fuel is represented as a `SpellFuelQuantity` record,
made up of an integer quantity and a `SpellFuelType`. Mods can add fuel types with 
`SpellFuelTypes#createSpellFuelType(String id, ChatFormatting colour)`. Spell fuel can
be stored on any item (either storing a single type, or a universal "colourless" type)
with an attached `SpellBatteryComponent` (id `"abyssal:spell_fuel_store"`) using the 
vanilla data component system.

`SpellBatteryComponent` items can be refueled by items with the `SpellRefuelComponent`
(`"abyssal:spell_fuel_store"`) and a `RestoreFuelConsumeEffect`. The same consume 
effect can be used for any item as  it reads the component to determine the 
`SpellFuelQuantity` refueled; the consume  effect gives it right-click behaviour and 
refuels, the component only gives the quantity. You can also directly refuel a player's 
`SpellBatteryComponent` items by calling `quantity.topUp(player)` on a `FuelQuantity`.

Spells are assigned to spellbook items (or any other item, in principle) with the
`SpellComponent` (`"abyssal:spellbook"`). This holds the `Identifier` for the spell,
and optionally another `Identifier` for the alternate cast.

Spells can be cast by holding any `ItemStack` whose `Item` extends `SpellStaff`, or by
calling `spell.cast(level, player, staff, book, ap)` in some other way, where `staff`
is the spellcasting focus `ItemStack`, `book` is the `ItemStack` which is the source of
the spell (though it need not actually have a `SpellComponent`) and `ap` is the 
applicable ability power modifier, usually from the player's attribute.

Spell casting logic lives in `Spell`, by extending this class to implement the `cast`
method and optionally the secondary `altBookCast`.

#### Adding Spells
Other mods can add spells through the public spell map.
This is an experimental API. It is not yet a NeoForge registry,
has no registration event, and may change between releases.
A minimal spell looks like this:
```java
public final class ExampleSpells {
    public static final Spell REVEAL = Spells.createSpell(
            new Spell(
                    Identifier.fromNamespaceAndPath("examplemod", "reveal"),
                    new SpellFuelQuantity(SpellFuelTypes.FUEL_LIGHT, 10)
            ) {
                @Override
                public InteractionResult cast(Level level, Player player,
                        ItemStack staff, ItemStack book, double abilityPower) {
                    if (!level.isClientSide()) {
                        // Perform authoritative world/entity changes.
                    }

                    return InteractionResult.SUCCESS;
                }
            }
    );

    public static void init() {
        // Calling this method forces class initialization and registration.
    }
}
```

Call `ExampleSpells.init()` during common mod initialisation on both physical
sides. Registration must occur before items containing that spell are decoded
or displayed.

The spell tooltip can be localised with the language key `spell.mod_id.spell_id`.

### Tag Configuration
The tag `abyssal:coin_purse_items` controls which items can fit in the coin purse.
`abyssal:charring_axe_destroys` controls which blocks are destroyed by the Charring
Axe as it burns logs (e.g. vines touching the logs).
`abyssal:shelf_amplifiers` controls which items amplify enchanting output when
placed in a Chiseled Bookshelf.

The mod's curios all use the default Curios tags `curios:necklace`, `curios:ring`,`curios:belt`, `curios:charm`,
so the slots will be shared with any other mods using any of those default slots types.

These tags can all be assigned in a datapack for modpack compatibility.

### Attributes and Related Systems

The mod adds the following attributes, which can, like vanilla attributes, easily be
used in commands or by other mods:

- **Magic Resistance** `"abyssal.magic_resist"` Default 0. Multiplies all "magical" sources of damage by a factor of `100/(100 + MR)`.
- **Ability Power** `"abyssal.ability_power"` Default 0. Spells and some items use this to scale their effects in ad hoc ways. 
- **Tenacity** `"abyssal.debuff_duration"` Default 1. Divisor on duration for all incoming harmful debuffs. That is, a modifier of +20% *reduces* debuff duration, dividing by 1.2.
- **Heal Rate** `"abyssal.healing_rate"` Default 1. Multiplier on all incoming healing, including natural regeneration.
- **Regeneration per 5s** `"abyssal.regen"` Default 0. The amount of health regenerated over every 5 second period (incrementally, each tick) without regard to hunger. 

The systems (mostly the event handlers) will pick up on these attributes regardless of
which items they are assigned to.

The mod adds the following potion effects, also usable like vanilla effects:

- Wounded `"abyssal:wounded"` reduces heal rate by 20% each rank.


The mod adds the following entity data attachments which are automatically attached
to all players:

- `"abyssal:no_combat_time"` Ticks since taking or dealing damage.
- `"abyssal:combat_time"` Ticks since spent without failing to take or deal damage for a continuous 100-tick period.
That is, the timer starts when you take or deal damage, and resets to and remains at zero when 5 seconds have passed with no damage.



### Other Utilities
The shift-to-expand tooltip system used by the added items and by spell data
components can be easily reused by other mods. 

Simply add the `abyssal:desc` component to the item:
```java
public static Item.Properties descProperties(Identifier id) {
    return new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, id))
            .component(ModDataComponents.DESC, new DescComponent(id));
}
```
Then add localisation entries into your lang files:
```yaml
  "ttdesc.examplemod.identifer.1": "The first line of text.",
  "ttdesc.examplemod.identifer.2": "A second paragraph.",
```
Minecraft's localisation files don't support `\n` newlines, but you can add
line breaks in the manner shown above. You can have arbitrarily many paragraphs
by increasing the suffix number. Note that if localising for another language,
if you have fewer paragraphs than the fallback language (e.g. `en_us`) then it
will take the missing paragraph from the fallback, which may sometimes be
undesirable. Formatting can be done with `§`-codes.

## Licence
See `LICENSE.txt`.