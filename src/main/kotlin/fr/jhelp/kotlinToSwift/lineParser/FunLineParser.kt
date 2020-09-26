package fr.jhelp.kotlinToSwift.lineParser

import java.util.regex.Pattern

/**
 * Pattern for function detection
 *
 * Fun line looks like
 *
 *      [@Throws] [override|private|public|internal|open]* fun <name>([parameters]) [: <type>] [{]
 *
 *      [...] means optional
 *
 * In follow regular expression:
 *
 *      +-------------------------------------------------+----+----------------------------------------------------------+
 *      |             Group                               | ID |                         Capture                          |
 *      +-------------------------------------------------+----+----------------------------------------------------------+
 *      |             @Throws                             | 1  | May throw indicator => [@Throws] fun                     |
 *      | (?:(?:override|private|public|internal)\s+)*fun | 2  | 'fun' declaration => [override|private] fun              |
 *      |  \s+[a-zA-Z][a-zA-Z0-9_]*\s*                    | 3  | 'fun' name => <name>                                     |
 *      |             \(.*\)                              | 4  | 'fun' parameters (without parenthesis) => ([parameters]) |
 *      | \s*:\s*([a-zA-Z][a-zA-Z0-9_?]*)                 | 5  | 'fun' return type (note the : not capture) =>  : <type>  |
 *      |             \s*\{                               | 6  | Eventual { ending => {                                   |
 *      +-------------------------------------------------+----+----------------------------------------------------------+
 */
private val FUNCTION_PATTERN = Pattern.compile("(?:(@Throws)\\s+)?((?:(?:override|private|public|internal|open)\\s+)*fun)(\\s+[a-zA-Z][a-zA-Z0-9_]*\\s*)\\((.*)\\)(?:\\s*:\\s*([a-zA-Z][a-zA-Z0-9_<>?]*))?(\\s*\\{)?")
private const val GROUP_FUN_THROW = 1
private const val GROUP_FUN_DECLARATION = 2
private const val GROUP_FUN_NAME = 3
private const val GROUP_FUN_PARAMETERS = 4
private const val GROUP_FUN_RETURN_TYPE = 5
private const val GROUP_FUN_END_CURLY = 6
private val REMOVE_OVERRIDE_OF = arrayOf("toString")

class FunLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcherFun = FUNCTION_PATTERN.matcher(trimLine)

        if (matcherFun.matches())
        {
            val parsed = StringBuilder()
            val name = matcherFun.group(GROUP_FUN_NAME)
            var declaration = matcherFun.group(GROUP_FUN_DECLARATION)

            if (name.trim() in REMOVE_OVERRIDE_OF && declaration.startsWith("override"))
            {
                declaration = declaration.substring(8).trim()
            }

            // Declaration in Swift is the same, just have to add a 'c' at end to transform 'fun' to 'func'
            parsed.append(declaration)
            parsed.append('c')

            // Function name
            parsed.append(name)

            // Function parameters
            parsed.append('(')
            parseParameters(matcherFun.group(GROUP_FUN_PARAMETERS), parsed)
            parsed.append(')')

            matcherFun.group(GROUP_FUN_THROW)?.let { parsed.append(" throws ") }

            // Return type
            matcherFun.group(GROUP_FUN_RETURN_TYPE)?.let {
                parsed.append(" -> ")
                parsed.append(it)
            }

            matcherFun.group(GROUP_FUN_END_CURLY)?.let { parsed.append(it) }
            return parsed.toString()
        }

        return ""
    }
}

fun parseParameters(parameters: String, parsed: StringBuilder)
{
    val parametersTrim = parameters.trim()

    if (parametersTrim.isEmpty())
    {
        return
    }

    val parametersList = parametersTrim.split(",")
    parseParameter(parametersList[0].trim(), parsed)

    (1 until parametersList.size).forEach { index ->
        parsed.append(", ")
        parseParameter(parametersList[index].trim(), parsed)
    }
}

fun parseParameter(parameter: String, parsed: StringBuilder)
{
    parsed.append("_ ")

    if (parameter.contains("->"))
    {
        val index = parameter.indexOf(':')
        parsed.append(parameter.substring(0, index + 1))
        parsed.append(" @escaping ")
        parsed.append(parameter.substring(index + 1))
    }
    else
    {
        parsed.append(parameter)
    }
}
