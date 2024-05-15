package llc.redstone.hysentials.htsl.actions

import llc.redstone.hysentials.htsl.actions.Types.*

class Option(
    val name: String,
    val slot: Int,
    val defaultValue: Any?,
    val type: Types,
    val options: List<String>? = null,
)

enum class Types {
    CHAT_INPUT,
    STATIC_OPTION_SELECT,
    ANVIL_INPUT,
    CONDITIONS,
    SUB_ACTIONS,
    TOGGLE,
    SOUND,
    LOCATION,
    ITEM,
    SLOT,
    DYNAMIC_OPTION_SELECT,
    ENCHANTMENT
}

abstract class Menus (
    open val options: List<Option> = emptyList(),
) {
    abstract fun getName(): String
    abstract fun getGUIName(): String
    abstract fun getSyntax(): String
    fun getKeyword(): String {
        return getSyntax().split(" ")[0]
    }
}

class Action(
    val enum: Actions,
    override val options: List<Option> = emptyList(),
): Menus() {
    companion object {
        val actions = mutableListOf<Action>().let {
            it.add(Action(Actions.CHANGE_STAT,
                listOf(
                    Option("stat", 10, "Kills", CHAT_INPUT),
                    Option("mode", 11, "INCREMENT", STATIC_OPTION_SELECT, listOf(
                        "Increment",
                        "Decrement",
                        "Set",
                        "Multiply",
                        "Divide"
                    )),
                    Option("amount", 12, 1, ANVIL_INPUT)
                )
            ))
            it.add(Action(Actions.CONDITIONAL,
                listOf(
                    Option("conditions", 10, emptyArray<Any>(), CONDITIONS), //TODO: Array of Conditions
                    Option("match_any_condition", 11, false, TOGGLE),
                    Option("if_actions", 12, emptyArray<Action>(), SUB_ACTIONS),
                    Option("else_actions", 13, emptyArray<Action>(), SUB_ACTIONS)
                )
            ))
            it.add(Action(Actions.SEND_MESSAGE,
                listOf(
                    Option("message", 10, "Hello!", CHAT_INPUT)
                )
            ))
            it.add(Action(Actions.PLAY_SOUND,
                listOf(
                    Option("sound", 10, null, SOUND),
                    Option("volume", 11, 0.7, ANVIL_INPUT),
                    Option("pitch", 12, 1, ANVIL_INPUT),
                    Option("location", 13, null, LOCATION)
                )
            ))
            it.add(Action(Actions.GIVE_ITEM,
                listOf(
                    Option("item", 10, null, ITEM),
                    Option("allow_multiple", 11, false, TOGGLE),
                    Option("inventory_slot", 12, -1, SLOT),
                    Option("replace_existing_item", 13, false, TOGGLE)
                )
            ))
            it.add(Action(Actions.TITLE,
                listOf(
                    Option("title", 10, "Hello World!", CHAT_INPUT),
                    Option("subtitle", 11, "", CHAT_INPUT),
                    Option("fade_in", 12, 1, ANVIL_INPUT),
                    Option("stay", 13, 5, ANVIL_INPUT),
                    Option("fade_out", 14, 1, ANVIL_INPUT)
                )
            ))
            it.add(Action(Actions.EXIT))
            it.add(Action(Actions.CHANGE_PLAYER_GROUP,
                listOf(
                    Option("group", 10, null, DYNAMIC_OPTION_SELECT),
                    Option("demotion_protection", 11, true, TOGGLE)
                )
            ))
            it.add(Action(Actions.KILL))
            it.add(Action(Actions.FULL_HEAL))
            it.add(Action(Actions.SPAWN))
            it.add(Action(Actions.ACTION_BAR,
                listOf(
                    Option("message", 10, "Hello World!", CHAT_INPUT)
                )
            ))
            it.add(Action(Actions.RESET_INVENTORY))
            it.add(Action(Actions.PARKOUR_CHECKPOINT))
            it.add(Action(Actions.REMOVE_ITEM,
                listOf(
                    Option("item", 10, null, ITEM)
                )
            ))
            it.add(Action(Actions.POTION_EFFECT,
                listOf(
                    Option("effect", 10, null, STATIC_OPTION_SELECT, listOf(
                        "Speed", "Slowness", "Haste", "Mining Fatigue", "Strength", "Instant Health", "Instant Damage", "Jump Boost", "Nausea", "Regeneration", "Resistance", "Fire Resistance", "Water Breathing", "Invisibility", "Blindness", "Night Vision", "Hunger", "Weakness", "Poison", "Wither", "Health Boost", "Absorption"
                    )),
                    Option("duration", 11, 60, ANVIL_INPUT),
                    Option("level", 12, 10, ANVIL_INPUT),
                    Option("override_existing_effects", 13, false, TOGGLE)
                )
            ))
            it.add(Action(Actions.CLOSE_MENU))
            it.add(Action(Actions.DISPLAY_MENU,
                listOf(
                    Option("menu", 10, null, DYNAMIC_OPTION_SELECT)
                )
            ))
            it.add(Action(Actions.CHANGE_TEAM_STAT,
                listOf(
                    Option("stat", 10, "Kills", CHAT_INPUT),
                    Option("mode", 11, "INCREMENT", STATIC_OPTION_SELECT, listOf(
                        "Increment", "Decrement", "Set", "Multiply", "Divide"
                    )),
                    Option("amount", 12, 1, ANVIL_INPUT),
                    Option("team", 13, "None", DYNAMIC_OPTION_SELECT)
                )
            ))
            it.add(Action(Actions.SET_PLAYER_TEAM,
                listOf(
                    Option("team", 10, "None", DYNAMIC_OPTION_SELECT)
                )
            ))
            it.add(Action(Actions.PAUSE,
                listOf(
                    Option("ticks_to_wait", 10, 20, ANVIL_INPUT)
                )
            ))
            it.add(Action(Actions.ENCHANT_HELD_ITEM,
                listOf(
                    Option("enchantment", 10, null, ENCHANTMENT),
                    Option("level", 11, 1, ANVIL_INPUT)
                )
            ))
            it.add(Action(Actions.APPLY_LAYOUT,
                listOf(
                    Option("layout", 10, null, DYNAMIC_OPTION_SELECT)
                )
            ))
            it.add(Action(Actions.TRIGGER_FUNCTION,
                listOf(
                    Option("function", 10, null, DYNAMIC_OPTION_SELECT),
                    Option("trigger_for_all_players", 11, false, TOGGLE)
                )
            ))
            it.add(Action(Actions.RANDOM_ACTION,
                listOf(
                    Option("actions", 10, emptyList<Any>(), SUB_ACTIONS)
                )
            ))
            it.add(Action(Actions.SET_GAMEMODE,
                listOf(
                    Option("gamemode", 10, null, STATIC_OPTION_SELECT, listOf(
                        "Adventure", "Survival", "Creative"
                    ))
                )
            ))
            it.add(Action(Actions.SET_COMPASS_TARGET,
                listOf(
                    Option("location", 10, null, LOCATION)
                )
            ))
            it.add(Action(Actions.FAIL_PARKOUR,
                listOf(
                    Option("reason", 10, "Failed!", CHAT_INPUT)
                )
            ))
            it.add(Action(Actions.FAIL_PARKOUR,
                listOf(
                    Option("reason", 10, "Failed!", CHAT_INPUT)
                )
            ))
            it.add(Action(Actions.TELEPORT_PLAYER,
                listOf(
                    Option("location", 10, null, LOCATION)
                )
            ))
            it.add(Action(Actions.CHANGE_GLOBAL_STAT,
                listOf(
                    Option("stat", 10, "Kills", CHAT_INPUT),
                    Option("mode", 11, "INCREMENT", STATIC_OPTION_SELECT, listOf(
                        "Increment", "Decrement", "Set", "Multiply", "Divide"
                    )),
                    Option("amount", 12, 1, ANVIL_INPUT)
                )
            ))
            it.add(Action(Actions.SEND_TO_LOBBY,
                listOf(
                    Option("location", 10, null, STATIC_OPTION_SELECT, listOf(
                        "Main Lobby", "Tournament Hall", "Blitz SG", "The TNT Games", "Mega Walls", "Arcade Games", "Cops and Crims", "UHC Champions", "Warlords", "Smash Heroes", "Housing", "SkyWars", "Speed UHC", "Classic Games", "Prototype", "Bed Wars", "Murder Mystery", "Build Battle", "Duels", "Wool Wars"
                    ))
                )
            ))
            it.add(Action(Actions.GIVE_EXP_LEVELS,
                listOf(
                    Option("levels", 10, 1, ANVIL_INPUT)
                )
            ))
            it.add(Action(Actions.CLEAR_EFFECTS))
            it.add(Action(Actions.SET_MAX_HEALTH,
                listOf(
                    Option("max_health", 10, 20, ANVIL_INPUT),
                    Option("mode", 11, "INCREMENT", STATIC_OPTION_SELECT, listOf(
                        "Increment", "Decrement", "Set", "Multiply", "Divide"
                    )),
                    Option("heal_on_change", 12, true, TOGGLE)
                )
            ))
            it.add(Action(Actions.CHANGE_HEALTH,
                listOf(
                    Option("health", 10, 20, ANVIL_INPUT),
                    Option("mode", 11, "INCREMENT", STATIC_OPTION_SELECT, listOf(
                        "Increment", "Decrement", "Set", "Multiply", "Divide"
                    ))
                )
            ))
            it.add(Action(Actions.CHANGE_HUNGER_LEVEL,
                listOf(
                    Option("hunger", 10, 20, ANVIL_INPUT),
                    Option("mode", 11, "INCREMENT", STATIC_OPTION_SELECT, listOf(
                        "Increment", "Decrement", "Set", "Multiply", "Divide"
                    ))
                )
            ))
            it.add(Action(Actions.BALANCE_PLAYER_TEAM))
            it.add(Action(Actions.CANCEL_EVENT))
            it.add(Action(Actions.CONSUME_ITEM))

            it
        }
    }

    override fun getName(): String {
        return enum.name
    }

    override fun getGUIName(): String {
        return enum.actionName
    }

    override fun getSyntax(): String {
        return enum.syntax
    }
}