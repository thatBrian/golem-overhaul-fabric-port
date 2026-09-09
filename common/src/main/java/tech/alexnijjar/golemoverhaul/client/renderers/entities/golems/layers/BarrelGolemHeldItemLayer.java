package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.layers;

import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.builtin.BlockAndItemGeoLayer;
import com.geckolib.util.RenderUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import tech.alexnijjar.golemoverhaul.common.entities.golems.BarrelGolem;

import java.util.List;

/**
 * Renders the emerald the barrel golem holds up while bartering, attached to the model's {@code item} bone.
 * GeckoLib 5's per-bone rendering positions the item at the bone, so the manual body-yaw math upstream needed is gone.
 */
public class BarrelGolemHeldItemLayer extends BlockAndItemGeoLayer<BarrelGolem, Void, LivingEntityRenderState> {

    private static final String ITEM_BONE = "item";

    public BarrelGolemHeldItemLayer(EntityRendererProvider.Context context, GeoRenderer<BarrelGolem, Void, LivingEntityRenderState> renderer) {
        super(context, renderer);
    }

    @Override
    protected List<RenderData> getRelevantBones(BarrelGolem golem, @Nullable Void relatedObject, LivingEntityRenderState renderState, float partialTick) {
        if (!golem.isBartering() || golem.getBarteringTicks() < 34) return List.of();
        if (golem.isDeadOrDying()) return List.of();
        ItemStack stack = golem.getMainHandItem();
        if (stack.isEmpty()) return List.of();

        ItemDisplayContext displayContext = ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
        return List.of(RenderData.item(ITEM_BONE, displayContext,
            RenderUtil.createRenderStateForItem(stack, this.itemModelResolver, displayContext, golem)));
    }

    @Override
    public void addRenderData(BarrelGolem golem, @Nullable Void relatedObject, LivingEntityRenderState renderState, float partialTick) {
        List<RenderData> contents = getRelevantBones(golem, relatedObject, renderState, partialTick);
        if (!contents.isEmpty()) {
            renderState.addGeckolibData(CONTENTS, contents);
        }
    }
}
