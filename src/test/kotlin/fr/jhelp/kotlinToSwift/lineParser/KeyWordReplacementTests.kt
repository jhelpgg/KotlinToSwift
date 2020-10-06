package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.keyWordReplacement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KeyWordReplacementTests
{
    @Test
    fun itReplacement()
    {
        Assertions.assertEquals("index = list.indexOfFirst { $0.length > 5 }",
                                keyWordReplacement("index = list.indexOfFirst { it.length > 5 }") )
    }

    @Test
    fun unitReplacement()
    {
        Assertions.assertEquals("Void unit. Unity unity. The Void. Computer Void",
                                keyWordReplacement("Unit unit. Unity unity. The Unit. Computer Unit"))
    }

    @Test
    fun longFloatReplacement()
    {
        Assertions.assertEquals("a = Long(0)",
                                keyWordReplacement("a = 0L"))
        Assertions.assertEquals("a = Long(0) + Long(5)",
                                keyWordReplacement("a = 0L + 5l"))
        Assertions.assertEquals("a = Long(0) + Long(5) * Float(3.14)",
                                keyWordReplacement("a = 0L + 5l * 3.14F"))
    }
}