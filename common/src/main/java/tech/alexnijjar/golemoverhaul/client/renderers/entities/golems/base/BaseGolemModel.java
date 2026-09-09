package tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.base;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.EntityType;
import tech.alexnijjar.golemoverhaul.client.renderers.GolemRenderData;
import tech.alexnijjar.golemoverhaul.common.entities.golems.base.BaseGolem;

/**
 * Model paths follow GeckoLib 5's defaulted layout: {@code <ns>:entity/<name>} resolves to
 * {@code geo/entity/<name>.geo.json} and {@code animations/entity/<name>.animation.json}; textures are full paths.
 */
public class BaseGolemModel<T extends BaseGolem> extends GeoModel<T> {

    private final Identifier model;
    private final Identifier animation;
    private final Identifier texture;
    private final Identifier textureDamaged;
    private final Identifier textureVeryDamaged;
    private final boolean turnsHead;
    private final int maxHeadRotation;

    public BaseGolemModel(
        RegistryEntry<EntityType<T>> golem,
        boolean turnsHead,
        int maxHeadRotation
    ) {
        this(
            BaseGolemRenderer.name(golem),
            BaseGolemRenderer.texture(golem),
            BaseGolemRenderer.name(golem),
            turnsHead,
            maxHeadRotation
        );
    }

    public BaseGolemModel(
        Identifier model,
        Identifier texture,
        Identifier animation,
        boolean turnsHead,
        int maxHeadRotation
    ) {
        this(
            model.withPrefix("entity/"),
            texture.withPath("textures/entity/%s_1.png".formatted(texture.getPath())),
            texture.withPath("textures/entity/%s_2.png".formatted(texture.getPath())),
            texture.withPath("textures/entity/%s_3.png".formatted(texture.getPath())),
            animation.withPrefix("entity/"),
            turnsHead,
            maxHeadRotation
        );
    }

    public BaseGolemModel(
        Identifier model,
        Identifier texture,
        Identifier textureDamaged,
        Identifier textureVeryDamaged,
        Identifier animation,
        boolean turnsHead,
        int maxHeadRotation
    ) {
        this.model = model;
        this.texture = texture;
        this.textureDamaged = textureDamaged;
        this.textureVeryDamaged = textureVeryDamaged;
        this.animation = animation;
        this.turnsHead = turnsHead;
        this.maxHeadRotation = maxHeadRotation;
    }

    public boolean turnsHead() {
        return this.turnsHead;
    }

    public int maxHeadRotation() {
        return this.maxHeadRotation;
    }

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return switch (renderState.getOrDefaultGeckolibData(GolemRenderData.CRACKINESS, Crackiness.Level.NONE)) {
            case NONE, LOW -> this.texture;
            case MEDIUM -> this.textureDamaged;
            case HIGH -> this.textureVeryDamaged;
        };
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return this.animation;
    }
}
