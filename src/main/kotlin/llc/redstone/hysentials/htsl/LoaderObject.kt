package llc.redstone.hysentials.htsl

import net.minecraft.item.ItemStack

data class LoaderObject(
    var type: String, var key: String? = null, var value: Any? = null, var command: Boolean? = null, var func: String? = null,
    var func1: ((MutableList<ItemStack>) -> Unit)? = null, var func2: (() -> Unit)? = null, var func3: ((String) -> Unit)? = null) {

    fun func(items: MutableList<ItemStack>) {
        func1?.invoke(items)
    }

    fun func() {
        func2?.invoke()
    }

    fun func(value: String) {
        func3?.invoke(value)
    }

    override fun toString(): String {
        return "LoaderObject(type='$type', key=$key, value=$value)"
    }
}
fun anvil(value: String) = LoaderObject("anvil", "text", value)
fun back() = LoaderObject("back")
fun returnToEditActions() = LoaderObject("returnToEditActions")
fun done() = LoaderObject("done")
fun done(func2: (() -> Unit)?) = LoaderObject("done", func2 = func2)

fun click(value: Int) = LoaderObject("click", "slot", value)
fun option(value: String) = LoaderObject("option", "option", value)
fun chat(value: String) = LoaderObject("chat", "text", value)
fun chat(value: String, func: String, command: Boolean) = LoaderObject("chat", "text", value, command, func)
fun chat(value: String, command: Boolean) = LoaderObject("chat", "text", value, command)
fun gotoLoader(value: String) = LoaderObject("goto", "name", value)
fun command(value: String) = LoaderObject("command", "command", value)
fun selectOrClick(value: String, slot: Int) = LoaderObject("selectOrClick", value, slot.toString())
fun manualOpen(value: String, msg: String) = LoaderObject("manualOpen", value, msg)
fun close() = LoaderObject("close")
fun setGuiContext(value: String) = LoaderObject("setGuiContext", "context", value)
fun setActionName(value: String) = LoaderObject("setActionName", "actionName", value)
fun item(value: String) = LoaderObject("item", "item", value)

fun page(value: Int) = LoaderObject("page", "page", value)

fun export(func1: ((MutableList<ItemStack>) -> Unit)?) = LoaderObject("export", func1 = func1)
fun doneExport(func2: (() -> Unit)?) = LoaderObject("doneExport", func2 = func2)
fun doneSub(func2: (() -> Unit)?) = LoaderObject("doneSub", func2 = func2)
fun donePage(func2: (() -> Unit)?) = LoaderObject("donePage", func2 = func2)
fun actionOrder(func2: (() -> Unit)?) = LoaderObject("actionOrder", func2 = func2)
fun chatInput(func3: ((String) -> Unit)) = LoaderObject("chat_input", func3 = func3)

