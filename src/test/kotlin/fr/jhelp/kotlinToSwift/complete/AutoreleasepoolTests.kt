package fr.jhelp.kotlinToSwift.complete

import fr.jhelp.kotlinToSwift.DISABLE_AUTOMATIC_PUBLIC
import fr.jhelp.kotlinToSwift.KotlinToSwiftOptions
import fr.jhelp.kotlinToSwift.test.tools.assertTransformed
import org.junit.jupiter.api.Test

class AutoreleasepoolTests
{
    @Test
    fun autoreleasepool()
    {
        val kotlwiftSource =
            """
                fun test()
                {
                    autoreleasepool {
                        // ...
                    }
                }
            """.trimIndent()

        val swiftExpected =
            """
                public func test()
                {
                    autoreleasepool {
                        // ...
                    }
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }
}