package no.hegz.cogsquests.aethercompat;

import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cogs & Quests compat shim: MobStacker calls setCustomName(null) on any mob that owns a boss bar
 * (its "boss name reset"). The Aether's Slider and Valkyrie Queen mirror setCustomName into their
 * synced boss-name entity data, which then serialises as a null Component and disconnects every
 * client that loads the boss ("Received unexpected null component"). The mixins below make those
 * two bosses ignore a null boss name. Server-side only; nothing is registered on the network.
 */
@Mod(AetherCompat.MODID)
public class AetherCompat {
    public static final String MODID = "cogsquests_aethercompat";
    public static final Logger LOGGER = LoggerFactory.getLogger("cogsquests_aethercompat");

    public AetherCompat() {
        LOGGER.info("Cogs & Quests Aether/MobStacker compat loaded");
    }
}
