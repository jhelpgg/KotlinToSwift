package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.KotlinToSwiftOptions
import fr.jhelp.kotlinToSwift.splitIgnoreOpenClose
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
private val FUNCTION_PATTERN =
    Pattern.compile("(?:(@Throws)\\s+)?((?:(?:override|private|public|internal|open)\\s+)*fun)(\\s+<[a-zA-Z0-9_, :<>]+>)?(\\s+[a-zA-Z][a-zA-Z0-9_<>.]*\\s*)\\(((?:.|\\n)*)\\)(?:\\s*:\\s*([a-zA-Z][a-zA-Z0-9_<>?, ]*))?(\\s*\\{)?")
private const val GROUP_FUN_THROW = 1
private const val GROUP_FUN_DECLARATION = 2
private const val GROUP_FUN_GENERIC = 3
private const val GROUP_FUN_NAME = 4
private const val GROUP_FUN_PARAMETERS = 5
private const val GROUP_FUN_RETURN_TYPE = 6
private const val GROUP_FUN_END_CURLY = 7
private val VISIBILITY_PATTERN = Pattern.compile("private|public|internal")
private val REMOVE_OVERRIDE_OF = arrayOf("toString")
private const val ESCAPING = "@Escaping"
private const val DISCARDABLE_RESULT = "@discardableResult\n    "

class FunLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcherFun = FUNCTION_PATTERN.matcher(trimLine)

        if (matcherFun.matches())
        {
            val parsed = StringBuilder()
            var name = matcherFun.group(GROUP_FUN_NAME)

            val indexPoint = name.lastIndexOf('.')

            if (indexPoint >= 0)
            {
                val indexSpace = name.lastIndexOf(' ', indexPoint)

                name =
                    if (indexSpace >= 0)
                    {
                        name.substring(0, indexSpace + 1) + name.substring(indexPoint + 1)
                    }
                    else
                    {
                        name.substring(indexPoint + 1)
                    }
            }

            var declaration = matcherFun.group(GROUP_FUN_DECLARATION)

            if (name.trim() in REMOVE_OVERRIDE_OF && declaration.startsWith("override"))
            {
                declaration = declaration.substring(8).trim()
            }

            if (KotlinToSwiftOptions.automaticPublic && !VISIBILITY_PATTERN.matcher(declaration).find() && !declaration.contains("open"))
            {
                declaration = "public $declaration"
            }

            // Declaration in Swift is the same, just have to add a 'c' at end to transform 'fun' to 'func'
            parsed.append(declaration)
            parsed.append('c')

            // Function name
            parsed.append(name)

            // generic part
            matcherFun.group(GROUP_FUN_GENERIC)?.let { generic -> parsed.append(generic) }

            // Function parameters
            parsed.append('(')
            parseParameters(matcherFun.group(GROUP_FUN_PARAMETERS), parsed)
            parsed.append(')')

            matcherFun.group(GROUP_FUN_THROW)?.let { parsed.append(" throws ") }

            // Return type
            matcherFun.group(GROUP_FUN_RETURN_TYPE)?.let {
                parsed.append(" -> ")
                parsed.append(it)

                parsed.insert(0, DISCARDABLE_RESULT)
                println("REMOVE_ME will add @discardableResult for : \n$trimLine")
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

    val parametersList = parametersTrim.splitIgnoreOpenClose(',', Pair('<', '>'))
    parseParameter(parametersList[0].trim(), parsed)

    (1 until parametersList.size).forEach { index ->
        parsed.append(", ")
        parseParameter(parametersList[index].trim(), parsed)
    }
}

fun parseParameter(parameter: String, parsed: StringBuilder)
{
    var hasEscaping = false
    val parameterClean =
        if (parameter.startsWith(ESCAPING))
        {
            hasEscaping = true
            parameter.substring(ESCAPING.length).trim()
        }
        else
        {
            parameter
        }

    parsed.append("_ ")

    if (parameterClean.contains("->"))
    {
        val index = parameterClean.indexOf(':')
        parsed.append(parameterClean.substring(0, index + 1))

        if (hasEscaping)
        {
            parsed.append(" @escaping ")
        }
        else
        {
            parsed.append(' ')
        }

        parsed.append(parameterClean.substring(index + 1))
    }
    else
    {
        parsed.append(parameterClean)
    }
}
