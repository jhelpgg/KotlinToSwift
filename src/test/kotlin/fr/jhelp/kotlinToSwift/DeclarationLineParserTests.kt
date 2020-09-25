package fr.jhelp.kotlinToSwift

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DeclarationLineParserTests
{
    @Test
    fun parseDeclaration()
    {
        val declarationLineParser = DeclarationLineParser()
        Assertions.assertEquals("var x = 42",
                                declarationLineParser.parse("var x = 42"))
        Assertions.assertEquals("let x = 42",
                                declarationLineParser.parse("val x = 42"))
        Assertions.assertEquals("private var x = 42",
                                declarationLineParser.parse("private var x = 42"))
        Assertions.assertEquals("private var x : Int = 42",
                                declarationLineParser.parse("private var x : Int = 42"))
    }
}