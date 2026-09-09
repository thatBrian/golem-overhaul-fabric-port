package tech.alexnijjar.golemoverhaul.common.recipes;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.jetbrains.annotations.NotNull;
import tech.alexnijjar.golemoverhaul.common.registry.ModRecipeSerializers;
import tech.alexnijjar.golemoverhaul.common.registry.ModRecipeTypes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public record GolemConstructionRecipe(
    List<String> pattern,
    Map<String, Either<ResourceKey<Block>, TagKey<Block>>> key,
    ResourceKey<EntityType<?>> entity,
    ResourceKey<Item> item,
    boolean visualOnly,
    float blockScale,
    float entityScale
) implements Recipe<SingleEntityInput> {

    public static final MapCodec<GolemConstructionRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            Codec.STRING.listOf().fieldOf("pattern").forGetter(GolemConstructionRecipe::pattern),
            Codec.unboundedMap(Codec.STRING, Codec.either(ResourceKey.codec(Registries.BLOCK), TagKey.hashedCodec(Registries.BLOCK))).fieldOf("key").forGetter(GolemConstructionRecipe::key),
            ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("entity").forGetter(GolemConstructionRecipe::entity),
            ResourceKey.codec(Registries.ITEM).fieldOf("item").forGetter(GolemConstructionRecipe::item),
            Codec.BOOL.optionalFieldOf("visualOnly", false).forGetter(GolemConstructionRecipe::visualOnly),
            Codec.FLOAT.optionalFieldOf("blockScale", 1f).forGetter(GolemConstructionRecipe::blockScale),
            Codec.FLOAT.optionalFieldOf("entityScale", 1f).forGetter(GolemConstructionRecipe::entityScale)
        ).apply(instance, GolemConstructionRecipe::new));

    // Recipes are only synced to clients for the recipe book, which never shows this type; the NBT-backed
    // stream codec is plenty and avoids hand-writing a 7-field byte codec.
    public static final StreamCodec<RegistryFriendlyByteBuf, GolemConstructionRecipe> STREAM_CODEC =
        ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    @Override
    public boolean matches(SingleEntityInput input, Level level) {
        Optional<ResourceKey<EntityType<?>>> key = BuiltInRegistries.ENTITY_TYPE.getResourceKey(input.entity());
        return key.isPresent() && key.get().equals(this.entity());
    }

    @Override
    public ItemStack assemble(SingleEntityInput input) {
        Item result = BuiltInRegistries.ITEM.getValue(this.item());
        return result == null ? ItemStack.EMPTY : result.getDefaultInstance();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleEntityInput>> getSerializer() {
        return ModRecipeSerializers.GOLEM_CONSTRUCTION.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<SingleEntityInput>> getType() {
        return ModRecipeTypes.GOLEM_CONSTRUCTION.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.GOLEM_CONSTRUCTION_CATEGORY.get();
    }

    public BlockPattern createPattern() {
        BlockPatternBuilder builder = BlockPatternBuilder.start();
        builder.aisle(this.pattern.toArray(new String[0]));
        this.key.forEach((k, v) -> {
            Predicate<BlockState> predicate = v.map(
                key -> BlockStatePredicate.forBlock(Objects.requireNonNull(BuiltInRegistries.BLOCK.getValue(key))),
                tagKey -> (Predicate<BlockState>) state -> state.is(tagKey));
            builder.where(k.charAt(0), BlockInWorld.hasState(predicate));
        });
        return builder.build();
    }
}
