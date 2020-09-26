package fr.jhelp.kotlinToSwift.lineParser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GuardInterfaceLineParserTests
{
    @Test
    fun parseGuard()
    {
        val guardInterfaceLineParser = GuardInterfaceLineParser()
        Assertions.assertEquals("guard (age>=0) else  { throw CommonManagedExceptions.IllegalArgumentException(\"Age must be positive\") }",
                                guardInterfaceLineParser.parse("(age>=0).guard { throw IllegalArgumentException(\"Age must be positive\") }"))
    }
}