package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.endCurlyIndex
import java.io.File
import java.util.regex.Pattern

private val startCompanionPattern = Pattern.compile("companion\\s+object\\s*\\{")

fun parseCompanionInFile(file: String): String
{
    val matcher = startCompanionPattern.matcher(file)

    if (!matcher.find())
    {
        return file
    }

    val before = matcher.start()
    val start = matcher.end()
    val end = endCurlyIndex(file, start)
    val transformed = StringBuilder()
    transformed.append(file.substring(0, before))
    transformed.append(file.substring(start, end - 1)
                           .replace("func ", "static func ")
                           .replace("var ", "static var ")
                           .replace("let ", "static let "))
    transformed.append(file.substring(end))
    return transformed.toString()
}