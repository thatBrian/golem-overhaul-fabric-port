package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base;

import com.geckolib.cache.model.GeoBone;
import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.common.entities.golems.base.BaseGolem;

public class BaseGolemRenderer<T extends BaseGolem> extends GeoEntityRenderer<T, LivingEntityRenderState> {

    protected final BaseGolemModel<T> golemModel;

    public BaseGolemRenderer(EntityRendererProvider.Context renderManager, BaseGolemModel<T> model) {
        super(renderManager, model);
        this.golemModel = model;
    }

    public static <T extends Entity> Identifier texture(RegistryEntry<EntityType<T>> golem) {
        String name = name(golem).getPath();
        return Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "%s/%s".formatted(name.replace("_golem", ""), name));
    }

    public static <T extends Entity> Identifier name(RegistryEntry<EntityType<T>> golem) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(golem.get());
    }

    @Override
    public void addRenderData(T animatable, @Nullable Void relatedObject, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.CRACKINESS, animatable.getCrackiness());
        renderState.addGeckolibData(GolemRenderData.HEALTH_FRACTION, animatable.getHealth() / animatable.getMaxHealth());
        addGolemRenderData(animatable, renderState, partialTick);
    }

    /**
     * Capture any golem-specific state the model or layers need; the entity is not available at render time.
     */
    protected void addGolemRenderData(T animatable, LivingEntityRenderState renderState, float partialTick) {
    }

    /**
     * Head tracking. Upstream did this in GeoModel#setCustomAnimations; GeckoLib 5 exposes the posed bones
     * through snapshots that run after the animation controllers, so the head is turned here instead.
     */
    @Override
    public void adjustModelBonesForRender(RenderPassInfo<LivingEntityRenderState> renderPassInfo, BoneSnapshots snapshots) {
        if (!this.golemModel.turnsHead()) return;
        LivingEntityRenderState renderState = renderPassInfo.renderState();
        if (renderState.getOrDefaultGeckolibData(GolemRenderData.HEAD_LOCKED, false)) return;

        renderPassInfo.model().getBone("head_rotation").ifPresent(head -> {
            GeoBone[] children = head.children();
            if (children.length == 0) return;

            int max = this.golemModel.maxHeadRotation();
            float pitch = renderState.getOrDefaultGeckolibData(DataTickets.ENTITY_PITCH, 0f);
            float yaw = renderState.getOrDefaultGeckolibData(DataTickets.ENTITY_YAW, 0f);
            snapshots.get(children[0].name()).ifPresent(bone -> bone
                .setRotX(Mth.clamp(pitch, -max, max) * Mth.DEG_TO_RAD)
                .setRotY(Mth.clamp(yaw, -max, max) * Mth.DEG_TO_RAD));
        });
    }
}
