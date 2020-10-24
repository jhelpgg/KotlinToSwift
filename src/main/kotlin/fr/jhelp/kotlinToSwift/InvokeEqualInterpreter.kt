package fr.jhelp.kotlinToSwift

import java.util.regex.Pattern

private val PATTERN_AVOID = Pattern.compile("\\s*(?:if|while|for|return)\\s*\\((?:.|\\s)*")

fun invokeEqualInterpreter(string: String): String
{
    if (PATTERN_AVOID.matcher(string).matches())
    {
        return string
    }

    val characters = string.toCharArray()
    var parenthesisCount = 0
    var insideString = false
    var insideQuote = false
    var escaped = false

    for (index in 0 until characters.size)
    {
        when (characters[index])
        {
            '\\' -> escaped = !escaped
            '"' ->
                when
                {
                    escaped      -> escaped = false
                    !insideQuote -> insideString = !insideString
                }
            '\'' ->
                when
                {
                    escaped       -> escaped = false
                    !insideString -> insideQuote = !insideQuote
                }
            '(' ->
                when
                {
                    escaped                       -> escaped = false
                    !insideString && !insideQuote -> parenthesisCount++
                }
            ')' ->
                when
                {
                    escaped                       -> escaped = false
                    !insideString && !insideQuote -> parenthesisCount--
                }
            '=' ->
                when
                {
                    escaped                                               -> escaped = false
                    !insideString && !insideQuote && parenthesisCount > 0 -> characters[index] = ':'
                }
            else -> escaped = false
        }
    }

    return String(characters)
}