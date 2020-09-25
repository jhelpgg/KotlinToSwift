package fr.jhelp.kotlinToSwift

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ConstructorLineParserTests
{
    @Test
    fun parseConstructor()
    {
        val constructorLineParser = ConstructorLineParser()
        Assertions.assertEquals("init(){",
                                constructorLineParser.parse("constructor()\n{"))
        Assertions.assertEquals("convenience override init(_ age:Int){\n     self.init(\"P\", age) ",
                                constructorLineParser.parse("@Override constructor(age:Int) : this(\"P\", age) {"))
        Assertions.assertEquals("init(_ age:Int) throws {\n     super.init(age) ",
                                constructorLineParser.parse("@Throws constructor(age:Int) : super(age) {"))
    }
}