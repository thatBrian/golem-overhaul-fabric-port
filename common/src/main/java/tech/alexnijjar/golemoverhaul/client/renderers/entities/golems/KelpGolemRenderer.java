package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems;

import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemModel;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemRenderer;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers.GolemGlowLayer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.KelpGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class KelpGolemRenderer extends BaseGolemRenderer<KelpGolem> {

    public static final Identifier GLOW = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/kelp/kelp_golem_glow.png");

    public KelpGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.KELP_GOLEM, true, 90));

        // The glow fades with health: the render colour (including alpha) is swapped in for this layer only.
        withRenderLayer(new GolemGlowLayer<>(this, state -> GLOW) {
            @Override
            public void submitRenderTask(RenderPassInfo<LivingEntityRenderState> renderPassInfo, SubmitNodeCollector renderTasks) {
                LivingEntityRenderState renderState = renderPassInfo.renderState();
                int previousColor = renderPassInfo.renderColor();
                int strength = Mth.clamp((int) (renderState.getOrDefaultGeckolibData(GolemRenderData.HEALTH_FRACTION, 1f) * 255), 0, 255);
                renderState.addGeckolibData(DataTickets.RENDER_COLOR, ARGB.color(strength, strength, strength, strength));
                super.submitRenderTask(renderPassInfo, renderTasks);
                renderState.addGeckolibData(DataTickets.RENDER_COLOR, previousColor);
            }
        });
    }

    @Override
    protected void addGolemRenderData(KelpGolem golem, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.CHARGED, golem.isCharged());
    }

    // The swirling "particle" bones are only shown while conduit-charged.
    @Override
    public void adjustModelBonesForRender(RenderPassInfo<LivingEntityRenderState> renderPassInfo, BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);
        if (renderPassInfo.renderState().getOrDefaultGeckolibData(GolemRenderData.CHARGED, false)) return;
        snapshots.ifPresent("particle", bone -> bone.skipRender(true).skipChildrenRender(true));
        snapshots.ifPresent("particle2", bone -> bone.skipRender(true).skipChildrenRender(true));
    }
}
