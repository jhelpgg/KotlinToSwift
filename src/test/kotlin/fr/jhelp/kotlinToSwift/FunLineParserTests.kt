package fr.jhelp.kotlinToSwift

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FunLineParserTests
{
    @Test
    fun parseFunctionDeclaration()
    {
        val funLineParser = FunLineParser()
        Assertions.assertEquals("public func test()",
                                funLineParser.parse("public fun test()"))
        Assertions.assertEquals("public func toString() -> String",
                                funLineParser.parse("override public fun toString() : String"))
        Assertions.assertEquals("override public func test(_ age:Int, _ name:String) -> Boolean",
                                funLineParser.parse("override public fun test(age:Int, name:String) : Boolean"))
        Assertions.assertEquals("private func mayFail() throws ",
                                funLineParser.parse("@Throws private fun mayFail()"))
        Assertions.assertEquals("open public func test(_ task : @escaping  ()->Int)",
                                funLineParser.parse("open public fun test(task : ()->Int)"))
        Assertions.assertEquals("func next() -> Element<T>?",
                                funLineParser.parse("fun next(): Element<T>?"))
    }
}