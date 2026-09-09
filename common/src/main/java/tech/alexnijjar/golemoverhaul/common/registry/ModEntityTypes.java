package tech.alexnijjar.golemoverhaul.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.common.entities.golems.*;
import tech.alexnijjar.golemoverhaul.common.entities.projectiles.CandleFlameProjectile;
import tech.alexnijjar.golemoverhaul.common.entities.projectiles.HoneyBlobProjectile;
import tech.alexnijjar.golemoverhaul.common.entities.projectiles.MudBallProjectile;
import tech.alexnijjar.golemoverhaul.common.tags.ModBiomeTags;

public class ModEntityTypes {

    public static final ResourcefulRegistry<EntityType<?>> ENTITY_TYPES =
            ResourcefulRegistries.create(BuiltInRegistries.ENTITY_TYPE, GolemOverhaul.MOD_ID);
    public static final ResourcefulRegistry<EntityType<?>> GOLEMS = ResourcefulRegistries.create(ENTITY_TYPES);
    public static final ResourcefulRegistry<EntityType<?>> PROJECTILES = ResourcefulRegistries.create(ENTITY_TYPES);

    public static final RegistryEntry<EntityType<BarrelGolem>> BARREL_GOLEM = golem("barrel_golem",
        EntityType.Builder.of(BarrelGolem::new, MobCategory.CREATURE)
            .sized(1, 1)
            .clientTrackingRange(10));

    public static final RegistryEntry<EntityType<CandleGolem>> CANDLE_GOLEM = golem("candle_golem",
        EntityType.Builder.of(CandleGolem::new, MobCategory.CREATURE)
            .sized(0.375f, 0.5625f)
            .clientTrackingRange(10));

    public static final RegistryEntry<EntityType<CoalGolem>> COAL_GOLEM = golem("coal_golem",
        EntityType.Builder.of(CoalGolem::new, MobCategory.AMBIENT)
            .sized(0.25f, 0.5f)
            .clientTrackingRange(10)
            .fireImmune());

    public static final RegistryEntry<EntityType<HayGolem>> HAY_GOLEM = golem("hay_golem",
        EntityType.Builder.of(HayGolem::new, MobCategory.CREATURE)
            .sized(0.75f, 2)
            .clientTrackingRange(10));

    public static final RegistryEntry<EntityType<HoneyGolem>> HONEY_GOLEM = golem("honey_golem",
        EntityType.Builder.of(HoneyGolem::new, MobCategory.CREATURE)
            .sized(0.875f, 1)
            .clientTrackingRange(10));

    public static final RegistryEntry<EntityType<KelpGolem>> KELP_GOLEM = golem("kelp_golem",
        EntityType.Builder.of(KelpGolem::new, MobCategory.CREATURE)
            .sized(0.8125f, 1.25f)
            .clientTrackingRange(10));

    public static final RegistryEntry<EntityType<NetheriteGolem>> NETHERITE_GOLEM = golem("netherite_golem",
        EntityType.Builder.of(NetheriteGolem::new, MobCategory.CREATURE)
            .sized(1.75f, 2.1f)
            .clientTrackingRange(10)
            .fireImmune());

    public static final RegistryEntry<EntityType<SlimeGolem>> SLIME_GOLEM = golem("slime_golem",
        EntityType.Builder.of(SlimeGolem::new, MobCategory.MONSTER)
            .sized(1.125f, 0.8125f)
            .clientTrackingRange(10));

    public static final RegistryEntry<EntityType<TerracottaGolem>> TERRACOTTA_GOLEM = golem("terracotta_golem",
        EntityType.Builder.of(TerracottaGolem::new, MobCategory.CREATURE)
            .sized(0.5f, 0.75f)
            .clientTrackingRange(10));

    public static final RegistryEntry<EntityType<CandleFlameProjectile>> CANDLE_FLAME = projectile("candle_flame",
        EntityType.Builder.<CandleFlameProjectile>of(CandleFlameProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));

    public static final RegistryEntry<EntityType<MudBallProjectile>> MUD_BALL = projectile("mud_ball",
        EntityType.Builder.<MudBallProjectile>of(MudBallProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));

