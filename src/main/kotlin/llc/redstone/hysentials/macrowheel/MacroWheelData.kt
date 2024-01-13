package llc.redstone.hysentials.macrowheel

import llc.redstone.hysentials.util.JsonData
import org.json.JSONArray
import org.json.JSONObject
import javax.crypto.Mac

class MacroWheelData {
    data class MacroWheel(
        var command: String,
        var icon: String,
        var hoverText: String
    ) {
        companion object {
            fun deserialize(json: JSONObject): MacroWheel {
                return MacroWheel(
                    json.getString("command"),
                    json.getString("icon"),
                    json.getString("hoverText")
                )
            }
        }

        fun serialize(): JSONObject {
            val json = JSONObject()
            json.put("command", command)
            json.put("icon", icon)
            json.put("hoverText", hoverText)
            return json
        }


    }

    class MacroJson : JsonData("./config/hysentials/macros.json", JSONObject().let {
        it.put("macros", JSONArray())
        it
    }) {
        fun addMacro(macro: MacroWheel) {
            val json = jsonObject.getJSONArray("macros")
            json.put(macro.serialize())
            save()
        }

        fun removeMacro(index: Int) {
            val json = jsonObject.getJSONArray("macros")
            json.remove(index)
            save()
        }

        fun setMacro(index: Int, macro: MacroWheel) {
            val json = jsonObject.getJSONArray("macros")
            json.put(index, macro.serialize())
            save()
        }

        fun getMacro(index: Int): MacroWheel {
            val json = jsonObject.getJSONArray("macros")
            return MacroWheel.deserialize(json.getJSONObject(index))
        }

        fun getMacros(): ArrayList<MacroWheel> {
            val macros = ArrayList<MacroWheel>()
            val json = jsonObject.getJSONArray("macros")
            for (i in 0 until json.length()) {
                val macro = MacroWheel.deserialize(json.getJSONObject(i))
                macros.add(macro)
            }
            return macros
        }
    }
}