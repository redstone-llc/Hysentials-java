package llc.redstone.hysentials.htsl.actions

import llc.redstone.hysentials.htsl.actions.Types.*

class Condition(
    val enum: Conditions,
    override val options: List<Option> = emptyList(),
): Menus() {
    companion object {
        val conditions = mutableListOf<Condition>().let {
            it.add(Condition(Conditions.HAS_GROUP,
                listOf(
                    Option("required_group", 10, null, DYNAMIC_OPTION_SELECT),
                    Option("include_higher_groups", 11, false, TOGGLE)
                )
            ))
            it.add(Condition(Conditions.PLAYER_STAT,
                listOf(
                    Option("stat", 10, "Kills", CHAT_INPUT),
                    Option("mode", 11, "EQUAL", STATIC_OPTION_SELECT, listOf(
                        "Less Than", "Less Than or Equal", "Equal", "Greater Than or Equal", "Greater Than"
                    )),
                    Option("amount", 12, null, ANVIL_INPUT)
                )
            ))
            it.add(Condition(Conditions.GLOBAL_STAT,
                listOf(
                    Option("stat", 10, "Kills", CHAT_INPUT),
                    Option("mode", 11, "EQUAL", STATIC_OPTION_SELECT, listOf(
                        "Less Than", "Less Than or Equal", "Equal", "Greater Than or Equal", "Greater Than"
                    )),
                    Option("amount", 12, null, ANVIL_INPUT)
                )
            ))
            it.add(Condition(Conditions.HAS_PERMISSION,
                listOf(
                    Option("required_permission", 10, null, STATIC_OPTION_SELECT, listOf(
                        "Fly", "Wood Door", "Iron Door", "Wood Trap Door", "Iron Trap Door", "Fence Gate", "Button", "Lever", "Use Launch Pads", "/tp", "/tp Other Players", "Jukebox", "Kick", "Ban", "Mute", "Pet Spawning", "Build", "Offline Build", "Fluid", "Pro Tools", "Use Chests", "Use Ender Chests", "Item Editor", "Switch Game Mode", "Edit Stats", "Change Player Group", "Change Gamerules", "Housing Menu", "Team Chat Spy", "Edit Actions", "Edit Regions", "Edit Scoreboard", "Edit Event Actions", "Edit Commands", "Edit Functions", "Edit Inventory Layouts", "Edit Teams", "Edit Custom Menus", "Item: Mailbox", "Item: Egg Hunt", "Item: Teleport Pad", "Item: Launch Pad", "Item: Action Pad", "Item: Hologram", "Item: NPCs", "Item: Action Button", "Item: Leaderboard", "Item: Trash Can", "Item: Biome Stick"
                    ))
                )
            ))
            it.add(Condition(Conditions.IN_REGION,
                listOf(
                    Option("region", 10, null, DYNAMIC_OPTION_SELECT)
                )
            ))
            it.add(Condition(Conditions.HAS_ITEM,
                listOf(
                    Option("item", 10, null, ITEM),
                    Option("what_to_check", 11, "Metadata", STATIC_OPTION_SELECT, listOf(
                        "Item Type", "Metadata"
                    )),
                    Option("where_to_check", 12, "Anywhere", STATIC_OPTION_SELECT, listOf(
                        "Hand", "Armor", "Hotbar", "Inventory", "Anywhere"
                    )),
                    Option("required_amount", 13, "Any Amount", STATIC_OPTION_SELECT, listOf(
                        "Any Amount", "Equal or Greater Amount"
                    ))
                )
            ))
            it.add(Condition(Conditions.IN_PARKOUR))
            it.add(Condition(Conditions.POTION_EFFECT,
                listOf(
                    Option("effect", 10, null, STATIC_OPTION_SELECT, listOf(
                        "Speed", "Slowness", "Haste", "Mining Fatigue", "Strength", "Instant Health", "Instant Damage", "Jump Boost", "Nausea", "Regeneration", "Resistance", "Fire Resistance", "Water Breathing", "Invisibility", "Blindness", "Night Vision", "Hunger", "Weakness", "Poison", "Wither", "Health Boost", "Absorption"
                    ))
                )
            ))
            it.add(Condition(Conditions.SNEAKING))
            it.add(Condition(Conditions.FLYING))
            it.add(Condition(Conditions.HEALTH,
                listOf(
                    Option("mode", 10, "EQUAL", STATIC_OPTION_SELECT, listOf(
                        "Less Than", "Less Than or Equal", "Equal", "Greater Than or Equal", "Greater Than"
                    )),
                    Option("amount", 11, null, ANVIL_INPUT)
                )
            ))
            it.add(Condition(Conditions.MAX_HEALTH,
                listOf(
                    Option("mode", 10, "EQUAL", STATIC_OPTION_SELECT, listOf(
                        "Less Than", "Less Than or Equal", "Equal", "Greater Than or Equal", "Greater Than"
                    )),
                    Option("amount", 11, null, ANVIL_INPUT)
                )
            ))
            it.add(Condition(Conditions.HUNGER_LEVEL,
                listOf(
                    Option("mode", 10, "EQUAL", STATIC_OPTION_SELECT, listOf(
                        "Less Than", "Less Than or Equal", "Equal", "Greater Than or Equal", "Greater Than"
                    )),
                    Option("amount", 11, null, ANVIL_INPUT)
                )
            ))
            it.add(Condition(Conditions.GAMEMODE,
                listOf(
                    Option("required_gamemode", 10, null, STATIC_OPTION_SELECT, listOf(
                        "Adventure", "Survival", "Creative"
                    ))
                )
            ))
            it.add(Condition(Conditions.PLACEHOLDER_NUMBER,
                listOf(
                    Option("placeholder", 10, null, CHAT_INPUT),
                    Option("mode", 11, "EQUAL", STATIC_OPTION_SELECT, listOf(
                        "Less Than", "Less Than or Equal", "Equal", "Greater Than or Equal", "Greater Than"
                    )),
                    Option("amount", 12, null, ANVIL_INPUT)
                )
            ))
            it.add(Condition(Conditions.IN_TEAM,
                listOf(
                    Option("required_team", 10, "None", DYNAMIC_OPTION_SELECT)
                )
            ))
            it.add(Condition(Conditions.TEAM_STAT,
                listOf(
                    Option("stat", 10, "Kills", CHAT_INPUT),
                    Option("team", 11, "None", DYNAMIC_OPTION_SELECT),
                    Option("mode", 12, "EQUAL", STATIC_OPTION_SELECT, listOf(
                        "Less Than", "Less Than or Equal", "Equal", "Greater Than or Equal", "Greater Than"
                    )),
                    Option("amount", 13, null, ANVIL_INPUT)
                )
            ))
            it.add(Condition(Conditions.CAN_PVP))
            it.add(Condition(Conditions.FISHING_ENVIRONMENT,
                listOf(
                    Option("environment", 10, null, STATIC_OPTION_SELECT, listOf(
                        "Water", "Lava"
                    ))
                )
            ))
            it.add(Condition(Conditions.PORTAL_TYPE,
                listOf(
                    Option("portal_type", 10, "End Portal", STATIC_OPTION_SELECT, listOf(
                        "Nether Portal", "End Portal"
                    ))
                )
            ))
            it.add(Condition(Conditions.DAMAGE_CAUSE,
                listOf(
                    Option("cause", 10, null, STATIC_OPTION_SELECT, listOf(
                        "Entity Attack", "Projectile", "Suffocation", "Fall", "Lava", "Fire", "Fire Tick", "Drowning", "Starvation", "Poison", "Thorns"
                    ))
                )
            ))
            it.add(Condition(Conditions.DAMAGE_AMOUNT,
                listOf(
                    Option("mode", 10, "EQUAL", STATIC_OPTION_SELECT, listOf(
                        "Less Than", "Less Than or Equal", "Equal", "Greater Than or Equal", "Greater Than"
                    )),
                    Option("amount", 11, null, ANVIL_INPUT)
                )
            ))
            it.add(Condition(Conditions.BLOCK_TYPE,
                listOf(
                    Option("item", 10, null, ITEM),
                    Option("match_type_only", 11, false, TOGGLE)
                )
            ))
            it.add(Condition(Conditions.IS_ITEM,
                listOf(
                    Option("item", 10, null, ITEM),
                    Option("what_to_check", 11, "Metadata", STATIC_OPTION_SELECT, listOf(
                        "Item Type", "Metadata"
                    )),
                    Option("where_to_check", 12, "Anywhere", STATIC_OPTION_SELECT, listOf(
                        "Hand", "Armor", "Hotbar", "Inventory", "Anywhere"
                    )),
                    Option("required_amount", 13, "Any", STATIC_OPTION_SELECT, listOf(
                        "Any Amount", "Equal or Greater Amount"
                    ))
                )
            ))
            it
        }
    }

    override fun getName(): String {
        return enum.name
    }

    override fun getGUIName(): String {
        return enum.conditionName
    }

    override fun getSyntax(): String {
        return enum.syntax
    }
}