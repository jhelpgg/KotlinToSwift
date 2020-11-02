package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.test.tools.assertEqualsIgnoreWhiteSpaceNumber
import org.junit.jupiter.api.Test

class ConstructorLineParserTests
{
    @Test
    fun parseConstructor()
    {
        val constructorLineParser = ConstructorLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("public init() {",
                                           constructorLineParser.parse("constructor()\n{"))
        assertEqualsIgnoreWhiteSpaceNumber("convenience override public init(_ age:Int) {\n     self.init(\"P\", age) ",
                                           constructorLineParser.parse("@Override constructor(age:Int) : this(\"P\", age) {"))
        assertEqualsIgnoreWhiteSpaceNumber("public init(_ age:Int) throws {\n     super.init(age) ",
                                           constructorLineParser.parse("@Throws constructor(age:Int) : super(age) {"))
    }
}