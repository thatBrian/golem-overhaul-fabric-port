package tech.alexnijjar.golemoverhaul.common.registry;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeModeTab;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.teamresourceful.resourcefullib.common.registry.builtin.ResourcefulItemRegistry;
import com.teamresourceful.resourcefullib.common.registry.builtin.base.ItemLikeEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.common.constants.ConstantComponents;
import tech.alexnijjar.golemoverhaul.common.items.CoalGolemItem;
import tech.alexnijjar.golemoverhaul.common.items.HoneyBlobItem;
import tech.alexnijjar.golemoverhaul.common.items.TooltipBlockItem;

@SuppressWarnings("unused")
public class ModItems {

    // ResourcefulItemRegistry stamps the registry key onto Item.Properties (required since 1.21.2).
    public static final ResourcefulItemRegistry ITEMS = ResourcefulRegistries.createForItems(GolemOverhaul.MOD_ID);
    public static final ResourcefulRegistry<CreativeModeTab> TABS = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, GolemOverhaul.MOD_ID);
    public static final RegistryEntry<CreativeModeTab> TAB = TABS.register("main", () -> new ResourcefulCreativeModeTab(Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "main"))
        .setItemIcon(() -> ModItems.CLAY_GOLEM_STATUE.get())
        .addRegistry(ITEMS)
        .build());

    public static final ItemLikeEntry<SpawnEggItem> BARREL_GOLEM_SPAWN_EGG = spawnEgg("barrel_golem_spawn_egg", ModEntityTypes.BARREL_GOLEM);
    public static final ItemLikeEntry<SpawnEggItem> CANDLE_GOLEM_SPAWN_EGG = spawnEgg("candle_golem_spawn_egg", ModEntityTypes.CANDLE_GOLEM);
    public static final ItemLikeEntry<SpawnEggItem> COAL_GOLEM_SPAWN_EGG = spawnEgg("coal_golem_spawn_egg", ModEntityTypes.COAL_GOLEM);
    public static final ItemLikeEntry<SpawnEggItem> HAY_GOLEM_SPAWN_EGG = spawnEgg("hay_golem_spawn_egg", ModEntityTypes.HAY_GOLEM);
    public static final ItemLikeEntry<SpawnEggItem> HONEY_GOLEM_SPAWN_EGG = spawnEgg("honey_golem_spawn_egg", ModEntityTypes.HONEY_GOLEM);
    public static final ItemLikeEntry<SpawnEggItem> KELP_GOLEM_SPAWN_EGG = spawnEgg("kelp_golem_spawn_egg", ModEntityTypes.KELP_GOLEM);
    public static final ItemLikeEntry<SpawnEggItem> NETHERITE_GOLEM_SPAWN_EGG = spawnEgg("netherite_golem_spawn_egg", ModEntityTypes.NETHERITE_GOLEM);
    public static final ItemLikeEntry<SpawnEggItem> SLIME_GOLEM_SPAWN_EGG = spawnEgg("slime_golem_spawn_egg", ModEntityTypes.SLIME_GOLEM);
    public static final ItemLikeEntry<SpawnEggItem> TERRACOTTA_GOLEM_SPAWN_EGG = spawnEgg("terracotta_golem_spawn_egg", ModEntityTypes.TERRACOTTA_GOLEM);

    // Blocks lost their hover-text hook in 26.1, so the tooltip lives on the BlockItem now.
    public static final ItemLikeEntry<BlockItem> CANDLE_GOLEM_BLOCK = ITEMS.register("candle_golem_block",
        properties -> new TooltipBlockItem(ModBlocks.CANDLE_GOLEM_BLOCK.get(), properties.useBlockDescriptionPrefix(), ConstantComponents.CANDLE_GOLEM_TOOLTIP),
        Item.Properties::new);
    public static final ItemLikeEntry<BlockItem> CLAY_GOLEM_STATUE = ITEMS.register("clay_golem_statue",
        properties -> new TooltipBlockItem(ModBlocks.CLAY_GOLEM_STATUE.get(), properties.useBlockDescriptionPrefix(), ConstantComponents.CLAY_GOLEM_STATUE_TOOLTIP),
        Item.Properties::new);
    public static final ItemLikeEntry<HoneyBlobItem> HONEY_BLOB = ITEMS.register("honey_blob", HoneyBlobItem::new, Item.Properties::new);
    public static final ItemLikeEntry<CoalGolemItem> COAL_GOLEM = ITEMS.register("coal_golem", CoalGolemItem::new, () -> new Item.Properties().stacksTo(16));

    private static ItemLikeEntry<SpawnEggItem> spawnEgg(String id, RegistryEntry<? extends EntityType<? extends Mob>> type) {
        return ITEMS.register(id, SpawnEggItem::new, () -> new Item.Properties().spawnEgg(type.get()));
    }
}
