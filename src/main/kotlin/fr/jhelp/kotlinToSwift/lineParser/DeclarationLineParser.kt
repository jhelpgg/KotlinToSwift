package fr.jhelp.kotlinToSwift.lineParser

import java.util.regex.Pattern

private val PATTERN_DECLARATION =
    Pattern.compile("((?:private|internal|public)\\s+)?((?:@T|t)ry\\s+)?(?:const\\s+)?(va[rl]\\s+)([a-zA-Z][a-zA-Z0-9_]*)(.*)")
private const val GROUP_DECLARATION_PRIVATE = 1
private const val GROUP_DECLARATION_TRY = 2
private const val GROUP_DECLARATION_VALR = 3
private const val GROUP_DECLARATION_NAME = 4
private const val GROUP_DECLARATION_SPECIFICATION = 5
const val PRIVATE_SET = "private set"
const val INTERNAL_SET = "internal set"

class DeclarationLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcher = PATTERN_DECLARATION.matcher(trimLine)

        if (matcher.matches())
        {
            val parsed = StringBuilder()

            matcher.group(GROUP_DECLARATION_PRIVATE)?.let { parsed.append(it) }

            matcher.group(GROUP_DECLARATION_TRY)?.let { parsed.append(it) }

            if (matcher.group(GROUP_DECLARATION_VALR).startsWith("val"))
            {
                parsed.append("let ")
            }
            else
            {
                parsed.append("var ")
            }

            parsed.append(matcher.group(GROUP_DECLARATION_NAME))
            val specification = matcher.group(GROUP_DECLARATION_SPECIFICATION)

            if (specification.endsWith(PRIVATE_SET))
            {
                parsed.insert(0, "private(set) ")
                parsed.append(specification.substring(0, specification.length - PRIVATE_SET.length))
            }
            else if (specification.endsWith(INTERNAL_SET))
            {
                parsed.insert(0, "internal(set) ")
                parsed.append(specification.substring(0, specification.length - INTERNAL_SET.length))
            }
            else
            {
                parsed.append(specification)
            }

            return if (matcher.group(GROUP_DECLARATION_TRY) != null)
            {
                val annotationLineParser = AnnotationLineParser()
                annotationLineParser.parse(parsed.toString())
            }
            else
            {
                parsed.toString()
            }
        }

        return ""
    }
}