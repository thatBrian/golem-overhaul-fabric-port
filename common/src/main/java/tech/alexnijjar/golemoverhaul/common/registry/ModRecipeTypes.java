package tech.alexnijjar.golemoverhaul.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeType;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.common.recipes.GolemConstructionRecipe;

public class ModRecipeTypes {

    public static final ResourcefulRegistry<RecipeType<?>> RECIPE_TYPES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_TYPE, GolemOverhaul.MOD_ID);
    // Every recipe needs a recipe-book category since 1.21.2, even ones the book never shows.
    public static final ResourcefulRegistry<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_BOOK_CATEGORY, GolemOverhaul.MOD_ID);

    public static final RegistryEntry<RecipeType<GolemConstructionRecipe>> GOLEM_CONSTRUCTION = register("golem_construction");
    public static final RegistryEntry<RecipeBookCategory> GOLEM_CONSTRUCTION_CATEGORY = RECIPE_BOOK_CATEGORIES.register("golem_construction", RecipeBookCategory::new);

    private static <T extends Recipe<?>> RegistryEntry<RecipeType<T>> register(String id) {
        return RECIPE_TYPES.register(id, () -> new RecipeType<>() {
            public String toString() {
                return id;
            }
        });
    }
}
