package fr.jhelp.kotlinToSwift

fun stringInterpret(string: String): String
{
    val characters = string.toCharArray()
    val length = characters.size
    val stringBuilder = StringBuilder()
    var insideString = false
    var escaped = false
    var curly = false
    var index = 0
    var start = -1
    var character: Char

    while (index < length)
    {
        character = characters[index]

        when (character)
        {
            '\\' ->
                when
                {
                    escaped ->
                    {
                        escaped = false
                        stringBuilder.append("\\\\")
                    }
                    else    -> escaped = true
                }
            '"'  ->
                when
                {
                    escaped ->
                    {
                        escaped = false
                        stringBuilder.append("\\\"")
                    }
                    start > 0 ->
                        {
                            if (index > start)
                            {
                                stringBuilder.append("\\(")
                                stringBuilder.append(string.substring(start, index))
                                stringBuilder.append(")")
                                stringBuilder.append(character)
                            }
                            else
                            {
                                stringBuilder.append("$")
                                stringBuilder.append(character)
                            }

                            start = -1
                        }
                    else    ->
                    {
                        insideString = !insideString
                        stringBuilder.append("\"")
                    }
                }
            '$'  ->
                when
                {
                    escaped      ->
                    {
                        escaped = false
                        stringBuilder.append("\$")
                    }
                    insideString -> start = index + 1
                    else         -> stringBuilder.append("$")
                }
            '{'  ->
                when
                {
                    escaped    ->
                    {
                        escaped = false
                        stringBuilder.append("\\{")
                    }
                    start >= 0 ->
                    {
                        start = index + 1
                        curly = true
                    }
                    else       -> stringBuilder.append("{")
                }
            '}'  ->
                when
                {
                    escaped ->
                    {
                        escaped = false
                        stringBuilder.append("\\}")
                    }
                    curly   ->
                    {
                        curly = false
                        stringBuilder.append("\\(")
                        stringBuilder.append(string.substring(start, index))
                        stringBuilder.append(")")
                        start = -1
                    }
                    else    -> stringBuilder.append("}")
                }
            else ->
                when
                {
                    escaped   ->
                    {
                        escaped = false
                        stringBuilder.append("\\")
                        stringBuilder.append(character)
                    }
                    start > 0 ->
                        if (!curly && character <= ' ')
                        {
                            if (index > start)
                            {
                                stringBuilder.append("\\(")
                                stringBuilder.append(string.substring(start, index))
                                stringBuilder.append(")")
                                stringBuilder.append(character)
                            }
                            else
                            {
                                stringBuilder.append("$")
                                stringBuilder.append(character)
                            }

                            start = -1
                        }
                    else      -> stringBuilder.append(character)
                }
        }

        index++
    }

    return stringBuilder.toString()
}
