package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.test.tools.assertEqualsIgnoreWhiteSpaceNumber
import org.junit.jupiter.api.Test

class AnnotationLineParserTests
{
    @Test
    fun parseOverride()
    {
        val annotationLineParser = AnnotationLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("override constructor()",
                                           annotationLineParser.parse("@Override constructor()"))
    }

    @Test
    fun parseTry()
    {
        val annotationLineParser = AnnotationLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("try someThingMayFail()",
                                           annotationLineParser.parse("@Try someThingMayFail()"))

        assertEqualsIgnoreWhiteSpaceNumber("var x = try someThingMayFail()",
                                           annotationLineParser.parse("@Try var x = someThingMayFail()"))

        assertEqualsIgnoreWhiteSpaceNumber("x = try someThingMayFail()",
                                           annotationLineParser.parse("@Try x = someThingMayFail()"))
    }
}