package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.test.tools.assertEqualsIgnoreWhiteSpaceNumber
import org.junit.jupiter.api.Test

class IfForWhileLineParserTests
{
    @Test
    fun parseIf()
    {
        val ifForWhileLineParser = IfForWhileLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("if person.age<5",
                                           ifForWhileLineParser.parse("if(person.age<5)"))
        assertEqualsIgnoreWhiteSpaceNumber("if person.age < 5  {",
                                           ifForWhileLineParser.parse("if(person.age < 5) {"))
        assertEqualsIgnoreWhiteSpaceNumber("if person.age > 18 && person.age < 30  {",
                                           ifForWhileLineParser.parse("if(person.age > 18 && person.age < 30) {"))
    }

    @Test
    fun parseWhile()
    {
        val ifForWhileLineParser = IfForWhileLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("while count < 10",
                                           ifForWhileLineParser.parse("while(count < 10)"))
        assertEqualsIgnoreWhiteSpaceNumber("while count < 10  {",
                                           ifForWhileLineParser.parse("while(count < 10) {"))
    }

    @Test
    fun parseFor()
    {
        val ifForWhileLineParser = IfForWhileLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("for element in list",
                                           ifForWhileLineParser.parse("for(element in list)"))
        assertEqualsIgnoreWhiteSpaceNumber("for element in list  {",
                                           ifForWhileLineParser.parse("for(element in list) {"))
    }
}