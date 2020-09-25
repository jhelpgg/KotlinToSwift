package fr.jhelp.kotlinToSwift

import java.util.regex.Pattern

private val PATTERN_CLOSURE_LAMBDA = Pattern.compile("(\\{[^-]+)->")
private const val GROUP_CLOSURE_LAMBDA_DECLARATION = 1

class ClosureLambdaLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcher = PATTERN_CLOSURE_LAMBDA.matcher(trimLine)

        if (matcher.find())
        {
            val stringBuilder = StringBuilder(trimLine.substring(0, matcher.start()))
            stringBuilder.append(matcher.group(GROUP_CLOSURE_LAMBDA_DECLARATION).trim())
            stringBuilder.append(" in ")
            stringBuilder.append(trimLine.substring(matcher.end()).trim())
            return stringBuilder.toString()
        }

        return ""
    }
}