package tech.alexnijjar.golemoverhaul.common.entities.golems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import tech.alexnijjar.golemoverhaul.common.config.GolemOverhaulConfig;
import tech.alexnijjar.golemoverhaul.common.entities.AdditionalBeeData;
import tech.alexnijjar.golemoverhaul.common.entities.IShearable;
import tech.alexnijjar.golemoverhaul.common.entities.golems.base.BaseGolem;
import tech.alexnijjar.golemoverhaul.common.entities.projectiles.HoneyBlobProjectile;
import tech.alexnijjar.golemoverhaul.common.registry.ModItems;
import tech.alexnijjar.golemoverhaul.mixins.common.BeeAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoneyGolem extends BaseGolem implements RangedAttackMob, IShearable {

    public static final byte NECTAR_PARTICLES_EVENT_ID = 8;

    public static final int RANGED_ATTACK_DELAY_TICKS = 6;

    public static final EntityDataAccessor<Byte> ID_HONEY_LEVEL = SynchedEntityData.defineId(HoneyGolem.class,
            EntityDataSerializers.BYTE);

    private final List<BeeData> bees = new ArrayList<>();

    private int attackAnimationDelay = -1;

    private boolean hasPopulatedInitialBees;

    public HoneyGolem(EntityType<? extends AbstractGolem> type, Level level) {
        super(type, level, false, GolemOverhaulConfig.allowSpawning && GolemOverhaulConfig.spawnHoneyGolems);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 6);
    }

    public static boolean checkMobSpawnRules(EntityType<? extends Mob> type, LevelAccessor level,
                                             EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        if (!GolemOverhaulConfig.spawnHoneyGolems || !GolemOverhaulConfig.allowSpawning)
            return false;
        return Mob.checkMobSpawnRules(type, level, spawnReason, pos, random);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_HONEY_LEVEL, (byte) 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("HoneyLevel", this.getHoneyLevel());
        ValueOutput.ValueOutputList beeList = output.childrenList("Bees");
        for (BeeData bee : bees) {
            ValueOutput entry = beeList.addChild();
            entry.store("EntityData", CompoundTag.CODEC, bee.tag);
            entry.putInt("TicksInHive", bee.ticks);
            entry.putInt("MinOccupationTicks", bee.minOccupationTicks);
        }
        output.putBoolean("HasPopulatedInitialBees", hasPopulatedInitialBees);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setHoneyLevel(input.getByteOr("HoneyLevel", (byte) 0));
        bees.clear();
        for (ValueInput entry : input.childrenListOrEmpty("Bees")) {
            entry.read("EntityData", CompoundTag.CODEC).ifPresent(tag -> bees.add(new BeeData(
                    tag,
                    entry.getIntOr("TicksInHive", 0),
                    entry.getIntOr("MinOccupationTicks", 2400))));
        }
        this.hasPopulatedInitialBees = input.getBooleanOr("HasPopulatedInitialBees", false);
    }

    @Override
    public boolean canMeleeAttack() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1, 20, 15));
    }

    public byte getHoneyLevel() {
        return this.entityData.get(ID_HONEY_LEVEL);
    }

    public void setHoneyLevel(byte honeyLevel) {
        this.entityData.set(ID_HONEY_LEVEL, honeyLevel);
    }

    public boolean isFullOfHoney() {
        return getHoneyLevel() >= 4;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.HONEY_BLOCK_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HONEY_BLOCK_BREAK;
    }

    @Override
    public int getAttackTicks() {
        return 15;
    }

    @Override
    public Item getRepairItem() {
        return Items.HONEY_BOTTLE;
    }

    @Override
    public float getRepairItemHealAmount() {
        return 20;
    }

    @Override
    public SoundEvent getRepairSound() {
        return SoundEvents.HONEY_BLOCK_BREAK;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        if (attackAnimationDelay == -1) {
            sendAttackEvent();
            attackAnimationDelay = RANGED_ATTACK_DELAY_TICKS;
        }
    }

    public void actuallyShoot(LivingEntity target) {
        if (target == null)
            return;
        Projectile projectile = new HoneyBlobProjectile(level(), this);
        projectile.setPos(getX(), getY(), getZ());

        double x = target.getX() - getX();
        double y = target.getY() - projectile.getY();
        double z = target.getZ() - getZ();
        double distance = Math.sqrt(x * x + z * z) * 0.2;
        projectile.shoot(x, y + distance + 0.8, z, 1.2f, 3);

        level().addFreshEntity(projectile);
        playSound(SoundEvents.SLIME_ATTACK, 1, 0.4f / (getRandom().nextFloat() * 0.4f + 0.8f));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide() && !hasPopulatedInitialBees) {
            hasPopulatedInitialBees = true;
            populateInitialBees();
        }
    }

    private void populateInitialBees() {
        final int count = 2 + level().getRandom().nextInt(4);
        for (int i = 0; i < count; i++) {
            Bee bee = Objects.requireNonNull(EntityType.BEE.create(level(), EntitySpawnReason.LOAD));
            ((AdditionalBeeData) bee).golemoverhaul$setOwner(this.getUUID());
            this.bees.add(new BeeData(saveBee(bee), 0, 2400));
        }
        this.setHoneyLevel((byte) count);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        if (!bees.isEmpty() && level().getRandom().nextDouble() < 0.005) {
            playSound(SoundEvents.BEEHIVE_WORK);
        }

        if (!level().isDarkOutside() && !level().isRaining()) {
            for (int i = 0; i < bees.size(); i++) {
                BeeData bee = bees.get(i);
                bee.ticks++;
                if (bee.ticks >= bee.minOccupationTicks) {
                    releaseBee(i);
                    i--;
                }
            }
        }

        if (!bees.isEmpty() && tickCount % 200 == 0) {
            this.heal(1);
        }

        attackAnimationDelay = Math.max(-1, attackAnimationDelay - 1);
        if (attackAnimationDelay == 0) {
            actuallyShoot(getTarget());
            attackAnimationDelay = -1;
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (level().isClientSide())
            return;
        if (!bees.isEmpty()) {
            this.releaseAllBees();
        }
    }

    @Override
    public @NotNull List<ItemStack> onSheared() {
        if (!isFullOfHoney()) {
            return List.of();
        }

        playSound(SoundEvents.BEEHIVE_SHEAR);

        if (!level().isClientSide()) {
            setHoneyLevel((byte) 0);
        }

        return List.of(new ItemStack(ModItems.HONEY_BLOB.get(), 5 + level().getRandom().nextInt(8)),
                new ItemStack(Items.HONEYCOMB, 3));
    }

    @Override
    public boolean isShearable() {
        return isFullOfHoney();
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        if (id == NECTAR_PARTICLES_EVENT_ID) {
            for (int i = 0; i < 8; ++i) {
                level().addParticle(ParticleTypes.FALLING_NECTAR,
                        getX() + getRandom().nextGaussian() * 0.25,
                        getY() + 0.5,
                        getZ() + getRandom().nextGaussian() * 0.25,
                        0, 0, 0);
            }
        }
    }

    public void healFromNectar() {
        this.heal(5);
        level().broadcastEntityEvent(this, NECTAR_PARTICLES_EVENT_ID);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (!bees.isEmpty() && source.getEntity() instanceof LivingEntity entity && canAttack(entity)) {
            if (entity instanceof Player player && player.isCreative())
                return super.hurtServer(level, source, amount);
            this.releaseAllBees().forEach(bee -> bee.setTarget(entity));
        }
        return super.hurtServer(level, source, amount);
    }

    public boolean canPutBee() {
        return bees.size() < 5;
    }

    public void putBee(Bee bee) {
        if (!canPutBee())
            return;
        ((AdditionalBeeData) bee).golemoverhaul$setOwner(this.getUUID());
        bees.add(new BeeData(saveBee(bee), 0, 2400));
        if (bee.hasNectar() && !isFullOfHoney()) {
            setHoneyLevel((byte) (getHoneyLevel() + 1));
            if (getHealth() < getMaxHealth()) {
                heal(10);
                level().broadcastEntityEvent(this, NECTAR_PARTICLES_EVENT_ID);
            }
        }
        bee.dropOffNectar();
        bee.discard();
        playSound(SoundEvents.BEEHIVE_ENTER);
    }

    private Bee releaseBee(int index) {
        BeeData data = bees.get(index);
        Bee bee = Objects.requireNonNull(EntityType.BEE.create(level(), EntitySpawnReason.LOAD));
        bee.load(TagValueInput.create(ProblemReporter.DISCARDING, registryAccess(), data.tag));
        bee.setPos(getX(), getY(), getZ());
        bee.dropOffNectar();
        bee.setHealth(bee.getMaxHealth());
        level().addFreshEntity(bee);
        bee.setStayOutOfHiveCountdown(400);
        playSound(SoundEvents.BEEHIVE_EXIT);
        bees.remove(index);
        return bee;
    }

    public List<Bee> releaseAllBees() {
        List<Bee> removedBees = new ArrayList<>();
        int size = this.bees.size();
        for (int i = 0; i < size; i++) {
            Bee bee = releaseBee(0);
            ((BeeAccessor) bee).setRemainingCooldownBeforeLocatingNewFlower(400);
            removedBees.add(bee);
        }
        return removedBees;
    }

    // Entities serialise through ValueOutput now; the bee's NBT is captured into a CompoundTag so the
    // hive list can keep the same shape it had upstream.
    private CompoundTag saveBee(Bee bee) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registryAccess());
        bee.saveWithoutId(output);
        return output.buildResult();
    }

    private static final class BeeData {

        private final CompoundTag tag;
        private int ticks;
        private final int minOccupationTicks;

        private BeeData(CompoundTag tag, int ticks, int minOccupationTicks) {
            this.tag = tag;
            this.ticks = ticks;
            this.minOccupationTicks = minOccupationTicks;
        }
    }
}
