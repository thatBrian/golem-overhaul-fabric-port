package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems;

import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemModel;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemRenderer;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers.GolemGlowLayer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.CoalGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class CoalGolemRenderer extends BaseGolemRenderer<CoalGolem> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/coal/coal_golem.png");
    public static final Identifier LIT_TEXTURE = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/coal/coal_golem_lit.png");

    public CoalGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.COAL_GOLEM, false, 0) {
            @Override
            public Identifier getTextureResource(GeoRenderState renderState) {
                return renderState.getOrDefaultGeckolibData(GolemRenderData.LIT, false) ? LIT_TEXTURE : TEXTURE;
            }
        });

        withRenderLayer(new GolemGlowLayer<>(this, state -> state.getOrDefaultGeckolibData(GolemRenderData.LIT, false) ? LIT_TEXTURE : null));
    }

    @Override
    protected void addGolemRenderData(CoalGolem golem, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.LIT, golem.isLit());
    }

    // The coal golem plays its own death animation instead of the vanilla topple.
    @Override
    protected float getDeathMaxRotation(GeoRenderState renderState) {
        return 0;
    }
}
