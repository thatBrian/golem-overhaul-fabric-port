package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers;

import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.GeoRenderLayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.NetheriteGolemRenderer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.NetheriteGolem;

public class NetheriteGolemFireLayer extends GeoRenderLayer<NetheriteGolem, Void, LivingEntityRenderState> {

    public NetheriteGolemFireLayer(GeoRenderer<NetheriteGolem, Void, LivingEntityRenderState> renderer) {
        super(renderer);
    }

    @Override
    public void submitRenderTask(RenderPassInfo<LivingEntityRenderState> renderPassInfo, SubmitNodeCollector renderTasks) {
        if (!renderPassInfo.willRender()) return;
        LivingEntityRenderState renderState = renderPassInfo.renderState();
        if (!renderState.getOrDefaultGeckolibData(GolemRenderData.CHARGED, false)) return;

        int summoningTicks = renderState.getOrDefaultGeckolibData(GolemRenderData.SUMMONING_TICKS, 0);
        boolean openFlame = summoningTicks <= 43 && summoningTicks > 10;
        getRenderer().submitRenderTasks(renderPassInfo, renderTasks.order(1), RenderTypes.eyes(openFlame ?
            NetheriteGolemRenderer.CHARGED_OVERLAY_OPEN :
            NetheriteGolemRenderer.CHARGED_OVERLAY));
    }
}
