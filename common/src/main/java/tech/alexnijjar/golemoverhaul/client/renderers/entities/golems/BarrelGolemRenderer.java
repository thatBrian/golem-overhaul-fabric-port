package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemModel;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemRenderer;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers.BarrelGolemHeldItemLayer;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers.GolemGlowLayer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.BarrelGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class BarrelGolemRenderer extends BaseGolemRenderer<BarrelGolem> {

    public static final Identifier GLOW = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/barrel/barrel_golem_glow.png");

    public BarrelGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.BARREL_GOLEM, true, 10));

        withRenderLayer(new GolemGlowLayer<>(this, state -> GLOW));
        withRenderLayer(new BarrelGolemHeldItemLayer(renderManager, this));
    }

    @Override
    protected void addGolemRenderData(BarrelGolem golem, LivingEntityRenderState renderState, float partialTick) {
        // The wake-up and barter animations own the head, so head tracking is suspended while they play.
        renderState.addGeckolibData(GolemRenderData.HEAD_LOCKED, golem.isWakingUp() || golem.isBartering());
    }
}
