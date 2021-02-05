package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.test.tools.assertEqualsIgnoreWhiteSpaceNumber
import org.junit.jupiter.api.Test

class CatchLineParserTests
{
    @Test
    fun parseCatch()
    {
        val catchLineParser = CatchLineParser()
        assertEqualsIgnoreWhiteSpaceNumber("} catch {",
                                           catchLineParser.parse("} catch(error:Exception) {"))
        assertEqualsIgnoreWhiteSpaceNumber("catch {",
                                           catchLineParser.parse("catch(error:Exception) {"))
        assertEqualsIgnoreWhiteSpaceNumber("} catch",
                                           catchLineParser.parse("} catch(error:Exception)"))
        assertEqualsIgnoreWhiteSpaceNumber("catch",
                                           catchLineParser.parse("catch(error:Exception)"))
    }
}