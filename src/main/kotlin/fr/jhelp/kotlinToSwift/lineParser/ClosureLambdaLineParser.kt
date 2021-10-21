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
        val replacement = StringBuilder(trimLine)
        var lineToParse = trimLine
        var someReplacementHappen = false
        var loopReplacement = false

        do
        {
            loopReplacement = false
            lineToParse= replacement.toString()
            replacement.clear()

            var matcher = PATTERN_WEAK_SELF_PARAMETER.matcher(lineToParse)

            if (matcher.find())
            {
                replacement.append(lineToParse.substring(0, matcher.start()))
                replacement.append("{")
                replacement.append(WEAK_SELF)
                replacement.append(matcher.group(GROUP_CLOSURE_LAMBDA_DECLARATION))
                replacement.append(" in ")
                replacement.append(GUARD_ELSE_HEADER)
                replacement.append(matcher.group(GROUP_CLOSURE_LAMBDA_RETURN_VALUE)
                                         .replace("\\\"", "\"")
                                         .replace("\\\\", "\\")
                                         .replace("\\n", "\n")
                                         .replace("\\t", "\t"))
                replacement.append(" }\n")
                replacement.append(lineToParse.substring(matcher.end()).trim())
                someReplacementHappen = true
                loopReplacement = true
                continue
            }

            matcher = PATTERN_WEAK_SELF.matcher(lineToParse)

            if(matcher.find())
            {
                replacement.append(lineToParse.substring(0, matcher.start()))
                replacement.append(WEAK_SELF)
                replacement.append(" in ")
                replacement.append(GUARD_ELSE_HEADER)
                replacement.append(matcher.group(GROUP_CLOSURE_LAMBDA_ONLY_RETURN_VALUE)
                                         .replace("\\\"", "\"")
                                         .replace("\\\\", "\\")
                                         .replace("\\n", "\n")
                                         .replace("\\t", "\t"))
                replacement.append(" }\n")
                replacement.append(lineToParse.substring(matcher.end()).trim())
                someReplacementHappen = true
                loopReplacement = true
                continue
            }

            matcher = PATTERN_CLOSURE_LAMBDA.matcher(lineToParse)

            if (matcher.find())
            {
                if (trimLine.endsWith("->"))
                {
                    return FORCE_LINE_CONTINUE
                }

                replacement.append(lineToParse.substring(0, matcher.start()))
                replacement.append(matcher.group(GROUP_CLOSURE_LAMBDA_DECLARATION).trim())
                replacement.append(" in ")
                replacement.append(lineToParse.substring(matcher.end()))
                someReplacementHappen = true
                loopReplacement = true
                continue
            }
        } while (loopReplacement)

        if(someReplacementHappen) {
            return lineToParse
        }

        return  ""
    }
}