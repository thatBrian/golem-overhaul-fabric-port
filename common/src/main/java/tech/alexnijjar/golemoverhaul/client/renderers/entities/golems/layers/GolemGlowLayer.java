package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers;

import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import tech.alexnijjar.golemoverhaul.common.entities.golems.base.BaseGolem;

import java.util.function.Function;

/**
 * An emissive overlay driven by an explicit texture instead of GeckoLib's {@code _glowmask} convention.
 * Returning {@code null} from the texture function skips the layer for that frame.
 */
public class GolemGlowLayer<T extends BaseGolem> extends AutoGlowingGeoLayer<T, Void, LivingEntityRenderState> {

    private final Function<LivingEntityRenderState, @Nullable Identifier> texture;

    public GolemGlowLayer(GeoRenderer<T, Void, LivingEntityRenderState> renderer, Function<LivingEntityRenderState, @Nullable Identifier> texture) {
        super(renderer);
        this.texture = texture;
    }

    @Override
    protected Identifier getTextureResource(LivingEntityRenderState renderState) {
        Identifier resolved = this.texture.apply(renderState);
        return resolved == null ? MissingTextureAtlasSprite.getLocation() : resolved;
    }

    @Override
    public void submitRenderTask(RenderPassInfo<LivingEntityRenderState> renderPassInfo, SubmitNodeCollector renderTasks) {
        if (this.texture.apply(renderPassInfo.renderState()) == null) return;
        super.submitRenderTask(renderPassInfo, renderTasks);
    }
}
