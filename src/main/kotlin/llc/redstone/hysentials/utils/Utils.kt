package llc.redstone.hysentials.utils

import llc.redstone.hysentials.utils.ColorFactory.web
import llc.redstone.hysentials.utils.RainbowColor.Companion.fromString
import net.minecraft.util.*
import java.awt.Color
import java.io.File
import java.io.IOException
import java.util.*


object Utils {

    @JvmStatic
    var random = Random()

    val isBSMod by lazy {
        val cal = Calendar.getInstance()
        return@lazy cal.get(Calendar.MONTH) == Calendar.APRIL && cal.get(Calendar.DAY_OF_MONTH) == 1
    }

    fun getBlocksWithinRangeAtSameY(center: BlockPos, radius: Int, y: Int): Iterable<BlockPos> {
        val corner1 = BlockPos(center.x - radius, y, center.z - radius)
        val corner2 = BlockPos(center.x + radius, y, center.z + radius)
        return BlockPos.getAllInBox(corner1, corner2)
    }

    /**
     * Checks if an object is equal to any of the other objects
     * @param object Object to compare
     * @param other Objects being compared
     * @return boolean
     */
    @JvmStatic
    fun equalsOneOf(`object`: Any?, vararg other: Any): Boolean {
        for (obj in other) {
            if (`object` == obj) return true
        }
        return false
    }

    fun customColorFromString(string: String?): CustomColor {
        if (string == null) throw NullPointerException("Argument cannot be null!")
        if (string.startsWith("rainbow(")) {
            return fromString(string)
        }
        return try {
            getCustomColorFromColor(web(string))
        } catch (e: IllegalArgumentException) {
            try {
                CustomColor.fromInt(string.toInt())
            } catch (ignored: NumberFormatException) {
                throw e
            }
        }
    }
    @JvmStatic
    fun getExpForLevel(level: Int): Int {
        var previous = 1000
        var amount = 0.0
        var multiplier = 1.1
        for (i in 0 until level) {
            previous = (previous + amount).toInt()
            amount = (100 * multiplier).toInt().toDouble()
            when {
                i % 20 == 0 && i < 50 -> multiplier += 0.1
                i % 5 == 0 && i > 100 -> multiplier += 0.1
                i % 2 == 0 && i > 150 -> multiplier += 0.1
            }
        }
        return previous
    }
    @JvmStatic
    fun getLevel(exp: Int): Float {
        var level = 0f
        var exp = exp
        while (exp >= getExpForLevel(level.toInt())) {
            level++
            exp -= getExpForLevel(level.toInt())
        }
        level += exp / getExpForLevel(level.toInt()).toFloat()
        return level
    }
    @JvmStatic
    fun getExpStart(exp: Int): Int {
        var level = 0f
        var remainingExp = exp
        while (remainingExp >= getExpForLevel(level.toInt())) {
            remainingExp -= getExpForLevel(level.toInt())
            level++
        }
        return remainingExp
    }


    private fun getCustomColorFromColor(color: Color) = CustomColor.fromInt(color.rgb)

    @Throws(IOException::class)
    fun getJavaRuntime(): String {
        val os = System.getProperty("os.name")
        val java = "${System.getProperty("java.home")}${File.separator}bin${File.separator}${
            if (os != null && os.lowercase().startsWith("windows")) "java.exe" else "java"
        }"
        if (!File(java).isFile) {
            throw IOException("Unable to find suitable java runtime at $java")
        }
        return java
    }
}