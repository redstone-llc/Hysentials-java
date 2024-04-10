package llc.redstone.hysentials.utils

data class Conditional(
    var conditions: List<String>,
    var actions: List<String>,
    var elseActions: List<String>,
    var any: Boolean,
    var index: Int,
    var page: Int
)