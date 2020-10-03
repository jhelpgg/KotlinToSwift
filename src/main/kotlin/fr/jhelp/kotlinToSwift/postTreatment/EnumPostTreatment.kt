package fr.jhelp.kotlinToSwift.postTreatment

import java.util.regex.Pattern

private val enumClassPattern = Pattern.compile("enum\\s+class\\s+([a-zA-Z0-9_]+)(\\s*)\\{")
private const val GROUP_NAME = 1
private const val GROUP_SPACE = 2


fun parseEnumFile(file: String): String
{
    val matcher = enumClassPattern.matcher(file)

    if (!matcher.find())
    {
        return file
    }

    val transformed = StringBuilder()
    transformed.append(file.substring(0, matcher.start()))
    transformed.append("public enum ")
    transformed.append(matcher.group(GROUP_NAME))
    transformed.append(matcher.group(GROUP_SPACE))
    transformed.append("{")
    transformed.append(matcher.group(GROUP_SPACE))
    transformed.append("   case ")
    transformed.append(file.substring(matcher.end()))
    return transformed.toString()
}