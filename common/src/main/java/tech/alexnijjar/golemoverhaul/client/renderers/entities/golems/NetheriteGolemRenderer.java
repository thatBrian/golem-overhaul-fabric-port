package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems;

import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemModel;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base.BaseGolemRenderer;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers.NetheriteGolemFireLayer;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers.NetheriteGolemGoldLayer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.NetheriteGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class NetheriteGolemRenderer extends BaseGolemRenderer<NetheriteGolem> {

    public static final Identifier GOLD_OVERLAY = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/netherite/netherite_golem_gold_overlay.png");
    public static final Identifier CHARGED_OVERLAY = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/netherite/netherite_golem_charged_glow.png");
    public static final Identifier CHARGED_OVERLAY_OPEN = Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "textures/entity/netherite/netherite_golem_charged_glow_open.png");

    public NetheriteGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BaseGolemModel<>(ModEntityTypes.NETHERITE_GOLEM, true, 10));
        this.withScale(1.2f); // Joosh said he made the model too small.

        withRenderLayer(new NetheriteGolemGoldLayer(this));
        withRenderLayer(new NetheriteGolemFireLayer(this));
    }

    @Override
    protected void addGolemRenderData(NetheriteGolem golem, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(GolemRenderData.CHARGED, golem.isCharged());
        renderState.addGeckolibData(GolemRenderData.GILDED, golem.isGilded());
        renderState.addGeckolibData(GolemRenderData.SUMMONING_TICKS, golem.getSummoningTicks());
    }

    // The netherite golem plays its own death animation instead of the vanilla topple.
    @Override
    protected float getDeathMaxRotation(GeoRenderState renderState) {
        return 0;
    }
}
