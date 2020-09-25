package fr.jhelp.kotlinToSwift

import java.io.File
import java.util.regex.Pattern

private val startCompanionPattern = Pattern.compile("companion\\s+object\\s*\\{")

fun parseCompanionInFiles(files: List<File>)
{
    var transformed: String

    for (file in files)
    {
        transformed = parseCompanionInFile(file.readText())
        file.writeText(transformed)
    }
}

fun parseCompanionInFile(file: String): String
{
    val matcher = startCompanionPattern.matcher(file)

    if (!matcher.find())
    {
        return file
    }

    val before = matcher.start()
    val start = matcher.end()
    val characters = file.toCharArray()
    var end = start
    var insideString = false
    var escaped = false
    var curlyCount = 0
    var mayCommentStart = false
    var mayMultiLineCommentEnd = false
    var lineComment = false
    var multilineComment = false

    while (curlyCount >= 0)
    {
        when (characters[end])
        {
            '\\' ->
            {
                if (!lineComment && !multilineComment)
                {
                    escaped = !escaped
                }

                mayMultiLineCommentEnd = false
                mayCommentStart = false
            }
            '"' ->
                if (!lineComment && !multilineComment)
                {
                    if (!escaped)
                    {
                        insideString = !insideString
                    }

                    escaped = false
                    mayMultiLineCommentEnd = false
                    mayCommentStart = false
                }
            '{' ->
                if (!lineComment && !multilineComment)
                {
                    if (!escaped && !insideString)
                    {
                        curlyCount++
                    }

                    escaped = false
                    mayMultiLineCommentEnd = false
                    mayCommentStart = false
                }
            '}' ->
                if (!lineComment && !multilineComment)
                {
                    if (!escaped && !insideString)
                    {
                        curlyCount--
                    }

                    escaped = false
                    mayMultiLineCommentEnd = false
                    mayCommentStart = false
                }
            '/' ->
                if (!lineComment && !multilineComment)
                {
                    if (!escaped && !insideString)
                    {
                        if (mayCommentStart)
                        {
                            mayCommentStart = false
                            lineComment = true
                        }
                        else
                        {
                            mayCommentStart = true
                        }
                    }
                    else
                    {
                        escaped = false
                        mayMultiLineCommentEnd = false
                        mayCommentStart = false
                    }
                }
                else if (mayMultiLineCommentEnd)
                {
                    escaped = false
                    mayMultiLineCommentEnd = false
                    multilineComment = false
                }
            '\n' ->
            {
                lineComment = false
                escaped = false
                mayMultiLineCommentEnd = false
                mayCommentStart = false
            }
            '*' ->
                if (!escaped && !insideString)
                {
                    if (mayCommentStart)
                    {
                        mayCommentStart = false
                        multilineComment = true
                    }
                    else if (multilineComment)
                    {
                        mayMultiLineCommentEnd = true
                    }
                }
                else
                {
                    escaped = false
                    mayMultiLineCommentEnd = false
                    mayCommentStart = false
                }
            else ->
            {
                escaped = false
                mayMultiLineCommentEnd = false
                mayCommentStart = false
            }
        }

        end++
    }

    val transformed = StringBuilder()
    transformed.append(file.substring(0, before))
    transformed.append(file.substring(start, end - 1).replace("func ", "static func "))
    transformed.append(file.substring(end))
    return transformed.toString()
}