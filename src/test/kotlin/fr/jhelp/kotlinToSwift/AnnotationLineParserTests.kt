package fr.jhelp.kotlinToSwift

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AnnotationLineParserTests
{
    @Test
    fun parseOverride()
    {
        val annotationLineParser = AnnotationLineParser()
        Assertions.assertEquals("override constructor()",
                                annotationLineParser.parse("@Override constructor()"))
    }

    @Test
    fun parseTry()
    {
        val annotationLineParser = AnnotationLineParser()
        Assertions.assertEquals("try someThingMayFail()",
                                annotationLineParser.parse("@Try someThingMayFail()"))

        Assertions.assertEquals("var x= try someThingMayFail()",
                                annotationLineParser.parse("@Try var x=someThingMayFail()"))

        Assertions.assertEquals("x= try someThingMayFail()",
                                annotationLineParser.parse("@Try x=someThingMayFail()"))
    }
}