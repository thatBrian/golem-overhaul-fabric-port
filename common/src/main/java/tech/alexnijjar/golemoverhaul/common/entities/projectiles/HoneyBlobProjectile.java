package tech.alexnijjar.golemoverhaul.common.entities.projectiles;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;
import tech.alexnijjar.golemoverhaul.common.registry.ModItems;
import tech.alexnijjar.golemoverhaul.common.tags.ModEntityTypeTags;

public class HoneyBlobProjectile extends AbstractArrow implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public HoneyBlobProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public HoneyBlobProjectile(Level level, LivingEntity owner) {
        super(ModEntityTypes.HONEY_BLOB.get(), level);
        setOwner(owner);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    private ParticleOptions getParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, Items.HONEY_BLOCK);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                level().addParticle(getParticle(), getX(), getY(), getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity livingEntity && BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType()).is(ModEntityTypeTags.HONEY_IMMUNE)) {
            livingEntity.heal(20);
            return;
        }
        if (getOwner() != null && entity.equals(getOwner())) return;
        if (level() instanceof ServerLevel serverLevel) {
            entity.hurtServer(serverLevel, damageSources().thrown(this, getOwner()), 6);
        }
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,
                getOwner() instanceof Player ? 180 : 60,
                2));
        }
        if (!level().isClientSide()) {
            level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide()) {
            level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.HONEY_BLOCK_BREAK;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ModItems.HONEY_BLOB.get().getDefaultInstance();
    }

    @Override
    public void tick() {
        super.tick();
        if (isInGround()) discard();
    }
}
