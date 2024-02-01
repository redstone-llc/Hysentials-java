package llc.redstone.hysentials.utils

import cc.polyfrost.oneconfig.utils.gui.OneUIScreen
import net.minecraftforge.client.event.GuiScreenEvent

abstract class OneScreen (useMinecraftScale: Boolean) : OneUIScreen(useMinecraftScale) {
    abstract fun drawPost(event: GuiScreenEvent.DrawScreenEvent.Post)
}