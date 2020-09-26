package fr.jhelp.kotlinToSwift.lineParser

import java.util.regex.Pattern

private val PATTERN_IF_WHILE_FOR = Pattern.compile("(if|while|for)\\s*\\((.*)\\)(\\s*\\{)?")
private const val GROUP_IF_WHILE_FOR_KEY_WORD = 1
private const val GROUP_IF_WHILE_FOR_CONDITION = 2
private const val GROUP_IF_WHILE_FOR_CURLY = 3

class IfForWhileLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcher = PATTERN_IF_WHILE_FOR.matcher(trimLine)

        if (matcher.matches())
        {
            val parsed = StringBuilder()
            parsed.append(matcher.group(GROUP_IF_WHILE_FOR_KEY_WORD))
            parsed.append(' ')
            parsed.append(matcher.group(GROUP_IF_WHILE_FOR_CONDITION))

            matcher.group(GROUP_IF_WHILE_FOR_CURLY)?.let {
                parsed.append(' ')
                parsed.append(it)
            }

            return parsed.toString()
        }

        return ""
    }
}