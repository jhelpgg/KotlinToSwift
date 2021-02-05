package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.test.tools.assertEqualsIgnoreWhiteSpaceNumber
import org.junit.jupiter.api.Test


class TestCasePostTreatmentTests
{
    @Test
    fun testEquals()
    {
        val kotlinVersion =
            """
                @TestCaseClass
                
                class SomethingTests 
                {
                    @Test
                    fun callMethod()
                    {
                        Assert.assertEquals("message", 4, 2+2)
                        Assert.assertEquals(4, 2*2)
                    }
                }
            """.trimIndent()
        val swiftVersion =
            """
                import XCTest

                class SomethingTests : XCTestCase
                {
                    fun test_callMethod()
                    {
                        XCTAssertEqual(4, 2+2, "message")
                        XCTAssertEqual(4, 2*2, "Objects are not equals")
                    }
                }
            """.trimIndent()
        assertEqualsIgnoreWhiteSpaceNumber(swiftVersion, parseTestFile(kotlinVersion))
    }

    @Test
    fun testBeforeAfter()
    {
        val kotlinVersion =
            """
                @TestCaseClass
                
                class SomethingTests 
                {
                    @Before
                    fun initialize()
                    {
                        Something.initialize()
                    }
                    
                    @After
                    fun freeMemory()
                    {
                        Something.freeMemory()
                    }
                
                    @Test
                    fun callMethod()
                    {
                        Assert.assertEquals("message", 4, 2+2)
                        Assert.assertEquals(4, 2*2)
                    }
                }
            """.trimIndent()
        val swiftVersion =
            """
                import XCTest

                class SomethingTests : XCTestCase
                {
                    override fun setUp()
                    {
                        super.setUp()
                        Something.initialize()
                    }
                
                    override fun tearDown()
                    { 
                         Something.freeMemory()
                         super.tearDown()
                    }
                
                    fun test_callMethod()
                    {
                        XCTAssertEqual(4, 2+2, "message")
                        XCTAssertEqual(4, 2*2, "Objects are not equals")
                    }
                }
            """.trimIndent()
        assertEqualsIgnoreWhiteSpaceNumber(swiftVersion, parseTestFile(kotlinVersion))
    }
}