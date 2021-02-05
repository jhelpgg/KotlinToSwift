package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.KotlinToSwiftOptions
import fr.jhelp.kotlinToSwift.endParenthesisIndex
import java.util.regex.Pattern

private val PATTERN_CONTAINS_CONSTRUCTOR = Pattern.compile("constructor\\s*\\(")
private val PATTERN_OVERRIDE = Pattern.compile("@?[Oo]verride")
private val PATTERN_PRIVATE_INTERNAL_PUBLIC = Pattern.compile("(private|internal|public)\\s+")
private const val MAY_THROWS = "@Throws"
private val PATTERN_SUPER_THIS = Pattern.compile(":\\s*(super|this)([^{]*)")
private const val GROUP_SUPER_THIS = 1
private const val GROUP_SUPER_THIS_PARAMETERS = 2

class ConstructorLineParser : LineParser
{

    override fun parse(trimLine: String): String
    {
        if (!PATTERN_CONTAINS_CONSTRUCTOR.matcher(trimLine).find())
        {
            return ""
        }

        if (!trimLine.endsWith('{'))
        {
            return FORCE_LINE_CONTINUE
        }

        val parsed = StringBuilder()

        if (PATTERN_OVERRIDE.matcher(trimLine).find())
        {
            parsed.append("override ")
        }

        var matcher = PATTERN_PRIVATE_INTERNAL_PUBLIC.matcher(trimLine)

        if (matcher.find())
        {
            parsed.append(matcher.group())
        }
        else if(KotlinToSwiftOptions.automaticPublic)
        {
            parsed.append("public ")
        }

        parsed.append("init(")
        val start = trimLine.indexOf('(')
        val end = endParenthesisIndex(trimLine, start + 1)
        parseParameters(trimLine.substring(start + 1, end - 1), parsed)
        parsed.append(")")

        if (trimLine.contains(MAY_THROWS))
        {
            parsed.append(" throws")
        }

        parsed.append("\n     {")
        matcher = PATTERN_SUPER_THIS.matcher(trimLine)

        if (matcher.find())
        {
            parsed.append("\n          ")
            var convenience = false

            if (matcher.group(GROUP_SUPER_THIS) == "this")
            {
                convenience = true
                parsed.append("self")
            }
            else
            {
                parsed.append("super")
            }

            parsed.append(".init")
            parsed.append(matcher.group(GROUP_SUPER_THIS_PARAMETERS))

            if (convenience)
            {
                parsed.insert(0, "convenience ")
            }
        }

        parsed.append("\n")
        return parsed.toString()
    }
}