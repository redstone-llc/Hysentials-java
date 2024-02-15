package llc.redstone.hysentials.updateGui

import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.IO
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.util.NetworkUtils
import llc.redstone.hysentials.utils.Utils
import llc.redstone.hysentials.websocket.Socket
import llc.redstone.hysentials.utils.RedstoneRepo
import llc.redstone.hysentials.utils.UpdateNotes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.minecraft.client.Minecraft
import net.minecraft.util.Util
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion
import org.json.JSONObject
import java.awt.Desktop
import java.io.File

class UpdateChecker {
    companion object {
        lateinit var instance: UpdateChecker
        val updateGetter = UpdateGetter()

        fun checkUpdateAndOpenMenu() {
            try {
                IO.launch {
                    updateGetter.run()
                    if (updateGetter.updateObj != null) {
                        Minecraft.getMinecraft().addScheduledTask {
                            Minecraft.getMinecraft().displayGuiScreen(RequestUpdateGui(inGame = true, deleteOld = true))
                        }
                    }
                }
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
        }

        fun installFromUrl(data: RedstoneRepo, notes: UpdateNotes) {
            IO.launch {
                updateGetter.updateObj = data
                updateGetter.updateNotes = notes
                if (updateGetter.updateObj != null) {
                    Minecraft.getMinecraft().addScheduledTask {
                        Minecraft.getMinecraft().displayGuiScreen(RequestUpdateGui(inGame = true, deleteOld = false))
                    }
                }
            }
        }
    }

    val updateAsset
        get() = updateGetter.updateObj!!
    val updateDownloadURL: String
        get() = updateGetter.updateObj!!.downloadUrl!!

    fun getJarNameFromUrl(url: String): String {
        return url.split(Regex("/")).last()
    }

    fun scheduleCopyUpdateAtShutdown(jarName: String, deleteOld: Boolean = false) {
        Runtime.getRuntime().addShutdownHook(Thread {
            try {
                println("Attempting to apply Hysentials update.")
                val oldJar = Hysentials.jarFile
                if (oldJar == null || !oldJar.exists() || oldJar.isDirectory) {
                    println("Old jar file not found.")
                    return@Thread
                }
                println("Copying updated jar to mods.")
                val newJar = File(File(Hysentials.modDir, "updates"), jarName)
                println("Copying to mod folder")
                val nameNoExtension = jarName.substringBeforeLast(".")
                val newExtension = jarName.substringAfterLast(".")
                val newLocation = File(
                    oldJar.parent,
                    "${if (oldJar.name.startsWith("!")) "!" else ""}${nameNoExtension}${if (oldJar.endsWith(".temp.jar") && newExtension == oldJar.extension) ".temp.jar" else ".$newExtension"}"
                )
                newLocation.createNewFile()
                newJar.copyTo(newLocation, true)
                newJar.delete()
                if (deleteOld && oldJar.delete()) {
                    println("successfully deleted the files. skipping install tasks")
                    return@Thread
                }

                if (!deleteOld) {
                    println("Skipping delete task")
                    return@Thread
                }

                println("Running delete task")
                val taskFile = File(File(Hysentials.modDir, "updates"), "tasks").listFiles()?.last()
                if (taskFile == null) {
                    println("Task doesn't exist")
                    return@Thread
                }
                val runtime = Utils.getJavaRuntime()
                if (Util.getOSType() == Util.EnumOS.OSX) {
                    val sipStatus = Runtime.getRuntime().exec("csrutil status")
                    sipStatus.waitFor()
                    if (!sipStatus.inputStream.use { it.bufferedReader().readText() }
                            .contains("System Integrity Protection status: disabled.")) {
                        println("SIP is NOT disabled, opening Finder.")
                        Desktop.getDesktop().open(oldJar.parentFile)
                        return@Thread
                    }
                }
                println("Using runtime $runtime")
                Runtime.getRuntime().exec("\"$runtime\" -jar \"${taskFile.absolutePath}\" \"${oldJar.absolutePath}\"")
                println("Successfully applied Hysentials update.")
            } catch (ex: Throwable) {
                println("Failed to apply Hysentials Update.")
                ex.printStackTrace()
            }
        })
    }

    fun downloadDeleteTask() {
        IO.launch {
            println("Checking for Hysentials install task...")
            val taskDir = File(File(Hysentials.modDir, "updates"), "tasks")
            // TODO Make this dynamic and fetch latest one or something
            val url =
                "https://github.com/Skytils/SkytilsMod-Data/releases/download/files/SkytilsInstaller-1.1.1.jar"
            val taskFile = File(taskDir, getJarNameFromUrl(url))
            if (taskDir.mkdirs() || withContext(Dispatchers.IO) {
                    taskFile.createNewFile()
                }) {
                println("Downloading Hysentials delete task.")

                if (!NetworkUtils.downloadFile(url, taskFile)) {
                    println("Downloading delete task failed!")
                } else {
                    println("Delete task successfully downloaded!")
                }
            }
        }
    }

    init {
        instance = this
        try {
            IO.launch {
                updateGetter.run()
            }
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
    }

//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    fun onGuiOpen(e: GuiOpenEvent) {
//        if (e.gui !is GuiMainMenu) return
//        if (updateGetter.updateObj == null) return
//        if (UpdateGui.complete) return
//        Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(RequestUpdateGui(false))
//    }

    class UpdateGetter {
        @Volatile
        var updateObj: RedstoneRepo? = null

        @Volatile
        var updateNotes: UpdateNotes? = null

        suspend fun run() {
            println("Checking for updates...")
            val latestRelease = when (HysentialsConfig.updateChannel) {
                1 -> let {
                    val req = NetworkUtils.getString( //beta
                        "https://backend.redstone.llc/api/update?type=beta"
                    )
                    if (req == null) {
                        println("Failed to get beta update")
                        return@let null
                    }
                    val body = Json.decodeFromString<RedstoneRepo>(req)
                    body.type = "beta"
                    body.downloadUrl =
                        "https://backend.redstone.llc/api/update/file?type=beta&uuid=${Minecraft.getMinecraft().session.profile.id}&key=${Socket.serverId}"
                    body
                }

                2 -> {
                    let {
                        val req = NetworkUtils.getString( //dev
                            "https://backend.redstone.llc/api/update?type=dev"
                        )
                        if (req == null) {
                            println("Failed to get dev update")
                            return@let null
                        }
                        val body = Json.decodeFromString<RedstoneRepo>(req)
                        body.type = "dev"
                        body.downloadUrl =
                            "https://backend.redstone.llc/api/update/file?type=dev&uuid=${Minecraft.getMinecraft().session.profile.id}&key=${Socket.serverId}"
                        body
                    }
                }

                else -> return println("Update Channel set as none")
            } ?: return println("Failed to get update")
            val latestTag = latestRelease.name.substringAfter("Hysentials-").substringBefore(".jar")
            val currentTag = Hysentials.VERSION
            println("Current version: $currentTag Latest version: $latestTag")

            val currentVersion = SkytilsVersion(currentTag)
            val latestVersion = SkytilsVersion(latestTag)
            if (currentVersion < latestVersion) {
                updateObj = latestRelease
                println("Update available!")
                val req = NetworkUtils.getString( //dev
                    "https://backend.redstone.llc/api/updatenotes?name=${latestRelease.name}"
                ) ?: return
                JSONObject(req).let {
                    if (it.has("error")) {
                        return@let null
                    } else {
                        val body = Json.decodeFromString<UpdateNotes>(req)
                        body.notes = "A new update has been released in the ${listOf("release", "beta", "dev")[HysentialsConfig.updateChannel]} channel!\n\n${body.notes}\n\nYou can find the full changelog in the Discord.\nhttps://discord.gg/redstone"
                        updateNotes = body
                    }
                }
            }
        }
    }

    class SkytilsVersion(val versionString: String) : Comparable<SkytilsVersion> {

        companion object {
            val regex = Regex("^(?<version>[\\d.]+)-?(?<type>\\D+)?(?<typever>\\d+\\.?\\d*)?\$")
        }

        private val matched by lazy { regex.find(versionString) }
        val isSafe by lazy { matched != null }

        val version by lazy { matched!!.groups["version"]!!.value }
        val versionArtifact by lazy { DefaultArtifactVersion(matched!!.groups["version"]!!.value) }
        val specialVersionType by lazy {
            val typeString = matched!!.groups["type"]?.value ?: return@lazy UpdateType.RELEASE

            return@lazy UpdateType.values().find { typeString == it.prefix } ?: UpdateType.UNKNOWN
        }
        val specialVersion by lazy {
            if (specialVersionType == UpdateType.RELEASE) return@lazy null
            return@lazy matched!!.groups["typever"]?.value?.toDoubleOrNull()
        }

        private val stringForm by lazy {
            "SkytilsVersion(versionString='$versionString', isSafe=$isSafe, version='$version', versionArtifact=$versionArtifact, specialVersionType=$specialVersionType, specialVersion=$specialVersion)"
        }

        override fun compareTo(other: SkytilsVersion): Int {
            if (!isSafe) return Int.MAX_VALUE
            if (!other.isSafe) return Int.MIN_VALUE
            if (other.specialVersionType != this.specialVersionType) return Int.MIN_VALUE
            return if (versionArtifact.compareTo(other.versionArtifact) == 0) {
                if (specialVersionType.ordinal == other.specialVersionType.ordinal) {
                    (specialVersion ?: 0.0).compareTo(other.specialVersion ?: 0.0)
                } else other.specialVersionType.ordinal - specialVersionType.ordinal
            } else versionArtifact.compareTo(other.versionArtifact)
        }

        override fun toString(): String = stringForm

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is SkytilsVersion) return false

            return versionString == other.versionString
        }

        override fun hashCode(): Int {
            return versionString.hashCode()
        }
    }

    enum class UpdateType(val prefix: String) {
        UNKNOWN("unknown"),
        RELEASE(""),
        DEV("dev"),
        BETA("beta"),
    }
}
