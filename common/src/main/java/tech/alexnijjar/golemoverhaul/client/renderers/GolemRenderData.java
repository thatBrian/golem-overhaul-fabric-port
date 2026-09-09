package tech.alexnijjar.golemoverhaul.client.renderers;

import com.geckolib.constant.DataTickets;
import com.geckolib.constant.dataticket.DataTicket;
import net.minecraft.world.entity.Crackiness;
import tech.alexnijjar.golemoverhaul.common.entities.golems.HayGolem;
import tech.alexnijjar.golemoverhaul.common.entities.golems.TerracottaGolem;

/**
 * GeckoLib 5 renders from an immutable render state rather than the live entity, so every piece of golem
 * state the models and layers need is captured into these tickets during state extraction.
 */
public final class GolemRenderData {

    public static final DataTicket<Crackiness.Level> CRACKINESS = DataTickets.create("golemoverhaul_crackiness", Crackiness.Level.class);
    public static final DataTicket<Float> HEALTH_FRACTION = DataTickets.create("golemoverhaul_health_fraction", Float.class);
    public static final DataTicket<Boolean> HEAD_LOCKED = DataTickets.create("golemoverhaul_head_locked", Boolean.class);

    public static final DataTicket<Boolean> LIT = DataTickets.create("golemoverhaul_lit", Boolean.class);
    public static final DataTicket<Boolean> CHARGED = DataTickets.create("golemoverhaul_charged", Boolean.class);
    public static final DataTicket<Boolean> GILDED = DataTickets.create("golemoverhaul_gilded", Boolean.class);
    public static final DataTicket<Integer> SUMMONING_TICKS = DataTickets.create("golemoverhaul_summoning_ticks", Integer.class);
    public static final DataTicket<Boolean> FULL_OF_HONEY = DataTickets.create("golemoverhaul_full_of_honey", Boolean.class);
    public static final DataTicket<Boolean> SHEARED = DataTickets.create("golemoverhaul_sheared", Boolean.class);
    public static final DataTicket<HayGolem.Color> HAY_COLOR = DataTickets.create("golemoverhaul_hay_color", HayGolem.Color.class);
    public static final DataTicket<Boolean> SLIME_LARGE = DataTickets.create("golemoverhaul_slime_large", Boolean.class);
    public static final DataTicket<TerracottaGolem.Type> TERRACOTTA_TYPE = DataTickets.create("golemoverhaul_terracotta_type", TerracottaGolem.Type.class);

    private GolemRenderData() {}
}
