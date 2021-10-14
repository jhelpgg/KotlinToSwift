package fr.jhelp.kotlinToSwift.complete

import fr.jhelp.kotlinToSwift.DISABLE_AUTOMATIC_PUBLIC
import fr.jhelp.kotlinToSwift.KotlinToSwiftOptions
import fr.jhelp.kotlinToSwift.test.tools.assertTransformed
import org.junit.jupiter.api.Test

class EscapingTests
{
    @Test
    fun withoutEscaping()
    {
        val kotlwiftSource =
            """
                fun test(f : () -> Unit)
                {
                    // ...
                }
            """.trimIndent()

        val swiftExpected =
            """
                public func test(_ f : () -> Void)
                {
                    // ...
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }

    @Test
    fun withEscaping()
    {
        val kotlwiftSource =
            """
                fun test(@Escaping f : () -> Unit)
                {
                    // ...
                }
            """.trimIndent()

        val swiftExpected =
            """
                public func test(_ f : @escaping () -> Void)
                {
                    // ...
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }
}