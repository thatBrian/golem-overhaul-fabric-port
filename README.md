# Golem Overhaul — unofficial Fabric port for Minecraft 26.1.x

> **This is an unofficial port.** Golem Overhaul is made by **Bonsai Studios** — Alex Nijjar, Joosh and 3xpl01t.
> The original mod, its art, models, animations, sounds and design are theirs; this repository only carries the
> changes needed to run it on Fabric for Minecraft 26.1.x. Thank you to Bonsai Studios for making it and for
> keeping the source public.
>
> - Original source: <https://github.com/bonsaistudi0s/Golem-Overhaul> (`1.21.1/main`, release 1.1.1)
> - Original on Modrinth: <https://modrinth.com/mod/golem-overhaul>
> - Original on CurseForge: <https://www.curseforge.com/minecraft/mc-mods/golem-overhaul>
>
> Please do not report problems with this build to Bonsai Studios — open an issue here instead.

Golem Overhaul adds 9 new golem types, all with their own unique mechanics.

## Requirements

| Dependency | Version |
|---|---|
| Minecraft | 26.1.2 (works on 26.1.x) |
| Fabric Loader | ≥ 0.19.2 |
| Fabric API | ≥ 0.155.3 |
| Java | ≥ 25 |
| [GeckoLib](https://modrinth.com/mod/geckolib) | ≥ 5.5.2 |
| [Architectury API](https://modrinth.com/mod/architectury-api) | ≥ 20.0.12 |
| [Resourceful Lib](https://modrinth.com/mod/resourceful-lib) | ≥ 4.0.1 |
| [Resourceful Config](https://modrinth.com/mod/resourceful-config) | ≥ 4.0.1 |

The mod is needed on both client and server.

## What changed in the port

The upstream code targets Minecraft 1.21.1 with GeckoLib 4, Architectury 13 and Resourceful Lib 3. Getting to 26.1.2
meant:

- **Build:** the Architectury multiloader layout (`common/` + `fabric/`) is kept so diffs against upstream stay
  readable, but it is compiled as a single Fabric Loom module; the NeoForge module is dropped. Minecraft 26.x ships
  unobfuscated, so there are no mappings.
- **GeckoLib 4 → 5:** every package moved (`software.bernie.geckolib` → `com.geckolib`) and rendering is driven by
  immutable render states. Golem state (crackiness, lit, charged, gilded, hay colour, …) is captured into data tickets
  during state extraction; head tracking, bone hiding and the health-scaled slime body are done through bone snapshots.
- **Resourceful Lib 3 → 4:** `CodecRecipe`/`CodecRecipeSerializer` are gone, so the golem-construction recipe now
  implements vanilla `Recipe` directly with a `RecipeSerializer` record and a recipe-book category.
- **Minecraft 1.21.1 → 26.1.2:** `ResourceLocation` → `Identifier`, `MobSpawnType` → `EntitySpawnReason`, NBT
  save/load through `ValueOutput`/`ValueInput`, `hurt` → `hurtServer`, block `render_type` inferred from textures,
  item-definition JSONs, spawn eggs with their own textures (vanilla removed the tinted template), key-mapping
  categories as registered objects, and the usual signature churn.
- **Removed:** JEI/REI recipe-viewer integration (recipes are server-only since 1.21.2 and neither viewer is in the
  target pack). Everything else is intended to behave as upstream.

Spawn-egg textures are simple placeholders drawn for this port; they are not Bonsai Studios' art.

## Building

```sh
./gradlew build
```

Needs JDK 25. The jar lands in `build/libs/`.

## Versioning

`<upstream version>-<minecraft>-<port revision>-fabric-unofficial`, e.g. `1.1.1-26.1.x-0.1.0-fabric-unofficial`.

## License

Upstream publishes no license file (Modrinth lists it as All Rights Reserved); all rights to the original work remain
with Bonsai Studios. The porting changes in this repository are offered on the same basis — as an unofficial community
port, not a relicensing. If Bonsai Studios would like this repository changed or removed, open an issue and it will be
done.
