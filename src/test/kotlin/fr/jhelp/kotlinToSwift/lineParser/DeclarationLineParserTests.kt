package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.test.tools.assertEqualsIgnoreWhiteSpaceNumber
import org.junit.jupiter.api.Test

class DeclarationLineParserTests
{
    @Test
    fun parseDeclaration()
    {
        val declarationLineParser = DeclarationLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("var x = 42",
                                           declarationLineParser.parse("var x = 42"))
        assertEqualsIgnoreWhiteSpaceNumber("let x = 42",
                                           declarationLineParser.parse("val x = 42"))
        assertEqualsIgnoreWhiteSpaceNumber("private var x = 42",
                                           declarationLineParser.parse("private var x = 42"))
        assertEqualsIgnoreWhiteSpaceNumber("private var x : Int = 42",
                                           declarationLineParser.parse("private var x : Int = 42"))
    }
}