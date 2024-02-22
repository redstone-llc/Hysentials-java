package llc.redstone.hysentials.utils.animation

import net.minecraft.client.model.ModelRenderer

class Animation(
    val model: ModelRenderer,
    val keyframes: List<Keyframe>,
    val children: List<Animation>? = null,
) {
    fun apply(time: Float) {
        var i = 0
        val keyframesSize = keyframes.size
        while (i < keyframesSize - 1) {
            val current = keyframes[i]
            val next = keyframes[i + 1]
            if (time > current.time && time < next.time) {
                val result = current.interpolate(next, time)
                model.rotateAngleZ = toRadians(result.rotateZ)
                model.rotateAngleY = toRadians(result.rotateY)
                model.rotateAngleX = toRadians(result.rotateX)
            }
            i++
        }

        children?.forEach { it.apply(time) }
    }

    fun toRadians(degrees: Float): Float {
        return degrees * Math.PI.toFloat() / 180
    }
}