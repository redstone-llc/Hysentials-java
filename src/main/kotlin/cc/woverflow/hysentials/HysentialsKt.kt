package cc.woverflow.hysentials

import cc.woverflow.hysentials.gui.UpdateChecker
import kotlinx.coroutines.*
import net.minecraftforge.common.MinecraftForge

class HysentialsKt {
    companion object {
        val IO = object : CoroutineScope {
            override val coroutineContext = Dispatchers.IO + SupervisorJob() + CoroutineName("Hysentials IO")
        }

        fun init() {
            MinecraftForge.EVENT_BUS.register(UpdateChecker())
        }

        fun postInit() {
            UpdateChecker.instance.downloadDeleteTask()
        }
    }


}