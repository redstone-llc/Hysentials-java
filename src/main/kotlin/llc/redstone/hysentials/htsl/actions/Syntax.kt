package llc.redstone.hysentials.htsl.actions

enum class Actions(val syntax: String, val actionName: String) {
    ACTION_BAR("actionBar <message>", "Display Action Bar"),
    APPLY_LAYOUT("applyLayout <layout>", "Apply Inventory Layout"),
    BALANCE_PLAYER_TEAM("balanceTeam", "Balance Player Team"),
    FAIL_PARKOUR("failParkour <reason>", "Fail Parkour"),
    CANCEL_EVENT("cancelEvent", "Cancel Event"),
    CHANGE_GLOBAL_STAT("globalstat <stat> <mode> <amount>", "Change Global Stat"),
    CHANGE_HEALTH("changeHealth <mode> <amount> <heal_on_change>", "Change Health"),
    CHANGE_PLAYER_GROUP("changePlayerGroup <group> <demotion_protection>", "Change Player's Group"),
    CHANGE_STAT("stat <stat> <mode> <amount>", "Change Player Stat"),
    CHANGE_TEAM_STAT("teamstat <stat> <team> <mode> <amount>", "Change Team Stat"),
    CLEAR_EFFECTS("clearEffects", "Clear All Potion Effects"),
    CLOSE_MENU("closeMenu", "Close Menu"),
    CONDITIONAL("if <match_any_condition> (<conditions>) {\n<if_actions>\n} else {\n<else_actions>\n}", "Conditional"),
    DISPLAY_MENU("displayMenu <menu>", "Display Menu"),
    ENCHANT_HELD_ITEM("enchant <enchantment> <level>", "Enchant Held Item"),
    EXIT("exit", "Exit"),
    FULL_HEAL("fullHeal", "Full Heal"),
    GIVE_EXP_LEVELS("xpLevel <levels>", "Give Experience Levels"),
    GIVE_ITEM("giveItem <item> <allow_multiple> <inventory_slot> <replace_existing_item>", "Give Item"),
    KILL("kill", "Kill Player"),
    PARKOUR_CHECKPOINT("parkCheck", "Parkour Checkpoint"),
    PAUSE("pause <ticks_to_wait>", "Pause Execution"),
    PLAY_SOUND("sound <sound> <volume> <pitch> <location>", "Play Sound"),
    POTION_EFFECT("applyPotion <effect> <duration> <level> <override_existing_effects>", "Apply Potion Effect"),
    RANDOM_ACTION("random {\n<actions>\n}", "Random Action"),
    REMOVE_ITEM("removeItem <item>", "Remove Item"),
    RESET_INVENTORY("resetInventory", "Reset Inventory"),
    SEND_MESSAGE("chat <message>", "Send a Chat Message"),
    SEND_TO_LOBBY("lobby <location>", "Send to Lobby"),
    SET_COMPASS_TARGET("compassTarget <location>", "Set Compass Target"),
    SET_GAMEMODE("gamemode <gamemode>", "Set Gamemode"),
    CHANGE_HUNGER_LEVEL("hungerLevel <mode> <hunger>", "Change Hunger Level"),
    SET_MAX_HEALTH("maxHealth <mode> <max_health> <heal_on_change>", "Change Max Health"),
    SET_PLAYER_TEAM("setTeam <team>", "Set Player Team"),
    SPAWN("houseSpawn", "Go to House Spawn"),
    TELEPORT_PLAYER("tp <location>", "Teleport Player"),
    TITLE("title <title> <subtitle> <fade_in> <stay> <fade_out>", "Display Title"),
    TRIGGER_FUNCTION("function <function> <trigger_for_all_players>", "Trigger Function"),
    CONSUME_ITEM("consumeItem", "Use/Remove Held Item");

    val keyword = syntax.split(" ")[0]

    companion object {
        fun getFromKeyword(keyword: String): Actions? {
            for (action in values()) {
                if (action.keyword == keyword) {
                    return action
                }
            }
            return null
        }
    }
}

enum class Conditions(val syntax: String, val conditionName: String) {
    BLOCK_TYPE("blockType <item> <match_type_only>", "Block Type"),
    CAN_PVP("canPvp", "Pvp Enabled"),
    DAMAGE_AMOUNT("damageAmount <mode> <amount>", "Damage Amount"),
    DAMAGE_CAUSE("damageCause <cause>", "Damage Cause"),
    FISHING_ENVIRONMENT("fishingEnv <environment>", "Fishing Environment"),
    FLYING("isFlying", "Player Flying"),
    GAMEMODE("gamemode <required_gamemode>", "Required Gamemode"),
    GLOBAL_STAT("globalstat <stat> <mode> <amount>", "Global Stat Requirement"),
    HAS_GROUP("hasGroup <required_group> <include_higher_groups>", "Required Group"),
    HAS_ITEM("hasItem <item> <what_to_check> <where_to_check> <required_amount>", "Has Item"),
    HAS_PERMISSION("hasPermission <required_permission>", "Required Permission"),
    HAS_TEAM("hasTeam <required_team>", "Required Team"),
    HEALTH("health <mode> <amount>", "Player Health"),
    HUNGER_LEVEL("hunger <mode> <amount>", "Player Hunger"),
    IN_PARKOUR("doingParkour", "Doing Parkour"),
    IN_REGION("inRegion <region>", "Within Region"),
    IN_TEAM("hasTeam <required_team>", "Required Team"),
    IS_ITEM("isItem <item> <what_to_check> <where_to_check> <required_amount>", "Is Item"),
    MAX_HEALTH("maxHealth <mode> <amount>", "Max Player Health"),
    PLACEHOLDER_NUMBER("placeholder <placeholder> <mode> <amount>", "Placeholder Number Requirement"),
    PLAYER_STAT("stat <stat> <mode> <amount>", "Player Stat Requirement"),
    PORTAL_TYPE("portal <portal_type>", "Portal Type"),
    POTION_EFFECT("hasPotion <effect>", "Has Potion Effect"),
    SNEAKING("isSneaking", "Player Sneaking"),
    TEAM_STAT("teamstat <stat> <team> <mode> <amount>", "Team Stat Requirement");

    val keyword = syntax.split(" ")[0]

    companion object {
        fun getFromKeyword(keyword: String): Conditions? {
            for (condition in values()) {
                if (condition.keyword == keyword) {
                    return condition
                }
            }
            return null
        }
    }
}

fun isValid(value: String): Boolean {
    return !(value == "actions" || value == "conditions")
}