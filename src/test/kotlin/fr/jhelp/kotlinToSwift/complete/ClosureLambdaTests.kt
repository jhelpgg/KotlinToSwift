package fr.jhelp.kotlinToSwift.complete

import fr.jhelp.kotlinToSwift.test.tools.DOLLAR
import fr.jhelp.kotlinToSwift.test.tools.assertTransformed
import org.junit.jupiter.api.Test

class ClosureLambdaTests
{
    @Test
    fun multiClosure()
    {
        val kotlwiftSource =
            """
                class Test
                {
                    val value = 73
                
                    fun test()
                    {
                        method({
                            @WeakSelf("") val sSelf = this
                            println("$DOLLAR{sSelf.value}")
                        },
                        { error ->
                           println("$DOLLAR{error}")  
                        })
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
                        method({ [weak self] in
                            guard let sSelf = self else { return }
                            print("\(sSelf.value)")
                        },
                        {   error in
                             print("\(error)")
                        })
                    }
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }
}