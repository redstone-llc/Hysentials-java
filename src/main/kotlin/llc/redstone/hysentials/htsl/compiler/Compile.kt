package llc.redstone.hysentials.htsl.compiler

import llc.redstone.hysentials.handlers.htsl.Queue
import llc.redstone.hysentials.htsl.actions.*
import llc.redstone.hysentials.htsl.actions.Types.*
import llc.redstone.hysentials.utils.ChatLib
import java.io.File
import kotlin.math.pow


var macros: Array<Macro>? = null
fun compileFile(importText: String, load: Boolean = true): MutableList<ActionObj>? {
    macros = emptyArray()
    ChatLib.chat("&3[HTSL] &fCompiling . . .")

    val actionobj = preProcess(importText.split("\n"))?: return null
    //processor
    for (i in actionobj.indices) {
        val actions = actionobj[i].actionList
        val newActionList = mutableListOf<Component?>()
        for (j in actions.indices) {
            val action = actions[j]
            val args = if (action.line.contains("\n")) getMultiline(action.line) else getArgs(action.line.trim())
            val currentLine = action.trueLine
            if (args.isEmpty()) {
                ChatLib.chat("&3[HTSL] &cSomething went wrong with expression evaluation on &eline ${currentLine}")
                return null
            }
            val keyword = args.first() as String
            args.removeAt(0)
            if (Actions.getFromKeyword(keyword) == null) {
                ChatLib.chat("&3[HTSL] &cUnknown action &e${keyword}&c on &eline ${currentLine}")
                return null
            }
            val syntax = Actions.getFromKeyword(keyword)!!
            val component = componentFunc(args, syntax, Action.actions.find { it.enum == syntax }!!)
            if (component is String) {
                ChatLib.chat("&3[HTSL] &c${component.replace("{line}", currentLine.toString())}")
                return null
            } else if (component != null && component is Component) {
                newActionList.add(component)
            } else {
                ChatLib.chat("&3[HTSL] &cUnknown action &e${keyword}&c on &eline ${currentLine}")
                return null
            }
        }
        actionobj[i].actionList.clear()
        actionobj[i].actions = newActionList
    }
    ChatLib.chat("&3[HTSL] &fCompiled successfully!")
    if (load) loadAction(actionobj)
    return actionobj;
}

fun main () {
    File("src/main/resources/htsl/").walk().forEach {
        if (it.extension == "htsl") {
            val file = compileFile(it.readText())
        }
    }
}

