package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems;

import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemModel;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemRenderer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.SlimeGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class SlimeGolemRenderer extends BaseGolemRenderer<SlimeGolem> {

    public static final Identifier LARGE_TEXTURE = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/slime/slime_golem.png");
    public static final Identifier SMALL_TEXTURE = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/slime/small_slime_golem.png");

    public static final Identifier LARGE_MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/slime/slime_golem");
    public static final Identifier SMALL_MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/slime/small_slime_golem");

    public SlimeGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.SLIME_GOLEM, false, 0) {
            @Override
            public Identifier getModelResource(GeoRenderState renderState) {
                return renderState.getOrDefaultGeckolibData(GolemRenderData.SLIME_LARGE, true) ? LARGE_MODEL : SMALL_MODEL;
            }

            @Override
            public Identifier getTextureResource(GeoRenderState renderState) {
                return renderState.getOrDefaultGeckolibData(GolemRenderData.SLIME_LARGE, true) ? LARGE_TEXTURE : SMALL_TEXTURE;
            }
        });
    }

    @Override
    protected void addGolemRenderData(SlimeGolem golem, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.SLIME_LARGE, golem.getSize().isLarge());
    }

    @Override
    public @Nullable RenderType getRenderType(LivingEntityRenderState renderState, Identifier texture) {
        return RenderTypes.entityTranslucent(texture);
    }

    // The inner body shrinks as the golem loses health.
    @Override
    public void adjustModelBonesForRender(RenderPassInfo<LivingEntityRenderState> renderPassInfo, BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);
        float healthFraction = renderPassInfo.renderState().getOrDefaultGeckolibData(GolemRenderData.HEALTH_FRACTION, 1f);
        float scale = 0.5f + Mth.clamp(healthFraction / 2, 0, 0.5f);
        snapshots.ifPresent("body_2", bone -> bone.setScale(scale, scale, scale));
    }
}
