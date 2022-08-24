package fr.jhelp.kotlinToSwift.protocol

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ExtendedGenericsIteratorTests
{
    @Test
    fun oneExtendWithoutGenerics()
    {
        val extendedGenericsIterator = ExtendedGenericsIterator("Runnable")
        Assertions.assertTrue(extendedGenericsIterator.hasNext())
        val (extended, generics) = extendedGenericsIterator.next()
        Assertions.assertEquals("Runnable", extended)
        Assertions.assertNull(generics)
        Assertions.assertFalse(extendedGenericsIterator.hasNext())
    }

    @Test
    fun twoExtendsWithoutGenerics()
    {
        val extendedGenericsIterator = ExtendedGenericsIterator("Runnable, Something")

        Assertions.assertTrue(extendedGenericsIterator.hasNext())
        val (extended, generics) = extendedGenericsIterator.next()
        Assertions.assertEquals("Runnable", extended)
        Assertions.assertNull(generics)

        Assertions.assertTrue(extendedGenericsIterator.hasNext())
        val (extended2, generics2) = extendedGenericsIterator.next()
        Assertions.assertEquals("Something", extended2)
        Assertions.assertNull(generics2)

        Assertions.assertFalse(extendedGenericsIterator.hasNext())
    }

    @Test
    fun oneWithGenerics()
    {
        val extendedGenericsIterator = ExtendedGenericsIterator("Comparable<String>")
        Assertions.assertTrue(extendedGenericsIterator.hasNext())
        val (extended, generics) = extendedGenericsIterator.next()
        Assertions.assertEquals("Comparable", extended)
        Assertions.assertEquals("<String>",generics)
        Assertions.assertFalse(extendedGenericsIterator.hasNext())
    }

    @Test
    fun twoWithGenerics()
    {
        val extendedGenericsIterator = ExtendedGenericsIterator("Comparable<String>, ActionTransformer<Int, Double>")

        Assertions.assertTrue(extendedGenericsIterator.hasNext())
        val (extended, generics) = extendedGenericsIterator.next()
        Assertions.assertEquals("Comparable", extended)
        Assertions.assertEquals("<String>",generics)

        Assertions.assertTrue(extendedGenericsIterator.hasNext())
        val (extended2, generics2) = extendedGenericsIterator.next()
        Assertions.assertEquals("ActionTransformer", extended2)
        Assertions.assertEquals("<Int, Double>",generics2)

        Assertions.assertFalse(extendedGenericsIterator.hasNext())
    }

    @Test
    fun realCase1()
    {
        val extendedGenericsIterator = ExtendedGenericsIterator("ActionTransformer<FutureResult<Result>, Result>")
        Assertions.assertTrue(extendedGenericsIterator.hasNext())
        val (extended, generics) = extendedGenericsIterator.next()
        Assertions.assertEquals("ActionTransformer", extended)
        Assertions.assertEquals("<FutureResult<Result>, Result>",generics)
        Assertions.assertFalse(extendedGenericsIterator.hasNext())
    }
}