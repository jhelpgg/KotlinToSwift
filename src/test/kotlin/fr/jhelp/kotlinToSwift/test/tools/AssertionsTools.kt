package fr.jhelp.kotlinToSwift.test.tools

import org.junit.jupiter.api.Assertions

fun assertEqualsIgnoreWhiteSpaceNumber(expected: String, actual: String)
{
    val comparison = compareEqualsIgnoreWhiteSpaceNumber(expected, actual)

    if (comparison.isNotEmpty())
    {
        Assertions.fail<String>(comparison)
    }
}