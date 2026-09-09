package tech.alexnijjar.golemoverhaul.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tech.alexnijjar.golemoverhaul.common.entities.golems.TerracottaGolem;
import tech.alexnijjar.golemoverhaul.common.registry.ModEntityTypes;

import java.util.stream.Stream;

public class ClayGolemStatueBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<ClayGolemStatueBlock> CODEC = simpleCodec(ClayGolemStatueBlock::new);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape SHAPE_NORTH = Stream.of(
        Block.box(9.6, 0, 3, 12.6, 3, 6),
        Block.box(3.5, 0, 3, 6.5, 3, 6),
        Block.box(4, 0, 4, 12, 9, 12),
        Block.box(2.5, 0, 6, 4.5, 6, 10),
        Block.box(11.5, 0, 6, 13.5, 6, 10),
        Block.box(6.5, 2, 2.2, 9.5, 7, 4.2)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final VoxelShape SHAPE_EAST = Stream.of(
        Block.box(10, 0, 9.6, 13, 3, 12.6),
        Block.box(10, 0, 3.5, 13, 3, 6.5),
        Block.box(4, 0, 4, 12, 9, 12),
        Block.box(6, 0, 2.5, 10, 6, 4.5),
        Block.box(6, 0, 11.5, 10, 6, 13.5),
        Block.box(11.8, 2, 6.5, 13.8, 7, 9.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final VoxelShape SHAPE_SOUTH = Stream.of(
        Block.box(3.4, 0, 10, 6.4, 3, 13),
        Block.box(9.5, 0, 10, 12.5, 3, 13),
        Block.box(4, 0, 4, 12, 9, 12),
        Block.box(11.5, -0, 6, 13.5, 6, 10),
        Block.box(2.5, -0, 6, 4.5, 6, 10),
        Block.box(6.5, 2, 11.8, 9.5, 7, 13.8)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static final VoxelShape SHAPE_WEST = Stream.of(
        Block.box(3, 0, 3.4, 6, 3, 6.4),
        Block.box(3, 0, 9.5, 6, 3, 12.5),
        Block.box(4, 0, 4, 12, 9, 12),
        Block.box(6, 0, 11.5, 10, 6, 13.5),
        Block.box(6, 0, 2.5, 10, 6, 4.5),
        Block.box(2.2, 2, 6.5, 4.2, 7, 9.5)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public ClayGolemStatueBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        if (level.isClientSide()) return;
        if (level.hasNeighborSignal(pos)) {
            spawnGolem(level, pos, state);
            level.destroyBlock(pos, false);
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
            .setValue(WATERLOGGED, fluidState.getType().equals(Fluids.WATER));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // The statue has no occlusion, so the old "11 - light block" ice-melt threshold reduces to 11.
        if (level.getBrightness(LightLayer.BLOCK, pos) > 11) {
            spawnGolem(level, pos, state);
            return;
        }

        if (level.getBiome(pos).value().getBaseTemperature() > 1) {
            spawnGolem(level, pos, state);
        }
    }

    private void spawnGolem(Level level, BlockPos pos, BlockState state) {
        TerracottaGolem golem = ModEntityTypes.TERRACOTTA_GOLEM.get().create(level, EntitySpawnReason.TRIGGERED);
        if (golem == null) {
            return;
        }

        golem.setPlayerCreated();
        golem.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state.getValue(FACING).toYRot(), 0);
        level.addFreshEntity(golem);
        level.destroyBlock(pos, false);
    }
}