fun componentFunc(args: MutableList<Any?>, syntax: Any, menu: Menus): Any? {
    val syntaxFull = if (syntax is Actions) syntax.syntax else (syntax as Conditions).syntax
    var params = Regex("<(.*?)>").findAll(syntaxFull).map { it.groupValues[1] }.toList()
    var component = Component(if (syntax is Actions) syntax.name else (syntax as Conditions).name)
    for (j in params.indices) {
        if (j >= args.size) {
            continue
        }
        if (args[j] == null) continue
        val argsString = if (args[j] is String) args[j] as String else null
        val param = params[j]
        val option = menu.options.find { it.name == param }
        val type = option?.type
        // handle operator aliases
        if (type == STATIC_OPTION_SELECT && syntax is Actions) {
            args[j] = validOperator(args[j] as String)
            if (argsString?.matches(Regex("Unknown operator(.*)")) == true) return argsString
        }
        // handle comparator aliases
        if (type == STATIC_OPTION_SELECT && syntax is Conditions) {
            args[j] = validComparator(args[j] as String)
            if (argsString?.matches(Regex("Unknown comparator(.*)")) == true) return argsString
        }
        // handle custom location type
        if (type == LOCATION) {
            val typeS = argsString?.lowercase()?.replace(" ", "_")
            if (!arrayOf("house_spawn", "current_location", "invokers_location", "custom_coordinates").contains(typeS)) {
                return "Invalid location &e${args[j]}&c on &eline {line}"
            }
            args[j] = Component(typeS!!)
            (args[j] as Component)["coords"] = args[j + 1] as String //WHY DO I DO THIS LOL
        }
        // handle toggles
        if (type == TOGGLE) {
            if (arrayOf("or", "true").contains(argsString)) {
                args[j] = true
            } else if (arrayOf("and", "false").contains(argsString)) {
                args[j] = false
            } else {
                return "Invalid toggle &e${args[j]}&c on &eline {line}"
            }
        }
        // handle item
        if (type == ITEM) {
            if (args[j] is Map<*, *> && (args[j]!! as Map<*, *>)["slot"] != null) {
                args[j] = Component("clickSlot")
            } else {
                // TODO: eventually allow json files
                return "Invalid item &e${args[j]}&c on &eline {line}"
            }
        }
        // handle conditions w/ recursion
        if (type == CONDITIONS) {
            val conditionsArg = argsString!!.split(",")
            var conditions = mutableListOf<Any?>()
            for (condition in conditionsArg) {
                val conditionArgs = getArgs(condition.trim())
                if (conditionArgs.isEmpty()) {
                    return "Invalid condition &e$condition&c on &eline {line}"
                }
                val keyword = conditionArgs.first() as String
                conditionArgs.removeAt(0)
                if (Conditions.getFromKeyword(keyword) == null) {
                    return "Unknown condition &e${keyword}&c on &eline {line}"
                }
                val conditionSyntax = Conditions.getFromKeyword(keyword)!!
                val conditionComponent = componentFunc(conditionArgs, conditionSyntax, Condition.conditions.find { it.enum == conditionSyntax }?: return "Unknown condition &e${keyword}&c on &eline {line}")
                if (conditionComponent is String) {
                    return conditionComponent
                } else if (conditionComponent != null) {
                    conditions.add(conditionComponent)
                } else {
                    return "Unknown condition &e${keyword}&c on &eline {line}"
                }
            }
            args[j] = conditions;
        }
        // handle subactions w/ recursion
        if (type == SUB_ACTIONS) {
            val subactions = mutableListOf<Any?>()
            val lines = argsString?.split("\n")
            if (lines != null) {
                for (line in lines) {
                    if (line.isBlank()) continue
                    val subactionArgs = getArgs(line)
                    if (subactionArgs.isEmpty()) {
                        return "Invalid subaction &e$line&c on &eline {line}"
                    }
                    val keyword = subactionArgs.first() as String
                    subactionArgs.removeAt(0)
                    if (Actions.getFromKeyword(keyword) == null) {
                        return "Unknown action &e${keyword}&c on &eline {line}"
                    }
                    val subactionSyntax = Actions.getFromKeyword(keyword)!!
                    val subactionComponent = componentFunc(subactionArgs, subactionSyntax, Action.actions.find { it.enum == subactionSyntax }?: return "Unknown action &e${keyword}&c on &eline {line}")
                    if (subactionComponent is String) {
                        return subactionComponent
                    } else if (subactionComponent != null) {
                        subactions.add(subactionComponent)
                    } else {
                        return "Unknown action &e${keyword}&c on &eline {line}"
                    }
                }
                args[j] = subactions;
            }
        }
        if (args[j] is String) {
            component[params[j]] = args[j]!!
        } else if (args[j] != null) {
            component[params[j]] = args[j]!!
        }
    }
    return component
}

fun preProcess(importActions: List<String>): MutableList<ActionObj>? {
    val actionObj = mutableListOf<ActionObj>()
    var currentContext = Context("DEFAULT", ContextTarget())
    var multilineComment: Boolean = false
    var depth: Int = 0
    val trueAction = mutableListOf<TrueAction>()
    var multilineAction: String? = null
    for (i in importActions.indices) {
        val line = importActions[i]
        if (line == "") continue
        if (line.startsWith("//")) continue
        if (line.startsWith("/*")) {
            multilineComment = true
        }
        if (line.endsWith("*/")) {
            if (multilineComment) {
                multilineComment = false
                continue
            }
            ChatLib.chat("&3[HTSL] &cBroken multiline comment on line &e${i + 1}")
        }
        if (multilineComment) continue
        if (line.endsWith("{")) depth++
        if (line.endsWith("{") && multilineAction == null) {
            multilineAction = line
            continue
        }
        if (line.startsWith("}")) depth--
        if (line == "}" && multilineAction != null && depth == 0) {
            //TODO loop

            multilineAction += "\n}"
            if (multilineAction.startsWith("if")) {
                multilineAction = multilineAction.replace(Regex("^if +\\("), "if and (").replace(Regex(" *} +else +\\{ *"), "\n} {\n")
            }
            trueAction.add(TrueAction(multilineAction, i))
            multilineAction = null
            continue
        }
        if (multilineAction != null) {
            multilineAction += "\n$line"
            continue
        }

        val goto = Regex("goto +(.*) +(.*)").find(line)
        if (goto != null) {
            actionObj.add(ActionObj(currentContext, trueAction))
            currentContext = Context(goto.groupValues[1].uppercase(), ContextTarget(goto.groupValues[2]))
            trueAction.clear()
            continue
        }

        if (line.startsWith("define")) {
            val value = line.split(Regex(" +")).let {
                if (it.size > 1) it[1] else null
            }
            if (value != null && (isValid(value) ||arrayOf("goto", "//", "/*", "*/", "loop").contains(value))) {
                ChatLib.chat("&3[HTSL] &cInvalid macro name &e$value")
                //TODO add it to the list of macros
                continue
            }
        }

        trueAction.add(TrueAction(line, i + 1))
    }

    actionObj.add(ActionObj(currentContext, trueAction))
    if (multilineComment) {
        ChatLib.chat("&3[HTSL] &cUnclosed multiline comment!")
        return null
    }
    if (depth > 0) {
        ChatLib.chat("&3[HTSL] &cUnclosed multiline action!")
        return null
    }
    return actionObj
}

