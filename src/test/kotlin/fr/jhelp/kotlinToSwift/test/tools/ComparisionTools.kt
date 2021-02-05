package fr.jhelp.kotlinToSwift.test.tools

fun compareEqualsIgnoreWhiteSpaceNumber(expected: String, actual: String): String
{
    val expectedCharacters = expected.trim().toCharArray()
    val expectedLength = expectedCharacters.size
    val actualCharacters = actual.trim().toCharArray()
    val actualLength = actualCharacters.size
    var indexExpected = 0
    var indexActual = 0
    var expectedChar: Char
    var actualChar: Char

    while (indexExpected < expectedLength && indexActual < actualLength)
    {
        expectedChar = expectedCharacters[indexExpected]
        actualChar = actualCharacters[indexActual]

        if (expectedChar <= ' ')
        {
            if (actualChar > ' ')
            {
                return "$actual not equivalent to $expected. Actual index $indexActual, expected index $indexExpected"
            }
            else
            {
                do
                {
                    indexExpected++
                }
                while (indexExpected < expectedLength && expectedCharacters[indexExpected] <= ' ')

                do
                {
                    indexActual++
                }
                while (indexActual < actualLength && actualCharacters[indexActual] <= ' ')
            }
        }
        else if (expectedChar != actualChar)
        {
            return "'$actual' not equivalent to '$expected'. Actual index $indexActual, expected index $indexExpected"
        }
        else
        {
            indexActual++
            indexExpected++
        }
    }

    if (indexExpected < expectedLength)
    {
        return "'$actual' not equivalent to '$expected'. Actual consumed, expected index $indexExpected"
    }

    if (indexActual < actualLength)
    {
        return "'$actual' not equivalent to '$expected'. Actual index $indexActual, expected consumed"
    }

    return ""
}