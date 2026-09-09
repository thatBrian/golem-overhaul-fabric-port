package tech.alexnijjar.golemoverhaul.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.builtin.ResourcefulBlockRegistry;
import com.teamresourceful.resourcefullib.common.registry.builtin.base.ItemLikeEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tech.alexnijjar.golemoverhaul.GolemOverhaul;
import tech.alexnijjar.golemoverhaul.common.blocks.CandleGolemBlock;
import tech.alexnijjar.golemoverhaul.common.blocks.ClayGolemStatueBlock;

public class ModBlocks {

    // ResourcefulBlockRegistry stamps the registry key onto the properties (required since 1.21.2).
    public static final ResourcefulBlockRegistry BLOCKS = ResourcefulRegistries.createForBlocks(GolemOverhaul.MOD_ID);

    public static final ItemLikeEntry<CandleGolemBlock> CANDLE_GOLEM_BLOCK = BLOCKS.register("candle_golem_block", CandleGolemBlock::new,
        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE).lightLevel(s -> s.getValue(CandleGolemBlock.LIT) ? 12 : 0));
    public static final ItemLikeEntry<ClayGolemStatueBlock> CLAY_GOLEM_STATUE = BLOCKS.register("clay_golem_statue", ClayGolemStatueBlock::new,
        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY).noOcclusion().randomTicks());
}
