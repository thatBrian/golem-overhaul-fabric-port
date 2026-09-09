package tech.alexnijjar.golemoverhaul.common.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;

public class ModEntityTypeTags {

    public static final TagKey<EntityType<?>> HONEY_IMMUNE = tag("honey_immune");

    private static TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, name));
    }
}
