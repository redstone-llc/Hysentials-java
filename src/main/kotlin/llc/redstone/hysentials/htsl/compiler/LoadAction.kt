package llc.redstone.hysentials.htsl.compiler

import llc.redstone.hysentials.handlers.htsl.Queue.*
import llc.redstone.hysentials.htsl.actions.*
import llc.redstone.hysentials.htsl.actions.Types.*
import llc.redstone.hysentials.htsl.*
import llc.redstone.hysentials.utils.Sound
import llc.redstone.hysentials.utils.checkLimits
import java.awt.Menu

fun loadAction(script: MutableList<ActionObj>): Any {
    val limits = checkLimits(script)
    if (limits is String) {
        println(limits)
        return false
    }
    script.forEach { container ->
        if (container.context.context != "DEFAULT") {
            add(returnToEditActions())
            add(close())

            val targetName = container.context.contextTarget.name ?: return@forEach
            when (container.context.context) {
                "FUNCTION" -> {
                    add(chat("/function edit $targetName", /* func = */ targetName, true))
                }

                "EVENT" -> {
                    add(chat("/eventactions", true))
                    add(option(targetName))
                }

                "COMMAND" -> {
                    add(chat("/customcommands", true))
                    add(option(if (targetName.startsWith("/")) targetName else "/$targetName"))
                }

                "NPC" -> {
                    add(gotoLoader(targetName))
                    add(click(12))
                }

                "BUTTON" -> {
                    add(gotoLoader(targetName))
                }

                "PAD" -> {
                    add(gotoLoader(targetName))
                }
            }
        }

        for (action in container.actions) {
            add(click(50))
            add(
                option(
                    Action.actions.find { it.enum == Actions.valueOf(action!!.type.uppercase()) }?.enum?.actionName
                        ?: continue
                )
            )
            importComponent(action, Action.actions.find { it.enum == Actions.valueOf(action!!.type.uppercase()) }!!)
            add(returnToEditActions())
        }
    }
    add(done())
    return true
}

fun getSlotFromIndex(index: Int): Int {
    var value = 10
    for (key in 0..20) {
        if (key == index) return value
        value += if (key == 6 || key == 13) 3 else 1
    }
    return 10
}

fun updateAction(index: Int, comp: Component) {
    val menu = Action.actions.find { it.enum == Actions.valueOf(comp.type.uppercase()) }!!
    if (comp.parentIndex != null) {
        val parent = comp.parentIndex!!.split("/")
        val page = (parent[0].toInt() / 21f).toInt()
        val slot = parent[0].toInt() % 21
        add(page(page)) // Go to the page where the parent is
        add(click(getSlotFromIndex(slot))) // Click the parent
        add(click(parent[1].toInt() + 10)) // Click either conditions, if_actions, else_actions, or sub_actions
        add(click(getSlotFromIndex(parent[2].toInt()))) // Click the action
        importComponent(comp, menu)
        add(returnToEditActions())
        add(back())
    } else {
        add(click(getSlotFromIndex(index))) // Click the action
        importComponent(comp, menu)
    }
    add(returnToEditActions())
}

fun importComponent(comp: Component?, menu: Menus) {
    if (comp == null) return
    add(setGuiContext(comp.type))
    comp.entries.forEach { (key, value) ->
        if (key == "type") return@forEach
        val option = menu.options.find { it.name == key } ?: return@forEach
        if (option.defaultValue?.equals(value) == true) return@forEach
        add(click(option.slot))
        when (option.type) {
            CHAT_INPUT -> {
                add(chat(value.toString()))
            }

            ANVIL_INPUT -> {
                add(anvil(value.toString()))
            }

            CONDITIONS -> {
                if (value is MutableList<*>) {
                    for (comp2 in value) {
                        if (comp2 is Component) {
                            add(click(50))
                            add(
                                option(
                                    Condition.conditions.find { it.enum == Conditions.valueOf(comp2.type.uppercase()) }?.enum?.conditionName
                                        ?: continue
                                )
                            )
                            importComponent(
                                comp2,
                                Condition.conditions.find { it.enum == Conditions.valueOf(comp2.type.uppercase()) }!!
                            )
                            add(returnToEditActions())
                        }
                    }
                    add(back())
                }
            }

            STATIC_OPTION_SELECT -> {
                add(option(option.options?.find { it.lowercase() == value.toString().replace("_", " ").lowercase() }!!))
            }

            DYNAMIC_OPTION_SELECT -> {
                add(option(value.toString()))
            }

            LOCATION -> {
                if (value is Component) {
                    if (value.type.lowercase().replace(" ", "_") == "custom_coordinates") {
                        add(click(13))
                        add(anvil(value["coords"].toString()))
                    } else {
                        val index = arrayOf(
                            "house_spawn",
                            "current_location",
                            "invokers_location"
                        ).indexOf(value.type.lowercase().replace(" ", "_"))
                        if (index != -1) {
                            add(click(10 + index))
                        }
                    }
                }
            }

            SUB_ACTIONS -> {
                if (value is MutableList<*>) {
                    for (comp2 in value) {
                        if (comp2 is Component) {
                            add(click(50))
                            add(
                                option(
                                    Action.actions.find { it.enum == Actions.valueOf(comp2.type.uppercase()) }?.enum?.actionName
                                        ?: continue
                                )
                            )
                            importComponent(
                                comp2,
                                Action.actions.find { it.enum == Actions.valueOf(comp2.type.uppercase()) }!!
                            )
                            add(returnToEditActions())
                        }
                    }
                    add(back())
                }
            }

            ITEM -> {
                //One day
            }

            ENCHANTMENT -> {
                if (value is Number) {
                    if (value.toInt() < 50) add(click(value.toInt() + 10)) else {
                        click(53)
                        click(value.toInt() - 40)
                    }
                }
            }

            SOUND -> {
                add(click(48))
                add(chat(Sound.fromName(value.toString()).getSoundPath()))
            }

            SLOT -> {
                if (Regex("(%.*%)|(\\d+)").matches(value.toString())) {
                    add(click(8))
                    add(anvil(value.toString()))
                } else {
                    add(option(value.toString()))
                }
            }

            else -> {} //Do nothing
        }
    }
}