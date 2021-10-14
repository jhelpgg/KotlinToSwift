package fr.jhelp.kotlinToSwift.lineParser

import java.util.regex.Pattern

private val PATTERN_CLOSURE_LAMBDA = Pattern.compile("(\\{[^-]+)->")
private val PATTERN_WEAK_SELF = Pattern.compile("@WeakSelf\\(\"(.*)\"\\)\\s*val\\s+sSelf\\s*=\\s*self")
private val PATTERN_WEAK_SELF_PARAMETER = Pattern.compile("\\{([^-]+)->\\s+@WeakSelf\\(\"(.*)\"\\)\\s*val\\s+sSelf\\s*=\\s*self")
private const val GROUP_CLOSURE_LAMBDA_DECLARATION = 1
private const val GROUP_CLOSURE_LAMBDA_RETURN_VALUE = 2
private const val GROUP_CLOSURE_LAMBDA_ONLY_RETURN_VALUE = 1
private const val WEAK_SELF = " [weak self] "
private const val GUARD_ELSE_HEADER = "\nguard let sSelf = self else { return "

class ClosureLambdaLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        var matcher = PATTERN_WEAK_SELF_PARAMETER.matcher(trimLine)

        if (matcher.find())
        {
            val stringBuilder = StringBuilder(trimLine.substring(0, matcher.start()))
            stringBuilder.append("{")
            stringBuilder.append(WEAK_SELF)
            stringBuilder.append(matcher.group(GROUP_CLOSURE_LAMBDA_DECLARATION))
            stringBuilder.append(" in ")
            stringBuilder.append(GUARD_ELSE_HEADER)
            stringBuilder.append(matcher.group(GROUP_CLOSURE_LAMBDA_RETURN_VALUE)
                                     .replace("\\\"", "\"")
                                     .replace("\\\\", "\\")
                                     .replace("\\n", "\n")
                                     .replace("\\t", "\t"))
            stringBuilder.append(" }\n")
            stringBuilder.append(trimLine.substring(matcher.end()).trim())
            return stringBuilder.toString()
        }

        matcher = PATTERN_WEAK_SELF.matcher(trimLine)

        if(matcher.find())
        {
            val stringBuilder = StringBuilder(trimLine.substring(0, matcher.start()))
            stringBuilder.append(WEAK_SELF)
            stringBuilder.append(" in ")
            stringBuilder.append(GUARD_ELSE_HEADER)
            stringBuilder.append(matcher.group(GROUP_CLOSURE_LAMBDA_ONLY_RETURN_VALUE)
                                     .replace("\\\"", "\"")
                                     .replace("\\\\", "\\")
                                     .replace("\\n", "\n")
                                     .replace("\\t", "\t"))
            stringBuilder.append(" }\n")
            stringBuilder.append(trimLine.substring(matcher.end()).trim())
            return stringBuilder.toString()
        }

        matcher = PATTERN_CLOSURE_LAMBDA.matcher(trimLine)

        if (matcher.find())
        {
            if (trimLine.endsWith("->"))
            {
                return FORCE_LINE_CONTINUE
            }

            val stringBuilder = StringBuilder(trimLine.substring(0, matcher.start()))
            stringBuilder.append(matcher.group(GROUP_CLOSURE_LAMBDA_DECLARATION).trim())
            stringBuilder.append(" in ")
            stringBuilder.append(trimLine.substring(matcher.end()).trim())
            return stringBuilder.toString()
        }

        return ""
    }
}