fun getArgs(input: String, includeQuotes: Boolean = false): MutableList<Any?> {
    val conversions = listOf(
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +globalstat +(.*)?"), "$1 %stat.global/$2%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +stat  +(.*)?"), "$1 %stat.player/$2%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +teamstat +(.*)? +?(.*)?"), "$1 %stat.team/$2 $3%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +randomint +(.*)? +?(.*)?"), "$1 %random.int/$2 $3%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +health"), "$1 %player.health%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +maxHealth"), "$1 %player.maxhealth%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +hunger"), "$1 %player.hunger%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +locX"), "$1 %player.location.x%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +locY"), "$1 %player.location.y%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +locZ"), "$1 %player.location.z%"),
        Conversion(Regex("(=|>|<|set|dec|mult|div|ment|inc|multiply|divide|equal|Less Than|Less Than or Equal|Greater Than|Greater Than or Equal) +unix"), "$1 %date.unix%")
    )

    var newInput = input
    for (conversion in conversions) {
        newInput = newInput.replace(conversion.regex, conversion.replacement)
    }

    val result = mutableListOf<Any?>()
    val re = Regex("\"(.*?)\"|\\{((?:\\{|[^}])*)}|(\\S+)")
    var match = re.find(newInput)
    while (match != null) {
        var arg: Any? = match.groups[1]?.value ?: match.groups[2]?.value ?: match.groups[3]?.value
        if (match.groupValues[1].isEmpty() && match.groupValues[2].isEmpty()) {
            val macro = macros?.find { it.name == arg }
            if (macro != null) {
                arg = getArgs(macro.value).joinToString(" ")
                result.addAll(arg.split(" "))
                match = match.next()
                continue
            }
            if (arg == "null") {
                arg = null
            }
            val slotMatch = Regex("slot_(\\d+)").find(arg as String)
            if (slotMatch != null) {
                arg = mapOf("slot" to slotMatch.groupValues[1].toInt())
            }
        } else if (match.groupValues[2].isNotEmpty()) {
            macros?.forEach { macro ->
                arg = (arg as String).replace(macro.name, macro.value)
            }
            try {
                result.add(evaluateExpression((arg as String)))
            } catch (e: Exception) {
                return mutableListOf()
            }
        }
        if (includeQuotes && match.groupValues[1].isNotEmpty()) {
            arg = "\"$arg\""
        }
        result.add(arg)
        match = match.next()
    }
    return result
}

fun parseExpression(expression: String): Double {
    val operators = mapOf(
        "+" to Operator(1, Double::plus),
        "-" to Operator(1, Double::minus),
        "*" to Operator(2, Double::times),
        "/" to Operator(2, Double::div),
        "^" to Operator(3) { a, b -> a.pow(b) }
    )

    val outputQueue = mutableListOf<Double>()
    val operatorStack = mutableListOf<String>()
    val tokens = expression.split(Regex("(\\d+|\\+|-|\\*|\\/|\\^|\\(|\\))")).filter { it.isNotBlank() }

    tokens.forEach { token ->
        when {
            token.toDoubleOrNull() != null -> outputQueue.add(token.toDouble())
            operators.containsKey(token) -> {
                while (operatorStack.isNotEmpty() && operators[token]!!.precedence <= operators[operatorStack.last()]!!.precedence) {
                    val op = operators[operatorStack.removeAt(operatorStack.size - 1)]!!
                    val b = outputQueue.removeAt(outputQueue.size - 1)
                    val a = outputQueue.removeAt(outputQueue.size - 1)
                    outputQueue.add(op.func(a, b))
                }
                operatorStack.add(token)
            }
            token == "(" -> operatorStack.add(token)
            token == ")" -> {
                while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                    val op = operators[operatorStack.removeAt(operatorStack.size - 1)]!!
                    val b = outputQueue.removeAt(outputQueue.size - 1)
                    val a = outputQueue.removeAt(outputQueue.size - 1)
                    outputQueue.add(op.func(a, b))
                }
                if (operatorStack.isNotEmpty()) {
                    operatorStack.removeAt(operatorStack.size - 1)  // Pop the '('
                }
            }
        }
    }

    while (operatorStack.isNotEmpty()) {
        val op = operators[operatorStack.removeAt(operatorStack.size - 1)]!!
        val b = outputQueue.removeAt(outputQueue.size - 1)
        val a = outputQueue.removeAt(outputQueue.size - 1)
        outputQueue.add(op.func(a, b))
    }

    return outputQueue.removeAt(outputQueue.size - 1)
}

