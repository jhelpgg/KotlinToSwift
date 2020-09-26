package fr.jhelp.kotlinToSwift.lineParser

private val AS_IS_ELEMENTS = arrayOf("{", "}")

class AsIsLineParser : LineParser
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