    public static final RegistryEntry<EntityType<HoneyBlobProjectile>> HONEY_BLOB = projectile("honey_blob",
        EntityType.Builder.<HoneyBlobProjectile>of(HoneyBlobProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10));

    // EntityType.Builder#build takes the registry key since 1.21.2.
    private static <T extends Entity> RegistryEntry<EntityType<T>> golem(String id, EntityType.Builder<T> builder) {
        return GOLEMS.register(id, () -> builder.build(key(id)));
    }

    private static <T extends Entity> RegistryEntry<EntityType<T>> projectile(String id, EntityType.Builder<T> builder) {
        return PROJECTILES.register(id, () -> builder.build(key(id)));
    }

    private static ResourceKey<EntityType<?>> key(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, id));
    }

    public static void init() {
        ENTITY_TYPES.init();
        registerAttributes();
        registerSpawnPlacements();
        registerBiomeModifications();
    }

    private static void registerAttributes() {
        EntityAttributeRegistry.register(BARREL_GOLEM, BarrelGolem::createAttributes);
        EntityAttributeRegistry.register(CANDLE_GOLEM, CandleGolem::createAttributes);
        EntityAttributeRegistry.register(COAL_GOLEM, CoalGolem::createAttributes);
        EntityAttributeRegistry.register(HAY_GOLEM, HayGolem::createAttributes);
        EntityAttributeRegistry.register(HONEY_GOLEM, HoneyGolem::createAttributes);
        EntityAttributeRegistry.register(KELP_GOLEM, KelpGolem::createAttributes);
        EntityAttributeRegistry.register(NETHERITE_GOLEM, NetheriteGolem::createAttributes);
        EntityAttributeRegistry.register(SLIME_GOLEM, SlimeGolem::createAttributes);
        EntityAttributeRegistry.register(TERRACOTTA_GOLEM, TerracottaGolem::createAttributes);
    }

    private static void registerSpawnPlacements() {
        SpawnPlacementsRegistry.register(ModEntityTypes.BARREL_GOLEM, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BarrelGolem::checkMobSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.CANDLE_GOLEM, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CandleGolem::checkMobSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.COAL_GOLEM, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoalGolem::checkMobSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.HAY_GOLEM, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HayGolem::checkMobSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.HONEY_GOLEM, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HoneyGolem::checkMobSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.KELP_GOLEM, SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, KelpGolem::checkMobSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.NETHERITE_GOLEM, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NetheriteGolem::checkMobSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.SLIME_GOLEM, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SlimeGolem::checkSlimeSpawnRules);
        SpawnPlacementsRegistry.register(ModEntityTypes.TERRACOTTA_GOLEM, SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TerracottaGolem::checkMobSpawnRules);
    }

    private static void registerBiomeModifications() {
        addSpawn(ModEntityTypes.COAL_GOLEM, ModBiomeTags.COAL_GOLEM_SPAWNS, 20, 3, 5);
        addSpawn(ModEntityTypes.HONEY_GOLEM, ModBiomeTags.HONEY_GOLEM_SPAWNS, 6, 1, 2);
        addSpawn(ModEntityTypes.SLIME_GOLEM, ModBiomeTags.SLIME_GOLEM_SPAWNS, 25, 1, 2);
        addSpawn(ModEntityTypes.SLIME_GOLEM, ModBiomeTags.SLIME_GOLEM_SWAMP_SPAWNS, 1, 1, 1);
        addSpawn(ModEntityTypes.TERRACOTTA_GOLEM, ModBiomeTags.TERRACOTTA_GOLEM_SPAWNS, 6, 1, 2);
    }

    private static <T extends Entity> void addSpawn(RegistryEntry<EntityType<T>> entityType,
                                                    TagKey<Biome> spawnBiomesTag, int weight, int minCount,
                                                    int maxCount) {
        // The spawn weight moved out of SpawnerData in 1.21.5; Architectury takes it separately.
        BiomeModifications.addProperties(
                biomeContext -> biomeContext.hasTag(spawnBiomesTag),
                (context, properties) -> properties.getSpawnProperties().addSpawn(
                        entityType.get().getCategory(),
                        new MobSpawnSettings.SpawnerData(entityType.get(), minCount, maxCount),
                        weight
                )
        );
    }
}
