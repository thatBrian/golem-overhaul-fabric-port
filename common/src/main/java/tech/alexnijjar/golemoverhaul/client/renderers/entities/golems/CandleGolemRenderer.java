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
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers.GolemGlowLayer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.CandleGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class CandleGolemRenderer extends BaseGolemRenderer<CandleGolem> {

    public static final Identifier GLOW_1 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/candle/candle_golem_1_glow.png");
    public static final Identifier GLOW_2 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/candle/candle_golem_2_glow.png");

    public static final Identifier MODEL_1 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/candle/candle_golem_1");
    public static final Identifier MODEL_2 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/candle/candle_golem_2");
    public static final Identifier MODEL_3 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/candle/candle_golem_3");

    public CandleGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.CANDLE_GOLEM, true, 90) {
            @Override
            public Identifier getModelResource(GeoRenderState renderState) {
                return switch (renderState.getOrDefaultGeckolibData(GolemRenderData.CRACKINESS, Crackiness.Level.NONE)) {
                    case NONE, LOW -> MODEL_1;
                    case MEDIUM -> MODEL_2;
                    case HIGH -> MODEL_3;
                };
            }
        });

        withRenderLayer(new GolemGlowLayer<>(this, state -> {
            if (!state.getOrDefaultGeckolibData(GolemRenderData.LIT, false)) return null;
            return switch (state.getOrDefaultGeckolibData(GolemRenderData.CRACKINESS, Crackiness.Level.NONE)) {
                case NONE, LOW -> GLOW_1;
                case MEDIUM -> GLOW_2;
                case HIGH -> null;
            };
        }));
    }

    @Override
    protected void addGolemRenderData(CandleGolem golem, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.LIT, golem.isLit());
    }
}
