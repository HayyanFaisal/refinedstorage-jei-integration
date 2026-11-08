# Unofficial port: Refined Storage - JEI Integration 2.0.1 for Minecraft 26.3 (Fabric)

This branch is an **unofficial** community port of Refined Storage - JEI Integration **v2.0.1** (Fabric) from
Minecraft **26.1.2** to Minecraft **26.3**. It is not made or supported by Refined Mods. Please don't report bugs from
this port to the upstream issue tracker.

- Base: upstream `v2.0.1` (the `develop` branch at the time has the same code)
- Target: Minecraft 26.3, JEI 31.17.0.108, Fabric Loader 0.20.1, Fabric API 0.165.2+26.3, Java 25
- Needs the unofficial Refined Storage 26.3 port:
  [HayyanFaisal/refinedstorage2, branch `mc-26.3`](https://github.com/HayyanFaisal/refinedstorage2/tree/mc-26.3)
  ([release v3.2.1-mc26.3](https://github.com/HayyanFaisal/refinedstorage2/releases/tag/v3.2.1-mc26.3))
- Only the **Fabric** module was ported. The NeoForge module is excluded from the build.

## AI assistance

This port was made with AI assistance. The code changes, API research and build setup were done with
**Claude Code** (Anthropic's AI coding assistant), working from the official open-source code (not decompiled
mod jars). Hayyan directed the work and tested it in game. Each JEI hook the add-on uses was checked against the
JEI 26.3 sources to make sure JEI still calls it.

## What changed

| Area | 26.1.2 | 26.3 | What the port does |
|---|---|---|---|
| Tooltips | `GuiGraphicsExtractor.tooltip(..., style)` | extra `boolean extraSpaceAfterFirstLine` | Passes `false` (Crafting Grid and Pattern Grid transfer error tooltips) |
| Current screen | `Minecraft.screen` | `minecraft.gui.screen()` | Used to find the parent screen when opening the autocrafting preview |
| Quartz Arsenal | compile-time dependency | no 26.3 build exists | The Wireless Crafting Grid menu is looked up by reflection. If a 26.3 build of Quartz Arsenal is installed, its recipe transfer still works |

Checked and left unchanged:

- JEI 30.30 deprecated `IRecipeTransferHandler.transferRecipe(container, recipe, slots, player, max, doTransfer)` in
  favour of `transferRecipe(IRecipeTransferContext, boolean)`. The new default method calls the old one, so the
  existing handlers still run. The only difference is that JEI's transfer listeners don't get a completion
  callback, which is the same as upstream on 26.1.2.
- `IGuiContainerHandler.getClickableIngredientUnderMouse` already uses the current (non-deprecated) overload.
- The add-on has no raw key-code or mouse-button checks. Ctrl/Cmd-click to autocraft goes through Refined Storage's
  `ClientPlatformUtil`, which the Refined Storage port already moved to SDL3 key codes.

### Build changes

- `fabric.mod.json`: `"minecraft": "~26.3"`, `"refinedstorage": ">=3.2.1"`, `"jei": ">=31.17.0.108"`.
- `gradle.properties`: MC 26.3, JEI 31.17.0.108, Refined Storage `3.2.1-mc263`, Refined Architect `1.6.4-mc263`.
- Gradle wrapper 9.4.0 → 9.9.0 (Fabric Loom 1.19 needs Gradle 9.8 or newer).
- `mavenLocal()` added, because the ported Refined Storage and the patched Refined Architect plugin aren't on a
  public Maven repository. The NeoForge module and the Quartz Arsenal dependency are removed.

## Building

Requires JDK 25.

```bash
# 1. Build the patched Refined Architect plugin and the Refined Storage port into your local Maven repo
#    (see PORTING-26.3.md in HayyanFaisal/refinedstorage2, branch mc-26.3), then:
cd refinedstorage2
RELEASE_VERSION=3.2.1-mc263 ./gradlew publishToMavenLocal -x checkstyleMain -x test -x javadoc

# 2. Build the add-on
cd ../refinedstorage-jei-integration
RELEASE_VERSION=2.0.1+mc26.3-port ./gradlew :refinedstorage-jei-integration-fabric:jar -x checkstyleMain -x test
# -> refinedstorage-jei-integration-fabric/build/libs/refinedstorage-jei-integration-fabric-2.0.1+mc26.3-port.jar
```

## Installing

Put the jar in the client's `mods` folder together with JEI and the Refined Storage 26.3 port. The add-on only does
client-side work (recipe transfers go through Refined Storage's own network packets), so a server doesn't need it.

## Known limitations

- Tested in game on one Fabric 26.3 modpack: Crafting Grid recipe transfer from JEI works.
- The NeoForge version is not ported.
- Unit tests, checkstyle and PMD were not run against 26.3.

## License

Refined Storage - JEI Integration is MIT licensed (see [LICENSE.md](LICENSE.md)), © Refined Mods. This port keeps the
same license.
