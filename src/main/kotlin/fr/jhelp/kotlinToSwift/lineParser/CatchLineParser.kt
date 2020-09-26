package fr.jhelp.kotlinToSwift.lineParser

import java.util.regex.Pattern

private val PATTERN_CATCH = Pattern.compile("(}\\s*)?catch\\s*\\([^)]*\\)(\\s*\\{)?")
private const val GROUP_CATCH_CLOSE_CURLY = 1
private const val GROUP_CATCH_OPEN_CURLY = 2

class CatchLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcher = PATTERN_CATCH.matcher(trimLine)

        if (matcher.matches())
        {
            val parsed = StringBuilder()
            matcher.group(GROUP_CATCH_CLOSE_CURLY)?.let { parsed.append(it) }
            parsed.append("catch")
            matcher.group(GROUP_CATCH_OPEN_CURLY)?.let { parsed.append(it) }
            return parsed.toString()
        }

        return ""
    }
}