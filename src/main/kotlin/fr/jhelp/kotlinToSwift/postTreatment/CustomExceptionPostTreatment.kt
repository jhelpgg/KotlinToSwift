package fr.jhelp.kotlinToSwift.postTreatment

import java.util.regex.Pattern

private val EXCEPTION_PATTERN =
    Pattern.compile("\\s*class\\s+([a-zA-Z0-9_]+)\\s*\\(\\s*([a-zA-Z0-9_]+)\\s*:\\s*String\\s*\\)\\s*:\\s*Exception\\s*\\(\\s*\\2\\s*\\)\\s*")
private const val CLASS_NAME_GROUP = 1

fun parseExceptionFile(file: String): String
{
    val matcher = EXCEPTION_PATTERN.matcher(file)

    if (!matcher.matches())
    {
        return file
    }

    val result = StringBuilder()
    result.append("class ")
    result.append(matcher.group(CLASS_NAME_GROUP))
    result.append(" : Error, CustomStringConvertible\n{\n")
    result.append("\tpublic let message : String\n")
    result.append("\t\n")
    result.append("\tinit(_ message:String)\n")
    result.append("\t{\n")
    result.append("\t\tself.message = message\n")
    result.append("\t}\n")
    result.append("\n")
    result.append("\tvar description : String { return \"")
    result.append(matcher.group(CLASS_NAME_GROUP))
    result.append(" : \\(self.message)\" }\n")
    result.append("}\n")

    return result.toString()
}