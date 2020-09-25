package fr.jhelp.kotlinToSwift

import java.util.regex.Pattern

private val PATTERN_TRY = Pattern.compile("try(\\s*\\{)?")
private const val GROUP_TRY_CURLY = 1

class TryLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcher = PATTERN_TRY.matcher(trimLine)

        if (matcher.matches())
        {
            val parsed = StringBuilder()
            parsed.append("do")
            matcher.group(GROUP_TRY_CURLY)?.let { parsed.append(it) }
            return parsed.toString()
        }

        return ""
    }
}