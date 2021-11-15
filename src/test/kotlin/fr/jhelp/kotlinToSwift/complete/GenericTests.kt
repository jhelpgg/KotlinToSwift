package fr.jhelp.kotlinToSwift.complete

import fr.jhelp.kotlinToSwift.test.tools.assertTransformed
import org.junit.jupiter.api.Test

class GenericTests
{
    @Test
    fun extendGenericTest()
    {
        val kotlwiftSource =
            """
                class ActionUnwrapSuccess<Result> : ActionTransformer<FutureResult<Result>, Result> {
                }
            """.trimIndent()

        val swiftExpected =
            """
                public class ActionUnwrapSuccess<Result> : ActionTransformer<FutureResult<Result>, Result> {
                }
            """.trimIndent()
        assertTransformed(kotlwiftSource, swiftExpected)
    }

    @Test
    fun extendGenericTest2()
    {
        val kotlwiftSource =
            """
package fr.feetme.sdk.tasks.future.actions

import fr.feetme.sdk.actions.ActionTransformer
import fr.feetme.sdk.tasks.future.FutureResult
import fr.jhelp.kotlinLight.Override

internal class ActionUnwrapSuccess<Result> : ActionTransformer<FutureResult<Result>, Result> {
    @Override
    constructor() : super() {
    }

    override fun transform(parameter: FutureResult<Result>): Result {
        return parameter.result!!.result!!
    }
}
            """.trimIndent()

        val swiftExpected =
            """
internal class ActionUnwrapSuccess<Result> : ActionTransformer<FutureResult<Result>, Result> {
    override public init() {
        super.init() 
    }

    public override func transform(_ parameter: FutureResult<Result>) -> Result {
        return parameter.result!.result!
    }
}
            """.trimIndent()
        assertTransformed(kotlwiftSource, swiftExpected)
    }
}