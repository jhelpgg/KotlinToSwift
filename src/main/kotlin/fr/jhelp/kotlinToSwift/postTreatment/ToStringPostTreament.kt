package fr.jhelp.kotlinToSwift.postTreatment

import java.util.regex.Pattern

private val CLASS_DECLARATION_PATTERN = Pattern.compile("class\\s+([a-zA-Z0-9_]+)(\\s*:[^{]*)?(\\s*\\{)")
private const val GROUP_CLASS_NAME = 1
private const val GROUP_IMPLEMENTS = 2
private const val GROUP_START_CURLY = 3
private val TO_STRING_OVERRIDE_PATTERN = Pattern.compile("fun(?:c)?\\s+toString\\s*\\(")

fun parseToStringInFile(file: String): String
{
    val matcherClass = CLASS_DECLARATION_PATTERN.matcher(file)

    if (!matcherClass.find())
    {
        return file
    }

    val matcherToString = TO_STRING_OVERRIDE_PATTERN.matcher(file)

    if (!matcherToString.find())
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
        result.append(", CustomStringConvertible ")
    }
    ?: result.append(" : CustomStringConvertible ")
    result.append(matcherClass.group(GROUP_START_CURLY))
    result.append("\n     public var description: String { return self.toString() }\n\n")
    result.append(file.substring(matcherClass.end()))

    return result.toString()
}