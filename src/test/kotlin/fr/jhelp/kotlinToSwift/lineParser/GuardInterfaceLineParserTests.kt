package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.test.tools.assertEqualsIgnoreWhiteSpaceNumber
import org.junit.jupiter.api.Test

class GuardInterfaceLineParserTests
{
    @Test
    fun parseGuard()
    {
        val guardInterfaceLineParser = GuardInterfaceLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("guard (age>=0) else  { throw IllegalArgumentException(\"Age must be positive\") }",
                                           guardInterfaceLineParser.parse("(age>=0).guard { throw IllegalArgumentException(\"Age must be positive\") }"))
    }
}