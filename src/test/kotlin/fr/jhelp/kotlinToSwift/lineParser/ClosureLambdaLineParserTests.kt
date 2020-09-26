package fr.jhelp.kotlinToSwift.lineParser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ClosureLambdaLineParserTests
{
    @Test
    fun parseLambda()
    {
        val closureLambdaLineParser = ClosureLambdaLineParser()
        Assertions.assertEquals("{ x in x+1 }",
                                closureLambdaLineParser.parse("{ x -> x+1 }"))
        Assertions.assertEquals("val f = { x in x+1 }",
                                closureLambdaLineParser.parse("val f = { x -> x+1 }"))
        Assertions.assertEquals("{ (x, y) in x + 2*y }",
                                closureLambdaLineParser.parse("{ (x, y) -> x + 2*y }"))
        Assertions.assertEquals("{ (x, y, z) in ",
                                closureLambdaLineParser.parse("{ (x, y, z) ->"))
    }
}