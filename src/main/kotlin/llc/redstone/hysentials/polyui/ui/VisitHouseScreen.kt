package llc.redstone.hysentials.polyui.ui

import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.guis.polyui.LwjglManagerImpl
import llc.redstone.hysentials.guis.polyui.PolyUIScreen
import llc.redstone.hysentials.polyui.Fonts.minecraftReg
import llc.redstone.hysentials.polyui.polyUI
import llc.redstone.hysentials.util.BUtils
import llc.redstone.hysentials.util.C
import net.minecraft.client.Minecraft
import org.polyfrost.polyui.PolyUI
import org.polyfrost.polyui.color.Colors
import org.polyfrost.polyui.component.events
import org.polyfrost.polyui.component.onClick
import org.polyfrost.polyui.component.setPalette
import org.polyfrost.polyui.component.withStates
import org.polyfrost.polyui.property.Settings
import org.polyfrost.polyui.renderer.data.Font
import org.polyfrost.polyui.renderer.data.PolyImage
import org.polyfrost.polyui.unit.Align
import org.polyfrost.polyui.unit.Vec2
import org.polyfrost.polyui.unit.by
import org.polyfrost.polyui.utils.LinkedList
import org.polyfrost.polyui.utils.MutablePair
import org.polyfrost.polyui.utils.radii
import org.polyfrost.polyui.utils.rgba

class VisitHouseScreen {
    companion object {
        @JvmStatic
        lateinit var INSTANCE: VisitHouseScreen;
    }

    var minecraftTen = Font("/assets/hysentials/fonts/Minecraft Ten.ttf")

    var setting = Settings()

    init {
        setting.debug = false
        setting.scrollMultiplier = Pair(-1f, -1f)
        INSTANCE = this
    }

    fun create(name: String = "Sin_ender", houses: List<Pair<String, Int>> = listOf()): PolyUI {
        val usernameImage = PolyImage(
            "https://mc-heads.net/head/$name/nohelm",
            type = PolyImage.Type.Raster
        ).apply {
            size = Vec2.Immutable(150f, 150f)
        }
        LwjglManagerImpl.INSTANCE.renderer.initImage(usernameImage)

        var calculatedHouse = mutableMapOf<Pair<String, Int>, LinkedList<MutablePair<String, Float>>>()

        if (houses.isNotEmpty()) {
            polyUI {
                size = 256f by 256f
                renderer = LwjglManagerImpl.INSTANCE.renderer
                settings = setting

                for (house in houses) {
                    calculatedHouse[house] = LinkedList()
                    calculate(house.first, calculatedHouse[house]!!) {
                        size = 256f by 256f
                        fontSize = 30f
                        font = minecraftReg
                    }
                }
            }
        }

        for ((house, textList) in calculatedHouse) {
            textList.forEach {
                it.first = it.first.trim()
            }
        }

        val ui = polyUI {
            size = 1200f by 500f
            renderer = LwjglManagerImpl.INSTANCE.renderer
            settings = setting

            text("$name'S HOUSES") {
                fontSize = 100f
                font = minecraftTen
                setPalette {
                    Colors.Palette(
                        rgba(255, 255, 255, 1f),
                        rgba(26, 34, 41),
                        rgba(14, 19, 23),
                        rgba(17, 23, 28, 0.5f),
                    )
                }
            }
            block(size = Vec2(1200f, 32f)) { block ->
                block.radii = 0f.radii()
                block.setPalette {
                    Colors.Palette(
                        rgba(255, 170, 0, 1f),
                        rgba(26, 34, 41),
                        rgba(14, 19, 23),
                        rgba(17, 23, 28, 0.5f),
                    )
                }
            }
            block(size = 1200f by 400f, at = 0f by 132f, alignment = Align(padding = 0f by 0f)) { block ->
                block.radii = 0f.radii()
                block.setPalette {
                    Colors.Palette(
                        rgba(0, 0, 0, 0.6f),
                        rgba(26, 34, 41),
                        rgba(14, 19, 23),
                        rgba(17, 23, 28, 0.5f),
                    )
                }

                spacer(1200f by 32f)
                group(alignment = Align(main = Align.Main.SpaceEvenly, padding = 96f by 48f, maxRowSize = 3)) {
                    it.visibleSize = 1200f by 288f
                    if (houses.isEmpty()) {
                        text("No houses found", alignment = Align(main = Align.Main.Center, padding = 0f by 0f)) {
                            font = minecraftReg
                            fontSize = 30f
                            setPalette {
                                Colors.Palette(
                                    rgba(255, 255, 255, 1f),
                                    rgba(26, 34, 41),
                                    rgba(14, 19, 23),
                                    rgba(17, 23, 28, 0.5f),
                                )
                            }
                        }
                    } else {
                        for ((house, textList) in calculatedHouse) {
                            box(size = 256f by 256f, alignment = Align(padding = 0f by 20f)) {
                                it.radii = 20f.radii()
                                it.setPalette {
                                    Colors.Palette(
                                        rgba(0, 0, 0, 0.3f),
                                        rgba(26, 34, 41),
                                        rgba(14, 19, 23),
                                        rgba(17, 23, 28, 0.5f),
                                    )
                                }
                                group(
                                    Align(
                                        main = Align.Main.SpaceEvenly,
                                        cross = Align.Cross.Center,
                                        mode = Align.Mode.Vertical,
                                        padding = 0f by 6f
                                    )
                                ) {
                                    it.size = 256f by 256f
                                    image(
                                        usernameImage
                                    ) {
                                        size = 125f by 125f
                                    }

                                    colorText(textList.first().first) {
                                        font = minecraftReg
                                        fontSize = 30f
                                        size = 256f by 256f
                                    }

                                    colorText(if (textList.size > 1) textList[1].first else "") {
                                        font = minecraftReg
                                        fontSize = 30f
                                        size = 256f by 256f
                                    }


                                    text("${house.second} Playing") {
                                        font = minecraftReg
                                        fontSize = 20f
                                        setPalette {
                                            Colors.Palette(
                                                rgba(255, 255, 255, 1f),
                                                rgba(26, 34, 41),
                                                rgba(14, 19, 23),
                                                rgba(17, 23, 28, 0.5f),
                                            )

                                        }
                                    }
                                }
                            }.onClick {
                                Minecraft.getMinecraft().thePlayer.sendChatMessage("/visit $name ${C.removeColor(house.first)}")
                            }.withStates()
                        }
                    }
                }.add()
            }

        }
        return ui
    }

    fun open(name: String = "Sin_ender", houses: List<Pair<String, Int>> = listOf()) {
        var ui = create(name, houses)
        var screen = PolyUIScreen(ui)
        Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(screen)
    }
}