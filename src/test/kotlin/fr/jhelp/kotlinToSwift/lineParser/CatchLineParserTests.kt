package fr.jhelp.kotlinToSwift.lineParser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CatchLineParserTests
{
    @Test
    fun parseCatch()
    {
        val catchLineParser = CatchLineParser()
        Assertions.assertEquals("} catch {",
                                catchLineParser.parse("} catch(error:Exception) {"))
        Assertions.assertEquals("catch {",
                                catchLineParser.parse("catch(error:Exception) {"))
        Assertions.assertEquals("} catch",
                                catchLineParser.parse("} catch(error:Exception)"))
        Assertions.assertEquals("catch",
                                catchLineParser.parse("catch(error:Exception)"))
    }
}