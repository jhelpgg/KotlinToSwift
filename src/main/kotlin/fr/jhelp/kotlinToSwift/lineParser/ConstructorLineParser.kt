package fr.jhelp.kotlinToSwift.lineParser

import java.util.regex.Pattern

private val PATTERN_CONSTRUCTOR = Pattern.compile("(@Throws\\s+)?(@Override\\s+)?constructor\\s*\\(([^)]*)\\)[^:{]*(:\\s*(super|this)([^{]*))?(\\s*\\{)")
private const val GROUP_CONSTRUCTOR_THROWS = 1
private const val GROUP_CONSTRUCTOR_OVERRIDE = 2
private const val GROUP_CONSTRUCTOR_PARAMETERS = 3
private const val GROUP_CONSTRUCTOR_SUPER_THIS = 5
private const val GROUP_CONSTRUCTOR_SUPER_THIS_PARAMETER = 6
private const val GROUP_CONSTRUCTOR_CURLY = 7

class ConstructorLineParser : LineParser
{

    override fun parse(trimLine: String): String
    {
        val matcher = PATTERN_CONSTRUCTOR.matcher(trimLine)

        if (matcher.matches())
        {
            val parsed = StringBuilder()

            matcher.group(GROUP_CONSTRUCTOR_OVERRIDE)?.let { parsed.append("override ") }
            parsed.append("init(")
            parseParameters(matcher.group(GROUP_CONSTRUCTOR_PARAMETERS), parsed)
            parsed.append(")")
            matcher.group(GROUP_CONSTRUCTOR_THROWS)?.let { parsed.append(" throws ") }
            parsed.append(matcher.group(GROUP_CONSTRUCTOR_CURLY))
            var convenience = false

            matcher.group(GROUP_CONSTRUCTOR_SUPER_THIS)?.let { superThis ->
                parsed.append("\n     ")

                if (superThis == "this")
                {
                    convenience = true
                    parsed.append("self")
                }
                else
                {
                    parsed.append("super")
                }

                parsed.append(".init")
                parsed.append(matcher.group(GROUP_CONSTRUCTOR_SUPER_THIS_PARAMETER))
            }

            if (convenience)
            {
                parsed.insert(0, "convenience ")
            }

            return parsed.toString()
        }

        return ""
    }
}