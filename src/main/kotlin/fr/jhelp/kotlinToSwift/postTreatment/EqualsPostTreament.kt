package fr.jhelp.kotlinToSwift.postTreatment

import java.util.regex.Pattern

private val CLASS_DECLARATION_PATTERN = Pattern.compile("class\\s+([a-zA-Z0-9_]+)(\\s*:[^{]*)?(\\s*\\{)")
private const val GROUP_CLASS_NAME = 1
private const val GROUP_IMPLEMENTS = 2
private const val GROUP_START_CURLY = 3
private val EQUALS_OVERRIDE_PATTERN =
    Pattern.compile("((?:public|internal)\\s+)?override\\s+(fun(?:c)?\\s+equals\\s*\\()")
private const val GROUP_PUBLIC_INTERNAL = 1
private const val GROUP_FUNCTION_DECLARATION = 2

fun parseEqualsInFile(file: String): String
{
    val matcherClass = CLASS_DECLARATION_PATTERN.matcher(file)

    if (!matcherClass.find())
    {
        return file
    }

    val matcherEquals = EQUALS_OVERRIDE_PATTERN.matcher(file)

    if (!matcherEquals.find())
    {
        return file
    }

    val result = StringBuilder()
    result.append(file.substring(0, matcherClass.start()))
    result.append("class ")
    val className = matcherClass.group(GROUP_CLASS_NAME)
    result.append(className)
    matcherClass.group(GROUP_IMPLEMENTS)?.let { implemented ->
        result.append(implemented)
        result.append(", Equatable ")
    }
    ?: result.append(" : Equatable ")
    result.append(matcherClass.group(GROUP_START_CURLY))
    result.append("\n     static public func == (lhs: ")
    result.append(className)
    result.append(", rhs: ")
    result.append(className)
    result.append(") -> Bool\n     {\n          return lhs.equals(rhs)\n     }\n\n")
    result.append(file.substring(matcherClass.end(), matcherEquals.start()))
    matcherEquals.group(GROUP_PUBLIC_INTERNAL)?.let { publicInternal -> result.append(publicInternal) }
    result.append(matcherEquals.group(GROUP_FUNCTION_DECLARATION))
    result.append(file.substring(matcherEquals.end()))

    return result.toString()
}