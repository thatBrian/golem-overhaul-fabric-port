package tech.alexnijjar.golemoverhaul.client.renderers.entities.projectiles;

import com.geckolib.renderer.specialty.DirectionalProjectileRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import tech.alexnijjar.golemoverhaul.common.entities.projectiles.HoneyBlobProjectile;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

/**
 * GeckoLib 5 ships a projectile renderer that orients the model along its flight path, which is what the
 * hand-rolled yaw/pitch rotation upstream was doing.
 */
public class HoneyBlobProjectileRenderer extends DirectionalProjectileRenderer<HoneyBlobProjectile, EntityRenderState> {

    public HoneyBlobProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, ModEntityTypes.HONEY_BLOB.get());
    }

    @Override
    public @Nullable RenderType getRenderType(EntityRenderState renderState, Identifier texture) {
        return RenderTypes.entityTranslucent(texture);
    }
}
