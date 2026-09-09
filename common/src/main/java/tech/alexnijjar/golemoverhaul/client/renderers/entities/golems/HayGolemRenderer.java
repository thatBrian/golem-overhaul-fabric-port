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
import tech.alexnijjar.golemoverhaul.common.entities.golems.HayGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class HayGolemRenderer extends BaseGolemRenderer<HayGolem> {

    public static final Identifier GREEN_TEXTURE_1 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/hay/green_hay_golem_1.png");
    public static final Identifier GREEN_TEXTURE_2 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/hay/green_hay_golem_2.png");
    public static final Identifier GREEN_TEXTURE_3 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/hay/green_hay_golem_3.png");

    public static final Identifier RED_TEXTURE_1 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/hay/red_hay_golem_1.png");
    public static final Identifier RED_TEXTURE_2 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/hay/red_hay_golem_2.png");
    public static final Identifier RED_TEXTURE_3 = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/hay/red_hay_golem_3.png");

    public static final Identifier GREEN_MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/hay/green_hay_golem");
    public static final Identifier SHEARED_GREEN_MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/hay/green_hay_golem_sheared");

    public static final Identifier RED_MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/hay/red_hay_golem");
    public static final Identifier SHEARED_RED_MODEL = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "entity/hay/red_hay_golem_sheared");

    public HayGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.HAY_GOLEM, true, 90) {
            @Override
            public Identifier getModelResource(GeoRenderState renderState) {
                boolean sheared = renderState.getOrDefaultGeckolibData(GolemRenderData.SHEARED, false);
                return renderState.getOrDefaultGeckolibData(GolemRenderData.HAY_COLOR, HayGolem.Color.GREEN) == HayGolem.Color.GREEN ?
                    sheared ? SHEARED_GREEN_MODEL : GREEN_MODEL :
                    sheared ? SHEARED_RED_MODEL : RED_MODEL;
            }

            @Override
            public Identifier getTextureResource(GeoRenderState renderState) {
                HayGolem.Color color = renderState.getOrDefaultGeckolibData(GolemRenderData.HAY_COLOR, HayGolem.Color.GREEN);
                return switch (renderState.getOrDefaultGeckolibData(GolemRenderData.CRACKINESS, Crackiness.Level.NONE)) {
                    case NONE, LOW -> color == HayGolem.Color.GREEN ? GREEN_TEXTURE_1 : RED_TEXTURE_1;
                    case MEDIUM -> color == HayGolem.Color.GREEN ? GREEN_TEXTURE_2 : RED_TEXTURE_2;
                    case HIGH -> color == HayGolem.Color.GREEN ? GREEN_TEXTURE_3 : RED_TEXTURE_3;
                };
            }
        });
    }

    @Override
    protected void addGolemRenderData(HayGolem golem, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.HAY_COLOR, golem.getColor());
        renderState.addGeckolibData(GolemRenderData.SHEARED, golem.isSheared());
    }
}
