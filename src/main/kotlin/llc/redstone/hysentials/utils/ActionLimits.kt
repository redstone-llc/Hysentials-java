package llc.redstone.hysentials.utils

import llc.redstone.hysentials.htsl.actions.Actions
import llc.redstone.hysentials.htsl.actions.Actions.*
import llc.redstone.hysentials.htsl.compiler.ActionObj
import llc.redstone.hysentials.htsl.compiler.Component
import llc.redstone.hysentials.htsl.compiler.Context
import llc.redstone.hysentials.htsl.compiler.ContextTarget

data class Limit(
    val action: Actions,
    val limit: Int
)

val limits = mutableListOf<Limit>(
    Limit(APPLY_LAYOUT, 5),
    Limit(POTION_EFFECT, 22),
    Limit(BALANCE_PLAYER_TEAM, 1),
    Limit(CANCEL_EVENT, 1),
    Limit(CHANGE_GLOBAL_STAT, 10),
    Limit(CHANGE_PLAYER_GROUP, 1),
    Limit(CHANGE_STAT, 10),
    Limit(CHANGE_TEAM_STAT, 10),
    Limit(CLEAR_EFFECTS, 5),
    Limit(CLOSE_MENU, 1),
    Limit(CONDITIONAL, 15),
    Limit(ACTION_BAR, 5),
    Limit(DISPLAY_MENU, 10),
    Limit(TITLE, 5),
    Limit(ENCHANT_HELD_ITEM, 23),
    Limit(EXIT, 1),
    Limit(FAIL_PARKOUR, 1),
    Limit(FULL_HEAL, 5),
    Limit(GIVE_EXP_LEVELS, 5),
    Limit(GIVE_ITEM, 20),
    Limit(SPAWN, 1),
    Limit(KILL, 1),
    Limit(PARKOUR_CHECKPOINT, 1),
    Limit(PLAY_SOUND, 25),
    Limit(PAUSE, 30),
    Limit(RANDOM_ACTION, 5),
    Limit(REMOVE_ITEM, 20),
    Limit(RESET_INVENTORY, 1),
    Limit(SEND_MESSAGE, 20),
    Limit(SEND_TO_LOBBY, 1),
    Limit(SET_COMPASS_TARGET, 5),
    Limit(SET_GAMEMODE, 1),
    Limit(CHANGE_HUNGER_LEVEL, 5),
    Limit(SET_MAX_HEALTH, 5),
    Limit(CHANGE_HEALTH, 5),
    Limit(SET_PLAYER_TEAM, 1),
    Limit(TELEPORT_PLAYER, 5),
    Limit(TRIGGER_FUNCTION, 10),
    Limit(CONSUME_ITEM, 1)
)

fun checkLimits(script: MutableList<ActionObj>): Any {
    val counts = mutableMapOf<String, MutableMap<String, Int>>()
    fun countAction(actions: MutableList<Component?>, context: Context, parentActionType: String) {
        if (actions.isEmpty()) return
        actions.forEach { action ->
            if (action?.type != null) {
                val first = "${context.context}_${context.contextTarget.name}"
                if (counts[first] == null) counts[first] = mutableMapOf()
                if (counts[first]!![action.type] == null) counts[first]!![action.type] = 0
                counts[first]!![action.type] = counts[first]!![action.type]!! + 1
                if (counts[first]!![action.type]!! > limits.find { it.action.name == action.type }!!.limit) {
                    throw Exception("Limit exceeded for ${action.type} in $first")
                }
            }
            arrayOf("if_actions", "else_actions", "actions").forEach {
                if (action!![it] != null) {
                    val subContext = Context(action.type, ContextTarget(it))
                    val subObj = ActionObj(
                        subContext,
                        mutableListOf(),
                        action[it]!! as MutableList<Component?>
                    )
                    val result = checkLimits(mutableListOf(subObj))
                    if (!(result is Boolean && result)) {
                        throw Exception(result as String)
                    }
                }
            }
        }
    }

    try {
        script.forEach { actionObj ->
            countAction(actionObj.actions, actionObj.context, actionObj.context.context)
        }
    } catch (e: Exception) {
        return e.message!!
    }

    return true
}
