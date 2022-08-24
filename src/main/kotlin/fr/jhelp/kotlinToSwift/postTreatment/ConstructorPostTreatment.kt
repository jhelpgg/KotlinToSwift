package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.endCurlyIndex
import java.util.regex.Pattern

private val startConstructorPattern = Pattern.compile("(init\\s*\\([^{]*\\{)(\\s*)(super.init\\s*\\([^)]*\\))")
private const val GROUP_CONSTRUCTOR = 1
private const val GROUP_SPACE = 2
private const val GROUP_SUPER = 3
private const val SUPER = "@Super"
private const val SUPER_LENGTH = SUPER.length

fun parseConstructorInFile(file: String): String
{
    val matcher = startConstructorPattern.matcher(file)
    var first = 0
    var before: Int
    var start: Int
    var end = 0
    var superIndex = -1
    val transformed = StringBuilder()

    while (matcher.find())
    {
        before = matcher.start()
        start = matcher.end()
        end = endCurlyIndex(file, start)
        transformed.append(file.substring(first, before))
        transformed.append(matcher.group(GROUP_CONSTRUCTOR))

        superIndex = file.indexOf(SUPER, start)

        if (superIndex in 0 until end)
        {
            transformed.append(file.substring(start, superIndex))
            transformed.append(matcher.group(GROUP_SUPER))
            transformed.append(file.substring(superIndex + SUPER_LENGTH, end))
        }
        else
        {
            transformed.append(file.substring(start, end - 1))
            transformed.append(matcher.group(GROUP_SUPER))
            transformed.append(matcher.group(GROUP_SPACE))
            transformed.append("}")
        }

        first = end
    }

    transformed.append(file.substring(end))
    return transformed.toString()
}