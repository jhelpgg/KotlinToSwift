package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.test.tools.assertEqualsIgnoreWhiteSpaceNumber
import org.junit.jupiter.api.Test

class ClosureLambdaLineParserTests
{
    @Test
    fun parseLambda()
    {
        val closureLambdaLineParser = ClosureLambdaLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("{ x in x+1 }",
                                           closureLambdaLineParser.parse("{ x -> x+1 }"))
        assertEqualsIgnoreWhiteSpaceNumber("val f = { x in x+1 }",
                                           closureLambdaLineParser.parse("val f = { x -> x+1 }"))
        assertEqualsIgnoreWhiteSpaceNumber("{ (x, y) in x + 2*y }",
                                           closureLambdaLineParser.parse("{ (x, y) -> x + 2*y }"))
        assertEqualsIgnoreWhiteSpaceNumber("{ (x, y, z) in ",
                                           closureLambdaLineParser.parse("{ (x, y, z) ->"))
    }
}