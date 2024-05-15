package llc.redstone.hysentials.utils.animation

enum class KeyframeType {
    LINEAR, // For now we only support linear
    SMOOTH,
    STEP,
    BEZIER
}

class Keyframe (
    val type: KeyframeType,
    val time: Float,
    var x: Float = 0F,
    var y: Float = 0F,
    var z: Float = 0F,
    var rotateX: Float = 0F,
    var rotateY: Float = 0F,
    var rotateZ: Float = 0F
){
    companion object {
        @JvmStatic
        fun create (time: Double, x: Double, y: Double, z: Double, rotateX: Double, rotateY: Double, rotateZ: Double): Keyframe {
            return Keyframe(KeyframeType.LINEAR, time.toFloat(), x.toFloat(), y.toFloat(), z.toFloat(), rotateX.toFloat(), rotateY.toFloat(), rotateZ.toFloat())
        }
    }

    fun interpolate(next: Keyframe, time: Float): Keyframe {
        val t = (time - this.time) / (next.time - this.time)

        return Keyframe(
            type,
            time,
            (x + (next.x - x) * t),
            (y + (next.y - y) * t),
            (z + (next.z - z) * t),
            (rotateX + (next.rotateX - rotateX) * t),
            (rotateY + (next.rotateY - rotateY) * t),
            (rotateZ + (next.rotateZ - rotateZ) * t)
        )
    }
}