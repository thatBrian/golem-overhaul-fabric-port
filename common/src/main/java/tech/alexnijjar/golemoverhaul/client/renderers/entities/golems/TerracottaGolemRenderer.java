package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems;

import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemModel;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemRenderer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.TerracottaGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class TerracottaGolemRenderer extends BaseGolemRenderer<TerracottaGolem> {

    public static final Identifier MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/terracotta/terracotta_golem");
    public static final Identifier CACTUS_MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/terracotta/cactus_terracotta_golem");
    public static final Identifier DEAD_BUSH_MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/terracotta/dead_bush_terracotta_golem");

    public TerracottaGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.TERRACOTTA_GOLEM, true, 10) {
            @Override
            public Identifier getModelResource(GeoRenderState renderState) {
                return switch (renderState.getOrDefaultGeckolibData(GolemRenderData.TERRACOTTA_TYPE, TerracottaGolem.Type.NORMAL)) {
                    case NORMAL -> MODEL;
                    case CACTUS -> CACTUS_MODEL;
                    case DEAD_BUSH -> DEAD_BUSH_MODEL;
                };
            }
        });
    }

    @Override
    protected void addGolemRenderData(TerracottaGolem golem, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.TERRACOTTA_TYPE, golem.getTerracottaType());
    }
}
