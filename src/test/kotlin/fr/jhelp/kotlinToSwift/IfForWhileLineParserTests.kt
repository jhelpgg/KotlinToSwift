package fr.jhelp.kotlinToSwift

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IfForWhileLineParserTests
{
    @Test
    fun parseIf()
    {
        val ifForWhileLineParser = IfForWhileLineParser()
        Assertions.assertEquals("if person.age<5",
                                ifForWhileLineParser.parse("if(person.age<5)"))
        Assertions.assertEquals("if person.age < 5  {",
                                ifForWhileLineParser.parse("if(person.age < 5) {"))
        Assertions.assertEquals("if person.age > 18 && person.age < 30  {",
                                ifForWhileLineParser.parse("if(person.age > 18 && person.age < 30) {"))
    }

    @Test
    fun parseWhile()
    {
        val ifForWhileLineParser = IfForWhileLineParser()
        Assertions.assertEquals("while count < 10",
                                ifForWhileLineParser.parse("while(count < 10)"))
        Assertions.assertEquals("while count < 10  {",
                                ifForWhileLineParser.parse("while(count < 10) {"))
    }

    @Test
    fun parseFor()
    {
        val ifForWhileLineParser = IfForWhileLineParser()
        Assertions.assertEquals("for element in list",
                                ifForWhileLineParser.parse("for(element in list)"))
        Assertions.assertEquals("for element in list  {",
                                ifForWhileLineParser.parse("for(element in list) {"))
    }
}