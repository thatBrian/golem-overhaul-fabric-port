package tech.alexnijjar.golemoverhaul.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.common.recipes.GolemConstructionRecipe;

public class ModRecipeSerializers {

    public static final ResourcefulRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_SERIALIZER, GolemOverhaul.MOD_ID);

    // RecipeSerializer became a plain record of (MapCodec, StreamCodec) in 1.21.2; Resourceful Lib's CodecRecipeSerializer is gone.
    public static final RegistryEntry<RecipeSerializer<GolemConstructionRecipe>> GOLEM_CONSTRUCTION = RECIPE_SERIALIZERS.register("golem_construction", () ->
        new RecipeSerializer<>(GolemConstructionRecipe.CODEC, GolemConstructionRecipe.STREAM_CODEC));
}
