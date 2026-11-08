package com.refinedmods.refinedstorage.jei.common;

import com.refinedmods.refinedstorage.common.grid.AbstractCraftingGridContainerMenu;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import org.jspecify.annotations.Nullable;

/**
 * Quartz Arsenal has no Minecraft 26.3 build, so it is looked up by reflection instead of being a compile-time
 * dependency. If a 26.3 build is installed, its Wireless Crafting Grid still gets JEI recipe transfer.
 */
public final class QuartzArsenalIntegration {
    private static final String WIRELESS_CRAFTING_GRID_MENU =
        "com.refinedmods.refinedstorage.quartzarsenal.common.wirelesscraftinggrid.WirelessCraftingGridContainerMenu";

    private QuartzArsenalIntegration() {
    }

    public static boolean isLoaded() {
        return findMenuClass() != null;
    }

    public static void load(final IRecipeTransferRegistration registration) {
        final Class<? extends AbstractCraftingGridContainerMenu> menuClass = findMenuClass();
        if (menuClass != null) {
            registration.addRecipeTransferHandler(new CraftingGridRecipeTransferHandler<>(menuClass),
                RecipeTypes.CRAFTING);
        }
    }

    private static @Nullable Class<? extends AbstractCraftingGridContainerMenu> findMenuClass() {
        try {
            return Class.forName(WIRELESS_CRAFTING_GRID_MENU).asSubclass(AbstractCraftingGridContainerMenu.class);
        } catch (final ClassNotFoundException | ClassCastException e) {
            return null;
        }
    }
}
