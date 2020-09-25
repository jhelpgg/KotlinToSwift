package fr.jhelp.kotlinToSwift

private val AS_IS_ELEMENTS = arrayOf("{", "}")

class AsIsParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        if(trimLine in AS_IS_ELEMENTS)
        {
            return trimLine
        }

        return ""
    }
}