package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems;

import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Crackiness;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemModel;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemRenderer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.HoneyGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class HoneyGolemRenderer extends BaseGolemRenderer<HoneyGolem> {

    public static final Identifier FULL_TEXTURE_1 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/honey/honey_golem_full_1.png");
    public static final Identifier FULL_TEXTURE_2 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/honey/honey_golem_full_2.png");
    public static final Identifier FULL_TEXTURE_3 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/honey/honey_golem_full_3.png");

    public HoneyGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.HONEY_GOLEM, true, 20) {
            @Override
            public Identifier getTextureResource(GeoRenderState renderState) {
                if (!renderState.getOrDefaultGeckolibData(GolemRenderData.FULL_OF_HONEY, false)) return super.getTextureResource(renderState);
                return switch (renderState.getOrDefaultGeckolibData(GolemRenderData.CRACKINESS, Crackiness.Level.NONE)) {
                    case NONE, LOW -> FULL_TEXTURE_1;
                    case MEDIUM -> FULL_TEXTURE_2;
                    case HIGH -> FULL_TEXTURE_3;
                };
            }
        });
    }

    @Override
    protected void addGolemRenderData(HoneyGolem golem, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.FULL_OF_HONEY, golem.isFullOfHoney());
    }
}
