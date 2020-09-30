package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.endCurlyIndex
import java.io.File
import java.util.regex.Pattern

private val startCompanionPattern = Pattern.compile("companion\\s+object\\s*\\{")
private val funcVarLetPattern = Pattern.compile("(func|var|let)\\s")
private const val TYPE_GROUP = 1

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
    val insideCompanion = file.substring(start, end - 1)
    val matcherCompanion = funcVarLetPattern.matcher(insideCompanion)
    var indexCompanion = 0
    var curlyIndex = 0
    var type = ""

    while(matcherCompanion.find())
    {
        if( matcherCompanion.start()<indexCompanion)
        {
            continue
        }

        transformed.append(insideCompanion.substring(indexCompanion, matcherCompanion.start()))
        transformed.append("static ")
        type =     matcherCompanion.group(TYPE_GROUP)
        transformed.append(type)
        transformed.append(" ")

        if(type == "func")
        {
            curlyIndex = insideCompanion.indexOf('{', matcherCompanion.end())
            indexCompanion =  endCurlyIndex(insideCompanion, curlyIndex + 1)
            transformed.append(insideCompanion.substring(matcherCompanion.end(), indexCompanion))
            continue
        }

        indexCompanion = matcherCompanion.end()
    }

    transformed.append(insideCompanion.substring(indexCompanion))
    transformed.append(file.substring(end))
    return transformed.toString()
}