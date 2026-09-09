package tech.alexnijjar.golemoverhaul.client.renderers.entities.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import org.joml.Vector3f;
import tech.alexnijjar.golemoverhaul.common.entities.projectiles.CandleFlameProjectile;

/**
 * A camera-facing flame sprite. 26.1 replaced immediate-mode drawing with submitted render tasks,
 * so the quad is handed to the collector as custom geometry instead of pushed through a Tesselator.
 */
public class CandleFlameProjectileRenderer extends EntityRenderer<CandleFlameProjectile, EntityRenderState> {

    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/particle/flame.png");

    public CandleFlameProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucentEmissive(TEXTURE), (pose, buffer) -> {
            vertex(pose, buffer, -0.25f, -0.4f, 1, 0);
            vertex(pose, buffer, 0.25f, -0.4f, 0, 0);
            vertex(pose, buffer, 0.25f, 0.1f, 0, 1);
            vertex(pose, buffer, -0.25f, 0.1f, 1, 1);
        });
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float u, float v) {
        Vector3f position = pose.pose().transformPosition(x, y, 0, new Vector3f());
        buffer.addVertex(position.x(), position.y(), position.z())
            .setColor(-1)
            .setUv(u, v)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(LightCoordsUtil.FULL_BRIGHT)
            .setNormal(0, 1, 0);
    }
}
