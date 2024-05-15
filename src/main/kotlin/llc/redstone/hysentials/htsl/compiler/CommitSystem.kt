package llc.redstone.hysentials.htsl.compiler

import llc.redstone.hysentials.handlers.htsl.Queue
import llc.redstone.hysentials.handlers.sbb.SbbRenderer
import llc.redstone.hysentials.htsl.actions.Action
import llc.redstone.hysentials.htsl.actions.Actions
import llc.redstone.hysentials.htsl.actions.Conditions
import llc.redstone.hysentials.htsl.actions.Menus
import llc.redstone.hysentials.htsl.done
import llc.redstone.hysentials.utils.getText
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiScreen
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import kotlin.math.pow

fun main() {
    val name = "test.htsl"
    val name2 = "test.htsl"

    val file1 = File("C:\\Users\\griff\\Desktop\\games\\MultiMC\\instances\\1.8.9\\.minecraft\\config\\hysentials\\htsl\\house\\$name2")
    val file2 = File("C:\\Users\\griff\\Desktop\\games\\MultiMC\\instances\\1.8.9\\.minecraft\\config\\hysentials\\htsl\\$name")


    if (!file1.exists() || !file2.exists()) {
        println("File not found")
        return
    }
    patch(file1, file2)
}

/*
Currently does not support goto or conditions
 */
fun patch(file1: File, file2: File): Any {

    val text = file1.readText()
    val text2 = file2.readText()

    val dmp = DiffMatchPatch()
    val diff = dmp.diffMain(text, text2)
    val patch = dmp.patchMake(diff)
    if (patch.isEmpty()) {
        return "No changes"
    }
    // Lines that are different
    val patchMap = mutableMapOf<String, String>()
    var patchText = dmp.patchApply(patch, text).first().toString()
    patch.forEach { patch ->
        val diff1 = patch.diffs[0].text.trim()
        val index = patch.start1 + diff1.length
        val index2 = patch.start2 + diff1.length
        val line = text.getLineFromIndex(index)
        if (line != text.getLineFromIndex(index2)) { // Check and make sure its not multi-line
            return "The patch is too complex to apply, please apply manually."
        }
        patchMap["$line:$index:$index2"] = getLineFromIndex(text, index)
    }

    val compiled = compileFile(text2, false)
    if (compiled == null) {
        return "Failed to compile"
    }
    for ((lineData, lineText) in patchMap) {
        if (lineText.startsWith("if")) continue //Not implemented yet
        val charStart = lineData.split(":")[1].toInt()
        val args = getArgs(lineText)

        val keyword = args[0] as String
        args.removeAt(0)

        if (compiled == null) continue
        val arg = patchText.getArgFromCharIndex(charStart)
        val actionMenu = Action.actions.find { it.enum == Actions.getFromKeyword(keyword) } ?: continue;
        val actionSyntax = Actions.getFromKeyword(keyword) ?: continue
        if (arg == null) continue
        val oldComp = updateAction(arg, actionMenu, actionSyntax)
        val newComp = compiled.getThing(lineData.split(":")[0].toInt() - 1) ?: continue //This is a little inefficient but eh it works
        oldComp.parentIndex = newComp.parentIndex
        updateAction(lineData.split(":")[0].toInt() - 1, oldComp)
    }
    Queue.add(done {
        var file = File(Minecraft.getMinecraft().mcDataDir, "config/hysentials");
        if (!file.exists()) file.mkdir();
        file = File(file, "htsl");
        if (!file.exists()) file.mkdir();
        file = File(file, "house")
        if (!file.exists()) file.mkdir();
        file = File(file, "${SbbRenderer.housingScoreboard.housingName}-${SbbRenderer.housingScoreboard.housingCreator}")
        if (!file.exists()) file.mkdir();
        file = File(file, file1.name)
        if (!file.exists()) file.createNewFile();
        file.writeText(patchText);
    })

    return "success"
}

fun MutableList<ActionObj>.getThing(check: Int): Component? {
    first().actions.forEachIndexed { index, it ->
        if (it == null) return@forEachIndexed
        var index = index;
        var parentIndex = ""
        if (index == check) {
            return it
        }

//        if (it.contains("conditions")) {
//            val conditions = (it["conditions"] as MutableList<Component?>)
//            conditions.forEachIndexed { index2, it ->
//                if (index + index2 == index) {
//                    return it?: return@forEachIndexed
//                }
//            }
//            index += conditions.size
//        }
        if (it.contains("if_actions")) {
            val ifActions = (it["if_actions"] as MutableList<Component?>)
            parentIndex = index.toString()
            index += 1
            ifActions.forEachIndexed { index2, it ->
                if (index + index2 == check) {
                    if (it != null) it.parentIndex = "$parentIndex/2/$index2"
                    return it?: return@forEachIndexed
                }
            }
            index += ifActions.size
        }
        if (it.contains("else_actions")) {
            val elseActions = (it["else_actions"] as MutableList<Component?>)
            index += 1
            elseActions.forEachIndexed { index2, it ->
                if (index + index2 == check) {
                    if (it != null) it.parentIndex = "$parentIndex/3/$index2"
                    return it?: return@forEachIndexed
                }
            }
            index += elseActions.size + 1
        }
        if (it.contains("sub_actions")) {
            val subActions = (it["sub_actions"] as MutableList<Component?>)
            subActions.forEachIndexed { index2, it ->
                if (index + index2 == check) {
                    if (it != null) it.parentIndex = "$parentIndex/0/$index2"
                    return it?: return@forEachIndexed
                }
            }
            index += subActions.size + 2
        }
    }
    return null;
}

private fun String.getLinePartialFromIndex(index: Int): String {
    val start = this.substring(0, index).lastIndexOf('\n') + 1
    return this.substring(start, index)
}

fun updateAction(arg: Result, menu: Menus, syntax: Any): Component {
    val args = mutableListOf<Any?>()
    for ((index, option) in menu.options.withIndex()) {
        if (index == arg.index - 1) {
            args.add(arg.text)
        } else {
            args.add(null)
        }
    }
    val comp = componentFunc(args, syntax, menu) as Component
    return comp
}

private fun String.getLineFromIndex(index: Int): Int {
    return this.substring(0, index).count { it == '\n' } + 1
}

data class Result(val text: String, val index: Int)

private fun String.getArgFromCharIndex(index: Int): Result? {
    var start = substring(0, index).lastIndexOf('\n') + 1
    var end = indexOf('\n', index)
    val text = substring(start, end)
    var localArg = index - start
    for (arg in getArgs(text, true)) {
        var argString = arg.toString()
        if (argString.startsWith("\"") && argString.endsWith("\"")) {
            argString = argString.substring(1, argString.length - 1)
        }
        if (localArg - argString.length - 1 <= 0) {
            return Result(argString, getArgs(text, true).indexOf(arg))
        }
        localArg -= argString.length + 1
    }
    return null
}

private fun getLineFromIndex(text: String, index: Int): String {
    val start = text.substring(0, index).lastIndexOf('\n') + 1
    val end = text.indexOf('\n', index)
    return text.substring(start, end)
}