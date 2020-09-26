package fr.jhelp.kotlinToSwift

import java.io.File
import java.util.regex.Pattern

private val startConstructorPattern = Pattern.compile("(init\\s*\\([^{]*\\{)(\\s*)(super.init\\s*\\([^)]*\\))")
private const val GROUP_CONSTRUCTOR = 1
private const val GROUP_SPACE = 2
private const val GROUP_SUPER = 3

fun parseConstructorInFile(file: String): String
{
    val matcher = startConstructorPattern.matcher(file)
    var first = 0
    var before: Int
    var start : Int
    var end = 0
    val transformed = StringBuilder()

    while (matcher.find())
    {
        before = matcher.start()
        start = matcher.end()
        end = endCurlyIndex(file, start)
        transformed.append(file.substring(first, before))
        transformed.append(matcher.group(GROUP_CONSTRUCTOR))
        transformed.append(file.substring(start, end-1))
        transformed.append(matcher.group(GROUP_SUPER))
        transformed.append(matcher.group(GROUP_SPACE))
        transformed.append("}")
        first = end
    }

    transformed.append(file.substring(end))
    return transformed.toString()
}