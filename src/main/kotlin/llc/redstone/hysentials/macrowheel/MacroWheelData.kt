package llc.redstone.hysentials.macrowheel

import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.util.JsonData
import llc.redstone.hysentials.util.Material
import org.json.JSONArray
import org.json.JSONObject
import javax.crypto.Mac

class MacroWheelData {
    data class MacroWheel(
        var index: Int,
        var name: String,
        var command: String,
        var icon: Material, // Item Material
        var hoverText: String
    ) {
        companion object {
            fun deserialize(json: JSONObject): MacroWheel {
                return MacroWheel(
                    json.getInt("index"),
                    json.getString("name"),
                    json.getString("command"),
                    Material.valueOf(json.getString("icon")),
                    json.getString("hoverText")
                )
            }
        }

        fun serialize(): JSONObject {
            val json = JSONObject()
            json.put("index", index)
            json.put("name", name)
            json.put("command", command)
            json.put("icon", icon.name)
            json.put("hoverText", hoverText)
            return json
        }

        fun save() {
            Hysentials.INSTANCE.macroJson.setMacro(index, this)
        }


    }

    class MacroJson : JsonData("./config/hysentials/macros.json", JSONObject().let {
        it.put("macros", JSONObject())
        it
    }) {
        fun addMacro(macro: MacroWheel) {
            val json = jsonObject.getJSONObject("macros")
            json.put(macro.index.toString(), macro.serialize())
            save()
        }

        fun removeMacro(index: Int) {
            val json = jsonObject.getJSONObject("macros")
            json.remove(index.toString())
            save()
        }

        fun setMacro(index: Int, macro: MacroWheel) {
            val json = jsonObject.getJSONObject("macros")
            json.put(index.toString(), macro.serialize())
            save()
        }

        fun getMacro(index: Int): MacroWheel? {
            val json = jsonObject.getJSONObject("macros")
            if (!json.has(index.toString())) return null
            return MacroWheel.deserialize(json.getJSONObject(index.toString()))
        }

        fun getMacros(): HashMap<Int, MacroWheel> {
            val macros = HashMap<Int, MacroWheel>()
            val json = jsonObject.getJSONObject("macros")
            for (i in json.keys()) {
                val macro = MacroWheel.deserialize(json.getJSONObject(i.toString()))
                macros[macro.index] = macro
            }
            return macros
        }
    }
}