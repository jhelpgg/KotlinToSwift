package fr.jhelp.kotlinToSwift.test.tools

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ComparisionToolsTests
{
    @Test
    fun test()
    {
        Assertions.assertEquals("", compareEqualsIgnoreWhiteSpaceNumber(
            """
                Hello world.
                This is a great
                              and
                nice                     day.
            """.trimIndent(),
            """
                Hello world. This is a 
                great                              and               nice             day.
            """.trimIndent()
        ))

        Assertions.assertEquals("", compareEqualsIgnoreWhiteSpaceNumber(
            """
                Hello world.
                This is a great
                              and
                nice                     day.
            """.trimIndent(),
            "Hello world. This is a great and nice day."
        ))


        var actual = """
                Hello world.
                This is a great
                              and
                nice                     day.
            """.trimIndent()
        var expected = "Hello world. This is a great and nice"
        Assertions.assertEquals("'$actual' not equivalent to '$expected'. Actual index 51, expected consumed",
                                compareEqualsIgnoreWhiteSpaceNumber(
                                    expected,
                                    actual
                                ))

        actual = """
                Hello world.
                This is a great
                              and
                nice                     
            """.trimIndent()
        expected = "Hello world. This is a great and nice day."
        Assertions.assertEquals("'$actual' not equivalent to '$expected'. Actual consumed, expected index 37",
                                compareEqualsIgnoreWhiteSpaceNumber(
                                    expected,
                                    actual
                                ))

        actual = """
                Hello world.
                This is a great
                              and
                nice                     day.
            """.trimIndent()
        expected = "Hello world.This is a great and nice day."
        Assertions.assertEquals("'$actual' not equivalent to '$expected'. Actual index 12, expected index 12",
                                compareEqualsIgnoreWhiteSpaceNumber(
                                    expected,
                                    actual
                                ))

        actual = """
                Hello world.
                This is a great
                              and
                nice                     day.
            """.trimIndent()
        expected = "Hello world. This is a nice day."
        Assertions.assertEquals("'$actual' not equivalent to '$expected'. Actual index 23, expected index 23",
                                compareEqualsIgnoreWhiteSpaceNumber(
                                    expected,
                                    actual
                                ))
    }
}