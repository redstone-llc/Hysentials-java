package cc.woverflow.hysentials.gui

import cc.woverflow.hysentials.Hysentials
import cc.woverflow.hysentials.HysentialsKt
import cc.woverflow.hysentials.config.HysentialsConfig
import cc.woverflow.hysentials.util.NetworkUtils
import cc.woverflow.hysentials.utils.Utils
import cc.woverflow.hysentials.websocket.Socket
import cc.woverflow.hysentials.utils.RedstoneRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.util.Util
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion
import java.awt.Desktop
import java.io.File

object UpdateChecker {
    val updateGetter = UpdateGetter()
    val updateAsset
        get() = updateGetter.updateObj!!
    val updateDownloadURL: String
        get() = updateGetter.updateObj!!.downloadUrl!!

    fun getJarNameFromUrl(url: String): String {
        return url.split(Regex("/")).last()
    }

    fun scheduleCopyUpdateAtShutdown(jarName: String) {
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
                if (oldJar.delete()) {
                    println("successfully deleted the files. skipping install tasks")
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
        HysentialsKt.IO.launch {
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
        try {
            HysentialsKt.IO.launch {
                updateGetter.run()
            }
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onGuiOpen(e: GuiOpenEvent) {
        if (e.gui !is GuiMainMenu) return
        if (updateGetter.updateObj == null) return
        if (UpdateGui.complete) return
        Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(RequestUpdateGui())
    }

    class UpdateGetter {
        @Volatile
        var updateObj: RedstoneRepo? = null

        suspend fun run() {
            println("Checking for updates...")
            val latestRelease = when (HysentialsConfig.updateChannel) {
                1 -> let {
                    val req = NetworkUtils.getString( //beta
                        "https://hysentials.redstone.llc/api/update?type=beta"
                    )
                    if (req == null) {
                        println("Failed to get beta update")
                        return@let null
                    }
                    val body = Json.decodeFromString<RedstoneRepo>(req)
                    body.type = "beta"
                    body.downloadUrl = "https://hysentials.redstone.llc/api/update/file?type=beta&uuid=${Minecraft.getMinecraft().session.profile.id.toString()}&key=${Socket.serverId}"
                    body
                }

                2 -> {
                    let {
                        val req = NetworkUtils.getString( //dev
                            "https://hysentials.redstone.llc/api/update?type=dev"
                        )
                        if (req == null) {
                            println("Failed to get dev update")
                            return@let null
                        }
                        val body = Json.decodeFromString<RedstoneRepo>(req)
                        body.type = "dev"
                        body.downloadUrl = "https://hysentials.redstone.llc/api/update/file?type=dev&uuid=${Minecraft.getMinecraft().session.profile.id.toString()}&key=${Socket.serverId}"
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
