package llc.redstone.hysentials.htsl.compiler

import llc.redstone.hysentials.htsl.actions.*

class ConvertAction {
    companion object {
        @JvmStatic
        fun convertJSON(actionobjs: MutableList<ActionObj>): String {
            val script = mutableListOf<String>()
            for (actionobj in actionobjs) {
                if (actionobj.context.context != "DEFAULT") {
                    script.add("goto ${actionobj.context.context} \"${actionobj.context.contextTarget.name}\"")
                }

                val actions = actionobj.actions
                for (action in actions) {
                    if (action == null) continue
                    var syntax = Actions.valueOf(action.type.uppercase())
                    val menu = Action.actions.find { it.enum == syntax }?: continue
                    script.add(convertComponent(action, syntax, menu))
                }
            }
            return script.joinToString("\n")
        }

        private fun convertComponent(comp: Component, syntax: Any, menu: Menus): String {
            val syntaxFull = if (syntax is Actions) syntax.syntax else (syntax as Conditions).syntax
            val properties = Regex("<(.*?)>").findAll(syntaxFull).toList().map { it.value }
            var action = syntaxFull
            for (property in properties) {
                val propertyMatch = Regex("<(.*)>").findAll(property).toList().map { it.groupValues[1] }
                if (propertyMatch.isEmpty()) continue
                if (menu.options.find { it.name == propertyMatch[0] } == null) continue
                val option = menu.options.find { it.name == propertyMatch[0] }!!
                if (option.type == Types.SUB_ACTIONS) {
                    val actions = (comp[propertyMatch[0]] as MutableList<Component?>)
                    val subActions = mutableListOf<String>()
                    for (action in actions) {
                        if (action == null) continue
                        val syntax = Actions.valueOf(action.type.uppercase())
                        val menu = Action.actions.find { it.enum == syntax }?: continue
                        subActions.add(convertComponent(action, syntax, menu))
                    }
                    action = action.replace(property, subActions.joinToString("\n"))
                } else if (option.type == Types.CONDITIONS) {
                    val conditions = (comp[propertyMatch[0]] as MutableList<Component?>)
                    val conditionActions = mutableListOf<String>()
                    for (condition in conditions) {
                        if (condition == null) continue
                        val syntax = Conditions.valueOf(condition.type.uppercase())
                        val menu = Condition.conditions.find { it.enum == syntax }?: continue
                        conditionActions.add(convertComponent(condition, syntax, menu))
                    }
                    action = action.replace(property, conditionActions.joinToString("\n"))
                } else if (option.type == Types.LOCATION) {
                    val location = comp[propertyMatch[0]] as Component
                    fun rel(value: Int) = if (value == 1) "~" else ""
                    action = action.replace(property,
                        "custom_coordinates ${rel(location["relX"].toString().toInt())}${location["x"]} ${rel(location["relY"].toString().toInt())}${location["y"]} ${rel(location["relZ"].toString().toInt())}${location["z"]} ${location["pitch"]} ${location["yaw"]}"
                    )
                } else if (option.type == Types.STATIC_OPTION_SELECT) {
                    if (option.options == null) {
                        val value = comp[propertyMatch[0]].toString()
                        if (value.contains(" ")) {
                            action = action.replace(property, "\"$value\"")
                        } else {
                            action = action.replace(property, value)
                        }
                    } else {
                        if (option.options.contains("Increment")) {
                            action = action.replace(property,
                                ExportAction.exportValidOperator(comp[propertyMatch[0]].toString())
                            )
                        } else if (option.options.contains("Less Than")) {
                            action = action.replace(property,
                                ExportAction.exportValidComparator(comp[propertyMatch[0]].toString())
                            )
                        } else {
                            val value = comp[propertyMatch[0]].toString()
                            if (value.contains(" ")) {
                                action = action.replace(property, "\"$value\"")
                            } else {
                                action = action.replace(property, value)
                            }
                        }
                    }
                }
                else {
                    val value = comp[propertyMatch[0]].toString()
                    if (value.contains(" ")) {
                        action = action.replace(property, "\"$value\"")
                    } else {
                        action = action.replace(property, value)
                    }
                }
            }
            return action
        }
    }
}