data class Operator(val precedence: Int, val func: (Double, Double) -> Double)

fun evaluateExpression(expression: String): String {
    val parts = expression.split('+').map { it.trim() }

    var result = ""
    var numberBuffer: Int? = null
    for (part in parts) {
        when {
            part.startsWith('[') && part.contains("][") -> {
                val arrayMatch = Regex("\\[(.*?)\\]\\[(.*?)\\]").find(part)
                if (arrayMatch != null) {
                    val array = arrayMatch.groupValues[1].split(",").map { it.trim() }
                    val index = parseExpression(arrayMatch.groupValues[2]).toInt()
                    result += (numberBuffer?.toString() ?: "") + array[index]
                    numberBuffer = null
                }
            }
            part.toIntOrNull() != null -> numberBuffer = (numberBuffer ?: 0) + part.toInt()
            else -> {
                result += (numberBuffer?.toString() ?: "") + part.replace(Regex("['\"]"), "")
                numberBuffer = null
            }
        }
    }
    return result + (numberBuffer?.toString() ?: "")
}

fun getMultiline(input: String): MutableList<Any?> {
    val result = mutableListOf<Any?>()
    var depth = 0
    var start = 0
    var inQuote = false
    var i = 0
    while (i < input.length) {
        if (input[i] == '"' && (i == 0 || input[i - 1] != '\\')) {
            inQuote = !inQuote
        }
        if (inQuote) {
            i++
            continue
        }
        when (input[i]) {
            '(', '{' -> {
                if (depth == 0) start = i
                depth++
            }
            ')', '}' -> {
                depth--
                if (depth == 0) {
                    result.add(input.substring(start + 1, i))
                }
            }
            else -> if (depth == 0 && input[i].isWhitespace().not()) {
                val end = input.slice(i until input.length).indexOfFirst { it.isWhitespace() }.let { if (it == -1) input.length else it + i }
                result.add(input.substring(i, end))
                i = end
            }
        }
        i++
    }
    return result
}

fun validOperator(operator: String): String {
    val validOperator = when (operator) {
        "inc", "+=" -> "increment"
        "dec", "-=" -> "decrement"
        "=" -> "set"
        "mult", "*=" -> "multiply"
        "div", "/=", "//=" -> "divide"
        else -> operator
    }
    return if (validOperator.lowercase() in listOf("increment", "decrement", "set", "multiply", "divide")) validOperator.uppercase() else "Unknown operator &e$operator&c on &eline {line}"
}

fun validComparator(comparator: String): String {
    val validComparator = when (comparator.lowercase()) {
        "=", "==", "equal" -> "EQUAL"
        "<" -> "Less Than"
        "=<", "<=" -> "Less Than or Equal"
        ">" -> "Greater Than"
        "=>", ">=" -> "Greater Than or Equal"
        else -> comparator
    }
    return if (validComparator in listOf("EQUAL", "Less Than", "Less Than or Equal", "Greater Than", "Greater Than or Equal")) validComparator else "Unknown comparator &e$comparator&c on &eline {line}"
}
data class Conversion(val regex: Regex, val replacement: String)

data class Macro(
    val name: String,
    val value: String
)

data class Context(val context: String, val contextTarget: ContextTarget)

data class TrueAction(
    val line: String,
    val trueLine: Int
)

data class ContextTarget(
    val name: String? = null
)

data class Component(
    val type: String,
    var parentIndex: String? = null
): HashMap<String, Any?>() {
    override fun toString(): String {
        return "Component(type=$type, ${if (parentIndex == null) "" else "parentIndex=$parentIndex, "}entries=${super.toString()})"
    }
}

data class ActionObj(
    val context: Context,
    val actionList: MutableList<TrueAction>,
    var actions: MutableList<Component?> = mutableListOf()
) {
    fun getConditionsCount(): Int {
        return actions.count { it?.type == "CONDITIONS" }
    }

    fun getActionsCount(): Int {
        return actions.count()
    }
}
