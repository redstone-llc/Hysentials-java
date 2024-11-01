package llc.redstone.hysentials.htsl.compiler

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.libs.universal.UMinecraft
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent
import llc.redstone.hysentials.HYSENTIALS_API
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.config.hysentialmods.HousingConfig
import llc.redstone.hysentials.handlers.htsl.Queue.add
import llc.redstone.hysentials.handlers.htsl.Queue.forceOperation
import llc.redstone.hysentials.handlers.redworks.BwRanks
import llc.redstone.hysentials.handlers.sbb.SbbRenderer
import llc.redstone.hysentials.htsl.*
import llc.redstone.hysentials.htsl.actions.Action
import llc.redstone.hysentials.htsl.actions.Condition
import llc.redstone.hysentials.htsl.actions.Menus
import llc.redstone.hysentials.htsl.actions.Types.*
import llc.redstone.hysentials.utils.ChatLib
import llc.redstone.hysentials.utils.getLore
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.item.ItemStack
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class ExportAction {
    companion object {
        @JvmStatic
        var actionobjs = mutableListOf<Component?>()

        @JvmStatic
        var subactions = mutableListOf<Component?>()

        @JvmStatic
        var exportMethod: String? = null

        @JvmStatic
        fun processMenu(menu: Menus, subMenuItems: List<ItemStack?>, actionKey: String, callback: (Component) -> Unit) {
            val action = Component(actionKey)
            forceOperation(actionOrder {
                callback(action)
            })
            for (option in menu.options) {
                fun getLore(index: Int): String {
                    try {
                        return ChatLib.removeFormatting(
                            subMenuItems[option.slot]?.getLore()?.get(index) ?: "Not Set"
                        )
                    } catch (e: Exception) {
                        println(subMenuItems[option.slot]?.getLore())
                        e.printStackTrace()
                        return "Not Set"
                    }
                } //If the lore is null, it will return "Not Set"
                when (option.type) {
                    TOGGLE -> {
                        action[option.name] = getLore(2) == "Enabled"
                        if (option.name == "match_any_condition") action[option.name] = getLore(5) == "Enabled"
                    }

                    LOCATION -> {
                        if (getLore(2) == "Not Set") {
                            action[option.name] = Component("custom_coordinates").let {
                                it["relZ"] = 1
                                it["relX"] = 1
                                it["relY"] = 1
                                it["x"] = 0
                                it["y"] = 0
                                it["z"] = 0
                                it["pitch"] = 0
                                it["yaw"] = 0
                                it
                            }
                        } else {
                            val temp = Regex("(~)?(-?\\d+(\\.\\d+)?)").findAll(getLore(2)).toList()
                            action[option.name] = Component("custom_coordinates").let {
                                it["relZ"] = if (temp[2].value.contains("~")) 1 else 0
                                it["relY"] = if (temp[1].value.contains("~")) 1 else 0
                                it["relX"] = if (temp[0].value.contains("~")) 1 else 0
                                it["x"] = temp[0].value.toDouble()
                                it["y"] = temp[1].value.toDouble()
                                it["z"] = temp[2].value.toDouble()
                                it["pitch"] = temp[3].value.toDouble()
                                it["yaw"] = temp[4].value.toDouble()
                            }
                        }
                    }

                    ITEM -> {
                        action[option.name] = null;
                    }

                    CONDITIONS -> {
                        if (Regex("No Conditions").matches(getLore(2))) {
                            action[option.name] = null
                        } else {
                            forceOperation(doneSub {
                                action[option.name] = subactions
                                subactions = mutableListOf()
                            })
                            forceOperation(back())
                            forceOperation(export {
                                subactions = mutableListOf()
                                processPage(it, subactions, Condition.conditions, 0)
                            })
                            forceOperation(click(option.slot))
                        }
                    }

                    SUB_ACTIONS -> {
                        if (Regex("No Actions").matches(getLore(4))) {
                            action[option.name] = null
                            forceOperation(doneSub {
                                subactions = mutableListOf()
                            })
                        } else {
                            forceOperation(doneSub {
                                action[option.name] = subactions
                                subactions = mutableListOf()
                            })
                            forceOperation(back())
                            forceOperation(export {
                                subactions = mutableListOf()
                                processPage(it, subactions, Action.actions, 0)
                            })
                            forceOperation(click(option.slot))
                        }
                    }

                    CHAT_INPUT -> {
                        if (HousingConfig.exportColorCodes) {
                            if (subMenuItems[option.slot]?.getLore()?.get(2)?.substring(6)?.isBlank() == true) {
                                action[option.name] = null
                            } else {
                                forceOperation(back())
                                forceOperation(chatInput {
                                    action[option.name] = it
                                })
                                forceOperation(click(option.slot))
                            }
                        } else {
                            action[option.name] = getLore(2)
                            if (action[option.name] == "Not Set") action[option.name] = null
                        }
                    }

                    else -> {
                        action[option.name] = getLore(2)
                        if (action[option.name] == "Not Set") action[option.name] = null
                        if (option.name == "ticks_to_wait") action[option.name] = getLore(4)
                        if (option.name == "team" && menu.options.size > 2) action[option.name] = getLore(5)
                        if (option.options?.contains("Increment") == true && action[option.name] != null) {
                            action[option.name] = exportValidOperator(action[option.name] as String)
                        }
                        if (option.options?.contains("Less Than") == true && action[option.name] != null) {
                            action[option.name] = exportValidComparator(action[option.name] as String)
                        }
                    }
                }
            }
        }

        fun exportValidOperator(operator: String): String {
            val validOperator = when (operator.lowercase()) {
                "increment" -> "+="
                "decrement" -> "-="
                "set" -> "="
                "multiply" -> "*="
                "divide" -> "/="
                else -> operator
            }
            return if (validOperator.lowercase() in listOf(
                    "+=",
                    "-=",
                    "=",
                    "*=",
                    "/="
                )
            ) validOperator.uppercase() else "Unknown operator &e$operator&c on &eline {line}"
        }

        fun exportValidComparator(comparator: String): String {
            val validComparator = when (comparator.lowercase()) {
                "equal" -> "="
                "less than" -> "<"
                "less than or equal" -> "<="
                "greater than" -> ">"
                "greater than or equal" -> ">="
                else -> comparator
            }
            return if (validComparator in listOf(
                    "=",
                    "<",
                    "<=",
                    ">",
                    ">="
                )
            ) validComparator else "Unknown comparator &e$comparator&c on &eline {line}"
        }

        fun exportAction(name: String) {
            var items = UMinecraft.getPlayer()!!.openContainer.inventorySlots.map { it.stack }
            items = items.subList(0, items.size - 36 - 9)
            actionobjs = mutableListOf()

            processPage(items, actionobjs, Action.actions, 0)

            add(doneExport {
                //file, library, clipboard
                val actions = mutableListOf(
                    ActionObj(
                        Context("DEFAULT", ContextTarget(null)),
                        mutableListOf(),
                        actionobjs.asReversed()
                    )
                )

                val code = ConvertAction.convertJSON(actions)

                val id = BwRanks.randomString(15);
                val json = JSONObject();
                json.put("name", name);
                json.put("code", code);
                json.put("creator", UMinecraft.getPlayer()!!.getName());
                json.put("description", "Exported from Hysentials");
                val codespace = JSONObject();
                codespace.put("functions", actions.size);
                codespace.put("conditions", actions.sumOf { it.getConditionsCount() });
                codespace.put("actions", actions.sumOf { it.getActionsCount() });
                json.put("codespace", codespace);
                when (exportMethod) {
                    "file" -> {
                        var file = File(Minecraft.getMinecraft().mcDataDir, "config/hysentials");
                        if (!file.exists()) file.mkdir();
                        file = File(file, "htsl");
                        if (!file.exists()) file.mkdir();
                        file = File(file, "house")
                        if (!file.exists()) file.mkdir();
                        file = File(file, "${SbbRenderer.housingScoreboard.housingName}-${SbbRenderer.housingScoreboard.housingCreator}")
                        if (!file.exists()) file.mkdir();
                        file = File(file, "$name.htsl");
                        if (!file.exists()) file.createNewFile();
                        file.writeText(code);
                        UChat.chat("&3[HTSL] &fExported to file &b" + file.absolutePath + "&f!");
                    }

                    "library" -> {
                        try {
                            val input = InputStreamReader(Hysentials.post(HYSENTIALS_API + "/action?id=" + id, json), StandardCharsets.UTF_8)
                            val s = IOUtils.toString(input);
                            val json = JSONObject(s);
                            if (json.getBoolean("success")) {
                                UChat.chat("&3[HTSL] &fExported successfully to &bAction Library!");
                                UTextComponent("&3[HTSL] &fClick here to edit your action.").setClick(
                                    ClickEvent.Action.OPEN_URL,
                                    "https://redstone.llc/actions/manage/" + id
                                ).chat();
                            } else {
                                UChat.chat("&cFailed to export!");
                            }
                        } catch (e: Exception) {
                            e.printStackTrace();
                        }
                    }
                    "clipboard" -> {
                        val selection = StringSelection(code)
                        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                        clipboard.setContents(selection, selection)
                        UChat.chat("&3[HTSL] &fCopied to clipboard!")
                    }
                }
            })
        }

        private fun getItems(): MutableList<ItemStack?> {
            return UMinecraft.getPlayer()!!.openContainer.inventorySlots.map { it.stack }.toMutableList()
        }

        fun processPage(
            subMenuItems: List<ItemStack?>,
            actionList: MutableList<Component?>,
            menuList: List<Menus>,
            page: Int
        ) {
            forceOperation(donePage {
                val items = getItems()
                val nextPage = items[items.size - 37]
                if (nextPage != null && ChatLib.removeFormatting(nextPage.displayName) == "Next Page") {
//                    var next = getItems();  //Keeping this here just in case
//                    next = next.subList(0, next.size - 36 - 9)
                    forceOperation(export {
                        processPage(it, actionList, menuList, page + 1)
                    })
                    forceOperation(click(getItems().size - 37))
                }
            })

            for ((i, item) in subMenuItems.withIndex()) {
                if (item == null) continue
                var menu: Menus? = null
                var actionKey: String? = null
                for (key in menuList) {
                    if (key.getGUIName() == ChatLib.removeFormatting(item.displayName)) {
                        menu = key
                        actionKey = key.getName()
                        break
                    }
                }
                if (menu == null) {
                    ChatLib.chat("&3[HTSL] &fConnor is that you?")
                    continue
                }
                if (menu.options.isNotEmpty()) {
                    //Basically do the bigger ones first

                    //to the right to the right
                    for (j in 0 until page) {
                        forceOperation(click(getItems().size - 37))
                    }

                    forceOperation(back())
                    forceOperation(export { items ->
                        processMenu(menu, items, actionKey!!) {
                            actionList.add(it)
                        }
                    })
                    forceOperation(click(i))
                } else {
                    forceOperation(actionOrder {
                        actionList.add(Component(actionKey!!))
                    })
                }
            }
        }
    }
}