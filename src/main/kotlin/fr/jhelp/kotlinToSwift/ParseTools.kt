package fr.jhelp.kotlinToSwift

/**
 * Compute index of }, corresponding to a {.
 *
 * It ignores { or } in String or in comments
 *
 * @param text Text where search the }
 * @param start Offset **after** the { to get it's corresponding } index
 */
fun endCurlyIndex(text: String, start: Int): Int
{
    val characters = text.toCharArray()
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

    return end
}

/**
 * Compute index of ), corresponding to a (.
 *
 * It ignores ( or ) in String or in comments
 *
 * @param text Text where search the )
 * @param start Offset **after** the ( to get it's corresponding ) index
 */
fun endParenthesisIndex(text: String, start: Int): Int
{
    val characters = text.toCharArray()
    var end = start
    var insideString = false
    var escaped = false
    var parenthesisCount = 0
    var mayCommentStart = false
    var mayMultiLineCommentEnd = false
    var lineComment = false
    var multilineComment = false

    while (parenthesisCount >= 0)
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
            '(' ->
                if (!lineComment && !multilineComment)
                {
                    if (!escaped && !insideString)
                    {
                        parenthesisCount++
                    }

                    escaped = false
                    mayMultiLineCommentEnd = false
                    mayCommentStart = false
                }
            ')' ->
                if (!lineComment && !multilineComment)
                {
                    if (!escaped && !insideString)
                    {
                        parenthesisCount--
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

    return end
}

fun String.indexOfIgnoreCommentString(character: Char, startIndex: Int = 0): Int
{
    val characters = this.toCharArray()
    var insideString = false
    var escaped = false
    var mayCommentStart = false
    var mayMultiLineCommentEnd = false
    var lineComment = false
    var multilineComment = false

    for (index in startIndex until characters.size)
    {
        when (characters[index])
        {
            character ->
                if (!insideString && !escaped && !lineComment && !multilineComment)
                {
                    return index
                }
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
            else      ->
            {
                escaped = false
                mayMultiLineCommentEnd = false
                mayCommentStart = false
            }
        }
    }

    return -1
}

private const val CHAR_0 = 0.toChar()

fun String.splitIgnoreOpenClose(delimiter: Char, vararg openClose: Pair<Char, Char>): List<String>
{
    val list = ArrayList<String>()
    val characters = this.toCharArray()
    var start = 0
    var openCount = 0
    var currentOpen = CHAR_0
    var waitClose = CHAR_0

    for ((index, character) in characters.withIndex())
    {
        if (waitClose == CHAR_0)
        {
            if (character == delimiter)
            {
                if (index > start)
                {
                    list.add(this.substring(start, index))
                }

                start = index + 1
            }
            else
            {
                for ((open, close) in openClose)
                {
                    if (character == open)
                    {
                        currentOpen = open
                        openCount = 1
                        waitClose = close
                        break
                    }
                }
            }
        }
        else if (character == waitClose)
        {
            openCount--

            if (openCount <= 0)
            {
                waitClose = CHAR_0
            }
        }
        else if (character == currentOpen)
        {
            openCount++
        }
    }

    if (start < this.length)
    {
        list.add(this.substring(start))
    }

    return list
}