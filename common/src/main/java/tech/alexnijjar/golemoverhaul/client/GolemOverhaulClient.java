package tech.alexnijjar.golemoverhaul.client;

import com.geckolib.renderer.GeoEntityRenderer;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.golems.*;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.projectiles.CandleFlameProjectileRenderer;
import tech.alexnijjar.golemoverhaul.client.renderers.entities.projectiles.HoneyBlobProjectileRenderer;
import tech.alexnijjar.golemoverhaul.common.entities.golems.NetheriteGolem;
import tech.alexnijjar.golemoverhaul.common.entities.projectiles.MudBallProjectile;
import tech.alexnijjar.golemoverhaul.common.network.NetworkHandler;
import tech.alexnijjar.golemoverhaul.common.network.packets.ServerboundGolemSummonPacket;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

public class GolemOverhaulClient {

    // Key categories are registered objects since 1.21.9; the label comes from key.category.<namespace>.<path>.
    public static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(GolemOverhaul.MOD_ID, "golemoverhaul"));

    public static final KeyMapping KEY_NETHERITE_GOLEM_SUMMON = new KeyMapping(
        "key.golemoverhaul.netherite_golem_summon",
        InputConstants.KEY_R,
        KEY_CATEGORY);

    public static void init() {
        registerKeyMappings();
        registerEntityRenderers();
        // Block render layers are no longer registered: 26.1 picks cutout/translucent from the texture itself.
    }

    private static void registerKeyMappings() {
        KeyMappingRegistry.register(KEY_NETHERITE_GOLEM_SUMMON);

        ClientTickEvent.CLIENT_PRE.register(minecraft -> {
            if (KEY_NETHERITE_GOLEM_SUMMON.consumeClick()) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;
                if (player.getVehicle() instanceof NetheriteGolem) {
                    NetworkHandler.CHANNEL.sendToServer(new ServerboundGolemSummonPacket());
                }
            }
        });
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntityTypes.BARREL_GOLEM, BarrelGolemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.CANDLE_GOLEM, CandleGolemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.COAL_GOLEM, CoalGolemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.HAY_GOLEM, HayGolemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.HONEY_GOLEM, HoneyGolemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.KELP_GOLEM, KelpGolemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.NETHERITE_GOLEM, NetheriteGolemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.SLIME_GOLEM, SlimeGolemRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.TERRACOTTA_GOLEM, TerracottaGolemRenderer::new);

        EntityRendererRegistry.register(ModEntityTypes.CANDLE_FLAME, CandleFlameProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.MUD_BALL, context ->
                new GeoEntityRenderer<MudBallProjectile, EntityRenderState>(context, ModEntityTypes.MUD_BALL.get()));
        EntityRendererRegistry.register(ModEntityTypes.HONEY_BLOB, HoneyBlobProjectileRenderer::new);
    }
}
