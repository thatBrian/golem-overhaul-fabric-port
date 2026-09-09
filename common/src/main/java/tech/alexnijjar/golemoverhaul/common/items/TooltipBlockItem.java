package tech.alexnijjar.golemoverhaul.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

/**
 * A block item with a fixed tooltip line. Upstream put these tooltips on the blocks themselves,
 * but 26.1 removed Block#appendHoverText, so the item carries it instead.
 */
public class TooltipBlockItem extends BlockItem {

    private final Component tooltip;

    public TooltipBlockItem(Block block, Item.Properties properties, Component tooltip) {
        super(block, properties);
        this.tooltip = tooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, builder, flag);
        builder.accept(this.tooltip);
    }
}
