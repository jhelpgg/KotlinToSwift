package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.indexOfIgnoreCommentString
import java.util.regex.Pattern

private val CLASS_DECLARATION_PATTERN =
    Pattern.compile("class\\s+([a-zA-Z0-9_]+)(\\s*:(?:\\s|.)*)Comparable<[^>]*>([^{]*\\{)")
private const val GROUP_CLASS_NAME = 1
private const val GROUP_IMPLEMENTS_BEFORE = 2
private const val GROUP_IMPLEMENTS_AFTER = 3
private val COMPARE_TO_OVERRIDE_PATTERN =
    Pattern.compile("((?:public|internal)\\s+)?override\\s+(fun(?:c)?\\s+compareTo\\s*\\()")
private const val GROUP_PUBLIC_INTERNAL = 1
private const val GROUP_FUNCTION_DECLARATION = 2

/**
 * Supposed called **AFTER** [parseEqualsInFile]
 */
fun parseComparableInFile(file: String): String
{
    val indexOpenCurly = file.indexOfIgnoreCommentString('{')

    if (indexOpenCurly < 0)
    {
        return file
    }

    val matcherClass = CLASS_DECLARATION_PATTERN.matcher(file.substring(0, indexOpenCurly + 1))

    if (!matcherClass.find())
    {
        return file
    }

    val matcherCompareTo = COMPARE_TO_OVERRIDE_PATTERN.matcher(file)

    if (!matcherCompareTo.find())
    {
        return file
    }

    val result = StringBuilder()
    result.append(file.substring(0, matcherClass.start()))
    result.append("class ")
    val className = matcherClass.group(GROUP_CLASS_NAME)
    result.append(className)
    val before = matcherClass.group(GROUP_IMPLEMENTS_BEFORE)
    val after = matcherClass.group(GROUP_IMPLEMENTS_AFTER)
    result.append(before)
    result.append("Comparable")
    result.append(after)

    if (!before.contains("Equatable") && !after.contains("Equatable"))
    {
        result.append("\n     static public func == (lhs: ")
        result.append(className)
        result.append(", rhs: ")
        result.append(className)
        result.append(") -> Bool\n     {\n          return lhs.compareTo(rhs) == 0\n     }\n\n")
    }

    result.append("\n     static public func < (lhs: ")
    result.append(className)
    result.append(", rhs: ")
    result.append(className)
    result.append(") -> Bool\n     {\n          return lhs.compareTo(rhs) < 0\n     }\n\n")

    result.append(file.substring(matcherClass.end(), matcherCompareTo.start()))
    matcherCompareTo.group(GROUP_PUBLIC_INTERNAL)?.let { publicInternal -> result.append(publicInternal) }
    result.append(matcherCompareTo.group(GROUP_FUNCTION_DECLARATION))
    result.append(file.substring(matcherCompareTo.end()))

    return result.toString()
}