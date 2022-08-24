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

    @Test
    fun multilClosureAndWeakSelf()
    {
          val kotlwiftSource =
            """
                fun isExerciseRehabilitation(exciseKey: String, @Escaping responseHandler: (Boolean) -> Unit) {
                    this.document.obtainValue(
                        { serverMap ->
                            @WeakSelf("") val sSelf = this
                            sSelf.isExerciseRehabilitationSucceed(serverMap, exciseKey, responseHandler)
                        },
                        { message ->
                            @WeakSelf("") val sSelf = this
                            sSelf.isExerciseRehabilitationFailed(message, responseHandler)
                        })
                }
            """.trimIndent()

        val swiftExpected =
            """
                public func isExerciseRehabilitation(_ exciseKey: String, _ responseHandler: @escaping (Bool) -> Void) {
                    self.document.obtainValue(
                        { [weak self] serverMap in
                            guard let sSelf = self else { return }
                            sSelf.isExerciseRehabilitationSucceed(serverMap, exciseKey, responseHandler)
                        },
                        { [weak self] message in
                            guard let sSelf = self else { return }
                            sSelf.isExerciseRehabilitationFailed(message, responseHandler)
                        })
                }
            """.trimIndent()

        assertTransformed(kotlwiftSource, swiftExpected)
    }
}