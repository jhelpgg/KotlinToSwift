package fr.jhelp.kotlinToSwift

import java.util.regex.Pattern

//((?:[^.]|\.[^g]|\.g[^u]|\.gu[^a]|\.gua[^r]]|\.guar[^d]]|\.guard[a-zA-Z0-9_])*)\.guard(\s*{.*})
private val PATTERN_GUARD = Pattern.compile("(.*)\\.guard(\\s*\\{\\s*throw)(\\s+)([^}]+})")
private const val GROUP_GUARD_BEFORE = 1
private const val GROUP_GUARD_OPEN_CURLY_THROW = 2
private const val GROUP_GUARD_SPACE_BEFORE = 3
private const val GROUP_GUARD_EXCEPTION_CLOSE_CURLY = 4

class GuardInterfaceLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcher = PATTERN_GUARD.matcher(trimLine)

        if (matcher.matches())
        {
            val parsed = StringBuilder()
            parsed.append("guard ")
            parsed.append(matcher.group(GROUP_GUARD_BEFORE))
            parsed.append(" else ")
            parsed.append(matcher.group(GROUP_GUARD_OPEN_CURLY_THROW))
            parsed.append(matcher.group(GROUP_GUARD_SPACE_BEFORE))
            parsed.append("CommonManagedExceptions.")
            parsed.append(matcher.group(GROUP_GUARD_EXCEPTION_CLOSE_CURLY))
            return parsed.toString()
        }

        return ""
    }
}