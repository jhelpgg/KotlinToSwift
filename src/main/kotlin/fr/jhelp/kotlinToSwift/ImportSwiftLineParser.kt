package fr.jhelp.kotlinToSwift

import java.lang.StringBuilder
import java.util.regex.Pattern

private val PATTERN_IMPORT_SWIFT = Pattern.compile("@ImportSwift\\s*\\(\\s*\"([a-zA-Z][a-zA-Z0-9_]*)\"\\s*\\)")
private const val GROUP_IMPORT_SWIFT_NAME = 1

class ImportSwiftLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcher = PATTERN_IMPORT_SWIFT.matcher(trimLine)

        if(matcher.matches())
        {
            val parsed = StringBuilder()
            parsed.append("import ")
            parsed.append(matcher.group(GROUP_IMPORT_SWIFT_NAME))
            return parsed.toString()
        }

        return ""
    }
}