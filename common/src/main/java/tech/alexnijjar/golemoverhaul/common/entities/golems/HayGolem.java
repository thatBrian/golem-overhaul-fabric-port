package tech.alexnijjar.golemoverhaul.common.entities.golems;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.alexnijjar.golemoverhaul.common.config.GolemOverhaulConfig;
import tech.alexnijjar.golemoverhaul.common.entities.IShearable;
import tech.alexnijjar.golemoverhaul.common.entities.golems.base.BaseGolem;
import tech.alexnijjar.golemoverhaul.common.recipes.GolemConstructionRecipe;
import tech.alexnijjar.golemoverhaul.common.recipes.SingleEntityInput;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;
import tech.alexnijjar.golemoverhaul.common.registry.ModRecipeTypes;
import tech.alexnijjar.golemoverhaul.common.registry.ModSoundEvents;
import tech.alexnijjar.golemoverhaul.common.utils.ModUtils;

import java.util.List;
import java.util.Locale;

public class HayGolem extends BaseGolem implements IShearable {

    private static final EntityDataAccessor<Byte> ID_COLOR = SynchedEntityData.defineId(HayGolem.class,
            EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_SHEARED = SynchedEntityData.defineId(HayGolem.class,
            EntityDataSerializers.BOOLEAN);

    public HayGolem(EntityType<? extends AbstractGolem> type, Level level) {
        super(type, level, true, GolemOverhaulConfig.allowSpawning && GolemOverhaulConfig.spawnHayGolems);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.ATTACK_DAMAGE, 3);
    }

    public static boolean checkMobSpawnRules(EntityType<? extends Mob> type, LevelAccessor level,
            EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        if (!GolemOverhaulConfig.spawnHayGolems || !GolemOverhaulConfig.allowSpawning)
            return false;
        return Mob.checkMobSpawnRules(type, level, spawnReason, pos, random);
    }

    public static void trySpawnGolem(Level level, BlockPos pos) {
        // Recipes are server-only since 1.21.2.
        if (!(level instanceof ServerLevel serverLevel)) return;
        GolemConstructionRecipe recipe = serverLevel.recipeAccess().getRecipeFor(ModRecipeTypes.GOLEM_CONSTRUCTION.get(),
                new SingleEntityInput(ModEntityTypes.HAY_GOLEM.get()), level).orElseThrow().value();
        BlockPattern.BlockPatternMatch pattern = recipe.createPattern().find(level, pos);
        if (pattern == null)
            return;
        HayGolem golem = ModEntityTypes.HAY_GOLEM.get().create(level, EntitySpawnReason.TRIGGERED);
        if (golem == null)
            return;
        golem.setColor(level.getRandom().nextBoolean() ? Color.GREEN : Color.RED);
        ModUtils.spawnGolemInWorld(level, pattern, golem, pattern.getBlock(1, 2, 0).getPos());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_COLOR, (byte) 0);
        builder.define(ID_SHEARED, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString("color", this.getColor().name().toLowerCase(Locale.ROOT));
        output.putBoolean("sheared", this.isSheared());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getString("color").ifPresent(color -> this.setColor(Color.valueOf(color.toUpperCase(Locale.ROOT))));
        this.setSheared(input.getBooleanOr("sheared", false));
    }

    @Override
    public boolean canTarget() {
        return false;
    }

    public Color getColor() {
        return Color.values()[this.entityData.get(ID_COLOR)];
    }

    public void setColor(Color color) {
        this.entityData.set(ID_COLOR, (byte) color.ordinal());
    }

    public boolean isSheared() {
        return this.entityData.get(ID_SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.entityData.set(ID_SHEARED, sheared);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSoundEvents.HAY_GOLEM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.HAY_GOLEM_DEATH.get();
    }

    @Override
    public Item getRepairItem() {
        return Items.WHEAT;
    }

    @Override
    public float getRepairItemHealAmount() {
        return 10;
    }

    @Override
    public SoundEvent getRepairSound() {
        return SoundEvents.VINE_BREAK;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyInstance,
            EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        this.setColor(level.getRandom().nextBoolean() ? Color.GREEN : Color.RED);
        return super.finalizeSpawn(level, difficultyInstance, spawnReason, spawnGroupData);
    }

    @Override
    protected AABB getAttackBoundingBox(double horizontalExpansion) {
        return super.getAttackBoundingBox(horizontalExpansion).inflate(0.5f, 0, 0.5f);
    }

    @Override
    public @NotNull List<ItemStack> onSheared() {
        playSound(SoundEvents.SNOW_GOLEM_SHEAR);
        setSheared(true);
        return List.of(Items.CARVED_PUMPKIN.getDefaultInstance());
    }

    @Override
    public boolean isShearable() {
        return !isSheared();
    }

    public enum Color {
        GREEN,
        RED,
    }
}
