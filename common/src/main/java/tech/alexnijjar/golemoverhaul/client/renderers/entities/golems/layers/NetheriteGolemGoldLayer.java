package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers;

import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.TextureLayerGeoLayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.NetheriteGolemRenderer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.NetheriteGolem;

public class NetheriteGolemGoldLayer extends TextureLayerGeoLayer<NetheriteGolem, Void, LivingEntityRenderState> {

    public NetheriteGolemGoldLayer(GeoRenderer<NetheriteGolem, Void, LivingEntityRenderState> renderer) {
        super(renderer, NetheriteGolemRenderer.GOLD_OVERLAY, RenderTypes::entityCutout);
    }

    @Override
    public void submitRenderTask(RenderPassInfo<LivingEntityRenderState> renderPassInfo, SubmitNodeCollector renderTasks) {
        if (!renderPassInfo.renderState().getOrDefaultGeckolibData(GolemRenderData.GILDED, false)) return;
        super.submitRenderTask(renderPassInfo, renderTasks);
    }
}
