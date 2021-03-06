package fr.jhelp.kotlinToSwift.lineParser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ClassInterfaceLineParserTests
{
    @Test
    fun parseClass()
    {
        val classInterfaceLineParser = ClassInterfaceLineParser()
        Assertions.assertEquals("public class Person",
                                classInterfaceLineParser.parse("public class Person"))

        Assertions.assertEquals("internal class Car : Engine",
                                classInterfaceLineParser.parse("internal class Car : Engine"))

        Assertions.assertEquals("internal class Car : Engine, Comparable {",
                                classInterfaceLineParser.parse("internal class Car : Engine, Comparable {"))

        Assertions.assertEquals("internal class Map<R> {",
                                classInterfaceLineParser.parse("internal class Map<R> {"))
    }

    @Test
    fun parseInterface()
    {
        val classInterfaceLineParser = ClassInterfaceLineParser()
        Assertions.assertEquals("protocol Person",
                                classInterfaceLineParser.parse("interface Person"))

        Assertions.assertEquals("public protocol Person",
                                classInterfaceLineParser.parse("public interface Person"))

        Assertions.assertEquals("internal protocol Car : Engine",
                                classInterfaceLineParser.parse("internal interface Car : Engine"))

        Assertions.assertEquals("internal protocol Car : Engine, Comparable {",
                                classInterfaceLineParser.parse("internal interface Car : Engine, Comparable {"))

        Assertions.assertEquals("internal protocol Map<R> {",
                                classInterfaceLineParser.parse("internal interface Map<R> {"))
    }
}