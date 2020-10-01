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