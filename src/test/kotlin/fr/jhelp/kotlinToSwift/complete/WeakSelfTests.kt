package fr.jhelp.kotlinToSwift.complete

import fr.jhelp.kotlinToSwift.test.tools.DOLLAR
import fr.jhelp.kotlinToSwift.test.tools.assertExact
import fr.jhelp.kotlinToSwift.test.tools.assertTransformed
import org.junit.jupiter.api.Test

class WeakSelfTests
{
    @Test
    fun withoutParameterNeitherReturnValue()
    {
        val kotlwiftSource =
            """
                class Test
                {
                    val value = 73
                
                    fun test()
                    {
                        method {
                            @WeakSelf("") val sSelf = this
                            println("$DOLLAR{sSelf.value}")
                        }
                    }
                }
            """.trimIndent()

        val swiftExpected =
            """
                public class Test
                {
                    let value = 73
                    
                    public func test()
                    {
                        method { [weak self] in
                            guard let sSelf = self else { return }
                            print("\(sSelf.value)")
                        }
                    }
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }

    @Test
    fun withParameterNoReturnValue()
    {
        val kotlwiftSource =
            """
                class Test
                {
                    val value = 73
                
                    fun test()
                    {
                        method { parameter ->
                            @WeakSelf("") val sSelf = this
                            println("$DOLLAR{sSelf.value}")
                        }
                    }
                }
            """.trimIndent()

        val swiftExpected =
            """
                public class Test
                {
                    let value = 73
                    
                    public func test()
                    {
                        method { [weak self] parameter in
                            guard let sSelf = self else { return }
                            print("\(sSelf.value)")
                        }
                    }
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }

    @Test
    fun withoutParameterButWithReturnValue()
    {
        val kotlwiftSource =
            """
                class Test
                {
                    val value = 73
                
                    fun test()
                    {
                        method {
                            @WeakSelf("0") val sSelf = this
                            return@method sSelf.value
                        }
                    }
                }
            """.trimIndent()

        val swiftExpected =
            """
                public class Test
                {
                    let value = 73
                    
                    public func test()
                    {
                        method { [weak self] in
                            guard let sSelf = self else { return 0 }
                            return sSelf.value
                        }
                    }
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }

    @Test
    fun withoutParameterAndReturnValue()
    {
        val kotlwiftSource =
            """
                class Test
                {
                    val value = 73
                
                    fun test()
                    {
                        method { parameter ->
                            @WeakSelf("0") val sSelf = this
                            return@method sSelf.value + parameter
                        }
                    }
                }
            """.trimIndent()

        val swiftExpected =
            """
                public class Test
                {
                    let value = 73
                    
                    public func test()
                    {
                        method { [weak self] parameter in
                            guard let sSelf = self else { return 0 }
                            return sSelf.value + parameter
                        }
                    }
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }

    @Test
    fun withoutParameterAndReturnString()
    {
        val kotlwiftSource =
            """
                class Test
                {
                    val value = 73
                
                    fun test()
                    {
                        method { parameter ->
                            @WeakSelf("\"\"") val sSelf = this
                            val result = sSelf.value + parameter
                            return@method "result = $DOLLAR{result}"
                        }
                    }
                }
            """.trimIndent()

        val swiftExpected =
            """
                public class Test
                {
                    let value = 73
                    
                    public func test()
                    {
                        method { [weak self] parameter in
                            guard let sSelf = self else { return "" }
                            let result = sSelf.value + parameter
                            return "result = \(result)"
                        }
                    }
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }

    @Test
    fun noWeakSelfRequired()
    {
        val kotlwiftSource =
            """
                class Test
                {
                    fun test()
                    {
                        method { parameter ->
                            return@method "parameter = $DOLLAR{parameter}"
                        }
                    }
                }
            """.trimIndent()

        val swiftExpected =
            """
                public class Test
                {
                    public func test()
                    {
                        method { parameter in 
                            return "parameter = \(parameter)"
                        }
                    }
                }
                
            """.trimIndent()

        assertExact(kotlwiftSource, swiftExpected)
    }
}
